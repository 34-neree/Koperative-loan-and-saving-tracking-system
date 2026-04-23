package model;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "meetings")
public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int meetingId;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(name = "meeting_date")
    private Timestamp meetingDate;

    @Column(length = 200)
    private String location;

    @Column(length = 2000)
    private String agenda;

    @Column(length = 5000)
    private String minutes;

    @Column(name = "created_by", length = 30)
    private String createdBy;

    @OneToMany(mappedBy = "meeting", cascade = CascadeType.ALL)
    private List<MeetingAttendance> attendances;

    public Meeting() {}

    public int getMeetingId() { return meetingId; }
    public void setMeetingId(int meetingId) { this.meetingId = meetingId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Timestamp getMeetingDate() { return meetingDate; }
    public void setMeetingDate(Timestamp meetingDate) { this.meetingDate = meetingDate; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getAgenda() { return agenda; }
    public void setAgenda(String agenda) { this.agenda = agenda; }

    public String getMinutes() { return minutes; }
    public void setMinutes(String minutes) { this.minutes = minutes; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public List<MeetingAttendance> getAttendances() { return attendances; }
    public void setAttendances(List<MeetingAttendance> attendances) { this.attendances = attendances; }
}
