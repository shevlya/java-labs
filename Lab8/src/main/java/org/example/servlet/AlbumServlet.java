package org.example.servlet;

import org.example.entity.Album;
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

@WebServlet("/albums")
public class AlbumServlet extends HttpServlet {

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

            if ("add".equals(action) || "update".equals(action)) {
                String name = request.getParameter("name").trim();
                String genre = request.getParameter("genre").trim();
                String artistName = request.getParameter("artist").trim();

                if (name.isEmpty() || genre.isEmpty() || artistName.isEmpty()) {
                    throw new Exception("Заполните все поля");
                }

                Artist artist = findArtist(session, artistName);
                if (artist == null) {
                    throw new Exception("Исполнитель «" + artistName + "» не найден в БД");
                }

                if ("add".equals(action)) {
                    Album album = new Album(name, genre);
                    album.setArtist(artist);
                    session.save(album);
                } else {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Album album = session.get(Album.class, id);
                    if (album == null) throw new Exception("Альбом не найден");
                    album.setName(name);
                    album.setGenre(genre);
                    album.setArtist(artist);
                    session.update(album);
                }
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Album album = session.get(Album.class, id);
                if (album != null) session.delete(album); // Каскадом удалит композиции
            }

            session.getTransaction().commit();
            response.sendRedirect("albums");

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            request.setAttribute("error", "Ошибка: " + e.getMessage());
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private Artist findArtist(Session session, String name) {
        List<Artist> list = session.createQuery("from Artist where name = :name", Artist.class)
                .setParameter("name", name).list();
        return list.isEmpty() ? null : list.get(0);
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Album> albums = session.createQuery("from Album order by id", Album.class).list();
            request.setAttribute("albums", albums);

            String edit = request.getParameter("edit");
            if (edit != null) {
                request.setAttribute("editAlbum", session.get(Album.class, Integer.parseInt(edit)));
            }
            request.getRequestDispatcher("/WEB-INF/jsp/albums.jsp").forward(request, response);
        } finally {
            session.close();
        }
    }
}
