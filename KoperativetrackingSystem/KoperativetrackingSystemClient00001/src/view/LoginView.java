package view;

import client.controller.MemberController;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField txtMemberId;
    private JPasswordField txtPassword;

    public LoginView() {
        UIStyle.setupFrame(this, "🔐 Cooperative Login");

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel title = new JLabel("🏦 Koperative Savings & Loan Tracker");
        title.setFont(UIStyle.TITLE_FONT);
        title.setForeground(UIStyle.PRIMARY_GREEN);

        txtMemberId = UIStyle.createTextField();
        txtPassword = UIStyle.createPasswordField();

        JButton btnLogin = UIStyle.createButton("Login 🔑");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        panel.add(UIStyle.createLabel("👤 Member ID:"), gbc);
        gbc.gridx = 1;
        panel.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(UIStyle.createLabel("🔒 Password:"), gbc);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        panel.add(btnLogin, gbc);

        add(panel);

        btnLogin.addActionListener(e -> {
            String memberId = txtMemberId.getText().trim();
            String password = new String(txtPassword.getPassword());

            MemberController controller = new MemberController();

            if (controller.login(memberId, password)) {
                session.UserSession.setMemberId(memberId);
                JOptionPane.showMessageDialog(this, "✅ Login successful");
                new DashboardView(memberId).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ Invalid credentials",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
