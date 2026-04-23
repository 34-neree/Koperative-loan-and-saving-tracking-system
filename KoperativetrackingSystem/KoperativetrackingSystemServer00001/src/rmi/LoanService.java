package rmi;

import model.Loan;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface LoanService extends Remote {

    void requestLoan(Loan loan) throws RemoteException;

    void approveLoan(String loanId) throws RemoteException;

    void repayLoan(String loanId, double amount) throws RemoteException;

    List<Loan> findByMember(String memberId) throws RemoteException;
}
