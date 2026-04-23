package rmi;

import model.LoanRepayment;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LoanRepaymentService extends Remote {

    void generateSchedule(int loanId, double totalPayable, int months) throws RemoteException;

    List<LoanRepayment> findByLoan(int loanId) throws RemoteException;

    List<LoanRepayment> findOverdue() throws RemoteException;

    void markPaid(int repaymentId, double amountPaid) throws RemoteException;
}
