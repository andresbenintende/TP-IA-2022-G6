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
    private List<Zombie> zombies;
    private Posicion posicion;
    private List<Girasol> girasoles;
    private int soles;

    public EstadoAgente(int[][] t, Posicion p, int s, List<Girasol> g, List<Zombie> z) {
        tablero = t;
        posicion = p;
        soles = s;
        zombies = z;
        girasoles = g;

    }

    public EstadoAgente() {
        tablero = new int[5][9];
        posicion = new Posicion(1,3);
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

        Posicion nuevaPosicion = new Posicion(posicion.getFila(),posicion.getColumna());

        List<Girasol> nuevosGirasoles = new ArrayList<>();
        nuevosGirasoles = girasoles;

        List<Zombie> nuevosZombies = new ArrayList<>();
        nuevosZombies = zombies;


        EstadoAgente nuevoEstado = new EstadoAgente(nuevoTablero,
                nuevaPosicion, this.soles, nuevosGirasoles, nuevosZombies);

        return nuevoEstado;
    }

    /**
     * Este método actualiza el estado del agente cuando se recibe una
     * percepción desde el simulador
     */
    @Override
    public void updateState(Perception p) {
        Percepcion percepcion = (Percepcion) p;

        int row = this.getRowPosition();
        int col = this.getColumnPosition();

        if (col == 0) {
            col = 3;
        } else {
            col = col - 1;
        }
        tablero[row][col] = pacmanPerception.getLeftSensor();

        row = this.getRowPosition();
        col = this.getColumnPosition();

        if (col == 3) {
            col = 0;
        } else {
            col = col + 1;
        }
        tablero[row][col] = pacmanPerception.getRightSensor();

        row = this.getRowPosition();
        col = this.getColumnPosition();

        if (row == 0) {
            row = 3;
        } else {
            row = row - 1;
        }
        tablero[row][col] = pacmanPerception.getTopSensor();


        row = this.getRowPosition();
        col = this.getColumnPosition();

        if (row == 3) {
            row = 0;
        } else {
            row = row + 1;
        }
        tablero[row][col] = pacmanPerception.getBottomSensor();

        energy = pacmanPerception.getEnergy();
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

        this.setRowPosition(1);
        this.setColumnPosition(1);

        this.setSoles(50);
    }

    /**
     * This method returns the String representation of the agent state.
     */
    @Override
    public String toString() {
        String str = "";

        str = str + " position=\"(" + getRowPosition() + "," + "" + getColumnPosition() + ")\"";
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

    public void setRowPosition(int value) {
        this.posicion.setFila(value);
    }

    public void setColumnPosition(int value) {
        this.posicion.setColumna(value);
    }

    public int getRowPosition() {
        return posicion.getFila();
    }

    public int getColumnPosition() {
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

