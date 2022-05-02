package Interface;

import Dominio.EstadoAmbiente;
import Dominio.Girasol;
import Dominio.Posicion;
import Dominio.Zombie;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UiMain {


    public static void main(String[] args) throws InterruptedException {


        EstadoAmbiente e= new EstadoAmbiente();

        List<Zombie> listaZomies = new ArrayList<>();
        List<Girasol> girasoles = new ArrayList<>();



       // girasoles.add(new Girasol(new Posicion(1,1),5));
       // girasoles.add(new Girasol(new Posicion(5,1),5));


        e.setGirasoles(girasoles);
        e.setZombies(listaZomies);
        e.setPosicionPlanta(new Posicion(5,1));

        Ventana ventana = new Ventana(e);


        System.out.println("----------------------ESTADO 1------------------------------");
        ventana.setVisible(true);

        TimeUnit.SECONDS.sleep(3);

        System.out.println("----------------------ESTADO 2------------------------------");
        listaZomies.add(new Zombie(new Posicion(5,10),-5));
        listaZomies.add(new Zombie(new Posicion(5,6),-4));
        girasoles.add(new Girasol(new Posicion(1,1),5));
        e.setPosicionPlanta(new Posicion(5,2));
        ventana.actualizar(e);

        TimeUnit.SECONDS.sleep(3);

        System.out.println("---------------------- ESTADO 3------------------------------");
         listaZomies.add(new Zombie(new Posicion(2,9),-2));
         listaZomies.add(new Zombie(new Posicion(3,8),-5));
         listaZomies.add(new Zombie(new Posicion(4,4),-4));
         girasoles.add(new Girasol(new Posicion(1,1),5));
         girasoles.add(new Girasol(new Posicion(5,1),5));
         e.setPosicionPlanta(new Posicion(5,3));
         ventana.actualizar(e);


    }
}