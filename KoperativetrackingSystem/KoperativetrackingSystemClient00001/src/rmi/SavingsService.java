package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import model.Savings;

public interface SavingsService extends Remote {

    void deposit(Savings savings) throws RemoteException;

    List<Savings> findByMember(String memberId) throws RemoteException;
}
