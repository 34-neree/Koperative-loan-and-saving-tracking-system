package dao.impl;

import dao.ShareDao;
import model.Share;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class ShareDaoImpl implements ShareDao {

    @Override
    public void save(Share share) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(share);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Share> findByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Share WHERE member.memberId = :mid")
                    .setParameter("mid", memberId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public int getTotalSharesByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Object result = session.createQuery(
                    "SELECT COALESCE(SUM(numberOfShares), 0) FROM Share WHERE member.memberId = :mid")
                    .setParameter("mid", memberId)
                    .uniqueResult();
            return ((Number) result).intValue();
        } finally {
            if (session != null) session.close();
        }
    }
}
