package Dominio;

import frsf.cidisi.faia.agent.search.GoalTest;
import frsf.cidisi.faia.state.AgentState;

/**
 * This class defines the 'isGoalState' method. It has the responsability of,
 * given the agent state, verify if it is a goal state. It is used by the
 * search process of the agent and by the simulator, to know when to stop.
 */
public class AgenteGoal extends GoalTest {

    @Override
    public boolean isGoalState(AgentState agentState) {
        if (((PacmanAgentState) agentState).isNoMoreFood() &&
                ((PacmanAgentState) agentState).isAllWorldKnown()) {
            return true;
        }
        return false;
    }
}
