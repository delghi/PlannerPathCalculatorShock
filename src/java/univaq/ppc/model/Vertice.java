/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Luca
 */
public class Vertice implements  Serializable{
    String nome;                    //nome
    int indice;                     //indice
    int valore;                     //valore
    String[] attributi;             //lista-attributi 
    Arco arcoEntrante = new Arco(); //arco-entrante
    int attr1Int;
    Random random = new Random();
    Map attributiVert = new HashMap();
    int id = 0;
    private static AtomicInteger next_id = new AtomicInteger(0);
    
    public Vertice(int indice, int valore) {
        this.indice = indice;
        this.valore = valore;
        this.nome = "Nome"+ this.indice;
        this.attr1Int = random.nextInt(98);
        this.arcoEntrante.attr2Int = random.nextInt(50);
        this.id = next_id.incrementAndGet();
    }
    
    public Vertice(int indice) {
        this.indice = indice;
        this.id = next_id.incrementAndGet();
        this.nome = "Nome"+ this.indice;
        this.attr1Int = random.nextInt(98);
        this.arcoEntrante.attr2Int = random.nextInt(50);
        
    }
    
    
    

    public int getindice() {
        return this.indice;
    }

    public String getNome() {
        return nome;
    }

    public int getId() {
        return this.id ;
    }

    public void setIndice(int id) {
        this.indice = id;
    }
    
    public int getValore() {
        return valore;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }

    
    
    public void setPesoArco(int i) {
        arcoEntrante.valore = i;
    }

    public int getValoreArcoEntrante() {
        return arcoEntrante.getValore();
    }

    public void addAttr(List<String> v, List<String> e) {
        //riempio attributi per vertice
        for (String attributo : v) {
            this.attributiVert.put(attributo, random.nextInt(98));
        }
        //per arco
        for(String attr: e) {
            this.arcoEntrante.attributiEdge.put(attr, random.nextInt(48));
        }
    }
    
    public void addSingleAttr (String name, int value) {
        this.attributiVert.put(name, value);
    }
    

    public Map getAttributiVert() {
        return attributiVert;
    }

    public Arco getArcoEntrante() {
        return arcoEntrante;
    }

    @Override
    public String toString() {
        return "Vertice{" + "nome=" + nome + ", indice=" + indice + ", valore=" + valore + ", attributi=" + attributi + ", arcoEntrante=" + arcoEntrante + ", attr1Int=" + attr1Int + ", random=" + random + ", attributiVert=" + attributiVert + '}';
    }
    
    
    
    
}
 