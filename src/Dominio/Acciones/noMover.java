package Dominio.Acciones;

import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class noMover extends SearchAction {
    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {

        EstadoAgente estadoAgente = (EstadoAgente) s;
        Posicion posicion = new Posicion(estadoAgente.getPosicionAgente());

        AccionAuxiliar accionAux = new AccionAuxiliar();

        estadoAgente.getCeldasVisitadas().add(posicion);

        accionAux.executeAux(estadoAgente,posicion);
        if(estadoAgente.getSoles() < 1)
            return null;
        return estadoAgente;
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {
        EstadoAmbiente estadoAmbiente = (EstadoAmbiente) est;
        EstadoAgente estadoAgente = ((EstadoAgente) ast);

        //------------------------------------------------------------------------------------------------------
        //Cuando se decide ejecutar la acción, se realizan las modificaciones en duplicado para ambos estados
        Posicion posicion = estadoAgente.getPosicionAgente();

        AccionAuxiliar accionAux = new AccionAuxiliar();
        //Se pasa la misma posición porque la planta no se mueve
        return accionAux.executeAux(estadoAgente, estadoAmbiente,posicion, posicion);
    }

    @Override
    public Double getCost() {
        return 0.0;
    }

    @Override
    public String toString() {
        return "noMover";
    }
}
