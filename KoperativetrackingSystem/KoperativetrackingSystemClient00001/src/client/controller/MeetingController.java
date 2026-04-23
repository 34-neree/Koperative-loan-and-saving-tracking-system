package client.controller;

import client.rmi.RMIClient;
import model.Meeting;
import model.MeetingAttendance;
import rmi.MeetingService;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;

public class MeetingController {

    private final MeetingService meetingService;

    public MeetingController() {
        this.meetingService = RMIClient.getMeetingService();
    }

    public void saveMeeting(Meeting meeting) {
        try {
            meetingService.saveMeeting(meeting);
            JOptionPane.showMessageDialog(null, "✅ Meeting saved");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to save meeting: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<Meeting> findAll() {
        try {
            return meetingService.findAll();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load meetings", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public void saveAttendance(MeetingAttendance attendance) {
        try {
            meetingService.saveAttendance(attendance);
            JOptionPane.showMessageDialog(null, "✅ Attendance recorded");
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to save attendance: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<MeetingAttendance> findAttendanceByMeeting(int meetingId) {
        try {
            return meetingService.findAttendanceByMeeting(meetingId);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "❌ Failed to load attendance", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}
