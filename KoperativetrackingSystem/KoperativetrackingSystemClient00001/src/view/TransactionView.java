package view;

import client.controller.TransactionController;
import model.Transaction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class TransactionView extends JFrame {

    private JComboBox<String> cmbType;
    private JTextField txtCategory, txtAmount, txtReceipt;
    private JTextArea txtDescription;
    private JTable tblTransactions;
    private JLabel lblSummary;
    private final TransactionController controller;
    private final String memberId;

    public TransactionView(String memberId) {
        this.memberId = memberId;
        this.controller = new TransactionController();

        UIStyle.setupFrame(this, "💹 Income & Expenses");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER + SUMMARY
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(UIStyle.BG_COLOR);

        JLabel lblHeader = new JLabel("💹 Income & Expense Tracking", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        lblSummary = UIStyle.createLabel("Loading summary...");
        lblSummary.setHorizontalAlignment(SwingConstants.CENTER);
        lblSummary.setFont(new Font("Segoe UI", Font.BOLD, 13));

        headerPanel.add(lblHeader, BorderLayout.NORTH);
        headerPanel.add(lblSummary, BorderLayout.SOUTH);

        // TABLE
        tblTransactions = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Type", "Category", "Amount", "Description", "Date", "Receipt"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblTransactions);

        // FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbType = new JComboBox<>(new String[]{"INCOME", "EXPENSE"});
        cmbType.setFont(UIStyle.LABEL_FONT);
        txtCategory = UIStyle.createTextField();
        txtAmount = UIStyle.createTextField();
        txtReceipt = UIStyle.createTextField();
        txtDescription = new JTextArea(2, 20);
        txtDescription.setFont(UIStyle.LABEL_FONT);
        txtDescription.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIStyle.createLabel("📊 Type:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cmbType, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIStyle.createLabel("📂 Category:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIStyle.createLabel("💰 Amount:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(UIStyle.createLabel("🧾 Receipt #:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtReceipt, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(UIStyle.createLabel("📝 Description:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtDescription), gbc);

        JButton btnSave = UIStyle.createButton("💾 Record");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnSave);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        add(panel);

        btnSave.addActionListener(e -> {
            try {
                Transaction t = new Transaction();
                t.setType((String) cmbType.getSelectedItem());
                t.setCategory(txtCategory.getText().trim());
                t.setAmount(Double.parseDouble(txtAmount.getText().trim()));
                t.setDescription(txtDescription.getText().trim());
                t.setReceiptNumber(txtReceipt.getText().trim());
                t.setRecordedBy(memberId);
                t.setTransactionDate(new Timestamp(System.currentTimeMillis()));
                controller.save(t);
                loadData();
                txtCategory.setText("");
                txtAmount.setText("");
                txtReceipt.setText("");
                txtDescription.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter a valid amount");
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadData();
    }

    private void loadData() {
        DefaultTableModel model = (DefaultTableModel) tblTransactions.getModel();
        model.setRowCount(0);
        List<Transaction> list = controller.findAll();
        if (list != null) {
            for (Transaction t : list) {
                model.addRow(new Object[]{
                        t.getTransactionId(), t.getType(), t.getCategory(),
                        t.getAmount(), t.getDescription(), t.getTransactionDate(),
                        t.getReceiptNumber()
                });
            }
        }

        double income = controller.getTotalIncome();
        double expense = controller.getTotalExpense();
        lblSummary.setText(String.format(
                "📊 Total Income: %,.0f RWF  |  Total Expense: %,.0f RWF  |  Net: %,.0f RWF",
                income, expense, income - expense));
    }
}
