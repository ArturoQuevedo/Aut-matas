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
import java.util.Scanner;

/**
 *
 * @author Equipo
 */
public class ClasePrueba {

    public static void probarAFNtoAFD() throws IOException {
        System.out.println("---------------------------------------------- Probar AFN a AFD ----------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFN en la función probarAFNtoAFD");

        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD();
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
        System.out.println("------------------------------------------- Probar AFN Lamda a AFN -------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFNL en la función probarAFN_LambdatoAFN");

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
        System.out.println("------------------------------------------- Probar AFN Lambda a AFD ------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFN en la función probarAFN_LambdatoAFD");

        AFN_Lambda afnl = new AFN_Lambda();
        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD a = new PCAFD();
        boolean afnlr;
        boolean afnr;
        boolean afdr;
        String afnlc = "abaab";

        afnl.initializeAFD("AFN_LambdatoAFD.txt");
        afn = AFN_LambdaToAFNSinImprimir(afnl);
        System.out.println("Con la cadena : " + afnlc);
        afnlr = afnl.procesarCadena2(afnlc);
        afnr = afn.procesarCadenaNoImprimir(afnlc);
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

    public static void probarComplemento() throws IOException {
        System.out.println("--------------------------------------------- Probar complemento ---------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFD en la función probarComplemento");

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

    public static void probarSimplificacion() throws IOException {
        System.out.println("------------------------------------------- Probar simplificacion --------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFD en la función probarComplemento");

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

    public static void probarProductoCartesiano() throws IOException {

        System.out.println("----------------------------------- Producto cartesiano entre dos automatas ----------------------------------");

        System.out.println("IMPORTANTE: Puede modificar los nombres de los archivos de los que se leen los dos AFD \n"
                + "a operar en la función probarProductoCartesiano");

        AFD afd1 = new AFD();
        afd1.initializeAFD("AFD1.txt");

        AFD afd2 = new AFD();
        afd2.initializeAFD("AFD2.txt");

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

    public static void validarAFNtoAFD() throws IOException {
        System.out.println("---------------------------------------------- Validar AFN a AFD ---------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFN en la función validarAFNtoAFD");

        AFN afn = new AFN();
        afn.initializeAFN("AFNtest.txt");
        ClaseValidacion cv = new ClaseValidacion();
        cv.validarAFNtoAFD(afn);
    }

    public static void validarAFNLambdatoAFN() throws IOException {
        System.out.println("------------------------------------------ Validar AFN Lambda a AFN ------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el AFNL en la función validarAFNLambdatoAFN");

        AFN_Lambda afnl = new AFN_Lambda();
        afnl.initializeAFD("AFN_Lambda1.txt");
        ClaseValidacion cv = new ClaseValidacion();
        cv.validarAFNLambatoAFN2(afnl);
    }

    public static void probarAutomata() throws IOException {

        System.out.println("------------------------------------------- Crear y probar autómata ------------------------------------------");
        System.out.println("IMPORTANTE: Puede modificar el nombre del archivo del que lee el autómata en la función probarAutomata");

        //Creacion del automata
        Automatas automata = new Automatas();
        automata.initializeAutomata("AFN_LambdatoAFN.txt");
        automata.createAutomata("AFN_LambdatoAFN.txt");

        //Archivos : AFD_entrega.txt - AFN_entrega.txt - AFN_Lambda_entrega.txt
        automata.elAlfabeto();
        Alfabeto alfabeto = new Alfabeto(automata.getSigma());
        automata.showAutomataData();

        //el numero es el tamaño de las cadenas que genera
        alfabeto.generarCadenaAleatoria(3);

        automata.allProcess("abab", alfabeto.getCadenas());
    }

    public static int funcionMenu() throws IOException {
        Scanner sc = new Scanner(System.in);
        int menu;
        System.out.println("---------------------------------------------------- MENU ----------------------------------------------------");

        System.out.println("1. Crear y probar autómata");
        System.out.println("2. Producto cartesiano");
        System.out.println("3. Validar AFN a AFD");
        System.out.println("4. Validar AFN Lambda a AFN");
        System.out.println("5. Probar AFN a AFD");
        System.out.println("6. Probar AFN Lamda a AFN");
        System.out.println("7. Probar AFN Lambda a AFD");
        System.out.println("8. Probar complemento");
        System.out.println("9. Probar simplificacion");
        System.out.println("10. Salir");
        
        menu = sc.nextInt();
        switch (menu) {
            case 1:
                probarAutomata();
                return 1;

            case 2:
                probarProductoCartesiano();
                return 1;
            case 3:
                validarAFNtoAFD();
                return 1;
            case 4:
                validarAFNLambdatoAFN();
                return 1;
            case 5:
                probarAFNtoAFD();
                return 1;
            case 6:
                probarAFN_LambdatoAFN();
                return 1;
            case 7:
                probarAFN_LambdatoAFD();
                return 1;
            case 8:
                probarComplemento();
                return 1;
            case 9:
                probarSimplificacion();
                return 1;
            case 10:
                return 0;
            default:
                return 0;
        }

    }

    public static void main(String[] args) throws Exception {

        
//        Automatas automata = new Automatas();
//        automata.initializeAutomata("AFN_Lambda_entrega.txt");
//        automata.createAutomata("AFN_Lambda_entrega.txt");
        
        probarAutomata();
//        int menu;
//        menu = funcionMenu();
//        while (menu == 1) {
//            menu = funcionMenu();
//        }

    }

}
