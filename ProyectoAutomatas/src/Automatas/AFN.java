
package Automatas;

import Automatas.AFD.Automata;
import java.util.ArrayList;
import java.io.*;
 
public class AFN {
    
 
    private  ArrayList<Character>sigma;
    private  ArrayList<String> states;
    private  String q;
    private  ArrayList<String> finalStates;
    private  ArrayList<String>[][] delta;

    
    public AFN(ArrayList<Character> sigma, ArrayList<String> states, String q, ArrayList<String> finalStates, ArrayList<String>[][] delta) {
        this.sigma = sigma;
        this.states = states;
        this.q = q;
        this.finalStates = finalStates;
        this.delta = delta;
    }

        public String getQ() {
            return q;
        }
    
    
    
    public  boolean processStringAFN(String cadena,String estado,int letra){
       int i=0;
        //estado Actual
       String state;
       //pocision del estado actual
       int posState;
       //simbolo actual
       String symbol;
       //posicion del simbolo actual            
       int posSymbol;
       //letra
       int posChar = letra;
       //asignamos el estado Actual
       state = estado;
       
       //empezamos el proceso
       System.out.println("posChar :"+posChar);
           
       posState = getRow(state);
       symbol = Character.toString(cadena.charAt(posChar));
       posChar++;
       posSymbol=getColumn(symbol);
       System.out.println("estado :"+state+">"+symbol);
       if(!this.delta[posState][posSymbol].isEmpty()){

        for(i=0;i<this.delta[posState][posSymbol].size();i++){           
           if(posChar<=cadena.length()-1){ 
               state = this.delta[posState][posSymbol].get(i);
               //System.out.println("estado :"+state+">"+symbol);
               processStringAFN(cadena,state,posChar);
           }
        }
       }else{
           System.out.println("procesamiento abortado");
         return false;
       }
       
       //verificando si esta en estado de aceptacion
        if(posChar == cadena.length()){
       for(i = 0; i < this.finalStates.size(); i++){
                
                if(state.equals(this.finalStates.get(i))){
                    System.out.println("Cadena Aceptada");
                    return true;
                }
                
       }
       
       //si supera el for
       System.out.println("Cadena No Aceptada");
       return false;
        }
       return false;
    }
    
    public  int getRow(String state){
            //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
            for(int i = 0; i < this.states.size(); i++ ){
                //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
                if(state.equals(this.states.get(i))){    
                    return i;
                }
                
            }
            return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
        }
    
    public  int getColumn(String symbol){
            //esta función es para obtener la columna en la que se encuentra un simbolo (se asume fila 0 )
            for(int i = 0; i < this.sigma.size(); i++ ){
                //solo nos interesa la los elementos de la primera fila entonces por eso la fijamos en [0][i]
                if(symbol.equals(Character.toString(this.sigma.get(i)))){
                    return i;
                }
                
            }
            return -1; // esto nunca deberia pasar a no se que pase un error de digitación
        }
  
    
 }




