/**
 * 
 */
package univaq.ppc.utility;


import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
/**
 * @author Luca
 *
 */
public class TemplateManager {
	/**
     * 
     * @param data              dati da inserire nel template          
     * @param path_template     pathname del template da caricare
     * @param response          
     * @param servlet_context
     * @throws IOException
     */
    public static void process(String path_template, Map data, HttpServletResponse response, ServletContext servlet_context) throws IOException{
        
        response.setContentType("text/html;charset=ISO-8859-1");        
        // Configurazione freemarker
        Configuration cfg = new Configuration();
        
        cfg.setDefaultEncoding("UTF-8");
            
        cfg.setServletContextForTemplateLoading(servlet_context, "/template");
        
        
        BeansWrapper wrapper = (BeansWrapper) cfg.getObjectWrapper();
        wrapper.setSimpleMapWrapper(true);
        cfg.setObjectWrapper(wrapper);
        //solo per debug --- mostra errori di template
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
        ///
        Template template = cfg.getTemplate(path_template);
        
        PrintWriter out = response.getWriter();
        
        try{
            template.process(data, out);
            
        } catch (TemplateException ex) {     
            
        } finally{
            out.flush();
            out.close(); 
        }
        
        

    }

    
    
}