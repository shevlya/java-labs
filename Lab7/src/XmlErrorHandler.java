import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlErrorHandler implements ErrorHandler {
    @Override
    public void warning(SAXParseException exception) {
        System.err.println("Предупреждение XML: " + exception.getMessage());
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw new SAXException("Ошибка валидации XML, не соответствует DTD: " + exception.getMessage(), exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new SAXException("Критическая ошибка XML: " + exception.getMessage(), exception);
    }
}
