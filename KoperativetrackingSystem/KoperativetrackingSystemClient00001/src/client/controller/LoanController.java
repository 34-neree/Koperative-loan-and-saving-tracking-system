package client.controller;

import client.rmi.RMIClient;
import model.Loan;
import rmi.LoanService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class LoanController {

    private final LoanService loanService;

    public LoanController() {
        this.loanService = RMIClient.getLoanService();
    }

    // REQUEST LOAN ------------------------------
    public void requestLoan(Loan loan) {
        try {
            loanService.requestLoan(loan);
            JOptionPane.showMessageDialog(null,
                    "✅ Loan request submitted");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Loan request failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // APPROVE LOAN ------------------------------
    public void approveLoan(String loanId) {
        try {
            loanService.approveLoan(loanId);
            JOptionPane.showMessageDialog(null,
                    "✅ Loan approved");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Approval failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // REPAY LOAN --------------------------------
    public void repayLoan(String loanId, double amount) {

        if (amount <= 0) {
            JOptionPane.showMessageDialog(null,
                    "Repayment amount must be positive",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            loanService.repayLoan(loanId, amount);
            JOptionPane.showMessageDialog(null,
                    "✅ Loan repayment successful");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Repayment failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // LOAD LOANS BY MEMBER ----------------------
    public List<Loan> getLoansByMember(String memberId) {
        try {
            return loanService.findByMember(memberId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load loans",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public List<Loan> findByMember(String memberId) {
        return getLoansByMember(memberId);
    }
}
