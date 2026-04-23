package client.controller;

import client.rmi.RMIClient;
import model.Fine;
import rmi.FineService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class FineController {

    private final FineService fineService;

    public FineController() {
        this.fineService = RMIClient.getFineService();
    }

    public void issueFine(Fine fine) {
        try {
            fineService.issueFine(fine);
            JOptionPane.showMessageDialog(null, "✅ Fine issued");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to issue fine: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Fine> findByMember(String memberId) {
        try {
            return fineService.findByMember(memberId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load fines", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<Fine> findUnpaid() {
        try {
            return fineService.findUnpaid();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load unpaid fines", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void markPaid(int fineId) {
        try {
            fineService.markPaid(fineId);
            JOptionPane.showMessageDialog(null, "✅ Fine marked as paid");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Payment failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
