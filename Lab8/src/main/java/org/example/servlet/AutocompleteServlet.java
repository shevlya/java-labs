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
        // 1. Читаем параметр 'query', который отправляет наш JS
        String query = request.getParameter("query");

        response.setContentType("text/plain; charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (query == null || query.trim().isEmpty()) {
            return;
        }

        String start = query.trim().toLowerCase();

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            // 2. Ищем исполнителей, чье имя начинается с введенных букв
            List<String> artists = session.createQuery(
                            "select a.name from Artist a where lower(a.name) like :prefix order by a.name", String.class)
                    .setParameter("prefix", start + "%")
                    .setMaxResults(10) // Ограничиваем выдачу 10 вариантами
                    .list();

            // 3. Ищем альбомы, чье название начинается с введенных букв
            List<String> albums = session.createQuery(
                            "select a.name from Album a where lower(a.name) like :prefix order by a.name", String.class)
                    .setParameter("prefix", start + "%")
                    .setMaxResults(10)
                    .list();

            // 4. Формируем ответ, разделяя значения переносом строки (\n)
            int count = 0;
            for (String name : artists) {
                if (count > 0) out.print("\n");
                out.print(name);
                count++;
            }

            for (String name : albums) {
                // Избегаем дубликатов, если имя альбома случайно совпало с именем артиста
                if (!artists.contains(name)) {
                    if (count > 0) out.print("\n");
                    out.print(name);
                    count++;
                }
                if (count >= 10) break; // Общее ограничение в 10 подсказок
            }

        } finally {
            session.close();
        }
    }
}