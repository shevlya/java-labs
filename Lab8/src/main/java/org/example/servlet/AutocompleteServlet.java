package org.example.servlet;

import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/autocomplete")
public class AutocompleteServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String queryText = request.getParameter("query");
        String type = request.getParameter("type");

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (queryText == null || queryText.trim().isEmpty() || type == null) {
            return;
        }

        String start = queryText.trim().toLowerCase();
        Session session = HibernateUtil.getSessionFactory().openSession();

        try {
            List<String> results;
            if ("artist".equalsIgnoreCase(type)) {
                results = session.createQuery(
                                "select a.name from Artist a where lower(a.name) like :prefix order by a.name", String.class)
                        .setParameter("prefix", start + "%")
                        .setMaxResults(10)
                        .list();
            } else if ("album".equalsIgnoreCase(type)) {
                results = session.createQuery(
                                "select a.name from Album a where lower(a.name) like :prefix order by a.name", String.class)
                        .setParameter("prefix", start + "%")
                        .setMaxResults(10)
                        .list();
            } else {
                results = java.util.Collections.emptyList();
            }

            for (int i = 0; i < results.size(); i++) {
                if (i > 0) {
                    out.print("\n");
                }
                out.print(results.get(i));
            }

        } finally {
            session.close();
        }
    }
}
