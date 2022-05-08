package Dominio;

import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Representa el estado interno del agente
 */
public class EstadoAgente extends SearchBasedAgentState {

    private int[][] tablero;
    private Posicion posicionAgente;
    private List<Posicion> celdasVisitadas;
    private int cantZombies;
    private List<Zombie> zombies;
    private List<Girasol> girasoles;
    private int soles;
    private boolean seMueve;

    public EstadoAgente(int[][] t, Posicion p, List<Posicion> v, int c, List<Zombie> z, List<Girasol> g, int s, boolean m) {
        tablero = t;
        posicionAgente = p;
        celdasVisitadas = v;
        cantZombies = c;
        zombies = z;
        girasoles = g;
        soles = s;
        seMueve = m;
    }

    public EstadoAgente() {
        tablero = new int[6][10]; //todo tablero
        posicionAgente = new Posicion(5, 1);
        celdasVisitadas = new ArrayList<>();
        cantZombies = 0;
        soles = 0;
        girasoles = new ArrayList<>();
        zombies = new ArrayList<>();
        seMueve = false;
        this.initState();
    }

    /**
     * Este método clona el estado del agente.
     * Se usa en el proceso de búsqueda cuando se crea el árbol de búsqueda
     */
    @Override
    public SearchBasedAgentState clone() {
        int[][] nuevoTablero = new int[6][10];

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero[0].length; col++) {
                nuevoTablero[row][col] = tablero[row][col];
            }
        }
        List<Zombie> nuevosZombies = new ArrayList<>();

        for(Zombie zombie : zombies){
                nuevosZombies.add(zombie.clone());
        }


        List<Girasol> nuevosGirasoles = new ArrayList<>();

        for(Girasol girasol : girasoles){
                nuevosGirasoles.add(girasol.clone());
        }

        Posicion nuevaPosicion = new Posicion(posicionAgente);

        List<Posicion> nuevasceldasVisitadas = new ArrayList<>();

        for (Posicion pos : celdasVisitadas
             ) {
            nuevasceldasVisitadas.add(new Posicion(pos.getFila(),pos.getColumna()));
        }

        EstadoAgente nuevoEstado = new EstadoAgente(nuevoTablero, nuevaPosicion, nuevasceldasVisitadas,  this.cantZombies, nuevosZombies, nuevosGirasoles, this.soles, this.seMueve);

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
            Posicion posicion = new Posicion(this.getFila() - (i+1), this.getColumna());

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de abajo
        for (int i = 0; i < percepcion.getSensorAbajo().size(); i++) {
            Integer valorCelda = percepcion.getSensorAbajo().get(i);
            Posicion posicion = new Posicion(this.getFila() + (i+1), this.getColumna());

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de la derecha
        for (int i = 0; i < percepcion.getSensorDer().size(); i++) {
            Integer valorCelda = percepcion.getSensorDer().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna() + (i+1));

            actualizarSensores(posicion, valorCelda);
        }

        //Actualizo sensor de la izquierda
        for (int i = 0; i < percepcion.getSensorIzq().size(); i++) {
            Integer valorCelda = percepcion.getSensorIzq().get(i);
            Posicion posicion = new Posicion(this.getFila(), this.getColumna() - (i+1));
            actualizarSensores(posicion, valorCelda);
        }

        cantZombies = percepcion.getCantidadZombies();
        soles = percepcion.getEnergiaAgente();
    }


    /**
     * Función genérica para analizar los sensores y actualizar el estado del agente
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
                    Optional<Zombie> zombieAux= this.zombies.stream().filter(zombie -> zombie.getPosicion() == posicionInformada).findFirst();
                    if(zombieAux.isPresent()) {zombieAux.get().setPosicion(posicionAux);}

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
        Optional<Girasol> girasol = this.girasoles.stream().filter(g -> g.checkPosicion(posicion)).findFirst();
        girasol.ifPresent(g -> g.setCantSoles(valorCelda));
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
       //TODO ESTABAMOS HARCODEANDO LA POSICION DE DEL AGENTE PLANTA AL INICIALIZARLA
        //  this.setFila(1);
       // this.setColumna(1);
       // this.setSoles(20);
    }

    /**
     * Este método retorna una representación del estado del agente mediante String
     */
    @Override
    public String toString() {
        String str = "";

        str = str + " posicion=\"(" + getFila() + "," + "" + getColumna() + ")\"";
        str = str + " soles=\"" + soles + "\"\n";

        str = str + "tablero=\"[ \n";
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
        Posicion posicionObj = ((EstadoAgente) obj).getPosicionAgente();

        for (int row = 0; row < tablero.length; row++) {
            for (int col = 0; col < tablero[0].length; col++) {
                if (tablero[row][col] != tableroObj[row][col]) {
                    return false;
                }
            }
        }
        if (posicionAgente.getFila() != posicionObj.getFila() || posicionAgente.getColumna() != posicionObj.getColumna())
            return false;

        if(this.getSoles() != ((EstadoAgente) obj).getSoles())
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

    public Posicion getPosicionAgente() {
        return posicionAgente;
    }

    public void setFila(int value) {
        this.posicionAgente.setFila(value);
    }

    public void setColumna(int value) {
        this.posicionAgente.setColumna(value);
    }

    public int getFila() {
        return posicionAgente.getFila();
    }

    public int getColumna() {
        return posicionAgente.getColumna();
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

    public int getCantZombies() { return cantZombies;}

    public void setCantZombies(int cant) {cantZombies = cant;}

    public boolean noHayMasZombies() {
        return ((zombies.size() == 0 && this.getSeMueve()) || cantZombies == 0);
    }

    public  boolean getSeMueve(){
        return this.seMueve;
    }
    public void setSeMueve(boolean movimiento) {
        this.seMueve = movimiento;
    }

    public List<Posicion> getCeldasVisitadas() {
        return celdasVisitadas;
    }

    public void setCeldasVisitadas(List<Posicion> celdasVisitadas) {
        this.celdasVisitadas = celdasVisitadas;
    }
}

