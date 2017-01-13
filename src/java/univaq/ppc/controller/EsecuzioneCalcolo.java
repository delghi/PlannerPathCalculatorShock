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
import univaq.ppc.model.Result;
import univaq.ppc.model.Vertice;
import univaq.ppc.utility.Database;
import univaq.ppc.utility.TemplateManager;

/**
 *
 * @author Luca
 */
public class EsecuzioneCalcolo extends HttpServlet {

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
        
        //prelevo dalla request i dati inviati dall'utente
        //passo 1: prendo dati principali dell'albero (name, split, depth)
        Albero selectedTree;
        //Map risultato = new HashMap(); //conterrà il risultato del calcolo
        Map data = new HashMap(); //serve per freeMarker (template)
        PrintWriter out = response.getWriter(); //test send alert
        String nomeAlbero = request.getParameter("nomeA");
        ResultSet rs = Database.selectRecord("albero", "nome='" + nomeAlbero + "'");
        int splitSize = 0, depth = 0, id_albero = 0;
  
        while(rs.next()) {
            splitSize = rs.getInt("splitsize");
            depth = rs.getInt("depth");
            id_albero = rs.getInt("id");
        }
        selectedTree = new Albero(nomeAlbero, splitSize, depth);
        //prendo i vertici dal db
        ResultSet rs2 = Database.selectRecord("vertice", "id_albero='" + id_albero + "'");
        int count = 0;
        while(rs2.next()) {
            //selectedTree.addVertice(new Vertice(rs2.getInt("vertexUid")-1), count);
            selectedTree.addVertice(new Vertice(count, count),count);
            count++;
        }
        //prendo archi
        ResultSet rs3 = Database.selectRecord("arco", "id_albero='" + id_albero + "'");
        count = 0;
        while(rs3.next()) {
            Vertice temp = selectedTree.getVertice(count);
            temp.setPesoArco(rs3.getInt("edgeUid")); 
            count++;
        }
        System.out.println("");
        //prendo prima  gli attributi dei vertici
        ResultSet rs4 = Database.selectJoinDet("vertice", "VertexAttrUsage", "vertice.vertexUid = VertexAttrUsage.objectVid", "AttrDef", "VertexAttrUsage.attrDefId = AttrDef.attrDefUid","vertice.id_albero='" + id_albero + "'");
        count = 0;
        while(rs4.next()) {
            if(count == selectedTree.getNumNodi()) count--; //evita errore import db
            Vertice temp = selectedTree.getVertice(count);
            temp.addSingleAttr(rs4.getString("name"), rs4.getInt("value"));
            count++;   
        }
        //prendo attributi archi
        ResultSet rs5 = Database.selectJoinDet("arco", "EdgeAttrUsage", "arco.edgeUid= EdgeAttrUsage.objectEdgeUid", "AttrDef", "EdgeAttrUsage.attrDefUid = AttrDef.attrDefUid","arco.id_albero='" + id_albero + "'");
        count = 0;
        while(rs5.next()) {
          if(count == selectedTree.getNumNodi()) count--; //evita errore import db
          Vertice temp = selectedTree.getVertice(count);
          temp.getArcoEntrante().setAttributiEdge(rs5.getString("name"), rs5.getInt("value"));
          count++;
        }
            System.out.println("---ALBERO RISCOSTRUITO----");
            //validazione parametri inseriti da utent
           int startV = Integer.parseInt(request.getParameter("vertice_start"));
           int endV = Integer.parseInt(request.getParameter("vertice_end")); 
           if(!(startV >= 0 && startV < endV && startV < selectedTree.getNumNodi())) {
               sendErrorMessage(" ERRORE(StartVertex) : Parametri inseriti non corretti. Riprova!", response, "creazioneAlbero");
           } 
           if(!(endV > 0 && endV > startV && endV <= selectedTree.getNumNodi())) {
               sendErrorMessage(" ERRORE(EndVertex) : Parametri inseriti non corretti. Riprova!", response, "creazioneAlbero");
           }
           //calcolo tempo 
           long inizio = System.nanoTime();
           Result risultato = eseguiCalcolo(selectedTree, selectedTree.getVertice(startV).getNome(), selectedTree.getVertice(endV).getNome());
           long tempoTrascorso = System.nanoTime() - inizio;
           
