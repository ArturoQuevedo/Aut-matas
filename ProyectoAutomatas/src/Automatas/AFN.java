package Automatas;

import static Automatas.Automatas.*;
import ProcesadoresDeCadenas.PCAFD;
import java.util.ArrayList;
import java.io.*;
import java.util.Collections;
import java.util.StringTokenizer;
import Automatas.Automatas;

public class AFN {

    private ArrayList<Character> sigma;
    private ArrayList<String> states;
    private ArrayList<String> endsStates;
    private String q;
    private ArrayList<String> finalStates;
    private ArrayList<String>[][] delta;
    public static boolean finale = false;
    public ArrayList<String> Aceptadas;
    public ArrayList<String> Rechazadas;
    public ArrayList<String> Abortadas;
    private ArrayList<String> inaccessibleStates = new ArrayList<>();

    public AFN() {
        this.endsStates = new ArrayList<>();
        this.sigma = new ArrayList<>();
        this.states = new ArrayList<>();
        this.finalStates = new ArrayList<>();
        this.Aceptadas = new ArrayList<>();
        this.Rechazadas = new ArrayList<>();
        this.Abortadas = new ArrayList<>();
    }

    public ArrayList<Character> getSigma() {
        return sigma;
    }

    public void setSigma(ArrayList<Character> sigma) {
        this.sigma = sigma;
    }

    public ArrayList<String> getStates() {
        return states;
    }

