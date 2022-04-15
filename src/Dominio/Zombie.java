package Dominio;

public class Zombie {
    private Posicion posicion;
    private int poder;

    public Zombie(Posicion posicion, int poder) {
        this.posicion = posicion;
        this.poder = poder;
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
}
