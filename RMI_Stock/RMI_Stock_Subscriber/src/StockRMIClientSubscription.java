
import java.rmi.Naming;
import java.util.Scanner;

/**
 * The StockRMIClientSubscription class interacts with the user to subscribe and
 * unsubscribe to specific stock. After subscribing, the client will begin to
 * receive updates to that stock price. After unsubscribing, the client are no
 * longer informed of updates to that stock.
 * 
 * @author cathe
 */
public class StockRMIClientSubscription {
 
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter user name:");
        String user = sc.nextLine();
        System.out.println("Enter S for subscribe or U for unsubscribe followed by the stock symbol of interest.");
        System.out.println("Enter ! to quit");
        
        String txt;
        String sub_key;
        String stockSym;
        String[] data;
        
        StockRMI servant = null;
        String url = "rmi://localhost/";
        try{
            servant = (StockRMI)Naming.lookup(url+"stockService");
        }catch (Exception e){
            e.printStackTrace();
        }
        
        while(!(txt=sc.nextLine()).equals("!")){
            data = txt.split(" ");
            sub_key = data[0];
            stockSym = data[1];
            try{
                if(sub_key.equals("S"))
                    servant.subscribe(user, stockSym);
                else
                    servant.unSubscribe(user, stockSym);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        
        // exit with "!"
        try{
            servant.deRegisterCallBack(user);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
