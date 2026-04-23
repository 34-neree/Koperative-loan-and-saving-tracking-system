package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "meeting_attendance")
public class MeetingAttendance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int attendanceId;

    private boolean attended;

    @Column(name = "fine_issued")
    private boolean fineIssued;

    @ManyToOne
    @JoinColumn(name = "meeting_id", nullable = false)
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public MeetingAttendance() {}

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }

    public boolean isAttended() { return attended; }
    public void setAttended(boolean attended) { this.attended = attended; }

    public boolean isFineIssued() { return fineIssued; }
    public void setFineIssued(boolean fineIssued) { this.fineIssued = fineIssued; }

    public Meeting getMeeting() { return meeting; }
    public void setMeeting(Meeting meeting) { this.meeting = meeting; }

    public Member getMember() { return member; }
    public void setMember(Member member) { this.member = member; }
}
