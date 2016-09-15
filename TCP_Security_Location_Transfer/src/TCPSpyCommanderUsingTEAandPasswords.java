import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TCP Server for Spy commander using TEA and Password for location receive.
 * @author cathe
 */
public class TCPSpyCommanderUsingTEAandPasswords {

    static String[][] userPwFinal = new String[4][3];
    static KMLFileEdit kmlFE = new KMLFileEdit();

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        // prompt the commerder to enter symmetric key
        System.out.print("Enter symmetric key for TEA: ");
        String symmetricKey = sc.nextLine();

        // do initialization
        init();

        // prepare for receiving data from client
        try{
            int serverPort = 33333;
            ServerSocket listenSocket = new ServerSocket(serverPort);
            System.out.println("Waiting for spies to visit…");
            while(true){
                Socket clientSocket = listenSocket.accept();
                Connection c = new Connection(clientSocket, symmetricKey);
            }
        }catch (IOException e){
            System.out.println("Listen socket: "+e.getMessage());
        }
    }

    /**
     * do initialization of userId and password.
     */
    private static void init(){
        // initialize the KML file
        String[][] userPwInit = {{"john","joh"},{"tom","to"},{"mike","mik"},{"michael","mich"}};
        // store the user and pwd using hash value
        for(int i = 0; i < 4; i++){
            // generate random GUID as salt
            String salt = UUID.randomUUID().toString();
            // compute the hash value of password+salt
            String pwSaltHash = null;
            try {
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update((userPwInit[i][1]+salt).getBytes());
                byte[] hashResult = md.digest();
                pwSaltHash = new String(hashResult,0,hashResult.length);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(TCPSpyCommanderUsingTEAandPasswords.class.getName()).log(Level.SEVERE, null, ex);
            }
            // store the userId, hash value and salt
            String[] userPwPair = {userPwInit[i][0], pwSaltHash, salt};
            userPwFinal[i] = userPwPair;
        }
        // delete the init userId & password pair
        userPwInit = null;
    }
}
