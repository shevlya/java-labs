import java.util.List;

public class Main {
    public static void main(String[] args) {
        MusicStoreOperations musicStoreOperations = new MusicStoreOperations();

        System.out.println("Вывод названия альбома и самой короткой композиции среди всех \n" +
                "композиций для этого альбома, исключая композиции, для которых данное число менее 5");
        List<String> albums = musicStoreOperations.getAlbumsAndShortestTracks();
        if (albums.isEmpty()) {
            System.out.println("Такие альбомы не найдены");
        } else {
            for (String album : albums) {
                System.out.println(" " + album);
            }
        }

        System.out.println();
        System.out.println("CRUD-операции для таблицы Композиций");

        System.out.println("До операций таблица Композиций");
        for (String composition : musicStoreOperations.getAllCompositions()) {
            System.out.println(" " + composition);
        }
        System.out.println();

        System.out.println(musicStoreOperations.addComposition("Tests", 10, 1));

        int newCompositionId = musicStoreOperations.getLastCompositionId();

        if (newCompositionId > 0) {
            System.out.println(musicStoreOperations.updateCompositionDuration(newCompositionId, 6));
            System.out.println(musicStoreOperations.deleteComposition(newCompositionId));
        } else {
            System.out.println("Ошибка получения ID новой композиции");
        }

        System.out.println("После операций таблица Композиций");
        for (String composition : musicStoreOperations.getAllCompositions()) {
            System.out.println(" " + composition);
        }
    }
}