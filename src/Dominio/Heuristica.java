package Dominio;

import Auxiliares.Auxiliar;
import frsf.cidisi.faia.solver.search.IEstimatedCostFunction;
import frsf.cidisi.faia.solver.search.IStepCostFunction;
import frsf.cidisi.faia.solver.search.NTree;

public class Heuristica  implements IEstimatedCostFunction {

    @Override
    public double getEstimatedCost(NTree node) {

        EstadoAgente estadoAgente= (EstadoAgente) node.getAgentState();
        double costoResponse =0.0;
        if(estadoAgente.getCantZombies()>0){
            for (Zombie z: estadoAgente.getZombies()) {
                int distanciaCasa = 9- z.getPosicion().getColumna();
                int distanciaPlanta = Auxiliar.calcularDistancia(estadoAgente.getPosicionAgente(), z.getPosicion());
                costoResponse += distanciaPlanta + 10*distanciaCasa;
            }
            return costoResponse;
        }
        return 0.0;
    }
}
