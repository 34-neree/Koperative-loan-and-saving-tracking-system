package rmi;

import model.Notification;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface NotificationService extends Remote {

    void send(Notification notification) throws RemoteException;

    List<Notification> findByMember(String memberId) throws RemoteException;

    List<Notification> findAll() throws RemoteException;
}
