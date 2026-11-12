package org.example;

import org.example.entity.Album;
import org.example.entity.Artist;
import org.example.entity.Composition;
import org.example.util.HibernateUtil;
import org.hibernate.Session;

import java.util.List;

public class Main {
    private static final String ROW = "%-3s %-28s %-5s %-26s %-24s%n";

    public static void main(String[] args) {
        Session session = HibernateUtil.getSessionFactoryForInit().openSession();
        System.out.println("Подключение к БД есть, таблицы созданы заново");

        try {
            fillTables(session);

            System.out.println("\nВывод всех композиций");
            printCompositions(session);

            Album album = session.createQuery("from Album order by id", Album.class)
                    .setMaxResults(1)
                    .uniqueResult();

            session.beginTransaction();
            Composition song = new Composition("Новая композиция", 7, album);
            session.save(song);
            session.getTransaction().commit();
            System.out.println("\nДобавление композиции с id = " + song.getId());
            printCompositions(session);

            session.beginTransaction();
            song.setName("Новая композиция (изменена)");
            song.setDuration(9);
            session.getTransaction().commit();
            System.out.println("\nРедактирование композиции с id = " + song.getId());
            printCompositions(session);

            session.beginTransaction();
            session.delete(song);
            session.getTransaction().commit();
            System.out.println("\nУдаление композиции с id = " + song.getId());
            printCompositions(session);

        } catch (Exception e) {
            if (session.getTransaction().isActive()) {
                session.getTransaction().rollback();
            }
            System.out.println("Ошибка: " + e.getMessage());
        } finally {
            session.close();
            HibernateUtil.shutdown();
        }
    }

    private static void fillTables(Session session) {
        Long count = session.createQuery("select count(a) from Artist a", Long.class).uniqueResult();
        if (count > 0) {
            System.out.println("В таблицах уже есть данные, заполнять не нужно.");
            return;
        }

        session.beginTransaction();

        Artist bach = saveArtist(session, "Бах Иоганн Себастьян");
        Artist vivaldi = saveArtist(session, "Вивальди Антонио");
        Artist mozart = saveArtist(session, "Моцарт Вольфганг Амадей");
        Artist tchaikovsky = saveArtist(session, "Чайковский Пётр Ильич");

        Album brandenburg = saveAlbum(session, "Бранденбургские концерты", "Барокко", bach);
        Album fourSeasons = saveAlbum(session, "Времена года", "Барокко", vivaldi);
        Album symphony40 = saveAlbum(session, "Симфония №40", "Классицизм", mozart);
        Album serenade = saveAlbum(session, "Маленькая ночная серенада", "Классицизм", mozart);
        Album childAlbum = saveAlbum(session, "Детский альбом", "Романтизм", tchaikovsky);

        saveComposition(session, "Концерт №1. Алегро", 4, brandenburg);
        saveComposition(session, "Концерт №2. Анданте", 6, brandenburg);
        saveComposition(session, "Концерт №3. Алегро", 5, brandenburg);

        saveComposition(session, "Весна. Алегро", 3, fourSeasons);
        saveComposition(session, "Лето. Престо", 3, fourSeasons);
        saveComposition(session, "Зима. Алегро", 4, fourSeasons);

        saveComposition(session, "Молто аллегро", 3, symphony40);
        saveComposition(session, "Финале. Престо", 2, symphony40);

        saveComposition(session, "Алегро", 6, serenade);
        saveComposition(session, "Романс. Анданте", 5, serenade);

        saveComposition(session, "Утренняя молитва", 2, childAlbum);
        saveComposition(session, "Марш деревянных солдатиков", 2, childAlbum);

        session.getTransaction().commit();
        System.out.println("Таблицы заполнены");
    }

    private static Artist saveArtist(Session session, String name) {
        Artist artist = new Artist(name);
        session.save(artist);
        return artist;
    }

    private static Album saveAlbum(Session session, String name, String genre, Artist artist) {
        Album album = new Album(name, genre, artist);
        session.save(album);
        return album;
    }

    private static void saveComposition(Session session, String name, int duration, Album album) {
        session.save(new Composition(name, duration, album));
    }

    private static void printCompositions(Session session) {
        List<Composition> list = session.createQuery("from Composition order by id", Composition.class).list();

        System.out.printf(ROW, "ID", "Композиция", "Мин.", "Альбом", "Исполнитель");
        for (Composition c : list) {
            System.out.printf(ROW, c.getId(), c.getName(), c.getDuration(),
                    c.getAlbum().getName(), c.getAlbum().getArtist().getName());
        }
        System.out.println("Всего композиций: " + list.size());
    }
}
