package UI;

import Dominio.EstadoAmbiente;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.*;


public class VentanaJuego extends JFrame{

    private final Font fuente = new Font("Niagaraphobia", Font.PLAIN, 22);
    private final Font fuenteEstrategia = new Font("Niagaraphobia", Font.PLAIN, 40);
    private final Font fuenteGrande = new Font("Niagaraphobia", Font.ITALIC, 60);

    private static final ImageIcon icon = new ImageIcon("src/UI/img/icon.png");

    String estrategia;

    JPanel panelPrincipal = new JPanel();

    public VentanaJuego(EstadoAmbiente estadoAmbiente, String estrategiaBusqueda) {
        iniciarVentana(estadoAmbiente, estrategiaBusqueda);
    }

    public void iniciarVentana(EstadoAmbiente estadoAmbiente, String estrategiaBusqueda) {

        this.setIconImage(icon.getImage());

        panelPrincipal = new JPanel();
        panelPrincipal.setBounds(0, 0, 1167, 500);
        panelPrincipal.setLayout(null);

        estrategia = estrategiaBusqueda;

        estadoAmbiente.getZombies().forEach(zombie -> {
            JLabel zombieLabel = new JLabel();
            ImageIcon zombieIcon = new ImageIcon("src/UI/img/zombie1.gif");
            switch ((zombie.getPoder())) {

                case -1:
                    zombieIcon = new ImageIcon("src/UI/img/zombie1.gif");
                    break;
                case -2:
                    zombieIcon = new ImageIcon("src/UI/img/zombie2.gif");
                    break;
                case -3:
                    zombieIcon = new ImageIcon("src/UI/img/zombie3.gif");
                    break;
                case -4:
                    zombieIcon = new ImageIcon("src/UI/img/zombie4.gif");
                    break;
                case -5:
                    zombieIcon = new ImageIcon("src/UI/img/zombie5.gif");
                    break;
            }

            zombieLabel.setIcon(new ImageIcon(zombieIcon.getImage().getScaledInstance(70, 80, 0)));
            zombieLabel.setBounds(205 + (zombie.getPosicion().getColumna() - 1) * 70, 80*(zombie.getPosicion().getFila()) -5, 70, 80);

            panelPrincipal.add(zombieLabel);

        });

        //Agente
        JLabel planta = new JLabel();
        ImageIcon plantaIcon = new ImageIcon("src/UI/img/planta.gif");

        planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(70, 80, 0)));
        planta.setBounds(205 + (estadoAmbiente.getPosicionPlanta().getColumna() - 1) * 70, 80*(estadoAmbiente.getPosicionPlanta().getFila()) -5, 70, 80);

        panelPrincipal.add(planta);

        //Girasoles

        estadoAmbiente.getGirasoles().forEach((g) -> {
            JLabel girasol = new JLabel();
            ImageIcon girasolIcon = new ImageIcon("src/UI/img/girasol.gif");

            girasol.setIcon(new ImageIcon(girasolIcon.getImage().getScaledInstance(70, 80, 0)));
            girasol.setBounds(205 + (g.getPosicion().getColumna() - 1)*70, 80 * (g.getPosicion().getFila()) -5, 70, 80);
            panelPrincipal.add(girasol);
        });

        //Información del Juego
        JLabel solesPlantaLabel = new JLabel();
        ImageIcon solesPlantalIcon = new ImageIcon("src/UI/img/soles.gif");
        solesPlantaLabel.setIcon(new ImageIcon(solesPlantalIcon.getImage().getScaledInstance(60, 60, 0)));
        solesPlantaLabel.setFont(fuente);
        solesPlantaLabel.setBounds(50, 510, 60, 60);
        JLabel solesPlantaValue = new JLabel(String.valueOf(estadoAmbiente.getSolesPlanta()));
        solesPlantaValue.setFont(fuente);
        solesPlantaValue.setBounds(130, 520, 60, 60);

        JLabel cantZombiesLabel = new JLabel();
        ImageIcon cantZombieslIcon = new ImageIcon("src/UI/img/cantZombie.gif");
        cantZombiesLabel.setIcon(new ImageIcon(cantZombieslIcon.getImage().getScaledInstance(60, 60, 0)));
        cantZombiesLabel.setFont(fuente);
        cantZombiesLabel.setBounds(180, 510, 60, 60);
        JLabel cantZombiesValue = new JLabel(String.valueOf(estadoAmbiente.getCantidadZombies()));
        cantZombiesValue.setFont(fuente);
        cantZombiesValue.setBounds(240, 520, 60, 60);

        JLabel estratBusqLabel = new JLabel("Estrategia: "+estrategia);
        estratBusqLabel.setFont(fuenteEstrategia);
        estratBusqLabel.setBounds(350,500,500,100);

        panelPrincipal.add(solesPlantaLabel);
        panelPrincipal.add(solesPlantaValue);
        panelPrincipal.add(cantZombiesLabel);
        panelPrincipal.add(cantZombiesValue);
        panelPrincipal.add(estratBusqLabel);


        //Tablero
        JLabel fondo = new JLabel();
        ImageIcon fondoIcon = new ImageIcon("src/UI/img/tablero.png");
        fondo.setIcon(new ImageIcon(fondoIcon.getImage().getScaledInstance(panelPrincipal.getWidth(), panelPrincipal.getHeight(), 0)));
        fondo.setBounds(0, 0, 1167, 500);

        JLabel startLabel = new JLabel("Preparados... ¡El Juego Comienza!", SwingConstants.CENTER);
        startLabel.setForeground(Color.white);
        startLabel.setFont(fuenteGrande);
        startLabel.setBounds(0,250,1167,100);

        JLabel startShadowLabel = new JLabel("Preparados... ¡El Juego Comienza!", SwingConstants.CENTER);
        startShadowLabel.setForeground(Color.darkGray);
        startShadowLabel.setFont(fuenteGrande);
        startShadowLabel.setBounds(2,252,1167,100);

        panelPrincipal.add(startLabel);
        panelPrincipal.add(startShadowLabel);

        panelPrincipal.add(fondo);

        panelPrincipal.setBackground(Color.lightGray);


        //Ventana del Juego
        this.getContentPane().add(panelPrincipal);
        this.setSize(1167, 630);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.repaint();
    }


    public void actualizar(EstadoAmbiente estadoAmbiente, Boolean exito) {

        this.getContentPane().removeAll();
        //Panel Principal
        panelPrincipal = new JPanel();
        panelPrincipal.setBounds(0, 0, 1167, 500);
        panelPrincipal.setLayout(null);

        //Chequea si el juego ha terminado
        if(exito != null) {
            if (exito) {
                JLabel exitoLabel = new JLabel("¡Éxito! Los Zombies han sido derrotados", SwingConstants.CENTER);
                exitoLabel.setForeground(Color.cyan);
                exitoLabel.setFont(fuenteGrande);
                exitoLabel.setBounds(0, 250, 1167, 100);

                JLabel exitoShadowLabel = new JLabel("¡Éxito! Los Zombies han sido derrotados", SwingConstants.CENTER);
                exitoShadowLabel.setForeground(Color.darkGray);
                exitoShadowLabel.setFont(fuenteGrande);
                exitoShadowLabel.setBounds(2, 252, 1167, 100);

                JLabel planta = new JLabel();
                ImageIcon plantaIcon = new ImageIcon("src/UI/img/plantaExito.gif");

                planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(140, 160, 0)));
                planta.setBounds(350, 330, 140, 160);


                panelPrincipal.add(planta);

                panelPrincipal.add(exitoLabel);
                panelPrincipal.add(exitoShadowLabel);

            } else {

                JLabel fracasoLabel = new JLabel("Juego terminado... ¡Hemos perdido =(!", SwingConstants.CENTER);
                fracasoLabel.setForeground(Color.red);
                fracasoLabel.setFont(fuenteGrande);
                fracasoLabel.setBounds(0, 250, 1167, 100);

                JLabel fracasoShadowLabel = new JLabel("Juego terminado... ¡Hemos perdido =(!", SwingConstants.CENTER);
                fracasoShadowLabel.setForeground(Color.darkGray);
                fracasoShadowLabel.setFont(fuenteGrande);
                fracasoShadowLabel.setBounds(2, 252, 1167, 100);

                panelPrincipal.add(fracasoLabel);
                panelPrincipal.add(fracasoShadowLabel);

                JLabel planta = new JLabel();
                ImageIcon plantaIcon = new ImageIcon("src/UI/img/plantaFracaso.gif");

                planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(140, 160, 0)));
                planta.setBounds(350, 330, 140, 160);


                panelPrincipal.add(planta);

            }
        }

        //Zombies
        estadoAmbiente.getZombies().forEach(zombie -> {
            JLabel zombieLabel = new JLabel();
            ImageIcon zombieIcon = new ImageIcon("src/UI/img/zombies/zombie1.png");
            ImageIcon zombieBoxIcon = new ImageIcon("src/UI/img/zombieBox.png");
            switch ((zombie.getPoder())) {

                case -1:
                    zombieIcon = new ImageIcon("src/UI/img/zombies/zombie1.gif");
                    break;
                case -2:
                    zombieIcon = new ImageIcon("src/UI/img/zombies/zombie2.gif");
                    break;
                case -3:
                    zombieIcon = new ImageIcon("src/UI/img/zombies/zombie3.gif");
                    break;
                case -4:
                    zombieIcon = new ImageIcon("src/UI/img/zombies/zombie4.gif");
                    break;
                case -5:
                    zombieIcon = new ImageIcon("src/UI/img/zombies/zombie5.gif");
                    break;
            }

            zombieLabel.setIcon(new ImageIcon(zombieIcon.getImage().getScaledInstance(70, 80, 0)));
            zombieLabel.setBounds(205 + (zombie.getPosicion().getColumna() - 1) * 70, 80*(zombie.getPosicion().getFila()) -5, 70, 80);

            JLabel poderZombieFondo = new JLabel();
            poderZombieFondo.setIcon(new ImageIcon(zombieBoxIcon.getImage().getScaledInstance(25, 25, 0)));
            poderZombieFondo.setBounds(255 + (zombie.getPosicion().getColumna() - 1)*70, 80 * (zombie.getPosicion().getFila()) + 43, 25, 25);

            JLabel poderZombieLabel = new JLabel(String.valueOf(zombie.getPoder()),SwingConstants.RIGHT);
            poderZombieLabel.setFont(fuente);
            poderZombieLabel.setForeground(Color.white);
            poderZombieLabel.setBounds(235 + (zombie.getPosicion().getColumna() - 1)*70, 80 * (zombie.getPosicion().getFila()) + 45, 40, 25);

            panelPrincipal.add((poderZombieLabel));
            panelPrincipal.add((poderZombieFondo));
            panelPrincipal.add(zombieLabel);

        });

        //Agente
        JLabel planta = new JLabel();
        ImageIcon plantaIcon = new ImageIcon("src/UI/img/planta.gif");

        planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(70, 80, 0)));
        planta.setBounds(205 + (estadoAmbiente.getPosicionPlanta().getColumna() - 1) * 70, 80*(estadoAmbiente.getPosicionPlanta().getFila()) -5, 70, 80);


        panelPrincipal.add(planta);

        //Girasoles

        estadoAmbiente.getGirasoles().forEach((g) -> {
            JLabel girasol = new JLabel();
            ImageIcon girasolIcon = new ImageIcon("src/UI/img/girasol.gif");
            ImageIcon solesBoxIcon = new ImageIcon("src/UI/img/solesBox.png");
            girasol.setIcon(new ImageIcon(girasolIcon.getImage().getScaledInstance(70, 80, 0)));
            girasol.setBounds(205 + (g.getPosicion().getColumna() - 1)*70, 80 * (g.getPosicion().getFila()) -5, 70, 80);

            JLabel solesGirasolFondo = new JLabel();
            solesGirasolFondo.setIcon(new ImageIcon(solesBoxIcon.getImage().getScaledInstance(25, 25, 0)));
            solesGirasolFondo.setBounds(255 + (g.getPosicion().getColumna() - 1)*70, 80 * (g.getPosicion().getFila()) + 43, 25, 25);

            JLabel solesGirasol = new JLabel(String.valueOf(g.getCantSoles()),SwingConstants.RIGHT);
            solesGirasol.setFont(fuente);
            solesGirasol.setForeground(Color.white);
            solesGirasol.setBounds(235 + (g.getPosicion().getColumna() - 1)*70, 80 * (g.getPosicion().getFila()) + 45, 40, 25);

            panelPrincipal.add((solesGirasol));
            panelPrincipal.add((solesGirasolFondo));
            panelPrincipal.add(girasol);

        });

        JPanel otroPanel = new JPanel();
        otroPanel.setBounds(0, 500, 1167, 130);
        otroPanel.setLayout(null);
        otroPanel.setBackground(Color.lightGray);


        //Información del Juego
        JLabel solesPlantaLabel = new JLabel();
        ImageIcon solesPlantalIcon = new ImageIcon("src/UI/img/soles.gif");
        solesPlantaLabel.setIcon(new ImageIcon(solesPlantalIcon.getImage().getScaledInstance(60, 60, 0)));
        solesPlantaLabel.setFont(fuente);
        solesPlantaLabel.setBounds(50, 10, 60, 60);
        JLabel solesPlantaValue = new JLabel(String.valueOf(estadoAmbiente.getSolesPlanta()));
        solesPlantaValue.setFont(fuente);
        solesPlantaValue.setBounds(130, 20, 60, 60);

        JLabel cantZombiesLabel = new JLabel();
        ImageIcon cantZombieslIcon = new ImageIcon("src/UI/img/cantZombie.gif");
        cantZombiesLabel.setIcon(new ImageIcon(cantZombieslIcon.getImage().getScaledInstance(60, 60, 0)));
        cantZombiesLabel.setFont(fuente);
        cantZombiesLabel.setBounds(180, 10, 60, 60);
        JLabel cantZombiesValue = new JLabel(String.valueOf(estadoAmbiente.getCantidadZombies()));
        cantZombiesValue.setFont(fuente);
        cantZombiesValue.setBounds(240, 20, 60, 60);

        JLabel estratBusqLabel = new JLabel("Estrategia: "+estrategia);
        estratBusqLabel.setFont(fuenteEstrategia);
        estratBusqLabel.setBounds(350,0,500,100);


        otroPanel.add(solesPlantaLabel);
        otroPanel.add(solesPlantaValue);
        otroPanel.add(cantZombiesLabel);
        otroPanel.add(cantZombiesValue);
        otroPanel.add(estratBusqLabel);

        //FONDO
        JLabel fondo = new JLabel();
        ImageIcon fondoIcon = new ImageIcon("src/UI/img/tablero.png");
        fondo.setIcon(new ImageIcon(fondoIcon.getImage().getScaledInstance(panelPrincipal.getWidth(), panelPrincipal.getHeight(), 0)));
        fondo.setBounds(0, 0, 1167, 500);

        panelPrincipal.add(fondo);

        panelPrincipal.setBackground(Color.lightGray);

        //CONFIGURACION VENTANA PRINCIPAL
        this.getContentPane().add(otroPanel);
        this.getContentPane().add(panelPrincipal);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
