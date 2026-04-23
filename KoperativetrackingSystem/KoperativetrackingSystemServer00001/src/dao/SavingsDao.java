package dao;

import model.Savings;
import java.util.List;

public interface SavingsDao {

    void deposit(Savings savings);

    List<Savings> findByMember(String memberId);
}
