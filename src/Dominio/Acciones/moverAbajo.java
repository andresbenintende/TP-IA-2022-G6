package Dominio.Acciones;

import Auxiliares.Auxiliar;
import Dominio.EstadoAgente;
import Dominio.Girasol;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

import java.util.List;

public class moverAbajo extends SearchAction {

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoAgente estadoAgente = (EstadoAgente) s;

        Posicion posicion = estadoAgente.getPosicion();

        //Chequeo si estoy en la fila de más abajo, no puedo moverme. Devuelvo null
        if(posicion.getFila() == 5)
            return null;

        posicion.setFila(posicion.getFila()+1);

        estadoAgente.setFila(posicion.getFila());

        //Una vez que logra moverse, ejecuta las acciones auxiliares
        int valorCelda = (estadoAgente.getTablero()[posicion.getFila()][posicion.getColumna()]);
        //Hay un girasol, entonces toma sus soles
        if(valorCelda > 0){
            estadoAgente.setSoles(estadoAgente.getSoles() + valorCelda);
        }
        //Busco zombies en las posiciones adyacentes
        List<Integer> celdasAdyacentes = Auxiliar.getAdyacentes(estadoAgente.getTablero(), posicion);

        //Para identificar facilmente la posición del zombie adyacente, seteo la posición auxiliar
        //sabiendo de antemando que las celdas adyacentes se ordenan siempre de la misma manera:
        //izquierda-arriba-derecha-abajo
        for(int celdaAdy : celdasAdyacentes){
            Posicion posAux = new Posicion(posicion.getFila(), posicion.getColumna());
            switch (celdasAdyacentes.indexOf(celdaAdy)) {
                case 0 -> //Izquierda
                        posAux.setColumna(posicion.getColumna() - 1);
                case 1 -> //Arriba
                        posAux.setFila(posicion.getFila() - 1);
                case 2 -> //Derecha
                        posAux.setColumna(posicion.getColumna() + 1);
                case 3 -> //Abajo
                        posAux.setFila(posicion.getFila() + 1);
                default -> {}
            }
            if(celdaAdy < 0 && estadoAgente.getSoles()+celdaAdy > 0){
                //Ocupo los soles necesarios para eliminar al zombie
                estadoAgente.setSoles(estadoAgente.getSoles() + celdaAdy);
                //matar zombie
                estadoAgente.getZombies().removeIf(zombie -> zombie.getPosicion() == posAux);
            }
        }

        //Siembro girasol
        if(estadoAgente.getSoles() > 1) {
            //Ocupo un sol para sembrar un girasol
            estadoAgente.setSoles(estadoAgente.getSoles()-1);
            //Añado el girasol a la lista de girasoles del agente
            estadoAgente.getGirasoles().add(new Girasol(posicion, 0));
        }

        return estadoAgente;
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {

        return null;
    }
    @Override
    public Double getCost() {
        return null;
    }
    @Override
    public String toString() {
        return null;
    }
}
