package Automatas;

/**
 * @author Arthur
 */
import java.util.*;
import java.io.*;

public class AFD {

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
            //para que esto funcione, "file.txt debe estar en la carpeta raiz, en la misma carpeta que src y nbproject"
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

        public boolean processString(String string, boolean imprimir) { //Procesar string con o sin detalles

            if (imprimir == true) {
                System.out.print(string + "\t");
            }
            String actualState;// este es el estado actual
            int actualStateP;//fila del estado actual
            String actualSymbol; //char a leer
            int actualSymbolP; //columna del char a leer

            actualState = this.q;
            //Ahora empezamos el proceso de la cadena
            while (!string.isEmpty() && !string.contains("$")) {
                actualStateP = getRow(actualState);
                actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.

                if (string.length() > 1) {
                    string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                } else {
                    string = "";
                }
                if (imprimir == true) {
                    System.out.print("[" + actualState + "," + actualSymbol + "] ");
                }

                actualSymbolP = getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)

                if (!this.delta[actualStateP][actualSymbolP].get(0).isEmpty()) {

                    actualState = this.delta[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición
                } else {
                    System.out.print("Usted no ingresó los estados limbo");
                    break;
                }
            }
            if (imprimir == true) {
                System.out.print("[" + actualState + "]" + "\t");
            }

            for (int k = 0; k < this.finalStates.size(); k++) {
                if (actualState.equals(this.finalStates.get(k))) {
                    System.out.print("Aceptada\n");
                    return true;
                }
            }
            System.out.print("No aceptada\n");
            return false;

        }

        public boolean processString(String string) {//Procesar string SIN detalles

            return processString(string, false);
        }

        public boolean processStringWithDetails(String string) {//Procesar string CON detalles

            return processString(string, true);
        }

        public void processStringList(ArrayList<String> stringList, boolean imprimirPantalla) throws IOException {

            //Verificar si "nombreArchivo.txt" ya existe
            int i = 1;
            String ruta = "nombreArchivo.txt";//aqui cambienlo por la ruta en donde quieran que se cree el archivo (lo ultimo es le nombre)
            File archivo = new File(ruta);
            BufferedWriter bw;
            while (true) {
                if (archivo.exists()) {
                    ruta = "nombreArchivo" + i + ".txt";; //aqui lo mismo
                    archivo = new File(ruta);
                    i++;
                } else {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    break;
                }
            }

            //Procesar la lista de cadenas
            while (!stringList.isEmpty()) {

                //Sacar de la lista el primer string a procesar
                String string = stringList.remove(0);

                if (imprimirPantalla) {
                    System.out.print(string + "\t");
                }
                bw.write(string + "\t");

                String actualState;// este es el estado actual
                int actualStateP;//fila del estado actual
                String actualSymbol; //char a leer
                int actualSymbolP; //columna del char a leer

                actualState = this.q;
                while (!string.isEmpty() && !string.contains("$")) {

                    actualStateP = getRow(actualState);
                    actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda

                    //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                    if (string.length() > 1) {
                        string = string.substring(1);
                    } else {
                        string = "";
                    }

                    if (imprimirPantalla) {
                        System.out.print("[" + actualState + "," + actualSymbol + "] ");
                    }

                    bw.write("[" + actualState + "," + actualSymbol + "] ");
                    actualSymbolP = getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)

                    if (!this.delta[actualStateP][actualSymbolP].get(0).isEmpty()) {

                        actualState = this.delta[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición

                    } else {
                        System.out.println("Usted no ingresó el autómata completo");
                        break;
                    }
                }

                if (imprimirPantalla) {
                    System.out.print("[" + actualState + "]" + "\t");
                }
                bw.write("[" + actualState + "]" + "\t");

                for (int k = 0; k < this.finalStates.size(); k++) {

                    if (actualState.equals(this.finalStates.get(k))) {
                        if (imprimirPantalla) {
                            System.out.print("Sí\n");
                        }
                        bw.write("Sí\n");
                        break;
                    }
                    if (k == this.finalStates.size() - 1) {//si esto ocurre entonces NO encontro ningun estado coincidente
                        if (imprimirPantalla) {
                            System.out.print("No\n");
                        }
                        bw.write("No\n");
                        break;
                    }
                }
            }
            bw.close();

        }
    }

    public static void main(String[] args) throws Exception {

        Automata afd = new Automata();
        afd.initializeAFD();
        //El autómata que lee acepta cadenas con un numero par de de a Y b
        
        //afd.processStringWithDetails("abababa");

        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aba");
        prueba.add("baba");
        prueba.add("abba");
        prueba.add("");
        prueba.add("b");
        afd.processStringList(prueba, true);
    }

}
