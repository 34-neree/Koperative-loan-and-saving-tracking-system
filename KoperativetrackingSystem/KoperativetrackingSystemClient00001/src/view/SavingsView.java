package view;

import client.controller.SavingsController;
import model.Savings;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SavingsView extends JFrame {

    private JTextField txtAmount, txtDescription;
    private JTable tblSavings;
    private final SavingsController controller;
    private final String memberId;

    public SavingsView(String memberId) {
        this.memberId = memberId;
        this.controller = new SavingsController();

        UIStyle.setupFrame(this, "💰 Savings");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblHeader = new JLabel("💰 Savings — Member " + memberId, SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblSavings = new JTable(new DefaultTableModel(
                new Object[]{"Date", "Amount", "Balance After", "Remark"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblSavings);

        // INPUT PANEL
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(UIStyle.BG_COLOR);

        txtAmount = UIStyle.createTextField();
        txtAmount.setPreferredSize(new Dimension(100, 30));
        txtDescription = UIStyle.createTextField();
        txtDescription.setPreferredSize(new Dimension(150, 30));

        JButton btnDeposit = UIStyle.createButton("➕ Deposit");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        inputPanel.add(UIStyle.createLabel("💵 Amount:"));
        inputPanel.add(txtAmount);
        inputPanel.add(UIStyle.createLabel("📝 Remark:"));
        inputPanel.add(txtDescription);
        inputPanel.add(btnDeposit);
        inputPanel.add(btnBack);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        btnDeposit.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(txtAmount.getText().trim());
                String remark = txtDescription.getText().trim();
                controller.deposit(memberId, amount, remark);
                loadSavings();
                txtAmount.setText("");
                txtDescription.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ Enter a valid amount",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadSavings();
    }

    private void loadSavings() {
        DefaultTableModel model = (DefaultTableModel) tblSavings.getModel();
        model.setRowCount(0);

        List<Savings> list = controller.findByMember(memberId);
        if (list != null) {
            for (Savings s : list) {
                model.addRow(new Object[]{
                        s.getDate(),
                        s.getAmount(),
                        s.getBalanceAfter(),
                        s.getRemark()
                });
            }
        }
    }
}
