package assignment2;

import java.util.function.Consumer;

/**
 * Utility object to hold the various callback functions
 */
public class CallbackHolder {
    
    private Consumer<String> onElementStartCallback = str -> {};
    private Consumer<String> onElementEndCallback = str -> {};
    private Consumer<String> onTextCallback = str -> {};

    public void onElementStart(Consumer<String> callback) {
        this.onElementStartCallback =  callback;
    }

    public void onElementEnd(Consumer<String> callback) { 
        this.onElementEndCallback = callback;
    }

    public void onText(Consumer<String> callback) {
        this.onTextCallback = callback;
    }
    
    public void fireElementStart(String elementName) {
        this.onElementStartCallback.accept(elementName);
    }
    
    public void fireElementEnd(String elementName) {
        this.onElementEndCallback.accept(elementName);
    }
    
    public void fireText(String text) {
        this.onTextCallback.accept(text);
    }
    
}
