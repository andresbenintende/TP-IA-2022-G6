package UI;

import Dominio.EstadoAmbiente;

public class Main {

    public static void main(String[] args) {

        EstadoAmbiente estadoAmbiente= new EstadoAmbiente();

        VentanaPrincipal ventana = new VentanaPrincipal(estadoAmbiente);

        ventana.setVisible(true);

    }
}
