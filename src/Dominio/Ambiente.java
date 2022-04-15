package Dominio;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.EnvironmentState;

public class Ambiente extends Environment{

    public Ambiente() {
        // Create the environment state
        this.environmentState = new EstadoAmbiente();
    }

    @Override
    public EstadoAmbiente getEnvironmentState() {
        return (EstadoAmbiente) super.getEnvironmentState();
    }

    /**
     * This method is called by the simulator. Given the Agent, it creates
     * a new perception reading, for example, the agent position.
     * @return A perception that will be given to the agent by the simulator.
     */
    @Override
    public Perception getPercept() {
        //Actualizar posición de los zombies
        moverZombies();

        // Create a new perception to return
        Percepcion perception = new Percepcion();

        // Get the actual position of the agent to be able to create the
        // perception
        Posicion pos = this.getEnvironmentState().getPosicionPlanta();
                // Set the perception sensors
        perception.setSensorArriba(this.getCeldaArriba(pos));
        perception.setSensorIzq(this.getCeldaIzq(pos));
        perception.setSensorDer(this.getCeldaDer(pos));
        perception.setSensorAbajo(this.getCeldaAbajo(pos));

        // Return the perception
        return perception;
    }

    private void moverZombies() {
        int[][] tablero = ((EstadoAmbiente)this.environmentState).getTablero();

    }

    @Override
    public String toString() {
        return environmentState.toString();
    }

    @Override
    public boolean agentFailed(Action actionReturned) {

        EstadoAmbiente estadoAmbiente =
                this.getEnvironmentState();

        int energiaPlanta = estadoAmbiente.getSolesPlanta();

        
        // Si la planta se queda sin energía, entonces falla
        if (energiaPlanta <= 0)
            return true;
        
        //Si un zombie traspasa la linea de meta, el juego termina
        for (Zombie zombie: estadoAmbiente.getZombies()) {
            if(zombie.getPosicion().getColumna() < 0)
                return true;
        }

        return false;
    }


    public int getCeldaArriba(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState)
                .getCeldaArriba(pos);
    }

    public int getCeldaIzq(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState)
                .getCeldaIzq(pos);
    }

    public int getCeldaDer(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState)
                .getCeldaDer(pos);
    }

    public int getCeldaAbajo(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState)
                .getCeldaAbajo(pos);
    }
}
