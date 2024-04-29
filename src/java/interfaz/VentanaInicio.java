package interfaz;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.administrador.VentanaAdministrador;
import interfaz.monitor.VentanaMonitor;
import interfaz.organizador.VentanaOrganizador;
import usuarios.Administrador;
import usuarios.Monitor;
import usuarios.Organizador;
import usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

public class VentanaInicio extends JFrame implements Bordes, Fuentes, Iconos, OperacionesBD {
    JPanel logoPanel;
    JPanel credentialsPanel;
    JPanel loginPanel;
    JLabel olympullLogo;
    JLabel userTag;
    JLabel passwordTag;
    JTextField userField;
    JPasswordField passwordField;
    JButton loginButton;
    JButton verRanking;

    public VentanaInicio() {
        // Configuración de la ventana
        setSize(500, 300);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Inicio");
        setLocationRelativeTo(null);

        logoPanel = new JPanel();
        logoPanel.setBackground(new Color(255, 255, 255));

        // Add logo to the label that will be on the top of the window
        ImageIcon logo = new ImageIcon("images/logo olympull v2.png");
        Image scalatedLogo = logo.getImage().getScaledInstance(268, 65, Image.SCALE_SMOOTH);
        logo = new ImageIcon(scalatedLogo);
        olympullLogo = new JLabel(logo);
        logoPanel.add(olympullLogo);
        add(logoPanel, BorderLayout.NORTH);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        // Configure credentials panel
        userTag = new JLabel("Usuario");
        userTag.setFont(fuenteBotonesEtiquetas);

        passwordTag = new JLabel("Contraseña");
        passwordTag.setFont(fuenteBotonesEtiquetas);
        
        userField = new JTextField();
        userField.setFont(fuenteCampoTexto);
        passwordField = new JPasswordField();
        passwordField.setFont(fuenteBotonesEtiquetas);

        credentialsPanel = new JPanel();
        credentialsPanel.setBackground(new Color(255, 255, 255));
        credentialsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        credentialsPanel.add(userTag);
        credentialsPanel.add(userField);
        credentialsPanel.add(passwordTag);
        credentialsPanel.add(passwordField);

        credentialsPanel.setBorder(borde);

        add(credentialsPanel);

        verRanking = new JButton("Ver ranking");
        verRanking.setFont(fuenteBotonesEtiquetas);

        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255));
        loginButton = new JButton("Iniciar sesión");
        loginButton.setFont(fuenteBotonesEtiquetas);

        loginPanel.add(loginButton);

        add(loginPanel);
        add(verRanking);

        // Action listener
        loginButton.addActionListener(e -> {

            // Lógica para comprobar la existencia de un usuario
            if (userField.getText() != null && passwordField.getPassword() != null) {
                try {
                    String whereClause = "WHERE NOMBRE='" + userField.getText() + "';";
                    ResultSet users = selectCol("T_USUARIOS", "NOMBRE", whereClause);

                    if (users.next()) {
                        String id = users.getString("NOMBRE");
                        String password = (passwordField.getText());
                        whereClause = "WHERE NOMBRE='" + id + "' AND PASSWORD='" + password + "'";
                        users = selectCol("T_USUARIOS", "*", whereClause);

                        if (users.next()) {
                            new CustomJOptionPane("Contraseña correcta. Iniciando sesión...");

                            if (users.getString("TIPO").equals("ADMINISTRADOR")) {
                                String name = users.getString("NOMBRE");
                                String pass = users.getString("PASSWORD");

                                Administrador usuario = new Administrador(name, pass);

                                new VentanaAdministrador(usuario);
                                dispose();

                            } else if (users.getString("TIPO").equals("MONITOR")) {
                                String name = users.getString("NOMBRE");
                                ArrayList<String> exercises = new ArrayList<>();

                                whereClause = "WHERE NOMBRE='" + name + "';";
                                users = selectCol("T_MONITORES", "EJERCICIO", whereClause);

                                while (users.next()) {
                                    exercises.add(users.getString("EJERCICIO"));

                                }

                                Monitor usuario = new Monitor(name, password, exercises);
                                new VentanaMonitor(usuario);
                                dispose();

                            } else if (users.getString("TIPO").equals("ORGANIZADOR")) {
                                String name = users.getString("NOMBRE");
                                ArrayList<String> itineraries = new ArrayList<>();

                                whereClause = "WHERE ORGANIZADOR='" + name + "';";
                                users = selectCol("T_ORGANIZADORES", "ITINERARIO", whereClause);

                                while (users.next()) {
                                    itineraries.add(users.getString("ITINERARIO"));

                                }

                                Organizador usuario = new Organizador(name, password, itineraries);
                                new VentanaOrganizador(usuario);
                                dispose();
                            }

                            userField.setText("");
                            passwordField.setText("");

                        } else {
                            new CustomJOptionPane("Contraseña incorrecta. Pruebe otra vez.");

                        }

                    } else {
                        new CustomJOptionPane("No existe el usuario " + userField.getText() + ". Para darse de alta, póngase en contacto con un administrador.");

                    }

                } catch (SQLException i) {
                    new CustomJOptionPane("ERROR - " + i.getMessage());
                    dispose();

                }
            }
        });

        verRanking.addActionListener(e -> {

        });

        this.setVisible(true);
    }

}
