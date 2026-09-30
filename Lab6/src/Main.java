import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        MusicStoreOperations musicStoreOperations = new MusicStoreOperations();

        try {
            printCompositions(musicStoreOperations);

            int newCompositionId = musicStoreOperations.addComposition("Test", 10, 1);
            System.out.println("Добавление композиции, ID: " + newCompositionId);
            printCompositions(musicStoreOperations);

            musicStoreOperations.updateCompositionDuration(newCompositionId, 6);
            System.out.println("Изменение длительности композиции");
            printCompositions(musicStoreOperations);

            musicStoreOperations.deleteComposition(newCompositionId);
            System.out.println("Удаление композиции");
            printCompositions(musicStoreOperations);

            System.out.println("Название альбома и самая короткая композиция среди всех композиций " +
                    "для этого альбома, исключая альбомы, где минимальная длительность менее 5:");

            List<String> albums = musicStoreOperations.getAlbumsAndShortestTracks();
            for (String album : albums) {
                System.out.println(" " + album);
            }
            System.out.println();
        } catch (SQLException e) {
            System.err.println("Ошибка работы с базой данных: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printCompositions(MusicStoreOperations musicStoreOperations) throws SQLException {
        System.out.println("Список композиций:");
        for (String composition : musicStoreOperations.getAllCompositions()) {
            System.out.println(" " + composition);
        }
        System.out.println();
    }
}
