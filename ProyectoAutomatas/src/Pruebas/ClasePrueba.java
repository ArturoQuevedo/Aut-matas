/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Automatas.AFD;
import Automatas.AFN;
import Automatas.AFN_Lambda;
import ProcesadoresDeCadenas.PCAFD;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Equipo
 */
public class ClasePrueba {
    
    //AFN a AFD ----------------------------------------------------------------
    
    public int getRow(String state, ArrayList<String> states) {
        //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
        for (int i = 0; i < states.size(); i++) {
            //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
            if (state.equals(states.get(i))) {
                return i;
            }

        }
        return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
    }
    
    public String concatStates(ArrayList<String> delta){
        String newState = "{";
        for (int i = 0; i < delta.size(); i++) {
            newState = newState.concat(delta.get(i) + ",");
        }
        newState = newState.concat("}");
        return newState;
    }
    
    public boolean containsF (ArrayList<String> delta, ArrayList<String> finalStates){
        boolean bool = false;
        for (int i = 0; i < delta.size(); i++) {
            if(finalStates.contains(delta.get(i))){
                bool = true;
                break;
            }
        }
        return bool;
    }
    
    public AFD AFNtoAFD(AFN afn){
        //se almacena localmente el AFN para ser editado
        ArrayList<String>[][] delta = afn.getDelta();
        ArrayList<Character> sigma = afn.getSigma();
        ArrayList<String> states = afn.getStates();
        ArrayList<String> finalStates = afn.getFinalStates();
        String q = afn.getQ();
        String newState;
        //tamaño original de la lista de estados
        int StatesSize = states.size();
        //busca si hay uno o mas estados en el delta y añade la concatenacion de estos a la lista de estados
        for (int i = 0; i < StatesSize; i++) {
            for (int j = 0; j < sigma.size(); j++) {
                if(delta[i][j].size()>1){
                    newState = concatStates(delta[i][j]);
                    states.add(newState);
                    if(containsF(delta[i][j],finalStates)){
                        finalStates.add(newState);
                    }
                }
            }
        }
        
        //--- EN PROCESO ---
        //función que busque los deltas de los nuevos estados añadidos
        //busca ahora si los estados creados anteriormente producen nuevos estados
        int statesToCheck = states.size()-StatesSize;
        

        //creacion de la matriz delta usada para añadir solo los estados alcanzables desde q0
        ArrayList<String>[][] testDelta = new ArrayList[states.size()][sigma.size()];
        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < sigma.size(); j++) {
                testDelta[i][j] = new ArrayList<String>();
            }
        }
        //verificar si son alcanzables
        
        
        //----------------------------------------------------------------
        
        AFD afd = new AFD();
        return afd;
    }

    public static void probarAFD(String fileRoute) throws IOException {
        AFD afd = new AFD();
        afd.initializeAFD(fileRoute); //Recibe el nombre del archivo donde está el AFD

        PCAFD procesadorAFD = new PCAFD();

        //Procesar las siguientes cadenas:
        System.out.println("\nPROCESAR CADENA CON DETALLES:");
        procesadorAFD.processStringWithDetails(afd, "abababab");//Con detalles

        System.out.println("\nPROCESAR CADENA SIN DETALLES:");
        procesadorAFD.processString(afd, "ababababa");//Sin detalles

        //Crear una lista de cadenas
        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aba");
        prueba.add("baba");
        prueba.add("abba");
        prueba.add("");
        prueba.add("b");

        //Procesar la cadena
        System.out.println("\nPROCESAR LISTA DE CADENAS:");
        procesadorAFD.processStringList(afd, prueba, "procesarCadenasAFD", true);
    }

    public static void probarAFN(String fileRoute) throws IOException {
        AFN afn = new AFN();
        afn.initializeAFN("AFN.txt");
        
        System.out.println("\nCOMPUTAR TODOS LOS PROCESAMIENTOS:");
        afn.computarTodosLosProcesamientos("aab", "procesarCadenasAFN");
        
        System.out.println("\nPROCESAR CADENA SIN DETALLES :");
        afn.procesarCadena("aab");
        
        System.out.println("\nPROCESAR CADENA CON DETALLES :");
        afn.procesarCadenaConDetalles("abb");
        
        //Agregar cadenas a una lista
        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("baba");        
        prueba.add("babab");
        System.out.println("\nPROCESAR LISTA DE CADENAS:");
        afn.processStringList(prueba, "procesarListaCadenasAFN", true);  

    }

    public static void probarAFNLambda(String fileRoute) throws IOException {
        
        AFN_Lambda afd = new AFN_Lambda();
        afd.initializeAFD("AFNLambda1.txt"); // Aqui se debe poner el nombre del archivo que se desea leer

        //Los metodos que se DEBEN USAR para obtener resultados son los siguientes :
        
        System.out.println("\nLAMBDA CLAUSURA");
        afd.printLambdaClausura("s1");
        
        System.out.println("\nLAMBDA CLAUSURA DE VARIOS ESTADOS");
        ArrayList<String> pruebasEstados = new ArrayList<String>();
        pruebasEstados.add("s1");
        pruebasEstados.add("s2");
        pruebasEstados.add("s3");
        pruebasEstados.add("s4");
        afd.calcularMuchasLambdaClausura(pruebasEstados);
        
        System.out.println("\nPROCESAR CADENA SIN DETALLES:");
        afd.procesarCadena("aba");

        System.out.println("\nPROCESAR CADENA CON DETALLES:");
        afd.procesarCadenaConDetalles2("aba");
        
        System.out.println("\nCOMPUTAR TODOS LOS PROCESAMIENTOS:");
        afd.computarTodosLosProcesamientos("aba", "procesarCadenasAFNLambda");
        
        ArrayList<String> pruebasCadenas = new ArrayList<String>();
        pruebasCadenas.add("aaaaaa");
        pruebasCadenas.add("aba");
        pruebasCadenas.add("aabb");
        pruebasCadenas.add("bbbbbbbb");
        pruebasCadenas.add("");
        System.out.println("\nCOMPUTAR LISTA DE CADENAS:");
        afd.procesarListaCadenas(pruebasCadenas, "procesarLstaCadenasAFNLambda", true);
        /*Cualquier otro metodo con nombre similar no dara el resultado esperado.
        NOTAS : 
        -Todas las "listas" (denotadas como "cadenas") recibidas deben ser ArrayList
        -Todos los parametros "nombreArchivo" van sin la extensión .txt y en caso de ya existir se les dara un nombre generico que :
            en caso de ser en el metodo "procesarListaCadenas" sera : nombreArchivoi.txt en donde "nombreArchivo" es el nombre que se ingreso y  la "i" representa un numero entero disponible.
            en caso de ser en el metodo "computarTodosLosProcesamientos" seran : nombreArchivoiAceptadas.txt,nombreArchivoiRechazadas.txt,nombreArchivoiAbortadas.txt en donde "nombreArchivo" es el nombre que se ingreso y  la "i" representa un numero entero disponible.
       
         */
        
        
    }

    public static void main(String[] args) throws Exception {
        //probarAFD("AFD1.txt");
        //probarAFN("AFN.txt");
        probarAFNLambda("AFNLambda2.txt");

    }

}
