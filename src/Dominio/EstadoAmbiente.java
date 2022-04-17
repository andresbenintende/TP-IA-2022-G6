package Dominio;

import Auxiliares.Aleatorio;
import frsf.cidisi.faia.state.EnvironmentState;

import java.util.ArrayList;
import java.util.List;

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
     * Este método es para inicializar el tablero real
     */
    @Override
    public void initState() {

        // Se setean las celdas como vacías (con 0)
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
        this.setPosicionPlanta(new Posicion(Aleatorio.nroRandom(0, 4),0));
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

    public void moverZombies(){
        for(Zombie zombie: zombies){
            //Si debe moverse en este ciclo de percepción ingresa
            if(zombie.getProxMov() == 0) {
                //Configuro el contador del proximo movimiento
                zombie.setProxMov(Aleatorio.nroRandom(0,3));
                Posicion posZombie = zombie.getPosicion();
                //Si la posición siguiente (posición adyacente izquierda) no está ocupada por otro zombie, avanza
                if (this.getTablero()[posZombie.getFila()][posZombie.getColumna() - 1] >= 0) {
                    posZombie.setColumna(posZombie.getColumna() - 1);
                    zombie.setPosicion(posZombie);
                }
            }
            //Disminuyo en 1 el contador
            else
                zombie.setProxMov(zombie.getProxMov()-1);
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

    public List<Integer> getCeldasArriba(Posicion pos) {
        if (pos.getFila() == 0) {
            return null;  //límite del tablero
        }
        List<Integer> celdas = new ArrayList<>();
        int celda;
        do{
            pos.setFila(pos.getFila()-1);
            celda = tablero[pos.getFila()][pos.getColumna()];
            celdas.add(celda);
            if(celda != 0) {
                return celdas;
            }
        }
        while(pos.getFila() != 0);
        return celdas;
    }

    public List<Integer> getCeldasIzq(Posicion pos) {
        if (pos.getColumna() == 0) {
            return null;  //límite del tablero
        }
        List<Integer> celdas = new ArrayList<>();
        int celda;
        do{
            pos.setColumna(pos.getColumna()-1);
            celda = tablero[pos.getFila()][pos.getColumna()];
            celdas.add(celda);
            if(celda != 0) {
                return celdas;
            }
        }
        while(pos.getColumna() != 0);
        return celdas;
    }

    public List<Integer> getCeldasAbajo(Posicion pos) {
        if (pos.getFila() == 4) {
            return null;  //límite del tablero
        }
        List<Integer> celdas = new ArrayList<>();
        int celda;
        do{
            pos.setFila(pos.getFila()+1);
            celda = tablero[pos.getFila()][pos.getColumna()];
            celdas.add(celda);
            if(celda != 0) {
                return celdas;
            }
        }
        while(pos.getFila() != 4);
        return celdas;
    }

    public List<Integer> getCeldasDer(Posicion pos) {
        if (pos.getFila() == 8) {
            return null;  //límite del tablero
        }
        List<Integer> celdas = new ArrayList<>();
        int celda;
        do{
            pos.setColumna(pos.getColumna()+1);
            celda = tablero[pos.getFila()][pos.getColumna()];
            celdas.add(celda);
            if(celda != 0) {
                return celdas;
            }
        }
        while(pos.getColumna() != 8);
        return celdas;
    }
}
