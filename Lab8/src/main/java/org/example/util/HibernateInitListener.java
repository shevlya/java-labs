package org.example.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class HibernateInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        sce.getServletContext().log("Инициализация Hibernate при старте приложения");
        try {
            HibernateUtil.getSessionFactory();
            sce.getServletContext().log("Hibernate инициализирован: схема БД проверена (hbm2ddl.auto=validate)");
        } catch (RuntimeException e) {
            sce.getServletContext().log("Не удалось инициализировать Hibernate", e);
            throw e;
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().log("Закрытие SessionFactory Hibernate");
        HibernateUtil.shutdown();
    }
}
