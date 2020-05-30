
package Automatas;

import java.util.ArrayList;
import java.io.*;
import java.util.StringTokenizer;
 
public class AFN {
        public static class Automata {

        private ArrayList<Character> sigma;
        private ArrayList<String> states;
        private String q;
        private ArrayList<String> finalStates;
        private ArrayList<String>[][] delta;
        public boolean finale = false; 

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

        public void initializeAFN() throws FileNotFoundException, IOException {

            File file = new File("C:\\Users\\User\\Documents\\GitHub\\Aut-matas\\ProyectoAutomatas\\src\\Automatas\\file.txt");//aqui cambienlo por la ruta en donde esta el archivo para leer
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
                            else{
                                char ch = line.charAt(0);
                                this.sigma.add(ch);
                            }
                        }

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
        
        public  int getRow(String state){
            //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
            for(int i = 0; i < this.states.size(); i++ ){
                //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
                if(state.equals(this.states.get(i))){    
                    return i;
                }
                
            }
            return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
        }
    
        public  int getColumn(String symbol){
            //esta función es para obtener la columna en la que se encuentra un simbolo (se asume fila 0 )
            for(int i = 0; i < this.sigma.size(); i++ ){
                //solo nos interesa la los elementos de la primera fila entonces por eso la fijamos en [0][i]
                if(symbol.equals(Character.toString(this.sigma.get(i)))){
                    return i;
                }
                
            }
            return -1; // esto nunca deberia pasar a no se que pase un error de digitación
        }
        
        public boolean printTheResult(String estado,int letra){
        int i;
        if(letra >= estado.length()){
            for(i = 0; i < this.finalStates.size(); i++){
                
                if(estado.equals(this.finalStates.get(i))){
                    
                    System.out.println(">>>>>>Cadena Aceptada");
                    return true;
                }
                
       }
       
       //si supera el for
       System.out.println(">>>>>>Cadena No Aceptada");
       return false;
        }
        return false;
    }
       
    public  boolean processStringAFN(String cadena,String estado,int letra){
       int i;
       //estado Actual
       String state;
       //pocision del estado actual
       int posState;
       //simbolo actual
       String symbol;
       //posicion del simbolo actual            
       int posSymbol;
       //letra
       int posChar = letra;
       //asignamos el estado Actual
       state = estado;
       
       //empezamos el proceso
           
       posState = getRow(state);
       symbol = Character.toString(cadena.charAt(posChar));
       
       posSymbol=getColumn(symbol);
       System.out.println("["+state+","+symbol+"]->");

       if(!this.delta[posState][posSymbol].isEmpty()){
        posChar++;
        for(i=0;i<this.delta[posState][posSymbol].size();i++){  
            state = this.delta[posState][posSymbol].get(i);
            //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
            if(printTheResult(state,posChar)){
                finale = true; 
            }
        
            if(posChar<cadena.length()){
               //System.out.println("estado :"+state+">"+symbol);..
              processStringAFN(cadena,state,posChar);
            }
            //Intento para solucionar el error de la cadena "*"
            //else if(posChar==1&&!cicle){
            //  processStringAFN(cadena,this.q,0,true);
            //}
            
        }
        
       }else{
           System.out.println(">>>>>>procesamiento abortado");
         return false;
       }
       
       //verificando si esta en estado de aceptacion
       
       
       return finale;
    }
    
    public  boolean processString(String string){
        boolean bool = processStringAFN(string,this.q,0);
        if(bool){
            System.out.println("----------------------------------------Cadena aceptada----------------------------------------");
            return bool;
        }
        System.out.println("----------------------------------------Cadena NO aceptada----------------------------------------");
        return bool;
    }
        
