package dao.impl;

import dao.LoanDao;
import model.Loan;
import model.Member;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class LoanDaoImpl implements LoanDao {

    @Override
    public void requestLoan(Loan loan) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(loan);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void approveLoan(String memberId) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Loan loan = (Loan) session.createQuery(
                    "FROM Loan WHERE member.memberId = :mid AND status='PENDING'")
                    .setParameter("mid", memberId)
                    .uniqueResult();

            if (loan != null) {
                loan.setStatus("APPROVED");
                session.update(loan);

                Member m = loan.getMember();
                m.setLoanBalance(m.getLoanBalance() + loan.getOutstanding());
                session.update(m);
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
    public void repayLoan(String memberId, double amount) {
        Session session = null;
        Transaction tx = null;

        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Loan loan = (Loan) session.createQuery(
                    "FROM Loan WHERE member.memberId = :mid AND status='APPROVED'")
                    .setParameter("mid", memberId)
                    .uniqueResult();

            if (loan != null) {
                loan.setOutstanding(loan.getOutstanding() - amount);
                session.update(loan);

                Member m = loan.getMember();
                m.setLoanBalance(m.getLoanBalance() - amount);
                session.update(m);
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
    public List<Loan> findByMember(String memberId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM Loan WHERE member.memberId = :mid")
                    .setParameter("mid", memberId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }
}
