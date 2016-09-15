
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/**
 * This is used to initiate and update KML file. Each time there is a valid visit
 * to the server, the server will update the KML file with the new location.
 * @author cathe
 */
public class KMLFileEdit {

    // used to maintain the userId & location pairs
    HashMap<String,String> hm = new HashMap<String,String>();
    // used to write into file
    File secretAgents = null;
    String path = "SecretAgents.kml";

    String userId = null;
    String userType = null;
    String location = null;

    // used to compose the content
    // the beginning part
    String start = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?> \n"
            + "<kml xmlns=\"http://earth.google.com/kml/2.2\" > \n"
            + "<Document> \n"
            + "<Style id=\"style1\"> \n"
            + "<IconStyle> \n"
            + "<Icon> \n"
            + "<href>http://maps.gstatic.com/intl/en_ALL/mapfiles/ms/micons/bluedot.png</href> \n"
            + "</Icon> \n"
            + "</IconStyle> \n"
            + "</Style> \n";
    // the location part for specific user
    String userLocation1 = "<Placemark> \n"
            + "   <name>";
    String userLocation2 = "</name> \n"
            + "   <description>";
    String userLocation3 = "</description> \n"
            + "   <styleUrl>#style1</styleUrl> \n"
            + "   <Point> \n"
            + "     <coordinates>";
    String userLocation4 = "</coordinates> \n"
            + "   </Point> \n"
            + "</Placemark> \n";
    // the end part
    String end = "</Document> \n"
            + "</kml> ";

    /**
     * Constructor for KMLFileEdit to complete initiation and file creation.
     */
    public KMLFileEdit(){
        // initiate the initial user and location
        initKML();
        try{
            // make sure the destination of the kml file
            secretAgents = new File(path);
            if(!secretAgents.exists()){
                secretAgents.createNewFile();
            }
        } catch (IOException e){
            System.out.println("IOException: "+e.getMessage());
        }
        // write the initial KML file
        writeKMLFile();
    }

    /**
     * Complete the initiation of userId and location.
     */
    private void initKML(){
        hm.put("john", "-79.945289,49.44431,0.00000");
        hm.put("tom", "-79.945289,34.44431,0.00000");
        hm.put("michael", "-79.945289,78.44431,0.00000");
        hm.put("mike", "-79.945289,65.44431,0.00000");
    }

    /**
     * Complete the location update after each valid visit.
     * @param userId the user who changed his location
     * @param location the updated location
     */
    public void updateKML(String userId, String location){
        // update the new location
        hm.put(userId, location);
        // rewrite the KML file again
        writeKMLFile();
    }

    /**
     * Compose the content and write the KML file.
     */
    public void writeKMLFile(){
        // compose the KML file content
        StringBuffer secretContent = new StringBuffer();
        // add the start part
        secretContent.append(start);
        // add the specific user part
        for (String user: hm.keySet()){
            userId = user;
            location = hm.get(user);
            userType = userId.equals("seanb")?"Spy Commander":"spy";
            secretContent.append(userLocation1);
            secretContent.append(userId);
            secretContent.append(userLocation2);
            secretContent.append(userType);
            secretContent.append(userLocation3);
            secretContent.append(location);
            secretContent.append(userLocation4);
        }
        // add the end part
        secretContent.append(end);

        try(BufferedWriter bw = new BufferedWriter(new FileWriter(secretAgents.getAbsolutePath()))){
            bw.write(new String(secretContent));
            bw.flush();
        }catch (IOException e){
            System.out.println("IOException: "+e.getMessage());
        }
    }
}
