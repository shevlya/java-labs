import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlErrorHandler implements ErrorHandler {
    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw new SAXException("не соответствует DTD: " + exception.getMessage(), exception);
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new SAXException("критическая ошибка XML: " + exception.getMessage(), exception);
    }

    @Override
    public void warning(SAXParseException exception) {
        System.err.println("предупреждение XML: " + exception.getMessage());
    }
}
