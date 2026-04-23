package view;

import client.controller.LoanRepaymentController;
import model.LoanRepayment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanRepaymentView extends JFrame {

    private JTextField txtLoanId, txtPayAmount;
    private JTable tblSchedule;
    private final LoanRepaymentController controller;
    private final String memberId;

    public LoanRepaymentView(String memberId) {
        this.memberId = memberId;
        this.controller = new LoanRepaymentController();

        UIStyle.setupFrame(this, "📅 Loan Repayment Schedule");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("📅 Loan Repayment Schedule", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblSchedule = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Due Date", "Amount Due", "Paid", "Status", "Penalty"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblSchedule);

        // INPUT PANEL
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(UIStyle.BG_COLOR);

        txtLoanId = UIStyle.createTextField();
        txtLoanId.setPreferredSize(new Dimension(80, 30));
        txtPayAmount = UIStyle.createTextField();
        txtPayAmount.setPreferredSize(new Dimension(100, 30));

        JButton btnLoad = UIStyle.createButton("🔍 Load Schedule");
        JButton btnPay = UIStyle.createButton("💳 Pay Installment");
        JButton btnOverdue = UIStyle.createButton("⚠️ Show Overdue");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        inputPanel.add(UIStyle.createLabel("Loan ID:"));
        inputPanel.add(txtLoanId);
        inputPanel.add(btnLoad);
        inputPanel.add(UIStyle.createLabel("Pay Amount:"));
        inputPanel.add(txtPayAmount);
        inputPanel.add(btnPay);
        inputPanel.add(btnOverdue);
        inputPanel.add(btnBack);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        btnLoad.addActionListener(e -> {
            try {
                int loanId = Integer.parseInt(txtLoanId.getText().trim());
                loadSchedule(loanId);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter a valid Loan ID");
            }
        });

        btnPay.addActionListener(e -> {
            int row = tblSchedule.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select an installment first");
                return;
            }
            try {
                int repaymentId = (int) tblSchedule.getValueAt(row, 0);
                double amount = Double.parseDouble(txtPayAmount.getText().trim());
                controller.markPaid(repaymentId, amount);
                int loanId = Integer.parseInt(txtLoanId.getText().trim());
                loadSchedule(loanId);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage());
            }
        });

        btnOverdue.addActionListener(e -> {
            loadOverdue();
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });
    }

    private void loadSchedule(int loanId) {
        DefaultTableModel model = (DefaultTableModel) tblSchedule.getModel();
        model.setRowCount(0);
        List<LoanRepayment> list = controller.findByLoan(loanId);
        if (list != null) {
            for (LoanRepayment r : list) {
                model.addRow(new Object[]{
                        r.getRepaymentId(), r.getDueDate(), r.getAmountDue(),
                        r.getAmountPaid(), r.getStatus(), r.getPenaltyApplied()
                });
            }
        }
    }

    private void loadOverdue() {
        DefaultTableModel model = (DefaultTableModel) tblSchedule.getModel();
        model.setRowCount(0);
        List<LoanRepayment> list = controller.findOverdue();
        if (list != null) {
            for (LoanRepayment r : list) {
                model.addRow(new Object[]{
                        r.getRepaymentId(), r.getDueDate(), r.getAmountDue(),
                        r.getAmountPaid(), r.getStatus(), r.getPenaltyApplied()
                });
            }
        }
    }
}
