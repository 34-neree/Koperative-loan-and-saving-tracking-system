package view;

import client.controller.MeetingController;
import model.Meeting;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class MeetingView extends JFrame {

    private JTextField txtTitle, txtLocation;
    private JTextArea txtAgenda;
    private JTable tblMeetings;
    private final MeetingController controller;
    private final String memberId;

    public MeetingView(String memberId) {
        this.memberId = memberId;
        this.controller = new MeetingController();

        UIStyle.setupFrame(this, "📋 Meetings & Attendance");

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(UIStyle.BG_COLOR);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("📋 Meeting Management", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // TABLE
        tblMeetings = new JTable(new DefaultTableModel(
                new Object[]{"ID", "Title", "Date", "Location", "Created By"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(tblMeetings);

        // FORM
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitle = UIStyle.createTextField();
        txtLocation = UIStyle.createTextField();
        txtAgenda = new JTextArea(3, 20);
        txtAgenda.setFont(UIStyle.LABEL_FONT);
        txtAgenda.setLineWrap(true);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(UIStyle.createLabel("📝 Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(UIStyle.createLabel("📍 Location:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtLocation, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(UIStyle.createLabel("📋 Agenda:"), gbc);
        gbc.gridx = 1;
        formPanel.add(new JScrollPane(txtAgenda), gbc);

        JButton btnSave = UIStyle.createButton("💾 Create Meeting");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnSave);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.SOUTH);

        add(panel);

        btnSave.addActionListener(e -> {
            Meeting meeting = new Meeting();
            meeting.setTitle(txtTitle.getText().trim());
            meeting.setLocation(txtLocation.getText().trim());
            meeting.setAgenda(txtAgenda.getText().trim());
            meeting.setMeetingDate(new Timestamp(System.currentTimeMillis()));
            meeting.setCreatedBy(memberId);
            controller.saveMeeting(meeting);
            loadMeetings();
            txtTitle.setText("");
            txtLocation.setText("");
            txtAgenda.setText("");
        });

        btnBack.addActionListener(e -> {
            new DashboardView(memberId).setVisible(true);
            dispose();
        });

        loadMeetings();
    }

    private void loadMeetings() {
        DefaultTableModel model = (DefaultTableModel) tblMeetings.getModel();
        model.setRowCount(0);
        List<Meeting> list = controller.findAll();
        if (list != null) {
            for (Meeting m : list) {
                model.addRow(new Object[]{
                        m.getMeetingId(), m.getTitle(), m.getMeetingDate(),
                        m.getLocation(), m.getCreatedBy()
                });
            }
        }
    }
}
