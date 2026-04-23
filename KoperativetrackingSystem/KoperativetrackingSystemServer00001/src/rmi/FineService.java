package rmi;

import model.Fine;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface FineService extends Remote {

    void issueFine(Fine fine) throws RemoteException;

    List<Fine> findByMember(String memberId) throws RemoteException;

    List<Fine> findUnpaid() throws RemoteException;

    void markPaid(int fineId) throws RemoteException;
}
