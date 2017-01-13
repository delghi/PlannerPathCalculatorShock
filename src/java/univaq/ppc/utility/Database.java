/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.utility;



import freemarker.ext.beans.HashAdapter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import univaq.ppc.model.Albero;
import univaq.ppc.model.Vertice;


public class Database {
	

	private static Connection db;
    /**
     * Connessione al database
     * @throws Exception 
     */
 
    
    	public static void connect() throws NamingException, SQLException {
        InitialContext ctx = new InitialContext();
        DataSource ds = (DataSource) ctx.lookup("java:comp/env/plannerpath");
        Database.db = ds.getConnection();
    }
    	
    	
    /**
     * Chiusura connessione al database
     * @throws java.sql.SQLException
     */
    public static void close() throws SQLException{
        Database.db.close();
    }
    
    /**
     * Select all record
     * @param table         tabella da cui prelevare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              dati prelevati
     * @throws java.sql.SQLException    
     */
    
    public static ResultSet selectAllRecord(String table) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table ;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
    
      /**
     * Select record con condizione
     * @param table         tabella da cui prelevare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              dati prelevati
     * @throws java.sql.SQLException    
    
      /**
     * Select record con condizione
     * @param table         tabella da cui prelevare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              dati prelevati
     * @throws java.sql.SQLException
     */
    
    
    
    public static ResultSet selectRecord(String table, String condition) throws SQLException {
        // Generazione query
        String query = "SELECT * FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    /**
     * Select record con condizione e ordinamento
     * @param table         tabella da cui prelevare i dati
     * @param condition     condizione per il filtro dei dati
     * @param order         ordinamento dei dati
     * @return              dati prelevati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectRecord(String table, String condition, String order) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table + " WHERE " + condition + " ORDER BY " + order;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
    /**
     * Select record con join tra due tabelle
     * @param table_1           nome della prima tabella
     * @param table_2           nome della seconda tabella
     * @param join_condition    condizione del join tra la tabelle
     * @param where_condition   condizione per il filtro dei dati
     * @return                  dati prelevati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectJoin(String table_1, String table_2, String join_condition, String where_condition) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " WHERE " + where_condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
    

    /**
     * Select record con join tra due tabelle e ordinamento
     * @param table_1           nome della prima tabella
     * @param table_2           nome della seconda tabella
     * @param join_condition    condizione del join tra la tabelle
     * @param group
     * @param where_condition   condizione per il filtro dei dati
     * @param order             ordinamento dei dati
     * @return                  dati prelevati
     * @throws java.sql.SQLException
     */
    public static ResultSet selectJoin(String table_1, String table_2, String join_condition, String group, String order, int limit) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " GROUP BY " + group + " ORDER BY " + order + " DESC " + " LIMIT " + limit;
        // Esecuzione query
        return Database.executeQuery(query);
    }
    
     public static ResultSet selectJoinDet(String table_1, String table_2, String join_condition, String table_3, String join_condition2, String condition) throws SQLException{
        // Generazione query
        String query = "SELECT * FROM " + table_1 + " JOIN " + table_2 + " ON " + join_condition + " JOIN " + table_3 + " ON " + join_condition2 +" WHERE " + condition;
        // Esecuzione query
        return Database.executeQuery(query);
    }

   
    
  
   
    
    // fine provaaaa
    /**
     * Update record
     * @param table         tabella in cui aggiornare i dati
     * @param data          dati da inserire
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'inserimento è andato a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean updateRecord(String table, Map<String,Object> data, String condition) throws SQLException{
        // Generazione query
        String query = "UPDATE " + table + " SET ";
        Object value;
        String attr;
        
        for(Map.Entry<String,Object> e:data.entrySet()){
            attr = e.getKey();
            value = e.getValue();
            if(value instanceof String){
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }else{
                query = query + attr + " = " + value + ", ";
            }
            
            
        }
        query = query.substring(0, query.length()-2) + " WHERE " + condition;
        
        // Esecuzione query
        return Database.updateQuery(query);
    }
    
    /**
     * Delete record
     * @param table         tabella in cui eliminare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              true se l'eliminazione è andata a buon fine, false altrimenti
     * @throws java.sql.SQLException
     */
    public static boolean deleteRecord(String table, String condition) throws SQLException{
        // Generazione query
        String query = "DELETE FROM " + table + " WHERE " + condition;
        // Esecuzione query
        return Database.updateQuery(query);
    }
    
