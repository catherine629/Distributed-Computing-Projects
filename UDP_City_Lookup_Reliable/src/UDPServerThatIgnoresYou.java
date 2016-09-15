import java.net.*;
import java.io.*;
import java.util.Random;
import java.util.TreeMap;

/**
 * This is a unreliable UDP server for city lookup that will ignore 90% of 
 * requests. This server will listen to the request from UDP client and ignore
 * 90% of them. For request it ignores, the server does nothing. For request it
 * does not ignore, the server will look up the coordinate of the city and 
 * respond the result back to the client. If there's no result about the city 
 * found, the server will return null.
 * 
 * @author cathe
 */
public class UDPServerThatIgnoresYou {
    
    static TreeMap<String,String> cityLocationPairs = null;
    
    public static void main(String args[]){
        // initiate the city location database
        initiate();
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        // use random number to ignore 90% requests
        Random rnd = new Random(); 
        try{
            // create the socket and set the port
            aSocket = new DatagramSocket(33333);
            // receive the request from client
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            while(true){
                aSocket.receive(request);
                // retrieve data from message
                String message = new String(request.getData(),0,request.getLength());
                // generate random number and to decide whether to ignore the request
                if(rnd.nextInt(10) < 9){
                    // ignore 90% of the request
                    System.out.println("Got request " + message +" but ignoring it.");
                    continue;
                }else {
                    // process 10% of the request
                    System.out.println("Got request " + message);
                    System.out.println("And making a reply");
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
    
    // initiate the cityLocationPairs
    static void initiate(){
        cityLocationPairs = new TreeMap();
        cityLocationPairs.put("Pittsburgh,PA", "40.440625,-79.995886");
        cityLocationPairs.put("New York,NY", "40.714167,-74.006389");
        cityLocationPairs.put("Washington,DC", "38.907192,-77.036871");
        cityLocationPairs.put("Chicago,IL", "41.878114,-87.629798");
    }
}
