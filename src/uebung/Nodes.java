package uebung;

import java.util.Iterator;
import java.util.stream.Stream;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Nodes implements Iterable<Node> {
    
    private Node[] nodes;
    
    public Nodes(NodeList source) {
        this.nodes = copyToArray(source);
    }
    
    public Nodes(Node[] nodes) {
        this.nodes = nodes;
    }
    
    
    public Stream<Node> stream() {
        return Stream.of(this.nodes);
    }

    @Override
    public Iterator<Node> iterator() {
        return new NodeIterator();
    }
    
    private Node[] copyToArray(NodeList list) {
        Node[] array = new Node[list.getLength()];
        for(int i = 0; i < array.length; ++i) {
            array[i] = list.item(i);
        }
        return array;
    }
    
    private class NodeIterator implements Iterator<Node> {
        
        private int index = 0;

        @Override
        public boolean hasNext() {
            return index < Nodes.this.nodes.length;
        }

        @Override
        public Node next() {
            return Nodes.this.nodes[index++];
        }
        
    }
    
    public static Nodes of(NodeList source) {
        return new Nodes(source);
    }
    
    public static Nodes of() {
        return new Nodes( new Node[0]);
    }
    
    public static Nodes of(Node...nodes ) {
        return new Nodes(nodes);
    }
    
    
}
