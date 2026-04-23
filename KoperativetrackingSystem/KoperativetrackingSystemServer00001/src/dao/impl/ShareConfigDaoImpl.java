package dao.impl;

import dao.ShareConfigDao;
import model.ShareConfig;
import config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ShareConfigDaoImpl implements ShareConfigDao {

    @Override
    public ShareConfig getConfig() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            ShareConfig config = (ShareConfig) session.createQuery("FROM ShareConfig")
                    .setMaxResults(1)
                    .uniqueResult();

            if (config == null) {
                // Create default config if none exists
                Transaction tx = session.beginTransaction();
                config = new ShareConfig();
                config.setSharePrice(5000.0);
                config.setMinimumSharesForLoan(5);
                config.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
                session.save(config);
                tx.commit();
            }
            return config;
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void updateConfig(ShareConfig config) {
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            config.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            session.update(config);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        } finally {
            if (session != null) session.close();
        }
    }
}
