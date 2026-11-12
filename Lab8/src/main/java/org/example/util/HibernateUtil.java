package org.example.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    private static SessionFactory buildSessionFactory(String hbm2ddlAuto) {
        try {
            Configuration configuration = new Configuration().configure();
            if (hbm2ddlAuto != null) {
                configuration.setProperty("hibernate.hbm2ddl.auto", hbm2ddlAuto);
            }
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Ошибка инициализации SessionFactory: " + ex);
            throw new IllegalStateException("Не удалось создать SessionFactory", ex);
        }
    }

    public static synchronized SessionFactory getSessionFactoryForInit() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory("create");
        }
        return sessionFactory;
    }

    public static synchronized SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = buildSessionFactory(null);
        }
        return sessionFactory;
    }

    public static synchronized void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
        sessionFactory = null;
    }
}
