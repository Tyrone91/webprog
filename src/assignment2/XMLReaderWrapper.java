package assignment2;

import java.io.InputStream;
import java.util.function.Consumer;

/**
 * Unified reader interface for the different XML reader implementation.
 * The corresponding classes will implement this interface to the {@link RssReader} only needs on implementation. 
 */
public interface XMLReaderWrapper {
    
    public void onElementStart(Consumer<String> callback);
    public void onElementEnd(Consumer<String> callback);
    
    public void onText(Consumer<String> callback);
    
    public void start(InputStream inputstream);
}
