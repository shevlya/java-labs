package org.example.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class HibernateInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Инициализация Hibernate при старте сервера");
        HibernateUtil.getSessionFactory();
        System.out.println("Hibernate инициализирован");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Закрытие Hibernate");
        HibernateUtil.shutdown();
    }
}
