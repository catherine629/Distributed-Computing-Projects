import java.rmi.Naming;
import java.util.Scanner;
/**
 * This class interacts with users to obtain stock update and pass it to the
 * server. This class represents an external source of events.
 * @author cathe
 */
public class StockRMIClientPriceUpdate {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String txt;
        String[] data;
        String stockSym;
        double price;
        StockRMI servant = null;
        
        String url = "rmi://localhost/";
        try{
            servant = (StockRMI)Naming.lookup(url+"stockService");
        }catch (Exception e){
            e.printStackTrace();
        }
        
        System.out.println("Enter stock symbol and price or ! to quit.");
        while(!(txt=sc.nextLine()).equals("!")){
            data = txt.split(" ");
            stockSym = data[0];
            price = Double.parseDouble(data[1]);
            
            try{
                servant.stockUpdate(stockSym, price);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
