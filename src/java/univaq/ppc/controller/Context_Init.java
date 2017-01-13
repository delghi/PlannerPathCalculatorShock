/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.controller;



import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebServlet;
import univaq.ppc.utility.Database;


/**
 *
 * @author Luca
 */
@WebServlet(name = "ContextListener", urlPatterns = {"/ContextListener"})
public class Context_Init implements ServletContextListener {

    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        
        try {
            Database.connect();
        } catch (NamingException | SQLException ex) {
            Logger.getLogger(Context_Init.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            Database.close();
        } catch (SQLException ex) {
            Logger.getLogger(Context_Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}