package dao.impl;

import dao.NotificationDao;
import model.Notification;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class NotificationDaoImpl implements NotificationDao {

    @Override
    public void save(Notification notification) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(notification);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Notification> findByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Notification WHERE member.memberId = :mid ORDER BY sentAt DESC")
                    .setParameter("mid", memberId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Notification> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Notification ORDER BY sentAt DESC")
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }
}
