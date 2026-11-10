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
        String query = request.getParameter("query");

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (query == null || query.trim().isEmpty()) {
            return;
        }

        String start = query.trim().toLowerCase();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<String> artists = session.createQuery(
                            "select a.name from Artist a where lower(a.name) like :prefix order by a.name", String.class)
                    .setParameter("prefix", start + "%")
                    .setMaxResults(10)
                    .list();

            List<String> albums = session.createQuery(
                            "select a.name from Album a where lower(a.name) like :prefix order by a.name", String.class)
                    .setParameter("prefix", start + "%")
                    .setMaxResults(10)
                    .list();

            int count = 0;
            for (String name : artists) {
                if (count > 0) out.print("\n");
                out.print(name);
                count++;
            }

            for (String name : albums) {
                if (!artists.contains(name)) {
                    if (count > 0) out.print("\n");
                    out.print(name);
                    count++;
                }
                if (count >= 10) break;
            }

        } finally {
            session.close();
        }
    }
}