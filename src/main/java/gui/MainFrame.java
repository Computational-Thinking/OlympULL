package gui;

import gui.custom_components.predefined_elements.Icons;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.labels.CustomLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.option_panes.MessageJOptionPane;
import gui.custom_components.text_fields.CustomPasswordField;
import gui.custom_components.text_fields.CustomTextField;
import gui.user_frames.monitor.MonitorFrame;
import gui.mvc_pattern.View;
import gui.user_frames.organizer.OrganizerFrame;
import operations.DBOperations;
import users.Admin;
import users.Monitor;
import users.Organizer;

import javax.swing.*;
import java.awt.*;

import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.ImageIcon;

public class MainFrame extends CustomFrame implements DBOperations {
    // Labels
    CustomLabel userTag;
    CustomLabel passwordTag;
    JLabel olympullLogo;

    // Text fields
    CustomTextField userField;
    CustomPasswordField passwordField;

    // Buttons
    CustomButton loginButton;
    CustomButton checkRanking;

    // Panels
    CustomPanel logoPanel;
    CustomPanel credentialsPanel;
    CustomPanel loginPanel;
    CustomPanel loginButtonPanel;
    CustomPanel rankingPanel;

    public MainFrame() {
        this.setSize(500, 355);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("OlympULL");

        // Panel de logo
        ImageIcon logo = new ImageIcon(Objects.requireNonNull(Icons.class.getClassLoader().
                getResource("src/main/resources/images/logo_olympull_v2.png")));
        Image scalatedLogo = logo.getImage().getScaledInstance(268, 65, Image.SCALE_SMOOTH);
        logo = new ImageIcon(scalatedLogo);
        olympullLogo = new JLabel(logo);
        logoPanel = new CustomPanel();
        logoPanel.add(olympullLogo);

        // Panel de inicio de sesión
        userTag = new CustomFieldLabel("Usuario");
        passwordTag = new CustomFieldLabel("Contraseña");
        userField = new CustomTextField("");
        passwordField = new CustomPasswordField("");
        loginButton = new CustomButton("Iniciar sesión");

        credentialsPanel = new CustomPanel();
        credentialsPanel.setLayout(new GridLayout(2, 2, 10, 10));

        credentialsPanel.add(userTag);
        credentialsPanel.add(userField);
        credentialsPanel.add(passwordTag);
        credentialsPanel.add(passwordField);

        loginButtonPanel = new CustomPanel();
        loginButtonPanel.setLayout(new FlowLayout());
        loginButtonPanel.add(loginButton);

        loginPanel = new CustomPanel();
        loginPanel.setLayout(new BorderLayout());
        loginPanel.add(credentialsPanel, BorderLayout.CENTER);
        loginPanel.add(loginButtonPanel, BorderLayout.SOUTH);

        // Panel de ranking
        checkRanking = new CustomButton("Ver ranking");

        rankingPanel = new CustomPanel();
        rankingPanel.setLayout(new FlowLayout());
        rankingPanel.add(checkRanking);

        // Se añaden los paneles a la ventana
        add(logoPanel, BorderLayout.NORTH);
        add(loginPanel, BorderLayout.CENTER);
        add(rankingPanel, BorderLayout.SOUTH);

        // Action listeners
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

                                Admin usuario = new Admin(name, pass);

                                new AdminFrame(usuario);
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
                                new MonitorFrame(usuario);
                                dispose();

                            } else if (users.getString("TIPO").equals("ORGANIZADOR")) {
                                String name = users.getString("NOMBRE");
                                ArrayList<String> itineraries = new ArrayList<>();

                                whereClause = "WHERE ORGANIZADOR='" + name + "';";
                                users = selectCol("T_ORGANIZADORES", "ITINERARIO", whereClause);

                                while (users.next()) {
                                    itineraries.add(users.getString("ITINERARIO"));

                                }

                                Organizer usuario = new Organizer(name, password, itineraries);
                                new OrganizerFrame(usuario);
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

        checkRanking.addActionListener(e -> {
            new View();
            dispose();
        });

        this.setVisible(true);
    }
}
