package rw.koperative.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rw.koperative.dto.TransactionRequest;
import rw.koperative.model.Transaction;
import rw.koperative.model.enums.FinancialType;
import rw.koperative.repository.TransactionRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(TransactionRequest request, String recordedBy) {
        Transaction tx = Transaction.builder()
                .type(FinancialType.valueOf(request.getType().toUpperCase()))
                .category(request.getCategory())
                .amount(request.getAmount())
                .description(request.getDescription())
                .recordedBy(recordedBy)
                .receiptNumber(request.getReceiptNumber())
                .build();
        return transactionRepository.save(tx);
    }

    public List<Transaction> getTransactions(String type, Integer month, Integer year) {
        if (type != null && month != null && year != null) {
            LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
            LocalDateTime end = start.plusMonths(1);
            return transactionRepository.findByTypeAndDateRange(FinancialType.valueOf(type.toUpperCase()), start, end);
        }
        if (type != null) {
            return transactionRepository.findByTypeOrderByTransactionDateDesc(FinancialType.valueOf(type.toUpperCase()));
        }
        return transactionRepository.findAll();
    }
}
