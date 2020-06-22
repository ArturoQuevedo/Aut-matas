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
    
    
    public static AFN AFN_LambdaToAFN(AFN_Lambda afnl) {

        System.out.println("Iniciando transformacion de AFN_Lambda a AFN\n\n");
        
        AFN afn = new AFN();
        int rowsNumber = afnl.getStates().size();
        int columnsNumber = afnl.getSigma().size();

        ArrayList<String>[][] newDelta = new ArrayList[rowsNumber][columnsNumber]; // este es el delta del nuevo AFN
        ArrayList<String> newFinalStates = new ArrayList<>();//estos son los nuevos estados de aceptación
        ArrayList<Character> newSigma = new ArrayList<>();

        for (int u = 0; u < afnl.getSigma().size() - 1; u++) {

            newSigma.add(afnl.getSigma().get(u));

        }
        ArrayList<String> comparación = new ArrayList<>();//estos son los nuevos estados de aceptación
        //inicializando newDelta
        for (int i = 0; i < afnl.getStates().size(); i++) {
            for (int j = 0; j < afnl.getSigma().size(); j++) {
                newDelta[i][j] = new ArrayList<String>();
            }
        }

        //calculando los nuevos estados de aceptación
        for (int i = 0; i < afnl.getStates().size(); i++) {

            comparación.clear();
            comparación = (ArrayList<String>) afnl.calcularLambdaClausura(afnl.getStates().get(i)).clone();

            for (int j = 0; j < afnl.getFinalStates().size(); j++) {

                if (comparación.contains(afnl.getFinalStates().get(j))) {

                    if (!newFinalStates.isEmpty()) {

                        if (!newFinalStates.contains(afnl.getStates().get(i))) {
                            newFinalStates.add(afnl.getStates().get(i));
                        }

                    } else {
                        newFinalStates.add(afnl.getStates().get(i));
                    }

                }

            }

        }

        System.out.println("");
        Collections.sort(newFinalStates);

        //Imprimiendo lambda clausuras de cada estado 
        afnl.calcularMuchasLambdaClausura(afnl.getStates());
        System.out.println("");
        //Llenando la matriz newDelta
        for (int i = 0; i < rowsNumber; i++) {
            for (int j = 0; j < columnsNumber - 1; j++) {
                ArrayList<String> states = new ArrayList<>();
                states = afnl.calcularLambdaClausura(afnl.getStates().get(i));// Aqui inicia el proceso de calcular la lambda clausura de un estado
                String symbol = Character.toString(afnl.getSigma().get(j));
                System.out.print("D(" + afnl.getStates().get(i) + "," + symbol + ") = ");
                System.out.print("$[D($[" + afnl.getStates().get(i) + "]," + symbol + ")] = ");
                System.out.print("$[D({");

                for (int k = 0; k < states.size(); k++) {
                    System.out.print(states.get(k));
                    if (k != states.size() - 1) {
                        System.out.print(",");
                    };
                }

                System.out.print("}," + symbol + ")] = ");

                ArrayList<String> states2 = new ArrayList<>();//Ahora se mire a donde se llega con un simbolo y la lambda clausura del estado anterior
                states2 = ProcessStatesWithSymbol(states, symbol, afnl); // y los estados resultantes son states2
                Collections.sort(states2);

                System.out.print("$[{");

                for (int k = 0; k < states2.size(); k++) {
                    System.out.print(states2.get(k));
                    if (k != states2.size() - 1) {
                        System.out.print(",");
                    };
                }

                System.out.print("}] = ");

                // esta son las nuevas transiciones para el estado y el simbolo en el nuevo AFN
                ArrayList<String> states3 = new ArrayList<>();
                states3 = afnl.calcularMuchasLambdaClausuraSinImprimir(states2);
                Collections.sort(states3);
                System.out.print("{");

                for (int k = 0; k < states3.size(); k++) {
                    System.out.print(states3.get(k));
                    if (k != states3.size() - 1) {
                        System.out.print(",");
                    };
                }

                System.out.print("}.");
                System.out.println("");

                newDelta[i][j] = states3;
            }

        }
        
        for(int u = 0;u < newDelta.length;u++){
            
            for(int v = 0; v < newDelta[u].length;v++){
                
                if(newDelta[u][v].isEmpty()) newDelta[u][v].add(afnl.getStates().get(u));
                
            }
            
        }
        

        afn.initializeAFNwithData(newSigma, afnl.getStates(), afnl.getQ(), newFinalStates, newDelta);
        System.out.println("\n\n");
        System.out.println("Terminando transformacion de AFN_Lambda a AFN\n\n");
        
        
        return afn;
    }

    ;
    
    
    public static ArrayList<String> obtenerEstadosAccesibles(AFD afdinput) {

        ArrayList<String> accesibles = new ArrayList<>();

        accesibles.add(afdinput.getQ());

        for (int j = 0; j < afdinput.getDelta().length; j++) {
            String estadoActual = afdinput.getStates().get(j);
            for (int k = 0; k < afdinput.getDelta()[j].length; k++) {
                if (afdinput.getDelta()[j][k].isEmpty()) {
                    continue;
                } else {
                    for (String transicion : afdinput.getDelta()[j][k]) {
                        if (!estadoActual.equals(transicion)) {
                            if(accesibles.contains(estadoActual)){
                                if (!accesibles.contains(transicion)) {
                                    accesibles.add(transicion);
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(accesibles);
        return accesibles;
    }
    
    public static AFD modificarAutomata(AFD afdinput, ArrayList<String> accesibles, ArrayList<String> inaccesibles){
       
        for (int i = 0; i < afdinput.getStates().size(); i++) {
            if (!accesibles.contains(afdinput.getStates().get(i))) {
                inaccesibles.add(afdinput.getStates().get(i));
            }
        }
        
        for (int i = 0; i < afdinput.getStates().size(); i++) {
            if(inaccesibles.contains(afdinput.getStates().get(i))){
                for (int j = 0; j < afdinput.getSigma().size(); j++) {
                    afdinput.getDelta()[i][j] = null;
                }
            }
        }
       
        
        return afdinput;
    }
    
    public static String[][] simplificacionAutomataAFD(AFD afdinput, ArrayList<String> accesibles){
        //Esta función hace La matriz Triangular que es la primera iteración del algoritmo 
        String[][] triangular = new String[accesibles.size()][accesibles.size()];
        
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                triangular[i][j] = "E";
            }
        }
        
        for (int i = 0; i < accesibles.size(); i++) {
            for (int j = i+1; j < accesibles.size(); j++) {
                String p = accesibles.get(j);
                boolean pAceptacion = afdinput.getFinalStates().contains(p);
                String q = accesibles.get(i);
                boolean qAceptacion = afdinput.getFinalStates().contains(q);
                if(!(pAceptacion == qAceptacion)){
                    triangular[i][j] = "1";
                }
            }
        }
        
        simplificacionAutomataAFD2(afdinput, triangular, accesibles);
        return triangular;
    }
    
    public static void simplificacionAutomataAFD2(AFD afdinput, String[][]triangular, ArrayList<String> accesibles){
        //Esta función hace la tabla de la segunda iteración y actualiza la matriz triangular con la segunda iteración
        ArrayList<Integer> posiciones = triangularAEstados(afdinput, triangular, accesibles);
        ArrayList<Integer> copyPos = (ArrayList<Integer>) posiciones.clone();
        
        ArrayList<String>[][] tabular = new ArrayList[posiciones.size()/2][afdinput.getSigma().size()];
        
        int indice = 0;
        int k = 0;
        while(!copyPos.isEmpty()){
            int posEstadoP = copyPos.remove(0);
            int posEstadoQ = copyPos.remove(0);
            k = 0;
             for (int j = 0; j < afdinput.getSigma().size(); j++) {
                String vaP = afdinput.getDelta()[posEstadoP][j].get(0); //SE PUEDE MORIR o no :v
                String vaQ = afdinput.getDelta()[posEstadoQ][j].get(0);

                tabular[indice][k] = new ArrayList();
                tabular[indice][k].add(vaP);
                tabular[indice][k].add(vaQ);
                k++;
                if(k==2)
                    indice++;
            }
        }
                        
        for(int i = 0;i< tabular.length;i++){
            for(int j = 0;j< tabular[i].length;j++){
                if(!(tabular[i][j] == null)){
                    String p = tabular[i][j].get(0);
                    String q = tabular[i][j].get(1);
                    
                    boolean pAceptacion = afdinput.getFinalStates().contains(p);
                    boolean qAceptacion = afdinput.getFinalStates().contains(q);
                    
                    if(!(pAceptacion == qAceptacion)){
                        tabular[i][j].add("2");
                    }
                }                
            }
        }       
        
        copyPos = (ArrayList<Integer>) posiciones.clone();

            for (int i = 0; i < tabular.length; i++) {
                if(!copyPos.isEmpty()){
                    int posEstadoP = accesibles.indexOf(afdinput.getStates().get(copyPos.remove(0)));
                    int posEstadoQ = accesibles.indexOf(afdinput.getStates().get(copyPos.remove(0)));

                    for (int j = 0; j < tabular[i].length; j++) {
                        if (!(tabular[i][j] == null)) {
                            if (tabular[i][j].contains("2")) {
                                triangular[posEstadoQ][posEstadoP] = "2";
                            }
                        }
                    }
                }
            }
        
        
        
        /*for(int i = 0;i<tabular.length;i++){
            for(int j = 0;j<tabular[i].length;j++){
                if(!(tabular[i][j] == null)){
                    for (String string : tabular[i][j]) {
                    System.out.print(string + ",");
                }
                System.out.println("");
                }
            }
        }
        
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                System.out.println(triangular[i][j]);
            }
        }*/
        
        printTriangular(triangular, accesibles);
        printTabular(tabular,posiciones,accesibles,afdinput);
        
    }
    
    public static ArrayList<Integer> triangularAEstados(AFD afdinput, String[][]triangular, ArrayList<String> accesibles){
        ArrayList<Integer> posiciones = new ArrayList<>();
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                if(triangular[i][j].equals("E")){
                    int indexP = afdinput.getStates().indexOf(accesibles.get(j));
                    int indexQ = afdinput.getStates().indexOf(accesibles.get(i));
                    //System.out.println("index P = " + indexP + " index Q = " + indexQ);
                    posiciones.add(indexP); // posiciones en estados, mas no en accesibles
                    posiciones.add(indexQ);
                }
            }
        }
        
        return posiciones;
    }
    
    public static void printTriangular(String[][] triangular, ArrayList<String> accesibles) {

        System.out.println("");// espaciado bonito :V
        for (int i = 0; i < accesibles.size(); i++) {
            for (int j = 0; j <= i; j++) {

                if (i == j) {
                    triangular[i][j] = accesibles.get(i);
                }
                    System.out.print(triangular[j][i]+"  ");


            }
            System.out.println("\n");
        }


    }

    ;
    
    public static void printTabular(ArrayList<String>[][] tabular, ArrayList<Integer> posiciones, ArrayList<String> accesibles, AFD afd) {

        ArrayList<Integer> copyPos2 = (ArrayList<Integer>) posiciones.clone();
        System.out.println("\n");// espaciado bonito :V
        System.out.print("{p,q}    ");// 4 espacios entre todo horizontalmente;

        for (int i = 0; i < afd.getSigma().size(); i++) {

            System.out.print("{De(p," + afd.getSigma().get(i) + "),De(q," + afd.getSigma().get(i) + ")}    ");

        }
        System.out.println("\n");
        while (!copyPos2.isEmpty()) {
            for (int i = 0; i < tabular.length; i++) {

                ArrayList<String> ordenar = new ArrayList<>();
                ordenar.add(accesibles.get(accesibles.indexOf(afd.getStates().get(copyPos2.remove(0)))));
                ordenar.add(accesibles.get(accesibles.indexOf(afd.getStates().get(copyPos2.remove(0)))));;
                Collections.sort(ordenar);
                
                System.out.print("{" + ordenar.remove(0) + "," + ordenar.remove(0) + "}    ");

                for (int j = 0; j < tabular[i].length; j++) {

                    ordenar.add(tabular[i][j].get(0));
                    ordenar.add(tabular[i][j].get(1));
                    Collections.sort(ordenar);
                    
                    
                    System.out.print("{" + ordenar.remove(0) + "," + ordenar.remove(0) + "}");
                    if (tabular[i][j].contains("2")) {
                        System.out.print(" X2       ");
                    }else System.out.print("          ");

                }
                System.out.println("\n");
            }

        }
    }

    ;
    
    public static void main(String[] args) throws Exception {
        
        AFN_Lambda afnl = new AFN_Lambda();// eso no es un "uno" es una "l" :v
        afnl.initializeAFD("AFN_Lambda1.txt"); // Aqui se debe poner el nombre del archivo que se desea leer

        //AFN_LambdaToAFN(afnl).getSigma().get(0);
        
        AFN afn = new AFN();
        afn = AFN_LambdaToAFN(afnl);
        afn.showDelta();
        afn.showFinalStates();
        afn.showInitialState();
        afn.showSigma();
        afn.showStates();
        
        
        afn.procesarCadenaConversion("");
        afn.procesarCadenaConDetallesConversion("");


        //AFD afd1 = new AFD();
        //afd1.initializeAFD("AFD1.txt");
               

        //ArrayList<String> accesibles = obtenerEstadosAccesibles(afd1);
        
        //afd1 = modificarAutomata(afd1, accesibles, new ArrayList<>());
        
//        afd1.showStates();
//       afd1.showDelta();

        //simplificacionAutomataAFD(afd1, accesibles);
        
//        simplificacionAutomataAFD2(afd1, triangular, accesibles);

    }
    
    
}
