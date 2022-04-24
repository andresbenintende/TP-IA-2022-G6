package Dominio;

import frsf.cidisi.faia.solver.search.IStepCostFunction;
import frsf.cidisi.faia.solver.search.NTree;

public class FuncionCosto implements IStepCostFunction {

    /**
     * This method calculates the cost of the given NTree node.
     */
    @Override
    public double calculateCost(NTree node) {
        EstadoAgente estadoAgente = ((EstadoAgente) node.getAgentState());
        return estadoAgente.getSoles();
    }

}
