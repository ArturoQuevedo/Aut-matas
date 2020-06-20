/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;
import Automatas.*;
import static Pruebas.ClasePrueba.probarAFNLambda;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Equipo
 */
public class Temporal_AFN_LambdaToAFN {
    
    
    public static ArrayList<String> ProcessStatesWithSymbol(ArrayList<String> states,String symbol, AFN_Lambda afnl){
        
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0;i < states.size();i++){
            
            int row = afnl.getPosEstado(states.get(i));
            int column = afnl.getPosSimbolo(symbol);
            ArrayList<String> temp = new ArrayList<>();
            temp = afnl.getDelta()[row][column]; // Aqui guardo la info que me dio delta para un usarla mas facil
            
                for(int j = 0; j <temp.size(); j++){
                    
                    if(!result.isEmpty() && !temp.isEmpty()){ // confirmo esto para evitar que el programa se muera en ciertos casos
                        if(!result.contains(temp.get(j))){ // confirmo que no se haya agregado antes
                            
                            result.add(temp.get(j)); // agrego los estados
                            
                        }
                    }else if(result.isEmpty() && !temp.isEmpty()){
                        
                        result.add(temp.get(j));
                        
                    }
            
                }
            
        }

        return result;
    }
    
    
    public static AFN AFN_LambdaToAFN(AFN_Lambda afnl){
    
        AFN afn = new AFN();//Falta ponerle todos los datos al AFN pero tendria que modificar la clase AFN
        int rowsNumber = afnl.getStates().size();
        int columnsNumber = afnl.getSigma().size();
                
        ArrayList<String>[][]newDelta = new ArrayList[rowsNumber][columnsNumber]; // este es el delta del nuevo AFN
        //inicializando newDelta
            for (int i = 0; i < afnl.getStates().size(); i++) {
                for (int j = 0; j < afnl.getSigma().size(); j++) {
                    newDelta[i][j] = new ArrayList<String>();
                }
            }
            
        
        //Imprimiendo lambda clausuras de cada estado 
        afnl.calcularMuchasLambdaClausura(afnl.getStates());
        //Llenando la matriz newDelta
            for(int i = 0; i < rowsNumber;i++){
                for(int j = 0; j < columnsNumber-1;j++){
                    ArrayList<String> states = new ArrayList<>();
                    states = afnl.calcularLambdaClausura(afnl.getStates().get(i));// Aqui inicia el proceso de calcular la lambda clausura de un estado
                    String symbol = Character.toString(afnl.getSigma().get(j));
                    System.out.print("D("+afnl.getStates().get(i)+","+symbol+") = ");
                    System.out.print("$[D($["+afnl.getStates().get(i)+"],"+symbol+")] = ");
                    System.out.print("$[D({");
                    
                    for(int k = 0; k < states.size(); k ++){
                        System.out.print(states.get(k));
                            if(k!= states.size()-1){
                                System.out.print(",");
                            };
                    }
                    
                    System.out.print("},"+symbol+")] = ");
                
                    ArrayList<String> states2 = new ArrayList<>();//Ahora se mire a donde se llega con un simbolo y la lambda clausura del estado anterior
                    states2 =ProcessStatesWithSymbol(states,symbol, afnl); // y los estados resultantes son states2
                    Collections.sort(states2);
                    
                    System.out.print("$[{");
                    
                    for(int k = 0; k < states2.size(); k ++){
                        System.out.print(states2.get(k));
                            if(k!= states2.size()-1){
                                System.out.print(",");
                            };
                    }
                    
                    System.out.print("}] = ");
                    
                    // esta son las nuevas transiciones para el estado y el simbolo en el nuevo AFN
                    ArrayList<String> states3 = new ArrayList<>();
                    states3 = afnl.calcularMuchasLambdaClausuraSinImprimir(states2);
                    Collections.sort(states3);
                    System.out.print("{");
                    
                    for(int k = 0; k < states3.size(); k ++){
                        System.out.print(states3.get(k));
                            if(k!= states3.size()-1){
                                System.out.print(",");
                            };
                    }
                    
                    System.out.print("}.");
                    System.out.println("");
                    
                    newDelta[i][j] = states3;
                }
                
            }
        
            
            
        return afn;
    };
    
    
    
    
    public static void main(String[] args) throws Exception {
        
        AFN_Lambda afnl = new AFN_Lambda();// eso no es un "uno" es una "l" :v
        afnl.initializeAFD("AFN_Lambda2.txt"); // Aqui se debe poner el nombre del archivo que se desea leer
        AFN_LambdaToAFN(afnl);
        

    }
    
    
}
