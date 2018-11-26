package uebung;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class uebung6 {
    
    public static class XPather {
        
        Document document;
        private XPath xPath;
        
        public XPather(Document doc) {
            this.document = doc;
            this.xPath  = XPathFactory.newInstance().newXPath();
        }
        
        public double asNumber(final String expression) {
            return (double)baseCall(expression, XPathConstants.NUMBER);
        }
        
        public double asBoolean(final String expression) {
            return (double)baseCall(expression, XPathConstants.BOOLEAN);
        }
        
        public NodeList asNodes(final String expression) {
            return (NodeList)baseCall(expression, XPathConstants.NODESET);
        }
        
        public NodeList asNode(final String expression) {
            return (NodeList)baseCall(expression, XPathConstants.NODE);
        }
        
        public String asText(final String expression) {
            return (String)baseCall(expression, XPathConstants.STRING);
        }
        
        private Object baseCall(final String expression, QName returnType) {
            try {
                return this.xPath.evaluate(expression, this.document, returnType);
            } catch (XPathExpressionException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public static class PersonCatcher {
        
        private static final Comparator<Person> FIRSTNAME_SORT = (p1,p2) -> p1.getFirstname().compareTo(p2.getFirstname());
        private Map<String, Person> personMap;
        private Map<String, String[]> friendMap;
        private Node root;
        
        public PersonCatcher(Node root) {
            this.root = root;
            this.personMap = new HashMap<>();
            this.friendMap = new HashMap<>();
        }
        
        public PersonCatcher start() {
            Nodes persons = BetterNode.of(this.root).findElementsByTag("person");
            for( Node n : persons) {                
                readPersonNode(n, false);
            }
            finalizeFriends();
            return this;
        }
        
        List<Person> sortedByFirstname() {
            List<Person> res = new ArrayList<>(this.personMap.values());
            res.sort(FIRSTNAME_SORT);
            return res;
        }
        
        
        private String text(Node n) {
            if(n == null) {
                return "";
            }
            return n.getTextContent();
        }
        
        private void finalizeFriends() {
            this.friendMap.forEach( (key,value) -> {
                final Person person = this.personMap.get(key);
                if(person == null) {
                    return;
                }
                
                Stream.of(value)
                    .map(this.personMap::get)
                    .filter(Objects::nonNull)
                    .forEach(person::pushFriend);
            });
        }
        
        private Person readPersonNode(Node node, boolean isChild) {
            BetterNode n = BetterNode.of(node);
            
            final String residence = text(n.findElementByTag("residence"));
            final String firstname = text(n.findElementByTag("firstname"));
            final String lastname = text(n.findElementByTag("lastname"));
            final String id = n.attribute("id");
            final String[] friends = n.attribute("friends").trim().split(" ");
            this.friendMap.put(id, friends);
            
            final Person p = new Person(id,isChild);
            p.setFirstname(firstname);
            p.setLastname(lastname);
            p.setResidence(residence);
            
            
            Node childrenElement = n.findElementByTag("children");
            if(childrenElement != null ){
                Nodes children = BetterNode.of(childrenElement).findElementsByTag("person");
                for(Node child : children) {
                    p.pushChild(readPersonNode(child,true));
                }
            }
            this.personMap.put(id, p);
            //System.out.println(p);
            return p;
        }
    }
    
    private static Document buildDocument(final String file) {
        final DocumentBuilderFactory factory =  DocumentBuilderFactory.newInstance();
        try( FileInputStream stream = new FileInputStream(new File(file))) {            
            return factory.newDocumentBuilder().parse(stream);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private static Node getPersonRoot(Document doc) {
        return doc.getElementsByTagName("persons").item(0);
    }
    
    private static void persons(Document doc) {
        Node root = getPersonRoot(doc);
        PersonCatcher catcher = new PersonCatcher(root);
        catcher.start();
        
        System.out.println("------ finished -------");
        for(Person p : catcher.sortedByFirstname()) {
            System.out.println(p);
        }
    }
    
    private static void xpath(Document doc) {
        XPather p = new XPather(doc);
        //A.)
        //System.out.println(p.asText("//persons/descendant::person/firstname/text()"));
        //System.out.println(Nodes.of(p.asNodes("//persons/descendant::person[./firstname/text()|./lastname/text()]")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        System.out.println(Nodes.of(p.asNodes("//persons/descendant::person/firstname/text()")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        
        //B.)
        System.out.println(p.asNumber("count(//person)"));
        
        //C.)
        //System.out.println(p.asNumber("count(//person[not(boolean(./lastname))])"));
        System.out.println(Nodes.of(p.asNodes("//person[not(boolean(./lastname))]")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        
        //D.)
        System.out.println(Nodes.of(p.asNodes("//persons/person/firstname")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        
        //E.)
        System.out.println(Nodes.of(p.asNodes("//comment()/ancestor::person/firstname")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        
        //F.)
        //System.out.println(Nodes.of(p.asNodes(".//person[./firstname/text()=Tigger]")).stream().map(Node::getTextContent).collect(Collectors.joining(",")));
        
        System.out.println("finished");
    }
    
    public static void run() {
        Document doc = buildDocument("persons.xml");
        Node root = getPersonRoot(doc);
        Nodes n = Nodes.of(root.getChildNodes());
        /*
        n.forEach( node -> {
            System.out.println(node.getNodeType());
            System.out.println(node.getNodeName());
        });
        */
        
        //persons(doc);
        xpath(doc);
        
    }
}
