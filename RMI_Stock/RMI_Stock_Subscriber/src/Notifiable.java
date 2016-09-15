
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Notifiable is an interface with two methods:
 * The method notify (String stockSym, double price) is called by the server 
 * and informs the callback client that a change in the stock price has occurred.
 * The method exit() is also called by the server and tells the callback client 
 * that it should cease listening for this client. The user has exited the system.
 * 
 * This interface will be implemented on the client side.
 * The implementation will be done by an object that extends the 
 * UnicastRemoteObject. The interface will be available on the client and the
 * server.
 * 
 * @author cathe
 */
public interface Notifiable extends Remote{
    /**
     * This method is used by stock service to notify all subscribed clients of
     * updated stock price.
     * @param stockSym the updated stock
     * @param price the new price
     * @throws RemoteException 
     */
    public void notify(String stockSym, double price) throws RemoteException;
    
    /**
     * This method is used by server to tell client to exit.
     * @throws RemoteException 
     */
    public void exit() throws RemoteException;
    
}
