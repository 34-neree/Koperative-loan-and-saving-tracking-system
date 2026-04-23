package client.controller;

import client.rmi.RMIClient;
import model.Share;
import model.ShareConfig;
import rmi.ShareService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class ShareController {

    private final ShareService shareService;

    public ShareController() {
        this.shareService = RMIClient.getShareService();
    }

    public void purchaseShares(Share share) {
        try {
            shareService.purchaseShares(share);
            JOptionPane.showMessageDialog(null, "✅ Shares purchased successfully");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Share purchase failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Share> findByMember(String memberId) {
        try {
            return shareService.findByMember(memberId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load shares", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public int getTotalShares(String memberId) {
        try {
            return shareService.getTotalSharesByMember(memberId);
        } catch (Exception e) {
            return 0;
        }
    }

    public ShareConfig getConfig() {
        try {
            return shareService.getConfig();
        } catch (Exception e) {
            return null;
        }
    }

    public void updateConfig(ShareConfig config) {
        try {
            shareService.updateConfig(config);
            JOptionPane.showMessageDialog(null, "✅ Share config updated");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Config update failed: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean isEligibleForLoan(String memberId) {
        try {
            return shareService.isEligibleForLoan(memberId);
        } catch (Exception e) {
            return false;
        }
    }
}
