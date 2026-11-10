package org.example.servlet;

import org.example.entity.Album;
import org.example.entity.Composition;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/compositions")
public class CompositionServlet extends HttpServlet {

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
                String albumName = request.getParameter("album").trim();
                int duration = Integer.parseInt(request.getParameter("duration").trim());

                if (name.isEmpty() || albumName.isEmpty() || duration <= 0) {
                    throw new Exception("Проверьте правильность заполнения полей");
                }

                Album album = findAlbum(session, albumName);
                if (album == null) throw new Exception("Альбом «" + albumName + "» не найден");

                if ("add".equals(action)) {
                    Composition comp = new Composition(name, duration);
                    comp.setAlbum(album);
                    session.save(comp);
                } else {
                    int id = Integer.parseInt(request.getParameter("id"));
                    Composition comp = session.get(Composition.class, id);
                    if (comp == null) throw new Exception("Композиция не найдена");
                    comp.setName(name);
                    comp.setDuration(duration);
                    comp.setAlbum(album);
                    session.update(comp);
                }
            }

            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                Composition comp = session.get(Composition.class, id);
                if (comp != null) session.delete(comp);
            }

            session.getTransaction().commit();
            response.sendRedirect("compositions");

        } catch (Exception e) {
            if (session.getTransaction().isActive()) session.getTransaction().rollback();
            request.setAttribute("error", "Ошибка: " + e.getMessage());
            showPage(request, response);
        } finally {
            session.close();
        }
    }

    private Album findAlbum(Session session, String name) {
        List<Album> list = session.createQuery("from Album where name = :name", Album.class)
                .setParameter("name", name).list();
        return list.isEmpty() ? null : list.get(0);
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            List<Composition> compositions = session.createQuery("from Composition order by id", Composition.class).list();
            request.setAttribute("compositions", compositions);

            String edit = request.getParameter("edit");
            if (edit != null) {
                request.setAttribute("editComposition", session.get(Composition.class, Integer.parseInt(edit)));
            }
            request.getRequestDispatcher("/WEB-INF/jsp/compositions.jsp").forward(request, response);
        } finally {
            session.close();
        }
    }
}
