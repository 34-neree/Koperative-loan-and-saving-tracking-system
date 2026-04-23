package rmi.impl;

import dao.LoanRepaymentDao;
import dao.impl.LoanRepaymentDaoImpl;
import model.LoanRepayment;
import rmi.LoanRepaymentService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class LoanRepaymentServiceImpl extends UnicastRemoteObject implements LoanRepaymentService {

    private final LoanRepaymentDao repaymentDao = new LoanRepaymentDaoImpl();

    public LoanRepaymentServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void generateSchedule(int loanId, double totalPayable, int months) throws RemoteException {
        try {
            repaymentDao.generateSchedule(loanId, totalPayable, months);
        } catch (Exception e) {
            throw new RemoteException("Error generating repayment schedule", e);
        }
    }

    @Override
    public List<LoanRepayment> findByLoan(int loanId) throws RemoteException {
        try {
            return repaymentDao.findByLoan(loanId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching repayments", e);
        }
    }

    @Override
    public List<LoanRepayment> findOverdue() throws RemoteException {
        try {
            return repaymentDao.findOverdue();
        } catch (Exception e) {
            throw new RemoteException("Error fetching overdue repayments", e);
        }
    }

    @Override
    public void markPaid(int repaymentId, double amountPaid) throws RemoteException {
        try {
            repaymentDao.markPaid(repaymentId, amountPaid);
        } catch (Exception e) {
            throw new RemoteException("Error recording repayment", e);
        }
    }
}
