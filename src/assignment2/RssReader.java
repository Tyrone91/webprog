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
    private XMLReaderWrapper reader;
    private Stack<StringBuilder> texts = new Stack<>();
    private Stack<Item> items = new Stack<>();
    private Collection<Item> foundItems = new HashSet<>();

    private Stack<State> states = new Stack<>();
    private String rssSourcename = "";
    private String rssSourcelink = "";
    
    /**
     * Handles the logic of the filtering of an XML file.
     * @param reader
     * @param filter
     */
    public RssReader(XMLReaderWrapper reader, String... filter) {
        this.activeFilter = new HashSet<>( Arrays.asList(filter));
        this.reader = reader;
        
        /* if no filter are provided use the default ones*/
        if(filter.length == 0) {
            activeFilter.addAll(DEFAULT_CONTENT_FILTER);
        }
        
        /* format the filters to a more usable format*/
        activeFilter = activeFilter.stream().map(String::toLowerCase).map(String::trim).collect(Collectors.toSet());
        
        /* init all callbacks */
        
        reader.onElementStart( str -> {
            if("item".equalsIgnoreCase(str)) {
                /* it is not really needed to push items on a stack. All feeds had a plain list structure. But it doesn't hurt and is more flexible*/
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
            
            /* we could move the text inside the state object, but the state object came after this implementation and I don't want to touch it anymore */
            texts.push( new StringBuilder());
        });
        
        reader.onText( str -> {
            this.texts.peek().append(str);
        });
        
        reader.onElementEnd( str -> {
            final State state = this.states.pop();
            final String text = texts.pop().toString();
            
            /* if we're closing a item object we can take over all relevant data and remove the finished item from the top of the stack */
            if("item".equalsIgnoreCase(str)) {
                final Item current = this.items.pop();
                if(current.description != null) {
                    if( activeFilter.stream().anyMatch(current.description.toLowerCase()::contains) ) {
                        current.sourcename = this.rssSourcename;
                        current.sourcelink = this.rssSourcelink;
                        activeFilter.stream().filter(current.description.toLowerCase()::contains).forEach(current.tags::add);
                        
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
            
            if( state.itemchild() && "pubDate".equalsIgnoreCase(str)) {
                this.items.peek().pubdate = text;
            }
            
            if( state.channelchild() && "title".equalsIgnoreCase(str)) {
                this.rssSourcename = text;
            }
            
            if( state.channelchild() && "link".equalsIgnoreCase(str)) {
                this.rssSourcelink = text;
            }
            
        });
    }
    
    /**
     * starts the reading of the given inputstream
     * @param inputstream
     * @return
     */
    public List<Entry> start(InputStream inputstream) {
        this.reader.start(inputstream);
        
        final ObjectFactory fac = new ObjectFactory();
        
        /* return a list of all found items mapped to an Entry element */
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
                e.setPubdate(item.pubdate);
                
                final Tags tags = fac.createTags();
                tags.getTag().addAll(item.tags);
                e.setTags(tags);
                
                return e;
            })
        .collect( Collectors.toList());
        
    }
    
    /**
     * Simple data holder object.
     */
    private class Item {
        
        public String title = "";
        public String description = "";
        public String link = "";
        public String sourcename = "";
        public String sourcelink = "";
        public String category = "";
        public String pubdate = "";
        public List<String> tags = new ArrayList<>();
    }
    
    /**
     * simple data holder object
     */
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
