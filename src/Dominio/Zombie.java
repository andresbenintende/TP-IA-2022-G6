package Dominio;

import Auxiliares.Aleatorio;

public class Zombie {
    private Posicion posicion;
    private int poder;
    private int proxMov;

    public Zombie(Posicion posicion, int poder) {
        this.posicion = posicion;
        this.poder = poder;
        this.proxMov = Aleatorio.nroRandom(0,2); //esto es para que no se mueva en el momento en que se crea
    }

    public Zombie() {
    }

    /**
     * This method will return a true if the position passed is the same where de zombie is
     * @param fila
     * @param columna
     * @return boolean
     */
    public boolean checkPosicion(int fila, int columna){
        if(this.getPosicion().getColumna()==columna && this.getPosicion().getFila()==fila){
            return true;
        }
        return  false;
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
