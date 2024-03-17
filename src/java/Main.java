
import interfaz.VentanaInicio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Datos de conexión
        String url = "jdbc:mysql://LAPTOP-QKDBRERA:3306/OLYMPULL_DB?useSSL=false";
        String usuario = "rrrguez";
        String contrasea = "adminPassword"; // Reemplaza 'CONTRASEÑA' con la contraseña real

        // Establecer conexión
        try {
            Connection conexion = DriverManager.getConnection(url, usuario, contrasea);
            System.out.println("Conexión establecida con la base de datos.");

            // Aquí puedes realizar consultas, actualizaciones, etc.

            // No olvides cerrar la conexión cuando hayas terminado
            conexion.close();
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }

        // VentanaInicio inicio = new VentanaInicio();
    }
}


/**
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        // Librería de MySQL
        String driver = "com.mysql.jdbc.Driver";

        // Nombre de la base de datos
        String database = "OLYMPULL_DB";

        // Host
        String hostname = "192.168.56.1";

        // Puerto
        String port = "3306";

        // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
        String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";

        // Nombre de usuario
        String username = "rrrguez";

        // Clave de usuario
        String password = "adminPassword";

        Connection conn = null;

        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
}
 */