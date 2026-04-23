package dao;

import model.LoanRepayment;
import java.util.List;

public interface LoanRepaymentDao {

    void save(LoanRepayment repayment);

    List<LoanRepayment> findByLoan(int loanId);

    List<LoanRepayment> findOverdue();

    void markPaid(int repaymentId, double amountPaid);

    void generateSchedule(int loanId, double totalPayable, int months);
}
