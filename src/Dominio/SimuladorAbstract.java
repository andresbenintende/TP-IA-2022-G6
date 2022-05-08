package Dominio;

import java.util.Vector;

import UI.VentanaJuego;
import frsf.cidisi.faia.agent.GoalBasedAgent;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.simulator.Simulator;
import frsf.cidisi.faia.simulator.events.EventType;
import frsf.cidisi.faia.simulator.events.SimulatorEventNotifier;

public abstract class SimuladorAbstract extends Simulator {

    /**
     *
     * @param environment
     */

    VentanaJuego ventanaJuego;
    public SimuladorAbstract(Environment environment, Vector<Agent> agents) {
        super(environment, agents);

        ventanaJuego = new VentanaJuego((EstadoAmbiente) environment.getEnvironmentState());
        ventanaJuego.setVisible(true);
    }

    public SimuladorAbstract(Environment environment, Agent agent) {
        Vector<Agent> ags = new Vector<Agent>();
        ags.add(agent);

        this.environment = environment;
        this.agents = ags;
    }

    @Override
    public void start() {

        System.out.println("----------------------------------------------------");
        System.out.println("--- " + this.getSimulatorName() + " ---");
        System.out.println("----------------------------------------------------");
        System.out.println();

        Perception perception;
        Action action;
        GoalBasedAgent agent;

        agent = (GoalBasedAgent) this.getAgents().firstElement();

        /*
         * Simulation starts. The environment sends perceptions to the agent, and
         * it returns actions. The loop condition evaluation is placed at the end.
         * This works even when the agent starts with a goal state (see agentSucceeded
         * method in the SearchBasedAgentSimulator).
         */
        do {

            System.out.println("------------------------------------");

            System.out.println("Enviando percepcion al agente...");
            perception = this.getPercept();
            agent.see(perception);
            System.out.println("Percepcion: " + perception);

            System.out.println("Estado del Agente: " + agent.getAgentState());
            System.out.println("Ambiente: " + environment);

            System.out.println("Consultando al agente por una accion...");
            action = agent.selectAction();

            if (action == null) {
                break;
            }

            System.out.println("Accion a ejecutar: " + action);
            System.out.println();

            this.actionReturned(agent, action);

            ventanaJuego.actualizar((EstadoAmbiente) environment.getEnvironmentState(), null);
            ventanaJuego.repaint();

        } while (!this.agentSucceeded(action) && !this.agentFailed(action));

        // Check what happened, if agent has reached the goal or not.
        if (this.agentSucceeded(action)) {
            System.out.println("¡El agente alcanzo el objetivo!");
            ventanaJuego.actualizar((EstadoAmbiente) environment.getEnvironmentState(),true);
            ventanaJuego.repaint();
        } else {
            System.out.println("ERROR: La simulacion finalizo pero el agente no alcanzo el objetivo.");
            ventanaJuego.actualizar((EstadoAmbiente) environment.getEnvironmentState(),false);
            ventanaJuego.repaint();
        }

        // Leave a blank line
        System.out.println();

        // FIXME: This call can be moved to the Simulator class
        this.environment.close();

        // Launch simulationFinished event
        SimulatorEventNotifier.runEventHandlers(EventType.SimulationFinished, null);
    }

    /**
     * Here we update the state of the agent and the real state of the
     * simulator.
     * @param action
     */
    protected void updateState(Action action) {
        this.getEnvironment().updateState(((GoalBasedAgent) agents.elementAt(0)).getAgentState(), action);
    }

    public abstract boolean agentSucceeded(Action action);

    public abstract boolean agentFailed(Action action);

    /**
     * This method is executed in the mail loop of the simulation when the
     * agent returns an action.
     * @param agent
     * @param action
     */
    public abstract void actionReturned(Agent agent, Action action);

    /**
     * @return The name of the simulator, e.g. 'SearchBasedAgentSimulator'
     */
    public abstract String getSimulatorName();
}

