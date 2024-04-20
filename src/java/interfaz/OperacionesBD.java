package interfaz;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public interface OperacionesBD {
    // Conexión a MV remota
    String sshHost = "10.6.130.204";
    String sshUser = "usuario";
    String sshPassword = "Usuario";
    int sshPort = 22; // Puerto SSH por defecto
    int localPort = 3307; // Puerto local para el túnel SSH
    String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
    int remotePort = 3306; // Puerto MySQL en la máquina remota
    JSch jsch = new JSch();

    // Conexión a MySQL por SSH
    String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
    String dbUser = "root";
    String dbPassword = "root";

    // Método para insertar en la base de datos
    default void insert(String table, String data) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "INSERT INTO " + table + " VALUES(" + data + ")";
        int rowsAffected = stmt.executeUpdate(sql);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido insertar en la tabla");
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();
    }

    // Método para actualizar valor en la tabla
    default void update(String table, String setClause, String whereClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "UPDATE " + table + " " + setClause + " " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido actualizar la tabla");
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();
    }

    default void delete(String table, String whereClause) throws JSchException, SQLException {
        // Conexión SSH a la MV remota
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Crear la sentencia
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "DELETE FROM " + table + " " + whereClause;
        int rowsAffected = stmt.executeUpdate(sql);

        if (!(rowsAffected > 0)) {
            new CustomJOptionPane("No se ha podido eliminar el registro de la tabla");
        }

        // Se cierra la conexión
        stmt.close();
        conn.close();
        session.disconnect();
    }
}
