package Auxiliares;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import Dominio.Agente;
import Dominio.EstadoAmbiente;
import Dominio.Posicion;
import Dominio.Zombie;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DOMParser {

    public DOMParser() {
    }

    ;

    public boolean estadoAmbienteParse(EstadoAmbiente estadoAmbiente) {

        boolean aleatorio = true;

        try {
            File inputFile = new File("src/Auxiliares/config.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("estadoAmbienteXML");

            //Aleatorio
            Node estadoAmbienteNodo = nList.item(0);

            if (estadoAmbienteNodo.getFirstChild().getTextContent().equals("false")) {

                aleatorio = false;

                for (int temp = 0; temp < estadoAmbienteNodo.getChildNodes().getLength(); temp++) {
                    Node nNode = estadoAmbienteNodo.getChildNodes().item(temp);

                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        switch (eElement.getTagName()) {
                            case "Zombie":
                                estadoAmbiente.getZombies().add(new Zombie(new Posicion(0, 0),
                                        Integer.parseInt(eElement.getElementsByTagName("poderZombie").item(0).getTextContent())));
                                break;
                            case "SolesPlanta":
                                estadoAmbiente.setSolesPlanta(Integer.parseInt(eElement.getTextContent()));
                                break;
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return aleatorio;
    }

    public int estrategiaBusquedaParser() {

        try {
            File inputFile = new File("src/Auxiliares/estrategiaBusqueda.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("estrategiaBusqueda");

            Node estadoAmbienteNodo = nList.item(0);
            Node nNode = estadoAmbienteNodo.getChildNodes().item(0);
            return Integer.parseInt(nNode.getTextContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
