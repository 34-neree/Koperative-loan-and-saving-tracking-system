package model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

public class Meeting implements Serializable {

    private static final long serialVersionUID = 1L;

    private int meetingId;
    private String title;
    private Timestamp meetingDate;
    private String location;
    private String agenda;
    private String minutes;
    private String createdBy;

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
}
