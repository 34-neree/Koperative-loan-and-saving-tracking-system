package view;

import client.controller.MemberController;
import model.Member;
import session.UserSession;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MemberView extends JFrame {

    private JTextField txtMemberId, txtNationalId, txtName, txtGender, txtPhone, txtAddress;
    private JPasswordField txtPassword;
    private JTable table;
    private MemberController controller = new MemberController();

    public MemberView() {
        UIStyle.setupFrame(this, "👥 Member Management");

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(UIStyle.BG_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // HEADER
        JLabel lblHeader = new JLabel("👥 Member Management", SwingConstants.CENTER);
        lblHeader.setFont(UIStyle.TITLE_FONT);
        lblHeader.setForeground(UIStyle.PRIMARY_GREEN);

        // FORM
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(UIStyle.BG_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMemberId = UIStyle.createTextField();
        txtNationalId = UIStyle.createTextField();
        txtName = UIStyle.createTextField();
        txtGender = UIStyle.createTextField();
        txtPhone = UIStyle.createTextField();
        txtAddress = UIStyle.createTextField();
        txtPassword = UIStyle.createPasswordField();

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(UIStyle.createLabel("🆔 Member ID:"), gbc);
        gbc.gridx = 1;
        form.add(txtMemberId, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        form.add(UIStyle.createLabel("🪪 National ID:"), gbc);
        gbc.gridx = 1;
        form.add(txtNationalId, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        form.add(UIStyle.createLabel("📛 Full Name:"), gbc);
        gbc.gridx = 1;
        form.add(txtName, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        form.add(UIStyle.createLabel("⚧ Gender:"), gbc);
        gbc.gridx = 1;
        form.add(txtGender, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        form.add(UIStyle.createLabel("📞 Phone:"), gbc);
        gbc.gridx = 1;
        form.add(txtPhone, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        form.add(UIStyle.createLabel("📍 Address:"), gbc);
        gbc.gridx = 1;
        form.add(txtAddress, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        form.add(UIStyle.createLabel("🔒 Password:"), gbc);
        gbc.gridx = 1;
        form.add(txtPassword, gbc);

        JButton btnSave = UIStyle.createButton("💾 Save");
        JButton btnClear = UIStyle.createButton("🧹 Clear");
        JButton btnDelete = UIStyle.createButton("🗑️ Delete");
        JButton btnBack = UIStyle.createButton("🔙 Back");

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        btnPanel.setBackground(UIStyle.BG_COLOR);
        btnPanel.add(btnSave);
        btnPanel.add(btnClear);
        btnPanel.add(btnDelete);
        btnPanel.add(btnBack);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2;
        form.add(btnPanel, gbc);

        // TABLE
        table = new JTable(new DefaultTableModel(
                new Object[]{"Member ID", "National ID", "Name", "Gender", "Phone", "Address"}, 0));
        JScrollPane scroll = new JScrollPane(table);

        mainPanel.add(lblHeader, BorderLayout.NORTH);
        mainPanel.add(form, BorderLayout.WEST);
        mainPanel.add(scroll, BorderLayout.CENTER);

        add(mainPanel);

        loadMembers();

        btnSave.addActionListener(e -> {
            Member m = new Member();
            m.setMemberId(txtMemberId.getText().trim());
            m.setNationalId(txtNationalId.getText().trim());
            m.setFullName(txtName.getText().trim());
            m.setGender(txtGender.getText().trim());
            m.setPhone(txtPhone.getText().trim());
            m.setAddress(txtAddress.getText().trim());
            m.setPassword(new String(txtPassword.getPassword()));

            controller.addMember(m);
            loadMembers();
            clearForm();
        });

        btnClear.addActionListener(e -> clearForm());

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Select a member first");
                return;
            }
            String id = (String) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete member " + id + "?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                controller.deleteMember(id);
                loadMembers();
            }
        });

        btnBack.addActionListener(e -> {
            new DashboardView(UserSession.getMemberId()).setVisible(true);
            dispose();
        });

        // Click row to populate form
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtMemberId.setText(String.valueOf(table.getValueAt(row, 0)));
                txtNationalId.setText(String.valueOf(table.getValueAt(row, 1)));
                txtName.setText(String.valueOf(table.getValueAt(row, 2)));
                txtGender.setText(String.valueOf(table.getValueAt(row, 3)));
                txtPhone.setText(String.valueOf(table.getValueAt(row, 4)));
                txtAddress.setText(String.valueOf(table.getValueAt(row, 5)));
            }
        });
    }

    private void clearForm() {
        txtMemberId.setText("");
        txtNationalId.setText("");
        txtName.setText("");
        txtGender.setText("");
        txtPhone.setText("");
        txtAddress.setText("");
        txtPassword.setText("");
    }

    private void loadMembers() {
        List<Member> list = controller.getAllMembers();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);

        if (list != null) {
            for (Member m : list) {
                model.addRow(new Object[]{
                        m.getMemberId(),
                        m.getNationalId(),
                        m.getFullName(),
                        m.getGender(),
                        m.getPhone(),
                        m.getAddress()
                });
            }
        }
    }
}
