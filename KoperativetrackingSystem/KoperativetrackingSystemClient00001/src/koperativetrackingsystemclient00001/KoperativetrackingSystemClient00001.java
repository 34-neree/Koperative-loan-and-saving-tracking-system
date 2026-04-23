package koperativetrackingsystemclient00001;

import view.LoginView;
import javax.swing.*;

/**
 * Main entry point for the Koperative Tracking System Client.
 * Launches the Login screen on the Event Dispatch Thread.
 */
public class KoperativetrackingSystemClient00001 {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Use default look and feel
            }
            new LoginView().setVisible(true);
        });
    }
}
