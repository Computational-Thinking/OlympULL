package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class Usuario {
    String userName;
    String password;
    String userType;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public void changePassword(String newPassword) {
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
        Session session = null;
        try {
            session = jsch.getSession(sshUser, sshHost, sshPort);
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            session.connect();
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        try {
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException ex) {
            throw new RuntimeException(ex);
        }

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        // Ejecutar consulta para cambiar contraseña
        String sql = "UPDATE T_USUARIOS SET PASSWORD='" + newPassword + "' WHERE NOMBRE_USUARIO='" + userName + "';";

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        int rowsAffected = 0;
        try {
            rowsAffected = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha cambiado la contraseña.");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido cambiar la contrasña.");
        }

        try {
            conn.close();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        session.disconnect();
    }
}
