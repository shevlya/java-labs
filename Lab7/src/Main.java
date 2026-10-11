import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Укажите два аргумента: входной XML и выходной XML");
            System.exit(1);
        }

        Path inputPath = Paths.get(args[0]);
        Path outputPath = Paths.get(args[1]);

        if (!Files.exists(inputPath)) {
            System.out.println("Входной файл не найден: " + inputPath.toAbsolutePath());
            System.exit(1);
        }

        Operations.readXMLFile(inputPath, outputPath);
    }
}