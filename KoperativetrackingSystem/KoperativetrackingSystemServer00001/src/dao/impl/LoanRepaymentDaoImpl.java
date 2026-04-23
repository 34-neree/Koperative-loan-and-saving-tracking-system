package dao.impl;

import dao.LoanRepaymentDao;
import model.Loan;
import model.LoanRepayment;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

public class LoanRepaymentDaoImpl implements LoanRepaymentDao {

    @Override
    public void save(LoanRepayment repayment) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            session.save(repayment);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<LoanRepayment> findByLoan(int loanId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM LoanRepayment WHERE loan.loanId = :lid ORDER BY dueDate ASC")
                    .setParameter("lid", loanId)
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public List<LoanRepayment> findOverdue() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.createQuery(
                    "FROM LoanRepayment WHERE status = 'PENDING' AND dueDate < :now")
                    .setParameter("now", new Timestamp(System.currentTimeMillis()))
                    .list();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void markPaid(int repaymentId, double amountPaid) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            LoanRepayment r = (LoanRepayment) session.get(LoanRepayment.class, repaymentId);
            if (r != null) {
                r.setAmountPaid(r.getAmountPaid() + amountPaid);
                r.setPaidDate(new Timestamp(System.currentTimeMillis()));

                if (r.getAmountPaid() >= r.getAmountDue()) {
                    r.setStatus("PAID");
                } else {
                    r.setStatus("PARTIAL");
                }
                session.update(r);
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
    public void generateSchedule(int loanId, double totalPayable, int months) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Loan loan = (Loan) session.get(Loan.class, loanId);
            double monthlyInstallment = totalPayable / months;

            Calendar cal = Calendar.getInstance();

            for (int i = 1; i <= months; i++) {
                cal.add(Calendar.MONTH, 1);

                LoanRepayment r = new LoanRepayment();
                r.setLoan(loan);
                r.setDueDate(new Timestamp(cal.getTimeInMillis()));
                r.setAmountDue(monthlyInstallment);
                r.setAmountPaid(0);
                r.setStatus("PENDING");
                r.setPenaltyApplied(0);

                session.save(r);
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
