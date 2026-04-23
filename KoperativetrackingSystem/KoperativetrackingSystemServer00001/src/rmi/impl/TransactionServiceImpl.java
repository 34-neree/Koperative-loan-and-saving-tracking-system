package rmi.impl;

import dao.TransactionDao;
import dao.impl.TransactionDaoImpl;
import model.Transaction;
import rmi.TransactionService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class TransactionServiceImpl extends UnicastRemoteObject implements TransactionService {

    private final TransactionDao transactionDao = new TransactionDaoImpl();

    public TransactionServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void save(Transaction transaction) throws RemoteException {
        try {
            transactionDao.save(transaction);
        } catch (Exception e) {
            throw new RemoteException("Error saving transaction", e);
        }
    }

    @Override
    public List<Transaction> findAll() throws RemoteException {
        try {
            return transactionDao.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error fetching transactions", e);
        }
    }

    @Override
    public List<Transaction> findByType(String type) throws RemoteException {
        try {
            return transactionDao.findByType(type);
        } catch (Exception e) {
            throw new RemoteException("Error fetching transactions by type", e);
        }
    }

    @Override
    public double getTotalByType(String type) throws RemoteException {
        try {
            return transactionDao.getTotalByType(type);
        } catch (Exception e) {
            throw new RemoteException("Error calculating total", e);
        }
    }
}
