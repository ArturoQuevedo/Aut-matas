package Cartesiano;


import Automatas.AFD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hp
 */
public class ProductCartesiano {

    public void printProcedure(String transition, String currentState1, String currentState2, Character currentSimbol) {
        char g = (char) 399;

        System.out.print(g + "((" + currentState1 + "," + currentState2 + ")," + currentSimbol + ") = (");
        System.out.print(g + "1(" + currentState1 + "," + currentSimbol + ")," + g + "2(" + currentState2 + "," + currentSimbol + ")) = ");
        System.out.print(transition);
        System.out.println("");
    }

    public void saveAutomata(ArrayList<Character> sigma, String initialState, ArrayList<String> states, ArrayList<String> finalStates, String[][] delta) throws IOException {
        String fileName = "productoCart.txt";
        File archivo = new File(fileName);
        BufferedWriter bw;

        int i = 1;
        while (true) {
            if (archivo.exists()) {

                fileName = "productoCart" + i + ".txt";
                archivo = new File(fileName);
                i++;
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                break;
            }
        }

        bw.write("#alphabet\n");
        for (int j = 0; j < sigma.size(); j++) {
            bw.write(sigma.get(j) + "\n");
        }
        bw.write("#states\n");
        for (int j = 0; j < states.size(); j++) {
            bw.write(states.get(j) + "\n");
        }
        bw.write("#initial\n");
        bw.write(initialState + "\n");

        bw.write("#accepting\n");
        for (int j = 0; j < finalStates.size(); j++) {
            bw.write(finalStates.get(j) + "\n");
        }

        bw.write("#transitions\n");
        for (int j = 0; j < states.size(); j++) {
            for (int k = 0; k < sigma.size(); k++) {

                bw.write(states.get(j) + ":" + sigma.get(k) + ">" + delta[j][k] + "\n");
            }
        }
        bw.close();
        System.out.println("El autómata resultante se ha guardado en " + fileName);
    }

    public void hallarProductoCartesiano(AFD afd1, AFD afd2, String type) throws IOException {

        //Los alfabetos de ambos autómatas deben ser iguales
        if (afd1.getSigma().equals(afd2.getSigma())) {

            String initialState = "(" + afd1.getQ() + "," + afd2.getQ() + ")";
            ArrayList<String> states = new ArrayList<>();
            ArrayList<String> finalStates = new ArrayList<>();
            String[][] delta = new String[afd1.getStates().size() * afd2.getStates().size()][afd1.getSigma().size()];

            ArrayList<String>[][] delta1 = afd1.getDelta();
            ArrayList<String>[][] delta2 = afd2.getDelta();

            String x, y, transition;
            int row = 0;
            for (int i = 0; i < afd1.getStates().size(); i++) {
                String currentState1 = afd1.getStates().get(i);
                for (int j = 0; j < afd2.getStates().size(); j++) {
                    String currentState2 = afd2.getStates().get(j);
                    states.add("(" + currentState1 + "," + currentState2 + ")");
                    for (int k = 0; k < afd1.getSigma().size(); k++) {
                        Character currentSimbol = afd1.getSigma().get(k);

                        x = delta1[i][k].get(0);
                        y = delta2[j][k].get(0);

                        transition = "(" + delta1[i][k].get(0) + "," + delta2[j][k].get(0) + ")";

                        delta[row][k] = transition;
                        //System.out.println("Se guardo "+transition+"en la posicion "+row+","+k);

                        switch (type) {
                            case "union":
                                if (finalStates.isEmpty() || (!finalStates.contains(transition))) {
                                    if (afd1.getFinalStates().contains(x) || afd2.getFinalStates().contains(y)) {
                                        

                                        finalStates.add(transition);
                                    }
                                }
                                break;

                            case "interseccion":

                                if (!finalStates.contains(transition)) {
                                    if (afd1.getFinalStates().contains(x) && afd2.getFinalStates().contains(y)) {
                                        
                                        finalStates.add(transition);
                                    }
                                }
                                break;
                            case "diferencia":
                                if (!finalStates.contains(transition)) {
                                    if (afd1.getFinalStates().contains(x) && !afd2.getFinalStates().contains(y)) {
                                        finalStates.add(transition);
                                    }
                                }
                                break;
                            case "diferencia simetrica":
                                if (!finalStates.contains(transition)) {
                                    if ((afd1.getFinalStates().contains(x) && !afd2.getFinalStates().contains(y))
                                            || (!afd1.getFinalStates().contains(x) && afd2.getFinalStates().contains(y))) {
                                        finalStates.add(transition);
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                        printProcedure(transition, currentState1, currentState2, currentSimbol);
                    }
                    row++;
                }
            }

            saveAutomata(afd1.getSigma(), initialState, states, finalStates, delta);

        }
    }

    public static void main(String[] args) throws Exception {

        AFD afd1 = new AFD();
        afd1.initializeAFD("AFD1.txt");

        AFD afd2 = new AFD();
        afd2.initializeAFD("AFD2.txt");
        
        ProductCartesiano pc = new ProductCartesiano();

        pc.hallarProductoCartesiano(afd1, afd2, "diferencia simetrica");

//        afd.showSigma();
//        afd.showStates();
//        afd.showInitialState();
//        afd.showFinalStates();
//        afd.showDelta();        
    }

}
