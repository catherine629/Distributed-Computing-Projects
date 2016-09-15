/**
 * This is a customized exception, which is thrown when the spy client tries to 
 * enter a wrong symmetric key.
 * @author cathe
 */
public class InvalidSymmetricKeyException extends Exception{

    /**
     * InvalidSymmetricKeyException Constructor.
     */
    public InvalidSymmetricKeyException(){
        super();
    }

    /**
     * This is an override and return the "Invalid Symmetric Key" message.
     * @return the message about this exception
     */
    @Override
    public String getMessage() {
        return new String("Throws an Invalid Symmetric Key."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
