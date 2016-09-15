
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The StockRMIServer is responsible for starting up the rmiregistry and binding
 * the servant to the registry.
 * @author cathe
 */
public class StockRMIServer {
    public static void main(String[] args){
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            StockRMIServant stockService = new StockRMIServant();
            System.out.println("Starting up the rmiregistry and bind the servant");
            Naming.rebind("stockService", stockService);
            System.out.println("Waiting for invocations from clients...");  
        } catch (MalformedURLException ex) {
            Logger.getLogger(StockRMIServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RemoteException re){
            re.printStackTrace();
        }
    }
}
