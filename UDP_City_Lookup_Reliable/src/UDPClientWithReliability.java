import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 * This is a UDP client socket for city lookup. This client will use socket to 
 * send message to the UDP server. The message will contain the city name and 
 * abbreviation of state. Then the server will look up the city and return its
 * coordinates back to this client. At last, the client will show the coordinates
 * to the user.
 * 
 * @author cathe
 */
public class UDPClientWithReliability {
    
    static DatagramSocket aSocket = null;
    
    public static void main(String args[]){
        // the message to be transmitted
            String message;
            Scanner sc = new Scanner(System.in);
            // prompt the user to enter message
            System.out.println("Enter city name and state abbreviation, and we will find its coordinates (e.g. Pittsburgh,PA)");
            message = sc.nextLine();
            // call the function to ask for coordinate from server
            String result = getLocation(message);
            
            // if result is null, tell the user
            if(result.equals("")){
                System.out.println("Could not resolve "+message);
            }
            // represent reply to user
            else{
                System.out.println(result);
            }                          
    }
    
    // precondition: city has a name in the form "cyti,state abbreviation"
    // postcondition: Longitude and latitude coordinates are returned or an empty string is returned if the city is not in our table.
    /**
     * This method is used to build connection with server, send city message
     * and receive coordinate response.
     * @param city the city to be looked up
     * @return the result received from server
     */
    static String getLocation(String city){
        String result = null;
        try{
            // set the server IP address and port
            InetAddress aHost = InetAddress.getLocalHost();
            int serverPort = 33333;
            // create a datagram socket
            aSocket = new DatagramSocket();
            aSocket.setSoTimeout(1000);
            
            // transfer message string to bytes for transmission
            byte[] m = city.getBytes();
            // transmit and the message and receive response from server
            DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
            byte[] buffer = new byte[1000];
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            while(true){
                try{
                    aSocket.send(request);
                    aSocket.receive(reply);
                    break;
                } catch (SocketTimeoutException se){
                    // if receive timed out, continue the loop and send message again
                    System.out.println("Receive timed out, transmit again");
                }
            }
            result = new String(reply.getData(),0,reply.getLength());
        } catch (SocketException e){
            System.out.println("Client Socket problem: "+e.getMessage());
        } catch (IOException e){
            System.out.println("Input/Output problem: "+e.getMessage());
        } finally{
            // close the socket after operation
            if(aSocket!=null)
                aSocket.close();
        }
        return result;
    }
}