           List attraversati = risultato.getVerticiAttraversati();
           data.put("vertici", attraversati);
           data.put("result", risultato.getRisultato());
           data.put("tempo", tempoTrascorso);
           TemplateManager.process("risultato.html",data , response, getServletContext());
           
    
    }

    protected Result eseguiCalcolo (Albero albero, String start, String end) {
        Map<String, Integer> attributiV = new HashMap();
        Map<String, Integer> attributiA = new HashMap();
        Map<List,Map<String, Integer>> r = new HashMap();
        //contatori vertici e archi
        int resultV = 0, resultE = 0;
        int i; //conterrà indice vertice 
        //array che conterrà il risultato
        int [] result = new int [2];
        Map <String, Integer> risultato = new HashMap();
        //test
        List vertici_attraversati = new ArrayList();
        //end-test
        //Passo 1 : controllo se il vertice start è il root dell'albero
        if(albero.getVertice(0).getNome().equals(start)) {
            //variabile che uso per scorrere l'albero
            int treeSize = albero.getSize();
            
            //ciclo for scorre lista di vertici fin quando non trova indice dell'endVertex (i)
            for( i = 0; albero.getVertice(i).getNome() != end; i++) {}
             //test
             vertici_attraversati.add(albero.getVertice(i).getNome());
             //end-test
             attributiV = albero.getVertice(i).getAttributiVert();
             for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
             for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             
             //scorro albero finchè non trovo vertice start
             while(albero.getVertice(i).getNome() != start) {
                 //test
                 //end-test
                 //trovo il padre del vertice in esame
                 i = ((i - 1) / albero.getSplitSize());
                 if(i < 0) break; //Startvertex è il root
                 vertici_attraversati.add(albero.getVertice(i).getNome());
                 attributiV = albero.getVertice(i).getAttributiVert();
                 for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
                 //aggiungo attributi arco solo se il vertice in questione è != da root 
                 if(!albero.getVertice(i).getNome().equals("Nome0")){
                     attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
                    for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                        String key = entry.getKey();
                        int value = entry.getValue();
                        if(risultato.containsKey(key)) {
                            int get = risultato.get(key);
                            risultato.put(key, value + get);
                        } else {
                            risultato.put(key, value);
                        }
                    }
                 }
             }
             
             if(i < 0) {
                i = 0;
                attributiV = albero.getVertice(i).getAttributiVert();
                vertici_attraversati.add(albero.getVertice(i).getNome());
                for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
             for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
                 
             } else if(!albero.getVertice(i).getNome().equals("Nome0")) {
              vertici_attraversati.add(albero.getVertice(i).getNome());
                 attributiV = albero.getVertice(i).getAttributiVert();
                for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
             for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
                 
             }
             
             Result rs = new Result(vertici_attraversati, risultato);
             return rs;
             
             
        } else {
            //cerco vertice endVertex
            for(i = 0; albero.getVertice(i).getNome() != end; i++) {}
            
            //calcolo sugli attributi del vertice in esame
            attributiV = albero.getVertice(i).getAttributiVert();
            vertici_attraversati.add(albero.getVertice(i).getNome());
             for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
             for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
             //calcolo per trovare il padre del vertex in esame
             i = ((i -1)/albero.getSplitSize());
             //scorro l'albero fino a quando non trovo startVertex
             while (albero.getVertice(i).getNome() != start){
                 //risalgo al padre del vertice attuale e calcolo 
                  attributiV = albero.getVertice(i).getAttributiVert();
                  vertici_attraversati.add(albero.getVertice(i).getNome());
                for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
                attributiA = albero.getVertice(i).getArcoEntrante().getAttributiEdge();
               for (Map.Entry<String, Integer> entry : attributiA.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
               i = ((i-1)/albero.getSplitSize());
             }
             
              attributiV = albero.getVertice(i).getAttributiVert();
              vertici_attraversati.add(albero.getVertice(i).getNome());
                for (Map.Entry<String, Integer> entry : attributiV.entrySet()) {
                   String key = entry.getKey();
                   int value = entry.getValue();
                   if(risultato.containsKey(key)) {
                       int get = risultato.get(key);
                       risultato.put(key, value + get);
                   } else {
                       risultato.put(key, value);
                   }
                }
                Result rs = new Result(vertici_attraversati, risultato);
                return rs;

        }
     
    }
    
    protected void sendErrorMessage (String error, HttpServletResponse response, String location) throws IOException {
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("alert('ERRORE!"+ error + "');");
        out.println("window.location = '"+ location+ "';");
        out.println("</script>");
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
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(EsecuzioneCalcolo.class.getName()).log(Level.SEVERE, null, ex);
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
            
            PrintWriter out = response.getWriter();//test send alert
            //check su dati inseriti dall'utente (end, start)
            if (!request.getParameter("vertice_start").equals("") && !request.getParameter("vertice_end").equals("")){
                processRequest(request, response);
            } else {
                out.println("<script type=\"text/javascript\">");
                out.println("alert('ERRORE! Inserire Vertice Start e/o Vertice End');");
                out.println("window.location = 'creazioneAlbero';");
                out.println("</script>");
            }
        } catch (SQLException ex) {
            Logger.getLogger(EsecuzioneCalcolo.class.getName()).log(Level.SEVERE, null, ex);
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
