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
