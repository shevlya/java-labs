package org.example;

import org.example.entity.Artist;
import org.example.entity.Album;
import org.example.entity.Composition;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        System.out.println("Подключение к базе данных установлено. Таблицы созданы (или уже существовали).\n");

        try {
            fillTables(session);

            System.out.println("Вывод всех композиций");
            printAllCompositions(session);

            Album firstAlbum = session.createQuery("from Album order by id", Album.class)
                    .setMaxResults(1)
                    .uniqueResult();

            session.beginTransaction();
            Composition newComposition = new Composition("Тестовая композиция", 7);
            newComposition.setAlbum(firstAlbum);
            session.save(newComposition);
            session.getTransaction().commit();
            System.out.println("\nДобавлена композиция с id = " + newComposition.getId() + " ");
            printAllCompositions(session);
        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }

    private static void fillTables(Session session) {
        System.out.println("Очистка и заполнение таблиц данными...");
        session.beginTransaction();

        session.createQuery("DELETE FROM Composition").executeUpdate();
        session.createQuery("DELETE FROM Album").executeUpdate();
        session.createQuery("DELETE FROM Artist").executeUpdate();
        session.getTransaction().commit();

        session.beginTransaction();

        Artist telemann = saveArtist(session, "Телеман Георг Филипп");
        Artist bach = saveArtist(session, "Бах Иоганн Себастьян");
        Artist mozart = saveArtist(session, "Моцарт Вольфганг Амадей");
        Artist beethoven = saveArtist(session, "Бетховен Людвиг");
        Artist vivaldi = saveArtist(session, "Вивальди Антонио ");

        Album tafelMusic = saveAlbum(session, "Tafelmusik", "Барокко", telemann);
        Album brandenburg = saveAlbum(session, "Бранденбургские концерты", "Барокко", bach);
        Album symphony40 = saveAlbum(session, "Симфония №40", "Классицизм", mozart);
        Album symphony5 = saveAlbum(session, "Симфония №5", "Классицизм", beethoven);
        Album fourSeasons = saveAlbum(session, "Времена года", "Барокко", vivaldi);
        Album sonatas = saveAlbum(session, "Сонаты и партиты", "Барокко", bach);

        saveComposition(session, "Увертюра ми-бемоль мажор", 6, tafelMusic);
        saveComposition(session, "Эйр ля минор", 5, tafelMusic);
        saveComposition(session, "Ригаудон до мажор", 7, tafelMusic);

        saveComposition(session, "Концерт №1 фа мажор", 4, brandenburg);
        saveComposition(session, "Концерт №2 фа мажор", 6, brandenburg);
        saveComposition(session, "Концерт №3 соль мажор", 5, brandenburg);

        saveComposition(session, "Молто аллегро", 3, symphony40);
        saveComposition(session, "Анданте", 4, symphony40);
        saveComposition(session, "Финале. Престо", 2, symphony40);

        saveComposition(session, "Алегро брио", 5, symphony5);
        saveComposition(session, "Анданте мото", 8, symphony5);
        saveComposition(session, "Алегро", 6, symphony5);

        saveComposition(session, "Весна", 10, fourSeasons);
        saveComposition(session, "Лето", 11, fourSeasons);
        saveComposition(session, "Осень", 9, fourSeasons);

        saveComposition(session, "Прелюдия соль минор", 4, sonatas);
        saveComposition(session, "Фуга соль минор", 6, sonatas);
        saveComposition(session, "Сицилиана соль минор", 3, sonatas);

        session.getTransaction().commit();
        System.out.println("Таблицы успешно заполнены.\n");
    }

    private static Artist saveArtist(Session session, String name) {
        Artist artist = new Artist(name);
        session.save(artist);
        return artist;
    }

    private static Album saveAlbum(Session session, String name, String genre, Artist artist) {
        Album album = new Album(name, genre);
        album.setArtist(artist);
        session.save(album);
        return album;
    }

    private static void saveComposition(Session session, String name, int duration, Album album) {
        Composition composition = new Composition(name, duration);
        composition.setAlbum(album);
        session.save(composition);
    }

    private static void printAllCompositions(Session session) {
        List<Composition> list = session.createQuery("from Composition order by id", Composition.class).list();

        System.out.printf("%-4s %-30s %-8s %-25s %-25s%n", "ID", "Название", "Мин.", "Альбом", "Исполнитель");
        for (Composition c : list) {
            System.out.printf("%-4d %-30s %-8d %-25s %-25s%n",
                    c.getId(),
                    c.getName(),
                    c.getDuration(),
                    c.getAlbum().getName(),
                    c.getAlbum().getArtist().getName());
        }
    }
}
