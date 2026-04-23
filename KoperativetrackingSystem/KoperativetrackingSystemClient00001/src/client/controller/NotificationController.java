package client.controller;

import client.rmi.RMIClient;
import model.Notification;
import rmi.NotificationService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController() {
        this.notificationService = RMIClient.getNotificationService();
    }

    public void send(Notification notification) {
        try {
            notificationService.send(notification);
            JOptionPane.showMessageDialog(null, "✅ Notification sent");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to send notification: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Notification> findByMember(String memberId) {
        try {
            return notificationService.findByMember(memberId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load notifications", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<Notification> findAll() {
        try {
            return notificationService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load notifications", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
