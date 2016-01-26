/**
 *
 * This is a servlet that aims at computing the appropriate
 * cryptographic hash value from the text transmitted by the
 * browser. This will provide two hash funtions: MD5 and SHA-1.
 * After computing, the original text will be echoed back to
 * the browser along with the name of the hash, and the hash value. 
 * The hash values sent back to the browser should be displayed
 * in two forms: as hexadecimal text and as base 64 notation.
 * 
 * @author cathe
 * 
 */
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import sun.misc.BASE64Encoder;

/**
 * 
 * WebServlet annotation below gives instructions to the web container. When the
 * user browses to "/ComputeHashes", then the servlet "ComputerHashes" will be used.
 * 
 * @author cathe
 * 
 */
@WebServlet(name = "ComputeHashes",
        urlPatterns = {"/ComputeHashes"})
public class ComputeHashes extends HttpServlet {
    /**
     * Handles the HTTP <code>GET</code> method and implement the text translation.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get the original data text
        String text = request.getParameter("textdata");
        
        // decide what hash function to be used
        String hashFunc = request.getParameter("type");
        
        // make the choice and implement hash functions
        MessageDigest md;
        byte[] hashResult = null;    
       
        try {
            md = MessageDigest.getInstance(hashFunc);
            md.update(text.getBytes());
            hashResult = md.digest();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ComputeHashes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // transmit the byte result to hex string
        String hexResult = null;
        try {
            hexResult = this.getHexString(hashResult);
        } catch (Exception ex) {
            Logger.getLogger(ComputeHashes.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // transmit the byte result to base 64
        BASE64Encoder be = new BASE64Encoder();
        String base64 = be.encode(hashResult);
        
        /** 
         * use the request attribute to pass data from servlet to the view.
         * The attributes are name/value pairs. Here are the original text, 
         * name of the hash, hash value as hexadecimal text & base 64 notation.
         */
        request.setAttribute("text", text);
        request.setAttribute("hashName", hashFunc);
        request.setAttribute("haxResult", hexResult);
        request.setAttribute("base64", base64);
        
        // Transfer control over next view
        String nextView = "result.jsp";
        RequestDispatcher view = request.getRequestDispatcher(nextView);
        view.forward(request, response);
    }
    
    /**
     * This method is to compute the hexadecimal representation of a byte array
     * 
     *  @param b the bytes to be transfered
     * 
     */
    public String getHexString(byte[] b) throws Exception{
        String result = "";
        for (int i=0; i<b.length; i++){
            result += Integer.toString((b[i]&0xff)+0x100,16).substring(1);
        }
        return result;
    }
}
