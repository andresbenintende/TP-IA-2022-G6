package UI;

import Dominio.EstadoAmbiente;


public class Main {

    public static void main(String[] args) {

        EstadoAmbiente estadoAmbiente= new EstadoAmbiente();

        VentanaConfiguracion ventana = new VentanaConfiguracion(estadoAmbiente);

        ventana.setVisible(true);
    }
}
