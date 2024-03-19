package interfaz;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import javax.swing.*;
import java.awt.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.ImageIcon;

public class VentanaInicio extends JFrame {
    JPanel logoPanel;
    JPanel credentialsPanel;
    JPanel loginPanel;
    JLabel olympullLogo;
    JLabel userTag;
    JLabel passwordTag;
    JTextField userField;
    JPasswordField passwordField;
    JButton loginButton;

    public VentanaInicio() {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Bienvenido a OlympULL");
        logoPanel = new JPanel();
        logoPanel.setBackground(new Color(249, 233, 93));

        // Add logo to the label that will be on the top of the window
        ImageIcon logo = new ImageIcon("images/logo olympull.png");
        Image originalIcon = logo.getImage();
        Image escalatedLogo = originalIcon.getScaledInstance(268, 56, Image.SCALE_SMOOTH);
        logo = new ImageIcon(escalatedLogo);
        olympullLogo = new JLabel(logo);
        logoPanel.add(olympullLogo);
        add(logoPanel, BorderLayout.NORTH);

        // Configure credentials panel
        userTag = new JLabel("Usuario");
        passwordTag = new JLabel("Contraseña");

        userField = new JTextField();
        passwordField = new JPasswordField();

        credentialsPanel = new JPanel();
        credentialsPanel.setBackground(new Color(249, 233, 93));
        credentialsPanel.setLayout(new GridLayout(2, 2));
        credentialsPanel.add(userTag);
        credentialsPanel.add(userField);
        credentialsPanel.add(passwordTag);
        credentialsPanel.add(passwordField);

        add(credentialsPanel);

        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(249, 233, 93));
        loginButton = new JButton("Iniciar sesión");
        loginPanel.add(loginButton);
        add(loginPanel);

        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Action listener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // Lógica para comprobar la existencia de un usuario
                if (userField.getText() != null && passwordField.getText() != null) {
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
                        Connection conn;
                        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

                        // Debugger
                        Statement stmt = conn.createStatement();

                        // Ejecutar la consulta SQL
                        String sql = "SELECT NOMBRE_USUARIO FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + userField.getText() + "'";
                        ResultSet rs = stmt.executeQuery(sql);

                        if (rs.next()) {
                            System.out.println("Existe el usuario");
                            String id = rs.getString("NOMBRE_USUARIO");
                            String sql2 = "SELECT PASSWORD FROM T_USUARIOS WHERE NOMBRE_USUARIO = " + "'" + id + "' AND PASSWORD = " + "'" + passwordField.getText() + "'";
                            rs = stmt.executeQuery(sql2);
                            if (rs.next()) {
                                System.out.println("Contraseña correcta. Iniciando sesión...");
                                JOptionPane.showMessageDialog(null, "Contraseña correcta. Iniciando sesión...");
                                VentanaAdministrador ventena = new VentanaAdministrador();
                            } else {
                                System.out.println("Contraseña incorrecta. Pruebe otra vez.");
                                JOptionPane.showMessageDialog(null, "Contraseña incorrecta. Pruebe otra vez.");

                            }
                        } else {
                            System.out.println("No existe el usuario " + userField.getText() + ". Para darse de alta, póngase en contacto con un administrador.");
                            JOptionPane.showMessageDialog(null, "No existe el usuario " + userField.getText() + ". Para darse de alta, póngase en contacto con un administrador.");
                        }

                        conn.close();
                        session.disconnect();
                    } catch (JSchException | SQLException i) {
                        i.printStackTrace();
                    }
                }
            }
        });
        this.setVisible(true);
    }

}
