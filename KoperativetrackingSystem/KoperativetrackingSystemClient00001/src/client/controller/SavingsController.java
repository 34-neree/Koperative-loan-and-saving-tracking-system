package client.controller;

import client.rmi.RMIClient;
import model.Savings;
import rmi.SavingsService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class SavingsController {

    private final SavingsService savingsService;

    public SavingsController() {
        this.savingsService = RMIClient.getSavingsService();
    }

    // DEPOSIT SAVINGS ---------------------------
    public void deposit(String memberId, double amount, String remark) {

        if (amount <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Amount must be greater than zero",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Savings s = new Savings();
            s.setMemberId(memberId);
            s.setAmount(amount);
            s.setRemark(remark);
            savingsService.deposit(s);
            JOptionPane.showMessageDialog(null,
                    "✅ Deposit successful");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Deposit failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LOAD SAVINGS HISTORY ----------------------
    public List<Savings> getSavingsByMember(String memberId) {
        try {
            return savingsService.findByMember(memberId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load savings",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<Savings> findByMember(String memberId) {
        return getSavingsByMember(memberId);
    }
}
