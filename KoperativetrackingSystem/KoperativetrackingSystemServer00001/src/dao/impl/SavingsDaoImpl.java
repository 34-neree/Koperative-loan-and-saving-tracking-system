package dao.impl;

import dao.SavingsDao;
import model.Member;
import model.Savings;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class SavingsDaoImpl implements SavingsDao {

    @Override
    public void deposit(Savings savings) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            session.save(savings);

            Member member = (Member) session.get(
                    Member.class,
                    savings.getMember().getMemberId()
            );

            member.setTotalSavings(member.getTotalSavings() + savings.getAmount());
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
    public List<Savings> findByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Savings WHERE member.memberId = :mid")
                    .setParameter("mid", memberId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }
}
