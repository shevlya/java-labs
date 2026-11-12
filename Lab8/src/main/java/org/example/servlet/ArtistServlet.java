package org.example.servlet;

import org.example.entity.Artist;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/artists")
public class ArtistServlet extends BaseServlet {

    private static final String PAGE = "artists.jsp";
    private static final String HQL_ALL = "from Artist order by id";
    private static final String HQL_BY_NAME = "from Artist where lower(name) = lower(:name)";
    private static final String HQL_BY_NAME_OTHER_ID =
            "from Artist where lower(name) = lower(:name) and id != :id";

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
                if (name.isEmpty()) {
                    throw new Exception("Введите имя исполнителя");
                }
                if ("add".equals(action)) {
                    checkNameUnique(session, name, null);
                    session.save(new Artist(name));
                } else {
                    int id = intParam(request, "id");
                    Artist artist = session.get(Artist.class, id);
                    if (artist == null) {
                        throw new Exception("Исполнитель не найден");
                    }
                    checkNameUnique(session, name, id);
                    artist.setName(name);
                }
            }
            if ("delete".equals(action)) {
                Artist artist = session.get(Artist.class, intParam(request, "id"));
                if (artist != null) {
                    session.delete(artist);
                }
            }
            session.getTransaction().commit();
            response.sendRedirect("artists");
        } catch (Exception e) {
            rollbackWithError(session, request, e);
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private void checkNameUnique(Session session, String name, Integer excludeId) throws Exception {
        Query<Artist> query;
        if (excludeId == null) {
            query = session.createQuery(HQL_BY_NAME, Artist.class);
        } else {
            query = session.createQuery(HQL_BY_NAME_OTHER_ID, Artist.class)
                    .setParameter("id", excludeId);
        }
        query.setParameter("name", name);
        if (query.setMaxResults(1).uniqueResult() != null) {
            throw new Exception("Исполнитель с именем " + name + " уже существует");
        }
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = openSession();
        try {
            request.setAttribute("artists", session.createQuery(HQL_ALL, Artist.class).list());
            if (!param(request, "edit").isEmpty()) {
                request.setAttribute("editArtist", session.get(Artist.class, intParam(request, "edit")));
            }
            forward(request, response, PAGE);
        } finally {
            session.close();
        }
    }
}
