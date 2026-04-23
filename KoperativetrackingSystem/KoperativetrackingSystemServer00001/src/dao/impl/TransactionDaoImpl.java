package dao.impl;

import dao.TransactionDao;
import model.Transaction;
import config.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class TransactionDaoImpl implements TransactionDao {

    @Override
    public void save(Transaction transaction) {
        Session session = null;
        org.hibernate.Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(transaction);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Transaction> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Transaction ORDER BY transactionDate DESC")
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Transaction> findByType(String type) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Transaction WHERE type = :type ORDER BY transactionDate DESC")
                    .setParameter("type", type)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public double getTotalByType(String type) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Object result = session.createQuery(
                    "SELECT COALESCE(SUM(amount), 0) FROM Transaction WHERE type = :type")
                    .setParameter("type", type)
                    .uniqueResult();
            return ((Number) result).doubleValue();
        } finally {
            if (session != null) session.close();
        }
    }
}
