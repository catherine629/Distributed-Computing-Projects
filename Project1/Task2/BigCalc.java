/**
 * This is a useful calculator application where six operations could be
 * implemented among large integers. The six operaions include addition,
 * multiplication, to determine relatively prime, modulo, modular inverse and
 * power.
 */
package BigIntegerCalculation;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * The following Servlet is used to collect and process parameters, after which
 * the corresponding calculation will be implemented.
 * 
 * @author cathe
 * 
 */
public class BigCalc extends HttpServlet {
    
    /**
     * Handles the HTTP <code>GET</code> method and implement the calculation.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String nextView;
        
        // extract the integer parameter, check validity and execute conversions 
        BigInteger x = null;
        BigInteger y = null;
        BigInteger result = null;
        
        nextView = "result.jsp";
        
        try{
            x = new BigInteger(request.getParameter("operant1"));
            y = new BigInteger(request.getParameter("operant2"));
        }catch (Exception e){
            
            // if the input doesn't represent a integer, choose the index view and ask the user to input again
            nextView = "wrongpage.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        }
        
        // extract the operation type
        String operation = request.getParameter("operation");
        
        // calculate the result according to corresponding operation types
        try{
        switch (operation)
        {
            case "add": result = x.add(y); break;
            case "multiply": result = x.multiply(y); break;
            case "relativelyPrime": result = x.gcd(y); break;
            case "modulo": result = x.mod(y); break;
            case "modInverse": result = x.modInverse(y); break;
            case "power": result = x.pow(y.intValue()); break;
        }} catch(ArithmeticException ae){
            nextView = "wrongpage.jsp";
            RequestDispatcher view = request.getRequestDispatcher(nextView);
            view.forward(request, response);
        }
        
        // pass the calculation result to result view via request attributes
        if(operation.equals("relativelyPrime"))
        {
            // if the greatest common divisor equals 1, x and y are relatively prime, otherwise, 
            if(result.intValue()==1)
                request.setAttribute("result", "relatively prime");
            else
                request.setAttribute("result", "not relatively prime");
        }
        else
            request.setAttribute("result", result);
        
        // pass other data to result view via request attributions
        request.setAttribute("operant1", x);
        request.setAttribute("operant2", y);
        request.setAttribute("type", operation);
        
        // transfer control over the correct "view"
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }

}
