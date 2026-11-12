package org.example.servlet;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServlet extends HttpServlet {

    protected static final String JSP_PATH = "/WEB-INF/jsp/";

    protected Session openSession() {
        return HibernateUtil.getSessionFactory().openSession();
    }

    protected String param(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        return value == null ? "" : value.trim();
    }

    protected int intParam(HttpServletRequest request, String name) {
        try {
            return Integer.parseInt(param(request, name));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Некорректное значение параметра " + name);
        }
    }

    protected void rollback(Session session) {
        if (session.getTransaction() != null && session.getTransaction().isActive()) {
            session.getTransaction().rollback();
        }
    }

    protected void rollbackWithError(Session session, HttpServletRequest request, Exception e) {
        rollback(session);
        request.setAttribute("error", "Ошибка: " + e.getMessage());
    }

    protected void forward(HttpServletRequest request, HttpServletResponse response, String page)
            throws ServletException, IOException {
        request.getRequestDispatcher(JSP_PATH + page).forward(request, response);
    }
}
