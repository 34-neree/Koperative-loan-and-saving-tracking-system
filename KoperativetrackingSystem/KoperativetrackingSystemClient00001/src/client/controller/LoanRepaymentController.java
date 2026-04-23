package client.controller;

import client.rmi.RMIClient;
import model.LoanRepayment;
import rmi.LoanRepaymentService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class LoanRepaymentController {

    private final LoanRepaymentService repaymentService;

    public LoanRepaymentController() {
        this.repaymentService = RMIClient.getLoanRepaymentService();
    }

    public void generateSchedule(int loanId, double totalPayable, int months) {
        try {
            repaymentService.generateSchedule(loanId, totalPayable, months);
            JOptionPane.showMessageDialog(null, "✅ Repayment schedule generated");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Schedule generation failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<LoanRepayment> findByLoan(int loanId) {
        try {
            return repaymentService.findByLoan(loanId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load schedule", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<LoanRepayment> findOverdue() {
        try {
            return repaymentService.findOverdue();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load overdue repayments", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void markPaid(int repaymentId, double amountPaid) {
        try {
            repaymentService.markPaid(repaymentId, amountPaid);
            JOptionPane.showMessageDialog(null, "✅ Repayment recorded");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Repayment failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
