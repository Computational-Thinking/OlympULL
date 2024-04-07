package usuarios;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.sql.*;

public class Monitor extends Usuario {
    int exerciseCode;

    public Monitor(String nombreUsuario, String password) {
        this.userName = nombreUsuario;
        this.password = password;
        this.userType = "monitor";

        this.exerciseCode = 1;
    }

    public void stablishScale(int[] points, String[] tags) throws SQLException, JSchException {
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
        String sql1 = "INSERT INTO T_BAREMOS VALUES(" + exerciseCode + ", '" + tags + "', '" + points + "');";
        int rowsAffected = stmt.executeUpdate(sql1);

        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(null, "Se ha establecido el baremo con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "No se ha podido establecer el baremo. Ya existe un baremo para este ejercicio. Póngase en contacto con el administrador si quiere modificarlo.");
        }

        conn.close();
        session.disconnect();
    }

}
