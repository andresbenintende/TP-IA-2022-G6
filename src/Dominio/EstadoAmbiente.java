package Dominio;

import Auxiliares.Aleatorio;
import frsf.cidisi.faia.state.EnvironmentState;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EstadoAmbiente extends EnvironmentState {

    private int[][] tablero;
    private List<Zombie> zombies;
    private Posicion posicionPlanta;
    private List<Girasol> girasoles;
    private int solesPlanta;

    public EstadoAmbiente(int[][] m) {
        tablero = m;
    }

    public EstadoAmbiente() {
        tablero = new int[6][11]; //todo tablero
        zombies= new ArrayList<>();
        girasoles = new ArrayList<>();
        this.initState();

    }

    /**
     * Este método es para inicializar el tablero real
     */
    @Override
    public void initState() {

        // Se setean las celdas como vacías (con 0)
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero[0].length; col++) {
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
        //Setear posicion random de la planta
       //this.setPosicionPlanta(new Posicion(Aleatorio.nroRandom(1, 5),1));  //TODO SI EL TABLERO ESTA REFERENCIADO A 1,1 COMO VERTICE INICIAL
        this.setPosicionPlanta(new Posicion(3,1));
        //Setear cant soles random de la planta
        this.setSolesPlanta(Aleatorio.nroRandom(2,20));

        //Guardamos la posicion de la planta en el tablero
        this.tablero[posicionPlanta.getFila()][posicionPlanta.getColumna()] = this.getSolesPlanta();
    }

    private void inicializarZombies() {
        crearZombies();
        for(Zombie zombie : zombies){
            zombie.setPoder(Aleatorio.nroRandom(-5,-1));
            zombie.setProxMov(Aleatorio.nroRandom(1,2)); //Se setea este valor para que el zombie no se mueva en el primer ciclo de percepcion
            //Seteo de posición:
            // Fila: aleatoriamente entre 0 y 4
            // Columna: por defecto se setea en 8.
            // Superposición: si se da el caso que la posición esté ocupada, se suma 1 a la columna
            //               (como si se tratase de una cola de espera)

            Posicion posicion = new Posicion(Aleatorio.nroRandom(1,5), 9);

            //HAY UN ZOMBIE EN LA POSICION LO TIRO PARA ATRAS
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

            }
            zombie.setPosicion(posicion);
        }
    }

    private void crearZombies() {
       int cantZombies = Aleatorio.nroRandom(4,5);//todo aleatorio zombies corregir
        for (int z= 1; z<=cantZombies; z++){
            zombies.add(new Zombie());
        }

    }

    public void moverZombies(){
        for(Zombie zombie: zombies){
            //Si debe moverse en este ciclo de percepción ingresa
            if(zombie.getProxMov() == 0) {
                //Configuro el contador del próximo movimiento
                zombie.setProxMov(Aleatorio.nroRandom(0,3));
                Posicion posZombie = zombie.getPosicion();
                //Si la posición siguiente (posición adyacente izquierda) no está ocupada por otro zombie, avanza
                if (this.getTablero()[posZombie.getFila()][posZombie.getColumna() - 1] >= 0) {

                    posZombie.setColumna(posZombie.getColumna() - 1);
                    zombie.setPosicion(posZombie);
                    //Si en esa posición hay un girasol lo elimina
                    if(this.getTablero()[posZombie.getFila()][posZombie.getColumna() ] > 0){
                            quitarSoles(posZombie);
                        }
                }
            }
            //Disminuyo en 1 el contador
            else
                zombie.setProxMov(zombie.getProxMov()-1);
        }
    }

    private void quitarSoles(Posicion posicion) {
        //Se encuentra un girasol
        Girasol girasolMuerto= this.girasoles.stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get();
        this.girasoles.remove(girasolMuerto);
    }

    public void generarSolesGirasol(){
        for (Girasol girasol : girasoles){
            girasol.setCantSoles(girasol.getCantSoles()+Aleatorio.nroRandom(1,3));
        }

    }

    /**
     * Representacion del tablero real
     */
    @Override
    public String toString() {
        String str = "";

        str = str + "[ \n";
        for (int row = 1; row < tablero.length; row++) {
            str = str + "[ ";
            for (int col = 1; col < tablero[0].length; col++) {
                if (tablero[row][col] >= 0 && tablero[row][col] <= 9) {
                    str = str + " ";
                }
                str = str + tablero[row][col] + " ";
            }
            str = str + "]\n";
        }
        str = str + " ]";

        return str;
    }
    public List<Integer> getCeldasArriba(Posicion p) {
        Posicion pos = new Posicion(p.getFila(),p.getColumna());
        if (pos.getFila() == 1) {
            return new ArrayList<>(); //límite del tablero
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
        while(pos.getFila() != 1);
        return celdas;
    }

    public List<Integer> getCeldasIzq(Posicion p) {
        Posicion pos = new Posicion(p.getFila(),p.getColumna());
        if (pos.getColumna() == 1) {
            return new ArrayList<>(); //límite del tablero
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
        while(pos.getColumna() != 1);
        return celdas;
    }

    public List<Integer> getCeldasAbajo(Posicion p) {
        Posicion pos = new Posicion(p.getFila(),p.getColumna());
        if (pos.getFila() == 5) {
            return new ArrayList<>(); //límite del tablero
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
        while(pos.getFila() != 5);
        return celdas;
    }

    public List<Integer> getCeldasDer(Posicion p) {
        Posicion pos = new Posicion(p.getFila(),p.getColumna());
        if (pos.getFila() == 9) {
            return new ArrayList<>(); //límite del tablero
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
        while(pos.getColumna() != 9);
        return celdas;
    }


    //GETTERS Y SETTERS
    public int getCantidadZombies() {
        return zombies.size();
    }

    public int[][] getTablero() {
        return tablero;
    }
    public void setTablero(int[][] tablero) {
        this.tablero = tablero;
    }
    public void setPosicionTablero(int row, int col, int value) {
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
    public List<Zombie> getZombies() {
        return zombies;
    }
    public void setZombies(List<Zombie> zombies) {
        this.zombies = zombies;
    }
    public List<Girasol> getGirasoles() {
        return girasoles;
    }
    public void setGirasoles(List<Girasol> girasoles) {
        this.girasoles = girasoles;
    }
}
