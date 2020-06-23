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
        return this.q;
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
        //System.out.println("Final states:");
        for (int i = 0; i < this.finalStates.size(); i++) {
            System.out.println(this.finalStates.get(i));
        }
    }

    public void showInitialState() {
        System.out.println("Initial state: ");
        System.out.println(this.q);
    }

    public void showLimboStates(){
        int i,j,k;
        System.out.println("Limbo states:");
        for(i=0;i<this.limboStates.size();i++){
            System.out.println(this.limboStates.get(i));
        }
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
        findLimboStates();
        showInitialState();
        showFinalStates();
        ShowInaccessibleStates();
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
    
    
    public ArrayList<String> obtenerEstadosAccesibles(AFD afdinput) {

        ArrayList<String> accesibles = new ArrayList<>();

        accesibles.add(afdinput.getQ());

        for (int j = 0; j < afdinput.getDelta().length; j++) {
            String estadoActual = afdinput.getStates().get(j);
            for (int k = 0; k < afdinput.getDelta()[j].length; k++) {
                if (afdinput.getDelta()[j][k].isEmpty()) {
                    continue;
                } else {
                    for (String transicion : afdinput.getDelta()[j][k]) {
                        if (!estadoActual.equals(transicion)) {
                            if(accesibles.contains(estadoActual)){
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
        return accesibles;
    }
    
    

    public ArrayList<String> getInaccessibleStates() {
        return inaccessibleStates;
    }
    
    
    public AFD modificarAutomata(AFD afdinput, ArrayList<String> accesibles, ArrayList<String> inaccesibles){
       
        for (int i = 0; i < afdinput.getStates().size(); i++) {
            if (!accesibles.contains(afdinput.getStates().get(i))) {
                inaccesibles.add(afdinput.getStates().get(i));
            }
        }
        
        for (int i = 0; i < afdinput.getStates().size(); i++) {
            if(inaccesibles.contains(afdinput.getStates().get(i))){
                for (int j = 0; j < afdinput.getSigma().size(); j++) {
                    afdinput.getDelta()[i][j] = null;
                }
            }
        }
       
        
        return afdinput;
    }
    
    public String[][] simplificarAFD(AFD afdinput){
        //Esta función hace La matriz Triangular que es la primera iteración del algoritmo 
        ArrayList<String> accesibles = obtenerEstadosAccesibles(afdinput);
        String[][] triangular = new String[accesibles.size()][accesibles.size()];
        
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                triangular[i][j] = "E";
            }
        }
        
        for (int i = 0; i < accesibles.size(); i++) {
            for (int j = i+1; j < accesibles.size(); j++) {
                String p = accesibles.get(j);
                boolean pAceptacion = afdinput.getFinalStates().contains(p);
                String q = accesibles.get(i);
                boolean qAceptacion = afdinput.getFinalStates().contains(q);
                if(!(pAceptacion == qAceptacion)){
                    triangular[i][j] = "1";
                }
            }
        }
        
        simplificacionAutomataAFD2(afdinput, triangular, accesibles);
        return triangular;
    }
    
    public void simplificacionAutomataAFD2(AFD afdinput, String[][]triangular, ArrayList<String> accesibles){
        //Esta función hace la tabla de la segunda iteración y actualiza la matriz triangular con la segunda iteración
        ArrayList<Integer> posiciones = triangularAEstados(afdinput, triangular, accesibles);
        ArrayList<Integer> copyPos = (ArrayList<Integer>) posiciones.clone();
        
        ArrayList<String>[][] tabular = new ArrayList[posiciones.size()/2][afdinput.getSigma().size()];
        
        int indice = 0;
        int k = 0;
        while(!copyPos.isEmpty()){
            int posEstadoP = copyPos.remove(0);
            int posEstadoQ = copyPos.remove(0);
            k = 0;
             for (int j = 0; j < afdinput.getSigma().size(); j++) {
                String vaP = afdinput.getDelta()[posEstadoP][j].get(0); //SE PUEDE MORIR o no :v
                String vaQ = afdinput.getDelta()[posEstadoQ][j].get(0);

                tabular[indice][k] = new ArrayList();
                tabular[indice][k].add(vaP);
                tabular[indice][k].add(vaQ);
                k++;
                if(k==2)
                    indice++;
            }
        }
                        
        for(int i = 0;i< tabular.length;i++){
            for(int j = 0;j< tabular[i].length;j++){
                if(!(tabular[i][j] == null)){
                    String p = tabular[i][j].get(0);
                    String q = tabular[i][j].get(1);
                    
                    boolean pAceptacion = afdinput.getFinalStates().contains(p);
                    boolean qAceptacion = afdinput.getFinalStates().contains(q);
                    
                    if(!(pAceptacion == qAceptacion)){
                        tabular[i][j].add("2");
                    }
                }                
            }
        }       
        
        copyPos = (ArrayList<Integer>) posiciones.clone();

            for (int i = 0; i < tabular.length; i++) {
                if(!copyPos.isEmpty()){
                    int posEstadoP = accesibles.indexOf(afdinput.getStates().get(copyPos.remove(0)));
                    int posEstadoQ = accesibles.indexOf(afdinput.getStates().get(copyPos.remove(0)));

                    for (int j = 0; j < tabular[i].length; j++) {
                        if (!(tabular[i][j] == null)) {
                            if (tabular[i][j].contains("2")) {
                                triangular[posEstadoQ][posEstadoP] = "2";
                            }
                        }
                    }
                }
            }
        
        
        
        /*for(int i = 0;i<tabular.length;i++){
            for(int j = 0;j<tabular[i].length;j++){
                if(!(tabular[i][j] == null)){
                    for (String string : tabular[i][j]) {
                    System.out.print(string + ",");
                }
                System.out.println("");
                }
            }
        }
        
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                System.out.println(triangular[i][j]);
            }
        }*/
        
        printTriangular(triangular, accesibles);
        printTabular(tabular,posiciones,accesibles,afdinput);
        
    }
    
    public ArrayList<Integer> triangularAEstados(AFD afdinput, String[][]triangular, ArrayList<String> accesibles){
        ArrayList<Integer> posiciones = new ArrayList<>();
        for(int i=0;i<triangular.length;i++){
            for(int j=i+1;j<triangular.length;j++){
                if(triangular[i][j].equals("E")){
                    int indexP = afdinput.getStates().indexOf(accesibles.get(j));
                    int indexQ = afdinput.getStates().indexOf(accesibles.get(i));
                    //System.out.println("index P = " + indexP + " index Q = " + indexQ);
                    posiciones.add(indexP); // posiciones en estados, mas no en accesibles
                    posiciones.add(indexQ);
                }
            }
        }
        
        return posiciones;
    }
    
    public void printTriangular(String[][] triangular, ArrayList<String> accesibles) {

        System.out.println("");// espaciado bonito :V
        for (int i = 0; i < accesibles.size(); i++) {
            for (int j = 0; j <= i; j++) {

                if (i == j) {
                    triangular[i][j] = accesibles.get(i);
                }
                    System.out.print(triangular[j][i]+"  ");


            }
            System.out.println("\n");
        }


    }

    ;
    
    public void printTabular(ArrayList<String>[][] tabular, ArrayList<Integer> posiciones, ArrayList<String> accesibles, AFD afd) {

        ArrayList<Integer> copyPos2 = (ArrayList<Integer>) posiciones.clone();
        System.out.println("\n");// espaciado bonito :V
        System.out.print("{p,q}    ");// 4 espacios entre todo horizontalmente;

        for (int i = 0; i < afd.getSigma().size(); i++) {

            System.out.print("{De(p," + afd.getSigma().get(i) + "),De(q," + afd.getSigma().get(i) + ")}    ");

        }
        System.out.println("\n");
        while (!copyPos2.isEmpty()) {
            for (int i = 0; i < tabular.length; i++) {

                ArrayList<String> ordenar = new ArrayList<>();
                ordenar.add(accesibles.get(accesibles.indexOf(afd.getStates().get(copyPos2.remove(0)))));
                ordenar.add(accesibles.get(accesibles.indexOf(afd.getStates().get(copyPos2.remove(0)))));;
                Collections.sort(ordenar);
                
                System.out.print("{" + ordenar.remove(0) + "," + ordenar.remove(0) + "}    ");

                for (int j = 0; j < tabular[i].length; j++) {

                    ordenar.add(tabular[i][j].get(0));
                    ordenar.add(tabular[i][j].get(1));
                    Collections.sort(ordenar);
                    
                    
                    System.out.print("{" + ordenar.remove(0) + "," + ordenar.remove(0) + "}");
                    if (tabular[i][j].contains("2")) {
                        System.out.print(" X2       ");
                    }else System.out.print("          ");

                }
                System.out.println("\n");
            }

        }
    }

    ;
    
    public static AFD hallarComplemento(AFD afdInput) {
        AFD complemento = new AFD();
        ArrayList<String> newfinalStates = new ArrayList<>();

        for (int i = 0; i < afdInput.getStates().size(); i++) {
            if (!afdInput.getFinalStates().contains(afdInput.getStates().get(i))) {
                newfinalStates.add(afdInput.getStates().get(i));
            }
        }

        complemento.initializeAFDwithData(afdInput.getSigma(), afdInput.getStates(), afdInput.getQ(), newfinalStates, afdInput.getDelta());
        return complemento;
    }
    
    
    
    
    

    public static void main(String[] args) throws Exception {

        AFD afd = new AFD();
        afd.initializeAFD("AFD3.txt");
        afd.simplificarAFD(afd);
        //afd.hallarEstadosInaccesibles(); // ejecutando esta función los estados inaccesibles quedan dentro del atributo (de la clase)InacessibleStates

        //El autómata que lee acepta cadenas con un numero par de de a Y b
        //afd.processStringWithDetails("abababa");
        
        
        //afd = AFNtoAFD(afn);
        AFD afdc = hallarComplemento(afd);
        
        
        //test del complemento
        System.out.println("\n complemento:");
        System.out.println(afdc.getFinalStates().get(3));
        
        

      
        /*afd.showSigma();
        afd.showStates();
        afd.showInitialState();
        afd.showFinalStates();
        afd.showDelta();*/

        
    }

}
