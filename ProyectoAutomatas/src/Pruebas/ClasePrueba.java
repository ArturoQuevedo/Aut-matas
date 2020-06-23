/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Cartesiano.ProductCartesiano;
import Automatas.AFD;
import Automatas.AFN;
import Automatas.AFN_Lambda;
import ProcesadoresDeCadenas.PCAFD;
import java.io.IOException;
import java.util.ArrayList;
import Automatas.*;
import ProcesadoresDeCadenas.Alfabeto;

/**
 *
 * @author Equipo
 */
public class ClasePrueba {

    




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
    
    public static void probarProductoCartesiano(String fileRoute1, String fileRoute2) throws IOException{
        AFD afd1 = new AFD();
        afd1.initializeAFD(fileRoute1);

        AFD afd2 = new AFD();
        afd2.initializeAFD(fileRoute2);
        
        ProductCartesiano pc = new ProductCartesiano();
        
        
        pc.hallarProductoCartesiano(afd1, afd2, "union");
        ArrayList<String> union = pc.getAcceptingStates();    
        
        pc.hallarProductoCartesianoSinImprimir(afd1, afd2, "interseccion");
        ArrayList<String> interseccion = pc.getAcceptingStates();
        
        pc.hallarProductoCartesianoSinImprimir(afd1, afd2, "diferencia");
        ArrayList<String> diferencia = pc.getAcceptingStates();
        
        pc.hallarProductoCartesianoSinImprimir(afd1, afd2, "diferencia simetrica");
        ArrayList<String> diferenciaSimetrica = pc.getAcceptingStates();
        
        System.out.println("Estados de aceptación del autómata 1");
        afd1.showFinalStates();
        System.out.println("Estados de aceptación del autómata 2");
        afd2.showFinalStates();
        System.out.println("Estados de aceptación de la Union");
        for (int i = 0; i < union.size(); i++) {
            System.out.println(union.get(i));            
            
        }
        System.out.println("Estados de aceptación de la Interseccion");
        for (int i = 0; i < interseccion.size(); i++) {
            System.out.println(interseccion.get(i));            
            
        }
        System.out.println("Estados de aceptación de la Diferencia");
        for (int i = 0; i < diferencia.size(); i++) {
            System.out.println(diferencia.get(i));            
            
        }
        System.out.println("Estados de aceptación de la Diferencia simetrica");
        for (int i = 0; i < diferenciaSimetrica.size(); i++) {
            System.out.println(diferenciaSimetrica.get(i));            
            
        }
        
    }
    
    public static void validarAFNtoAFD(String fileRoute) throws IOException{
        System.out.println("Validar AFN a AFD");
        AFN afn = new AFN();
        afn.initializeAFN(fileRoute);
        ClaseValidacion cv= new ClaseValidacion();
        cv.validarAFNtoAFD2(afn);        
    }
    
    public static void validarAFNLambdatoAFN(String fileRoute) throws IOException{
        System.out.println("\nValidar AFNLambda a AFN");
        AFN_Lambda afnl = new AFN_Lambda();
        afnl.initializeAFD(fileRoute);
        ClaseValidacion cv= new ClaseValidacion();
        cv.validarAFNLambatoAFN2(afnl);
    }


    public static void main(String[] args) throws Exception {
       
        Automatas automata = new Automatas();
        
        automata.initializeAutomata("file.txt");
        automata.createAutomata();
        automata.elAlfabeto();
        Alfabeto alfabeto = new Alfabeto(automata.getSigma());
        //el numero es el tamaño de las cadenas que genera
        alfabeto.generarCadenaAleatoria(4);
        //la "aa" puede ser remplazada
        automata.allProcess("aa",alfabeto.getCadenas());
        //probarAFD("AFD1.txt");
        //probarAFN("AFN.txt");        
        //probarAFNLambda("AFNLambda2.txt");
        probarProductoCartesiano("AFD1.txt","AFD2.txt");
        //validarAFNtoAFD("AFNtest.txt");
        //validarAFNLambdatoAFN("AFN_Lambda1.txt");
        
        
        
        

    }

}

