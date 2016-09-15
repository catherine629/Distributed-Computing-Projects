import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author cathe
 */
@WebServlet(name = "SearchServlet", urlPatterns = {"/SearchServlet/*"})
public class SearchServlet extends HttpServlet {

    SearchEventModel sem = new SearchEventModel();
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        System.out.println("Console: doGET visited");
        
        Eventful result;
        
        // The parameters are on the path /name so skip over the '/'
        String parameters = (request.getPathInfo()).substring(1);
        
        String[] data = parameters.split("&");
        String keyword = data[0].replace("+", " ");
        String location = data[1].replace("+", " ");
        
        // Use SearchEventModel to process the search
        result = sem.doSearchEvent(keyword, location);
        String resultXML;
        
        response.setStatus(200);
        response.setContentType(request.getHeader("Accept"));
        
        if(result==null){
            // return 404 if no event is searched
            resultXML = "No event found";
        }else{
            resultXML = EventXMLProcess.eventToXML(result);
            // tell the client the type of the response
        }   
        // return response
        PrintWriter out = response.getWriter();
        System.out.println(resultXML);
        out.print(resultXML);    
    }
}
