package org.example.servlet;

import org.example.entity.Album;
import org.example.entity.Composition;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/compositions")
public class CompositionServlet extends BaseServlet {

    private static final String PAGE = "compositions.jsp";
    private static final String HQL_ALL = "from Composition order by id";
    private static final String HQL_ALBUM_BY_NAME = "from Album where name = :name";

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
                String albumName = param(request, "album");
                int duration = intParam(request, "duration");

                if (name.isEmpty() || albumName.isEmpty() || duration <= 0) {
                    throw new Exception("Проверьте правильность заполнения полей");
                }
                Album album = findAlbum(session, albumName);
                if (album == null) {
                    throw new Exception("Альбом " + albumName + " не найден");
                }
                if ("add".equals(action)) {
                    session.save(new Composition(name, duration, album));
                } else {
                    Composition comp = session.get(Composition.class, intParam(request, "id"));
                    if (comp == null) {
                        throw new Exception("Композиция не найдена");
                    }
                    comp.setName(name);
                    comp.setDuration(duration);
                    comp.setAlbum(album);
                }
            }
            if ("delete".equals(action)) {
                Composition comp = session.get(Composition.class, intParam(request, "id"));
                if (comp != null) {
                    session.delete(comp);
                }
            }
            session.getTransaction().commit();
            response.sendRedirect("compositions");
        } catch (Exception e) {
            rollbackWithError(session, request, e);
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private Album findAlbum(Session session, String name) {
        return session.createQuery(HQL_ALBUM_BY_NAME, Album.class)
                .setParameter("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = openSession();
        try {
            request.setAttribute("compositions", session.createQuery(HQL_ALL, Composition.class).list());
            if (!param(request, "edit").isEmpty()) {
                request.setAttribute("editComposition",
                        session.get(Composition.class, intParam(request, "edit")));
            }
            forward(request, response, PAGE);
        } finally {
            session.close();
        }
    }
}
