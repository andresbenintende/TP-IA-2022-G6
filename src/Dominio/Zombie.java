package Dominio;

import Auxiliares.Aleatorio;

public class Zombie {
    private Posicion posicion;
    private int poder;
    private int proxMov;

    public Zombie(Posicion posicion, int poder) {
        this.posicion = posicion;
        this.poder = poder;
        this.proxMov = Aleatorio.nroRandom(0,3);
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setPosicion(Posicion posicion) {
        this.posicion = posicion;
    }

    public int getPoder() {
        return poder;
    }

    public void setPoder(int poder) {
        this.poder = poder;
    }

    public int getProxMov() {
        return proxMov;
    }

    public void setProxMov(int proxMov) {
        this.proxMov = proxMov;
    }
}