    /**
     * Count record
     * @param table         tabella in cui contare i dati
     * @param condition     condizione per il filtro dei dati
     * @return              numero dei record se la query è stata eseguita on successo, -1 altrimenti
     * @throws java.sql.SQLException
     */
    public static int countRecord(String table, String condition) throws SQLException{

        // Generazione query
        String query = "SELECT COUNT(*) FROM " + table + " WHERE " + condition;
        // Esecuzione query
        ResultSet record = Database.executeQuery(query);
        record.next();
        // Restituzione del risultato
        return record.getInt(1);

    }
    
    /**
     * Imposta a NULL un attributo di una tabella  
     * @param table         tabella in cui è presente l'attributo
     * @param attribute     attributo da impostare a NULL
     * @param condition     condizione
     * @return
     * @throws java.sql.SQLException
     */
    public static boolean resetAttribute(String table, String attribute, String condition) throws SQLException{
        String query = "UPDATE " + table + " SET " + attribute + " = NULL WHERE " + condition;
        return Database.updateQuery(query);
    }

    // <editor-fold defaultstate="collapsed" desc="Metodi ausiliari.">
    
    /**
     * executeQuery personalizzata
     * @param query query da eseguire
     */
    private static ResultSet executeQuery(String query) throws SQLException{
        Statement s1 = Database.db.createStatement();
        ResultSet records = s1.executeQuery(query);

        return records; 
            
    }
    
