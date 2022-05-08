package UI;

import Dominio.*;
import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class VentanaPrincipal extends JFrame implements ActionListener {

    public EstadoAmbiente estadoAmbiente;
    private int estrategiaBusqueda;

    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
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

    public VentanaPrincipal(EstadoAmbiente est) {

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
        menuItem1 = new JMenuItem("Iniciar Juego");
        menuItem1.setFont(fuente);
        menuItem2 = new JMenuItem("Configuración del Escenario");
        menuItem2.setFont(fuente);
        menuItem3 = new JMenuItem("Información del Escenario");
        menuItem3.setFont(fuente);
        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.add(menuItem3);

        menuItem1.addActionListener(this);
        menuItem2.addActionListener(this);
        menuItem3.addActionListener(this);

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
        if(e.getSource() == menuItem1){
            Agente agente = new Agente();
            Ambiente ambiente = new Ambiente();

            SearchBasedAgentSimulator simulator = new SearchBasedAgentSimulator(ambiente, agente);

            simulator.start();
        }

        if (e.getSource() == menuItem2) {

            config.setVisible(true);
            config.setSize(450, 450);
            config.setLocationRelativeTo(null);
            config.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

            config.setTitle("Configuración del Juego");


            configPanel.setBounds(0, 0, 450, 450);
            configPanel.setLayout(new GridBagLayout());

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

            JLabel estrategiaBusq = new ConfigLabel("Estrategia de Búsqueda", JLabel.LEFT);

            String[] estBusq = {"Profundidad", "Anchura", "Costo Fijo", "A*", "Avara"};
            estratBusqComboBox = new JComboBox<>(estBusq);
            estratBusqComboBox.setName("estratBusqComboBox");
            estratBusqComboBox.setEnabled(!random);

            setGridBagConstraints(2, 0, 1, 1, 0, 0.5, 10, 0, insets, 0, 0);
            configPanel.add(aleatorioLbl, constraints);
            setGridBagConstraints(3, 0, 1, 1, 0, 0.5, 10, 0, insets, 0, 0);
            configPanel.add(aleatorioBtn, constraints);
            setGridBagConstraints(0, 1, 3, 1, 0, 0, 10, 0, insets, 0, 10);
            configPanel.add(solesPlantaLbl, constraints);
            setGridBagConstraints(3, 1, 1, 1, 0, 0, 10, 0, insets, 0, 0);
            configPanel.add(solesPlantaComboBox, constraints);
            setGridBagConstraints(0, 2, 3, 1, 0, 0, 10, 0, insets, 0, 10);
            configPanel.add(estrategiaBusq, constraints);
            setGridBagConstraints(3, 2, 1, 1, 0, 0, 10, 0, insets, 0, 0);
            configPanel.add(estratBusqComboBox, constraints);


            for (int i = 1; i < 11; i++) {
                JLabel zombiesLbl = new ConfigLabel("Zombie " + (i), JLabel.LEFT);
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
                zombieCheck.setSelected(i < 6);
                zombieCheck.setEnabled(!random && i > 5);
                poderZombie.setEnabled(!random && i < 6);

                zombieCheck.addActionListener(a -> habilitarPoderZombieComboBox(configPanel, zombieCheck.getName(), zombieCheck.isSelected()));
            }

            for (int i = 11; i < 21; i++) {
                JLabel zombiesLbl = new ConfigLabel("Zombie " + (i), JLabel.LEFT);
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


        if (e.getSource() == menuItem3) {
            getContentPane().setBackground(new Color(0, 255, 0));
        }
    }

    private void guardarConfig(boolean random, JPanel configPanel) {
        if (!random) {
            int solesIniciales = 0;
            List<Zombie> zombiesList = new ArrayList<>();
            for (Component c : configPanel.getComponents()) {
                if (Objects.equals(c.getName(), "solesComboBox"))
                    solesIniciales = ((Integer) ((JComboBox<?>) c).getSelectedItem());

                if (Objects.equals(c.getName(), "estratBusqComboBox"))
                    estrategiaBusqueda = (((JComboBox<?>) c).getSelectedIndex());

                if ((c instanceof PoderZombieComboBox) && c.getName().contains("Zombie "))
                    zombiesList.add(new Zombie(new Posicion(0, 0), (int) ((JComboBox<?>) c).getSelectedItem()));
            }
            estadoAmbiente = new EstadoAmbiente(random, solesIniciales, zombiesList);
        }
        else{
            estadoAmbiente = new EstadoAmbiente(random, null, null);
        }
        System.out.println(estadoAmbiente.getZombies());
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
            if (Objects.equals(c.getName(), "solesComboBox") || Objects.equals(c.getName(), "estratBusqComboBox")
            ) {
                c.setEnabled(!random);
            } else {
                if (c instanceof PoderZombieComboBox && zombiesFijos(c.getName())) {
                    c.setEnabled(!random);
                }
                if (c instanceof JCheckBox && !zombiesFijos(c.getName())) {
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
                c.setEnabled(isSelected || zombiesFijos(c.getName()));
            }
        }
    }

    private boolean zombiesFijos(String nombreElemento) {
        String[] arr = {"Zombie 1", "Zombie 2", "Zombie 3", "Zombie 4", "Zombie 5"};
        return Arrays.asList(arr).contains(nombreElemento);
    }
}
