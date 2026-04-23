package config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Load hibernate.cfg.xml from classpath
            return new Configuration()
                    .configure("hibernate.cfg.xml")
                    .buildSessionFactory();

        } catch (Throwable ex) {
            System.err.println("❌ Initial SessionFactory creation failed.");
            ex.printStackTrace();
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Provide global access point
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Close caches and connection pools
    public static void shutdown() {
        getSessionFactory().close();
    }
}
