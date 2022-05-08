package Dominio;

import frsf.cidisi.faia.exceptions.PrologConnectorException;

public class SearchMain {

    public static void main(String[] args) throws PrologConnectorException {
        Agente agente = new Agente();
        Ambiente ambiente = new Ambiente();

        Simulador simulator = new Simulador(ambiente, agente);

        simulator.start();


    }
}
