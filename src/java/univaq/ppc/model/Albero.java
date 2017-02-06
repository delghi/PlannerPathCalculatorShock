/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package univaq.ppc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Luca
 */
public class Albero {
    
    String nome;
    int splitSize; //archi entranti
    int depth; //altezza albero 
    Vertice [] albero = new Vertice[0];  //lista vertici dell'albero
    int numNodi;
    //test
    List attributi = new ArrayList();
    int id;
    

    public Albero(String nome, int splitSize, int depth, List attrTemp) {
        this.nome = nome;
        this.splitSize = splitSize;
        this.depth = depth;
        this.numNodi = getNumeroNodi(depth, splitSize); // 
        albero = new Vertice[numNodi];//assegno all'array 'albero' una lunghezza pari al numero totale dei vertici
        this.attributi = attrTemp;
    }
    
    public static int getNumeroNodi (int depth, int splitSize){
        int result = 0; 
	for(int i = 0; i<depth; i++) {
            result += Math.pow(splitSize, i);
	}
	System.out.println("NUMERO DI VERTICI:   ");
        System.out.println(result);
        
        return result;
    }

    public Albero() {
    }

    public Albero(String nome, int splitSize, int depth) {
        this.nome = nome;
        this.splitSize = splitSize;
        this.depth = depth;
        this.numNodi = getNumeroNodi(depth, splitSize); // 
        albero = new Vertice[numNodi];
    }

    public Albero(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    
    
    //metodi getter

    public int getSize() {
        return this.numNodi;
    }

    public String getNome() {
        return this.nome;
    }
    //aggiunge un vertice 'v' nella posizione 'k' dell'array 'albero'
    public void addVertice (Vertice v, int k) {
        this.albero[k] = v;
    }
    
    public Vertice getVertice(int i) {
        for(int j = 0; j < albero.length; j++){ //scorre l'intero albero alla ricerca del vertice con indice 'i'
            if (i == albero[j].getindice()) return albero[j]; //quando trovato ce lo restituisce in output (con indice valore)
        }
        return null; //altrimenti restituisce 'null'
    }

    public int getSplitSize() {
        return splitSize;
    }

    public void setSplitSize(int splitSize) {
        this.splitSize = splitSize;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
    //test

    public Vertice[] getAlbero() {
        return albero;
    }

    public List getAttributi() {
        return attributi;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumNodi(int numNodi) {
        this.numNodi = numNodi;
    }

    public void setAttributi(List attributi) {
        this.attributi = attributi;
    }

    public int getNumNodi() {
        return numNodi;
    }
    
        
    
    public void creaAlbero(List attributiV, List attributiE) {
        int size = this.getSize(); //numero nodi totali albero
        Random random = new Random(); // serve per generare numero random
        
        for (int i = 0; i < size; i++) {
            int num1 = random.nextInt(100); //valore random da assegnare ai vertici
            int num2 = random.nextInt(70); // valore random da assegnare agli archi
            
            this.albero[i] = new Vertice(i, num1);
            this.albero[i].addAttr(attributiV, attributiE);
            
            if (i == 0) {
                this.albero[i].setPesoArco(0);  // assegna peso 0 se si tratta di vertice padre
            } else {
                this.albero[i].setPesoArco(num2); //assegna random altrimenti
            }
        }
        
    }
    
    
    
    
}
