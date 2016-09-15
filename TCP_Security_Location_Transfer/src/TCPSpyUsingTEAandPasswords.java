import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * TCP Client for Spy using TEA and Password for location transmission.
 * @author cathe
 */
public class TCPSpyUsingTEAandPasswords {
    
    static Socket s = null;
    
    public static void main(String args[]){
        
        Scanner sc = new Scanner(System.in);
        String symmetricKey;
        String userId;
        String password;
        String location;
        
        // prompt the spy to enter required info
        System.out.print("Enter symmetric key for TEA: ");
        symmetricKey = sc.nextLine();
        System.out.print("Enter your ID: ");
        userId = sc.nextLine();
        System.out.print("Enter your Password: ");
        password = sc.nextLine();
        System.out.print("Enter your location: ");
        location = sc.nextLine();
        
        String result = tansmitLocation(symmetricKey, userId, password, location,args[0]);
        System.out.println(result);
    }
    
    /**
     * This method is used to transmit required info to Spy commander. The 
     * transmission will if the symmetric key or userId and password are not valid. 
     * @param key symmetric key
     * @param userId the userId of spy
     * @param password the password of spy
     * @param location the location coordinate of this spy
     * @param IP the server IP
     * @return result the response from server
     */
    public static String tansmitLocation(String key, String userId, String password, String location, String IP){
        String result = null;
        // use TEA to encrypt the location message
        String message = userId + ";" + password + ";" +location;
        TEA t = new TEA(key.getBytes());
        byte[] encryptedMessage = t.encrypt(message.getBytes());
        
        try{
            // transmit data to server
            int serverPort = 33333;
            s = new Socket(IP,serverPort);
            DataOutputStream dos = new DataOutputStream(s.getOutputStream());
            dos.write(encryptedMessage);
            dos.flush();
            
            // receive data from server
            DataInputStream is = new DataInputStream(s.getInputStream());
            byte[] reply = new byte[1024];
            is.read(reply);
            result = new String(reply,0,reply.length);
            if(result.equals("Invalid Symmetric Key"))
                throw new InvalidSymmetricKeyException();
        } catch(InvalidSymmetricKeyException e){
            System.out.println(e.getMessage());
            System.out.println("The connection will be close.");
        }catch(UnknownHostException e){
            System.out.println("Socket: "+e.getMessage());
        } catch (EOFException e){
            System.out.println("EOF: "+e.getMessage());
        } catch (IOException e){
            System.out.println("Readline: "+e.getMessage());
        } finally{
            if(s!=null)
                try{
                    s.close();
                } catch (IOException e){
                    System.out.println("Close: "+e.getMessage());
                }
        }
        return result;
    }
}