       /* public boolean processStringWithDetails(String string){
            String actualState;// este es el estado actual
            int actualStateP;//la posición del estado dentro de la matriz (esta siempre en algun lugar de la primera columna)
            String actualSymbol; //Aqui podria ser un char pero prefiero los strings <3
            int actualSymbolP; //la posición del simbolo dentro de la matriz (esta siempre en algun lugar de la primera fila)
            String accumulate; //String acumulada que ha sido ya procesada

            actualState=q;
            //Ahora empezamos el proceso de la cadena
            while(!string.isEmpty()&&!string.contains("$")){
                
            actualStateP = getRow(actualState);
            accumulate = "[" + actualState + "," + string + "]->";  
            actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.
              
                if(string.length()>1){
                    string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                }else{
                    string = "";
                }
            
            actualSymbolP = getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)
            
                for (int i = 0; i < this.delta[actualStateP][actualSymbolP].get(0).length(); i++) {
                    if(this.processSubStringWithDetails(string, actualState, accumulate)){
                        System.out.println("Cadena Aceptada");
                        return true; // el estado es de aceptación         
                    }
                }
            }

            //si llega hasta aqui entonces el estado no es de aceptación
            System.out.println("Cadena No Aceptada");
            return false;
        
        }*/
        
        public void processStringList(ArrayList<String> stringList, boolean imprimirPantalla) throws IOException{
            
            if(imprimirPantalla){ //Creo que toca imprimir hasta lo de los archivos pero no estoy seguro
            
            int i = 1;
            String ruta = "C:\\Users\\User\\Documents\\GitHub\\Aut-matas\\ProyectoAutomatas\\src\\Automatas\\nombreArchivo.txt";//aqui cambienlo por la ruta en donde quieran que se cree el archivo (lo ultimo es le nombre)
            File archivo = new File(ruta);
            BufferedWriter bw;
            System.out.println("Comprobando si nombreArchivo.txt esta creado\n");
            while(true){
                
                if(archivo.exists()){
                    ruta = "C:\\Users\\User\\Documents\\GitHub\\Aut-matas\\ProyectoAutomatas\\src\\Automatas\\nombreArchivo" + i + ".txt"; //aqui lo mismo
                    System.out.println("El archivo ya esta creado, cambiando nombre\n");
                    System.out.println("Comprobando si nombreArchivo"+ i +".txt esta creado\n");
                    archivo = new File(ruta);
                    i++;
                }else{
                    bw = new BufferedWriter(new FileWriter(archivo));
                    System.out.println("Archivo creado con exito\n");
                    break;
                }
            }
            
            
                
                while(!stringList.isEmpty()){
                    
                    int j = 0;//esta "j" se queda siempre en cero ya que el array list ira moviendo todo hacia la izqueirda hasta estar vacio
                    String string = stringList.remove(j);
                    bw.write(string +"     ,");
                    String actualState;// este es el estado actual
                    int actualStateP;//la posición del estado dentro de la matriz (esta siempre en algun lugar de la primera columna)
                    String actualSymbol; //Aqui podria ser un char pero prefiero los strings <3
                    int actualSymbolP; //la posición del simbolo dentro de la matriz (esta siempre en algun lugar de la primera fila)
  
                    actualState=q;
                    //Ahora empezamos el proceso de la cadena
                    while(!string.isEmpty()&&!string.contains("$")){ 
                        System.out.println("Estado actual : " + actualState + "\n");
                        actualStateP = getRow(actualState);    
                        actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.
                
                        if(string.length()>1){
                            string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                        }else{
                            string = "";
                        }
                    System.out.println("Simbolo a procesar : " + actualSymbol + ". Cadena restante : " + string + "\n");
                    bw.write("[ "+actualState +","+actualSymbol+" ],");
                    actualSymbolP = getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)
            
                        if(!this.delta[actualStateP][actualSymbolP].get(0).isEmpty()){
                
                            actualState = this.delta[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición
                
                        }else{
                    
                        //aqui abortariamos ?
                    
                        }
            
                    }
            
                    for(int k = 0; k < this.finalStates.size(); k++){
                
                        if(actualState.equals(this.finalStates.get(k))){
                            System.out.println("Estado actual : " + actualState + "\n");
                            System.out.println("La cadena fue aceptada\n");
                            bw.write("    Si\n");
                            break;
                        } 
                        if(k == this.finalStates.size()-1){
                            System.out.println("Estado actual : " + actualState + "\n");
                            System.out.println("La cadena no fue aceptada\n");//si esto ocurre entonces el ciclo esta apunto de terminar y no encontro ningun estado coincidente
                            bw.write("    No\n");
                            break;
                        }
                
                    }
            
            
                
                
                
                }
                
            bw.close();
                
            }else{
              
                int i = 1;
                String ruta = "C:\\Users\\User\\Documents\\GitHub\\Aut-matas\\ProyectoAutomatas\\src\\Automatas\\nombreArchivo.txt";//aqui cambienlo por la ruta en donde quieran que se cree el archivo (lo ultimo es le nombre)
                File archivo = new File(ruta);
                BufferedWriter bw;
                while(true){
                
                    if(archivo.exists()){
                        ruta = "C:\\Users\\User\\Documents\\GitHub\\Aut-matas\\ProyectoAutomatas\\src\\Automatas\\nombreArchivo" + i + ".txt"; ; //aqui lo mismo
                        archivo = new File(ruta);
                        i++;
                    }else{
                        bw = new BufferedWriter(new FileWriter(archivo));
                        break;
                    }
                }
            
            
                
                    while(!stringList.isEmpty()){
                    
                        int j = 0;//esta "j" se queda siempre en cero ya que el array list ira moviendo todo hacia la izqueirda hasta estar vacio
                        String string = stringList.remove(j);
                        bw.write(string +"     ,");
                        String actualState;// este es el estado actual
                        int actualStateP;//la posición del estado dentro de la matriz (esta siempre en algun lugar de la primera columna)
                        String actualSymbol; //Aqui podria ser un char pero prefiero los strings <3
                        int actualSymbolP; //la posición del simbolo dentro de la matriz (esta siempre en algun lugar de la primera fila)
  
                        actualState=q;
                        //Ahora empezamos el proceso de la cadena
                        while(!string.isEmpty()&&!string.contains("$")){               
                            actualStateP = getRow(actualState);    
                            actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.
                
                            if(string.length()>1){
                                string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                            }else{
                                string = "";
                            }
                        bw.write("[ "+actualState +","+actualSymbol+" ],");
                        actualSymbolP = getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)
            
                            if(!this.delta[actualStateP][actualSymbolP].get(0).isEmpty()){
                
                                actualState = this.delta[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición
                
                            }else{
                    
                            //aqui abortariamos ?
                    
                            }
            
                        }
            
                        for(int k = 0; k < this.finalStates.size(); k++){
                
                            if(actualState.equals(this.finalStates.get(k))){
                                bw.write("    Si\n");
                                break;
                            } 
                            if(k == this.finalStates.size()-1){
                                bw.write("    No\n");//si esto ocurre entonces el ciclo esta apunto de terminar y no encontro ningun estado coincidente
                                break;
                            }
                
                        }
            
            
                
                
                
                    }
                
                bw.close();
                
                
                }
            
            
            
            
            }
        
        
    }

    public static void main(String[] args) throws Exception {

        Automata afd = new Automata();
        afd.initializeAFN(); // esto deberia tirarnos error a los que no tenemos el txt de Arturo
        afd.showSigma();
        afd.showStates();
        afd.showInitialState();
        afd.showFinalStates();
        afd.showDelta();
        //por el momento todo lo que esta arriba se tiene que ejecutar para llenar el automata con todo, si lo desactivan ps se muere
        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aaaaaaab");
        prueba.add("$");
        prueba.add("");
        afd.processString("aa");
        //afd.processStringWithDetails("aab");
        //afd.processStringList(prueba, false);
        //afd.processStringList(prueba,true);
        //afd.processStringList(prueba,true);
        

    }

}






