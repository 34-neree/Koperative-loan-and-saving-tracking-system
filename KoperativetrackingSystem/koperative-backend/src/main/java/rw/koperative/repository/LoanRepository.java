package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Loan;
import rw.koperative.model.enums.LoanStatus;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByMemberIdOrderByAppliedDateDesc(Long memberId);
    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByStatusIn(List<LoanStatus> statuses);
    boolean existsByMemberIdAndStatusIn(Long memberId, List<LoanStatus> statuses);
}
