package view;

import client.controller.ShareController;
import model.Share;
import model.ShareConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ShareView extends JFrame {

    private JTextField txtShares;
    private JTable tblShares;
    private JLabel lblConfig, lblEligibility;
    private final ShareController controller;
    private final String memberId;

    public ShareView(String memberId) {
        this.memberId = memberId;
        this.controller = new ShareController();

        UIStyle.setupFrame(this, "📈 Share Capital (Imigabane)");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("📈 Share Capital — Member " + memberId, SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // CONFIG INFO
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        infoPanel.setBackground(UIStyle.LIGHT_GREEN);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblConfig = UIStyle.createLabel("Loading config...");
        lblEligibility = UIStyle.createLabel("Checking loan eligibility...");
        infoPanel.add(lblConfig);
        infoPanel.add(lblEligibility);

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.setBackground(UIStyle.BG_COLOR);
        topPanel.add(lblHeader, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);

        // TABLE
        tblShares = new JTable(new DefaultTableModel(
                new Object[]{"Share ID", "Shares", "Price/Share", "Total Value", "Status"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblShares);

        // INPUT
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        inputPanel.setBackground(UIStyle.BG_COLOR);

        txtShares = UIStyle.createTextField();
        txtShares.setPreferredSize(new Dimension(100, 30));

        JButton btnPurchase = UIStyle.createButton("🛒 Purchase Shares");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        inputPanel.add(UIStyle.createLabel("Number of Shares:"));
        inputPanel.add(txtShares);
        inputPanel.add(btnPurchase);
        inputPanel.add(btnBack);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.SOUTH);

        add(panel);

        btnPurchase.addActionListener(e -> {
            try {
                Share share = new Share();
                share.setMemberId(memberId);
                share.setNumberOfShares(Integer.parseInt(txtShares.getText().trim()));
                controller.purchaseShares(share);
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "❌ Enter a valid number");
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadData();
    }

    private void loadData() {
        // Load shares table
        DefaultTableModel model = (DefaultTableModel) tblShares.getModel();
        model.setRowCount(0);
        List<Share> list = controller.findByMember(memberId);
        if (list != null) {
            for (Share s : list) {
                model.addRow(new Object[]{
                        s.getShareId(), s.getNumberOfShares(),
                        s.getSharePriceAtPurchase(), s.getTotalValue(), s.getStatus()
                });
            }
        }

        // Load config info
        ShareConfig config = controller.getConfig();
        if (config != null) {
            lblConfig.setText("💰 Share Price: " + config.getSharePrice()
                    + " RWF | Min Shares for Loan: " + config.getMinimumSharesForLoan()
                    + " | Your Total: " + controller.getTotalShares(memberId));
        }

        // Eligibility
        boolean eligible = controller.isEligibleForLoan(memberId);
        lblEligibility.setText(eligible
                ? "✅ You ARE eligible for a loan based on shares"
                : "❌ You are NOT YET eligible for a loan (buy more shares)");
        lblEligibility.setForeground(eligible ? UIStyle.PRIMARY_GREEN : Color.RED);
    }
}
