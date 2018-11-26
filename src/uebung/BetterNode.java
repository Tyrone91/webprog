package uebung;

import org.w3c.dom.Node;

public class BetterNode {
    
    private Node source;
    
    public BetterNode(Node source) {
        this.source = source;
    }
    
    public Node source() {
        return this.source;
    }
    
    public Nodes children() {
        return Nodes.of(this.source.getChildNodes());
    }
    
    public String attribute(final String name) {
        if(this.source.getAttributes().getNamedItem(name) == null) {
            return "";
        }
        return this.source.getAttributes().getNamedItem(name).getNodeValue();
    }
    
    Nodes findElementsByTag(final String tag) {
        Node[] res = children()
                .stream()
                .filter( n -> n.getNodeName().equals(tag))
                .toArray( size -> new Node[size]);
        
        return Nodes.of(res);
    }
    
    Node findElementByTag(final String tag) {
        return children()
            .stream()
            .filter( n -> n.getNodeName().equals(tag))
            .findFirst()
            .orElse(null);
    }
    
    public static BetterNode of(Node n) {
        return new BetterNode(n);
    }
}
