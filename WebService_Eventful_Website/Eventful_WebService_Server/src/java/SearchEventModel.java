
import com.evdb.javaapi.APIConfiguration;
import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.operations.EventOperations;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class is the logic model to search for a specific event.
 * @author cathe
 */
public class SearchEventModel {
    
    /**
     * This method is used to fetch specific event from eventful and return the 
     * XML string of selected event. In this process, this method will call
     * randomEventFromXML method to randomly select a event and transform its
     * format as required.
     * @param keyword the keyword of event
     * @param location the location of event
     * @return the XML string of obtained event
     */
    public Eventful doSearchEvent(String keyword, String location) {
        String response = "";
        
        EventOperations eo = new EventOperations();
        EventSearchRequest esr = new EventSearchRequest();
        List<Event> events = new ArrayList<>();
        Event e = null;
        
        APIConfiguration.setApiKey("****");
        APIConfiguration.setEvdbUser("****");
        APIConfiguration.setEvdbPassword("****");
        
        
        esr.setKeywords(keyword);
        esr.setLocation(location);
        
        SearchResult sr = null;
        try {
                sr = eo.search(esr);
                System.out.println(sr.getTotalItems());
                if (sr.getTotalItems() > 1) {
                    events = sr.getEvents();
                    int eventNo = new Random().nextInt(events.size());
                    System.out.println(eventNo);
                    e = events.get(eventNo);
                    
                    String lo = e.getVenueAddress()+", "+e.getVenueCity()+", "+e.getVenueRegion();
                    String title = e.getTitle();
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy, EEE, HH:mm");
                    String time = sdf.format(e.getStartTime());
                    return new Eventful(title, time, lo);
                }else{
                    return null;
                }
        }catch(EVDBRuntimeException var){
                return null;
        } catch( EVDBAPIException var){
                return null;
        }
    }
    
}
    
