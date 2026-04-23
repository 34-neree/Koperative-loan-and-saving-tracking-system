package view;

import javax.swing.*;
import java.awt.*;

public class DashboardView extends JFrame {

    public DashboardView(String memberId) {
        UIStyle.setupFrame(this, "📊 Koperative Dashboard");

        JPanel panel = new JPanel(new GridLayout(10, 1, 10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel lblWelcome = new JLabel("👋 Welcome, Member " + memberId, SwingConstants.CENTER);
        lblWelcome.setFont(UIStyle.TITLE_FONT);
        lblWelcome.setForeground(UIStyle.PRIMARY_GREEN);

        JButton btnMembers   = UIStyle.createButton("👥 Members");
        JButton btnSavings   = UIStyle.createButton("💰 Savings");
        JButton btnLoans     = UIStyle.createButton("📄 Loans");
        JButton btnShares    = UIStyle.createButton("📈 Share Capital (Imigabane)");
        JButton btnRepay     = UIStyle.createButton("📅 Loan Repayments");
        JButton btnMeetings  = UIStyle.createButton("📋 Meetings & Attendance");
        JButton btnFines     = UIStyle.createButton("⚖️ Fines & Penalties");
        JButton btnTransact  = UIStyle.createButton("💹 Income & Expenses");
        JButton btnNotify    = UIStyle.createButton("📨 Notifications");
        JButton btnLogout    = UIStyle.createButton("🚪 Logout");

        panel.add(lblWelcome);
        panel.add(btnMembers);
        panel.add(btnSavings);
        panel.add(btnLoans);
        panel.add(btnShares);
        panel.add(btnRepay);
        panel.add(btnMeetings);
        panel.add(btnFines);
        panel.add(btnTransact);
        panel.add(btnNotify);

        // We need an extra row for logout
        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setBackground(UIStyle.BG_COLOR);
        outerPanel.add(panel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(UIStyle.BG_COLOR);
        bottomPanel.add(btnLogout);
        outerPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(outerPanel);

        // Navigation handlers
        btnMembers.addActionListener(e -> {
            new MemberView().setVisible(true);
            dispose();
        });

        btnSavings.addActionListener(e -> {
            new SavingsView(memberId).setVisible(true);
            dispose();
        });

        btnLoans.addActionListener(e -> {
            new LoanView(memberId).setVisible(true);
            dispose();
        });

        btnShares.addActionListener(e -> {
            new ShareView(memberId).setVisible(true);
            dispose();
        });

        btnRepay.addActionListener(e -> {
            new LoanRepaymentView(memberId).setVisible(true);
            dispose();
        });

        btnMeetings.addActionListener(e -> {
            new MeetingView(memberId).setVisible(true);
            dispose();
        });

        btnFines.addActionListener(e -> {
            new FineView(memberId).setVisible(true);
            dispose();
        });

        btnTransact.addActionListener(e -> {
            new TransactionView(memberId).setVisible(true);
            dispose();
        });

        btnNotify.addActionListener(e -> {
            new NotificationView(memberId).setVisible(true);
            dispose();
        });

        btnLogout.addActionListener(e -> {
            new LoginView().setVisible(true);
            dispose();
        });
    }
}
