/*
 * Servlet Start (HomePage)- chiamata in GET controlla 
 * per prima cosa se sono presenti nel DB alberi precedentemente inseriti. In caso di 
 * esito positivo l'utente può:
 * 1) effettuare il calcolo utilizzando la form presente 
 * 2) cliccare sul button di creazione nuovo albero
 * 3) cliccare sul button di eliminazione albero
 * 
 *
 */
package univaq.ppc.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import univaq.ppc.model.Vertice;
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
            throws ServletException, IOException, SQLException, ClassNotFoundException {
        
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
         
        //creazione Albero
        Albero albero = new Albero(nome, splitSize, depth, attrTemp);
        albero.creaAlbero(attributiV, attributiE);
        System.out.println(albero);
        //inserisco albero 
         Map dataTree = new HashMap();
        dataTree.put("nome", albero.getNome());
        dataTree.put("splitsize", albero.getSplitSize());
        dataTree.put("depth", albero.getDepth());
        Database.insertRecord("albero", dataTree);
        ResultSet rs = Database.selectRecord("albero", "nome='" + dataTree.get("nome") + "'");
        int idAlbero = 0;
        while(rs.next()){
            idAlbero = rs.getInt("id");  
         }
        //inserisco definizione attributi vertice
        Map attrDefinition = new HashMap();
        
        for (String attr : attributiV) {
            attrDefinition.put("name", attr);
            attrDefinition.put("nomeAlbero", albero.getNome());
            attrDefinition.put("id_albero", idAlbero);
            Database.insertRecord("attr_def", attrDefinition);
            ResultSet rs2 = Database.selectRecord("attr_def", "nomeAlbero='" + albero.getNome() + "'");
            while(rs2.next()) {    
                attrDefinition.put(attr, rs2.getInt("attrDefUid"));
            }
        }
        Map attrDefinitionEdge = new HashMap();
         for (String attr : attributiE) {
            attrDefinitionEdge.put("name", attr);
            attrDefinitionEdge.put("nomeAlbero", albero.getNome());
            attrDefinitionEdge.put("id_albero", idAlbero);
            Database.insertRecord("attr_def", attrDefinitionEdge);
            ResultSet rs2 = Database.selectRecord("attr_def", "nomeAlbero='" + albero.getNome() + "'");
            while(rs2.next()) {    
                attrDefinitionEdge.put(attr, rs2.getInt("attrDefUid"));
            }
        }
        
        //se non è il primo albero che viene inserito prendo gli id 
        Boolean isFirst = false;
        int vertexId = 0;
        int edgeId = 0;
        int vertexUsageUid = 0;
        int edgeAttrUid = 0;
        ResultSet rsVert = Database.selectLastId("vertice", "vertice.vertexUid");
            
                while(rsVert.next()) {
                    vertexId = rsVert.getInt("vertexUid") +1 ;
                } 
                if (vertexId != 0) {
                   
            ResultSet rsArco = Database.selectLastId("arco", "arco.edgeUid");
            while(rsArco.next()) {
                    edgeId = rsArco.getInt("edgeUid") + 1;
                }
            ResultSet rsVAttrUsage = Database.selectLastId("vertex_attr_usage", "vertex_attr_usage.vertexUsageUid");
            while(rsVAttrUsage.next()) {
                    vertexUsageUid = rsVAttrUsage.getInt("vertexUsageUid") + 1;
            }
            ResultSet rsEAttrUsage = Database.selectLastId("edge_attr_usage", "edge_attr_usage.edgeAttrUid");
            while(rsEAttrUsage.next()) {
                    edgeAttrUid = rsEAttrUsage.getInt("edgeAttrUid") + 1;
                }
            } else isFirst = true;
        
        
        String COMMA_DELIMITER = ",";
        String NEW_LINE = "\n";
        //scrivo vertici su fileCSV 
        FileWriter fileWriter = new FileWriter("/Applications/MAMP/db/mysql/plannerpath/vertici.csv");
        Vertice[] vertici = albero.getAlbero();
        System.out.println("inizio scrittura vertici");
        for(int i = 0; i<vertici.length; i++) {
            if(isFirst == false) vertici[i].setIndice(vertexId+i);
            fileWriter.append(String.valueOf(vertici[i].getindice()));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(idAlbero));
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(vertici[i].getNome());
            fileWriter.append(COMMA_DELIMITER);
            fileWriter.append(String.valueOf(vertici[i].getArcoEntrante().getId()));
            fileWriter.append(NEW_LINE);
        }
        fileWriter.flush();
        fileWriter.close();
        System.out.println("fine scrittura vertici");
        //
        
        FileWriter fileWriterA = new FileWriter("/Applications/MAMP/db/mysql/plannerpath/archi.csv");
        System.out.println("inizio scrittura archi");
        for(int i = 0; i<vertici.length; i++) {
            if(isFirst == false) vertici[i].getArcoEntrante().setId(edgeId + i);
            fileWriterA.append(String.valueOf(vertici[i].getArcoEntrante().getId()));
            fileWriterA.append(COMMA_DELIMITER);
            fileWriterA.append(String.valueOf(vertici[i].getArcoEntrante().getValore()));
            fileWriterA.append(COMMA_DELIMITER);
            fileWriterA.append(String.valueOf(idAlbero));
            fileWriterA.append(NEW_LINE);
        }
        fileWriterA.flush();
        fileWriterA.close();
         System.out.println("fine scrittura archi");
         
         
          FileWriter fileWriterAttrV = new FileWriter("/Applications/MAMP/db/mysql/plannerpath/attributiVertici.csv");
          
              
        System.out.println("inizio scrittura attributi vertice");
        for(int i = 0; i<vertici.length; i++) {
             vertexUsageUid++;
            for(String attr : attributiV){
            fileWriterAttrV.append(String.valueOf(vertexUsageUid));
            fileWriterAttrV.append(COMMA_DELIMITER);
            fileWriterAttrV.append(String.valueOf(vertici[i].getindice()));
            fileWriterAttrV.append(COMMA_DELIMITER);
            fileWriterAttrV.append(String.valueOf(attrDefinition.get(attr)));
            fileWriterAttrV.append(COMMA_DELIMITER);
            fileWriterAttrV.append(String.valueOf(vertici[i].getAttributiVert().get(attr)));
            fileWriterAttrV.append(NEW_LINE);
            }
        }
        fileWriterAttrV.flush();
        fileWriterAttrV.close();
         System.out.println("fine scrittura attributi Vert");
        
        
         
        FileWriter fileWriterAttrA = new FileWriter("/Applications/MAMP/db/mysql/plannerpath/attributiArchi.csv"); 
        System.out.println("inizio scrittura attributi archi");
        for(int i = 0; i<vertici.length; i++) {
            edgeAttrUid++;
            for(String attr : attributiE){
            fileWriterAttrA.append(String.valueOf(edgeAttrUid));
            fileWriterAttrA.append(COMMA_DELIMITER);
            fileWriterAttrA.append(String.valueOf(vertici[i].getArcoEntrante().getId()));
            fileWriterAttrA.append(COMMA_DELIMITER);
            fileWriterAttrA.append(String.valueOf(attrDefinitionEdge.get(attr)));
            fileWriterAttrA.append(COMMA_DELIMITER);
            fileWriterAttrA.append(String.valueOf(vertici[i].getArcoEntrante().getAttributiEdge().get(attr)));
            fileWriterAttrA.append(NEW_LINE);
            }
        }
        fileWriterAttrA.flush();
        fileWriterAttrA.close();
        System.out.println("fine scrittura attributi archi");
        
       
        System.out.println("+++DEBUG PRINT+++");
        System.out.println("+++INIZIO INSERIMENTO SU DB+++");
        System.out.println("+++INSERIMENTO ARCHI+++");
         Database.insertCSV("arco", "archi.csv");
         System.out.println("+++INSERIMENTO VERTICI+++");
         Database.insertCSV("vertice","vertici.csv"); 
         System.out.println("+++INSERIMENTO ATTRIBUTI VERTICI+++");
         Database.insertCSV("vertex_attr_usage","attributiVertici.csv"); 
         System.out.println("+++INSERIMENTO ATTRIBUTI ARCHI+++");
         Database.insertCSV("edge_attr_usage","attributiArchi.csv"); 
          System.out.println("+++FINE INSERIMENTO SU DB+++");
         
         
        //Database.insertAlbero("albero", albero);
        List alberi = new ArrayList();
       
        ResultSet rs4 = Database.selectAllRecord("albero");
       Map data = new HashMap();
       while(rs4.next()) {
           alberi.add(rs4.getString("nome"));
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
        } catch (ClassNotFoundException ex) {
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
