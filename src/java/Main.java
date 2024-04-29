import interfaz.VentanaInicio;
import interfaz.organizador.VentanaOrganizador;
import usuarios.Organizador;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Organizador organizador = new Organizador("organizador1", "organizador1", new ArrayList<>());
        // Abrir ventana principal
        //new VentanaInicio();
        new VentanaOrganizador(organizador);
    }
}
