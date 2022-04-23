package Dominio;

import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent the internal state of the Pacman.
 */
public class EstadoAgente extends SearchBasedAgentState {

    private int[][] tablero;
    private Posicion posicion;
    private int cantZombies;
    private List<Zombie> zombies;
    private List<Girasol> girasoles;
    private int soles;

    public EstadoAgente(int[][] t, Posicion p, int c, List<Zombie> z, List<Girasol> g, int s) {
        tablero = t;
        posicion = p;
        cantZombies = c;
        zombies = z;
        girasoles = g;
        soles = s;
    }

    public EstadoAgente() {
        tablero = new int[5][9];
        posicion = new Posicion(1,3);
        cantZombies = 5;
        soles = 5;
        this.initState();
    }

    /**
     * Este método clona el estado del agente.
     * Se usa en el proceso de búsqueda cuando se crea el árbol de búsqueda
     */
    @Override
    public SearchBasedAgentState clone() {
        int[][] nuevoTablero = new int[5][9];

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                nuevoTablero[row][col] = tablero[row][col];
            }
        }
        List<Zombie> nuevosZombies = new ArrayList<>();
        nuevosZombies = zombies;

        List<Girasol> nuevosGirasoles = new ArrayList<>();
        nuevosGirasoles = girasoles;

        EstadoAgente nuevoEstado = new EstadoAgente(nuevoTablero,
                this.getPosicion(), this.cantZombies, nuevosZombies, nuevosGirasoles, this.soles);

        return nuevoEstado;
    }

    /**
     * Este método actualiza el estado del agente cuando se recibe una
     * percepción desde el simulador
     */
    @Override
    public void updateState(Perception p) {
        Percepcion percepcion = (Percepcion) p;

        //Actualizar sensores
        //Actualizo sensor de arriba
        for(int i = 0; i < percepcion.getSensorArriba().size(); i++){
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila()-(i+1), this.getColumna());

            actualizarSensoresVerticales(posicion,valorCelda);
        }

        //Actualizo sensor de abajo
        for(int i = 0; i < percepcion.getSensorAbajo().size(); i++){
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila()+(i+1), this.getColumna());

            actualizarSensoresVerticales(posicion,valorCelda);
        }

        //Actualizo sensor de la derecha
        for(int i = 0; i < percepcion.getSensorDer().size(); i++){
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna()+(i+1));

            actualizarSensorDer(posicion,valorCelda);
        }

        //Actualizo sensor de la izquierda
        for(int i = 0; i < percepcion.getSensorIzq().size(); i++){
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna()-(i+1));

            actualizarSensorIzq(posicion,valorCelda);
        }



        soles = percepcion.getEnergiaSoles();
    }


    /**
     * Funcion genérica para analizar los sensores de arriba y abajo,
     * Ya que el comportamiento de ambos es similar
     * @param posicion
     * @param valorCelda
     */
    private void actualizarSensoresVerticales(Posicion posicion, Integer valorCelda) {
        int valorTablero = this.tablero[posicion.getFila()][posicion.getColumna()];

        if(valorTablero != valorCelda) {
            if(valorCelda > 0){
                actualizarListaGirasol(posicion, valorCelda);
            }

            if(valorCelda == 0){
                if(valorTablero > 0){
                    this.girasoles.removeIf(girasol -> girasol.getPosicion() == posicion);
                }
                else{

                }
            }

            if(valorCelda < =0)

        }

        if(valorCelda != 0){
            //Zombie
            if(valorCelda < 0){
                actualizarListaZombie(posicion, valorCelda);
            }
            //Girasol
            if(valorCelda > 0){
                actualizarListaGirasol(posicion, valorCelda);
            }
        }
        else{
            //Si en el tablero figura que tenía un zombie, entonces el zombie avanzó
            if(this.tablero[posicion.getFila()][posicion.getColumna()] < 0) {
                actualizarListaZombie(posicion, valorCelda);
                //"Muevo" el zombie dentro del tablero
                this.tablero[posicion.getFila()][posicion.getColumna()-1] = this.tablero[posicion.getFila()][posicion.getColumna()];
            }

            if(this.tablero[posicion.getFila()][posicion.getColumna()] > 0) {
                this.girasoles.removeIf(girasol -> girasol.getPosicion() == posicion);
            }
        }
        //Actualizo el valor del tablero
        this.tablero[posicion.getFila()][posicion.getColumna()] = valorCelda;

    }

    private void actualizarSensorDer(Posicion posicion, Integer valorCelda) {
        if(valorCelda != 0){
            //Zombie
            if(valorCelda < 0){
                actualizarListaZombie(posicion, valorCelda);
            }
            //Girasol
            if(valorCelda > 0){
                actualizarListaGirasol(posicion, valorCelda);
            }
        }
    }
    private void actualizarSensorIzq(Posicion posicion, Integer valorCelda) {

    }


    private void actualizarListaGirasol(Posicion posicion, Integer valorCelda) {
        this.girasoles.stream().filter(girasol -> girasol.getPosicion() == posicion).findFirst().get().setCantSoles(valorCelda);
    }

    private void actualizarListaZombie(Posicion posicion, Integer valorCelda) {
        //Si en la posicion tenía registrado un girasol, entonces el zombie lo eliminó
        if(hayGirasol(posicion)){
            this.girasoles.removeIf(girasol -> girasol.getPosicion() == posicion);
        }

        //Si en la posición tenía registrado un zombie, quiere decir que se movió,
        //por lo tanto registramos un movimiento ficticio, para no perder el rastro del zombie
        if(hayZombie(posicion)){
            Posicion posicionAux = new Posicion(posicion.getFila(), posicion.getColumna()-1);
            this.zombies.stream().filter(zombie -> zombie.getPosicion() == posicion).findFirst().get().setPosicion(posicionAux);
        }
        //En la posición no tenía registrado un zombie, por lo tanto lo agrego a la lista
        else {
            this.zombies.add(new Zombie(posicion, valorCelda));
        }
    }

    private boolean hayZombie(Posicion posicion) {
        for(Zombie zombie : this.zombies){
            if(zombie.getPosicion() == posicion)
                return true;
        }
        return false;
    }

    private boolean hayGirasol(Posicion posicion) {
        for(Girasol girasol : this.girasoles){
            if(girasol.getPosicion() == posicion)
                return true;
        }
        return false;
    }

    /**
     * This method is optional, and sets the initial state of the agent.
     */
    @Override
    public void initState() {
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                tablero[row][col] = PacmanPerception.UNKNOWN_PERCEPTION;
            }
        }

        this.setFila(1);
        this.setColumna(1);

        this.setSoles(50);
    }

    /**
     * This method returns the String representation of the agent state.
     */
    @Override
    public String toString() {
        String str = "";

        str = str + " position=\"(" + getFila() + "," + "" + getColumna() + ")\"";
        str = str + " energy=\"" + energy + "\"\n";

        str = str + "world=\"[ \n";
        for (int row = 0; row < tablero.length; row++) {
            str = str + "[ ";
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] == -1) {
                    str = str + "* ";
                } else {
                    str = str + tablero[row][col] + " ";
                }
            }
            str = str + " ]\n";
        }
        str = str + " ]\"";

        return str;
    }

    /**
     * This method is used in the search process to verify if the node already
     * exists in the actual search.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PacmanAgentState))
            return false;

        int[][] worldObj = ((PacmanAgentState) obj).getWorld();
        int[] positionObj = ((PacmanAgentState) obj).getPosition();

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] != worldObj[row][col]) {
                    return false;
                }
            }
        }

        if (position[0] != positionObj[0] || position[1] != positionObj[1]) {
            return false;
        }

        return true;
    }

    // The following methods are Pacman-specific:

    public int[][] getWorld() {
        return tablero;
    }

    public int getWorldPosition(int row, int col) {
        return tablero[row][col];
    }

    public void setWorldPosition(int row, int col, int value) {
        this.tablero[row][col] = value;
    }

    public Posicion getPosicion() {
        return posicion;
    }

    public void setFila(int value) {
        this.posicion.setFila(value);
    }

    public void setColumna(int value) {
        this.posicion.setColumna(value);
    }

    public int getFila() {
        return posicion.getFila();
    }

    public int getColumna() {
        return posicion.getColumna();
    }

    public int getSoles() {
        return soles;
    }

    private void setSoles(int soles) {
        this.soles = soles;
    }

    public boolean isAllWorldKnown() {
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] == PacmanPerception.UNKNOWN_PERCEPTION) {
                    return false;
                }
            }
        }

        return true;
    }

    public int getUnknownCellsCount() {
        int result = 0;

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] == PacmanPerception.UNKNOWN_PERCEPTION) {
                    result++;
                }
            }
        }

        return result;
    }

    public int getRemainingFoodCount() {
        int result = 0;

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] == PacmanPerception.FOOD_PERCEPTION) {
                    result++;
                }
            }
        }

        return result;
    }

    public boolean isNoMoreFood() {
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero.length; col++) {
                if (tablero[row][col] == PacmanPerception.FOOD_PERCEPTION) {
                    return false;
                }
            }
        }
        return true;
    }

    public int getVisitedCellsCount() {
        return visitedCells;
    }

    public void increaseVisitedCellsCount() {
        this.visitedCells = +20;
    }
}

