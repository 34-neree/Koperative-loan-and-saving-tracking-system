package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.LoanRepayment;
import rw.koperative.model.enums.RepaymentStatus;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LoanRepaymentRepository extends JpaRepository<LoanRepayment, Long> {
    List<LoanRepayment> findByLoanIdOrderByDueDateAsc(Long loanId);
    List<LoanRepayment> findByStatus(RepaymentStatus status);
    List<LoanRepayment> findByStatusAndDueDateBefore(RepaymentStatus status, LocalDate date);
}
