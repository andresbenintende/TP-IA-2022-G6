package Dominio;

import Auxiliares.DOMParser;
import Dominio.Acciones.*;
import frsf.cidisi.faia.agent.Perception;

import java.util.Vector;

import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.solver.search.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Agente extends SearchBasedAgent {

    Search busquedaSolver;
    int estrategia;

    public Agente() {

        DOMParser parser = new DOMParser();
        setEstrategiaBusqueda(parser.estrategiaBusquedaParser());

        // Prueba de meta del agente
        AgenteGoal goal = new AgenteGoal();

        // Estado del agente
        EstadoAgente estadoAgente = new EstadoAgente();

        this.setAgentState(estadoAgente);

        // Creación de los operadores
        Vector<SearchAction> operadores = new Vector<>();
        operadores.addElement(new moverArriba());
        operadores.addElement(new moverDerecha());
        operadores.addElement(new moverAbajo());
        operadores.addElement(new moverIzquierda());
        operadores.addElement(new noMover());

        // Creación del problema que deberá resolver el agente
        Problem problem = new Problem(goal, estadoAgente, operadores);
        this.setProblem(problem);
    }

    /**
     * Este método es ejecutado por el simulador para requerirle una acción al agente.
     */
    @Override
    public Action selectAction() {
        busquedaSolver.setVisibleTree(Search.EFAIA_TREE);

        // Configura el solucionador de problemas
        this.setSolver(busquedaSolver);

        // Consulta a busquedaSolver por la mejor acción
        Action selectedAction = null;
        try {
            selectedAction = this.getSolver().solve(new Object[]{this.getProblem()});
        } catch (Exception ex) {
            Logger.getLogger(Agente.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Devuelve la acción seleccionada
        return selectedAction;
    }

    /**
     * Este método es ejecutado por el simulador para brindar una percepción al agente
     * Luego actualiza su estado.
     *
     * @param p
     */
    @Override
    public void see(Perception p) {
        this.getAgentState().updateState(p);
    }

    public void setEstrategiaBusqueda(int indexEstrategia) {
        estrategia = indexEstrategia;
        switch (indexEstrategia) {
            case 0:
                DepthFirstSearch e1 = new DepthFirstSearch();
                busquedaSolver = new Search(e1);
                break;
            case 1:
                BreathFirstSearch e2 = new BreathFirstSearch();
                busquedaSolver = new Search(e2);
                break;
            case 2:
                IStepCostFunction c1 = new FuncionCosto();
                UniformCostSearch e3 = new UniformCostSearch(c1);
                busquedaSolver = new Search(e3);
            case 3:
                IStepCostFunction c2 = new FuncionCosto();
                IEstimatedCostFunction h1 = new Heuristica();
                AStarSearch e4 = new AStarSearch(c2, h1);
                busquedaSolver = new Search(e4);
                break;
            case 4:
                IEstimatedCostFunction h2 = new Heuristica();
                GreedySearch e5 = new GreedySearch(h2);
                busquedaSolver = new Search(e5);
                break;
        }
    }

    public String getEstrategiaBusqueda() {
        switch (estrategia) {
            case 0:
                return "Busqueda en Profundidad";
            case 1:
                return "Busqueda en Anchura";
            case 2:
                return "Busqueda de Costo Fijo";
            case 3:
                return "Busqueda A*";
            case 4:
                return "Busqueda Avara";
            default:
                return "Sin Información";
        }
    }
}
