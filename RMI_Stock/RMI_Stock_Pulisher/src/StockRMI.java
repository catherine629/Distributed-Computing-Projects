
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This is the interface to the remote stock service.
 * There are three types of clients of this service.
 * 
 * One client will be calling stockUpdate() to report on new prices for various
 * stocks.
 * 
 * A second client will be calling subscribe() and unsubscribe() to register
 * interest in or remove interest from a particular stock. This client will also
 * deRegisterCallBack() when it terminates.
 * 
 * A third type of client will be calling the registerCallBack() method so that 
 * it may receive call backs from the service when stock prices change.
 * 
 * Two two last clients are associated with a single human user. So, for example,
 * a user might use a call to subscribe to register interest in a particular stock.
 * At that point, the stock service will begin to make calls back to the client 
 * when that stock price changes.
 * 
 * @author cathe
 */
public interface StockRMI extends Remote{
    public boolean subscribe(String user, String stockSym) throws RemoteException;
    boolean unSubscribe(String user, String stockSym) throws RemoteException;
    public void stockUpdate(String stockSym, double price) throws RemoteException;
    public void registerCallBack(Notifiable remoteClient, String user) throws RemoteException;
    public void deRegisterCallBack(String user) throws RemoteException;
}
