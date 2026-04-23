package model;

import java.io.Serializable;

public class MeetingAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    private int attendanceId;
    private int meetingId;
    private String memberId;
    private boolean attended;
    private boolean fineIssued;

    public MeetingAttendance() {}

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }

    public boolean isFineIssued() { return fineIssued; }
    public void setFineIssued(boolean fineIssued) { this.fineIssued = fineIssued; }
}
