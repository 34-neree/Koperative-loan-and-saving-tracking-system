package client.controller;

import client.rmi.RMIClient;
import model.Member;
import rmi.MemberService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class MemberController {

    private final MemberService memberService;

    public MemberController() {
        this.memberService = RMIClient.getMemberService();
    }

    // AUTHENTICATION ----------------------------
    public boolean login(String memberId, String password) {
        try {
            return memberService.authenticate(memberId, password);
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "Login failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // CREATE MEMBER -----------------------------
    public void addMember(Member member) {
        try {
            memberService.save(member);
            JOptionPane.showMessageDialog(null,
                    "✅ Member added successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to add member: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // UPDATE MEMBER -----------------------------
    public void updateMember(Member member) {
        try {
            memberService.update(member);
            JOptionPane.showMessageDialog(null,
                    "✅ Member updated successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Update failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // DELETE MEMBER -----------------------------
    public void deleteMember(String memberId) {
        try {
            memberService.delete(memberId);
            JOptionPane.showMessageDialog(null,
                    "✅ Member deleted successfully");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Delete failed: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // FETCH ALL MEMBERS -------------------------
    public List<Member> getAllMembers() {
        try {
            return memberService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load members",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
