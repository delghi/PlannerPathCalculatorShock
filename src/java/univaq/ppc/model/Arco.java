/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;



/**
 *
 * @author Luca
 */
public class Arco implements Serializable{

    int id = 0;
    int valore;
    int attr2Int; //prova
    String[] attr; //lista attributi archi
    Map attributiEdge = new HashMap();
    private static AtomicInteger next_id = new AtomicInteger(0);

    public Arco(int valore, String[] attr) {
        this.valore = valore;
        for (int i = 0; i < attr.length; i++) {  ////riempimento attributelist dell'arco
            this.attr[i] = attr[i];
        }
        this.attr = attr;
    }

    public Arco() {
        this.id = next_id.incrementAndGet();
    }

    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getPeso() {
        return valore;
    }

    public void setPeso(int valore) {
        this.valore = valore;
    }

    public int getValore() {
        return valore;
    }

    public Map getAttributiEdge() {
        return attributiEdge;
    }
    
    public void setAttributiEdge(String name, int value) {
        this.attributiEdge.put(name, value);
    }

    @Override
    public String toString() {
        return "Arco{" + "valore=" + valore + ", attr2Int=" + attr2Int + ", attr=" + attr + ", attributiEdge=" + attributiEdge + '}';
    }
    
    
    
}
