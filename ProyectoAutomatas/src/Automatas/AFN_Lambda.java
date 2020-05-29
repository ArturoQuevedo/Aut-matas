package Automatas;

import java.util.*;
import java.io.*;


public class AFN_Lambda {

    public static class Automata {

        private ArrayList<Character> sigma;
        private ArrayList<String> states;
        private String q;
        private ArrayList<String> finalStates;
        private ArrayList<String>[][] delta;

        public Automata() {
            this.sigma = new ArrayList<>();
            this.states = new ArrayList<>();
            this.finalStates = new ArrayList<>();
        }

        public void initializeDelta(int sizeOfStates, int sizeofSigma) {
            this.delta = new ArrayList[sizeOfStates][sizeofSigma];
            for (int i = 0; i < sizeOfStates; i++) {
                for (int j = 0; j < sizeofSigma; j++) {
                    this.delta[i][j] = new ArrayList<String>();
                }
            }
        }

        public void showDelta() {
            for (int i = 0; i < this.states.size(); i++) {
                System.out.print("Estado " + this.states.get(i) + "   ");
                for (int j = 0; j < this.sigma.size(); j++) {
                    System.out.print(this.delta[i][j] + " ");
                }
                System.out.println("\n");
            }

        }

        public void showSigma() {
            System.out.println("Alphabet:");
            for (int i = 0; i < this.sigma.size(); i++) {
                System.out.println(this.sigma.get(i));
            }
        }

        public void showStates() {
            System.out.println("States:");
            for (int i = 0; i < this.states.size(); i++) {
                System.out.println(this.states.get(i));
            }
        }

        public void showFinalStates() {
            System.out.println("Final states:");
            for (int i = 0; i < this.finalStates.size(); i++) {
                System.out.println(this.finalStates.get(i));
            }
        }

        public void showInitialState() {
            System.out.println("Initial state: " + this.q);
        }

