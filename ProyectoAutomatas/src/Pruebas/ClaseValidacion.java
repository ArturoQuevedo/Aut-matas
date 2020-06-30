/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruebas;

import Automatas.AFD;
import Automatas.AFN;
import Automatas.AFN_Lambda;
import Automatas.Automatas;
import ProcesadoresDeCadenas.Alfabeto;
import ProcesadoresDeCadenas.PCAFD;
import java.util.ArrayList;

public class ClaseValidacion {

    public void validarAFNLambatoAFN(AFN_Lambda afnl) {

        AFN afn = Automatas.AFN_LambdaToAFNSinImprimir(afnl);
        Alfabeto alfabeto = new Alfabeto(afn.getSigma());

        alfabeto.fiveThousandsStringGenerator();

        boolean afnResult, afnlResult;
        int equal = 0;
        ArrayList<String> diffResult = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            
            afnlResult = afnl.procesarCadena2(alfabeto.cadenas.get(i));
            //System.out.println("AFNL procesó "+alfabeto.cadenas.get(i)+" "+afnlResult);
            
            afnResult = afn.procesarCadenaSinImprimirNiMadres(alfabeto.cadenas.get(i));
            //System.out.println("AFN procesó "+alfabeto.cadenas.get(i)+" "+afnResult);            

            if ((afnResult && afnlResult) || (!afnResult && !afnlResult)) {
                equal++;
            } else {

                diffResult.add(alfabeto.cadenas.get(i));
            }
        }

        System.out.println(equal + " cadenas dieron el mismo resultado");
        System.out.println(diffResult.size() + " cadenas dieron distinto resultado");
        if (!diffResult.isEmpty()) {
            System.out.println("No dieron el mismo resultado las cadenas:");
            for (int i = 0; i < diffResult.size(); i++) {
                System.out.println(diffResult.get(i));
            }
        }

    }
    
    public void validarAFNLambatoAFN2(AFN_Lambda afnl) {

        AFN afn = Automatas.AFN_LambdaToAFNSinImprimir(afnl);
        AFD afd = Automatas.AFNtoAFDSinImprimir(afn);
        PCAFD pc = new PCAFD();
        Alfabeto alfabeto = new Alfabeto(afn.getSigma());

        alfabeto.fiveThousandsStringGenerator();

        boolean afnResult, afnlResult;
        int equal = 0;
        ArrayList<String> diffResult = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            
            afnlResult = afnl.procesarCadena(alfabeto.cadenas.get(i));
            //System.out.println("AFNL procesó "+alfabeto.cadenas.get(i)+" "+afnlResult);
            
            afnResult = pc.processStringSinImprimirNiMadres(afd, alfabeto.cadenas.get(i),false);
            //System.out.println("AFN procesó "+alfabeto.cadenas.get(i)+" "+afnResult);            

            if ((afnResult && afnlResult) || (!afnResult && !afnlResult)) {
                equal++;
            } else {

                diffResult.add(alfabeto.cadenas.get(i));
            }
        }

        System.out.println(equal + " cadenas dieron el mismo resultado");
        System.out.println(diffResult.size() + " cadenas dieron distinto resultado");
        if (!diffResult.isEmpty()) {
            System.out.println("No dieron el mismo resultado las cadenas:");
            for (int i = 0; i < diffResult.size(); i++) {
                System.out.println(diffResult.get(i));
            }
        }

    }
    
    public void validarAFNtoAFD(AFN afn) {

        AFD afd = Automatas.AFNtoAFD(afn);
        PCAFD pc = new PCAFD();
        Alfabeto alfabeto = new Alfabeto(afn.getSigma());
        

        alfabeto.fiveThousandsStringGenerator();

        boolean afdResult, afnResult;
        int equal = 0;
        ArrayList<String> diffResult = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            
            afdResult = pc.processStringSinImprimirNiMadres(afd,alfabeto.cadenas.get(i),true);
            //System.out.println("AFNL procesó "+alfabeto.cadenas.get(i)+" "+afnlResult);
            
            afnResult = afn.procesarCadenaSinImprimirNiMadres(alfabeto.cadenas.get(i));
            //System.out.println("AFN procesó "+alfabeto.cadenas.get(i)+" "+afnResult);            

            if ((afdResult && afnResult) || (!afdResult && !afnResult)) {
                equal++;
            } else {

                diffResult.add(alfabeto.cadenas.get(i));
            }
        }

        System.out.println(equal + " cadenas dieron el mismo resultado");
        System.out.println(diffResult.size() + " cadenas dieron distinto resultado");
        if (!diffResult.isEmpty()) {
            System.out.println("No dieron el mismo resultado las cadenas:");
            for (int i = 0; i < diffResult.size(); i++) {
                System.out.println(diffResult.get(i));
            }
        }

    }
    
    public void validarAFNtoAFD2(AFN afn) {

        AFD afd = Automatas.AFNtoAFDSinImprimir(afn);
        PCAFD pc = new PCAFD();
        Alfabeto alfabeto = new Alfabeto(afn.getSigma());
        

        alfabeto.fiveThousandsStringGenerator();

        boolean afdResult, afnResult;
        int equal = 0;
        ArrayList<String> diffResult = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            
            afdResult = pc.processStringSinImprimirNiMadres(afd,alfabeto.cadenas.get(i),false);
            //System.out.println("AFNL procesó "+alfabeto.cadenas.get(i)+" "+afnlResult);
            
            afnResult = pc.processStringSinImprimirNiMadres(afd,alfabeto.cadenas.get(i),false);
            //System.out.println("AFN procesó "+alfabeto.cadenas.get(i)+" "+afnResult);            

            if ((afdResult && afnResult) || (!afdResult && !afnResult)) {
                equal++;
            } else {

                diffResult.add(alfabeto.cadenas.get(i));
            }
        }

        System.out.println(equal + " cadenas dieron el mismo resultado");
        System.out.println(diffResult.size() + " cadenas dieron distinto resultado");
        if (!diffResult.isEmpty()) {
            System.out.println("No dieron el mismo resultado las cadenas:");
            for (int i = 0; i < diffResult.size(); i++) {
                System.out.println(diffResult.get(i));
            }
        }

    }
    
    

    public static void main(String[] args) throws Exception {

        AFN_Lambda afnl = new AFN_Lambda();
        afnl.initializeAFD("AFN_Lambda1.txt");
        
        AFN afn = Automatas.AFN_LambdaToAFNSinImprimir(afnl);
            
        ClaseValidacion cv = new ClaseValidacion();
        //cv.validarAFNLambatoAFN(afnl);
        cv.validarAFNtoAFD2(afn);

    }

}
