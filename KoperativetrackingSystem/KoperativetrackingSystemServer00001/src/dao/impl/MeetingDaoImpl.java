package dao.impl;

import dao.MeetingDao;
import model.Meeting;
import model.MeetingAttendance;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MeetingDaoImpl implements MeetingDao {

    @Override
    public void saveMeeting(Meeting meeting) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(meeting);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Meeting> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Meeting ORDER BY meetingDate DESC").list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Meeting findById(int meetingId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return (Meeting) session.get(Meeting.class, meetingId);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void saveAttendance(MeetingAttendance attendance) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(attendance);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<MeetingAttendance> findAttendanceByMeeting(int meetingId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM MeetingAttendance WHERE meeting.meetingId = :mid")
                    .setParameter("mid", meetingId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }
}
