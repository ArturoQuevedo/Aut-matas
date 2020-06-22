/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import Automatas.*;
import ProcesadoresDeCadenas.PCAFD;
import java.util.Collections;

/**
 *
 * @author andre
 */
public class Automatas {
    private AFD afd = new AFD();
    private AFN afn = new AFN();
    private AFN_Lambda afn_lambda = new AFN_Lambda();
    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;
    public String TipoAuntomata;
    private PCAFD pcafd = new PCAFD();

    public Automatas() {
    }



    public void initializeAutomata(String fileRoute) throws FileNotFoundException, IOException {

            File file = new File(fileRoute);

            if (!file.exists()) {
                System.out.println("NO SE ENCONTRO EL ARCHIVO");
                System.exit(1);
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringTokenizer tokenizer;

            String line;
            while ((line = br.readLine()) != null) {

                switch (line) {
                    case ("#automata"):
                         while (!(line = br.readLine()).startsWith("#")) {
                         this.TipoAuntomata = line;
                         }
                   

                }
            }
            br.close();

        }

    public void createAutomata() throws IOException{
        if(this.TipoAuntomata.equals("AFD")){
            afd.initializeAFD("file.txt");
        }else if(this.TipoAuntomata.equals("AFN")){
            afn.initializeAFN("file.txt");
        }else if(this.TipoAuntomata.equals("AFNE")){
            afn_lambda.initializeAFD("file.txt");
        }else{
            System.out.println("El automata seleccionado no existe");
        }
    }
    
      

    
    public void showAutomataData(){
        if(this.TipoAuntomata.equals("AFD")){
            afd.showAllTipeOfStates();
        }else if(this.TipoAuntomata.equals("AFN")){
            afn.showAllTipeOfStates();
        }else if(this.TipoAuntomata.equals("AFNE")){
            afn_lambda.showAllTipeOfStates();
        }else{
            System.out.println("El automata seleccionado no existe");
        }
    }
    
    public void ProcessStringAutomata(ArrayList<String> prueba) throws IOException{
        if(this.TipoAuntomata.equals("AFD")){
            ProcessStringAFD(afd,prueba)
                    
                    
                    
                    
                    
                    
                    
                    
                    
                    ;
        }else if(this.TipoAuntomata.equals("AFN")){
            ProcessStringAFN(prueba);
        }else if(this.TipoAuntomata.equals("AFNE")){
            ProcessStringAFN_Lambda(prueba);
        }else{
            System.out.println("El automata seleccionado no existe");
        }
    }
    
    public void ProcessStringAFD(AFD afd,ArrayList<String> prueba) throws IOException{
        pcafd.processStringList(afd,prueba,"procesarCadenasAFD", true);
    }
   
    public void ProcessStringAFN(ArrayList<String> prueba) throws IOException{
        afn.processStringList(prueba,"procesarListaCadenasAFN", true);
    }
    
    public void ProcessStringAFN_Lambda(ArrayList<String> prueba) throws IOException{
        afn_lambda.procesarListaCadenas(prueba,"procesarLstaCadenasAFNLambda", true);
    }
    
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    //---------------------------------------------DE AFN_Lmabda a AFN-----------------------------
    
    
    public static ArrayList<String> ProcessStatesWithSymbol(ArrayList<String> states, String symbol, AFN_Lambda afnl) {

        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < states.size(); i++) {

            int row = afnl.getPosEstado(states.get(i));
            int column = afnl.getPosSimbolo(symbol);
            ArrayList<String> temp = new ArrayList<>();
            temp = afnl.getDelta()[row][column]; // Aqui guardo la info que me dio delta para un usarla mas facil

            for (int j = 0; j < temp.size(); j++) {

                if (!result.isEmpty() && !temp.isEmpty()) { // confirmo esto para evitar que el programa se muera en ciertos casos
                    if (!result.contains(temp.get(j))) { // confirmo que no se haya agregado antes

                        result.add(temp.get(j)); // agrego los estados

                    }
                } else if (result.isEmpty() && !temp.isEmpty()) {

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

        for (int u = 0; u < newDelta.length; u++) {

            for (int v = 0; v < newDelta[u].length; v++) {

                if (newDelta[u][v].isEmpty()) {
                    newDelta[u][v].add(afnl.getStates().get(u));
                }

            }

        }

        afn.initializeAFNwithData(newSigma, afnl.getStates(), afnl.getQ(), newFinalStates, newDelta);
        System.out.println("\n\n");
        System.out.println("Terminando transformacion de AFN_Lambda a AFN\n\n");

        return afn;
    }

    ;
    
    //--------------------------------------------------------------------------------------------------------
    
    
    //AFN a AFD ----------------------------------------------------------------------------------------------
    
    
    public static int getRow(String state, ArrayList<String> states) {
        //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
        for (int i = 0; i < states.size(); i++) {
            //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
            if (state.equals(states.get(i))) {
                return i;
            }
        }
        return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
    }

    public static String concatStates(ArrayList<String> delta) {
        ArrayList<String> mydelta = delta;
        Collections.sort(mydelta);

        //se concatenan los estados
        String newState = "";
        if (mydelta.size() > 1) {
            for (int i = 0; i < mydelta.size() - 1; i++) {
                newState = newState.concat(mydelta.get(i) + ";");
            }
            newState = newState.concat(mydelta.get(mydelta.size() - 1));
        } else {
            newState = mydelta.get(0);
        }
        return newState;
    }

    public static boolean containsFinal(ArrayList<String> delta, ArrayList<String> finalStates) {
        boolean bool = false;
        for (int i = 0; i < delta.size(); i++) {
            if (finalStates.contains(delta.get(i))) {
                bool = true;
                break;
            }
        }
        return bool;
    }

    public static AFD AFNtoAFD(AFN afn) {

        System.out.println("Iniciando transformacion de AFN a AFD\n\n");
        
        
        //se almacena localmente el AFN para ser editado
        ArrayList<String>[][] delta = afn.getDelta();
        ArrayList<Character> sigma = afn.getSigma();
        ArrayList<String> AFNstates = afn.getStates();
        ArrayList<String> finalStates = afn.getFinalStates();
        String q = afn.getQ();
        //tamaño original de la lista de estados
        int statesSize = AFNstates.size();

        //nuevo estado para añadir
        String newState;

        //datos del AFD producido
        ArrayList<String> AFDStates = new ArrayList<>();
        ArrayList<String> AFDfinalStates = new ArrayList<>();
        AFDStates.add(q);
        //se crea una matriz de ArrayList para el AFD
        ArrayList<String>[][] AFDdelta = new ArrayList[statesSize * 3][sigma.size()];
        for (int i = 0; i < statesSize * 3; i++) {
            for (int j = 0; j < sigma.size(); j++) {
                AFDdelta[i][j] = new ArrayList<String>();
            }
        }

        //si q0 esta en los estados finales, se añade a los de AFD
        if (finalStates.contains(q)) {
            AFDfinalStates.add(q);
        }
        
        //Se imprime la matriz delta original
        System.out.print("  D  !!  ");
        for (int j = 0; j < sigma.size(); j++) {
            System.out.print(sigma.get(j) + "  |");
        }
        System.out.println("|");
        
        for (int i = 0; i < statesSize; i++) {
            System.out.print(AFNstates.get(i) + " !! ");
            for (int j = 0; j < sigma.size(); j++) {
                System.out.print(" ");
                for (int k = 0; k < delta[i][j].size(); k++) {
                    System.out.print(delta[i][j].get(k) + ";");
                }
                System.out.print(" |");
            }
            System.out.println("|");
        }

        System.out.println("----------------------------------");
        System.out.println("----------------------------------");
        
        //recorre cada uno de los estados alcanzables por el AFD final
        for (int i = 0; i < AFDStates.size(); i++) {

            //index para usar en la matriz delta del AFN
            int stateindex = getRow(AFDStates.get(i), AFNstates);

            //evalua si el estado pertenece a los estados del AFN original
            if (stateindex >= 0) {

                //itera a través del sigma
                for (int j = 0; j < sigma.size(); j++) {

                    //si mas de un estados en el delta
                    if (delta[stateindex][j].size() > 1) {

                        //concatena los estados a los que pasan
                        newState = concatStates(delta[stateindex][j]);

                        //añade el edge al estado con el que se calculo el index
                        AFDdelta[i][j].add(newState);

                        //si este nuevo estado no esta registrado ya, lo añade
                        if (!AFDStates.contains(newState)) {

                            AFDStates.add(newState);

                            //si tiene un estado final, lo añade a los estados finales
                            if (containsFinal(delta[stateindex][j], finalStates)) {
                                AFDfinalStates.add(newState);
                            }
                        }

                    } //si el estado pertenece a los estados del AFN original y no esta en los del AFD, se añade
                    else if (delta[stateindex][j].size() == 1 && !AFDStates.contains(delta[stateindex][j].get(0))) {
                        AFDStates.add(delta[stateindex][j].get(0));
                        AFDdelta[i][j].add(delta[stateindex][j].get(0));
                        if (finalStates.contains(delta[stateindex][j].get(0))) {
                            AFDfinalStates.add(delta[stateindex][j].get(0));
                        }
                    } else {
                        AFDdelta[i][j].add(delta[stateindex][j].get(0));
                    }

                }

            } //Si el estado originario es uno de los estados concatenados
            else {
                
                System.out.print(AFDStates.get(i) + " !! ");

                //encontrar a los estados que puede saltar
                ArrayList<String> newStates = new ArrayList<>();

                //se divide el estado en sus componentes
                String[] split = AFDStates.get(i).split(";");

                //se ubica en una letra de sigma
                for (int j = 0; j < sigma.size(); j++) {

                    //se mueve por los diferentes Arraylist que contienen los estados que componen el estado
                    for (int k = 0; k < split.length; k++) {

                        //index para usar en la matriz delta del AFN
                        int index = getRow(split[k], AFNstates);

                        //itera sobre los diferentes estados del arraylist
                        for (int l = 0; l < delta[index][j].size(); l++) {

                            newState = delta[index][j].get(l);

                            //si no esta contenido, lo añade
                            if (!newStates.contains(newState)) {
                                newStates.add(newState);
                            }
                        }

                    }

                    //concatena los estados a los que pasan
                    newState = concatStates(newStates);

                    //añade el edge al estado con el que se calculo el index
                    AFDdelta[i][j].add(newState);
                    System.out.print(" " + newState + " |");

                    //si este nuevo estado no esta registrado ya, lo añade
                    if (!AFDStates.contains(newState)) {

                        AFDStates.add(newState);

                        //si tiene un estado final, lo añade a los estados finales
                        if (containsFinal(newStates, finalStates)) {
                            AFDfinalStates.add(newState);
                        }
                    }

                    //se limpia el arraylist para volver a usarse
                    newStates.clear();
                }
                System.out.println("|");
            }

        }
        
        System.out.println("\n");
        //se crea y se retorna el AFD producido
        AFD afd = new AFD();
        afd.initializeAFDwithData(sigma, AFDStates, q, AFDfinalStates, AFDdelta);
        
        System.out.println("Terminando transformacion de AFN a AFD\n\n");
        
        return afd;
    }
    

    //---------------------------------------------------------------------------------------------------------
    //AFN_Lambda a AFD-----------------------------------------------------------------------------------------
    
    
    public static AFD AFN_LambdaToAFD(AFN_Lambda afnl){
        
        AFN afn = new AFN();
        afn = AFN_LambdaToAFN(afnl);
        return AFNtoAFD(afn);
        
        
    } 
    
    
    
    //----------------------------------------------------------------------------------------------------------
    
    
    
    
    public static void main(String[] args) throws IOException{
       /* Automatas automata = new Automatas();
        automata.initializeAutomata("file.txt");
        automata.createAutomata();
        automata.showAutomataData();*/
        
        /*//Pruebas AFN_Lambda to AFN
        AFN_Lambda afnl = new AFN_Lambda();
        afnl.initializeAFD("AFN_Lambda1.txt");
        AFN_LambdaToAFN(afnl);*/

        /*//Pruebas AFN to AFD
        AFN afn = new AFN();
        afn.initializeAFN("AFNtest3.txt"); // Aqui se debe poner el nombre del archivo que se desea leer
        AFNtoAFD(afn);*/
        
        //Pruebas AFN_Lambda to AFD
        AFN_Lambda afnl = new AFN_Lambda();
        afnl.initializeAFD("AFN_Lambda1.txt");
        AFN_LambdaToAFD(afnl);
        
        
        
        
        //test del afn to afd
        //afn.procesarCadenaConversion("aabaa");
        //afn.procesarCadenaConDetallesConversion("abaaaaab");
        
        
        
        //datos prueba
        /*ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aba");
        prueba.add("baba");
        prueba.add("abba");
        prueba.add("");
        prueba.add("b");
        automata.afd.findLimboStates();
*/
        //automata.ProcessStringAutomata(prueba);
    }
    
}
