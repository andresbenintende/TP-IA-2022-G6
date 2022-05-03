package Dominio;

import frsf.cidisi.faia.exceptions.PrologConnectorException;
import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

public class SearchMain {

    public static void main(String[] args) throws PrologConnectorException {
        Agente agente = new Agente();
       Ambiente ambiente = new Ambiente();

        SearchBasedAgentSimulator simulator = new SearchBasedAgentSimulator(ambiente, agente);

        simulator.start();


    }
}
