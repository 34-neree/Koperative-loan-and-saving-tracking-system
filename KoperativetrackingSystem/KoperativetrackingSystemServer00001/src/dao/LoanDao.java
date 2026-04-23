package dao;

import model.Loan;
import java.util.List;

public interface LoanDao {

    void requestLoan(Loan loan);

    void approveLoan(String memberId);

    void repayLoan(String memberId, double amount);

    List<Loan> findByMember(String memberId);
}
