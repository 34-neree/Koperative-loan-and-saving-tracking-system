package rmi.impl;

import dao.LoanDao;
import dao.impl.LoanDaoImpl;
import model.Loan;
import rmi.LoanService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoanServiceImpl extends UnicastRemoteObject implements LoanService {

    private final LoanDao loanDao = new LoanDaoImpl();

    public LoanServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void requestLoan(Loan loan) throws RemoteException {
        try {
            loanDao.requestLoan(loan);
        } catch (Exception e) {
            throw new RemoteException("Error requesting loan", e);
        }
    }

    @Override
    public void approveLoan(String loanId) throws RemoteException {
        try {
            loanDao.approveLoan(loanId);
        } catch (Exception e) {
            throw new RemoteException("Error approving loan", e);
        }
    }

    @Override
    public void repayLoan(String loanId, double amount) throws RemoteException {
        try {
            loanDao.repayLoan(loanId, amount);
        } catch (Exception e) {
            throw new RemoteException("Error repaying loan", e);
        }
    }

    @Override
    public List<Loan> findByMember(String memberId) throws RemoteException {
        try {
            return loanDao.findByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching loans", e);
        }
    }
}
