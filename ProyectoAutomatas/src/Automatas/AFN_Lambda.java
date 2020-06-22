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
        public ArrayList<String> aceptada ;
        public ArrayList<String> rechazada;
        public ArrayList<String> abortada;
        private ArrayList<String> inaccessibleStates = new ArrayList<>();
        
        
         

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

                System.out.println("letra: "+letraActual);
                simbolo = Character.toString(cadena.charAt(letraActual));

                posicionEstado = getPosEstado(estadoActual);
                posicionSimbolo = getPosSimbolo(simbolo);
                System.out.println("Estado: "+estadoActual+":"+simbolo);
                
                String temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
                
                this.globalito.get(indice).add(temp);
                System.out.println(temp);
                if(!this.delta[posicionEstado][posicionLambda].isEmpty()){
                    if(!loop){
                        for(i = 0;i<this.delta[posicionEstado][posicionLambda].size();i++){
                            System.out.println(this.delta[posicionEstado][posicionLambda]);
                            if(letraActual< cadena.length()){
                                estadoActual = this.delta[posicionEstado][posicionLambda].get(i);
                                System.out.println("Estado: "+estadoActual+">"+lambda);
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
                        temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
                        System.out.println(temp);
                        this.globalito.get(indice).add(temp);
                        this.globalito.get(indice).add("Abortado");
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
                            aceptada = computarTodosLosProcesamientos(cadena, estadoActual, letraActual, false, (aceptada), salida, indice, estadoAnterior) || aceptada;                            
                        }else{
                            this.globalito.get(indice).add("No aceptacion");
                            indice++;
                            System.out.println("Cadena rechazada");
                            aceptada = false;
                            return aceptada;
                        }
                    }
                }else{
                    temp = String.format("[%s, %s]", estadoActual, cadena.substring(letraActual, cadena.length()));
                    System.out.println(temp);
                    this.globalito.get(indice).add(temp);
                    this.globalito.get(indice).add("Abortado");
                    System.out.println("Procesamiento abortado");
                    aceptada = aceptada || false;
                    return aceptada;
                }

                if(!aceptada){
                    aceptada = (verifyComputarTodosLosProcesamientos(letraActual, cadena, estadoActual, i, aceptada, indice)) || aceptada;
                }
            }
            return aceptada;
        }
        
        public int computarTodosLosProcesamientos(String cadena,String nombreArchivo) throws IOException{
            String salida = "";
            int indice = 0;
            boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
            this.filtro();
            int total = 0;
            for(int i=0;i<globalito.size();i++){
                if(!globalito.get(i).isEmpty()) total++;
                else break;
            }
            
        //Cadenas Aceptadas    
        int i = 1; // todas van a manejar el mismo numero al final
        String ruta = nombreArchivo + "Aceptadas.txt" ;
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

                ruta = nombreArchivo + "Aceptadas"+i+".txt"; 
                ruta2 = nombreArchivo + "Rechazadas"+i+".txt"; 
                ruta3 = nombreArchivo + "Abortadas"+i+".txt"; 
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
        int j = 0;
        bw.write(cadena+"\n");
        System.out.println(cadena);
        if(aceptada.isEmpty()){
            System.out.println("No hay procesamientos aceptados");
        }else{
        while(true){
            
            if(j == aceptada.size()){
                System.out.println("");
                break;
            }
            
            if(aceptada.get(j).equals("Aceptacion")){
                bw.write(aceptada.get(j)+"\n");
                System.out.print(aceptada.get(j));
                System.out.println("");
                j++;
            }else{
                
                 bw.write(aceptada.get(j)+"->");
                 System.out.print(aceptada.get(j)+"->");
                 j++;
                
            }
            
            
   
        }
        }
         
        
        //escribiendo en el documento de Rechazadas con el formato adecuado  
        j = 0; 
        bw2.write(cadena+"\n");
        if(rechazada.isEmpty()){
            System.out.println("No hay procesamientos rechazados");
        }else{
        while(true){
            
            if(j == rechazada.size()){
                System.out.println("");
                break;
            }
            
            if(rechazada.get(j).equals("No aceptacion")){
                bw2.write(rechazada.get(j)+"\n");
                System.out.print(rechazada.get(j));
                System.out.println("");
                j++;
            }else{
                
                 bw2.write(rechazada.get(j)+"->");
                 System.out.print(rechazada.get(j)+"->");
                 j++;
                
            }
            
            
            
   
        }
        }
         
        //escribiendo en el documento de Abortadas con el formato adecuado  
        j = 0; 
        bw3.write(cadena+"\n");
        if(abortada.isEmpty()){
            System.out.println("No hay procesamientos abortados");
        }else{
        while(true){
            
            if(j == abortada.size()){
                System.out.println("");
                break;
            }
            
            if(abortada.get(j).equals("Abortado")){
                bw3.write(abortada.get(j)+"\n");
                System.out.print(abortada.get(j));
                System.out.println("");
                j++;
            }else{
                
                 bw3.write(abortada.get(j)+"->");
                 System.out.print(abortada.get(j)+"->");
                 j++;
                
            }
            
            
   
        }
        }
            bw.close();
            bw2.close();
            bw3.close();
            
           
            
            
            System.out.println("Numero de procesamientos realizados : "+ total);
            return total;
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
        
        public void obtenerLista(ArrayList<String> lista, int inicio, int ultimo, ArrayList<String> objetivo){
            for(int i=0;i<lista.size();i++){
                if(i >= inicio && i<= ultimo){
                    objetivo.add(lista.get(i));
                }
                if(i>ultimo) break;
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
                                obtenerLista(globalito.get(i), proceso, j, abortada);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, abortada);
                            proceso = j;
                            break;
                        case 1:
                            if(proceso==0)
                                obtenerLista(globalito.get(i), proceso, j, rechazada);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, rechazada);
                            proceso = j;
                            break;
                        case 2:
                            if(proceso==0)
                                obtenerLista(globalito.get(i), proceso, j, aceptada);
                            else
                                obtenerLista(globalito.get(i), proceso+1, j, aceptada);
                            proceso = j;
                            break;
                        default:
                            break;
                    }
                }
            }
            //System.out.println("recorri todo.");
        }
        
        public void procesarListaCadenas(ArrayList<String> cadenas, String nombreArchivo, boolean imprimirPantalla ) throws IOException{
           
            
            //Verificar si "nombreArchivo.txt" ya existe
            int i = 1;
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
            
            while(!cadenas.isEmpty()){
                String cadena = cadenas.remove(0);
                String salida = "";
                int indice = 0;
                boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
                this.filtro();
                int total = 0;
                for(i=0;i<globalito.size();i++){
                    if(!globalito.get(i).isEmpty()) total++;
                    else break;
                }
           
                     
                    if(!aceptada.isEmpty()){
                        
                        ArrayList<String> a = new ArrayList<>();
                        ArrayList<String> b = new ArrayList<>();
                        a = aceptacionMasCorto(); 
                        b = noAceptacionMasCorto();

                        int pAceptados = Integer.valueOf(a.get(0));
                        int pNoAceptados = Integer.valueOf(b.get(0));
                        if(imprimirPantalla) System.out.println(cadena);
                        bw.write("\n"+cadena + "\n");

                        for(i = 1; i < (a.size()-1);i++ ){
                            
                            if(imprimirPantalla)System.out.print(a.get(i)+"->");
                            bw.write(a.get(i)+"->");
                            
                        }
                        if(imprimirPantalla)System.out.print(a.get(a.size()-1)+ "\n");
                        bw.write(a.get(a.size()-1)+ "\n");
                        if(imprimirPantalla)System.out.println("En total hay " + total + " procesamientos posibles.");
                        bw.write("\n"+"En total hay " + total + " procesamientos posibles.");
                        if(imprimirPantalla)System.out.println("En total hay " + pAceptados + " procesamientos aceptados.");
                        bw.write("\n"+"En total hay " + pAceptados + " procesamientos aceptados.");
                        if(imprimirPantalla)System.out.println("En total hay " + pNoAceptados + " procesamientos rechazados.");
                        bw.write("\n"+"En total hay " + pNoAceptados + " procesamientos rechazados.");
                        if(imprimirPantalla)System.out.println("En total hay " + (total-(pAceptados+pNoAceptados)) + " procesamientos abortados.");
                        bw.write("\n"+"En total hay " + (total-(pAceptados+pNoAceptados)) + " procesamientos abortados.");
                        if(imprimirPantalla)System.out.println("Si");
                        bw.write("\n"+"Si");
                        
                        
                    }else if(!rechazada.isEmpty()){
                        
                        ArrayList<String> b = new ArrayList<>();
                        b = noAceptacionMasCorto();
                        int pAceptados = 0;
                        int pNoAceptados = Integer.valueOf(b.get(0));
                        if(imprimirPantalla) System.out.println(cadena);
                        bw.write("\n"+cadena + "\n");
                        for(i = 1; i < b.size();i++ ){
                            
                            if(imprimirPantalla)System.out.print(b.get(i)+"->");
                            bw.write(b.get(i)+"->");
                            
                        }
                        if(imprimirPantalla)System.out.println("En total hay " + total + " procesamientos posibles.");
                        bw.write("\n"+"En total hay " + total + " procesamientos posibles.");
                        if(imprimirPantalla)System.out.println("En total hay " + total + " procesamientos posibles.");
                        bw.write("\n"+"En total hay " + total + " procesamientos posibles.");
                        if(imprimirPantalla)System.out.println("En total hay " + pAceptados + " procesamientos aceptados.");
                        bw.write("\n"+"En total hay " + pAceptados + " procesamientos aceptados.");
                        if(imprimirPantalla)System.out.println("En total hay " + pNoAceptados + " procesamientos rechazados.");
                        bw.write("\n"+"En total hay " + pNoAceptados + " procesamientos rechazados.");
                        if(imprimirPantalla)System.out.println("En total hay " + (total-(pAceptados+pNoAceptados)) + " procesamientos abortados.");
                        bw.write("\n"+"En total hay " + (total-(pAceptados+pNoAceptados)) + " procesamientos abortados.");
                        if(imprimirPantalla)System.out.println("No");
                        bw.write("\n"+"No");
                        
                    }else System.out.println("Esto no es un atomata :c"); // esto es cuando no tiene ninguna aceptada ni rechazada
                    
                    //Limpiando todo para sel siguiente ciclo
                globalito.clear();
                aceptada.clear();
                rechazada.clear();
                abortada.clear();
                for(int o=0;o<250;o++){
                    ArrayList<String> a = new ArrayList<>();
                    globalito.add(a);
                }
                    
                    
            }
            bw.close();
        }
        
        
        
        
        public ArrayList<String> aceptacionMasCorto(){
        
            ArrayList<String> corto = new ArrayList<>();
            int in = 0;  //el indice mas corto hasta hora
            int fin = 0; // el indice mas largo hasta hora
            int temin = 0; // lo mismo de arriba pero temporal para ir comparando
            int temfin = 0; // same
            int longitud = 0; //la resta de fin con in 
            int numero = 0;//numero de procesamientos
            int j  = 0; //contador
            if(aceptada.isEmpty()){
                corto.add(0,Integer.toString(numero));
                return corto;
                
            }else{
                
                while(true){
                    
                    if(j == aceptada.size()){
                    break;
            }
            
            if(aceptada.get(j).equals("Aceptacion")){
                numero++;
                temfin = j;
                if((temfin-temin) > longitud){
                    in = temin;
                    fin = temfin;
                    longitud = (temfin-temin);
                }
                temin = j+1;
                j++;
            }else j++;
                    
                }
                for(int k = 0;k <= longitud;k++){
                    corto.add(aceptada.get(in));
                    in++;
                }

            }
            corto.add(0,Integer.toString(numero));
            return corto;
            
        }
        public ArrayList<String> noAceptacionMasCorto(){
        
            ArrayList<String> corto = new ArrayList<>();
            int in = 0;  //el indice mas corto hasta hora
            int fin = 0; // el indice mas largo hasta hora
            int temin = 0; // lo mismo de arriba pero temporal para ir comparando
            int temfin = 0; // same
            int longitud = 0; //la resta de fin con in
            int numero = 0;//numero de procesamientos
            int j  = 0; //contador
            
            if(rechazada.isEmpty()){
                corto.add(0,Integer.toString(numero));
                return corto;
                
            }else{
                
                while(true){
                    
                    if(j == rechazada.size()){
                    break;
            }
            
            if(rechazada.get(j).equals("No aceptacion")){
                numero++;
                temfin = j;
                if((temfin-temin) > longitud){
                    in = temin;
                    fin = temfin;
                    longitud = (temfin-temin);
                }
                temin = j+1;
                j++;
            }else j++;
                    
                }
                
                for(int k = 0;k <= longitud;k++){
                    corto.add(rechazada.get(in));
                    in++;
                }
                
            }
            corto.add(0,Integer.toString(numero));
            return corto;
            
        }
        
        
        public boolean procesarCadenaConDetalles2(String cadena){
            
            

                String salida = "";
                int indice = 0;
                boolean resultado = computarTodosLosProcesamientos(cadena, this.q, 0, false, true, salida, indice, this.q);
                this.filtro();      
                    if(!aceptada.isEmpty()){
                        
                        ArrayList<String> a = new ArrayList<>();
                        a = aceptacionMasCorto(); 
                        System.out.println(cadena);
                        for(int i = 1; i < (a.size()-1);i++ ){ 
                            System.out.print(a.get(i)+"->");  
                        }
                        System.out.print(a.get(a.size()-1)+"\n");
                        return true;
                        
                        
            
            
  
                    }else {
                        System.out.println("No hay procesamientos aceptados");
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
        
        afnl.initializeAFD("AFN_Lambda2.txt");
        
        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("");
        prueba.add("aaaaa");
        prueba.add("bbbbbb");
        prueba.add("");
        prueba.add("aaaaaaaaaaaab");
        // Aqui se debe poner el nombre del archivo que se desea leer
        //afnl.hallarEstadosInaccesibles();// ejecutando esta función los estados inaccesibles quedan dentro del atributo (de la clase)InacessibleStates
        //System.out.println(afd.inaccessibleStates.get(0));
        //afd.computarTodosLosProcesamientos("aba", "ProbandoLambda");

  

        
        //test del afn_lambda to afd
        //afnl.procesarCadenaConversion("aaaaaaaaaaaaaa");
        //afnl.procesarCadenaConDetallesConversion("aaab");
        afnl.procesarListaCadenasConversion(prueba, "probando23", true);
       
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
