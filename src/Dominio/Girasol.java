package Dominio;

public class Girasol {
    private Posicion posicion;
    private int cantSoles;


    public Girasol(Posicion posicion, int cantSoles) {
        this.posicion = posicion;
        this.cantSoles = cantSoles;
    }
    /**
     * This method will return a perception made by the subclass of Environment
     * @param fila,
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
    public int getCantSoles() {
        return cantSoles;
    }
    public void setCantSoles(int cantSoles) {
        this.cantSoles = cantSoles;
    }
}
