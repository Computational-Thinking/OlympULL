import com.jcraft.jsch.JSchException;
import interfaz.VentanaInicio;
import interfaz.administrador.VentanaAdministrador;
import usuarios.Administrador;

import java.sql.*;

public class Main {
    public static Connection conn;
    public static void main(String[] args) throws JSchException, SQLException {
        // Abrir ventana principal
        new VentanaInicio();

    }
}
