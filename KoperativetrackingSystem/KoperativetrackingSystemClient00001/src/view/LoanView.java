package view;

import client.controller.LoanController;
import model.Loan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LoanView extends JFrame {

    private JTextField txtAmount, txtPeriod, txtRepayAmount;
    private JTable tblLoans;
    private final LoanController controller;
    private final String memberId;

    public LoanView(String memberId) {
        this.memberId = memberId;
        this.controller = new LoanController();

        UIStyle.setupFrame(this, "📄 Loans");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("📄 Loan Management — Member " + memberId, SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblLoans = new JTable(new DefaultTableModel(
                new Object[]{"Loan ID", "Amount", "Interest", "Period", "Outstanding", "Status"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblLoans);

        // INPUT PANEL
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtAmount = UIStyle.createTextField();
        txtPeriod = UIStyle.createTextField();
        txtRepayAmount = UIStyle.createTextField();

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(UIStyle.createLabel("💵 Loan Amount:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(UIStyle.createLabel("📆 Period (months):"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtPeriod, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(UIStyle.createLabel("💳 Repay Amount:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(txtRepayAmount, gbc);

        JButton btnRequest = UIStyle.createButton("📝 Request Loan");
        JButton btnApprove = UIStyle.createButton("✅ Approve");
        JButton btnRepay = UIStyle.createButton("💳 Repay");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnRequest);
        btnPanel.add(btnApprove);
        btnPanel.add(btnRepay);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        inputPanel.add(btnPanel, gbc);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        btnRequest.addActionListener(e -> {
            try {
                Loan loan = new Loan();
                loan.setMemberId(memberId);
                loan.setAmount(Double.parseDouble(txtAmount.getText().trim()));
                loan.setRepaymentPeriod(Integer.parseInt(txtPeriod.getText().trim()));
                controller.requestLoan(loan);
                loadLoans();
                txtAmount.setText("");
                txtPeriod.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter valid numbers");
            }
        });

        btnApprove.addActionListener(e -> {
            int row = tblLoans.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a loan first");
                return;
            }
            String loanId = String.valueOf(tblLoans.getValueAt(row, 0));
            controller.approveLoan(loanId);
            loadLoans();
        });

        btnRepay.addActionListener(e -> {
            int row = tblLoans.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a loan first");
                return;
            }
            try {
                String loanId = String.valueOf(tblLoans.getValueAt(row, 0));
                double amount = Double.parseDouble(txtRepayAmount.getText().trim());
                controller.repayLoan(loanId, amount);
                loadLoans();
                txtRepayAmount.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter a valid repay amount");
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadLoans();
    }

    private void loadLoans() {
        DefaultTableModel model = (DefaultTableModel) tblLoans.getModel();
        model.setRowCount(0);

        List<Loan> list = controller.findByMember(memberId);
        if (list != null) {
            for (Loan l : list) {
                model.addRow(new Object[]{
                        l.getMemberId(),
                        l.getAmount(),
                        l.getInterest(),
                        l.getRepaymentPeriod(),
                        l.getOutstanding(),
                        l.getStatus()
                });
            }
        }
    }
}
