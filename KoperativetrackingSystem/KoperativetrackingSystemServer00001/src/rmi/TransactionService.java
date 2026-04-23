package rmi;

import model.Transaction;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface TransactionService extends Remote {

    void save(Transaction transaction) throws RemoteException;

    List<Transaction> findAll() throws RemoteException;

    List<Transaction> findByType(String type) throws RemoteException;

    double getTotalByType(String type) throws RemoteException;
}
