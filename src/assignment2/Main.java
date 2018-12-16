package assignment2;

import java.io.FileOutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

public class Main {
    
    private static Set<String> SOURCES = new HashSet<>( Arrays.asList(
            "https://www.tagesschau.de/newsticker.rdf",
            "http://www.spiegel.de/schlagzeilen/index.rss",
            "https://www.heise.de/newsticker/heise.rdf"
            ));
    
    public static void main(String[] args) {
        //XMLReaderWrapper xmlreader = new SAXConnector();
        XMLReaderWrapper xmlreader = new STAXConnector();
        
        
        
        
        ObjectFactory fac = new ObjectFactory();
        Rss rss = fac.createRss();
        for(String src : SOURCES) {
            try {
                RssReader reader = new RssReader(xmlreader);
                reader.start(new URL(src).openStream()).forEach(rss.getEntry()::add);
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
    }
}
