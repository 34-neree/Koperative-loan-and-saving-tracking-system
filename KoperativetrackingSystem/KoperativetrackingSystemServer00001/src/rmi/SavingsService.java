package rmi;

import model.Savings;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface SavingsService extends Remote {

    void deposit(Savings savings) throws RemoteException;

    List<Savings> findByMember(String memberId) throws RemoteException;
}
