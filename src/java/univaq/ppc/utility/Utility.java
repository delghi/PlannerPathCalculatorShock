/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.utility;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Luca
 */
public class Utility {
    
      //metodo che ha la funzione di inviare un alert per segnalare un messaggio al'utente
      public static void sendAlertMessage (String message, HttpServletResponse response, String location) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('"+ message + "');");
        out.println("window.location = '"+ location+ "';");
        out.println("</script>");
    }
    
}
