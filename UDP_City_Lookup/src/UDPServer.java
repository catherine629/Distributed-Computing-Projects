import java.net.*;
import java.io.*;
import java.util.TreeMap;

/**
 * This is a UDP server for city lookup. This server will listen to the request 
 * from UDP client. After processing the request message, the server will look
 * up the coordinate of the city and response the result back to the client. If 
 * there's no result about the city found, the server will return null.
 * 
 * @author cathe
 */
public class UDPServer {
    
    static TreeMap<String,String> cityLocationPairs = null;
    
    public static void main(String args[]){
        // initiate the city location database
        initiate();
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try{
            // create the socket and set the port
            aSocket = new DatagramSocket(33333);
            // receive the request from client
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while(true){
                aSocket.receive(request);
                // retrieve data from message
                String message = new String(request.getData(),0,request.getLength());
                // look up the coordinate in TreeMap accordingn to the message
                String result = cityLocationPairs.get(message);
                if(result!=null){
                    // if found, print out "Handling reuqest"
                    System.out.println("Handling request for "+message);
                    buffer = result.getBytes();
                }else{
                    // if not found, print out "Was unable to handle a request for..."
                    System.out.println("Was unable to handle a request for "+message);
                    buffer = "".getBytes();
                }
                // prepare datagram packet to send back the result
                DatagramPacket reply = new DatagramPacket(buffer,buffer.length,request.getAddress(),request.getPort());
                aSocket.send(reply);
            }
            
        }catch (SocketException e){
            System.out.println("Socket problem: " + e.getMessage());
        }catch (IOException e){
            System.out.println("Input/Output problem: " + e.getMessage());
        }finally {
            // close the socket after operation
            if(aSocket!=null)
                aSocket.close();
        }
    }
    
    /**
     * Initiate the cityLocationPairs.
     */
    static void initiate(){
        cityLocationPairs = new TreeMap();
        cityLocationPairs.put("Pittsburgh,PA", "40.440625,-79.995886");
        cityLocationPairs.put("New York,NY", "40.714167,-74.006389");
        cityLocationPairs.put("Washington,DC", "38.907192,-77.036871");
        cityLocationPairs.put("Chicago,IL", "41.878114,-87.629798");
    }
}
