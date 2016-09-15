import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Make connection with clients, process data from clients and give response.
 * @author cathe
 */
public class Connection extends Thread{
    DataInputStream in;
    DataOutputStream out;
    Socket clientSocket;
    String symmetricKey;
    static int numOfVisit = 0;
    
    /**
     * This method is used to establish thread for building connection with client.
     * @param aClientSocket the accepted client socket
     * @param symmetricKey the symmetric key
     */
    public Connection (Socket aClientSocket, String symmetricKey){
        this.symmetricKey = symmetricKey;
        try{
            numOfVisit++;
            clientSocket = aClientSocket;
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            this.start();
        }catch(IOException e){
            System.out.println("Connection: "+e.getMessage());
        }
    }
    
    /**
     * This is the specific implementation for the connection thread.
     */
    public void run(){
        try{
            // read data from client
            byte[] data = new byte[1024];
            int end = in.read(data);
            // create a new byte[] to contain the data with specific length
            byte[] transferedData = new byte[end];
            System.arraycopy(data, 0, transferedData, 0, end);
            // process data and execute corresponding check and operations
            String reply = processData(transferedData);
            // send back response
            out.write(reply.getBytes());
        }catch (EOFException e){
            System.out.println("EOF: "+e.getMessage());
        }catch (IOException e){
            System.out.println("Readline: "+e.getMessage());
        }finally {
            try{
                clientSocket.close();
            }catch (IOException e){
                System.out.println("Close: "+e.getMessage());
            }
        }
    }
    
    /**
     * This method is used to process the data, including to verify its validation,
     * execute location update and return response.
     * @param data the message received from the client
     * @return the response
     */
    public String processData(byte[] data){
        // use TEA to decrypt data
        TEA t = new TEA(symmetricKey.getBytes());
        byte[] clearData = t.decrypt(data);
        boolean symKeyVerified = true;
        String[] messageArray = null;
        try{
            String clearText = new String(clearData,0,clearData.length);
            // seperate the clearText into userId, password and location
            messageArray = clearText.split(";");
            // check whether the spy enters the correct symmetric key, that is, check whether the decrypted text contains these three part.
            symKeyVerified = isCorrectFormat(messageArray);
        } catch(Exception e){
            symKeyVerified = false;
        }
        if(symKeyVerified==false){
            // ignore the request and notify Sean
            System.out.println("Got visit " + numOfVisit + " illegal symmetric key used.");
            return new String("Invalid Symmetric Key");
        }else {
            String userId = messageArray[0];
            String password = messageArray[1];
            String location = messageArray[2];
            // verify the userId and password via comparing the hash of the salt plus the password
            boolean userVerified = isCorrectUserPassword(userId, password);
            if(userVerified==false){
                // if the userId or password is wrong, tell the spy and spy commander
                System.out.println("Got visit "+numOfVisit+" from "+ userId +" Illegal UserId or Password attempt.");
                return new String("Not a valid user-id or password.");
            }else{
                // if the userId and password is correct, update the location
                System.out.println("Got visit "+numOfVisit+" from "+ userId);
                TCPSpyCommanderUsingTEAandPasswords.kmlFE.updateKML(userId, location);
                return new String("Thank you. Your location was securely transmitted to Intelligence Headquarters.");
            }
        }
    }
    
    /**
     * The method is used to check whether the String contains messy code.
     * @param text data to be checked
     * @return whether the text are properly formatted
     */
    private boolean isCorrectFormat(String[] text){
        // if the length of string array does not equal 3, return false
        if(text.length!=3)
        {
            return false;
        }
        // if the location does not contain "," and ".", return false
        return text[2].contains(",")&&text[2].contains(".");
    }
    
    /**
     * The method is used to check whether the userId and password is correct.
     * @param userId the userId of spy
     * @param password the password of spy
     * @return whether the userId and password are correct
     */
    private boolean isCorrectUserPassword(String userId, String password){
        // get the original  password pair
        String userIdPwPair[][] = TCPSpyCommanderUsingTEAandPasswords.userPwFinal;
        String pwSaltHash = null;
        // check whether the userId is correct
        int flag = -1;
        for(int i=0; i<4; i++){
            if(userIdPwPair[i][0].equals(userId)){
                flag = i;
                break;
            }
        }
        if(flag==-1){
            // if there does not exist the userId, return false
            return false;
        }else {
            // if exists, check the password
            try {
                // compute the hash value of provided password + salt
                MessageDigest md;
                md = MessageDigest.getInstance("MD5");
                md.update((password+userIdPwPair[flag][2]).getBytes());
                byte[] hashResult = md.digest();
                pwSaltHash = new String(hashResult,0,hashResult.length);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(TCPSpyCommanderUsingTEAandPasswords.class.getName()).log(Level.SEVERE, null, ex);
            }
            return pwSaltHash.equals(userIdPwPair[flag][1]);
        }   
    }
}