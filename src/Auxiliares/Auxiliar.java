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
    public static Posicion getPosicionZombie(Posicion posicion, int index) {
        Posicion posicionZombie = new Posicion(posicion.getFila(), posicion.getColumna());
        switch (index) {
            case 0 -> //Izquierda
                    posicionZombie.setColumna(posicion.getColumna() - 1);
            case 1 -> //Arriba
                    posicionZombie.setFila(posicion.getFila() - 1);
            case 2 -> //Derecha
                    posicionZombie.setColumna(posicion.getColumna() + 1);
            case 3 -> //Abajo
                    posicionZombie.setFila(posicion.getFila() + 1);
            default -> {}
        }
        return  posicionZombie;
    }
}
