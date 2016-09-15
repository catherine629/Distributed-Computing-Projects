
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cathe
 */
public class StockRMIServant extends UnicastRemoteObject implements StockRMI{

    // Given a stock, get a list of users that are interested in that stock
    private static Map<String, HashSet<String>> stocks = new TreeMap();
    // Given a user, get the remote object reference to kts callback method
    private static Map<String, Notifiable> users = new TreeMap();
    
    public StockRMIServant() throws RemoteException{}
    
    /**
     * This method is called by client to subscribe their interested stock.
     * 
     * @param user client name
     * @param stockSym stock name
     * @return whether to subscribe successfully
     * @throws RemoteException 
     */
    @Override
    public boolean subscribe(String user, String stockSym) throws RemoteException {
        if(stocks.containsKey(stockSym)){
            // if the stocks contains stockSym, add the user to the user list
            stocks.get(stockSym).add(user);
            System.out.println(user+" subscibed to "+stockSym+".");
        }
        else{
            // else, create the new stock named by stockSym
            stocks.put(stockSym, new HashSet<>());
            stocks.get(stockSym).add(user);
            System.out.println(user+" subscibed to "+stockSym+".");
        }
        return true;
    }

    /**
     * This method is called by client to unsubscribe their previously interested stock.
     * 
     * @param user client name
     * @param stockSym stock name
     * @return whether to unsubscribe successfully
     * @throws RemoteException 
     */
    @Override
    public boolean unSubscribe(String user, String stockSym) throws RemoteException {
        if(!stocks.containsKey(stockSym)){
            // if stockSym doesn't exist, return false
            System.out.println(user+" failed in unsubscribing "+stockSym+".");
            return false;
        }else{
            if(stocks.get(stockSym).contains(user)){
                // if the user is on the subscription list of such stock, remove it
                stocks.get(stockSym).remove(user);
                System.out.println(user+" unsubscibed to "+stockSym+".");
                return true;
            }else{
                // if the user is not on the subscription list of such stock, return false
                System.out.println(user+" failed in unsubscribing "+stockSym+".");
                return false;
            }
        }
    }

    /**
     * This method is called by external source to inform of the update to stock
     * price and notify all subscribed users.
     * 
     * @param stockSym
     * @param price
     * @throws RemoteException 
     */
    @Override
    public void stockUpdate(String stockSym, double price) throws RemoteException {
        if(stocks.get(stockSym).isEmpty()){
            // if no users are interested in this stock, no one is notified
            System.out.println(stockSym+" price updated to "+price+", but no one is notified.");
        }
        else{
            System.out.println(stockSym+" price updated to "+price+", and the following subscribed clients are notified:");
            for(String user: stocks.get(stockSym)){
                users.get(user).notify(stockSym, price);
                System.out.println(user+" ");
            }
        }
    }

    /**
     * This method is called by client to pass a remote reference of itself to the server for later call back.
     * 
     * @param remoteClient a reference to remote client
     * @param user client name
     * @throws RemoteException 
     */
    @Override
    public void registerCallBack(Notifiable remoteClient, String user) throws RemoteException {
        System.out.println(user+" registered callback on server.");
        users.put(user, remoteClient);
    }

    /**
     * This method is called by client to delete corresponding remote reference on the server and exit.
     * 
     * @param user client name
     * @throws RemoteException 
     */
    @Override
    public void deRegisterCallBack(String user) throws RemoteException {
        users.get(user).exit();
        System.out.println(user+" exit.");
        
        // remove all info about this user
        for(HashSet<String> u: stocks.values()){
            u.remove(user);
        }
        System.out.println(user+"'s subscription info has been removed.");
        users.remove(user);
        System.out.println(user+" deregistered callback on server.");
    }
    
}
