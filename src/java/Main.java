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
