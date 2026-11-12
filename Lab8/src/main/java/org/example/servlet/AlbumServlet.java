package org.example.servlet;

import org.example.entity.Album;
import org.example.entity.Artist;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/albums")
public class AlbumServlet extends BaseServlet {

    private static final String PAGE = "albums.jsp";
    private static final String HQL_ALL = "from Album order by id";
    private static final String HQL_ARTIST_BY_NAME = "from Artist where name = :name";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        showPage(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = param(request, "action");
        Session session = openSession();

        try {
            session.beginTransaction();

            if ("add".equals(action) || "update".equals(action)) {
                String name = param(request, "name");
                String genre = param(request, "genre");
                String artistName = param(request, "artist");
                if (name.isEmpty() || genre.isEmpty() || artistName.isEmpty()) {
                    throw new Exception("Заполните все поля");
                }
                Artist artist = findArtist(session, artistName);
                if (artist == null) {
                    throw new Exception("Исполнитель " + artistName + " не найден в БД");
                }
                if ("add".equals(action)) {
                    session.save(new Album(name, genre, artist));
                } else {
                    Album album = session.get(Album.class, intParam(request, "id"));
                    if (album == null) {
                        throw new Exception("Альбом не найден");
                    }
                    album.setName(name);
                    album.setGenre(genre);
                    album.setArtist(artist);
                }
            }
            if ("delete".equals(action)) {
                Album album = session.get(Album.class, intParam(request, "id"));
                if (album != null) {
                    session.delete(album);
                }
            }
            session.getTransaction().commit();
            response.sendRedirect("albums");
        } catch (Exception e) {
            rollbackWithError(session, request, e);
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private Artist findArtist(Session session, String name) {
        return session.createQuery(HQL_ARTIST_BY_NAME, Artist.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = openSession();
        try {
            request.setAttribute("albums", session.createQuery(HQL_ALL, Album.class).list());
            if (!param(request, "edit").isEmpty()) {
                request.setAttribute("editAlbum", session.get(Album.class, intParam(request, "edit")));
            }
            forward(request, response, PAGE);
        } finally {
            session.close();
        }
    }
}
