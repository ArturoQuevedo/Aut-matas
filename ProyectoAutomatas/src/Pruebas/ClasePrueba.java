/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Cartesiano.ProductCartesiano;
import Automatas.*;
import ProcesadoresDeCadenas.PCAFD;
import java.io.IOException;
import java.util.ArrayList;
import static Automatas.Automatas.AFNtoAFD;
import static Automatas.Automatas.AFN_LambdaToAFN;
import static Automatas.Automatas.AFN_LambdaToAFD;
import static Automatas.Automatas.AFNtoAFD2;
import static Automatas.Automatas.AFN_LambdaToAFN2;
import static Automatas.AFD.hallarComplemento;


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
    
    public static void probarAFNtoAFD2() throws IOException {

        AFN_Lambda afn = new AFN_Lambda();
        AFN afn2 = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD ();
        boolean afnr;
        String afnc = "bbbba";
        
        
        
        afn.initializeAFD("probarAFNtoAFD.txt");
        afn2 = AFN_LambdaToAFN2(afn);
        afd = AFN_LambdaToAFD(afn);


        

        afnr = afn.procesarCadena(afnc);
        
        System.out.println("Con la cadena : " + afnc);
        
        if (afnr == a.processString(afd, afnc)) {
            System.out.println("son iguales");

        } else {
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFN----------------------------");
        afn2.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFD----------------------------");
        afd.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }

    
    
    public static void probarAFN_LambdatoAFN() throws IOException 
    {

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        boolean afnlr;
        String afnlc = "bbb";
 
        
        afnl.initializeAFD("probarAFN_LambdatoAFN.txt");
        afn = AFN_LambdaToAFN(afnl);

        
        
        afnlr = afnl.procesarCadena(afnlc);
        
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print("Con la cadena : " + afnlc);
        if (afnlr == afn.procesarCadena(afnlc)) {
            System.out.println(" son iguales");

        } else {
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFL_Lambda----------------------------");
        afnl.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFN----------------------------");
        afn.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }
    
    public static void probarAFN_LambdatoAFN2() throws IOException 
    {

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD ();
        boolean afnlr;
        String afnlc = "bbbbbbbbbbbbaababa";
 
        
        afnl.initializeAFD("probarAFN_LambdatoAFN.txt");
        afn = AFN_LambdaToAFN2(afnl);
        afd = AFN_LambdaToAFD(afnl);

        
        
        afnlr = afnl.procesarCadena(afnlc);
        
        System.out.println("Con la cadena : " + afnlc);
        if (afnlr == a.processString(afd, afnlc)) {
            System.out.println(" son iguales");

        } else {
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFL_Lambda----------------------------");
        afnl.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFN----------------------------");
        afn.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }
    
    public static void probarAFN_LambdatoAFD() throws IOException {

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        AFD afd = new AFD();
        boolean afnlr;
        boolean afnr;
        boolean afdr;
        String afnlc = "aaaabbbbb";
 
        
        afnl.initializeAFD("probarAFN_LambdatoAFN.txt");
        afn = AFN_LambdaToAFN(afnl);

        
        System.out.println("Con la cadena : " + afnlc);
        afnlr = afnl.procesarCadena(afnlc);
        afnr = afn.procesarCadena(afnlc);
        afd = AFNtoAFD(afn);
        afdr = afnl.procesarCadenaConversion(afnlc);
        
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print("Con la cadena : " + afnlc);
        
        if (afnlr == afnr && afnr == afdr) {
            System.out.println(" son Iguales");

        } else {
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFL_Lambda----------------------------");
        afnl.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFN----------------------------");
        afn.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFD----------------------------");
        afd.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }
    
    
    public static void probarAFN_LambdatoAFD2() throws IOException {

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD ();
        boolean afnlr;
        boolean afnr;
        boolean afdr;
        String afnlc = "bbbbaaabbbbbbbbb";
 
        
        afnl.initializeAFD("probarAFN_LambdatoAFN.txt");
        afn = AFN_LambdaToAFN(afnl);

        
        System.out.println("Con la cadena : " + afnlc);
        afnlr = afnl.procesarCadena(afnlc);
        afd = AFNtoAFD2(afn);
        afdr = a.processString(afd, afnlc);
        afnr = afdr;
        
        System.out.println("Con la cadena : " + afnlc);
        
        if (afnlr == afnr && afnr == afdr) {
            System.out.println(" son Iguales");

        } else {
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFL_Lambda----------------------------");
        afnl.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFN----------------------------");
        afn.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFD----------------------------");
        afd.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }
    
    
    public static void probarComplemento() throws IOException{
        AFD afd1 = new AFD();
        AFD complemento;
        afd1.initializeAFD("AFD_probar_complemento.txt");
        
        complemento = hallarComplemento(afd1);
        
        
        System.out.println("------------ AFD INICIAL ------------");
        afd1.showAllTipeOfStates();
        
        System.out.println("------------ AFD INICIAL Finaliza ------------");
        
        System.out.println("------------ AFD COMPLEMENTO ------------");
        
        complemento.showAllTipeOfStates();
        
        System.out.println("------------ AFD COMPLEMENTO Finaliza ------------");
    }
    
    
    public static void probarSimplificacion() throws IOException{
        AFD afd1 = new AFD();
        afd1.initializeAFD("AFD_probar_simplificacion.txt");
        PCAFD procesadorAFD = new PCAFD();

        System.out.println("------------ TABLA TRIANGULAR Y TABLA DE TRANSICIONES ------------");
        
        afd1.simplificarAFD(afd1);
        
        System.out.println("------------ CADENAS ------------");
        
        procesadorAFD.processStringWithDetails(afd1, "aba");
        
        procesadorAFD.processStringWithDetails(afd1, "baba");
        
        procesadorAFD.processStringWithDetails(afd1, "babaab");
        
    }
    
    
    
    public static void probarProductoCartesiano(String fileRoute1, String fileRoute2, String type) throws IOException{
        AFD afd1 = new AFD();
        afd1.initializeAFD(fileRoute1);

        AFD afd2 = new AFD();
        afd2.initializeAFD(fileRoute2);
        
        ProductCartesiano pc = new ProductCartesiano();

        pc.hallarProductoCartesiano(afd1, afd2, type);
        
        
    }
    

    public static void main(String[] args) throws Exception {
        //probarAFD("AFD1.txt");
        //probarAFN("AFN.txt");        
        //probarAFNLambda("AFNLambda2.txt");
        //probarProductoCartesiano("AFD1.txt","AFD2.txt","diferencia simetrica");
        //probarAFNtoAFD2();
        //probarAFN_LambdatoAFN2();
        //probarAFN_LambdatoAFD2();
        //probarComplemento();
        probarSimplificacion();
        
        

    }

}

