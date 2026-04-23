package dao;

import model.Meeting;
import model.MeetingAttendance;
import java.util.List;

public interface MeetingDao {

    void saveMeeting(Meeting meeting);

    List<Meeting> findAll();

    Meeting findById(int meetingId);

    void saveAttendance(MeetingAttendance attendance);

    List<MeetingAttendance> findAttendanceByMeeting(int meetingId);
}
