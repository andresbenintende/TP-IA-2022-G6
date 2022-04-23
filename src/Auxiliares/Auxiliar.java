package Auxiliares;

import Dominio.Posicion;

import java.util.ArrayList;
import java.util.List;

public class Auxiliar {

    public static List<Integer> getAdyacentes(int[][] tablero, Posicion posicion){
        List<Integer> adyacentes = new ArrayList<>();
        //Izquierda
        adyacentes.add(posicion.getColumna() > 1 ? tablero[posicion.getFila()][posicion.getColumna()-1] : 0);

        //Arriba
        adyacentes.add(posicion.getFila() > 1 ? tablero[posicion.getFila()-1][posicion.getColumna()] : 0);

        //Derecha
        adyacentes.add(posicion.getColumna() < 9 ? tablero[posicion.getFila()][posicion.getColumna()+1] : 0);

        //Abajo
        adyacentes.add(posicion.getFila() < 5 ? tablero[posicion.getFila()+1][posicion.getColumna()] : 0);

        return  adyacentes;
    }

}
