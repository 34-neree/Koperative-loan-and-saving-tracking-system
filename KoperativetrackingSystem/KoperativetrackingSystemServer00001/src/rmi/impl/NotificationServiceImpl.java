package rmi.impl;

import dao.NotificationDao;
import dao.impl.NotificationDaoImpl;
import model.Notification;
import rmi.NotificationService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class NotificationServiceImpl extends UnicastRemoteObject implements NotificationService {

    private final NotificationDao notificationDao = new NotificationDaoImpl();

    public NotificationServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void send(Notification notification) throws RemoteException {
        try {
            notificationDao.save(notification);
        } catch (Exception e) {
            throw new RemoteException("Error sending notification", e);
        }
    }

    @Override
    public List<Notification> findByMember(String memberId) throws RemoteException {
        try {
            return notificationDao.findByMember(memberId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching notifications", e);
        }
    }

    @Override
    public List<Notification> findAll() throws RemoteException {
        try {
            return notificationDao.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error fetching all notifications", e);
        }
    }
}
