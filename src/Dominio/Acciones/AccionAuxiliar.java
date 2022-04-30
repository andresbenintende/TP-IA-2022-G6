package Dominio.Acciones;

import Auxiliares.Auxiliar;
import Dominio.*;

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

    public EstadoAmbiente executeAux(EstadoAgente estadoAgente, EstadoAmbiente estadoAmbiente, Posicion posicionActual, Posicion posicionNueva){
        //Una vez que logra moverse (o no), ejecuta las acciones auxiliares
        //valor del casillero al que la planta se moverá
        int valorCelda = (estadoAgente.getTablero()[posicionNueva.getFila()][posicionNueva.getColumna()]);
        //Seteo la posición del agente en los estados
        estadoAgente.setFila(posicionNueva.getFila());
        estadoAgente.setColumna(posicionNueva.getColumna());
        estadoAmbiente.setPosicionPlanta(posicionNueva);

        //Pierdo soles vs. zombie
        if (valorCelda < 0) {
            int solesQuitados = valorCelda * 2;
            int solesPlantaActualizados = estadoAgente.getSoles() + solesQuitados;

            //Actualizo la energía del agente
            estadoAgente.setSoles(solesPlantaActualizados);

            estadoAmbiente.setSolesPlanta(solesPlantaActualizados);
        }

        //Hay un girasol, entonces toma sus soles
        if (valorCelda > 0) {
            //Si en la posicion hay un girasol y tiene soles
            Optional<Girasol> auxGirasol = estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicionNueva)).findFirst();
            if (auxGirasol.isPresent() && auxGirasol.get().getCantSoles() > 0) {
                //Agente
                estadoAgente.setSoles(estadoAgente.getSoles() + auxGirasol.get().getCantSoles());
                estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicionNueva)).findFirst().get().setCantSoles(0);
                estadoAgente.setPosicionTablero(auxGirasol.get().getPosicion().getFila(),auxGirasol.get().getPosicion().getColumna(),0);
                //Ambiente
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
                estadoAmbiente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicionNueva)).findFirst().get().setCantSoles(0);
                estadoAmbiente.setPosicionTablero(auxGirasol.get().getPosicion().getFila(),auxGirasol.get().getPosicion().getColumna(),0);
            }
        }

        //Busco zombies en las posiciones adyacentes
        List<Integer> celdasAdyacentes = Auxiliar.getAdyacentes(estadoAgente.getTablero(), posicionNueva);

        //Para identificar fácilmente la posición del zombie adyacente, seteo la posición auxiliar
        //sabiendo de antemano que las celdas adyacentes se ordenan siempre de la misma manera:
        //izquierda-arriba-derecha-abajo
        for (int celdaAdy : celdasAdyacentes) {
            //Si hay un zombie y suficiente soles
            if (celdaAdy < 0 && estadoAgente.getSoles() + celdaAdy > 0) {
                Posicion posZombie = Auxiliar.getPosicionZombie(posicionNueva, celdasAdyacentes.indexOf(celdaAdy));

                //Actualizo el estado del AGENTE
                estadoAgente.setSoles(estadoAgente.getSoles() + celdaAdy);
                estadoAgente.getZombies().removeIf(zombie -> zombie.checkPosicion(posZombie));
                estadoAgente.setCantZombies(estadoAgente.getCantZombies()-1);
                //Actualizo el tablero
                estadoAgente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);

                //Actualizo el estado del AMBIENTE
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
                estadoAmbiente.getZombies().removeIf(zombie -> zombie.checkPosicion(posZombie));
                //Actualizo el tablero
                estadoAmbiente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), 0);
            }
        }

        //Siembro girasol sólo si tengo energía suficiente y si no tengo detectado ningún zombie
        if (estadoAgente.getSoles() > 1 && estadoAgente.getZombies().size() == 0) {
            //Actualizo el estado del AGENTE
            //Si no hay un girasol en la posición, añado el girasol a la lista
            if (!estadoAgente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicionNueva)).findFirst().isPresent()) {
                //Ocupo un sol para sembrar un girasol
                estadoAgente.setSoles(estadoAgente.getSoles() - 1);
                estadoAgente.getGirasoles().add(new Girasol(posicionNueva, 0));
            }

            //Actualizo el estado del AMBIENTE
            if (!estadoAmbiente.getGirasoles().stream().filter(girasol -> girasol.checkPosicion(posicionNueva)).findFirst().isPresent()) {
                estadoAmbiente.setSolesPlanta(estadoAgente.getSoles());
                estadoAmbiente.getGirasoles().add(new Girasol(posicionNueva, 0));
            }
        }

        //Actualizo el tablero en la posición anterior, si es que se movió
        if(!posicionNueva.checkPosicion(posicionActual)) {
            //Si en la posición anterior había un zombie, entonces actualizo el tablero al valor del poder del zombie
            //Sino, seteo el tablero en 0 (puede que haya un girasol, pero este se quedará sin soles porque los consumió el agente)
            Optional<Zombie> zombieAux = estadoAgente.getZombies().stream().filter(zombie -> zombie.checkPosicion(posicionNueva)).findFirst();
            if (zombieAux.isPresent()) {
                var posZombie = zombieAux.get().getPosicion();
                estadoAgente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), zombieAux.get().getPoder());
                estadoAmbiente.setPosicionTablero(posZombie.getFila(), posZombie.getColumna(), zombieAux.get().getPoder());
            }
            else {
                estadoAgente.setPosicionTablero(posicionActual.getFila(), posicionActual.getColumna(), 0);
                estadoAmbiente.setPosicionTablero(posicionActual.getFila(), posicionActual.getColumna(), 0);
            }
        }
        return estadoAmbiente;
    }
}
