package Dominio.Acciones;

import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class moverIzquierda extends SearchAction {
    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoAgente estadoAgente = (EstadoAgente) s;

        Posicion posicion = new Posicion(estadoAgente.getPosicionAgente());

        //Chequeo si estoy en la columna de más a la derecha, no puedo moverme. Devuelvo null
        if (posicion.getColumna() == 1)
            return null;

        posicion.setColumna(posicion.getColumna() - 1);
        estadoAgente.setColumna(posicion.getColumna());
        estadoAgente.setSeMueve(true);

        AccionAuxiliar accionAux = new AccionAuxiliar();
        return accionAux.executeAux(estadoAgente,posicion);
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {

        EstadoAmbiente estadoAmbiente = (EstadoAmbiente) est;
        EstadoAgente estadoAgente = ((EstadoAgente) ast);

        //------------------------------------------------------------------------------------------------------
        //Cuando se decide ejecutar la acción, se realizan las modificaciones en duplicado para ambos estados
        Posicion posicion = estadoAgente.getPosicionAgente();

        //Chequeo si estoy en la columna más a la derecha, no puedo moverme. Devuelvo null
        if (posicion.getColumna() == 1) {
            return estadoAmbiente;
        }

        //Actualizo posicion de la planta
        posicion.setColumna(posicion.getColumna() - 1);
        estadoAgente.setColumna(posicion.getColumna());

        AccionAuxiliar accionAux = new AccionAuxiliar();
        return accionAux.executeAux(estadoAgente, estadoAmbiente, posicion);
    }

    @Override
    public Double getCost() {
        return 0.0;
    }
    @Override
    public String toString() {
        return "moverIzquierda";
    }
}
