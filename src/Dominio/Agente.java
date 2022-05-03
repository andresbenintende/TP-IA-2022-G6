package Dominio;

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

    public Agente() {

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

        // Creación de la estrategia de búsqueda

     //Búsqueda en profundidad
        BreathFirstSearch estrategia = new BreathFirstSearch();
        /**
         * Estrategias de búsqueda:
         *
         * Búsqueda en profundidad
         * DepthFirstSearch estrategia = new DepthFirstSearch();
         *
         * Búsqueda en anchura:
         * BreathFirstSearch estrategia = new BreathFirstSearch();
         *
         * Búsqueda de Costo Uniforme:
         * IStepCostFunction costo = new CostFunction();
         * UniformCostSearch estrategia = new UniformCostSearch(costo);
         *
         * Búsqueda A*:
         * IStepCostFunction costo = new CostFunction();
         * IEstimatedCostFunction heurística = new Heuristic();
         * AStarSearch estrategia = new AStarSearch(costo, heurística);
         *
         * Greedy Search:
         * IEstimatedCostFunction heurística = new Heuristic();
         * GreedySearch estrategia = new GreedySearch(heurística);
         */

        // Crea un objeto del tipo Search con la estrategia elegida
        Search busquedaSolver = new Search(estrategia);

        /* Genera un archivo XML con el árbol de búsqueda.
         * También puede generarse en PDF usando PDF_TREE */
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
     * @param p
     */
    @Override
    public void see(Perception p) {
        this.getAgentState().updateState(p);
    }
}

