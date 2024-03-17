/**
import interfaz.VentanaInicio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        // Datos de conexión
        String url = "jdbc:mysql://192.168.56.1:3306/OLYMPULL_DB";
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

import com.jcraft.jsch.*;
import interfaz.VentanaInicio;

import java.sql.*;

public class Main {
    public static Connection conn;
    public static void main(String[] args) {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        try {
            // Conexión SSH a la MV remota
            JSch jsch = new JSch();
            Session session = jsch.getSession(sshUser, sshHost, sshPort);
            session.setPassword(sshPassword);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            // Debugger
            System.out.println("Conexión con la máquina establecida");

            // Abrir un túnel SSH al puerto MySQL en la máquina remota
            session.setPortForwardingL(localPort, remoteHost, remotePort);

            // Conexión a MySQL a través del túnel SSH
            String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
            String dbUser = "root";
            String dbPassword = "root";
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

            // Debugger
            System.out.println("Conexión con MySQL establecida");

/**
            // Consulta SQL de prueba
            Statement stmt = conn.createStatement();
            String sql = "INSERT INTO PRUEBA (COLUMNA_1, COLUMNA_2) VALUES (10, 'ADIOS')";
            int rowsAffected = stmt.executeUpdate(sql);

            // Verificar el número de filas afectadas
            if (rowsAffected > 0) {
                System.out.println("Inserción exitosa. Se insertaron " + rowsAffected + " filas.");
            } else {
                System.out.println("La inserción no tuvo éxito. No se insertaron filas.");
            }

            // Cerrar recursos
            stmt.close();
 */
            // Cerrar conexión a MySQL
            conn.close();

            // Cerrar la conexión SSH
            session.disconnect();

            // Abrir ventana principal
            VentanaInicio ventana = new VentanaInicio();


        } catch (JSchException | SQLException e) {
            e.printStackTrace();
        }
    }
}
