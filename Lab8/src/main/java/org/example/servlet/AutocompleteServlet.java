package org.example.servlet;

import org.hibernate.Session;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/autocomplete")
public class AutocompleteServlet extends BaseServlet {
    private static final int MAX_RESULTS = 10;

    private static final String HQL_ARTIST =
            "select a.name from Artist a where lower(a.name) like :prefix order by a.name";
    private static final String HQL_ALBUM =
            "select a.name from Album a where lower(a.name) like :prefix order by a.name";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        String query = param(request, "query");
        String type = param(request, "type");
        if (query.isEmpty()) {
            return;
        }
        String hql;
        if ("artist".equalsIgnoreCase(type)) {
            hql = HQL_ARTIST;
        } else if ("album".equalsIgnoreCase(type)) {
            hql = HQL_ALBUM;
        } else {
            return;
        }
        Session session = openSession();
        try {
            List<String> results = session.createQuery(hql, String.class)
                    .setParameter("prefix", query.toLowerCase() + "%")
                    .setMaxResults(MAX_RESULTS)
                    .list();
            response.getWriter().print(String.join("\n", results));
        } finally {
            session.close();
        }
    }
}
