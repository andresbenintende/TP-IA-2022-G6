package Dominio;

import Auxiliares.Aleatorio;

public class Zombie {
    private Posicion posicion;
    private int poder;
    private int proxMov;

    public Zombie(Posicion posicion, int poder) {
        this.posicion = posicion;
        this.poder = poder;
        this.proxMov = Aleatorio.nroRandom(0, 2); //esto es para que no se mueva en el momento en que se crea
    }

    public Zombie() {
    }

    /**
     * This method will return a true if the position passed is the same where de zombie is
     *
     * @param pos
     * @return boolean
     */
    public boolean checkPosicion(Posicion pos) {
        if (this.getPosicion().getColumna() == pos.getColumna() && this.getPosicion().getFila() == pos.getFila()) {
            return true;
        }
        return false;
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

    public Zombie clone() {
        try {
            return (Zombie) super.clone();
        } catch (CloneNotSupportedException e) {
            return new Zombie(new Posicion(posicion.getFila(), posicion.getColumna()), poder);
        }
    }
}
