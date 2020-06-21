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
        }else if(this.TipoAuntomata.equals("AFN$")){
            afn_lambda.initializeAFD("file.txt");
        }else{
            System.out.println("El automata seleccionado no existe");
        }
    }
    
    public void showAFDData(){
        afd.showSigma();
        afd.showStates();
        afd.showInitialState();
        afd.showFinalStates();
        afd.showDelta();
    }
    
    public void showAFNData(){
        afn.showSigma();
        afn.showStates();
        afn.showInitialState();
        afn.showFinalStates();
        afn.showDelta();
    }
       
    public void showAFN_LambdaData(){
        afn_lambda.showSigma();
        afn_lambda.showStates();
        afn_lambda.showInitialState();
        afn_lambda.showFinalStates();
        afn_lambda.showDelta();
    }
    
    public void showAutomataData(){
        if(this.TipoAuntomata.equals("AFD")){
            showAFDData();
        }else if(this.TipoAuntomata.equals("AFN")){
            showAFNData();
        }else if(this.TipoAuntomata.equals("AFN$")){
            showAFN_LambdaData();
        }else{
            System.out.println("El automata seleccionado no existe");
        }
    }
    
    public void ProcessStringAutomata(ArrayList<String> prueba) throws IOException{
        if(this.TipoAuntomata.equals("AFD")){
            ProcessStringAFD(afd,prueba);
        }else if(this.TipoAuntomata.equals("AFN")){
            ProcessStringAFN(prueba);
        }else if(this.TipoAuntomata.equals("AFN$")){
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
    
    public static void main(String[] args) throws IOException{
        Automatas automata = new Automatas();
        automata.initializeAutomata("file.txt");
        automata.createAutomata();
        automata.showAutomataData();
        //datos prueba
        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aba");
        prueba.add("baba");
        prueba.add("abba");
        prueba.add("");
        prueba.add("b");
        
        automata.ProcessStringAutomata(prueba);
    }
    
}
