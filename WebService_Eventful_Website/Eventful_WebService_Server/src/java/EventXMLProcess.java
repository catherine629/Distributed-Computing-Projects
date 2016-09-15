

import java.io.StringReader;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * This class is used to process XML for event, say, create XML from event object
 * or parse event object from XML.
 * Created by cathe on 11/13/2015.
 */
public class EventXMLProcess {

    /**
     * This method is used to create XML string from event.
     * @param e the event object
     * @return the XML string
     */
    static public String eventToXML(Eventful e){
        StringBuffer xml = new StringBuffer();
        xml.append("<event>").append("\r\n");
        xml.append("<title>" + e.getTitle() + "</title>").append("\r\n");
        xml.append("<time>" + e.getTime() + "</time>").append("\r\n");
        xml.append("<location>" + e.getLocation() + "</location>").append("\r\n");
        xml.append("</event>").append("\r\n");
        return xml.toString();
    }

    /**
     * This method is used to parse event object from XML string.
     * @param XML the XML string
     * @return the event object
     */
    static public Eventful eventFromXML(String XML){

        Document eventDoc = getDocument(XML);
        eventDoc.getDocumentElement().normalize();

        String[] tagNames = {"title", "time", "location"};
        NodeList nl = null;
        Node n = null;
        HashMap<String, String> infopairs = new HashMap<>();

        for(String s: tagNames){
            nl = eventDoc.getElementsByTagName(s);
            n = nl.item(0);
            infopairs.put(s, n.getTextContent());
        }
        
        Eventful event = new Eventful(infopairs.get("title"), infopairs.get("time"),infopairs.get("location"));
        return event;
    }

    // create document based on XML string, used to parse XML.
    static public Document getDocument(String xmlString) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document spyDoc = null;
        try  {
            builder = factory.newDocumentBuilder();
            spyDoc = builder.parse( new InputSource( new StringReader( xmlString ) ) );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spyDoc;
    }
}

