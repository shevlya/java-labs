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
            String artistName = request.getParameter("artist");

            if (artistName != null && !artistName.trim().isEmpty()) {
                List<Object[]> fullList = session.createQuery(
                                "select c.name, c.duration, a.name, a.genre, ar.name " +
                                        "from Composition c join c.album a join a.artist ar " +
                                        "where lower(ar.name) = lower(:name) " +
                                        "order by ar.name, a.name, c.name", Object[].class)
                        .setParameter("name", artistName.trim())
                        .list();
                request.setAttribute("fullList", fullList);
            }

            List<Object[]> stats = session.createQuery(
                            "select ar.name, count(distinct a.id), count(c.id), coalesce(sum(c.duration), 0) " +
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
