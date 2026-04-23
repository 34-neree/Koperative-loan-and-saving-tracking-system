package dao.impl;

import dao.MemberDao;
import model.Member;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
import org.hibernate.Query;

public class MemberDaoImpl implements MemberDao {

    @Override
    public void save(Member member) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(member);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void update(Member member) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.update(member);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(String memberId) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Member m = (Member) session.get(Member.class, memberId);
            if (m != null) {
                session.delete(m);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Member findById(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return (Member) session.get(Member.class, memberId);
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<Member> findAll() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery("FROM Member").list();
        } finally {
            if (session != null) session.close();
        }
    }

   @Override
public boolean authenticate(String memberId, String password) throws Exception {

    Session session = null;

    try {
        session = HibernateUtil.getSessionFactory().openSession();

        Query query = session.createQuery(
            "FROM Member WHERE memberId = :mid AND password = :pwd"
        );
        query.setParameter("mid", memberId);
        query.setParameter("pwd", password);

        return query.uniqueResult() != null;

    } finally {
        if (session != null) {
            session.close();
        }
    }
}


    }

