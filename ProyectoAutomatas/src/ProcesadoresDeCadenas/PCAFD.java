/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcesadoresDeCadenas;

import Automatas.AFD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Equipo
 */
public class PCAFD {

    public boolean processString(AFD afd, String string, boolean imprimir) { //Procesar string con o sin detalles

        if (imprimir == true) {
            System.out.print(string + "\n");
        }
        String actualState;// este es el estado actual
        int actualStateP;//fila del estado actual
        String actualSymbol; //char a leer
        int actualSymbolP; //columna del char a leer

        actualState = afd.getQ();
        //Ahora empezamos el proceso de la cadena
        while (!string.isEmpty() && !string.contains("$")) {
            actualStateP = afd.getRow(actualState);
            actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda en cada iteración y luego actualizamos el string.

            if (string.length() > 1) {
                string = string.substring(1); //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
            } else {
                string = "";
            }
            if (imprimir == true) {
                System.out.print("[" + actualState + "," + actualSymbol + string + "]->");
            }

            actualSymbolP = afd.getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)

            if (!afd.getDelta()[actualStateP][actualSymbolP].get(0).isEmpty()) {

                actualState = afd.getDelta()[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición
            } else {
                System.out.print("Usted no ingresó los estados limbo");
                break;
            }
        }
        if (imprimir == true) {
            System.out.print("[" + actualState + "]" + "\t");
        }

        for (int k = 0; k < afd.getFinalStates().size(); k++) {
            if (actualState.equals(afd.getFinalStates().get(k))) {
                System.out.print("Aceptación\n");
                return true;
            }
        }
        System.out.print("No aceptación\n");
        return false;

    }

    public boolean processString(AFD afd, String string) {//Procesar string SIN detalles

        return processString(afd, string, false);
    }

    public boolean processStringWithDetails(AFD afd, String string) {//Procesar string CON detalles

        return processString(afd, string, true);
    }

    public void processStringList(AFD afd, ArrayList<String> stringList, String nombreArchivo, boolean imprimirPantalla) throws IOException {

        //Verificar si "nombreArchivo.txt" ya existe
        int i = 1;
        String ruta = nombreArchivo + ".txt";//aqui cambienlo por la ruta en donde quieran que se cree el archivo (lo ultimo es le nombre)
        File archivo = new File(ruta);
        BufferedWriter bw;
        while (true) {
            if (archivo.exists()) {

                ruta = "AFD_Generado" + i + ".txt";; //aqui lo mismo
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
                System.out.print(string + "\n");
            }
            bw.write(string + "\n");

            String actualState;// este es el estado actual
            int actualStateP;//fila del estado actual
            String actualSymbol; //char a leer
            int actualSymbolP; //columna del char a leer

            actualState = afd.getQ();
            while (!string.isEmpty() && !string.contains("$")) {

                actualStateP = afd.getRow(actualState);
                actualSymbol = Character.toString(string.charAt(0)); // quitamos la primera letra de la izquierda

                //Este if es para controlar el caso en que solo quede o sea un string de tamaño 1
                if (string.length() > 1) {
                    string = string.substring(1);
                } else {
                    string = "";
                }

                if (imprimirPantalla) {
                    System.out.print("[" + actualState + "," + actualSymbol + string + "]->");
                }

                bw.write("[" + actualState + "," + actualSymbol + string + "]->");
                actualSymbolP = afd.getColumn(actualSymbol);//buscamos la posición de ese simbolo en nuestra matriz(es algun lugar de la primera fila)

                if (!afd.getDelta()[actualStateP][actualSymbolP].get(0).isEmpty()) {

                    actualState = afd.getDelta()[actualStateP][actualSymbolP].get(0);//Ya que esto es un AFD, siempre habra como maximo un elemento en esa posición

                } else {
                    System.out.println("Usted no ingresó el autómata completo");
                    System.exit(1);
                }
            }

            if (imprimirPantalla) {
                System.out.print("[" + actualState + "]" + "\t");
            }
            bw.write("[" + actualState + "]" + "\t");

            for (int k = 0; k < afd.getFinalStates().size(); k++) {

                if (actualState.equals(afd.getFinalStates().get(k))) {
                    if (imprimirPantalla) {
                        System.out.print("Aceptación\n");
                    }
                    bw.write("Aceptación\n");
                    break;
                }
                if (k == afd.getFinalStates().size() - 1) {//si esto ocurre entonces NO encontro ningun estado coincidente
                    if (imprimirPantalla) {
                        System.out.print("No Aceptación\n");
                    }
                    bw.write("No Aceptación\n");
                    break;
                }
            }
        }
        bw.close();
        System.out.println("Se creó el archivo con el procesamiento de las cadenas");

    }

    public static void main(String[] args) throws Exception {

        AFD afd = new AFD();
        afd.initializeAFD("file.txt");

        PCAFD procesador = new PCAFD();
        //procesador.processStringWithDetails(afd, "abababab");        

        ArrayList<String> prueba = new ArrayList<>();
        prueba.add("aa");
        prueba.add("bb");
        prueba.add("aba");
        prueba.add("baba");
        prueba.add("abba");
        prueba.add("");
        prueba.add("b");
        //procesador.processStringWithDetails(afd, "abbbba");
        procesador.processStringList(afd, prueba, "ProbandoLa1", true);
    }

}
