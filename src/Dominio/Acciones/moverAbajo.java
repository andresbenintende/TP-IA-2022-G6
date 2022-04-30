package Dominio.Acciones;

import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public class moverAbajo extends SearchAction {

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoAgente estadoAgente = (EstadoAgente) s;

        Posicion posicion = new Posicion(estadoAgente.getPosicionAgente());

        //Chequeo si estoy en la fila de más abajo o hay un zombie, no puedo moverme. Devuelvo null
        if(posicion.getFila() == 5 || estadoAgente.getPosicionTablero(posicion.getFila()+1,posicion.getColumna()) < 0)
            return null;

        //Si hay un zombie en la fila actual, no se cambia de fila
        if(estadoAgente.getZombies().stream().anyMatch(zombie -> zombie.getPosicion().getFila() == posicion.getFila()))
            return null;

        posicion.setFila(posicion.getFila()+1);
        //Si ya la visitó, no vuelve
        if(estadoAgente.getCeldasVisitadas().stream().anyMatch(pos -> pos.checkPosicion(posicion)))
            return null;
        else
            estadoAgente.getCeldasVisitadas().add(posicion);
        estadoAgente.setFila(posicion.getFila());
        estadoAgente.setSeMueve(true);

        AccionAuxiliar accionAux = new AccionAuxiliar();
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
        Posicion posicionActual = new Posicion(estadoAgente.getPosicionAgente());
        Posicion posicionNueva = new Posicion(0,0);

        //Chequeo si estoy en la fila de más abajo, no puedo moverme. Devuelvo null
        if(posicionActual.getFila() == 5){
            return estadoAmbiente;
        }

        //Actualizo posicion de la planta
        posicionNueva.setFila(posicionActual.getFila()+1);
        posicionNueva.setColumna(posicionActual.getColumna());
        estadoAgente.setFila(posicionActual.getFila());

        AccionAuxiliar accionAux = new AccionAuxiliar();
        return accionAux.executeAux(estadoAgente, estadoAmbiente,posicionActual, posicionNueva);
    }
    @Override
    public Double getCost() {
        return 1.0;
    }
    @Override
    public String toString() {
        return "moverAbajo";
    }
}
