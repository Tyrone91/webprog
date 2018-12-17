package assignment2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class Main {
    
    private enum ReaderMode {STAX, SAX};
    
    private static class CommandLineValues {
        
        public ReaderMode readermode;
        public Set<String> rssfeeds;
        public String[] keywords;
        public boolean fromFile;
    }
    
    private static Set<String> SOURCES = new HashSet<>( Arrays.asList(
            "https://www.tagesschau.de/newsticker.rdf",
            "http://www.spiegel.de/schlagzeilen/index.rss",
            "https://www.heise.de/newsticker/heise.rdf"
            ));
    
    private static int set(Map<String,String> target, String key, String value, int newIndex) {
        target.put(key, value);
        return newIndex;
    }
    
    private static void register(Map<String, Function<Integer, Integer>> target, Function<Integer, Integer> f, String... keys) {
        for(String k : keys) {
            target.put(k, f);
        }
    }
    
    private static final String MODE = "mode";
    private static final String KEYS = "keys";
    private static final String FEEDS = "feeds";
    private static final String OFFLINE = "offline";
    private static final String DEFAULTS = "defaults";
    
    /**
     * This functions parse the passed command line values and returns a new object with the wanted options.
     * if an error happens null will be returned.
     * @param args
     * @return
     */
    public static CommandLineValues parse(String[] args) {
        final CommandLineValues res = new CommandLineValues();
        
        if(args.length == 0) {
            System.err.println("No arguments provided. Type --help for help");
            return null;
        }
        
        if("--help".equalsIgnoreCase(args[0])) {
            System.out.println("-m or --mode ${mode}. Possible modes StAX or SAX. Default: SAX");
            System.out.println("-k or --keys ${key, ...}. Keys to look for. Default: Berlin, Welt, Mensch, Java");
            System.out.println("-f or --feeds ${feed, ...}. searched feeds. Tageschau, Spiegel, Heise");
            System.out.println("-o or --offline. Use files instead of urls. Default false");
            System.out.println("-d or --defaults. Use all default values");
        }
        
        Map<String,String> keyValue = new HashMap<>();
        Map<String, Function<Integer, Integer>> lookFor = new HashMap<>();
        
        register(lookFor, idx -> set(keyValue, MODE, args[idx+1], idx+1), "-m", "--mode");
        register(lookFor, idx -> set(keyValue, FEEDS, args[idx+1], idx+1), "-f", "--feeds");
        register(lookFor, idx -> set(keyValue, KEYS, args[idx+1], idx+1), "-k", "--keys");
        register(lookFor, idx -> set(keyValue, OFFLINE, args[idx], idx), "-o", "--offline");
        register(lookFor, idx -> set(keyValue, DEFAULTS, args[idx], idx), "-d", "--defaults");
        
        
        for(int i = 0; i < args.length; ++i) {
            Function<Integer,Integer> f = lookFor.get(args[i]);
            if(f == null) {
                System.err.format("Unknown argument '%s'\n", args[i]);
                return null;
            }
            i = f.apply(i);
        }
        
        res.rssfeeds = SOURCES;
        res.keywords = new String[0];
        res.readermode = ReaderMode.SAX;
        res.fromFile = false;
        
        if(keyValue.containsKey(DEFAULTS)) {
            return res;
        }
        
        if(keyValue.containsKey(OFFLINE)) {
            res.fromFile = true;
        }
        
        if(keyValue.containsKey(MODE)) {
            final String m = keyValue.get(MODE);
            if("stax".equalsIgnoreCase(m)) {
                res.readermode = ReaderMode.STAX;
            } else if("sax".equalsIgnoreCase(m)) {
                res.readermode = ReaderMode.SAX;
            } else {
                System.err.format("invalid argument for mode '%s'\n", m);
                return null;
            }
        }
        
        if(keyValue.containsKey(KEYS)) {
            final String keys = keyValue.get(KEYS);
            res.keywords = keys.split(",");
        }
        
        if(keyValue.containsKey(FEEDS)) {
            final String keys = keyValue.get(FEEDS);
            res.rssfeeds =  new HashSet<>( Arrays.asList(keys.split(","))); 
        }
        
        return res;
    }
    
    public static void main(String[] args) {
        CommandLineValues lines = parse(args);
        if(lines == null) {
            return;
        }
        
        XMLReaderWrapper xmlreader = null;
        if(lines.readermode == ReaderMode.SAX) {
            xmlreader = new SAXConnector();
            System.out.println("using sax mode");
        } else {
            xmlreader = new STAXConnector();
            System.out.println("using stax mode");
        }
        
        if(lines.fromFile) {
            System.out.println("loading feeds from file");
        }
        
        System.out.println("fetch from: " + lines.rssfeeds.stream().collect(Collectors.joining(",")));
        System.out.println("filter for keys: " + Arrays.stream(lines.keywords).collect(Collectors.joining(",")) + " or defaults if none provided.");
        
        ObjectFactory fac = new ObjectFactory();
        Rss rss = fac.createRss();
        for(String src : lines.rssfeeds) {
            try {
                RssReader reader = new RssReader(xmlreader, lines.keywords);
                InputStream stream = lines.fromFile ? new FileInputStream(src) : new URL(src).openStream(); 
                List<Entry> entries =  reader.start(stream);
                XMLGregorianCalendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("yyyy-MM-dd").format( new Date()));
                XMLGregorianCalendar time = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat("HH:mm:ss").format( new Date()));
                entries.forEach(e -> e.setPulldate(date));
                entries.forEach(e -> e.setPulltime(time));
                entries.forEach(rss.getEntry()::add);
            } catch(Exception ex) {
                ex.printStackTrace(System.err);
            }
        }
        
        try {            
            JAXBContext context = JAXBContext.newInstance("assignment2");
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            
            m.marshal( rss, new FileOutputStream("rssresult.xml"));
        } catch(Exception ex ) {
            ex.printStackTrace(System.err);
        }
        
        System.out.println("finished");
    }
}
