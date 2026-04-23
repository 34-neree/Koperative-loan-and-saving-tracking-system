package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.RepaymentRequest;
import rw.koperative.exception.ResourceNotFoundException;
import rw.koperative.model.LoanRepayment;
import rw.koperative.model.enums.RepaymentStatus;
import rw.koperative.repository.LoanRepaymentRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class LoanRepaymentService {

    private final LoanRepaymentRepository repaymentRepository;

    public LoanRepaymentService(LoanRepaymentRepository repaymentRepository) {
        this.repaymentRepository = repaymentRepository;
    }

    public List<LoanRepayment> getSchedule(Long loanId) {
        return repaymentRepository.findByLoanIdOrderByDueDateAsc(loanId);
    }

    public LoanRepayment recordPayment(RepaymentRequest request) {
        LoanRepayment repayment = repaymentRepository.findById(request.getRepaymentId())
                .orElseThrow(() -> new ResourceNotFoundException("Repayment not found"));
        BigDecimal newPaid = repayment.getAmountPaid().add(request.getAmountPaid());
        repayment.setAmountPaid(newPaid);
        repayment.setPaidDate(LocalDateTime.now());
        repayment.setStatus(newPaid.compareTo(repayment.getAmountDue()) >= 0 ? RepaymentStatus.PAID : RepaymentStatus.PARTIAL);
        return repaymentRepository.save(repayment);
    }

    public List<LoanRepayment> getOverdueRepayments() {
        return repaymentRepository.findByStatusAndDueDateBefore(RepaymentStatus.PENDING, LocalDate.now());
    }
}
