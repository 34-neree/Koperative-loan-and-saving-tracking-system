package rmi;

import model.Meeting;
import model.MeetingAttendance;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MeetingService extends Remote {

    void saveMeeting(Meeting meeting) throws RemoteException;

    List<Meeting> findAll() throws RemoteException;

    Meeting findById(int meetingId) throws RemoteException;

    void saveAttendance(MeetingAttendance attendance) throws RemoteException;

    List<MeetingAttendance> findAttendanceByMeeting(int meetingId) throws RemoteException;
}
