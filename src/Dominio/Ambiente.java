package Dominio;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.EnvironmentState;

import java.util.List;

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
     * Este método es invocado por el simulador. Dado el agente, este crea
     * una nueva percepción leyendo, por ejemplo, la posición del agente.
     * @return Una percepción que el simulador entregará al agente
     */
    @Override
    public Perception getPercept() {
        //Actualizar posición de los zombies
        moverZombies();

        //Generar soles-girasol
        generarSolesGirasol();

        // Nueva percepción
        Percepcion perception = new Percepcion();

        //Obtener la posición real del agente para poder crear la percepción
        Posicion pos = this.getEnvironmentState().getPosicionPlanta();
        // Configurar los sensores de percepción
        perception.setSensorArriba(this.getCeldasArriba(pos));
        perception.setSensorIzq(this.getCeldasIzq(pos));
        perception.setSensorDer(this.getCeldasDer(pos));
        perception.setSensorAbajo(this.getCeldasAbajo(pos));
        perception.setCantidadZombies(this.getEnvironmentState().getCantidadZombies());
        perception.setEnergiaSoles(this.getEnvironmentState().getSolesPlanta());

        // Devuelve la percepción
        return perception;
    }

    private void generarSolesGirasol() {
        ((EstadoAmbiente)this.environmentState).generarSolesGirasol();
    }

    private void moverZombies() {
        ((EstadoAmbiente)this.environmentState).moverZombies();
    }

    @Override
    public String toString() {
        return environmentState.toString();
    }

    @Override
    public boolean agentFailed(Action actionReturned) {

        EstadoAmbiente estadoAmbiente =this.getEnvironmentState();

        int energiaPlanta = estadoAmbiente.getSolesPlanta();

        // Si la planta se queda sin energía, entonces falla
        if (energiaPlanta <= 0)
            return true;
        
        //Si un zombie traspasa la linea de meta, el juego termina
        for (Zombie zombie: estadoAmbiente.getZombies()) {
            if(zombie.getPosicion().getColumna() < 1)
                return true;
        }
        return false;
    }

    public List<Integer> getCeldasArriba(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState).getCeldasArriba(pos);
    }

    public List<Integer> getCeldasIzq(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState).getCeldasIzq(pos);
    }

    public List<Integer> getCeldasDer(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState).getCeldasDer(pos);
    }

    public List<Integer> getCeldasAbajo(Posicion pos) {
        return ((EstadoAmbiente) this.environmentState).getCeldasAbajo(pos);
    }
}
