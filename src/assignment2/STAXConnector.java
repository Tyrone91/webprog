package assignment2;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;

public class STAXConnector implements XMLReaderWrapper {
    
    private CallbackHolder callbacks;
    private Map<Integer,Consumer<XMLStreamReader>> eventMap;
    
    public STAXConnector() {
        this.callbacks = new CallbackHolder();
        this.eventMap = new HashMap<>();
        initEventMap();
    }

    @Override
    public void onElementStart(Consumer<String> callback) {
        this.callbacks.onElementStart(callback);
    }

    @Override
    public void onElementEnd(Consumer<String> callback) {
        this.callbacks.onElementEnd(callback);
    }

    @Override
    public void onText(Consumer<String> callback) {
        this.callbacks.onText(callback);
    }
    
    private STAXConnector on(int event, Consumer<XMLStreamReader> callback) {
        this.eventMap.put(event, callback);
        return this;
    }
    
    private void call(int event, XMLStreamReader reader) {
        Consumer<XMLStreamReader> callback = this.eventMap.get(event);
        if(callback == null) {
            //System.out.println("Log unknown event: " + event);
            return;
        }
        callback.accept(reader);
    }
    
    private void initEventMap() {
        on(XMLEvent.START_ELEMENT, this::readElementStart);
        on(XMLEvent.END_ELEMENT, this::readElementEnd);
        on(XMLEvent.CHARACTERS, this::readCharacters);
    }
    
    private void readElementStart(XMLStreamReader reader) {
        String name = reader.getLocalName();
        this.callbacks.fireElementStart(name);
    }
    
    private void readElementEnd(XMLStreamReader reader) {
        String name = reader.getLocalName();
        this.callbacks.fireElementEnd(name);
    }
    
    private void readCharacters(XMLStreamReader reader) {
        String text = reader.getText();
        this.callbacks.fireText(text);
    }
    
    @Override
    public void start(InputStream inputstream) {
        try{
            final XMLInputFactory factory = XMLInputFactory.newFactory();
            XMLStreamReader reader = factory.createXMLStreamReader(inputstream);
            
            while(reader.hasNext()) {
                call(reader.next(), reader);
            }
        } catch(Exception ex) {
            System.out.println("exception3");
            ex.printStackTrace(System.err);
        }
    }
}
