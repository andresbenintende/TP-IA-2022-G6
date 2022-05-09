/**
 * Basado en el simulador SearchBasedAgentSimulator de FAIA
 * Este incluye interacción con la UI para mostrar la evolución de la simulación
 * y los resultados de la misma
 * Andrés Benintende | UTN FRSF - INTELIGENCIA ARTIFICIAL 2022
 */
package Dominio;

import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.GoalBasedAgent;
import frsf.cidisi.faia.agent.search.GoalTest;
import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.AgentState;

import java.util.Arrays;
import java.util.Vector;

public class Simulador extends SimuladorAbstract{

    public Simulador(Environment environment, Vector<Agent> agents) {
        super(environment, agents);
    }

    public Simulador(Environment environment, Agent agent) {
        this(environment, new Vector<Agent>(Arrays.asList(agent)));
    }

    @Override
    public boolean agentSucceeded(Action actionReturned) {

        SearchBasedAgent sa = (SearchBasedAgent) getAgents().firstElement();
        Problem p = sa.getProblem();
        GoalTest gt = p.getGoalState();
        AgentState aSt = p.getAgentState();

        return gt.isGoalState(aSt);
    }

    @Override
    public boolean agentFailed(Action actionReturned) {
        return this.environment.agentFailed(actionReturned);
    }

    @Override
    public String getSimulatorName() {
        return "Simulador Basado en Busqueda - Grupo 6 IA 2022";
    }

    void showSolution() {
        GoalBasedAgent agent = (GoalBasedAgent) this.getAgents().firstElement();

        agent.getSolver().showSolution();
    }

    @Override
    public void actionReturned(Agent agent, Action action) {
        this.updateState(action);
        this.showSolution();
    }
}
