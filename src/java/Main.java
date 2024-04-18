import interfaz.VentanaInicio;
import interfaz.administrador.VentanaAdministrador;
import usuarios.Administrador;

import java.sql.*;

public class Main {
    public static Connection conn;
    public static void main(String[] args) {
        // Abrir ventana principal
        // new VentanaInicio();
        Administrador administrador = new Administrador("admin1", "admin1");
        new VentanaAdministrador(administrador);
    }
}
