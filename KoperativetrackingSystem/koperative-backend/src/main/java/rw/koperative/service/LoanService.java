package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.LoanRequest;
import rw.koperative.exception.BadRequestException;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.Loan;
import rw.koperative.model.LoanRepayment;
import rw.koperative.model.Member;
import rw.koperative.model.enums.LoanStatus;
import rw.koperative.model.enums.RepaymentStatus;
import rw.koperative.repository.LoanRepository;
import rw.koperative.repository.LoanRepaymentRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class LoanService {

    private final LoanRepository loanRepository;
    private final LoanRepaymentRepository repaymentRepository;
    private final MemberService memberService;
    private final ShareService shareService;

    public LoanService(LoanRepository loanRepository, LoanRepaymentRepository repaymentRepository,
                       MemberService memberService, ShareService shareService) {
        this.loanRepository = loanRepository;
        this.repaymentRepository = repaymentRepository;
        this.memberService = memberService;
        this.shareService = shareService;
    }

    public Loan applyForLoan(LoanRequest request) {
        Member member = memberService.findMemberOrThrow(request.getMemberId());

        // Check no active loans in default
        boolean hasActiveLoan = loanRepository.existsByMemberIdAndStatusIn(
                member.getId(), Arrays.asList(LoanStatus.APPLIED, LoanStatus.UNDER_REVIEW, LoanStatus.APPROVED, LoanStatus.DISBURSED));
        if (hasActiveLoan) {
            throw new BadRequestException("Member already has an active loan");
        }

        Loan loan = Loan.builder()
                .member(member)
                .amountRequested(request.getAmountRequested())
                .interestRate(request.getInterestRate() != null ? request.getInterestRate() : new BigDecimal("10.00"))
                .termMonths(request.getTermMonths() != null ? request.getTermMonths() : 12)
                .purpose(request.getPurpose())
                .status(LoanStatus.APPLIED)
                .build();

        if (request.getGuarantor1Id() != null) {
            loan.setGuarantor1(memberService.findMemberOrThrow(request.getGuarantor1Id()));
        }
        if (request.getGuarantor2Id() != null) {
            loan.setGuarantor2(memberService.findMemberOrThrow(request.getGuarantor2Id()));
        }

        return loanRepository.save(loan);
    }

    public Loan reviewLoan(Long loanId) {
        Loan loan = findLoanOrThrow(loanId);
        validateTransition(loan, LoanStatus.UNDER_REVIEW);
        loan.setStatus(LoanStatus.UNDER_REVIEW);
        return loanRepository.save(loan);
    }

    public Loan approveLoan(Long loanId, BigDecimal approvedAmount) {
        Loan loan = findLoanOrThrow(loanId);
        validateTransition(loan, LoanStatus.APPROVED);
        loan.setStatus(LoanStatus.APPROVED);
        loan.setAmountApproved(approvedAmount != null ? approvedAmount : loan.getAmountRequested());
        loan.setApprovedDate(LocalDateTime.now());
        return loanRepository.save(loan);
    }

    public Loan rejectLoan(Long loanId) {
        Loan loan = findLoanOrThrow(loanId);
        loan.setStatus(LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }

    public Loan disburseLoan(Long loanId) {
        Loan loan = findLoanOrThrow(loanId);
        validateTransition(loan, LoanStatus.DISBURSED);
        loan.setStatus(LoanStatus.DISBURSED);
        loan.setDisbursedDate(LocalDateTime.now());
        Loan saved = loanRepository.save(loan);

        // Auto-generate repayment schedule
        generateRepaymentSchedule(saved);

        return saved;
    }

    private void generateRepaymentSchedule(Loan loan) {
        BigDecimal principal = loan.getAmountApproved();
        BigDecimal rate = loan.getInterestRate().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        BigDecimal totalInterest = principal.multiply(rate);
        BigDecimal totalPayable = principal.add(totalInterest);
        BigDecimal monthlyInstallment = totalPayable.divide(BigDecimal.valueOf(loan.getTermMonths()), 2, RoundingMode.HALF_UP);

        List<LoanRepayment> repayments = new ArrayList<>();
        LocalDate dueDate = LocalDate.now().plusMonths(1);

        for (int i = 0; i < loan.getTermMonths(); i++) {
            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .dueDate(dueDate.plusMonths(i))
                    .amountDue(monthlyInstallment)
                    .amountPaid(BigDecimal.ZERO)
                    .status(RepaymentStatus.PENDING)
                    .penaltyApplied(BigDecimal.ZERO)
                    .build();
            repayments.add(repayment);
        }
        repaymentRepository.saveAll(repayments);
    }

    public List<Loan> getMemberLoans(Long memberId) {
        return loanRepository.findByMemberIdOrderByAppliedDateDesc(memberId);
    }

    public List<Loan> getPendingLoans() {
        return loanRepository.findByStatusIn(Arrays.asList(LoanStatus.APPLIED, LoanStatus.UNDER_REVIEW));
    }

    public Loan findLoanOrThrow(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with ID: " + id));
    }

    private void validateTransition(Loan loan, LoanStatus target) {
        LoanStatus current = loan.getStatus();
        boolean valid = switch (target) {
            case UNDER_REVIEW -> current == LoanStatus.APPLIED;
            case APPROVED -> current == LoanStatus.UNDER_REVIEW;
            case DISBURSED -> current == LoanStatus.APPROVED;
            default -> false;
        };
        if (!valid) {
            throw new BadRequestException("Invalid transition from " + current + " to " + target);
        }
    }
}
