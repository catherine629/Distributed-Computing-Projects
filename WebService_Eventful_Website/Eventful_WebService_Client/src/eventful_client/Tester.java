package eventful_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Project4Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
            String response ="";
            int status = 0;

            String keyword = "music";
            String location = "New+York";

            try{
                // pase the keyword and location on the URL line
                URL url = new URL("http://localhost:8080/Project4AndroidServer/SearchServlet/"+keyword+"&"+location);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();

                // set request method to GET
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "text/xml");

                // wait for response
                status = conn.getResponseCode();

                String output = "";
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((output = br.readLine())!= null){
                    response += output;
                }
                conn.disconnect();

            }catch (Exception ex){
                ex.printStackTrace();
            }

            System.out.println(response);
    }

}
