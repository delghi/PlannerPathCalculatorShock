/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import univaq.ppc.model.Albero;
import univaq.ppc.utility.Database;
import univaq.ppc.utility.TemplateManager;

/**
 *
 * @author Luca
 */
public class CreazioneAlbero extends HttpServlet {

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
        
        String nome = request.getParameter("nome_albero");
        int depth = Integer.parseInt(request.getParameter("depth"));
        int splitSize = Integer.parseInt(request.getParameter("split_size"));
        //test-attributi --- max 4 per vertice e per edge (assumption!)
        List<String> attributiV = new ArrayList<String>();
        List<String> attributiE = new ArrayList<String>();
        List attrTemp = new ArrayList<String>();
        //prendo dalla request gli attributi dei vertici
        String attributoVertice1 = request.getParameter("attr1");
        String attributoVertice2 = request.getParameter("attr2");
        String attributoVertice3 = request.getParameter("attr3");
        String attributoVertice4 = request.getParameter("attr4");
        // ... e quelli degli archi
        String attributoArco1 = request.getParameter("attrEdge1");
        String attributoArco2 = request.getParameter("attrEdge2");
        String attributoArco3 = request.getParameter("attrEdge3");
        String attributoArco4 = request.getParameter("attrEdge4");
        // check su attributi --- se non sono vuoti li aggiungo alla lista 
        if(!attributoVertice1.isEmpty()) {
            attributiV.add(attributoVertice1);
            attrTemp.add(attributoVertice1);
        }
        if(!attributoVertice2.isEmpty()){
            attributiV.add(attributoVertice2);
            attrTemp.add(attributoVertice2);
        }
        if(!attributoVertice3.isEmpty()) {
            attributiV.add(attributoVertice3);
            attrTemp.add(attributoVertice3);
        }
        if(!attributoVertice4.isEmpty()) {
            attributiV.add(attributoVertice4);
            attrTemp.add(attributoVertice4);
        }
        if(!attributoArco1.isEmpty()) {
            attributiE.add(attributoArco1);
            attrTemp.add(attributoArco1);
        }
        if(!attributoArco2.isEmpty()) {
            attributiE.add(attributoArco2);
            attrTemp.add(attributoArco2);
        }
        if(!attributoArco3.isEmpty()) {
            attributiE.add(attributoArco3);
            attrTemp.add(attributoArco3);
        }
        if(!attributoArco4.isEmpty()){
            attributiE.add(attributoArco4);
            attrTemp.add(attributoArco4);
        }
         
        //end-test
        Albero albero = new Albero(nome, splitSize, depth, attrTemp);
        albero.creaAlbero(attributiV, attributiE);
        System.out.println(albero);
        Database.insertAlbero("albero", albero);
        List alberi = new ArrayList();
       ResultSet rs = Database.selectAllRecord("albero");
       Map data = new HashMap();
       while(rs.next()) {
           alberi.add(rs.getString("nome"));
       }

       data.put("default", 0);
       data.put("defaultMax", albero.getNumNodi()-1);
       data.put("lista_alberi", alberi);
       TemplateManager.process("index2.html", data, response, getServletContext());
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
            String button  = request.getParameter("btncrea");
            if(button != null){
                if(button.equals("Crea Albero")) {
                    TemplateManager.process("creazione.html", data, response, getServletContext());
                    return;
                }
            }
            
            ResultSet rs = Database.selectAllRecord("albero");
            
            List alberi = new ArrayList();
            while(rs.next()) {
                alberi.add(rs.getString("nome"));
            }
            
            data.put("lista_alberi", alberi);
            // se ho almeno un albero salvato nel db offro possibilità di eseguire il calcolo
            if(!alberi.isEmpty()){
              TemplateManager.process("index2.html", data, response, getServletContext());
            } else { //altrimenti vado direttamente alla pagina di creazione albero
                TemplateManager.process("creazione.html", data, response, getServletContext());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CreazioneAlbero.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(CreazioneAlbero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
