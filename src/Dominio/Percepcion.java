package Dominio;

import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;

import java.util.ArrayList;
import java.util.List;

public class Percepcion extends Perception {

    public static int EMPTY_PERCEPTION = 0;

    private List<Integer> sensorIzq;
    private List<Integer> sensorArriba;
    private List<Integer> sensorDer;
    private List<Integer> sensorAbajo;
    private int energiaSoles;

    public Percepcion() {

        energiaSoles = 20;
        this.sensorIzq = new ArrayList<>();
        this.sensorArriba = new ArrayList<>();
        this.sensorDer = new ArrayList<>();
        this.sensorAbajo = new ArrayList<>();

    }

    public Percepcion(Agent agent, Environment environment) {
        super(agent, environment);
    }

    /**
     * Este método es usado para inicializar las percepciones
     */
    @Override
    public void initPerception(Agent agent, Environment environment) {
      //  Agente planta = (Agente) agent;
        Ambiente ambiente = (Ambiente) environment;
        EstadoAmbiente estadoAmbiente =
                ambiente.getEnvironmentState();

        Posicion pos = estadoAmbiente.getPosicionPlanta();

        this.setSensorArriba(estadoAmbiente.getCeldasArriba(pos));
        this.setSensorIzq(estadoAmbiente.getCeldasIzq(pos));
        this.setSensorDer(estadoAmbiente.getCeldasDer(pos));
        this.setSensorAbajo(estadoAmbiente.getCeldasAbajo(pos));
    }

    public List<Integer> getSensorIzq() {
        return sensorIzq;
    }

    public void setSensorIzq(List<Integer> sensorIzq) {
        this.sensorIzq = sensorIzq;
    }

    public List<Integer> getSensorArriba() {
        return sensorArriba;
    }

    public void setSensorArriba(List<Integer> sensorArriba) {
        this.sensorArriba = sensorArriba;
    }

    public List<Integer> getSensorDer() {
        return sensorDer;
    }

    public void setSensorDer(List<Integer> sensorDer) {
        this.sensorDer = sensorDer;
    }

    public List<Integer> getSensorAbajo() {
        return sensorAbajo;
    }

    public void setSensorAbajo(List<Integer> sensorAbajo) {
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

        str.append("Soles: ").append(this.energiaSoles);
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
