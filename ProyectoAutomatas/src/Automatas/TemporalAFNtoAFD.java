package Automatas;

import ProcesadoresDeCadenas.PCAFD;
import java.util.ArrayList;
import java.util.Collections;

public class TemporalAFNtoAFD {

//AFN a AFD ----------------------------------------------------------------
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
        return afd;
    }

    public static AFD hallarComplemento(AFD afdInput) {
        AFD complemento = new AFD();
        ArrayList<String> newfinalStates = new ArrayList<>();

        for (int i = 0; i < afdInput.getStates().size(); i++) {
            if (!afdInput.getFinalStates().contains(afdInput.getStates().get(i))) {
                newfinalStates.add(afdInput.getStates().get(i));
            }
        }

        complemento.initializeAFDwithData(afdInput.getSigma(), afdInput.getStates(), afdInput.getQ(), newfinalStates, afdInput.getDelta());
        return complemento;
    }

    public static void main(String[] args) throws Exception {

        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD procesadorAFD = new PCAFD();
        
        afn.initializeAFN("AFNtest.txt"); // Aqui se debe poner el nombre del archivo que se desea leer
        afd = AFNtoAFD(afn);
        AFD afdc = hallarComplemento(afd);
        
        //test del afn to afd
        afn.procesarCadenaConversion("aabaa");
        afn.procesarCadenaConDetallesConversion("aabaa");
        
        //test del complemento
        System.out.println("\n complemento:");
        procesadorAFD.processString(afdc, "aabaa");
        procesadorAFD.processStringWithDetails(afdc, "aabaa");
    }
}
