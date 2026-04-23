package view;

import client.controller.NotificationController;
import model.Notification;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class NotificationView extends JFrame {

    private JTextField txtMemberId, txtPhone;
    private JTextArea txtMessage;
    private JTable tblNotifications;
    private final NotificationController controller;
    private final String memberId;

    public NotificationView(String memberId) {
        this.memberId = memberId;
        this.controller = new NotificationController();

        UIStyle.setupFrame(this, "📨 Notifications");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("📨 SMS / System Notifications", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblNotifications = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Member", "Phone", "Message", "Channel", "Status", "Sent At"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblNotifications);

        // FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMemberId = UIStyle.createTextField();
        txtPhone = UIStyle.createTextField();
        txtMessage = new JTextArea(3, 20);
        txtMessage.setFont(UIStyle.LABEL_FONT);
        txtMessage.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIStyle.createLabel("👤 Member ID:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIStyle.createLabel("📱 Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIStyle.createLabel("💬 Message:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtMessage), gbc);

        JButton btnSend = UIStyle.createButton("📤 Send Notification");
        JButton btnAll = UIStyle.createButton("📋 Show All");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnSend);
        btnPanel.add(btnAll);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        add(panel);

        btnSend.addActionListener(e -> {
            Notification n = new Notification();
            n.setMemberId(txtMemberId.getText().trim());
            n.setPhone(txtPhone.getText().trim());
            n.setMessage(txtMessage.getText().trim());
            n.setChannel("SYSTEM");
            n.setStatus("SENT");
            n.setSentAt(new Timestamp(System.currentTimeMillis()));
            controller.send(n);
            loadAll();
            txtMemberId.setText("");
            txtPhone.setText("");
            txtMessage.setText("");
        });

        btnAll.addActionListener(e -> loadAll());

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadAll();
    }

    private void loadAll() {
        DefaultTableModel model = (DefaultTableModel) tblNotifications.getModel();
        model.setRowCount(0);
        List<Notification> list = controller.findAll();
        if (list != null) {
            for (Notification n : list) {
                model.addRow(new Object[]{
                        n.getNotificationId(), n.getMemberId(), n.getPhone(),
                        n.getMessage(), n.getChannel(), n.getStatus(), n.getSentAt()
                });
            }
        }
    }
}
