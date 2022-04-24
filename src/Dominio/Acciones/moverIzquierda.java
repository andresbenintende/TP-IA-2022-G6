package Dominio.Acciones;

import Auxiliares.Auxiliar;
import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Girasol;
import Dominio.Posicion;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

import java.util.List;

public class moverIzquierda extends SearchAction {
    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) {
        EstadoAgente estadoAgente = (EstadoAgente) s;

        Posicion posicion = estadoAgente.getPosicion();

        //Chequeo si estoy en la columna de más a la derecha, no puedo moverme. Devuelvo null
        if (posicion.getColumna() == 1)
            return null;

        posicion.setColumna(posicion.getColumna() - 1);
        estadoAgente.setColumna(posicion.getColumna());

        //Una vez que logra moverse, ejecuta las acciones auxiliares
        //valor del cassillero al que la planta se movera
        int valorCelda = (estadoAgente.getTablero()[posicion.getFila()][posicion.getColumna()]);


        if (valorCelda < 0) {
            int solesQuitados = valorCelda * 2;         //solesQuitados valor negativo
            estadoAgente.setSoles(estadoAgente.getSoles() + solesQuitados);
        }

        //Hay un girasol, entonces toma sus soles
        if (valorCelda > 0) {
            estadoAgente.setSoles(estadoAgente.getSoles() + valorCelda);
            estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
        }
        //Busco zombies en las posiciones adyacentes
        List<Integer> celdasAdyacentes = Auxiliar.getAdyacentes(estadoAgente.getTablero(), posicion);

        //Para identificar fácilmente la posición del zombie adyacente, seteo la posición auxiliar
        //sabiendo de antemano que las celdas adyacentes se ordenan siempre de la misma manera:
        //izquierda-arriba-derecha-abajo
        for (int celdaAdy : celdasAdyacentes) {
            if (celdaAdy < 0 && estadoAgente.getSoles() + celdaAdy > 0) {

                Posicion posZombie = Auxiliar.getPosicionZombie(posicion, celdasAdyacentes.indexOf(celdaAdy));
                //Ocupo los soles necesarios para eliminar al zombie
                estadoAgente.setSoles(estadoAgente.getSoles() + celdaAdy);
                //matar zombie
                estadoAgente.getZombies().removeIf(zombie -> zombie.getPosicion() == posZombie);
            }
        }

        //Siembro girasol
        if (estadoAgente.getSoles() > 1) {
            //Ocupo un sol para sembrar un girasol
            estadoAgente.setSoles(estadoAgente.getSoles() - 1);
            //Añado el girasol a la lista de girasoles del agente
            estadoAgente.getGirasoles().add(new Girasol(posicion, 0));
        }

        return estadoAgente;
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {

        EstadoAmbiente estadoAmbiente = (EstadoAmbiente) est;
        EstadoAgente estadoAgente = ((EstadoAgente) ast);

        //------------------------------------------------------------------------------------------------------
        //Cuando se decide ejecutar la accion, se relizan las modificacions en duplicado para ambos estados
        Posicion posicion = estadoAgente.getPosicion();

        //Chequeo si estoy en la columna más a la derecha, no puedo moverme. Devuelvo null
        if (posicion.getColumna() == 1) {
            return estadoAmbiente;
        }

        //Actualizo posicion de la planta
        posicion.setColumna(posicion.getColumna() - 1);
        estadoAgente.setColumna(posicion.getColumna());

        //Una vez que logra moverse, ejecuta las acciones auxiliares
        //valor del cassillero al que la planta se movera
        int valorCelda = (estadoAgente.getTablero()[posicion.getFila()][posicion.getColumna()]);

        //Pierdo soles vs zombie
        if (valorCelda < 0) {
            int solesQuitados = valorCelda * 2;
            int solesPlantaActualizados = estadoAgente.getSoles() + solesQuitados;

            //actualizao el estado del agente
            estadoAgente.setSoles(solesPlantaActualizados);
            //Actualizo el tablero
            estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), solesPlantaActualizados);

            //Actualizo el estado del ambiente
            estadoAmbiente.setPosicionPlanta(posicion);
            estadoAmbiente.setSolesPlanta(solesPlantaActualizados);
            //Actualizo el tablero
            estadoAmbiente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), solesPlantaActualizados);


        }

        //Hay un girasol, entonces toma sus soles
        if (valorCelda > 0) {
            //Agente
            estadoAgente.setSoles(estadoAgente.getSoles() + valorCelda);
            estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
            //Actualizo el tablero
            estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), 0);

            //Ambiente
            estadoAmbiente.setSolesPlanta(estadoAgente.getSoles() + valorCelda);
            estadoAmbiente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
            //Actualizo el tablero
            estadoAmbiente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), 0);


        }

        //Busco zombies en las posiciones adyacentes
        List<Integer> celdasAdyacentes = Auxiliar.getAdyacentes(estadoAgente.getTablero(), posicion);

        //Para identificar fácilmente la posición del zombie adyacente, seteo la posición auxiliar
        //sabiendo de antemano que las celdas adyacentes se ordenan siempre de la misma manera:
        //izquierda-arriba-derecha-abajo
        for (int celdaAdy : celdasAdyacentes) {
            //Si hay un zoombie y suficiente soles
            if (celdaAdy < 0 && estadoAgente.getSoles() + celdaAdy > 0) {
                Posicion posZombie = Auxiliar.getPosicionZombie(posicion, celdasAdyacentes.indexOf(celdaAdy));

                //actualizao el estado del AGENTE

                estadoAgente.setSoles(estadoAgente.getSoles() + celdaAdy);
                estadoAgente.getZombies().removeIf(zombie -> zombie.getPosicion() == posZombie);
                //Actualizo el tablero
                estadoAgente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
                estadoAgente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles() + celdaAdy);


                //actualizao el estado del AMBIENTE
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles() + celdaAdy);
                estadoAmbiente.getZombies().removeIf(zombie -> zombie.getPosicion() == posZombie);
                //Actualizo el tablero
                estadoAmbiente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
                estadoAmbiente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles() + celdaAdy);
            }
        }

        //Siembro girasol
        if (estadoAgente.getSoles() > 1) {

            //actualizao el estado del AGENTE

            //Ocupo un sol para sembrar un girasol
            estadoAgente.setSoles(estadoAgente.getSoles() - 1);
            //Añado el girasol a la lista de girasoles del agente
            estadoAgente.getGirasoles().add(new Girasol(posicion, 0));
            //Actualizo el tablero
            estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), 0);
            estadoAgente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles() - 1);

            //actualizao el estado del AMBIENTE

            estadoAmbiente.setSolesPlanta(estadoAgente.getSoles() - 1);
            estadoAmbiente.getGirasoles().add(new Girasol(posicion, 0));
            //Actualizo el tablero
            estadoAmbiente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), 0);
            estadoAmbiente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles() - 1);
        }


        return estadoAmbiente;
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
