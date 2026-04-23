package rmi.impl;

import dao.FineDao;
import dao.impl.FineDaoImpl;
import model.Fine;
import rmi.FineService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class FineServiceImpl extends UnicastRemoteObject implements FineService {

    private final FineDao fineDao = new FineDaoImpl();

    public FineServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void issueFine(Fine fine) throws RemoteException {
        try {
            fineDao.save(fine);
        } catch (Exception e) {
            throw new RemoteException("Error issuing fine", e);
        }
    }

    @Override
    public List<Fine> findByMember(String memberId) throws RemoteException {
        try {
            return fineDao.findByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching fines", e);
        }
    }

    @Override
    public List<Fine> findUnpaid() throws RemoteException {
        try {
            return fineDao.findUnpaid();
        } catch (Exception e) {
            throw new RemoteException("Error fetching unpaid fines", e);
        }
    }

    @Override
    public void markPaid(int fineId) throws RemoteException {
        try {
            fineDao.markPaid(fineId);
        } catch (Exception e) {
            throw new RemoteException("Error marking fine as paid", e);
        }
    }
}
