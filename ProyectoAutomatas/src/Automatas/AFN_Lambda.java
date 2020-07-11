package Automatas;



import static Automatas.Automatas.*;
import ProcesadoresDeCadenas.PCAFD;
import java.util.*;
import java.io.*;


public class AFN_Lambda {


        private ArrayList<Character> sigma;
        private ArrayList<String> states;
        private String q;
        private ArrayList<String> finalStates;
        private ArrayList<String>[][] delta;
        public ArrayList<ArrayList<String>> globalito = new ArrayList<ArrayList<String>>();
        public int totalProcesamientos = 0;
        public ArrayList<String> aceptada ;
        public ArrayList<String> aceptada_L;
        public ArrayList<String> rechazada_L ;
        public ArrayList<String> rechazada;
        public ArrayList<String> abortada;
        private ArrayList<String> inaccessibleStates = new ArrayList<>();
        public ArrayList<ArrayList<String>> aceptadaR = new ArrayList<ArrayList<String>>();
        public ArrayList<ArrayList<String>> rechazadaR = new ArrayList<ArrayList<String>>();
        public ArrayList<ArrayList<String>> abortadaR = new ArrayList<ArrayList<String>>();
        
        
        public String ultimoEstado = "";
        
        
         

        public AFN_Lambda() {
            this.sigma = new ArrayList<>();
            this.states = new ArrayList<>();
            this.finalStates = new ArrayList<>();
            this.aceptada = new ArrayList<>();
            this.rechazada = new ArrayList<>();
            this.abortada = new ArrayList<>();
            
            for(int i=0;i<250;i++){
                ArrayList<String> a = new ArrayList<>();
                this.globalito.add(a);
            }
            
            for(int i=0;i<250;i++){
                ArrayList<String> a = new ArrayList<>();
                this.aceptadaR.add(a);
            }
            
            for(int i=0;i<250;i++){
                ArrayList<String> a = new ArrayList<>();
                this.rechazadaR.add(a);
            }
            
            for(int i=0;i<500;i++){
                ArrayList<String> a = new ArrayList<>();
                this.abortadaR.add(a);
            }
            
            
            
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
        System.out.println("Initial state: ");
        System.out.println(this.q);
    }
    public void ShowInaccessibleStates(){
    int i,j,k;
        System.out.println("Inaccesible States:");
        for(i=0;i<this.inaccessibleStates.size();i++){
            System.out.println(this.inaccessibleStates.get(i));
        }
    }
    
