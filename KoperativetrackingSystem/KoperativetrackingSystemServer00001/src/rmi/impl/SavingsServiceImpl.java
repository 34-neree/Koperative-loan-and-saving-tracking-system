package rmi.impl;

import dao.SavingsDao;
import dao.impl.SavingsDaoImpl;
import model.Savings;
import rmi.SavingsService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class SavingsServiceImpl extends UnicastRemoteObject implements SavingsService {

    private final SavingsDao savingsDao = new SavingsDaoImpl();

    public SavingsServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void deposit(Savings savings) throws RemoteException {
        try {
            savingsDao.deposit(savings);
        } catch (Exception e) {
            throw new RemoteException("Error making deposit", e);
        }
    }

    @Override
    public List<Savings> findByMember(String memberId) throws RemoteException {
        try {
            return savingsDao.findByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error retrieving savings", e);
        }
    }
}
