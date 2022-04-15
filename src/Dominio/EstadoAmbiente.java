package Dominio;

import Auxiliares.Aleatorio;
import frsf.cidisi.faia.state.EnvironmentState;

public class EstadoAmbiente extends EnvironmentState {

    private int[][] tablero;
    private Zombie[] zombies;
    private Posicion posicionPlanta;
    private int solesPlanta;

    public EstadoAmbiente(int[][] m) {
        tablero = m;
    }

    public EstadoAmbiente() {
        tablero = new int[5][9];
        this.initState();
    }

    public int[][] getTablero() {
        return tablero;
    }

    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }

    public void setTablero(int row, int col, int value) {
        this.tablero[row][col] = value;
    }

    public Posicion getPosicionPlanta() {
        return posicionPlanta;
    }

    public void setPosicionPlanta(Posicion posicionPlanta) {
        this.posicionPlanta = posicionPlanta;
    }

    public int getSolesPlanta() {
        return solesPlanta;
    }

    public void setSolesPlanta(int solesPlanta) {
        this.solesPlanta = solesPlanta;
    }

    public Zombie[] getZombies() {
        return zombies;
    }

    public void setZombies(Zombie[] zombies) {
        this.zombies = zombies;
    }


    /**
     * This method is used to setup the initial real tablero.
     */
    @Override
    public void initState() {

        // Sets all cells as empty
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                tablero[row][col] = Percepcion.EMPTY_PERCEPTION;
            }
        }
        inicializarPosiciones();

    }

    private void inicializarPosiciones() {
        inicializarPlanta();
        inicializarZombies();
    }

    private void inicializarPlanta() {
        this.setPosicionPlanta(new Posicion(Aleatorio.nroRandom(0, 4),1));
        this.setSolesPlanta(Aleatorio.nroRandom(2,20));
        this.tablero[posicionPlanta.getFila()][posicionPlanta.getColumna()] = this.getSolesPlanta();
    }

    private void inicializarZombies() {
        for(Zombie zombie : zombies){
            zombie.setPoder(Aleatorio.nroRandom(-5,-1));

            //Seteo de posición:
            // Fila: aleatoriamente entre 0 y 4
            // Columna: por defecto se setea en 8.
            // Superposición: si se da el caso que la posición esté ocupada, se suma 1 a la columna
            //               (como si se tratase de una cola de espera)

            Posicion posicion = new Posicion(Aleatorio.nroRandom(0,4), 8);

            if(tablero[posicion.getFila()][posicion.getColumna()] < 0){
                posicion.setColumna(posicion.getColumna() + 1);
            }
            else{
                //La aparicion está condicionada
                if(Aleatorio.booleanRandom()) {
                    tablero[posicion.getFila()][posicion.getColumna()] = zombie.getPoder();
                }
                else {
                    posicion.setColumna(posicion.getColumna() + 1);

                }
                zombie.setPosicion(posicion);
            }
        }
    }

    /**
     * Representacion del tablero real
     */
    @Override
    public String toString() {
        String str = "";

        str = str + "[ \n";
        for (int row = 0; row < tablero.length; row++) {
            str = str + "[ ";
            for (int col = 0; col < tablero.length; col++) {
                str = str + tablero[row][col] + " ";
            }
            str = str + " ]\n";
        }
        str = str + " ]";

        return str;
    }

    public int getCeldaArriba(Posicion pos) {
        if (pos.getFila() == 0) {
            return -1;  //limite del tablero
        }
        return tablero[pos.getFila() - 1][pos.getColumna()];
    }

    public int getCeldaIzq(Posicion pos) {
        if (pos.getColumna() == 0) {
            return -1;  //limite del tablero
        }
        return tablero[pos.getFila()][pos.getColumna() - 1];
    }

    public int getCeldaDer(Posicion pos) {
        if (pos.getColumna() == 8) {
            return -1;  //limite del tablero
        }
        return tablero[pos.getFila()][pos.getColumna() + 1];
    }

    public int getCeldaAbajo(Posicion pos) {
        if (pos.getFila() == 4) {
            return -1;  //limite del tablero
        }
        return tablero[pos.getFila() + 1][pos.getColumna()];
    }

}
