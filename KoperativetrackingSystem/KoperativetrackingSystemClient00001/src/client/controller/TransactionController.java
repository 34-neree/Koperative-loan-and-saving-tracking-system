package client.controller;

import client.rmi.RMIClient;
import model.Transaction;
import rmi.TransactionService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController() {
        this.transactionService = RMIClient.getTransactionService();
    }

    public void save(Transaction transaction) {
        try {
            transactionService.save(transaction);
            JOptionPane.showMessageDialog(null, "✅ Transaction recorded");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to save transaction: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Transaction> findAll() {
        try {
            return transactionService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load transactions", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<Transaction> findByType(String type) {
        try {
            return transactionService.findByType(type);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load transactions", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public double getTotalIncome() {
        try {
            return transactionService.getTotalByType("INCOME");
        } catch (Exception e) {
            return 0;
        }
    }

    public double getTotalExpense() {
        try {
            return transactionService.getTotalByType("EXPENSE");
        } catch (Exception e) {
            return 0;
        }
    }
}
