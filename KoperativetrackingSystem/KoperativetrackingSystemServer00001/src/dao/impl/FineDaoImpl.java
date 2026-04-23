package dao.impl;

import dao.FineDao;
import model.Fine;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Timestamp;
import java.util.List;

public class FineDaoImpl implements FineDao {

    @Override
    public void save(Fine fine) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(fine);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Fine> findByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Fine WHERE member.memberId = :mid ORDER BY issuedDate DESC")
                    .setParameter("mid", memberId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Fine> findUnpaid() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Fine WHERE status = 'UNPAID' ORDER BY issuedDate ASC")
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void markPaid(int fineId) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Fine fine = (Fine) session.get(Fine.class, fineId);
            if (fine != null) {
                fine.setStatus("PAID");
                fine.setPaidDate(new Timestamp(System.currentTimeMillis()));
                session.update(fine);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