    public void setStates(ArrayList<String> states) {
        this.states = states;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public ArrayList<String> getFinalStates() {
        return finalStates;
    }

    public void setFinalStates(ArrayList<String> finalStates) {
        this.finalStates = finalStates;
    }

    public ArrayList<String>[][] getDelta() {
        return delta;
    }

    public void setDelta(ArrayList<String>[][] delta) {
        this.delta = delta;
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

    public void ShowInaccessibleStates() {
        int i, j, k;
        System.out.println("Inaccesible States:");
        for(i=0;i<this.inaccessibleStates.size();i++){
            System.out.println(this.inaccessibleStates.get(i));
        }
    }

    public void showAllTipeOfStates() {
        hallarEstadosInaccesibles();
        showSigma();
        showStates();
        showInitialState();
        ShowInaccessibleStates();
        showFinalStates();
        showDelta();
    }

    public void initializeAFN(String route) throws FileNotFoundException, IOException {

        File file = new File(route);//aqui cambienlo por la ruta en donde esta el archivo para leer
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

    public boolean getTheResult(String estado, int letra, String cadena) {
        int i;
        if (letra >= cadena.length()) {
            for (i = 0; i < this.finalStates.size(); i++) {
                if (estado.equals(this.finalStates.get(i))) {
                    return true;
                }
            }
            //si supera el for
            return false;
        }
        return false;
    }

    public boolean printTheResult(String estado, int letra, String cadena) {
        int i;
        if (letra >= cadena.length()) {
            for (i = 0; i < this.finalStates.size(); i++) {
                if (estado.equals(this.finalStates.get(i))) {
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

    public boolean registerTheResult(String estado, int letra, String acc,String cadena) {
        int i;
        if (letra >= cadena.length()) {
            for (i = 0; i < this.finalStates.size(); i++) {

                if (estado.equals(this.finalStates.get(i))) {

                    Aceptadas.add(acc + "Aceptada");
                    return true;
                }

            }

            //si supera el for
            Rechazadas.add(acc + "Rechazada");
            return false;
        }
        return false;
    }

    public boolean registerListResult(String estado, int letra, String acc,String cadena) {
        int i;
        if (letra >= cadena.length()) {
            for (i = 0; i < this.finalStates.size(); i++) {

                if (estado.equals(this.finalStates.get(i))) {
                    Aceptadas.add(acc + "Cadena aceptada");
                    return true;
                }

            }

            //si supera el for
            Rechazadas.add(acc + "Cadena No Aceptada");
            return false;
        }
        return false;
    }

    public int computeStringAFN(String cadena, String estado, int letra, String accumulated) {
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
        //string con los detalles del procesamiento
        String acc = accumulated;
        //test
        int test = 0;
        //empezamos el proceso
        posState = getRow(state);
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);
        acc += ("[" + state + "," + cadena.substring(posChar) + "]->");

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (registerTheResult(state, posChar, acc,cadena)) {
                    test = 1;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    computeStringAFN(cadena, state, posChar, acc);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {

            Abortadas.add(acc + "Abortada");
            return 0;
        }

        //verificando si esta en estado de aceptacion
        return test;
    }

    public boolean computeStringlistAFN(String cadena, String estado, int letra, String accumulated) {
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
        //string con los detalles del procesamiento
        String acc = accumulated;
        //resultado de la operacion
        boolean result = false;

        //empezamos el proceso
        posState = getRow(state);
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);
        acc += ("[" + state + "," + cadena.substring(posChar) + "]->");

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (registerListResult(state, posChar, acc,cadena)) {
                    result = true;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    computeStringlistAFN(cadena, state, posChar, acc);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {
            Abortadas.add(acc + "Cadena Abortada");
            return false;
        }

        //verificando si esta en estado de aceptacion
        return result;
    }
/*
    public void ImprimirProcessStringAFN(String cadena) {
        int i, j;
        System.out.println(this.q);
        newProcessStringAFN(cadena, this.q, 0);
        for (i = 0; i < this.endsStates.size(); i++) {
            System.out.println("estado finale: " + this.endsStates.get(i));
            for (j = 0; j < this.finalStates.size(); j++) {
                if (this.endsStates.get(i).equals(this.finalStates.get(j))) {
                    System.out.println("es un estado de aceptacion");
                    j = this.finalStates.size() + 1;
                } else if (this.endsStates.get(i).equals("aborted") && j == this.finalStates.size() - 1) {
                    System.out.println("es un estado abortado");
                    j = this.finalStates.size() + 1;
                } else if (j == this.finalStates.size() - 1) {
                    System.out.println("es un estado rechazado");
                }
            }
        }
    }
*/
/*
    public void newProcessStringAFN(String cadena, String estado, int posLetra) {
        //declaracion de variables
        int i, j, k;
        int posState, posSymbol;
        String symbol;
        ArrayList<String> delta1 = new ArrayList<>();
        //inicializacion 
        symbol = Character.toString(cadena.charAt(posLetra));
        posState = getRow(estado);
        posSymbol = getColumn(symbol);
        delta1 = this.delta[posState][posSymbol];
        //procesos
        posLetra++;
        if (!delta1.isEmpty()) {
            for (i = 0; i < delta1.size(); i++) {
                estado = delta1.get(i);
                if (posLetra >= cadena.length()) {
                    this.endsStates.add(estado);
                }
                if (posLetra < cadena.length()) {
                    newProcessStringAFN(cadena, estado, posLetra);
                }
            }
        } else {
            this.endsStates.add("aborted");
        }

        return;
    }
*/
    public boolean processStringAFN(String cadena, String estado, int letra) {
        int i;
        String state;
        int posState;
        String symbol;
        int posSymbol;
        int posChar = letra;
        state = estado;

        //empezamos el proceso
        posState = getRow(state);
        if (cadena.length() == 0) {
            if (finalStates.contains(estado)) {
                return true;
            }
            return false;
        }
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);
        System.out.println("[" + state + "," + symbol + "]->");

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (printTheResult(state, posChar, cadena)) {
                    finale = true;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    processStringAFN(cadena, state, posChar);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {
            System.out.println(">>>>>>procesamiento abortado");
            return false;
        }

        //verificando si esta en estado de aceptacion
        return finale;
    }

    public boolean procesarCadenaAFN(String cadena, String estado, int letra) {
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
        if (cadena.length() == 0) {
            if (finalStates.contains(estado)) {
                return true;
            }
            return false;
        }
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (getTheResult(state, posChar, cadena)) {
                    finale = true;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    procesarCadenaAFN(cadena, state, posChar);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {
            return false;
        }

        //verificando si esta en estado de aceptacion
        return finale;
    }

    public boolean procesarCadenaConDetallesAFN(String cadena, String estado, int letra, String accumulated) {
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
        //string con los detalles del procesamiento
        String acc = accumulated;

        //empezamos el proceso
        if (cadena.length() == 0) {
        if (finalStates.contains(estado)) {
                return true;
            }
            return false;
        }
        posState = getRow(state);
        symbol = Character.toString(cadena.charAt(posChar));

        posSymbol = getColumn(symbol);
        acc += ("[" + state + "," + cadena.substring(posChar) + "]->");

        if (!this.delta[posState][posSymbol].isEmpty()) {
            posChar++;
            for (i = 0; i < this.delta[posState][posSymbol].size(); i++) {
                state = this.delta[posState][posSymbol].get(i);
                //importante no dejar la impresion fuera del for o nada fuera del for porque a veces se lo traga el vacio
                if (getTheResult(state, posChar, cadena)) {
                    System.out.println(acc + "Cadena Aceptada");
                    finale = true;
                }

                if (posChar < cadena.length()) {
                    //System.out.println("estado :"+state+">"+symbol);..
                    procesarCadenaConDetallesAFN(cadena, state, posChar, acc);
                }
                //Intento para solucionar el error de la cadena "*"
                //else if(posChar==1&&!cicle){
                //  processStringAFN(cadena,this.q,0,true);
                //}

            }

        } else {
            return false;
        }

        //verificando si esta en estado de aceptacion
        return finale;
    }

    public boolean procesarCadena(String string) {
        boolean bool = procesarCadenaAFN(string, this.q, 0);
        if (bool) {
            System.out.println("----------------------------------------Cadena aceptada----------------------------------------");
            return bool;
        }
        System.out.println("----------------------------------------Cadena NO aceptada----------------------------------------");
        return bool;
    }

    public boolean procesarCadenaSinImprimirNiMadres(String string) {
        boolean bool = procesarCadenaAFN(string, this.q, 0);
        if (bool) {

            return bool;
        }
        return bool;
    }

    public boolean procesarCadenaConDetalles(String string) {
        boolean bool = procesarCadenaConDetallesAFN(string, this.q, 0, "");
        if (bool) {
            System.out.println("----------------------------------------Cadena aceptada----------------------------------------");
            return bool;
        }
        System.out.println("----------------------------------------Cadena NO aceptada----------------------------------------");
        return bool;
    }

    public int computarTodosLosProcesamientos(String string, String fileName) throws IOException {

        //lugar de la ruta
        String ruta = (fileName);
        int counter = 0;
        int computed = computeStringAFN(string, this.q, 0, "");
        File archivo = new File(ruta + "Aceptadas" + ".txt");
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(archivo));
        for (int i = 0; i < Aceptadas.size(); i++) {
            System.out.println("cadena:" + Aceptadas.get(i));
            bw.write(Aceptadas.get(i) + "\n");
            counter++;
        }
        bw.close();

        archivo = new File(ruta + "Rechazadas" + ".txt");
        bw = new BufferedWriter(new FileWriter(archivo));

        for (int i = 0; i < Rechazadas.size(); i++) {
            System.out.println("cadena:" + Rechazadas.get(i));
            bw.write(Rechazadas.get(i) + "\n");
            counter++;
        }
        bw.close();

        archivo = new File(ruta + "Abortadas" + ".txt");
        bw = new BufferedWriter(new FileWriter(archivo));

        for (int i = 0; i < Abortadas.size(); i++) {
            System.out.println("cadena:" + Abortadas.get(i));
            bw.write(Abortadas.get(i) + "\n");
            counter++;
        }
        bw.close();
        Aceptadas.clear();
        Rechazadas.clear();
        Abortadas.clear();
        System.out.println("cadenas procesadas:" + counter);

        return counter;
    }

    public void processStringList(ArrayList<String> stringList, String nombreArchivo, boolean imprimirPantalla) throws IOException {

        int i = 1;
        boolean result;
        int counterAc = 0, counterRe = 0, counterAb = 0; //contadores para las cadenas
        String ruta = nombreArchivo;//aqui cambienlo por la ruta en donde quieran que se cree el archivo (lo ultimo es le nombre)
        File archivo = new File(ruta + ".txt");
        BufferedWriter bw;
        if (imprimirPantalla) {
            System.out.println("Comprobando si nombreArchivo.txt esta creado\n");
        }
        while (true) {

            if (archivo.exists()) {
                ruta = nombreArchivo + i + ".txt"; //aqui lo mismo
                if (imprimirPantalla) {
                    System.out.println("El archivo ya esta creado, cambiando nombre\n");
                    System.out.println("Comprobando si nombreArchivo" + i + ".txt esta creado\n");
                }
                archivo = new File(ruta);
                i++;
            } else {
                bw = new BufferedWriter(new FileWriter(archivo));
                if (imprimirPantalla) {
                    System.out.println("Archivo creado con exito\n");
                }
                break;
            }
        }

        while (!stringList.isEmpty()) {

            int j = 0;//esta "j" se queda siempre en cero ya que el array list ira moviendo todo hacia la izqueirda hasta estar vacio
            String string = stringList.remove(j);
            bw.write(string + "     ,");
            if (imprimirPantalla) {
                System.out.println(string + "     ,");
            }
            String actualState;// este es el estado actual
            int actualStateP;//la posición del estado dentro de la matriz (esta siempre en algun lugar de la primera columna)
            String actualSymbol; //Aqui podria ser un char pero prefiero los strings <3
            int actualSymbolP; //la posición del simbolo dentro de la matriz (esta siempre en algun lugar de la primera fila)

            actualState = q;
            //Ahora empezamos el proceso de la cadena
            result = computeStringlistAFN(string, actualState, 0, "");

            //registramos despues los datos obtenidos tras el proceso y los guardamos
            for (int k = 0; k < Aceptadas.size(); k++) {
                if (imprimirPantalla) {
                    System.out.println(Aceptadas.get(k));
                }
                bw.write(Aceptadas.get(k));
                counterAc++;
            }
            for (int k = 0; k < Rechazadas.size(); k++) {
                if (imprimirPantalla) {
                    System.out.println(Rechazadas.get(k));
                }
                bw.write(Rechazadas.get(k));
                counterRe++;
            }
            for (int k = 0; k < Abortadas.size(); k++) {
                if (imprimirPantalla) {
                    System.out.println(Abortadas.get(k));
                }
                bw.write(Abortadas.get(k));
                counterAb++;
            }
            Aceptadas.clear();
            Rechazadas.clear();
            Abortadas.clear();

            //guardamos ahora los contadores para los procesamientos
            if (imprimirPantalla) {
                System.out.println("procesamientos posibles: " + (counterAb + counterAc + counterRe));
            }
            bw.write("procesamientos posibles: " + (counterAb + counterAc + counterRe));

            if (imprimirPantalla) {
                System.out.println("procesamientos Aceptados: " + counterAc);
            }
            bw.write("procesamientos Aceptados: " + counterAc);
            if (imprimirPantalla) {
                System.out.println("procesamientos Rechazados: " + counterRe);
            }
            bw.write("procesamientos Rechazados: " + counterRe);
            if (imprimirPantalla) {
                System.out.println("procesamientos Abortados: " + counterAb);
            }
            bw.write("procesamientos Abortados: " + counterAb);

            //guardamos por último el resultado
            if (result) {
                if (imprimirPantalla) {
                    System.out.println("Si");
                }
                bw.write("Si");
            } else {
                if (imprimirPantalla) {
                    System.out.println("No");
                }
                bw.write("No");
            }

        }

    }

    public boolean procesarCadenaConversion(String cadena) {
        AFD afd = new AFD();
        afd = AFNtoAFD(this);
        PCAFD procesadorAFD = new PCAFD();
        return procesadorAFD.processString(afd, cadena);
    }

    public boolean procesarCadenaConDetallesConversion(String cadena) {
        AFD afd = new AFD();
        afd = AFNtoAFD(this);
        PCAFD procesadorAFD = new PCAFD();
        return procesadorAFD.processStringWithDetails(afd, cadena);
    }

    public void procesarListaCadenasConversion(ArrayList<String> stringList, String nombreArchivo, boolean imprimirPantalla) throws IOException {
        AFD afd = new AFD();
        afd = AFNtoAFD(this);
        PCAFD procesadorAFD = new PCAFD();
        procesadorAFD.processStringList(afd, stringList, nombreArchivo, imprimirPantalla);
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

    public void initializeAFNwithData(ArrayList<Character> sigma, ArrayList<String> states, String q, ArrayList<String> finalStates, ArrayList<String>[][] delta) {

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
                for (int k = 0; k < delta[i][j].size(); k++) {

                    this.delta[i][j].add(delta[i][j].get(k));

                }

                //System.out.print(this.delta[i][j].get(0) + " ");
            }
            //System.out.println("");
        }
        //System.out.println("");

    }

    public static void main(String[] args) throws Exception {

        AFN afn = new AFN();
        AFD afd = new AFD();
        PCAFD pcafd = new PCAFD();
        afn.initializeAFN("AFNtest.txt");
        afd = Automatas.AFNtoAFD(afn);
        System.out.println(afn.procesarCadenaSinImprimirNiMadres("b"));
        System.out.println(pcafd.processStringSinImprimirNiMadres(afd, "b", false));
        //afd.hallarEstadosInaccesibles();// ejecutando esta función los estados inaccesibles quedan dentro del atributo (de la clase)InacessibleStates
        //System.out.println(afd.inaccessibleStates.get(0));
        /*ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aaaaaaab");
        prueba.add("");
        afd.computarTodosLosProcesamientos("aab", "nombreArchivo");
        afd.procesarCadena("aab");
        afd.procesarCadenaConDetalles("abb");
        //afd.processStringList(prueba, "abb", true);
         */
    }

}
