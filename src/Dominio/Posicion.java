package Dominio;

public class Posicion {

    public int fila;
    public int columna;

    public Posicion(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public Posicion(Posicion p) {
        this.fila = p.getFila();
        this.columna = p.getColumna();
    }

    /**
     * This method will return a perception made by the subclass of Environment
     * @param pos
     * @return boolean
     */
    public boolean checkPosicion(Posicion pos){
        if(this.getColumna()==pos.getColumna() && this.getFila()==pos.getFila()){
            return true;
        }
        return  false;
    }
    public int getFila() {
        return fila;
    }
    public void setFila(int fila) {
        this.fila = fila;
    }
    public int getColumna() {
        return columna;
    }
    public void setColumna(int columna) {
        this.columna = columna;
    }
}