    public void showAllTipeOfStates(){
        hallarEstadosInaccesibles();
        showSigma();
        showStates();
        showInitialState();
        ShowInaccessibleStates();
        showFinalStates();
        showDelta();
    }
        public void initializeAFD(String fileRoute) throws FileNotFoundException, IOException {

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
                            tokenizer = new StringTokenizer(line, " :>;");

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
         

        public int getPosEstado(String estado){
            int val = -1; // aqui se asignan posiciones a estados que no existen, posible solución es hacer val = -1 pero no se si afecte el codigo de cesar
            for(int i=0;i<this.states.size();i++){
//                System.out.println(this.states.get(i));
                if(estado.equals(this.states.get(i))){
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        public int getPosSimbolo(String simbolo){
            int val = -1;
            for(int i=0;i<this.sigma.size();i++){
//                System.out.println(this.states.get(i));
                if(simbolo.equals(Character.toString(this.sigma.get(i)))){
                    val = i;
                    break;
                }
            }
            return val;
        }
        
        boolean procesarCadena(String cadena, String estado, int letra, boolean loop, boolean aceptada, String estadoAnterior){
            if(!(letra>=cadena.length())){
                int i;
                aceptada = false;
                String estadoActual;
                String simbolo;
                String lambda = "$";


                int posicionEstado;
                int posicionSimbolo;
                int posicionLambda = getPosSimbolo(lambda);

                int letraActual = letra;

                estadoActual = estado;

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
                                if(estadoAnterior.equals(estadoActual)){
                                    loop = true;
                                    aceptada = procesarCadena(cadena, estadoActual, letraActual, true, aceptada, estadoAnterior);
                                }
                                else{
                                    estadoAnterior = estado;
                                    aceptada = procesarCadena(cadena, estadoActual, letraActual, loop, aceptada, estadoAnterior) || aceptada;
                                }
                                    
                            }
                        }
                    }else{
//                        System.out.println("Procesamiento abortado");
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
                            aceptada = procesarCadena(cadena, estadoActual, letraActual, false, (aceptada), estadoAnterior) || aceptada;                            
                        }else{
//                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
//                    System.out.println("Procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada){
                    aceptada = (verify(letraActual, cadena, estadoActual, i, aceptada)) || aceptada;
                }
            }
            return aceptada;
        }
        
        
        boolean procesarCadenaConDetalles(String cadena, String estado, int letra, boolean loop, boolean aceptada, String estadoAnterior){
            if(!(letra>=cadena.length())){
                int i;
                aceptada = false;
                String estadoActual;
                String simbolo;
                String lambda = "$";


                int posicionEstado;
                int posicionSimbolo;
                int posicionLambda = getPosSimbolo(lambda);

                int letraActual = letra;

                estadoActual = estado;

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
                                if(estadoAnterior.equals(estadoActual)){
                                    loop = true;
                                    aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, true, aceptada, estadoAnterior);
                                }
                                else{
                                    estadoAnterior = estado;
                                    aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, loop, aceptada, estadoAnterior) || aceptada;
                                }
                                    
                            }
                        }
                    }else{
                        System.out.println("Procesamiento abortado");
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
                            aceptada = procesarCadenaConDetalles(cadena, estadoActual, letraActual, false, (aceptada), estadoAnterior) || aceptada;                            
                        }else{
                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
                    System.out.println("Procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada){
                    aceptada = (verifyConDetalles(letraActual, cadena, estadoActual, i, aceptada)) || aceptada;
                }
            }
            return aceptada;
        }
        
        boolean procesarCadenaConDetalles(String cadena){
            return procesarCadenaConDetalles(cadena, this.q, 0, false, true, this.q);
        }
        
        public boolean procesarCadena(String cadena){
            return procesarCadena(cadena, this.q, 0, false, true, this.q);
        }
        
        public boolean verify(int letraActual, String cadena, String estadoActual, int i, boolean aceptada){
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
        
        public boolean verifyConDetalles(int letraActual, String cadena, String estadoActual, int i, boolean aceptada){
//            System.out.println("Voy en la letra "+letraActual + " y el size de la cadena es "+ cadena.length()+" Estado actual "+ estadoActual);
            if(letraActual == cadena.length()){
                for(i = 0; i< this.finalStates.size(); i++){
                    if(estadoActual.equals(this.finalStates.get(i))){
                        //System.out.println("Cadena Aceptada");
                        return true;
                    }
                }
                //System.out.println("Cadena Rechazada");
                return false;
            }
            return aceptada;
        }
        
        public ArrayList<String> calcularLambdaClausura(String estado){
        
            ArrayList<String> visitedStates = new ArrayList<>();//Los estados que ya visitamos para evitar caer en bucles
            ArrayList<String> statesToVisit = new ArrayList<>();//los estados que tenemos que visitar (solo entran los que no estan en el array de arriba)
            ArrayList<String> antiMultiplesVisitas = new ArrayList<>(); // esto evitara que un mismo lugar se visite varias veces (es complicado explicarlo aqui entonces preguntenme plox :v (A Nicolas))
            for(int i = 0;i < this.states.size();i++){
            antiMultiplesVisitas.add(this.states.get(i));
            }
           
            statesToVisit.add(estado);
            visitedStates.add(estado); // esto lo hago para que la función no tenga problema iniciando, al final del codigo se quita
            
            while(!statesToVisit.isEmpty()){
                
                int statePosition  = getPosEstado(statesToVisit.get(0));
                int symbolPosition = getPosSimbolo("$");
                
                if(this.delta[statePosition][symbolPosition].isEmpty()){
                //en el caso de que no vaya hacia ningun lado con lambda
                    visitedStates.add(statesToVisit.remove(0));
             
                }else{
                    //verificando que el estado que se quiere agregar no se haya visitado antes
                    
                    for(int i = 0; i < this.delta[statePosition][symbolPosition].size(); i++){
                  
                        for(int j = 0; j < visitedStates.size();j++ ){
                            
                            if(this.delta[statePosition][symbolPosition].get(i).equals(visitedStates.get(j))){
                                
                                break;
                                
                            }
                            
                            if(j == visitedStates.size()-1){
                                // si llego hasta aqui es por que el estado no se ha visitado hasta el momento entonces se agrega

                                if(antiMultiplesVisitas.contains(this.delta[statePosition][symbolPosition].get(i))){
                                    
                                    statesToVisit.add(this.delta[statePosition][symbolPosition].get(i)); // esto es para evitar multiples visitas debido a que visitedStates se actualiza lento 
                                    antiMultiplesVisitas.remove(this.delta[statePosition][symbolPosition].get(i));
                                    
                                }
                                
                                
                                
                            }
                           
                        }
                   
                    }
                //pasando el estado que se acaba de visitar a la lista de visitados y quitandolo de los estados a visitar
                    
                visitedStates.add(statesToVisit.remove(0));
                
                }
            
            }
        visitedStates.remove(0); // aqui se quita lo que añadi al inicio 
        
        
        
        
        return visitedStates;
        
        };
        
        public ArrayList<String> printLambdaClausura(String estado){
        
            ArrayList<String> lambdaClausura = new ArrayList<>();
            lambdaClausura = calcularLambdaClausura(estado);
            Collections.sort(lambdaClausura);
            
            System.out.print("La Lambda Clausura del estado " + estado +" es : "+"[ " );
            
            for(int i = 0; i < lambdaClausura.size()-1;i++){
                
                System.out.print(lambdaClausura.get(i) + ", ");
                
            }
            System.out.print(lambdaClausura.get(lambdaClausura.size()-1));//esto es sencillamente par aque no quede una coma suelta al final :V
            
            System.out.print(" ]\n");
            return lambdaClausura;
            
        }
        
        public ArrayList<ArrayList<String>> calcularMuchasLambdaClausura (ArrayList<String> estados){
        
        ArrayList<ArrayList<String>> muchasLambdaClausuras = new ArrayList<>();
        
        for(int i = 0; i < estados.size();i++){
            
            muchasLambdaClausuras.add(printLambdaClausura(estados.get(i)));
            
        }

        return muchasLambdaClausuras;
        }
        
        
        
        public ArrayList<String> calcularMuchasLambdaClausuraSinImprimir(ArrayList<String> estados){
        
            ArrayList<String> muchasLambdaClausuras = new ArrayList<>();
            ArrayList<String> temporal = new ArrayList<>();
            
            for(int i = 0; i < estados.size(); i++){
                
                temporal = calcularLambdaClausura(estados.get(i));
                if(!temporal.isEmpty()){
                    
                    for(int j = 0;j < temporal.size();j++){
                      
                        if(!muchasLambdaClausuras.contains(temporal.get(j))){
                        
                            muchasLambdaClausuras.add(temporal.get(j));
                            
                        }
                        
                    }
                    
                    
                }
                
            }
        
            
            
            return muchasLambdaClausuras;
        }
        

        boolean computarTodosLosProcesamientos(String cadena, String estado, int letra, boolean loop, boolean aceptada, String salida, int indice, String estadoAnterior){

            if(!(letra>=cadena.length())){
                int i;
                aceptada = false;
                String estadoActual;
                String simbolo;
                String lambda = "$";


                int posicionEstado;
                int posicionSimbolo;
                int posicionLambda = getPosSimbolo(lambda);

                int letraActual = letra;

                estadoActual = estado;

//                System.out.println("letra: "+letraActual);
                simbolo = Character.toString(cadena.charAt(letraActual));

                posicionEstado = getPosEstado(estadoActual);
                posicionSimbolo = getPosSimbolo(simbolo);
//                System.out.println("Estado: "+estadoActual+":"+simbolo);
                
                String temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
                
                this.globalito.get(indice).add(temp);
//                System.out.println(temp);
                if(!this.delta[posicionEstado][posicionLambda].isEmpty()){
                    if(!loop){
                        for(i = 0;i<this.delta[posicionEstado][posicionLambda].size();i++){
//                            System.out.println(this.delta[posicionEstado][posicionLambda]);
                            if(letraActual< cadena.length()){
                                estadoActual = this.delta[posicionEstado][posicionLambda].get(i);
//                                System.out.println("Estado: "+estadoActual+">"+lambda);
                                if(estadoAnterior.equals(estadoActual)){
                                    loop = true;
                                    aceptada = computarTodosLosProcesamientos(cadena, estadoActual, letraActual, true, aceptada, salida, indice, estadoAnterior);
                                    indice++;
                                    temp = String.format("[%s, %s]", this.q, cadena);
                                    this.globalito.get(indice).add(temp);
                                }
                                else{
                                    estadoAnterior = estado;
                                    aceptada = computarTodosLosProcesamientos(cadena, estadoActual, letraActual, loop, aceptada, salida, indice, estadoAnterior) || aceptada;
                                    indice++;
                                    temp = String.format("[%s, %s]", this.q, cadena);
                                    this.globalito.get(indice).add(temp);
                                }
                                    
                            }
                        }
                    }else{
                        if(this.delta[posicionEstado][posicionSimbolo].isEmpty()){
                            temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
    //                        System.out.println(temp);
                            this.globalito.get(indice).add(temp);
                            this.globalito.get(indice).add("Abortado");
                            this.totalProcesamientos++;
    //                        System.out.println("Procesamiento abortado");
                            aceptada = false;
                            return aceptada;
                        }
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
                            aceptada = computarTodosLosProcesamientos(cadena, estadoActual, letraActual, false, (aceptada), salida, indice, estadoAnterior) || aceptada;
                            if(!aceptada){
                                letraActual = letra;
                            }
                        }else{
                            this.globalito.get(indice).add("No aceptacion");
                            indice++;
//                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
                    temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
//                    System.out.println(temp);
                    this.globalito.get(indice).add(temp);
                    this.globalito.get(indice).add("Abortado");
                    this.totalProcesamientos++;
//                    System.out.println("Procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada){
                    aceptada = (verifyComputarTodosLosProcesamientos(letraActual, cadena, estadoActual, i, aceptada, indice)) || aceptada;
                }
            }else if(this.finalStates.contains(estado) && cadena.length() == 0){
                String temp = String.format("[%s, %s]", this.q, " ");
                this.globalito.get(indice).add(temp);
                this.globalito.get(indice).add("Aceptacion");
                return true;
            }else if(this.finalStates.contains(estado)){
                if(!aceptada){
                    aceptada = (verifyComputarTodosLosProcesamientos(letra, cadena, estado, 0, aceptada, indice)) || aceptada;
                }
                return aceptada;
            }
            
            if(!aceptada){
                    aceptada = (verifyComputarTodosLosProcesamientos(letra, cadena, estado, 0, aceptada, indice)) || aceptada;
                }
            return aceptada;
        }
        
        public void organizar(){
            int i = 0;
            int j = 0;
            
            //Organizando las aceptadas
            while (true) {

                if (j == aceptada.size()) {
                    break;
                }

                if (aceptada.get(j).equals("Aceptacion")) {
                    
                    aceptadaR.get(i).add(aceptada.get(j));
                    i++;
                    j++;
                    
                } else {
                    aceptadaR.get(i).add(aceptada.get(j));
                    j++;
                    
                }

            }
            
            i = 0;
            j = 0;
            //Organizando las rechazadas
            while (true) {

                if (j == rechazada.size()) {
                    break;
                }

                if (rechazada.get(j).equals("No aceptacion")) {
                    
                    rechazadaR.get(i).add(rechazada.get(j));
                    i++;
                    j++;
                    
                } else {
                    rechazadaR.get(i).add(rechazada.get(j));
                    j++;
                    
                }

            }
            
            i = 0;
            j = 0;
            //Organizando las abrotadas
            while (true) {

                if (j == abortada.size()) {
                    break;
                }

                if (abortada.get(j).equals("Abortado")) {
                    
                    abortadaR.get(i).add(abortada.get(j));
                    i++;
                    j++;
                    
                } else {
                    abortadaR.get(i).add(abortada.get(j));
                    j++;
                    
                }

            }
               
            
        }
        
       public void eliminarRepetidas() {
           

        //aceptadas   
        
        for (int i = 0; i < (aceptadaR.size() - 1); i++) {
            for (int j = i+1; j < aceptadaR.size(); j++) {
              
                if (aceptadaR.get(i).equals(aceptadaR.get(j))) {
                aceptadaR.remove(j);
                j--;

            }
                
            }
            
            
        }
        
        
        //rechazadas   
        
        for (int i = 0; i < (rechazadaR.size() - 1); i++) {
            for (int j = i+1; j < rechazadaR.size(); j++) {
              
                if (rechazadaR.get(i).equals(rechazadaR.get(j))) {
                rechazadaR.remove(j);
                j--;

            }
                
            }
            
            
        }
        
        
        //abortadas  
        
        for (int i = 0; i < (abortadaR.size() - 1); i++) {
            for (int j = i+1; j < abortadaR.size(); j++) {
              
                if (abortadaR.get(i).equals(abortadaR.get(j))) {
                abortadaR.remove(j);
                j--;

            }
                
            }
            
            
        }
        
        
        

        


    }
        
        public int computarTodosLosProcesamientos(String cadena, String nombreArchivo) throws IOException {
        //aqui poner la completadora
        String salida = "";
        this.totalProcesamientos = 0;
        int indice = 0;
        boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
        this.filtro();
        organizar();
        eliminarRepetidas();
        procesoFinalAceptacion();
        procesoFinalNoAceptacion();
        eliminarRepetidas();
        if (!resultado) {
            if (!aceptadaR.isEmpty()) {
                resultado = true;
            }
        }
        

        int total = this.totalProcesamientos;
        int tamañoAcep = aceptadaR.size();
        int tamañoNoAcep = rechazadaR.size();
        int tamañoAbort = abortadaR.size();
        for (int j = 0; j < tamañoAcep; j++) {
            //lo largote es el estado final de cada proceso
            procesarCadenaVaciaAceptada(j, aceptadaR.get(j).get(aceptadaR.get(j).size() - 2).substring(1, 3));

        }
        for (int j = 0; j < tamañoNoAcep; j++) {
            //lo largote es el estado final de cada proceso
            procesarCadenaVaciaNoAceptada(j, rechazadaR.get(j).get(rechazadaR.get(j).size() - 2).substring(1, 3));

        }
        eliminarRepetidas();

        tamañoAcep = aceptadaR.size();
        tamañoNoAcep = rechazadaR.size();
        tamañoAbort = abortadaR.size();

        //Cadenas Aceptadas    
        int i = 1; // todas van a manejar el mismo numero al final
        String ruta = nombreArchivo + "Aceptadas.txt";
        String ruta2 = nombreArchivo + "Rechazadas.txt";
        String ruta3 = nombreArchivo + "Abortadas.txt";
        File archivo = new File(ruta);
        File archivo2 = new File(ruta2);
        File archivo3 = new File(ruta3);
        BufferedWriter bw;
        BufferedWriter bw2;
        BufferedWriter bw3;
        while (true) {
            if (archivo.exists()) {

                ruta = nombreArchivo + "Aceptadas" + i + ".txt";
                ruta2 = nombreArchivo + "Rechazadas" + i + ".txt";
                ruta3 = nombreArchivo + "Abortadas" + i + ".txt";
                archivo = new File(ruta);
                archivo2 = new File(ruta2);
                archivo3 = new File(ruta3);
                i++;

            } else {

                bw = new BufferedWriter(new FileWriter(archivo));
                bw2 = new BufferedWriter(new FileWriter(archivo2));
                bw3 = new BufferedWriter(new FileWriter(archivo3));
                break;
            }
        }
        //escribiendo en el documento de aceptadas con el formato adecuado  
        bw.write(cadena + "\n");
        System.out.println(cadena);

        if (aceptadaR.isEmpty()) {
            System.out.println("No hay procesamientos aceptados");
        } else {
            System.out.println("Procesamientos aceptados ------------------------------------------------");
            for (int f = 0; f < aceptadaR.size(); f++) {

                for (int h = 0; h < aceptadaR.get(f).size(); h++) {
                    if (aceptadaR.get(f).get(h).equals("Aceptacion")) {
                        
                        bw.write(aceptadaR.get(f).get(h) + "\n");
                        System.out.print(aceptadaR.get(f).get(h));
                        System.out.println("");

                    } else {
                        bw.write(aceptadaR.get(f).get(h) + "->");
                        System.out.print(aceptadaR.get(f).get(h) + "->");
                    }

                }

            }

            System.out.println("------------------------------------------------------------");
        }

        //escribiendo en el documento de Rechazadas con el formato adecuado  
        bw2.write(cadena + "\n");

        if (rechazadaR.isEmpty()) {
            System.out.println("No hay procesamientos rechazados");
        } else {
            System.out.println("Procesamientos rechazados ------------------------------------------------");
            for (int f = 0; f < rechazadaR.size(); f++) {
                for (int h = 0; h < rechazadaR.get(f).size(); h++) {

                    if (rechazadaR.get(f).get(h).equals("No aceptacion")) {

                        bw2.write(rechazadaR.get(f).get(h) + "\n");
                        System.out.print(rechazadaR.get(f).get(h));
                        System.out.println("");

                    } else {

                        bw2.write(rechazadaR.get(f).get(h) + "->");
                        System.out.print(rechazadaR.get(f).get(h) + "->");
                    }

                }

            }

            System.out.println("------------------------------------------------------------");
        }

        //escribiendo en el documento de Abortadas con el formato adecuado  
        bw3.write(cadena + "\n");
        if (abortadaR.isEmpty()) {
            System.out.println("No hay procesamientos abortados");
        } else {
            System.out.println("Procesamientos abortados ------------------------------------------------");
            for (int f = 0; f < abortadaR.size(); f++) {
                for (int h = 0; h < abortadaR.get(f).size(); h++) {

                    if (abortadaR.get(f).get(h).equals("Abortado")) {

                        bw3.write(abortadaR.get(f).get(h) + "\n");
                        System.out.print(abortadaR.get(f).get(h));
                        System.out.println("");

                    } else {

                        bw3.write(abortadaR.get(f).get(h) + "->");
                        System.out.print(abortadaR.get(f).get(h) + "->");
                    }

                }

            }
            System.out.println("------------------------------------------------------------");
        }
            System.out.println("En total hay : "  +(tamañoAcep + tamañoNoAcep + tamañoAbort) + " procesamientos");
        bw.close();
        bw2.close();
        bw3.close();
            
        globalito.clear();
        aceptadaR.clear();
        rechazadaR.clear();
        abortadaR.clear();
        aceptada.clear();
        rechazada.clear();
        abortada.clear();
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            globalito.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            aceptadaR.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            rechazadaR.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            abortadaR.add(a);
        }

        return (tamañoAcep + tamañoNoAcep + tamañoAbort);
    }
        
        public boolean procesarCadena2(String cadena){
         
        String salida = "";
        this.totalProcesamientos = 0;
        int indice = 0;
        boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
        globalito.clear();
        aceptadaR.clear();
        rechazadaR.clear();
        abortadaR.clear();
        aceptada.clear();
        rechazada.clear();
        abortada.clear();
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            globalito.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            aceptadaR.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            rechazadaR.add(a);
        }
        for (int o = 0; o < 250; o++) {
            ArrayList<String> a = new ArrayList<>();
            abortadaR.add(a);
        }
        return resultado;    
        }
        
    void procesarCadenaVaciaAceptada(int indice, String estadoActual) {
        ArrayList<String> estados = new ArrayList<>(); //los estados a lso que se puede llegar con lambda y cadena vacia 
        estados = calcularLambdaClausura(estadoActual);
        estados.remove(estadoActual);
        //aqui lo ideal seria poner todo el camino de como se llega cada estado pero no se como hacerlo :c tonces solo pondre el estado destino y si es de aceptación o no 
        if (!estados.isEmpty()) {
            ArrayList<String> procesamiento = new ArrayList<>();

            for (int i = 0; i < estados.size(); i++) {

                if (this.getFinalStates().contains(estados.get(i))) {
                    procesamiento = (ArrayList<String>) aceptadaR.get(indice).clone();
                    procesamiento.remove(procesamiento.size() - 1); // quitando el "aceptado" o "no aceptado"
                    procesamiento.add("[" + estados.get(i) + ",]");
                    procesamiento.add("Aceptacion");
                    this.aceptadaR.add(procesamiento);
                } else {
                    procesamiento = (ArrayList<String>) aceptadaR.get(indice).clone();
                    procesamiento.remove(procesamiento.size() - 1); // quitando el "aceptado" o "no aceptado"
                    procesamiento.add("[" + estados.get(i) + ",]");
                    procesamiento.add("No aceptacion");
                    this.rechazadaR.add(procesamiento);
                }

            }

        }

    }
        
    void procesarCadenaVaciaNoAceptada(int indice, String estadoActual) {
        ArrayList<String> estados = new ArrayList<>(); //los estados a lso que se puede llegar con lambda y cadena vacia 
        estados = calcularLambdaClausura(estadoActual);
        estados.remove(estadoActual);
        //aqui lo ideal seria poner todo el camino de como se llega cada estado pero no se como hacerlo :c tonces solo pondre el estado destino y si es de aceptación o no 
        if (!estados.isEmpty()) {
            ArrayList<String> procesamiento = new ArrayList<>();

            for (int i = 0; i < estados.size(); i++) {

                if (this.getFinalStates().contains(estados.get(i))) {
                    procesamiento = (ArrayList<String>) rechazadaR.get(indice).clone();
                    procesamiento.remove(procesamiento.size() - 1); // quitando el "aceptado" o "no aceptado"
                    procesamiento.add("[" + estados.get(i) + ",]");
                    procesamiento.add("Aceptacion");
                    this.aceptadaR.add(procesamiento);
                } else {
                    procesamiento = (ArrayList<String>) rechazadaR.get(indice).clone();
                    procesamiento.remove(procesamiento.size() - 1); // quitando el "aceptado" o "no aceptado"
                    procesamiento.add("[" + estados.get(i) + ",]");
                    procesamiento.add("No aceptacion");
                    this.rechazadaR.add(procesamiento);
                }

            }

        }

    }
        
        
        void confirmadora(ArrayList<String> estados,ArrayList<String> procIncompleto,int columna){
            //que los estados sean la lambda clausura, verificar todos los de la lambda clausura si llega a aceptación procesando el simbolo
            procIncompleto.remove(procIncompleto.size()-1);
            int fila = 0;
            for(int i = 0; i < estados.size();i++){
                fila = getPosEstado(estados.get(i));
                //El simbolo siempre es el mismo, solo cambia el estado que intenta procesarlo
                if(getDelta()[fila][columna].isEmpty()){
                    continue;
                }else{
                    //buscamos que pasa cuando procesamos los estados con la lambda clausura del estado que nos envio aqui
                    for(int j = 0;j <getDelta()[fila][columna].size();j++ ){
                    if(this.getFinalStates().contains(getDelta()[fila][columna].get(j))){
                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.add("[" + estados.get(i) + ","+ this.getSigma().get(columna) +"]");
                        procesamiento.add("[" + getDelta()[fila][columna].get(j) + ",]");
                        procesamiento.add("Aceptacion");
                        aceptadaR.add(procesamiento);
                    }else{
                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.add("[" + estados.get(i) + ","+ this.getSigma().get(columna) +"]");
                        procesamiento.add("[" + getDelta()[fila][columna].get(j) + ",]");
                        procesamiento.add("No aceptacion");
                        rechazadaR.add(procesamiento);
                    }
                    
                }
                }
                
            }
            
        }
    
        void procesoFinalAceptacion() {
        ArrayList<String> estados = new ArrayList<>();
        ArrayList<String> procIncompleto = new ArrayList<>();
        String pareja = "";
        int fila = 0;
        int columna = 0;

        for (int i = 0; i < aceptadaR.size(); i++) {

            if (aceptadaR.get(i).isEmpty()) {
                aceptadaR.remove(i);
                i--;
                //Esto es para eliminar las posiciones que no tienen nada
                continue;
            }
            pareja = aceptadaR.get(i).get(aceptadaR.get(i).size() - 2);
            //buscamos la posicion antes del "aceptada" y comprobamos si ya esta terminada o no
            if (!pareja.substring(3, 5).equals(",]")) {
                //desde la posicion uno hasta la posicion 3(exclusivo) esta el estado y desde el 5 hasta el 6 (exclusivo)
                fila = getPosEstado(pareja.substring(1, 3));
                columna = getPosSimbolo(pareja.substring(5, 6));
                if (getDelta()[fila][columna].isEmpty()) {// Este if revisa si no se puede llegar a ningun lado con el estado y el simbolo
                    if (getDelta()[fila][getSigma().size() - 1].isEmpty()) {//Este se asegura de que se pueda mover con transiciones lambda, si no, el procesamiento se elimina

                        aceptadaR.remove(i);
                            i--;
                            continue;

                    }else if (getDelta()[fila][getSigma().size() - 1].contains(pareja.substring(1, 3)) && getDelta()[fila][getSigma().size() - 1].size() == 1) {
                        //Este if se asegura de que si no esta vacio, el mismo no sea la unica transición posible, si lo es, se elimina
                            aceptadaR.remove(i);
                            i--;
                            continue;

                        }else{
                        //Aqui empezaremos a mirar si podemos llegar a un estado de aceptacion con las transiciones lambda y procesando el simbolo

                         ArrayList<String> estados2 = new ArrayList<>();
                         estados2 = calcularLambdaClausura(pareja.substring(1, 3));
                         estados2.remove(pareja.substring(1, 3));
                         procIncompleto = (ArrayList<String>) aceptadaR.get(i).clone();
                         aceptadaR.remove(i);
                         i--;
                         confirmadora(estados2,procIncompleto,columna);
                         continue;
                            
                        }

                      
                          
                      
                        
                    

                } else {

                    estados = getDelta()[fila][columna];

                }
                procIncompleto = (ArrayList<String>) aceptadaR.get(i).clone();
                aceptadaR.remove(i);
                i--;

                for (int j = 0; j < estados.size(); j++) {

                    if (getFinalStates().contains(estados.get(j))) {

                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.add(procesamiento.size() - 1, "[" + estados.get(j) + ",]");
                        aceptadaR.add(procesamiento);

                    } else {

                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.remove(procesamiento.size() - 1);
                        procesamiento.add("[" + estados.get(j) + ",]");
                        procesamiento.add("No aceptacion");
                        rechazadaR.add(procesamiento);

                    }

                }

            }

        }

        for (int i = 0; i < abortadaR.size(); i++) {

            if (abortadaR.get(i).isEmpty()) {
                abortadaR.remove(i);
            }

        }

    }
        
        void procesoFinalNoAceptacion() {

        ArrayList<String> estados = new ArrayList<>();
        ArrayList<String> procIncompleto = new ArrayList<>();
        String pareja = "";
        int fila = -1;
        int columna = -1;

        for (int i = 0; i < rechazadaR.size(); i++) {

            if (rechazadaR.get(i).isEmpty()) {
                rechazadaR.remove(i);
                i--;
                continue;
            }
            pareja = rechazadaR.get(i).get(rechazadaR.get(i).size() - 2);
            //buscamos la posicion antes del "aceptada" y comprobamos si ya esta terminada o no
                        if (!pareja.substring(3, 5).equals(",]")) {
                //desde la posicion uno hasta la posicion 3(exclusivo) esta el estado y desde el 5 hasta el 6 (exclusivo)
                fila = getPosEstado(pareja.substring(1, 3));
                columna = getPosSimbolo(pareja.substring(5, 6));
                if (getDelta()[fila][columna].isEmpty()) {// Este if revisa si no se puede llegar a ningun lado con el estado y el simbolo
                    if (getDelta()[fila][getSigma().size() - 1].isEmpty()) {//Este se asegura de que se pueda mover con transiciones lambda, si no, el procesamiento se elimina

                        rechazadaR.remove(i);
                            i--;
                            continue;

                    }else if (getDelta()[fila][getSigma().size() - 1].contains(pareja.substring(1, 3)) && getDelta()[fila][getSigma().size() - 1].size() == 1) {
                        //Este if se asegura de que si no esta vacio, el mismo no sea la unica transición posible, si lo es, se elimina
                            rechazadaR.remove(i);
                            i--;
                            continue;

                        }else{
                        //Aqui empezaremos a mirar si podemos llegar a un estado de aceptacion con las transiciones lambda y procesando el simbolo

                         ArrayList<String> estados2 = new ArrayList<>();
                         estados2 = calcularLambdaClausura(pareja.substring(1, 3));
                         estados2.remove(pareja.substring(1, 3));
                         procIncompleto = (ArrayList<String>) rechazadaR.get(i).clone();
                         rechazadaR.remove(i);
                         i--;
                         confirmadora(estados2,procIncompleto,columna);
                         continue;
                            
                        }

                      
                          
                      
                        
                    

                } else {

                    estados = getDelta()[fila][columna];

                }
                procIncompleto = (ArrayList<String>) rechazadaR.get(i).clone();
                rechazadaR.remove(i);
                i--;

                for (int j = 0; j < estados.size(); j++) {

                    if (getFinalStates().contains(estados.get(j))) {

                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.remove(procesamiento.size() - 1);
                        procesamiento.add( "[" + estados.get(j) + ",]");
                        procesamiento.add("Aceptacion");
                        aceptadaR.add(procesamiento);

                    } else {

                        ArrayList<String> procesamiento = new ArrayList<>();
                        procesamiento = (ArrayList<String>) procIncompleto.clone();
                        procesamiento.add(procesamiento.size() - 1, "[" + estados.get(j) + ",]");
                        rechazadaR.add(procesamiento);

                    }

                }

            }

        }

    }
        
        
        public boolean verifyComputarTodosLosProcesamientos(int letraActual, String cadena, String estadoActual, int i, boolean aceptada, int indice){
//            System.out.println("Voy en la letra "+letraActual + " y el size de la cadena es "+ cadena.length()+" Estado actual "+ estadoActual);
            if(letraActual == cadena.length()){
                for(i = 0; i< this.finalStates.size(); i++){
                    if(estadoActual.equals(this.finalStates.get(i))){  
                        this.globalito.get(indice).add("Aceptacion");

                        //System.out.println("Cadena Aceptada");
                        return true;
                    }
                }
                this.globalito.get(indice).add("No aceptacion");
                //System.out.println("Cadena rechazada");
                return false;
            }
            return aceptada;
        }
        
        
        public int respuesta(String cadena){
            switch (cadena) {
                case "Abortado":
                    return 0;
                case "No aceptacion":
                    return 1;
                case "Aceptacion":
                    return 2;
                default:
                    return 3;
            }
        }
        
        public void obtenerLista(ArrayList<String> lista, int inicio, int ultimo, ArrayList<String> objetivo, int index){
            boolean accepted = false;
            if(objetivo == aceptada){
                String estadoInicial = globalito.get(0).get(0);
                objetivo.add(estadoInicial);
                for(int i=0;i<=index;i++){
                    for(int j=1;j<globalito.get(i).size();j++){
                        
                        String prueba = globalito.get(i).get(j);
                        
                        if (prueba.equals("Aceptacion")) {
                            
                                                    //this.ultimoEstado = objetivo.get(objetivo.size()-1);
                                                    //System.out.println("------------------------------------------------Este es :  " + this.ultimoEstado);

                                                }
                        
                        
                        
                        if(prueba.equals("Abortado")){
                            if(objetivo.contains(globalito.get(i).get(j-1))){
                                String del = globalito.get(i).get(j-1);
                                if(!del.equals(estadoInicial)){
                                    int delinit = objetivo.lastIndexOf("Aceptacion");
                                    if(delinit != -1){
                                        if(delinit < objetivo.size()-1){
                                            for(int k = delinit;k<objetivo.size();k++){
                                                if(objetivo.get(k).equals(del)){
                                                    objetivo.remove(k);
                                                }
                                            }
                                        }
                                    }
//                                    objetivo.removeIf(n -> (n.equals(del)));
                                }
                            }
                        }
//                        }else if(prueba.equals("Aceptacion")){
//                            for(int k = inicio;k <= ultimo;k++){
//                                String adding = globalito.get(i).get(k);
//                                objetivo.add(adding);
//                            }
//                        }
                        if(!prueba.equals(globalito.get(i).get(j-1))){
                            if(!prueba.equals("Abortado") && !prueba.equals("No aceptacion")){
                                if(!prueba.equals(estadoInicial)){
//                                    if(!objetivo.contains(prueba)){
//                                        objetivo.add(prueba);
//                                    }
                                    if(!prueba.equals("Aceptacion")){   
                                        String last = objetivo.get(objetivo.size()-1);
                                        int initA = last.indexOf(" ")+2;
                                        int initB = prueba.indexOf(" ")+1;
                                        String subA = last.substring(initA, last.length()-1);
                                        String subB = prueba.substring(initB, prueba.length()-1);
                                        if(subA.equals(subB)){
                                            objetivo.add(prueba);
                                        }
                                        
                                        if(last.equals("Aceptacion")){
                                            accepted = true;
                                            objetivo.add(prueba);
                                        }
//                                        System.out.println(last);
                                        
                                        
                                    }
                                }
                            }
                        }
                    }
                    
//                    String prueba = globalito.get(i).get(1);
//                    if(!prueba.equals("Abortado") && !prueba.equals("Aceptacion") && !prueba.equals("No aceptacion")){
//                        objetivo.add(prueba);
//                    }
                }
                if(!accepted){
                    objetivo.add("Aceptacion");
                }else{
                    accepted = false;
                }
            }else{
                for(int i=inicio;i<lista.size();i++){
                    if(i >= inicio && i<= ultimo){
                        objetivo.add(lista.get(i));
                    }
                    if(i>ultimo) break;
                }
            }
        }
        
        public void filtro(){
            for(int i=0;i<globalito.size();i++){
                int proceso = 0;
                for(int j=0;j<globalito.get(i).size();j++){
                    
                    int resp = this.respuesta(globalito.get(i).get(j));
                    switch (resp) {
                        case 0:
                            if(proceso==0)
                                obtenerLista(globalito.get(i), proceso, j, abortada, 0);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, abortada, 0);
                            proceso = j;
                            break;
                        case 1:
                            if(proceso==0)
                                obtenerLista(globalito.get(i), proceso, j, rechazada, 0);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, rechazada, 0);
                            proceso = j;
                            break;
                        case 2:
                            if(proceso==0)
                                obtenerLista(globalito.get(i), proceso, j, aceptada, i);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, aceptada, i);
                            proceso = j;
                            break;
                        default:
                            break;
                    }
                }
            }
            //System.out.println("recorri todo.");
        }
        
      public void procesarListaCadenas(ArrayList<String> cadenas, String nombreArchivo, boolean imprimirPantalla) throws IOException {

        //Verificar si "nombreArchivo.txt" ya existe
        int i = 1;
        this.totalProcesamientos = 0;
        int aceptadaMasCorta = -1;
        int noAceptadaMasCorta = -1;
        String ruta = nombreArchivo + ".txt";
        File archivo = new File(ruta);
        BufferedWriter bw;
        while (true) {
            if (archivo.exists()) {

                ruta = nombreArchivo + i + ".txt";
                archivo = new File(ruta);
                i++;

            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                break;
            }
        }

        while (!cadenas.isEmpty()) {
            String cadena = cadenas.remove(0);
            String salida = "";
            int indice = 0;
            boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
            this.filtro();
            organizar();
            eliminarRepetidas();    
            procesoFinalAceptacion();
            procesoFinalNoAceptacion();
            eliminarRepetidas();
            
            
            if (!resultado) {
                if (!aceptadaR.isEmpty()) {
                    resultado = true;
                }
            }

            int tamañoAcep = aceptadaR.size();
            int tamañoNoAcep = rechazadaR.size();
            int tamañoAbort = abortadaR.size();
            for (int j = 0; j < tamañoAcep; j++) {
                //lo largote es el estado final de cada proceso
                procesarCadenaVaciaAceptada(j, aceptadaR.get(j).get(aceptadaR.get(j).size() - 2).substring(1, 3));

            }
            for (int j = 0; j < tamañoNoAcep; j++) {
                //lo largote es el estado final de cada proceso
                procesarCadenaVaciaNoAceptada(j, rechazadaR.get(j).get(rechazadaR.get(j).size() - 2).substring(1, 3));

            }
            eliminarRepetidas();
            tamañoAcep = aceptadaR.size();
            tamañoNoAcep = rechazadaR.size();
            tamañoAbort = abortadaR.size();
            int total = (tamañoAbort + tamañoNoAcep + tamañoAcep);

            aceptadaMasCorta = aceptacionMasCorto();
            noAceptadaMasCorta = noAceptacionMasCorto();

            if (!aceptadaR.isEmpty()) {

                if (imprimirPantalla) {
                    System.out.println(cadena);
                }
                bw.write("\n" + cadena + "\n");

                for (i = 0; i < (aceptadaR.get(aceptadaMasCorta).size() - 1); i++) {

                    if (imprimirPantalla) {
                        System.out.print(aceptadaR.get(aceptadaMasCorta).get(i) + "->");
                    }
                    bw.write(aceptadaR.get(aceptadaMasCorta).get(i) + "->");

                }
                if (imprimirPantalla) {
                    System.out.print(aceptadaR.get(aceptadaMasCorta).get(aceptadaR.get(aceptadaMasCorta).size() - 1) + "\n");
                }
                bw.write(aceptadaR.get(aceptadaMasCorta).get(aceptadaR.get(aceptadaMasCorta).size() - 1) + "\n");

                if (imprimirPantalla) {
                    System.out.println("En total hay " + total + " procesamientos posibles.");
                }
                bw.write("\n" + "En total hay " + total + " procesamientos posibles.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoAcep + " procesamientos aceptados.");
                }
                bw.write("\n" + "En total hay " + tamañoAcep + " procesamientos aceptados.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoNoAcep + " procesamientos rechazados.");
                }
                bw.write("\n" + "En total hay " + tamañoNoAcep + " procesamientos rechazados.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoAbort + " procesamientos abortados.");
                }
                bw.write("\n" + "En total hay " + tamañoAbort + " procesamientos abortados.");
                if (imprimirPantalla) {
                    System.out.println("Si");
                }
                bw.write("\n" + "Si");

                //Limpiando todo para sel siguiente ciclo
                globalito.clear();
                aceptadaR.clear();
                rechazadaR.clear();
                abortadaR.clear();
                aceptada.clear();
                rechazada.clear();
                abortada.clear();
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    globalito.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    aceptadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    rechazadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    abortadaR.add(a);
                }

            } else if (!rechazadaR.isEmpty()) {

                if (imprimirPantalla) {
                    System.out.println(cadena);
                }
                bw.write("\n" + cadena + "\n");

                for (i = 0; i < (rechazadaR.get(noAceptadaMasCorta).size() - 1); i++) {

                    if (imprimirPantalla) {
                        System.out.print(rechazadaR.get(noAceptadaMasCorta).get(i) + "->");
                    }
                    bw.write(rechazadaR.get(noAceptadaMasCorta).get(i) + "->");

                }
                if (imprimirPantalla) {
                    System.out.print(rechazadaR.get(noAceptadaMasCorta).get(rechazadaR.get(noAceptadaMasCorta).size() - 1) + "\n");
                }
                bw.write(rechazadaR.get(noAceptadaMasCorta).get(rechazadaR.get(noAceptadaMasCorta).size() - 1) + "\n");

                if (imprimirPantalla) {
                    System.out.println("En total hay " + total + " procesamientos posibles.");
                }
                bw.write("\n" + "En total hay " + total + " procesamientos posibles.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoAcep + " procesamientos aceptados.");
                }
                bw.write("\n" + "En total hay " + tamañoAcep + " procesamientos aceptados.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoNoAcep + " procesamientos rechazados.");
                }
                bw.write("\n" + "En total hay " + tamañoNoAcep + " procesamientos rechazados.");
                if (imprimirPantalla) {
                    System.out.println("En total hay " + tamañoAbort + " procesamientos abortados.");
                }
                bw.write("\n" + "En total hay " + tamañoAbort + " procesamientos abortados.");
                if (imprimirPantalla) {
                    System.out.println("Si");
                }
                bw.write("\n" + "No");

                //Limpiando todo para sel siguiente ciclo
                globalito.clear();
                aceptadaR.clear();
                rechazadaR.clear();
                abortadaR.clear();
                aceptada.clear();
                rechazada.clear();
                abortada.clear();
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    globalito.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    aceptadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    rechazadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    abortadaR.add(a);
                }
            } else {
                System.out.println("TODOS LOS PROCESAMIENTOS DE ESTA CADENA " + cadena + "  ESTAN ABORTADOS.");

                //Limpiando todo para sel siguiente ciclo
                globalito.clear();
                aceptadaR.clear();
                rechazadaR.clear();
                abortadaR.clear();
                aceptada.clear();
                rechazada.clear();
                abortada.clear();
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    globalito.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    aceptadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    rechazadaR.add(a);
                }
                for (int o = 0; o < 250; o++) {
                    ArrayList<String> a = new ArrayList<>();
                    abortadaR.add(a);
                }
            }

            
        }
        bw.close();
    }
        
                
        public int aceptacionMasCorto() {
        int corto = 0;
        if (aceptadaR.isEmpty()) {
            //System.out.println("No hay cadenas Aceptadas");
            return 0;
        } else {

            for (int i = 1; i < aceptadaR.size(); i++) {

                if (aceptadaR.get(corto).size() > aceptadaR.get(i).size()) {
                    corto = i;
                }

            }

            return corto;

        }

    }

    public int noAceptacionMasCorto() {
        int corto = 0;
        if (rechazadaR.isEmpty()) {
            //System.out.println("No hay cadenas No Aceptadas");
            return 0;
        } else {

            for (int i = 1; i < rechazadaR.size(); i++) {

                if (rechazadaR.get(corto).size() > rechazadaR.get(i).size()) {
                    corto = i;
                }

            }

            return corto;

        }
    }
        
        
     
        
        
    public boolean procesarCadenaConDetalles2(String cadena) {
        String salida = "";
        int indice = 0;
        boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
        this.filtro();
        organizar();
        eliminarRepetidas();
        procesoFinalAceptacion();
        procesoFinalNoAceptacion();
        eliminarRepetidas();
        if (!resultado) {
            if (!aceptadaR.isEmpty()) {
                resultado = true;
            }
        }

        int tamañoAcep = aceptadaR.size();
        int tamañoNoAcep = rechazadaR.size();
        for (int j = 0; j < tamañoAcep; j++) {
            //lo largote es el estado final de cada proceso
            procesarCadenaVaciaAceptada(j, aceptadaR.get(j).get(aceptadaR.get(j).size() - 2).substring(1, 3));

        }
        for (int j = 0; j < tamañoNoAcep; j++) {
            //lo largote es el estado final de cada proceso
            procesarCadenaVaciaNoAceptada(j, rechazadaR.get(j).get(rechazadaR.get(j).size() - 2).substring(1, 3));

        }
        eliminarRepetidas();
        if (!aceptadaR.isEmpty()) {

            ArrayList<String> a = new ArrayList<>();
            a = (ArrayList<String>) aceptadaR.get(aceptacionMasCorto()).clone();
            System.out.println(cadena);
            for (int i = 0; i < (a.size() - 1); i++) {
                System.out.print(a.get(i) + "->");
            }
            System.out.print(a.get(a.size() - 1) + "\n");
            globalito.clear();
            aceptadaR.clear();
            rechazadaR.clear();
            abortadaR.clear();
            aceptada.clear();
            rechazada.clear();
            abortada.clear();
            for (int o = 0; o < 250; o++) {
                ArrayList<String> b = new ArrayList<>();
                globalito.add(b);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> b = new ArrayList<>();
                aceptadaR.add(b);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> b = new ArrayList<>();
                rechazadaR.add(b);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> b = new ArrayList<>();
                abortadaR.add(b);
            }
            return true;

        } else {
            System.out.println("No hay procesamientos aceptados");

            globalito.clear();
            aceptadaR.clear();
            rechazadaR.clear();
            abortadaR.clear();
            aceptada.clear();
            rechazada.clear();
            abortada.clear();
            for (int o = 0; o < 250; o++) {
                ArrayList<String> a = new ArrayList<>();
                globalito.add(a);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> a = new ArrayList<>();
                aceptadaR.add(a);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> a = new ArrayList<>();
                rechazadaR.add(a);
            }
            for (int o = 0; o < 250; o++) {
                ArrayList<String> a = new ArrayList<>();
                abortadaR.add(a);
            }
            return false;
        }

    }

    public ArrayList<String> getStates() {
        return states;
    }

    public ArrayList<String>[][] getDelta() {
        return delta;
    }

    public ArrayList<Character> getSigma() {
        return sigma;
    }

    public void hallarEstadosInaccesibles() {

        ArrayList<String> accesibles = new ArrayList<>();
        this.inaccessibleStates.clear();

        accesibles.add(this.getQ());

        for (int j = 0; j < this.getDelta().length; j++) {
            String estadoActual = this.getStates().get(j);
            for (int k = 0; k < this.getDelta()[j].length; k++) {
                if (this.getDelta()[j][k].isEmpty()) {
                    continue;
                } else {
                    for (String transicion : this.getDelta()[j][k]) {
                        if (!estadoActual.equals(transicion)) {
                            if (accesibles.contains(estadoActual)) {
                                if (!accesibles.contains(transicion)) {
                                    accesibles.add(transicion);
                                }
                            }
                        }
                    }
                }
            }
        }
        Collections.sort(accesibles);
        for (int i = 0; i < this.states.size(); i++) {
            if (!accesibles.contains(this.states.get(i))) {
                this.inaccessibleStates.add(this.states.get(i));
            }
        }

    }

    public ArrayList<String> getInaccessibleStates() {
        return inaccessibleStates;
    }

    public String getQ() {
        return q;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }
    
    
    
     public boolean procesarCadenaConversion(String cadena){
        AFN afn = new AFN();
        afn = AFN_LambdaToAFN(this);
        return afn.procesarCadenaConversion(cadena);
        
    }
    
    public boolean procesarCadenaConDetallesConversion(String cadena){
        
        AFN afn = new AFN();
        afn = AFN_LambdaToAFN(this);
        return afn.procesarCadenaConDetallesConversion(cadena);
 
    }
    
    public void procesarListaCadenasConversion(ArrayList<String> stringList, String nombreArchivo, boolean imprimirPantalla) throws IOException{
        
        AFN afn = new AFN();
        afn = AFN_LambdaToAFN(this);
        afn.procesarListaCadenasConversion(stringList,nombreArchivo,imprimirPantalla);
    }
    

    
    
    
    
    public static void main(String[] args) throws Exception {


        AFN_Lambda afnl = new AFN_Lambda();
        
        afnl.initializeAFD("AFN_Lambda_entrega.txt");
        afnl.showDelta();
//        System.out.println(afnl.procesarCadenaConDetalles2("abbb"));
        
        
        ArrayList<String> prueba = new ArrayList<>();
//        prueba.add("");
        prueba.add("aab");
//        prueba.add("aa");
//        prueba.add("aaa");
//        prueba.add("abbb");
//        prueba.add("bbcb");
//        prueba.add("bbbbbb");
//        prueba.add("aaaaaaaaaaaab");

        afnl.procesarListaCadenas(prueba, "probandoLambda", true);
        //afnl.computarTodosLosProcesamientos("aaa", "holuu");
        //afnl.procesarListaCadenas(prueba, "pruebaLambda.txt", true);
        // Aqui se debe poner el nombre del archivo que se desea leer
        //afnl.hallarEstadosInaccesibles();// ejecutando esta función los estados inaccesibles quedan dentro del atributo (de la clase)InacessibleStates
        //System.out.println(afd.inaccessibleStates.get(0));
        //afd.computarTodosLosProcesamientos("aba", "ProbandoLambda");

//        afnl.computarTodosLosProcesamientos("a", "hola.txt");

        
        //test del afn_lambda to afd
//        afnl.procesarCadenaConversion("aaaaaaaaaaaaaa");
        //afnl.procesarCadenaConDetallesConversion("aaab");
        //afnl.procesarListaCadenasConversion(prueba, "probando23", true);
       
        //afnl.procesarCadenaConDetallesConversion("aabaa");
        
        /*

        Los metodos que se DEBEN USAR para obtener resultados son los siguientes : 
        afd.calcularLambdaClausura(estado);
        afd.calcularMuchasLambdaClausura(estados);
        afd.procesarCadena(cadena);
        afd.procesarCadenaConDetalles2(cadena);
        afd.computarTodosLosProcesamientos(cadena, nombreArchivo);
        afd.procesarListaCadenas(cadenas, nombreArchivo, true);
        Cualquier otro metodo con nombre similar no dara el resultado esperado.
        NOTAS : 
        -Todas las "listas" (denotadas como "cadenas") recibidas deben ser ArrayList
        -Todos los parametros "nombreArchivo" van sin la extensión .txt y en caso de ya existir se les dara un nombre generico que :
            en caso de ser en el metodo "procesarListaCadenas" sera : nombreArchivoi.txt en donde "nombreArchivo" es el nombre que se ingreso y  la "i" representa un numero entero disponible.
            en caso de ser en el metodo "computarTodosLosProcesamientos" seran : nombreArchivoiAceptadas.txt,nombreArchivoiRechazadas.txt,nombreArchivoiAbortadas.txt en donde "nombreArchivo" es el nombre que se ingreso y  la "i" representa un numero entero disponible.
       
       */
        
        
      
        
        
        
        
        
       
    }


}
