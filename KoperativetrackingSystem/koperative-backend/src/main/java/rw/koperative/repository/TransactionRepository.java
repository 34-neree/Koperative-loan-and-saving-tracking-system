package rw.koperative.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import rw.koperative.model.Transaction;
import rw.koperative.model.enums.FinancialType;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByTypeOrderByTransactionDateDesc(FinancialType type);

    @Query("SELECT t FROM Transaction t WHERE t.transactionDate BETWEEN :start AND :end ORDER BY t.transactionDate DESC")
    List<Transaction> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    @Query("SELECT t FROM Transaction t WHERE t.type = :type AND t.transactionDate BETWEEN :start AND :end ORDER BY t.transactionDate DESC")
    List<Transaction> findByTypeAndDateRange(@Param("type") FinancialType type, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
