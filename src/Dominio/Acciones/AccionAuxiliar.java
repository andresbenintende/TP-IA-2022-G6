package Dominio.Acciones;

import Auxiliares.Auxiliar;
import Dominio.EstadoAgente;
import Dominio.EstadoAmbiente;
import Dominio.Girasol;
import Dominio.Posicion;

import java.util.List;
import java.util.Optional;

public class AccionAuxiliar {

    public EstadoAgente executeAux(EstadoAgente estadoAgente, Posicion posicion){
        //Ejecuta las acciones auxiliares
        //valor del casillero al que la planta se moverá
        int valorCelda = (estadoAgente.getTablero()[posicion.getFila()][posicion.getColumna()]);

        if (valorCelda < 0) {
            int solesQuitados = valorCelda * 2;         //solesQuitados valor negativo
            estadoAgente.setSoles(estadoAgente.getSoles() + solesQuitados);
            estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), valorCelda);
        }

        //Hay un girasol, entonces toma sus soles
        if (valorCelda > 0) {
            estadoAgente.setSoles(estadoAgente.getSoles() + valorCelda);
            estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
            estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), 0);
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
                estadoAgente.getZombies().removeIf(zombie -> zombie.checkPosicion(posZombie));
                estadoAgente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
            }
        }

        //Siembro girasol
        if (estadoAgente.getSoles() > 1) {
            if (!estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().isPresent()) {
                //Ocupo un sol para sembrar un girasol
                estadoAgente.setSoles(estadoAgente.getSoles() - 1);
                //Añado el girasol a la lista de girasoles del agente
                estadoAgente.getGirasoles().add(new Girasol(posicion, 0));
            }
        }
        return estadoAgente;
    }

    public EstadoAmbiente executeAux(EstadoAgente estadoAgente, EstadoAmbiente estadoAmbiente, Posicion posicion){
        //Una vez que logra moverse (o no), ejecuta las acciones auxiliares
        //valor del casillero al que la planta se moverá
        int valorCelda = (estadoAgente.getTablero()[posicion.getFila()][posicion.getColumna()]);

        //Pierdo soles vs. zombie
        if (valorCelda < 0) {
            int solesQuitados = valorCelda * 2;
            int solesPlantaActualizados = estadoAgente.getSoles() + solesQuitados;

            //Actualizo el estado del agente
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
            //Si en la posicion hay un girasol y tiene soles
            Optional<Girasol> auxGirasol = estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst();
            if (auxGirasol.isPresent() && auxGirasol.get().getCantSoles() > 0) {
                //Agente
                estadoAgente.setSoles(estadoAgente.getSoles() + auxGirasol.get().getCantSoles());
                estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
                //Actualizo el tablero del agente
                estadoAgente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), estadoAgente.getSoles());

                //Ambiente
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
                estadoAmbiente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().get().setCantSoles(0);
                //Actualizo el tablero del ambiente
                estadoAmbiente.setPosicionTablero(posicion.getFila(), posicion.getColumna(), estadoAgente.getSoles());
            }
        }

        //Busco zombies en las posiciones adyacentes
        List<Integer> celdasAdyacentes = Auxiliar.getAdyacentes(estadoAgente.getTablero(), posicion);

        //Para identificar fácilmente la posición del zombie adyacente, seteo la posición auxiliar
        //sabiendo de antemano que las celdas adyacentes se ordenan siempre de la misma manera:
        //izquierda-arriba-derecha-abajo
        for (int celdaAdy : celdasAdyacentes) {
            //Si hay un zombie y suficiente soles
            if (celdaAdy < 0 && estadoAgente.getSoles() + celdaAdy > 0) {
                Posicion posZombie = Auxiliar.getPosicionZombie(posicion, celdasAdyacentes.indexOf(celdaAdy));

                //Actualizo el estado del AGENTE
                estadoAgente.setSoles(estadoAgente.getSoles() + celdaAdy);
                estadoAgente.getZombies().removeIf(zombie -> zombie.checkPosicion(posZombie));
                //Actualizo el tablero
                estadoAgente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
                estadoAgente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles());

                //Actualizo el estado del AMBIENTE
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
                estadoAmbiente.getZombies().removeIf(zombie -> zombie.checkPosicion(posZombie));
                //Actualizo el tablero
                estadoAmbiente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
                estadoAmbiente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles());
            }
        }

        //Siembro girasol
        if (estadoAgente.getSoles() > 1) {
            //Actualizo el estado del AGENTE
            //Ocupo un sol para sembrar un girasol
            estadoAgente.setSoles(estadoAgente.getSoles() - 1);
            //Añado el girasol a la lista de girasoles del agente
            if (!estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().isPresent()) {
                estadoAgente.getGirasoles().add(new Girasol(posicion, 0));
            }
            //Actualizo el tablero
            estadoAgente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles());

            //Actualizo el estado del AMBIENTE
            estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
            if (!estadoAmbiente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicion)).findFirst().isPresent()) {
                estadoAmbiente.getGirasoles().add(new Girasol(posicion, 0));
            }
            //Actualizo el tablero
            estadoAmbiente.setPosicionTablero(estadoAgente.getFila(), estadoAgente.getColumna(), estadoAgente.getSoles());
        }
        return estadoAmbiente;
    }
}
