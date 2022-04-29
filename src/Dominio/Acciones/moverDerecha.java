package Dominio.Acciones;

import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class moverDerecha extends SearchAction {

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoAgente estadoAgente = (EstadoAgente) s;

        Posicion posicion = new Posicion(estadoAgente.getPosicionAgente());

        //Chequeo si estoy en la columna de más a la derecha, no puedo moverme. Devuelvo null
        if(posicion.getColumna() == 9)
            return null;

        posicion.setColumna(posicion.getColumna()+1);
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
        Posicion posicionActual = estadoAgente.getPosicionAgente();
        Posicion posicionNueva = new Posicion(0,0);

        //Chequeo si estoy en la columna más a la derecha, no puedo moverme. Devuelvo null
        if(posicionActual.getColumna() == 9){
            return estadoAmbiente;
        }

        //Actualizo posicion de la planta
        posicionNueva.setColumna(posicionActual.getColumna()+1);
        estadoAgente.setColumna(posicionActual.getColumna());

        AccionAuxiliar accionAux = new AccionAuxiliar();
        return accionAux.executeAux(estadoAgente, estadoAmbiente,posicionActual,posicionNueva);
    }

    @Override
    public Double getCost() {
        return 0.0;
    }
    @Override
    public String toString() {
        return "moverDerecha";
    }
}
