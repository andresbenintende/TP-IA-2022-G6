package UI;

import Dominio.*;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VentanaConfiguracion extends JFrame implements ActionListener {

    public EstadoAmbiente estadoAmbiente;

    private JMenuItem configMenuItem;
    private JMenuItem salirMenuItem;
    private final Font fuente = new Font("Niagaraphobia", Font.PLAIN, 18);

    private boolean random = true;
    private final JButton aleatorioBtn = new JButton("");
    private final JComboBox<Integer> solesPlantaComboBox = new JComboBox<>();
    private JComboBox<String> estratBusqComboBox;
    private final Insets insets = new Insets(0, 0, 0, 0);
    private final GridBagConstraints constraints = new GridBagConstraints();

    private final ImageIcon toggleON = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/toggle_on.png")));
    private final ImageIcon toggleOFF = new ImageIcon(Objects.requireNonNull(getClass().getResource("img/toggle_off.png")));

    private JFrame config = new JFrame();
    private JPanel configPanel = new ConfigPanel();

    private JLabel label = new JLabel();

    public VentanaConfiguracion(EstadoAmbiente est) {

        this.setTitle("Plants vs. Zombies | Inteligencia Artificial | UTN FRSF - 2022 | Grupo 6");

        Inicializar(est);
    }

    private void Inicializar(EstadoAmbiente est) {

        estadoAmbiente = est;

        /* Creamos el JMenuBar y lo asociamos con el JFrame */
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        /* Creamos el primer JMenu y lo pasamos como parámetro al JMenuBar mediante el método add */
        JMenu menu1 = new JMenu("Menú");
        menu1.setFont(fuente);
        menuBar.add(menu1);

        /* Creamos el segundo y tercer objetos de la clase JMenu y los asociamos con el primer JMenu creado */
        configMenuItem = new JMenuItem("Configuración del Escenario");
        configMenuItem.setFont(fuente);
        salirMenuItem = new JMenuItem("Salir");
        salirMenuItem.setFont(fuente);
        menu1.add(configMenuItem);
        menu1.add(salirMenuItem);

        configMenuItem.addActionListener(this);
        salirMenuItem.addActionListener(this);

        JuegoPanel panelJuego = new JuegoPanel();

        label.setText(String.valueOf(estadoAmbiente.getSolesPlanta()));

        this.getContentPane().add(panelJuego);
        panelJuego.add(label);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    /* Método que implementa las acciones de cada ítem de menú */
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == configMenuItem) {
            config.setVisible(true);
            config.setSize(450, 450);
            config.setLocationRelativeTo(null);
            config.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            config.setTitle("Configuración del Juego");

            configPanel.setBounds(0, 0, 450, 450);
            configPanel.setLayout(new GridBagLayout());

            JLabel estrategiaBusq = new ConfigLabel("Estrategia de Búsqueda", JLabel.LEFT);

            String[] estBusq = {"Profundidad", "Anchura", "Costo Fijo", "A*", "Avara"};
            estratBusqComboBox = new JComboBox<>(estBusq);
            estratBusqComboBox.setName("estratBusqComboBox");
            estratBusqComboBox.setEnabled(true);

            JLabel aleatorioLbl = new ConfigLabel("Aleatorio", JLabel.LEFT);

            aleatorioBtn.setIcon(toggleON);
            aleatorioBtn.setBackground(new Color(0, 0, 0, 0f));
            aleatorioBtn.setBorder(null);
            aleatorioBtn.addActionListener(ex -> setRandom());

            JLabel solesPlantaLbl = new ConfigLabel("Soles Iniciales (Agente)", JLabel.LEFT);

            for (int i = 2; i < 21; i++) {
                solesPlantaComboBox.addItem(i);
            }
            solesPlantaComboBox.setName("solesComboBox");
            solesPlantaComboBox.setEnabled(!random);

            setGridBagConstraints(0, 0, 3, 1, 0, 0, 10, 0, insets, 0, 10);
            configPanel.add(estrategiaBusq, constraints);
            setGridBagConstraints(3, 0, 1, 1, 0, 0, 10, 0, insets, 0, 0);
            configPanel.add(estratBusqComboBox, constraints);
            setGridBagConstraints(2, 1, 1, 1, 0, 0.5, 10, 0, insets, 0, 0);
            configPanel.add(aleatorioLbl, constraints);
            setGridBagConstraints(3, 1, 1, 1, 0, 0.5, 10, 0, insets, 0, 0);
            configPanel.add(aleatorioBtn, constraints);
            setGridBagConstraints(0, 2, 3, 1, 0, 0, 10, 0, insets, 0, 10);
            configPanel.add(solesPlantaLbl, constraints);
            setGridBagConstraints(3, 2, 1, 1, 0, 0, 10, 0, insets, 0, 0);
            configPanel.add(solesPlantaComboBox, constraints);


            JLabel zombiesLbl;

            for (int i = 1; i < 11; i++) {
                zombiesLbl = new ConfigLabel("Zombie " + (i), JLabel.LEFT);
                JCheckBox zombieCheck = new JCheckBox();
                zombieCheck.setName(zombiesLbl.getText());
                PoderZombieComboBox poderZombie = new PoderZombieComboBox();
                poderZombie.setName(zombiesLbl.getText());
                Insets derInset = new Insets(0, 5, 0, 5);
                setGridBagConstraints(0, 2 + i, 1, 1, 0, 0, 10, 0, derInset, 0, 0);
                configPanel.add(zombiesLbl, constraints);
                setGridBagConstraints(1, 2 + i, 1, 1, 0, 0, 10, 0, derInset, 0, 0);
                configPanel.add(zombieCheck, constraints);
                setGridBagConstraints(2, 2 + i, 1, 1, 0, 0, 10, 0, derInset, 0, 0);
                configPanel.add(poderZombie, constraints);
                poderZombie.setEnabled(!random);
                zombieCheck.setEnabled(false);

                zombieCheck.addActionListener(a -> habilitarPoderZombieComboBox(configPanel, zombieCheck.getName(), zombieCheck.isSelected()));
            }

            for (int i = 11; i < 21; i++) {
                zombiesLbl = new ConfigLabel("Zombie " + (i), JLabel.LEFT);
                JCheckBox zombieCheck = new JCheckBox();
                zombieCheck.setName(zombiesLbl.getText());
                PoderZombieComboBox poderZombie = new PoderZombieComboBox();
                poderZombie.setName(zombiesLbl.getText());
                Insets izqInset = new Insets(0, 5, 0, 5);
                setGridBagConstraints(3, 2 + i - 10, 1, 1, 0, 0, 10, 0, izqInset, 0, 0);
                configPanel.add(zombiesLbl, constraints);
                setGridBagConstraints(5, 2 + i - 10, 1, 1, 0, 0, 10, 0, izqInset, 0, 0);
                configPanel.add(zombieCheck, constraints);
                setGridBagConstraints(6, 2 + i - 10, 1, 1, 0, 0, 10, 0, izqInset, 0, 0);
                configPanel.add(poderZombie, constraints);
                poderZombie.setEnabled(!random);
                zombieCheck.setEnabled(false);

                zombieCheck.addActionListener(a -> habilitarPoderZombieComboBox(configPanel, zombieCheck.getName(), zombieCheck.isSelected()));
            }

            setGridBagConstraints(2, 24, 2, 1, 0, 0.5, GridBagConstraints.CENTER, 0, insets, 0, 0);
            JButton guardarButton = new JButton("Guardar");
            guardarButton.addActionListener(act -> guardarConfig(random, configPanel));

            configPanel.add(guardarButton, constraints);

            config.add(configPanel);

        }

        if (e.getSource() == salirMenuItem) {
            System.exit(0);
        }
    }

    private void guardarConfig(boolean random, JPanel configPanel) {
        if (!random) {
            int solesIniciales = 0;
            List<Zombie> zombiesList = new ArrayList<>();
            for (Component c : configPanel.getComponents()) {
                if (Objects.equals(c.getName(), "solesComboBox"))
                    solesIniciales = ((Integer) ((JComboBox<?>) c).getSelectedItem());

                if ((c instanceof PoderZombieComboBox) && c.getName().contains("Zombie ") && c.isEnabled()) {
                    zombiesList.add(new Zombie(new Posicion(0, 0), (int) ((JComboBox<?>) c).getSelectedItem()));
                }
            }
            System.out.println(estadoAmbiente.getZombies());
            estadoAmbiente = new EstadoAmbiente(random, solesIniciales, zombiesList);
        } else {
            System.out.println("entra en else");
            estadoAmbiente = new EstadoAmbiente(random, 0, new ArrayList<>());
        }
        grabarConfig(estadoAmbiente);
    }

    private void setGridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = gridwidth;
        constraints.gridheight = gridheight;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.anchor = anchor;
        constraints.fill = fill;
        constraints.insets = insets;
        constraints.ipadx = ipadx;
        constraints.ipady = ipady;
    }

    private void setRandom() {
        random = !random;
        if (random) {
            aleatorioBtn.setIcon(toggleON);
        } else {
            aleatorioBtn.setIcon(toggleOFF);
        }
        bloquearCampos(configPanel);

    }

    void bloquearCampos(Container container) {
        for (Component c : container.getComponents()) {
            if (Objects.equals(c.getName(), "solesComboBox")) {
                c.setEnabled(!random);
            } else {
                //if (c instanceof PoderZombieComboBox && zombiesFijos(c.getName())) {
                //    c.setEnabled(!random);
                //}
                if (c instanceof JCheckBox){// && !zombiesFijos(c.getName())) {
                    c.setEnabled(!random);
                    habilitarPoderZombieComboBox(container, c.getName(), ((JCheckBox) c).isSelected());
                }
            }
        }
    }

    void habilitarPoderZombieComboBox(Container container, String nombreCheckBox, boolean isSelected) {
        for (Component c : container.getComponents()) {
            if ((c instanceof PoderZombieComboBox) &&
                    (Objects.equals(c.getName(), nombreCheckBox))) {
                c.setEnabled(isSelected);// || zombiesFijos(c.getName()));
            }
        }
    }

    @Deprecated
    private boolean zombiesFijos(String nombreElemento) {
        String[] arr = {"Zombie 1", "Zombie 2", "Zombie 3", "Zombie 4", "Zombie 5"};
        return Arrays.asList(arr).contains(nombreElemento);
    }

    public void grabarConfig(EstadoAmbiente estadoAmbiente) {
        System.out.println(estadoAmbiente.getZombies());
        Document document;
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

            document = documentBuilder.newDocument();

            // estadoAmbienteXML element
            Element estadoAmbienteXML = document.createElement("estadoAmbienteXML");
            document.appendChild(estadoAmbienteXML);

            Element aleatorioElement = document.createElement("aleatorio");
            aleatorioElement.appendChild(document.createTextNode(String.valueOf(random)));
            estadoAmbienteXML.appendChild(aleatorioElement);

            if (!random) {
                Element solesPlantaElement = document.createElement("SolesPlanta");
                solesPlantaElement.appendChild(document.createTextNode(String.valueOf(estadoAmbiente.getSolesPlanta())));
                estadoAmbienteXML.appendChild(solesPlantaElement);

                // Zombies
                int i = 1;
                for (Zombie zombie : estadoAmbiente.getZombies()) {

                    Element zombieElement = document.createElement("Zombie");
                    estadoAmbienteXML.appendChild(zombieElement);

                    Attr idZombie = document.createAttribute("id");
                    idZombie.setValue(String.valueOf(i++));
                    zombieElement.setAttributeNode(idZombie);

                    // posicion element
                    Element posicion = document.createElement("Posicion");
                    zombieElement.appendChild(posicion);

                    // fila element
                    Element fila = document.createElement("fila");
                    fila.appendChild(document.createTextNode(String.valueOf(zombie.getPosicion().getFila())));
                    posicion.appendChild(fila);

                    // col element
                    Element col = document.createElement("columna");
                    col.appendChild(document.createTextNode(String.valueOf(zombie.getPosicion().getFila())));
                    posicion.appendChild(col);

                    Element poder = document.createElement("poderZombie");
                    poder.appendChild(document.createTextNode(String.valueOf(zombie.getPoder())));
                    zombieElement.appendChild(poder);
                }

                //Girasoles
                i = 1;
                for (Girasol girasol : estadoAmbiente.getGirasoles()) {
                    Element girasolElement = document.createElement("Girasol");
                    estadoAmbienteXML.appendChild(girasolElement);

                    Attr idGirasol = document.createAttribute("id");
                    idGirasol.setValue(String.valueOf(i++));
                    girasolElement.setAttributeNode(idGirasol);

                    // posicion element
                    Element posicion = document.createElement("Posicion");
                    girasolElement.appendChild(posicion);

                    // fila element
                    Element fila = document.createElement("fila");
                    fila.appendChild(document.createTextNode(String.valueOf(girasol.getPosicion().getFila())));
                    posicion.appendChild(fila);

                    // col element
                    Element col = document.createElement("columna");
                    col.appendChild(document.createTextNode(String.valueOf(girasol.getPosicion().getFila())));
                    posicion.appendChild(col);

                    Element soles = document.createElement("solesGirasol");
                    soles.appendChild(document.createTextNode(String.valueOf(girasol.getCantSoles())));
                    girasolElement.appendChild(soles);

                }
            }
            crearXML(document,"src/Auxiliares/config.xml");

            document = documentBuilder.newDocument();
            Element estrategiaBusquedaElement = document.createElement("estrategiaBusqueda");
            estrategiaBusquedaElement.appendChild(document.createTextNode((String.valueOf(estratBusqComboBox.getSelectedIndex()))));
            document.appendChild(estrategiaBusquedaElement);

            crearXML(document,"src/Auxiliares/estrategiaBusqueda.xml");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private void crearXML (Document document, String nombreArchivo) throws TransformerException {

        // create the xml file
        //transform the DOM Object to an XML File
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult(new File(nombreArchivo));

        transformer.transform(domSource, streamResult);

        System.out.println("Done creating XML File");

    }
}
