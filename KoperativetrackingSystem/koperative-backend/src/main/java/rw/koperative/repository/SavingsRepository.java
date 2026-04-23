package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Savings;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SavingsRepository extends JpaRepository<Savings, Long> {
    List<Savings> findByMemberIdOrderByTransactionDateDesc(Long memberId);

    @Query("SELECT COALESCE(SUM(CASE WHEN s.transactionType = 'DEPOSIT' THEN s.amount ELSE -s.amount END), 0) " +
           "FROM Savings s WHERE s.member.id = :memberId")
    BigDecimal calculateBalanceByMemberId(@Param("memberId") Long memberId);

    @Query("SELECT s FROM Savings s WHERE s.transactionDate BETWEEN :start AND :end ORDER BY s.transactionDate DESC")
    List<Savings> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
