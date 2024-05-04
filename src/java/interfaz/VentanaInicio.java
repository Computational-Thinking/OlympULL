package interfaz;

import interfaz.administrador.VentanaAdministrador;
import interfaz.custom_components.*;
import interfaz.monitor.VentanaMonitor;
import interfaz.organizador.VentanaOrganizador;
import usuarios.Administrador;
import usuarios.Monitor;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;

import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class VentanaInicio extends JFrame implements Bordes, Fuentes, Iconos, OperacionesBD {
    // Credentials stuff
    JLabel userTag = new CustomFieldLabel("Usuario");
    JLabel passwordTag = new CustomFieldLabel("Contraseña");
    JTextField userField = new CustomTextField("");
    JPasswordField passwordField = new CustomPasswordField("");
    CustomButton loginButton = new CustomButton("Iniciar sesión");

    //
    CustomButton verRanking = new CustomButton("Ver ranking");

    JPanel logoPanel;
    JPanel credentialsPanel;
    JPanel loginPanel;
    JLabel olympullLogo;

    public VentanaInicio() {
        // Configuración de la ventana
        setSize(500, 315);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Inicio");
        setLocationRelativeTo(null);
        setResizable(false);

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
        credentialsPanel = new JPanel();
        credentialsPanel.setBackground(new Color(255, 255, 255));
        credentialsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        credentialsPanel.add(userTag);
        credentialsPanel.add(userField);
        credentialsPanel.add(passwordTag);
        credentialsPanel.add(passwordField);

        credentialsPanel.setBorder(borde);

        add(credentialsPanel);

        JPanel dummy1 = new JPanel();
        dummy1.setLayout(new FlowLayout());
        dummy1.add(verRanking);
        dummy1.setBorder(borde);

        loginPanel = new JPanel();
        loginPanel.setBackground(new Color(255, 255, 255));
        loginPanel.setLayout(new BorderLayout());
        JPanel dummy = new JPanel();
        dummy.setLayout(new FlowLayout());
        dummy.add(loginButton);
        dummy.setBackground(new Color(255, 255, 255));

        loginPanel.add(credentialsPanel, BorderLayout.CENTER);
        loginPanel.add(dummy, BorderLayout.SOUTH);
        loginPanel.setBorder(borde);

        add(loginPanel, BorderLayout.CENTER);
        add(dummy1, BorderLayout.SOUTH);

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
                            new MessageJOptionPane("Contraseña correcta. Iniciando sesión...");

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
                            new ErrorJOptionPane("Contraseña incorrecta. Pruebe otra vez.");

                        }

                    } else {
                        new ErrorJOptionPane("No existe el usuario " + userField.getText() + ". Para darse de alta, póngase en contacto con un administrador.");

                    }

                } catch (SQLException i) {
                    new ErrorJOptionPane(i.getMessage());
                    dispose();

                }
            }
        });

        verRanking.addActionListener(e -> {
            new MessageJOptionPane("Funcionalidad en obras. Disculpe las molestias.");
        });

        this.setVisible(true);
    }

}