        public void initializeAFD() throws FileNotFoundException, IOException {

            File file = new File("file.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringTokenizer tokenizer;

            String line;
            while ((line = br.readLine()) != null) {

                switch (line) {
                    case ("#alphabet"):

                        while (!(line = br.readLine()).startsWith("#")) {

                            if (line.contains("-")) {   //Rango de caracteres Ej:  a-z
                                char ch = line.charAt(0);   //Primer caracter del rango

                                //Mientras no se llegue al final del rango
                                while (ch != line.charAt(2)) {
                                    //Agregar ch
                                    this.sigma.add(ch);
                                    //Pasar al siguiente caracter ASCII                                    
                                    ch = (char) ((int) ch + 1);

                                    if (ch == line.charAt(2)) { //Si se llega al Ãºtimo, agregarlo
                                        //Agregar ch                                        
                                        this.sigma.add(ch);
                                    }
                                }
                            }
                        }
                        this.sigma.add('$');

                    case ("#states"):

                        while (!(line = br.readLine()).startsWith("#")) {
                            this.states.add(line);
                            //System.out.println("Se agregÃ³ "+line+" a los estados");
                        }

                        //DespuÃ©s de aÃ±adir todo el alfabeto y estados, se tiene el tamaÃ±o de la matriz de transiciÃ³n
                        this.initializeDelta(this.states.size(), this.sigma.size());
                    //System.out.println("Se creo una matriz de " + this.states.size() + " por " + this.sigma.size());

                    case ("#initial"):

                        while (!(line = br.readLine()).startsWith("#")) {
                            this.q = line;
                            //System.out.println("Se agregÃ³ "+line+" a los estados iniciales");
                        }

                    case ("#accepting"):

                        while ((!(line = br.readLine()).startsWith("#")) && this.states.contains(line)) {
                            this.finalStates.add(line);
                            //System.out.println("Se agregÃ³ "+line+" a los estados de aceptaciÃ³n");
                        }

                    case ("#transitions"):

                        while ((line = br.readLine()) != null) {
                            tokenizer = new StringTokenizer(line, " :>,");

                            String currentState = tokenizer.nextToken();
                            Character currentChar = tokenizer.nextToken().charAt(0);
                            String transition;

                            //System.out.println("Estado " + currentState);
                            //System.out.println("Caracter " + currentChar);
                            if (this.states.contains(currentState) && this.sigma.contains(currentChar)) {

                                
                                while (tokenizer.hasMoreElements()) {
                                    transition = tokenizer.nextToken();
                                    //System.out.println(this.states.indexOf(currentState) + " " + this.sigma.indexOf(currentChar));
                                    this.delta[this.states.indexOf(currentState)][this.sigma.indexOf(currentChar)].add(transition);
                                    

                                }
                            }
                        }
                    default:

                }
            }
            br.close();

        }
         

        int getPosEstado(String estado){
            int val = 0;
            for(int i=0;i<this.states.size();i++){
//                System.out.println(this.states.get(i));
                if(estado.equals(this.states.get(i))){
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        int getPosSimbolo(String simbolo){
            int val = 0;
            for(int i=0;i<this.states.size();i++){
//                System.out.println(this.states.get(i));
                if(simbolo.equals(Character.toString(this.sigma.get(i)))){
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        boolean procesarCadena(String cadena, String estado, int letra, boolean loop, boolean aceptada){
            if(!(letra>=cadena.length())){
                int i;
                aceptada = false;
                String estadoActual;
                String estadoAnterior;
                String simbolo;
                String lambda = "$";


                int posicionEstado;
                int posicionSimbolo;
                int posicionLambda = getPosSimbolo(lambda);

                int letraActual = letra;

                estadoActual = estado;
                estadoAnterior = estadoActual;

//                System.out.println("letra: "+letraActual);
                simbolo = Character.toString(cadena.charAt(letraActual));

                posicionEstado = getPosEstado(estadoActual);
                posicionSimbolo = getPosSimbolo(simbolo);
//                System.out.println("Estado: "+estadoActual+":"+simbolo);
                if(!this.delta[posicionEstado][posicionLambda].isEmpty()){
                    if(!loop){
                        for(i = 0;i<this.delta[posicionEstado][posicionLambda].size();i++){
//                            System.out.println(this.delta[posicionEstado][posicionLambda]);
                            if(letraActual< cadena.length()){
                                estadoActual = this.delta[posicionEstado][posicionLambda].get(i);
//                                System.out.println("Estado: "+estadoActual+">"+lambda);
                                if(estadoAnterior == estadoActual)
                                    aceptada = procesarCadena(cadena, estadoActual, letraActual, true, aceptada);
                                else
                                    aceptada = procesarCadena(cadena, estadoActual, letraActual, false, aceptada ) || aceptada;
                            }
                        }
                    }else{
//                        System.out.println("procesamiento abortado");
                        aceptada = false;
                        return aceptada;
                    }
                }
                estadoActual = estadoAnterior;
                if(!this.delta[posicionEstado][posicionSimbolo].isEmpty()){
                    for(i = 0;i<this.delta[posicionEstado][posicionSimbolo].size();i++){
//                        System.out.println(estadoActual+": " +simbolo + ">" + this.delta[posicionEstado][posicionSimbolo]);
                        if(letraActual< cadena.length()){
                            ++letraActual;
                            estadoActual = this.delta[posicionEstado][posicionSimbolo].get(i);
//                            System.out.println("Estado: "+estadoActual+">"+simbolo);
                            aceptada = procesarCadena(cadena, estadoActual, letraActual, false, (aceptada)) || aceptada;
                        }else{
//                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
//                    System.out.println("procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada)
                    aceptada = (verify(letraActual, cadena, estadoActual, i, aceptada)) || aceptada;
            }
            return aceptada;
        }
        
        
        boolean procesarCadenaConDetalles(String cadena, String estado, int letra, boolean loop, boolean aceptada){
            if(!(letra>=cadena.length())){
                int i;
                aceptada = false;
                String estadoActual;
                String estadoAnterior;
                String simbolo;
                String lambda = "$";


                int posicionEstado;
                int posicionSimbolo;
                int posicionLambda = getPosSimbolo(lambda);

                int letraActual = letra;

                estadoActual = estado;
                estadoAnterior = estadoActual;

                System.out.println("letra: "+letraActual);
                simbolo = Character.toString(cadena.charAt(letraActual));

                posicionEstado = getPosEstado(estadoActual);
                posicionSimbolo = getPosSimbolo(simbolo);
                System.out.println("Estado: "+estadoActual+":"+simbolo);
                if(!this.delta[posicionEstado][posicionLambda].isEmpty()){
                    if(!loop){
                        for(i = 0;i<this.delta[posicionEstado][posicionLambda].size();i++){
                            System.out.println(this.delta[posicionEstado][posicionLambda]);
                            if(letraActual< cadena.length()){
                                estadoActual = this.delta[posicionEstado][posicionLambda].get(i);
                                System.out.println("Estado: "+estadoActual+">"+lambda);
                                if(estadoAnterior == estadoActual)
                                    aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, true, aceptada);
                                else
                                    aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, false, aceptada ) || aceptada;
                            }
                        }
                    }else{
                        System.out.println("procesamiento abortado");
                        aceptada = false;
                        return aceptada;
                    }
                }
                estadoActual = estadoAnterior;
                if(!this.delta[posicionEstado][posicionSimbolo].isEmpty()){
                    for(i = 0;i<this.delta[posicionEstado][posicionSimbolo].size();i++){
                        System.out.println(estadoActual+": " +simbolo + ">" + this.delta[posicionEstado][posicionSimbolo]);
                        if(letraActual< cadena.length()){
                            ++letraActual;
                            estadoActual = this.delta[posicionEstado][posicionSimbolo].get(i);
                            System.out.println("Estado: "+estadoActual+">"+simbolo);
                            aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, false, (aceptada)) || aceptada;
                        }else{
                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
                    System.out.println("procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada)
                    aceptada = (verifyConDetalles(letraActual, cadena, estadoActual, i, aceptada)) || aceptada;
            }
            return aceptada;
        }
        
        boolean procesarCadenaConDetalles(String cadena){
            return procesarCadenaConDetalles(cadena, this.q, 0, false, true);
        }
        
        boolean procesarCadena(String cadena){
            return procesarCadena(cadena, this.q, 0, false, true);
        }
        
        boolean verify(int letraActual, String cadena, String estadoActual, int i, boolean aceptada){
//            System.out.println("Voy en la letra "+letraActual + " y el size de la cadena es "+ cadena.length()+" Estado actual "+ estadoActual);
            if(letraActual == cadena.length()){
                for(i = 0; i< this.finalStates.size(); i++){
                    if(estadoActual.equals(this.finalStates.get(i))){
//                        System.out.println("Cadena Aceptada");
                        return true;
                    }
                }
//                System.out.println("Cadena Rechazada");
                return false;
            }
            return aceptada;
        }
        
        boolean verifyConDetalles(int letraActual, String cadena, String estadoActual, int i, boolean aceptada){
//            System.out.println("Voy en la letra "+letraActual + " y el size de la cadena es "+ cadena.length()+" Estado actual "+ estadoActual);
            if(letraActual == cadena.length()){
                for(i = 0; i< this.finalStates.size(); i++){
                    if(estadoActual.equals(this.finalStates.get(i))){
                        System.out.println("Cadena Aceptada");
                        return true;
                    }
                }
                System.out.println("Cadena Rechazada");
                return false;
            }
            return aceptada;
        }
    }   
    
    public static void main(String[] args) throws Exception {

        Automata afd = new Automata();
        afd.initializeAFD();
        afd.showSigma();
        afd.showStates();
        afd.showInitialState();
        afd.showFinalStates();
        afd.showDelta();
        boolean resultado = afd.procesarCadena("babaabb");
//        boolean resultado = afd.procesarCadena("b");
//
        System.out.println(resultado);
        
    }

}