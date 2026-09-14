import java.util.List;

public class Main {

    public static void main(String[] args) {
        MusicStoreOperations musicStoreOperations = new MusicStoreOperations();

        System.out.println("Список композиций до операций:");
        for (String composition : musicStoreOperations.getAllCompositions()) {
            System.out.println(" " + composition);
        }
        System.out.println();

        System.out.println("Добавление композиции " + musicStoreOperations.addComposition("Test", 10, 1));

        int newCompositionId = musicStoreOperations.getLastCompositionId();

        printCompositions(musicStoreOperations);

        System.out.println("Изменение длительности композиции " + musicStoreOperations.updateCompositionDuration(newCompositionId, 6));
        printCompositions(musicStoreOperations);


        System.out.println("Удаление композиции " + musicStoreOperations.deleteComposition(newCompositionId));
        printCompositions(musicStoreOperations);

        System.out.println("Получить альбомы с самыми короткими композициями в них, " +
                "исключая альбомы, где минимальная длительность менее 5: ");

        List<String> albums = musicStoreOperations.getAlbumsAndShortestTracks();
        for (String album : albums) {
            System.out.println(" " + album);
        }
        System.out.println();
    }

    private static void printCompositions(MusicStoreOperations musicStoreOperations) {
        System.out.println("Список композиций:");
        for (String composition : musicStoreOperations.getAllCompositions()) {
            System.out.println(" " + composition);
        }
        System.out.println();
    }
}
