package Dominio;

import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

public class Percepcion extends Perception {

    public static int UNKNOWN_PERCEPTION = -1;
    public static int EMPTY_PERCEPTION = 0;
    public static int ENEMY_PERCEPTION = 1;
    public static int FOOD_PERCEPTION = 2;

    private int sensorIzq;
    private int sensorArriba;
    private int sensorDer;
    private int sensorAbajo;
    private int energiaSoles;

    public Percepcion() {
        energiaSoles = 20;
    }

    public Percepcion(Agent agent, Environment environment) {
        super(agent, environment);
    }

    /**
     * This method is used to setup the perception.
     */
    @Override
    public void initPerception(Agent agent, Environment environment) {
      //  PacmanAgent pacmanAgent = (PacmanAgent) agent;
        Ambiente ambiente = (Ambiente) environment;
        EstadoAmbiente estadoAmbiente =
                ambiente.getEnvironmentState();

        Posicion pos = estadoAmbiente.getPosicionPlanta();

        this.setSensorArriba(estadoAmbiente.getCeldaArriba(pos));
        this.setSensorIzq(estadoAmbiente.getCeldaIzq(pos));
        this.setSensorDer(estadoAmbiente.getCeldaDer(pos));
        this.setSensorAbajo(estadoAmbiente.getCeldaAbajo(pos));
    }

    // The following methods are Pacman-specific:

    public int getSensorIzq() {
        return sensorIzq;
    }

    public void setSensorIzq(int sensorIzq) {
        this.sensorIzq = sensorIzq;
    }

    public int getSensorArriba() {
        return sensorArriba;
    }

    public void setSensorArriba(int sensorArriba) {
        this.sensorArriba = sensorArriba;
    }

    public int getSensorDer() {
        return sensorDer;
    }

    public void setSensorDer(int sensorDer) {
        this.sensorDer = sensorDer;
    }

    public int getSensorAbajo() {
        return sensorAbajo;
    }

    public void setSensorAbajo(int sensorAbajo) {
        this.sensorAbajo = sensorAbajo;
    }

    public int getEnergiaSoles() {
        return energiaSoles;
    }

    public void setEnergiaSoles(int energiaSoles) {
        this.energiaSoles = energiaSoles;
    }

    @Override
    public String toString() {
        StringBuffer str = new StringBuffer();

        str.append("Soles: " + this.energiaSoles);
        str.append("; ");
        str.append("Sensor Izquierdo: " + this.sensorIzq);
        str.append("; ");
        str.append("Sensor Arriba: " + this.sensorArriba);
        str.append("; ");
        str.append("Sensor Derecho: " + this.sensorDer);
        str.append("; ");
        str.append("Sensor Abajo: " + this.sensorAbajo);

        return str.toString();
    }
}
