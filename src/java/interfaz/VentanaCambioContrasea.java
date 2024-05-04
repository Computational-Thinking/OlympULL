package interfaz;

import interfaz.administrador.VentanaAdministrador;
import interfaz.custom_components.Bordes;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fuentes;
import interfaz.custom_components.Iconos;
import interfaz.monitor.VentanaMonitor;
import interfaz.organizador.VentanaOrganizador;
import usuarios.Administrador;
import usuarios.Monitor;
import usuarios.Organizador;
import usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class VentanaCambioContrasea extends JFrame implements Bordes, Fuentes, Iconos {
    // Etiquetas
    JLabel usuarioLabel;
    JLabel newPassLabel;
    JLabel confirmNewPassLabel;
    JLabel usuarioField;
    JLabel titleLabel;

    // Password field
    JPasswordField newPassField;
    JPasswordField confirmNewPassField;

    // Botones
    JButton confirmButton;
    JButton goBackButton;

    // Paneles
    JPanel fieldsPanel;
    JPanel buttonPanel;
    JPanel upperPanel;

    public VentanaCambioContrasea(Usuario usuario) {
        setSize(475, 275);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Panel Administrador");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);
        setVisible(true);

        // Panel superior
        goBackButton = new JButton("< Desconectar");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(120, 30));

        titleLabel = new JLabel("Cambiar contraseña");
        titleLabel.setFont(fuenteTitulo);

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);

        upperPanel.add(titleLabel, BorderLayout.WEST);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        // Panel de inputs
        usuarioLabel = new JLabel("Usuario");
        usuarioLabel.setFont(fuenteBotonesEtiquetas);

        usuarioField = new JLabel(usuario.getUserName());
        usuarioField.setFont(fuenteCampoTexto);

        newPassLabel = new JLabel("Nueva contraseña");
        newPassLabel.setPreferredSize(new Dimension(200, 30));

        newPassField = new JPasswordField();
        newPassField.setPreferredSize(new Dimension(200, 30));

        confirmNewPassLabel = new JLabel("Confirmar nueva contraseña");
        confirmNewPassLabel.setPreferredSize(new Dimension(200, 30));

        confirmNewPassField = new JPasswordField();
        confirmNewPassField.setPreferredSize(new Dimension(200, 30));

        fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 5, 5));
        fieldsPanel.setBorder(borde);

        fieldsPanel.add(usuarioLabel);
        fieldsPanel.add(usuarioField);
        fieldsPanel.add(newPassLabel);
        fieldsPanel.add(newPassField);
        fieldsPanel.add(confirmNewPassLabel);
        fieldsPanel.add(confirmNewPassField);

        // Panel de botón
        confirmButton = new JButton("Cambiar contraseña");
        confirmButton.setPreferredSize(new Dimension(200, 30));
        confirmButton.setFont(fuenteBotonesEtiquetas);

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBorder(borde);

        buttonPanel.add(confirmButton);

        add(upperPanel, BorderLayout.NORTH);
        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            if (Objects.equals(usuario.getUserType(), "administrador")) {
                new VentanaAdministrador((Administrador) usuario);
                dispose();

            } else if (Objects.equals(usuario.getUserType(), "organizador")) {
                new VentanaOrganizador((Organizador) usuario);
                dispose();

            } else if (Objects.equals(usuario.getUserType(), "monitor")) {
               new VentanaMonitor((Monitor) usuario);
               dispose();

            } else {
                new ErrorJOptionPane("Ha ocurrido un error inesperado. Abortando...");
                dispose();

            }
        });

        confirmButton.addActionListener(e -> {
            if (Objects.equals(newPassField.getText(), confirmNewPassField.getText())
                    && !Objects.equals(newPassField.getText(), "")) {
                String newPassword = confirmNewPassField.getText();
                usuario.changePassword(newPassword);

                if (Objects.equals(usuario.getUserType(), "administrador")) {
                    new VentanaAdministrador((Administrador) usuario);
                    dispose();

                } else if (Objects.equals(usuario.getUserType(), "organizador")) {
                    new VentanaOrganizador((Organizador) usuario);
                    dispose();

                } else if (Objects.equals(usuario.getUserType(), "monitor")) {
                    new VentanaMonitor((Monitor) usuario);
                    dispose();

                } else {
                    new ErrorJOptionPane("Ha ocurrido un error inesperado. Abortando...");
                    dispose();

                }

            } else {
                new ErrorJOptionPane("Las contraseñas no coinciden");

            }

        });
    }
}
