package rmi.impl;

import dao.MeetingDao;
import dao.impl.MeetingDaoImpl;
import model.Meeting;
import model.MeetingAttendance;
import rmi.MeetingService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class MeetingServiceImpl extends UnicastRemoteObject implements MeetingService {

    private final MeetingDao meetingDao = new MeetingDaoImpl();

    public MeetingServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void saveMeeting(Meeting meeting) throws RemoteException {
        try {
            meetingDao.saveMeeting(meeting);
        } catch (Exception e) {
            throw new RemoteException("Error saving meeting", e);
        }
    }

    @Override
    public List<Meeting> findAll() throws RemoteException {
        try {
            return meetingDao.findAll();
        } catch (Exception e) {
            throw new RemoteException("Error fetching meetings", e);
        }
    }

    @Override
    public Meeting findById(int meetingId) throws RemoteException {
        try {
            return meetingDao.findById(meetingId);
        } catch (Exception e) {
            throw new RemoteException("Error finding meeting", e);
        }
    }

    @Override
    public void saveAttendance(MeetingAttendance attendance) throws RemoteException {
        try {
            meetingDao.saveAttendance(attendance);
        } catch (Exception e) {
            throw new RemoteException("Error saving attendance", e);
        }
    }

    @Override
    public List<MeetingAttendance> findAttendanceByMeeting(int meetingId) throws RemoteException {
        try {
            return meetingDao.findAttendanceByMeeting(meetingId);
        } catch (Exception e) {
            throw new RemoteException("Error fetching attendance", e);
        }
    }
}
