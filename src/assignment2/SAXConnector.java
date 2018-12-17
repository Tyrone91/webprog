package assignment2;

import java.io.InputStream;
import java.util.function.Consumer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * SAX Version of a {@link XMLReaderWrapper}
 */
public class SAXConnector extends DefaultHandler implements XMLReaderWrapper {
    
    private CallbackHolder callbackHolder;
    
    public SAXConnector() {
        this.callbackHolder = new CallbackHolder();
    }

    @Override
    public void onElementStart(Consumer<String> callback) {
        this.callbackHolder.onElementStart(callback);
    }

    @Override
    public void onElementEnd(Consumer<String> callback) {
        this.callbackHolder.onElementEnd(callback);
    }

    @Override
    public void onText(Consumer<String> callback) {
        this.callbackHolder.onText(callback);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        this.callbackHolder.fireElementStart(qName);
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        this.callbackHolder.fireElementEnd(qName);
    }
    
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        final String str = new String(ch, start, length);
        this.callbackHolder.fireText(str);
    }
    
    @Override
    public void start(InputStream inputstream) {
        
        try {
            final SAXParserFactory factory = SAXParserFactory.newInstance();
            final SAXParser parser = factory.newSAXParser();
            final XMLReader reader = parser.getXMLReader();
            
            
            reader.setContentHandler(this);
            reader.parse( new InputSource(inputstream));
            
        } catch(Exception ex) {
            ex.printStackTrace(System.err);
        }
        
        
    }
}
