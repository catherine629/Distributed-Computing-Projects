
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * The StockPriceCallBack class is meant to provide callable methods to the
 * server side and would normally run on the dealer's computer.
 * @author cathe
 */
public class StockPriceCallBack extends UnicastRemoteObject implements Notifiable{
    
    static StockRMI servant = null;
    
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter user name:");
        String user = sc.nextLine();
        
        System.out.println("Looking up the server in the registry.");
        String url = "rmi://localhost/";
        try{
            servant = (StockRMI)Naming.lookup(url+"stockService");
            
            System.out.println("Creating a callback object to handle calls from the server.");
            Notifiable spcb = new StockPriceCallBack();
            
            System.out.println("Registering the callback with a name at the server.");
            servant.registerCallBack(spcb , user);
            
            System.out.println("Callback handler for "+user+" ready.");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public StockPriceCallBack() throws RemoteException{}
    
    @Override
    /**
     * This method is used by server to notify the client of the price update to
     * specific stock.
     */
    public void notify(String stockSym, double price) throws RemoteException {
        System.out.println(" Get price update to "+stockSym+": "+price);
    }

    @Override
    /**
     * This method is called by server when it is time to terminate the
     * thread that is handling callbacks on the client.
     */
    public void exit() throws RemoteException {
        try{
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("StockPirceCallBack exiting.");
        } catch (Exception e){
            System.out.println("Exception thrown "+e);
        }
    }
    
}
