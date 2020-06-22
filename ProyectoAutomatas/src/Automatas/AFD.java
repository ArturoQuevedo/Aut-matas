package Automatas;

/**
 * @author Arthur
 */
import java.util.*;
import java.io.*;

public class AFD {

    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;
    private ArrayList<String> limboStates;
    private ArrayList<String> inaccessibleStates = new ArrayList<>();

    public AFD() {
        this.sigma = new ArrayList<>();
        this.states = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.limboStates = new ArrayList<>();
    }

    public ArrayList<Character> getSigma() {
        return sigma;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public String getQ() {
        return q;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public ArrayList<String>[][] getDelta() {
        return delta;
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

    public void showLimboStates(){
        int i,j,k;
        System.out.println("Limbo states:");
        for(i=0;i<this.limboStates.size();i++){
            System.out.println(this.limboStates);
        }
    }
    
    public void showAllTipeOfStates(){
        findLimboStates();
        showInitialState();
        showFinalStates();
        //aqui van los estados inalcanzables
        showLimboStates();
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
                        } else {
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
    
    public void initializeAFDwithData(ArrayList<Character> sigma, ArrayList<String> states, String q, ArrayList<String> finalStates, ArrayList<String>[][] delta){
        
        //se guarda el sigma producido
        //System.out.print("sigma: ");
        for (int i = 0; i < sigma.size(); i++) {
            this.sigma.add(sigma.get(i));
            //System.out.print(this.sigma.get(i) + " ");
        }
        //System.out.println("");
        
        //se guarda los estados producidos
        //System.out.print("states: ");
        for (int i = 0; i < states.size(); i++) {
            this.states.add(states.get(i));
            //System.out.print(this.states.get(i) + " ");
        }
        //System.out.println("");
        
        //se inicializa el delta
        this.initializeDelta(this.states.size(), this.sigma.size());
        
        //se guarda el q0
        //System.out.print("q: ");
        this.q = q;
        //System.out.println(this.q);
        
        //se guarda los estados finales
        //System.out.print("finalStates: ");
        for (int i = 0; i < finalStates.size(); i++) {
            this.finalStates.add(finalStates.get(i));
            //System.out.print(this.finalStates.get(i) + " ");
        }
        //System.out.println("");
        
        //se guarda la matriz delta
        //System.out.println("Delta: ");
        for (int i = 0; i < states.size(); i++) {
            for (int j = 0; j < sigma.size(); j++) {
                this.delta[i][j].add(delta[i][j].get(0));
                //System.out.print(this.delta[i][j].get(0) + " ");
            }
            //System.out.println("");
        }
        //System.out.println("");

    }

    public int getRow(String state) {
        //esta función es para obtener la fila en la que se encuentra un estado (se asume columna 0)
        for (int i = 0; i < this.states.size(); i++) {
            //solo nos interesa la los elementos de la primera columna entonces por eso la fijamos en [j][0]
            if (state.equals(this.states.get(i))) {
                return i;
            }

        }
        return -1; // esto nunca deberia pasar a no se que pas eun error de digitación
    }

    public int getColumn(String symbol) {
        //esta función es para obtener la columna en la que se encuentra un simbolo (se asume fila 0 )
        for (int i = 0; i < this.sigma.size(); i++) {
            //solo nos interesa la los elementos de la primera fila entonces por eso la fijamos en [0][i]
            if (symbol.equals(Character.toString(this.sigma.get(i)))) {
                return i;
            }

        }
        return -1; // esto nunca deberia pasar a no se que pase un error de digitación
    }
    
    public void findLimboStates(){
        int i,j,k;
        int count;
        boolean isLimbo = false;
        for(i=0;i<this.states.size();i++){
            count = 0;
            isLimbo = false;
            for (j = 0; j < this.sigma.size(); j++) {
                if(this.delta[i][j].isEmpty()){
                    count++;
                }
               
            }
            if(count==this.sigma.size()){
                    isLimbo = true;
                for(j=0;j<this.finalStates.size();j++){
                    if(this.states.get(i).equals(this.finalStates.get(j))){
                         isLimbo = false;
                    }
                }
                if(isLimbo){
                    this.limboStates.add(this.states.get(i));
                }
            }
            
        }
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
    
    
    

    public static void main(String[] args) throws Exception {

        AFD afd = new AFD();
        afd.initializeAFD("AFNtest4.txt");
        afd.hallarEstadosInaccesibles(); // ejecutando esta función los estados inaccesibles quedan dentro del atributo (de la clase)InacessibleStates
        System.out.println(afd.getInaccessibleStates().get(0)); 
        //El autómata que lee acepta cadenas con un numero par de de a Y b
        //afd.processStringWithDetails("abababa");

      
        /*afd.showSigma();
        afd.showStates();
        afd.showInitialState();
        afd.showFinalStates();
        afd.showDelta();*/

        
    }

}
