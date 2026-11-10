package org.example.servlet;

import org.example.entity.Artist;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/artists")
public class ArtistServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showPage(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();

            if ("add".equals(action)) {
                String name = request.getParameter("name").trim();
                if (name.isEmpty()) throw new Exception("Введите имя исполнителя");
                session.save(new Artist(name));
            }

            if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                String name = request.getParameter("name").trim();
                Artist artist = session.get(Artist.class, id);
                if (artist == null) throw new Exception("Исполнитель не найден");
                artist.setName(name);
                session.update(artist);
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Artist artist = session.get(Artist.class, id);
                if (artist != null) {
                    // ЗДЕСЬ СРАБОТАЕТ КАСКАДНОЕ УДАЛЕНИЕ!
                    // Hibernate сам удалит все альбомы и композиции этого артиста.
                    session.delete(artist);
                }
            }

            session.getTransaction().commit();
            response.sendRedirect("artists"); // Перезагрузка страницы после POST

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            request.setAttribute("error", "Ошибка: " + e.getMessage());
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Artist> artists = session.createQuery("from Artist order by id", Artist.class).list();
            request.setAttribute("artists", artists);

            String edit = request.getParameter("edit");
            if (edit != null) {
                request.setAttribute("editArtist", session.get(Artist.class, Integer.parseInt(edit)));
            }
            request.getRequestDispatcher("/WEB-INF/jsp/artists.jsp").forward(request, response);
        } finally {
            session.close();
        }
    }
}
