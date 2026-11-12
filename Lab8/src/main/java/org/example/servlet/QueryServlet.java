package org.example.servlet;

import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/queries")
public class QueryServlet extends BaseServlet {

    private static final String HQL_BY_ARTIST =
            "select c.name, c.duration, a.name, a.genre, ar.name " +
                    "from Composition c join c.album a join a.artist ar " +
                    "where lower(ar.name) like lower(:name) " +
                    "order by ar.name, a.name, c.name";

    private static final String HQL_STATS =
            "select ar.name, count(distinct a.id), count(c.id), coalesce(sum(c.duration), 0) " +
                    "from Artist ar left join ar.albums a left join a.compositions c " +
                    "group by ar.id, ar.name " +
                    "order by ar.name";

    private static final String PAGE = "queries.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String artistName = param(request, "artist");
        Session session = openSession();

        try {
            if (!artistName.isEmpty()) {
                request.setAttribute("fullList", session.createQuery(HQL_BY_ARTIST, Object[].class)
                        .setParameter("name", "%" + artistName + "%")
                        .list());
            }
            request.setAttribute("stats", session.createQuery(HQL_STATS, Object[].class).list());
        } finally {
            session.close();
        }
        forward(request, response, PAGE);
    }
}