    /**
     * updateQuery personalizzata
     * @param query query da eseguire
     */
    private static boolean updateQuery(String query) throws SQLException{
        
        Statement s1;
        
        s1 = Database.db.createStatement();
        s1.executeUpdate(query); 
        s1.close();
        return true; 

    }
   // </editor-fold>
     public static boolean insertRecord(String table, Map<String, Object> data) throws SQLException {
        // Generazione query
        String query = "INSERT INTO " + table + " SET ";
        Object value;
        String attr;

        for (Map.Entry<String, Object> e : data.entrySet()) {
            attr = e.getKey();
            value = e.getValue();
            if (value instanceof Integer) {
                query = query + attr + " = " + value + ", ";
            } else {
                value = value.toString().replace("\'", "\\'");
                query = query + attr + " = '" + value + "', ";
            }
        }
        query = query.substring(0, query.length() - 2);
        // Esecuzione query
        return Database.updateQuery(query);
    }
     
     
     public static boolean insertAlbero (String table, Albero albero) throws SQLException {
       
        // passo 1:  Inserisco prima i dati principali dell' albero
        String nome_albero = albero.getNome();
        int split = albero.getSplitSize();
        int depth = albero.getDepth();
        Map dataTree = new HashMap();
        dataTree.put("nome", nome_albero);
        dataTree.put("splitsize", split);
        dataTree.put("depth", depth);
        Database.insertRecord("albero", dataTree);
       
        ResultSet rs = Database.selectRecord("albero", "nome='" + dataTree.get("nome") + "'");
        int idAlbero = 0;
        while(rs.next()){
            idAlbero = rs.getInt("id");  
         }
        
         // passo 2: inserisco def attributi vertex e edge
        Map attrDefinition = new HashMap();
        List<String> attrTemp = albero.getAttributi();
        // scorro lista e aggiungo definizione degli attributi
        Map<String,Integer> attrDef = new HashMap();
        for (String attr : attrTemp) {
            attrDefinition.put("name", attr);
            attrDefinition.put("nomeAlbero", albero.getNome());
            Database.insertRecord("AttrDef", attrDefinition);
            //passo 3 : prendo id degli attributi appena inseriti
            
            
            ResultSet rs2 = selectRecord("AttrDef", "nomeAlbero='" + albero.getNome() + "'");
            while(rs2.next()) {    
                attrDef.put(attr, rs2.getInt("attrDefUid"));
            }
        }
   
        // passo 4: inserisco ogni vertice dell'albero
        Vertice[] listavertici = albero.getAlbero();
        for (int i = 0; i<listavertici.length; i++) {
            
         //Database.insertVertice("vertice", listavertici[i], idAlbero);
         Map vertex = new HashMap();   
         String nomeV = listavertici[i].getNome();
         int arco_entrante = listavertici[i].getValoreArcoEntrante();
         int edgeId = 0;
         Map edge = new HashMap();
         edge.put("valore", arco_entrante);
         edge.put("id_albero", idAlbero);
         Database.insertRecord("arco", edge);
         ResultSet rs3 = Database.selectRecord("arco", "id_albero='" + idAlbero + "'");
         while(rs3.next()) {
              edgeId = rs3.getInt("edgeUid");
         }
         vertex.put("id_albero", idAlbero);
         vertex.put("nome", nomeV);
         vertex.put("arco_entrante", edgeId);
         Database.insertRecord("vertice", vertex);
         int vertexId = 0;
         ResultSet rs4 = Database.selectRecord("vertice", "id_albero='" + idAlbero + "'");
         while(rs4.next()){
             vertexId = rs4.getInt("vertexUid");
         }
         //passo 5: inserimento valori attributi vertice
         vertex.clear();
         vertex = listavertici[i].getAttributiVert();
         Map vertexAttrUsage = new HashMap();
         Integer attrDefId = 0;
         for (Map.Entry<String, Integer> entry : attrDef.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            attrDefId = value;
            if(vertex.containsKey(key)) {
                vertexAttrUsage.put("objectVid", vertexId);
                vertexAttrUsage.put("attrDefId", attrDefId);
                vertexAttrUsage.put("value", vertex.get(key));
                Database.insertRecord("VertexAttrUsage", vertexAttrUsage);
            }
         }
         //passo 6: inserimento valori attributi arco
         edge.clear();
         edge = listavertici[i].getArcoEntrante().getAttributiEdge();
         Map edgeAttrUsage = new HashMap();
         for (Map.Entry<String, Integer> entry : attrDef.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            attrDefId = value;
             if (edge.containsKey(key)) {
               edgeAttrUsage.put("objectEdgeUid", edgeId);
               edgeAttrUsage.put("attrDefUid", attrDefId);
               edgeAttrUsage.put("value", edge.get(key));
               Database.insertRecord("EdgeAttrUsage", edgeAttrUsage);  
             }
            
         }
        
         
         
        }
        
        
        
         
         return true;
     }
     
     public static boolean insertVertice (String table, Vertice vertice, int id_albero) throws SQLException {
         
         String nome = vertice.getNome();
         int vertexUid = vertice.getIndice();
         int arco_entrante = vertice.getValoreArcoEntrante();
         int edgeId = 0;
         Map data = new HashMap();
         //inserisco prima edge per avere il suo id
         Map edge = new HashMap();
         edge.put("valore", arco_entrante);
         edge.put("id_albero", id_albero);
         Database.insertRecord("arco", edge);
         ResultSet rs = Database.selectRecord("arco", "id_albero='" + id_albero + "'");
         while(rs.next()) {
              edgeId = rs.getInt("edgeUid");
         }
         data.put("id_albero", id_albero);
         data.put("nome", nome);
//         data.put("vertexUid", vertexUid);
//         data.put("arco_entrante", arco_entrante);
         data.put("arco_entrante", edgeId);
         Database.insertRecord("vertice", data);
         
        
         
         return true;
     }
     
}

