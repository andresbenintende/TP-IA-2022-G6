package Dominio;

import frsf.cidisi.faia.solver.search.IStepCostFunction;
import frsf.cidisi.faia.solver.search.NTree;

public class Heuristica  implements IStepCostFunction {
    @Override
    public double calculateCost(NTree node) {
        EstadoAgente estadoAgente = ((EstadoAgente) node.getAgentState());
        return estadoAgente.getSoles();
    }
}
