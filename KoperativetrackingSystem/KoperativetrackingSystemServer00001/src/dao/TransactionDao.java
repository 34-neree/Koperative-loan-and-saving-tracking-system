package dao;

import model.Transaction;
import java.util.List;

public interface TransactionDao {

    void save(Transaction transaction);

    List<Transaction> findAll();

    List<Transaction> findByType(String type);

    double getTotalByType(String type);
}
