import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;

public class Operations {

    private static final String SUBJECT = "subject";
    private static final String MARK = "mark";
    private static final String AVERAGE = "average";

    public static void readXMLFile(Path inputPath, Path outPath) {
        try {
            Document doc = loadDocument(inputPath);
            BigDecimal correctAverage = calculateAverage(doc);
            if (correctAverage == null) {
                System.out.println("Предметы не найдены, средняя оценка не может быть вычислена");
                return;
            }
            updateAverageNode(doc, correctAverage);
            saveDocument(doc, outPath);
            System.out.println("Файл сохранен: " + outPath.toAbsolutePath());
        } catch (ParserConfigurationException ex) {
            System.err.println("Ошибка конфигурации парсера: " + ex.getMessage());
        } catch (SAXException ex) {
            System.err.println("Ошибка валидации XML: " + ex.getMessage());
        } catch (IOException ex) {
            System.err.println("Ошибка ввода-вывода при чтении XML: " + ex.getMessage());
        } catch (Exception e) {
            System.err.println("Произошла ошибка при обработке XML:");
            e.printStackTrace();
        }
    }

    private static Document loadDocument(Path inputPath) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new XmlErrorHandler());
        return builder.parse(inputPath.toFile());
    }

    private static BigDecimal calculateAverage(Document doc) {
        NodeList subjects = doc.getElementsByTagName(SUBJECT);

        BigDecimal sum = BigDecimal.ZERO;
        int counter = 0;

        for (int i = 0; i < subjects.getLength(); i++) {
            Element subject = (Element) subjects.item(i);
            String markStr = subject.getAttribute(MARK);
            try {
                sum = sum.add(new BigDecimal(markStr));
                counter++;
            } catch (NumberFormatException e) {
                System.err.printf("Неверный формат оценки '%s'\n", markStr);
            }
        }
        if (counter == 0) {
            System.out.printf("У студента нет оценок\n");
            return null;
        }
        return sum.divide(new BigDecimal(counter), 1, RoundingMode.HALF_UP);
    }

    private static void updateAverageNode(Document doc, BigDecimal correctAverage) {
        NodeList averages = doc.getElementsByTagName(AVERAGE);

        if (averages.getLength() > 0) {
            Node averageNode = averages.item(0);
            String currentText = averageNode.getTextContent().trim();

            try {
                BigDecimal currentAverage = new BigDecimal(currentText).setScale(1, RoundingMode.HALF_UP);
                if (correctAverage.compareTo(currentAverage) != 0) {
                    averageNode.setTextContent(correctAverage.toString());
                    System.out.printf("Средняя оценка скорректирована: было %s, стало %s\n", currentText, correctAverage);
                } else {
                    System.out.printf("Средняя оценка верна (%s), коррекция не нужна\n", currentAverage);
                }
            } catch (NumberFormatException e) {
                averageNode.setTextContent(correctAverage.toString());
                System.out.printf("Некорректное значение среднего '%s' заменено на %s\n", currentText, correctAverage);
            }
        } else {
            Element newAverage = doc.createElement(AVERAGE);
            newAverage.setTextContent(correctAverage.toString());
            doc.getDocumentElement().appendChild(newAverage);
            System.out.printf("Добавлен <average> со значением %s\n", correctAverage);
        }
    }

    private static void saveDocument(Document doc, Path outPath) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "student.dtd");
        transformer.transform(new DOMSource(doc), new StreamResult(outPath.toFile()));
    }
}
