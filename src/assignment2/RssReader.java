package assignment2;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.stream.Collectors;

public class RssReader {
    
    private static final Set<String> DEFAULT_CONTENT_FILTER = new HashSet<>(Arrays.asList("Android", "Java", "Welt", "Mensch", "Berlin")); 
    
    private Set<String> activeFilter;
    private StringBuilder currentText;
    private XMLReaderWrapper reader;
    private Stack<StringBuilder> texts = new Stack<>();
    private Stack<Item> items = new Stack<>();
    private Collection<Item> foundItems = new HashSet<>();

    private Stack<State> states = new Stack<>();
    private String rssSourcename = "";
    private String rssSourcelink = "";
    
    public RssReader(XMLReaderWrapper reader, String... filter) {
        this.currentText = new StringBuilder();
        this.activeFilter = new HashSet<>( Arrays.asList(filter));
        this.reader = reader;
        
        if(filter.length == 0) {
            activeFilter.addAll(DEFAULT_CONTENT_FILTER);
        }
        
        reader.onElementStart( str -> {
            if("item".equalsIgnoreCase(str)) {
                items.push( new Item());
            }
            
            
            final State state = new State();
            String prev = "";
            if(!states.isEmpty()) {
                prev = states.peek().currentElement;
            }
            state.currentElement = str;
            state.prevElement = prev;
            
            this.states.push(state);
            
            //System.out.format("<%s>\n", str);
            texts.push( new StringBuilder());
        });
        
        reader.onText( str -> {
            this.texts.peek().append(str);
        });
        
        reader.onElementEnd( str -> {
            final State state = this.states.pop();
            final String text = texts.pop().toString();
            if("item".equalsIgnoreCase(str)) {
                final Item current = this.items.pop();
                if(current.description != null) {
                    if( activeFilter.stream().anyMatch(current.description::contains) ) {
                        current.sourcename = this.rssSourcename;
                        current.sourcelink = this.rssSourcelink;
                        activeFilter.stream().filter(current.description::contains).forEach(current.tags::add);
                        
                        this.foundItems.add(current);
                    }
                }
            }
            
            if( state.itemchild() && "title".equalsIgnoreCase(str)) {
                this.items.peek().title = text;
            }
            
            if( state.itemchild() && "description".equalsIgnoreCase(str)) {
                this.items.peek().description = text;
            }
            
            if( state.itemchild() && "link".equalsIgnoreCase(str)) {
                this.items.peek().link = text;
            }
            
            if( state.itemchild() && "category".equalsIgnoreCase(str)) {
                this.items.peek().category = text;
            }
            
            if( state.channelchild() && "title".equalsIgnoreCase(str)) {
                this.rssSourcename = text;
            }
            
            if( state.channelchild() && "link".equalsIgnoreCase(str)) {
                this.rssSourcelink = text;
            }
            
            //System.out.format("</%s>\n",str);
            currentText = new StringBuilder();
        });
    }
    
    public List<Entry> start(InputStream inputstream) {
        this.reader.start(inputstream);
        
        this.foundItems.forEach( item -> {
            System.out.println("title: " + item.title);
            System.out.println("dsc: " + item.description);
            System.out.println("link: " + item.link);
            System.out.println("srcname: " + item.sourcename);
            System.out.println("srclink: " + item.sourcelink);
            System.out.println("category: " + item.category);
            System.out.println("tags: " + item.tags.stream().collect(Collectors.joining(",")));
        });
        
        final ObjectFactory fac = new ObjectFactory();
        
        
        return this.foundItems
        .stream()
        .map( item -> {
                Entry e = fac.createEntry();
                e.setCategory(item.category);
                e.setDescription(item.description);
                e.setTitle(item.title);
                e.setLink(item.link);
                e.setSourcelink(item.sourcelink);
                e.setSourcename(item.sourcename);
                
                final Tags tags = fac.createTags();
                tags.getTag().addAll(item.tags);
                e.setTags(tags);
                
                return e;
            })
        .collect( Collectors.toList());
        
    }
    
    public class Item {
        
        public String title = "";
        public String description = "";
        public String link = "";
        public String sourcename = "";
        public String sourcelink = "";
        public String category = "";
        public List<String> tags = new ArrayList<>();
    }
    
    public class State {
        
        public String prevElement;
        public String currentElement;
        
        public boolean itemchild() {
            return prevElement.equalsIgnoreCase("item");
        }
        
        public boolean channelchild() {
            return prevElement.equalsIgnoreCase("channel");
        }
    }
}
