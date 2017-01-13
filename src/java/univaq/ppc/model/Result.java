/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.model;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Luca
 */
public class Result {
    private List verticiAttraversati;
    private Map risultato;

    public Result(List verticiAttraversati, Map risultato) {
        this.verticiAttraversati = verticiAttraversati;
        this.risultato = risultato;
    }

    public List getVerticiAttraversati() {
        return verticiAttraversati;
    }

    public void setVerticiAttraversati(List verticiAttraversati) {
        this.verticiAttraversati = verticiAttraversati;
    }

    public Map getRisultato() {
        return risultato;
    }

    public void setRisultato(Map risultato) {
        this.risultato = risultato;
    }
    
    
    
    
}
