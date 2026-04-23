package rw.koperative.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rw.koperative.dto.DashboardStats;
import rw.koperative.model.Savings;
import rw.koperative.model.Loan;
import rw.koperative.model.enums.FineStatus;
import rw.koperative.model.enums.FinancialType;
import rw.koperative.model.enums.LoanStatus;
import rw.koperative.model.enums.RepaymentStatus;
import rw.koperative.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final MemberRepository memberRepository;
    private final SavingsRepository savingsRepository;
    private final LoanRepository loanRepository;
    private final LoanRepaymentRepository repaymentRepository;
    private final FineRepository fineRepository;
    private final TransactionRepository transactionRepository;

    public DashboardController(MemberRepository memberRepository, SavingsRepository savingsRepository,
                               LoanRepository loanRepository, LoanRepaymentRepository repaymentRepository,
                               FineRepository fineRepository, TransactionRepository transactionRepository) {
        this.memberRepository = memberRepository;
        this.savingsRepository = savingsRepository;
        this.loanRepository = loanRepository;
        this.repaymentRepository = repaymentRepository;
        this.fineRepository = fineRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public ResponseEntity<DashboardStats> getStats() {
        long totalMembers = memberRepository.count();
        long pendingLoans = loanRepository.findByStatusIn(
                Arrays.asList(LoanStatus.APPLIED, LoanStatus.UNDER_REVIEW)).size();
        long overdueRepayments = repaymentRepository
                .findByStatusAndDueDateBefore(RepaymentStatus.PENDING, LocalDate.now()).size();

        BigDecimal totalUnpaidFines = fineRepository.findByStatus(FineStatus.UNPAID).stream()
                .map(f -> f.getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIncome = transactionRepository.findByTypeOrderByTransactionDateDesc(FinancialType.INCOME)
                .stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactionRepository.findByTypeOrderByTransactionDateDesc(FinancialType.EXPENSE)
                .stream().map(t -> t.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate real total savings (all deposits - all withdrawals)
        BigDecimal totalSavings = savingsRepository.findAll().stream()
                .map(s -> "DEPOSIT".equals(s.getTransactionType()) ? s.getAmount() : s.getAmount().negate())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Calculate total loans disbursed
        BigDecimal totalLoansDisbursed = loanRepository.findByStatusIn(
                Arrays.asList(LoanStatus.DISBURSED, LoanStatus.CLOSED)).stream()
                .map(l -> l.getAmountApproved() != null ? l.getAmountApproved() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        DashboardStats stats = DashboardStats.builder()
                .totalMembers(totalMembers)
                .totalSavings(totalSavings)
                .totalLoansDisbursed(totalLoansDisbursed)
                .pendingLoanApprovals(pendingLoans)
                .overdueRepayments(overdueRepayments)
                .totalUnpaidFines(totalUnpaidFines)
                .totalIncome(totalIncome)
                .totalExpenses(totalExpenses)
                .netBalance(totalIncome.subtract(totalExpenses))
                .build();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/monthly-chart")
    public ResponseEntity<List<Map<String, Object>>> getMonthlyChart() {
        List<Map<String, Object>> result = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 5; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
            LocalDateTime start = monthStart.atStartOfDay();
            LocalDateTime end = monthEnd.atTime(23, 59, 59);

            // Sum savings deposits for this month
            BigDecimal savings = savingsRepository.findByDateRange(start, end).stream()
                    .filter(s -> "DEPOSIT".equals(s.getTransactionType()))
                    .map(Savings::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Sum loans disbursed this month
            BigDecimal loans = loanRepository.findAll().stream()
                    .filter(l -> l.getDisbursedDate() != null
                            && !l.getDisbursedDate().isBefore(start)
                            && !l.getDisbursedDate().isAfter(end))
                    .map(l -> l.getAmountApproved() != null ? l.getAmountApproved() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> point = new LinkedHashMap<>();
            point.put("month", monthStart.getMonth().toString().substring(0, 3));
            point.put("savings", savings);
            point.put("loans", loans);
            result.add(point);
        }

        return ResponseEntity.ok(result);
    }
}
