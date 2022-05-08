package Dominio;

import frsf.cidisi.faia.solver.search.IEstimatedCostFunction;
import frsf.cidisi.faia.solver.search.NTree;

public class Heuristica  implements IEstimatedCostFunction {
    @Override
    public double getEstimatedCost(NTree node) {
        EstadoAgente estadoAgente = (EstadoAgente) node.getAgentState();
        return estadoAgente.getSoles();
    }
}
