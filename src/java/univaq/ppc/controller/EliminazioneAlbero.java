/*

 */
package univaq.ppc.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import univaq.ppc.utility.Database;
import univaq.ppc.utility.TemplateManager;
import univaq.ppc.utility.Utility;

/**
 *
 * @author Luca
 */
public class EliminazioneAlbero extends HttpServlet{

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
         
        String nome_albero = request.getParameter("nome_albero");
        if(Database.deleteRecord("albero", "nome='" + nome_albero + "'")) {
            Utility.sendAlertMessage("Eliminazione effettuata correttamente!", response, "eliminazioneAlbero");
            //response.sendRedirect("creazioneAlbero");
        } else {
            Utility.sendAlertMessage("Eliminazione non riuscita", response, "eliminazioneAlbero");
            response.sendRedirect("eliminazioneAlbero");
        }
        
     
    }
    

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        try {
            Map data = new HashMap();
            ResultSet rs = Database.selectAllRecord("albero");
            List alberi = new ArrayList();
            while(rs.next()) {
                alberi.add(rs.getString("nome"));
            }
            data.put("lista_alberi", alberi);
            TemplateManager.process("eliminazione.html", data, response, getServletContext());
        } catch (SQLException ex) {
            Logger.getLogger(EliminazioneAlbero.class.getName()).log(Level.SEVERE, null, ex);
        }
         

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EliminazioneAlbero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Servlet con funzione di eliminazione di un albero";
    }// </editor-fold>

}
