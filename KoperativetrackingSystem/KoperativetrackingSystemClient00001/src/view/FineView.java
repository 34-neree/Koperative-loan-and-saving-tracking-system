package view;

import client.controller.FineController;
import model.Fine;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class FineView extends JFrame {

    private JTextField txtMemberId, txtReason, txtAmount;
    private JTable tblFines;
    private final FineController controller;
    private final String memberId;

    public FineView(String memberId) {
        this.memberId = memberId;
        this.controller = new FineController();

        UIStyle.setupFrame(this, "⚖️ Fines & Penalties");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("⚖️ Fine Management", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblFines = new JTable(new DefaultTableModel(
                new Object[]{"Fine ID", "Member", "Reason", "Amount", "Status", "Issued Date"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblFines);

        // FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMemberId = UIStyle.createTextField();
        txtReason = UIStyle.createTextField();
        txtAmount = UIStyle.createTextField();

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIStyle.createLabel("👤 Member ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIStyle.createLabel("📝 Reason:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtReason, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIStyle.createLabel("💰 Amount:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAmount, gbc);

        JButton btnIssue = UIStyle.createButton("⚖️ Issue Fine");
        JButton btnPay = UIStyle.createButton("💳 Mark Paid");
        JButton btnUnpaid = UIStyle.createButton("📋 Show Unpaid");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnIssue);
        btnPanel.add(btnPay);
        btnPanel.add(btnUnpaid);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        add(panel);

        btnIssue.addActionListener(e -> {
            try {
                Fine fine = new Fine();
                fine.setMemberId(txtMemberId.getText().trim());
                fine.setReason(txtReason.getText().trim());
                fine.setAmount(Double.parseDouble(txtAmount.getText().trim()));
                fine.setStatus("UNPAID");
                fine.setIssuedDate(new Timestamp(System.currentTimeMillis()));
                controller.issueFine(fine);
                loadUnpaid();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter a valid amount");
            }
        });

        btnPay.addActionListener(e -> {
            int row = tblFines.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a fine first");
                return;
            }
            int fineId = (int) tblFines.getValueAt(row, 0);
            controller.markPaid(fineId);
            loadUnpaid();
        });

        btnUnpaid.addActionListener(e -> loadUnpaid());

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadUnpaid();
    }

    private void loadUnpaid() {
        DefaultTableModel model = (DefaultTableModel) tblFines.getModel();
        model.setRowCount(0);
        List<Fine> list = controller.findUnpaid();
        if (list != null) {
            for (Fine f : list) {
                model.addRow(new Object[]{
                        f.getFineId(), f.getMemberId(), f.getReason(),
                        f.getAmount(), f.getStatus(), f.getIssuedDate()
                });
            }
        }
    }
}
