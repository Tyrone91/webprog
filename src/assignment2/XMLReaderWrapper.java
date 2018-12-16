package assignment2;

import java.io.InputStream;
import java.util.function.Consumer;

public interface XMLReaderWrapper {
    
    public void onElementStart(Consumer<String> callback);
    public void onElementEnd(Consumer<String> callback);
    
    public void onText(Consumer<String> callback);
    
    public void start(InputStream inputstream);
}
