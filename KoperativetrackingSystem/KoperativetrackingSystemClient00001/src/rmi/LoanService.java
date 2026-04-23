package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Loan;

public interface LoanService extends Remote {

    void requestLoan(Loan loan) throws RemoteException;

    void approveLoan(String memberId) throws RemoteException;

    void repayLoan(String memberId, double amount) throws RemoteException;

    List<Loan> findByMember(String memberId) throws RemoteException;
}
