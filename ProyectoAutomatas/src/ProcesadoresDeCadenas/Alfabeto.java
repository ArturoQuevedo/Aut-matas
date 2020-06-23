/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcesadoresDeCadenas;

import java.util.ArrayList;

/**
 *
 * @author andre
 */
public class Alfabeto {

    public ArrayList<Character> simbolos;
    public ArrayList<String> cadenas;

    public Alfabeto(ArrayList<Character> simbolos) {
        this.simbolos = simbolos;
        this.cadenas = new ArrayList<>();
    }

    public ArrayList<String> getCadenas() {
        return cadenas;
    }

    public boolean generarCadenaAleatoria(int n) {
        boolean fiveThousands;
        int size;
        size = this.simbolos.size();
        fiveThousands = theRealRecursiveStringsGenerator("", size, n);
        return fiveThousands;
    }

    //el nombre de la funcion esta sujeto a cambios
    public boolean theRealRecursiveStringsGenerator(String base, int size, int n) {
        if (this.cadenas.size() >= 5000) {
            //System.out.println("ya hay 5000 cadenas de prueba");
            return true;
        }
        if (n == 0) {
            //System.out.println(base);
            //System.out.println("---");
            this.cadenas.add(base);
            return false;
        }
        for (int i = 0; i < size; i++) {
            String newBase = base + this.simbolos.get(i);
            theRealRecursiveStringsGenerator(newBase, size, n - 1);
        }
        return false;
    }

    public void fiveThousandsStringGenerator() {
        boolean fiveThousandsString = false;
        int i = 0;
        while (!fiveThousandsString) {
            i++;
            fiveThousandsString = generarCadenaAleatoria(i);
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<Character> simb = new ArrayList<>();
        simb.add('a');
        simb.add('b');
        Alfabeto alfabeto = new Alfabeto(simb);
        alfabeto.fiveThousandsStringGenerator();
        for (int i = 0; i < 20; i++) {
            System.out.println(alfabeto.cadenas.get(i));

        }
    }

}
