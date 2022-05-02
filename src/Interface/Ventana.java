package Interface;

import Dominio.EstadoAmbiente;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class Ventana  extends JFrame {


    /**
     *
     */
    private static final long serialVersionUID = 1L;



    public Ventana(EstadoAmbiente e) {

        init(e);
    }

    public void init(EstadoAmbiente estadoAmbiente) {
        System.out.println("----------------------------------------------------");

        //PANEL PRINCIPAL
        JPanel panelPrincipal= new JPanel();
        panelPrincipal.setBounds(0, 0, 1000, 500);
        panelPrincipal.setLayout(null);


        //DIBUJAR ZOMBIES

        estadoAmbiente.getZombies().forEach(z->{
            System.out.println("dibujar zombie");

            JLabel zombie = new JLabel();
            ImageIcon zombieIcon= new ImageIcon("src/Interface/Imagenes/zombie1.png");
            switch((z.getPoder())) {

                case -1:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie1.png");
                    break;
                case -2:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie2.png");
                    break;
                case -3:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie3.png");
                    break;
                case -4:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie4.png");
                    break;
                case -5:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie5.png");
                    break;
                case -6:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie6.png");
                    break;
            }

            zombie.setIcon(new ImageIcon(zombieIcon.getImage().getScaledInstance(80, 80, 0)));

            zombie.setBounds(115*(z.getPosicion().getColumna()-1), 100*(z.getPosicion().getFila()-1), 80, 80);

            panelPrincipal.add(zombie);

        });



        //DIBUJAR AGENTE
        JLabel planta = new JLabel();
        ImageIcon plantaIcon = new ImageIcon("src/Interface/Imagenes/planta.png");

        planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(80, 80, 0)));
        planta.setBounds((estadoAmbiente.getPosicionPlanta().getColumna()-1)*115, (estadoAmbiente.getPosicionPlanta().getFila()-1)*100, 80, 80); // le sumo 70 en x para avanzar y 80 en y


        panelPrincipal.add(planta);

        //DIBUJAR GIRASOLES

        estadoAmbiente.getGirasoles().forEach((g)->{
            JLabel girasol = new JLabel();
            ImageIcon girasolIcon= new ImageIcon("src/Interface/Imagenes/girasol.png");

            girasol.setIcon(new ImageIcon(girasolIcon.getImage().getScaledInstance(80, 80, 0)));

            girasol.setBounds(115*(g.getPosicion().getColumna()-1), 100*(g.getPosicion().getColumna()-1), 80, 80);

            panelPrincipal.add(girasol);


        });

        //FONDO
        JLabel fondo = new JLabel();
        ImageIcon fondoIcon = new ImageIcon("src/Interface/Imagenes/fondoJuego2.png");
        fondo.setIcon(new ImageIcon(fondoIcon.getImage().getScaledInstance(panelPrincipal.getWidth(), panelPrincipal.getHeight(), 0)));
        fondo.setBounds(0, 0, 1200, 500);

        panelPrincipal.add(fondo);

        //CONFIGURACION VENTANA PRINCIPAL
        this.getContentPane().add(panelPrincipal);
        this.setSize(1100, 530);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);


    }


    public void actualizar(EstadoAmbiente estadoAmbiente) {

        this.getContentPane().removeAll();
        //PANEL PRINCIPAL
        JPanel panelPrincipal= new JPanel();
        panelPrincipal.setBounds(0, 0, 1000, 500);
        panelPrincipal.setLayout(null);


        //DIBUJAR ZOMBIES

        estadoAmbiente.getZombies().forEach(z->{
            System.out.println("dibujar zombie");

            JLabel zombie = new JLabel();
            ImageIcon zombieIcon= new ImageIcon("src/Interface/Imagenes/zombie1.png");
            switch((z.getPoder())) {

                case 1:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie1.png");
                    break;
                case 2:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie2.png");
                    break;
                case 3:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie3.png");
                    break;
                case 4:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie4.png");
                    break;
                case 5:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie5.png");
                    break;
                case 6:
                    zombieIcon = new ImageIcon("src/Interface/Imagenes/zombie6.png");
                    break;
            }

            zombie.setIcon(new ImageIcon(zombieIcon.getImage().getScaledInstance(80, 80, 0)));

            zombie.setBounds(115*(z.getPosicion().getColumna()-1), 100*(z.getPosicion().getFila()-1), 80, 80);

            panelPrincipal.add(zombie);

        });



        //DIBUJAR AGENTE
        JLabel planta = new JLabel();
        ImageIcon plantaIcon = new ImageIcon("src/Interface/Imagenes/planta.png");

        planta.setIcon(new ImageIcon(plantaIcon.getImage().getScaledInstance(80, 80, 0)));
        planta.setBounds((estadoAmbiente.getPosicionPlanta().getColumna()-1)*115, (estadoAmbiente.getPosicionPlanta().getFila()-1)*100, 80, 80); // le sumo 70 en x para avanzar y 80 en y


        panelPrincipal.add(planta);

        //DIBUJAR GIRASOLES

        estadoAmbiente.getGirasoles().forEach((g)->{
            JLabel girasol = new JLabel();
            ImageIcon girasolIcon= new ImageIcon("src/Interface/Imagenes/girasol.png");

            girasol.setIcon(new ImageIcon(girasolIcon.getImage().getScaledInstance(80, 80, 0)));

            girasol.setBounds(115*(g.getPosicion().getColumna()-1), 100*(g.getPosicion().getColumna()-1), 80, 80);

            panelPrincipal.add(girasol);


        });

        //FONDO
        JLabel fondo = new JLabel();
        ImageIcon fondoIcon = new ImageIcon("src/Interface/Imagenes/fondoJuego2.png");
        fondo.setIcon(new ImageIcon(fondoIcon.getImage().getScaledInstance(panelPrincipal.getWidth(), panelPrincipal.getHeight(), 0)));
        fondo.setBounds(0, 0, 1200, 500);

        panelPrincipal.add(fondo);


        //CONFIGURACION VENTANA PRINCIPAL
        this.getContentPane().add(panelPrincipal);


    }

}
