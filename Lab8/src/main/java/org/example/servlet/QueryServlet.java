package org.example.servlet;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/queries")
public class QueryServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // Запрос 1 (3 таблицы): Все композиции с указанием альбома, жанра и исполнителя
            List<Object[]> fullList = session.createQuery(
                            "select c.name, c.duration, a.name, a.genre, ar.name " +
                                    "from Composition c join c.album a join a.artist ar " +
                                    "order by ar.name, a.name, c.name", Object[].class)
                    .list();
            request.setAttribute("fullList", fullList);

            // Запрос 2 (3 таблицы): Статистика по исполнителям
            // Используем left join, чтобы показать даже тех исполнителей, у которых пока нет альбомов
            List<Object[]> stats = session.createQuery(
                            "select ar.name, count(distinct a.id), count(c.id), sum(c.duration) " +
                                    "from Artist ar " +
                                    "left join ar.albums a " +
                                    "left join a.compositions c " +
                                    "group by ar.id, ar.name " +
                                    "order by ar.name", Object[].class)
                    .list();
            request.setAttribute("stats", stats);

        } finally {
            session.close();
        }
        request.getRequestDispatcher("/WEB-INF/jsp/queries.jsp").forward(request, response);
    }
}
