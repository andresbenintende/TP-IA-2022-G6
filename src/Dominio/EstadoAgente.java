package Dominio;

import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        posicion = new Posicion(1, 3);
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

        EstadoAgente nuevoEstado = new EstadoAgente(nuevoTablero, this.getPosicion(), this.cantZombies, nuevosZombies, nuevosGirasoles, this.soles);

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
        for (int i = 0; i < percepcion.getSensorArriba().size(); i++) {
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila() - (i + 1), this.getColumna());

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de abajo
        for (int i = 0; i < percepcion.getSensorAbajo().size(); i++) {
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila() + (i + 1), this.getColumna());

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de la derecha
        for (int i = 0; i < percepcion.getSensorDer().size(); i++) {
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna() + (i + 1));

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de la izquierda
        for (int i = 0; i < percepcion.getSensorIzq().size(); i++) {
            Integer valorCelda = percepcion.getSensorArriba().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna() - (i + 1));

            actualizarSensores(posicion, valorCelda);
        }

        cantZombies = percepcion.getCantidadZombies();
        soles = percepcion.getEnergiaSoles();
    }


    /**
     * Funcion genérica para analizar los sensores y actualizar el estado del agente
     *
     * @param posicionInformada
     * @param valorInformado
     */
    private void actualizarSensores(Posicion posicionInformada, Integer valorInformado) {
        int valorCelda = this.tablero[posicionInformada.getFila()][posicionInformada.getColumna()];

        if (valorCelda != valorInformado) {
            if (valorInformado > 0) {
                actualizarListaGirasol(posicionInformada, valorInformado);
            }

            if (valorInformado == 0) {
                //En esta posición había un girasol que fue eliminado por un zombie
                if (valorCelda > 0) {
                    this.girasoles.removeIf(girasol -> girasol.getPosicion() == posicionInformada);
                } else {
                    //Se escapó un zombie y tengo que "moverlo" manualmente
                    Posicion posicionAux = new Posicion(posicionInformada.getFila(), posicionInformada.getColumna() - 1);
                    this.zombies.stream().filter(zombie -> zombie.getPosicion() == posicionInformada).findFirst().get().setPosicion(posicionAux);

                    //Actualizo el tablero en este punto para el movimiento manual
                    tablero[posicionAux.getFila()][posicionAux.getColumna()] = valorCelda;
                }
            }
            if (valorInformado < 0) {
                //valorCelda == 0? en ese casillero no había nada antes, ahora tengo un zombie
                if (valorCelda == 0) {
                    agregarZombie(posicionInformada, valorInformado);
                }

                //valorCelda > 0? antes tenia un girasol, ahora tengo un zombie que mató al girasol
                if (valorCelda > 0) {
                    //Quitar al girasol de la lista
                    this.girasoles.removeIf(girasol -> girasol.getPosicion() == posicionInformada);
                    //Agregar al zombie a la lista
                    agregarZombie(posicionInformada, valorInformado);
                }

                //valorCelda < 0? era otro zombie que se movió y ahora estoy viendo un zombie diferente
                if (valorCelda < 0) {
                    //"muevo" el zombie que estaba anteriormente en esa posición
                    Posicion posAux = new Posicion(posicionInformada.getFila(), posicionInformada.getColumna() - 1);
                    agregarZombie(posAux, tablero[posicionInformada.getFila()][posicionInformada.getColumna()]);

                    //agrego el nuevo zombie detectado
                    agregarZombie(posicionInformada, valorInformado);
                }
            }
            //Actualizo el valor del tablero
            this.tablero[posicionInformada.getFila()][posicionInformada.getColumna()] = valorInformado;
        }
    }

    /**
     * Esta función agrega un zombie nuevo o actualiza la posición de un zombie preexistente.
     * Verifica si en la fila de percepción ya existe un zombie con ese poder.
     * Si no existe, lo agrega. Si existe, actualiza su posición.
     *
     * @param posicionInformada
     * @param valorInformado
     */
    private void agregarZombie(Posicion posicionInformada, Integer valorInformado) {
        Optional<Zombie> zombieAux = this.zombies.stream().filter(zombie -> zombie.getPosicion().getFila() == posicionInformada.getFila())
                .filter(zombiesFila -> zombiesFila.getPoder() == valorInformado).findFirst();
        if (zombieAux.isPresent()) {
            //Actualizo el tablero y la posición del zombie
            Posicion posAux = zombieAux.get().getPosicion();
            tablero[posAux.getFila()][posAux.getColumna()] = 0;
            zombieAux.get().setPosicion(posicionInformada);
        } else {
            //Es un zombie nuevo, lo agrego
            this.zombies.add(new Zombie(posicionInformada, valorInformado));
        }
    }

    private void actualizarListaGirasol(Posicion posicion, Integer valorCelda) {
        this.girasoles.stream().filter(girasol -> girasol.getPosicion() == posicion).findFirst().get().setCantSoles(valorCelda);
    }

    /**
     * Método opcional para inicializar el estado del agente
     */
    @Override
    public void initState() {
        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero[0].length; col++) {
                tablero[row][col] = Percepcion.EMPTY_PERCEPTION;
            }
        }
        this.setFila(1);
        this.setColumna(1);
        this.setSoles(20);
    }

    /**
     * Este método retorna una representación del estado del agente mediante String
     */
    @Override
    public String toString() {
        String str = "";

        str = str + " posicion=\"(" + getFila() + "," + "" + getColumna() + ")\"";
        str = str + " soles=\"" + soles + "\"\n";

        str = str + "tablreo=\"[ \n";
        for (int row = 0; row < tablero.length; row++) {
            str = str + "[ ";
            for (int col = 0; col < tablero[0].length; col++) {
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
     * Este método es usado en el proceso de búsqueda para verificar si el nodo ya existe
     * en la búsqueda actual
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EstadoAgente))
            return false;

        int[][] tableroObj = ((EstadoAgente) obj).getTablero();
        Posicion posicionObj = ((EstadoAgente) obj).getPosicion();

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero[0].length; col++) {
                if (tablero[row][col] != tableroObj[row][col]) {
                    return false;
                }
            }
        }
        if (posicion.getFila() != posicionObj.getFila() || posicion.getColumna() != posicionObj.getColumna())
            return false;
        return true;
    }

    // Métodos específicos del agente Planta:

    public int[][] getTablero() {
        return tablero;
    }

    public int getPosicionTablero(int row, int col) {
        return tablero[row][col];
    }

    public void setPosicionTablero(int row, int col, int value) {
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

    public void setSoles(int soles) {
        this.soles = soles;
    }

    public List<Girasol> getGirasoles() {
        return girasoles;
    }

    public List<Zombie> getZombies() {
        return zombies;
    }

    public boolean noHayMasZombies() {
        return cantZombies == 0;
    }
}

