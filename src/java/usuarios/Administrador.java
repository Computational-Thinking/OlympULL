package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.VentanaAdministrador;

import javax.swing.*;
import java.sql.*;

public class Administrador extends Usuario {
    public void createOlympiad(String title, String description, int year, int nUnpluggedMentions, int nUnpluggedTeams, int nPluggedMentions, int nPluggedTeams) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Consulta para añadir la nueva olimpiada
        String sql = "INSERT INTO T_OLIMPIADAS VALUES (" + title + ", " + description + ", " + year + ", " + nPluggedMentions + ", " + nPluggedTeams + "," + nUnpluggedMentions + ", " + nUnpluggedTeams + ")";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            System.out.println("Se ha creado la olimpiada " + title + ".");
        } else {
            System.out.println("No se ha podido crear la olimpiada " + title + ".");
        }

        conn.close();
        session.disconnect();
    }

    public void createExercise(String code, String title, String category, String resources, String type, String material, int year) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar consulta para añadir nuevo ejercicio
        String sql = "INSERT INTO T_OLIMPIADAS VALUES (" + code + "," + title + ", " + category + ", " + resources + ", " + type + "," + material + "," + year + ")";
        int rowsAffected = stmt.executeUpdate(sql);

        if (rowsAffected > 0) {
            System.out.println("Se ha creado el usuario.");
        } else {
            System.out.println("No se ha podido crear el usuario.");
        }

        conn.close();
        session.disconnect();
    }

    public void createUser(String name, String passw, String type) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT NOMBRE_USUARIO FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "ERROR. Ya existe un usuario con este nombre de usuario.");
        } else {
            String sql2 = "INSERT INTO T_USUARIOS(NOMBRE_USUARIO, PASSWORD, TIPO_USUARIO) VALUES ('" + name + "', '" + passw + "', '" + type + "');";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                System.out.println("Se ha creado el usuario.");
            } else {
                System.out.println("No se ha podido crear el usuario.");
            }
            JOptionPane.showMessageDialog(null, "Usuario dado de alta con éxito.");
        }
        // Consulta para añadir el usuario a la base de datos


        conn.close();
        session.disconnect();
    }

    public void deleteUser(String name) throws JSchException, SQLException {
        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

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
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        // Debugger
        Statement stmt = conn.createStatement();

        // Ejecutar la consulta SQL
        String sql1 = "SELECT NOMBRE_USUARIO FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + name + "'";
        ResultSet rs = stmt.executeQuery(sql1);

        if (rs.next()) {
            String sql2 = "DELETE FROM T_USUARIOS WHERE NOMBRE_USUARIO = '" + name + "';";
            int rowsAffected = stmt.executeUpdate(sql2);

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, "Usuario eliminado con éxito.");

            } else {
                JOptionPane.showMessageDialog(null, "ERROR. No se ha podido eliminar el usuario.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "ERROR. No existe el usuario.");
        }

        conn.close();
        session.disconnect();
    }
}
