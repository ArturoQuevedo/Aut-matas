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
import static Automatas.Automatas.AFNtoAFDSinImprimir;
import static Automatas.Automatas.AFN_LambdaToAFNSinImprimir;
import static Automatas.AFD.hallarComplemento;
import Automatas.*;
import ProcesadoresDeCadenas.Alfabeto;


/**
 *
 * @author Equipo
 */
public class ClasePrueba {

    




    
    public static void probarAFNtoAFD() throws IOException {

        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD ();
        boolean afnr;
        String afnc = "bbbba";
        afn.initializeAFN("AFNtoAFD.txt");
        afd = AFNtoAFDSinImprimir(afn);
        afnr = a.processString(afd, afnc);
        
        
        if (afnr == afn.procesarCadenaConversion(afnc)) {
            System.out.println("Con la cadena : " + afnc);
            System.out.println("son iguales");

        } else {
            System.out.println("Con la cadena : " + afnc);
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFN----------------------------");
        afn.showAllTipeOfStates();
        System.out.println("-------------------------------");
        System.out.println("AFD----------------------------");
        afd.showAllTipeOfStates();
        System.out.println("-------------------------------");
        
        

    }

    
    
    public static void probarAFN_LambdatoAFN() throws IOException {

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        boolean afnlr;
        String afnlc = "aaababbb";
 
        
        afnl.initializeAFD("AFN_LambdatoAFN.txt");
        afn = AFN_LambdaToAFNSinImprimir(afnl);

        
        
        afnlr = afnl.procesarCadena2(afnlc);
        
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        
        if (afnlr == afnl.procesarCadenaConversion(afnlc)) {
            System.out.print("Con la cadena : " + afnlc);
            System.out.println(" son iguales");

        } else {
            System.out.print("Con la cadena : " + afnlc);
            System.out.println(" no son iguales");

        };
       
        
        System.out.println("AFN_Lambda----------------------------");
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
        PCAFD a = new PCAFD ();
        boolean afnlr;
        boolean afnr;
        boolean afdr;
        String afnlc = "abaab";
 
        
        afnl.initializeAFD("AFN_LambdatoAFD.txt");
        afn = AFN_LambdaToAFNSinImprimir(afnl);
        System.out.println("Con la cadena : " + afnlc);
        afnlr = afnl.procesarCadena2(afnlc);
        afnr = afn.procesarCadenaSinImprimirNiMadres(afnlc);
        afd = AFNtoAFDSinImprimir(afn);
        afdr = a.processString(afd, afnlc);
        
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        
        
        if (afnlr == afnl.procesarCadenaConversion(afnlc) && afnlr == afdr) {
            System.out.print("Con la cadena : " + afnlc);
            System.out.println(" son Iguales");

        } else {
            System.out.print("Con la cadena : " + afnlc);
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
       
        
        //Creacion del automata
        Automatas automata = new Automatas();
        automata.initializeAutomata("AFD_entrega.txt");
        automata.createAutomata("AFD_entrega.txt");
        //Archivos : AFD_entrega.txt - AFN_entrega.txt - AFN_Lambda_entrega.txt
        automata.elAlfabeto();
        Alfabeto alfabeto = new Alfabeto(automata.getSigma());
        automata.showAutomataData();
        
        
        //el numero es el tamaño de las cadenas que genera
        alfabeto.generarCadenaAleatoria(3);
        
        
        
        /*                      Procesamiento de cadenas                        */
        //la "abab" puede ser remplazada
        automata.allProcess("abab",alfabeto.getCadenas()); 
        
        //Producto cartesiano entre dos automatas
        System.out.println("--------- Producto cartesiano entre dos automatas ---------");
        probarProductoCartesiano("AFD1.txt","AFD2.txt");
        
        //Validar AFN en AFD
        System.out.println("--------- Validar AFN en AFD ---------");
        validarAFNtoAFD("AFNtest.txt");
        
        //Validar AFN_Lambda to AFN
        System.out.println("--------- Validar AFN_Lambda to AFN ---------");
        validarAFNLambdatoAFN("AFN_Lambda1.txt");
        
        //Probar producto cartesiano
        System.out.println("--------- Probar producto cartesiano ---------");
        probarProductoCartesiano("AFD1.txt","AFD2.txt");
        
        
        //Probar AFN to AFD
        System.out.println("--------- Probar AFN to AFD ---------");
        probarAFNtoAFD();
        
        
        //Probar AFN_Lambda to AFN
        System.out.println("--------- Probar AFN_Lambda to AFN ---------");
        probarAFN_LambdatoAFN();
        
        
        //Probar AFN_Lambda to AFD
        System.out.println("--------- Probar AFN_Lambda to AFD ---------");
        probarAFN_LambdatoAFD();
        
        
        //Probar complemento de AFD
        System.out.println("--------- Probar complemento de AFD ---------");
        probarComplemento();
        
        
        //Probar simplificacion de un AFD
        System.out.println("--------- Probar simplificacion de un AFD ---------");
        probarSimplificacion();
    }

}

