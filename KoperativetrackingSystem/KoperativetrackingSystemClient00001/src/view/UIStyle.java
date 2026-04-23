package view;

import java.awt.*;
import javax.swing.*;

public class UIStyle {

    // 🎨 Colors
    public static final Color BG_COLOR = Color.decode("#F5FFFA"); // MintCream
    public static final Color PRIMARY_GREEN = Color.decode("#2E8B57"); // SeaGreen
    public static final Color LIGHT_GREEN = Color.decode("#E8F5E9");
    public static final Color TEXT_COLOR = Color.DARK_GRAY;

    // 🖋 Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    // 🔘 Styled Button
    public static JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(PRIMARY_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // 🏷 Styled Label
    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(LABEL_FONT);
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    // 📝 Styled TextField
    public static JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setFont(LABEL_FONT);
        return txt;
    }

    // 🔐 Styled PasswordField
    public static JPasswordField createPasswordField() {
        JPasswordField pwd = new JPasswordField();
        pwd.setFont(LABEL_FONT);
        return pwd;
    }

    // 🪟 Frame setup
    public static void setupFrame(JFrame frame, String title) {
        frame.setTitle(title);
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(BG_COLOR);
    }
}
