package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaNuevoUsuario extends JFrame implements Bordes, Fuentes, Iconos {
    // Etiquetas
    JLabel title;
    JLabel userNameLabel;
    JLabel userPasswordLabel;
    JLabel userTypeLabel;

    // Campos de texto
    JTextField userNameField;
    JTextField userPasswordField;

    // Combo boxes
    JComboBox userTypeComboBox;

    // Botones
    JButton goBackButton;
    JButton createUserButton;

    // Paneles
    JPanel upperPanel;
    JPanel inputPanel;
    JPanel createButtonPanel;

    public VentanaNuevoUsuario(Administrador administrador) {
        // Configuración de la ventana
        setSize(500, 270);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Nuevo usuario");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(iconoVentana);
        setVisible(true);

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Definición de etiqueta de título
        title = new JLabel("Nuevo usuario");
        title.setFont(fuenteTitulo);

        // Configuración de panel superior
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);

        upperPanel.add(title, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        // Configuración de los componentes del panel de input del usuario
        userNameLabel = new JLabel("Nombre de usuario (*)");
        userNameLabel.setFont(fuenteBotonesEtiquetas);

        userPasswordLabel = new JLabel("Contraseña (*)");
        userPasswordLabel.setFont(fuenteBotonesEtiquetas);

        userTypeLabel = new JLabel("Tipo de usuario (*)");
        userTypeLabel.setFont(fuenteBotonesEtiquetas);

        userNameField = new JTextField();
        userNameField.setFont(fuenteCampoTexto);

        userPasswordField = new JTextField();
        userPasswordField.setFont(fuenteCampoTexto);

        String[] userTypes = {"ADMINISTRADOR", "ORGANIZADOR", "MONITOR"};
        userTypeComboBox = new JComboBox<>(userTypes);
        userTypeComboBox.setFont(fuenteCampoTexto);

        createUserButton = new JButton("Crear usuario");
        createUserButton.setFont(fuenteBotonesEtiquetas);
        createUserButton.setPreferredSize(new Dimension(175, 30));

        // Configuración del panel de input del usuario
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(borde);

        // COnfiguración del panel de botón de crear
        createButtonPanel = new JPanel();
        createButtonPanel.setLayout(new FlowLayout());
        createButtonPanel.setBorder(borde);

        inputPanel.add(userNameLabel);
        inputPanel.add(userNameField);
        inputPanel.add(userPasswordLabel);
        inputPanel.add(userPasswordField);
        inputPanel.add(userTypeLabel);
        inputPanel.add(userTypeComboBox);

        createButtonPanel.add(createUserButton);

        // Se añaden los diferentes elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (userNameField.getText().matches("^\\s*$")
                        || userPasswordField.getText().matches("^\\s*$")) {
                    new CustomJOptionPane("Los campos Nombre y Password son obligatorios");

                } else {
                    String name = userNameField.getText();
                    String password = userPasswordField.getText();
                    String type = String.valueOf(userTypeComboBox.getSelectedItem());

                    try {
                        if (administrador.createUser(name, password, type) == 0) {
                            new CustomJOptionPane("Se ha creado el usuario");
                            userNameField.setText("");
                            userPasswordField.setText("");
                            userTypeComboBox.setSelectedItem(userTypeComboBox.getItemAt(0));
                        }

                    } catch (JSchException | SQLException ex) {
                        new CustomJOptionPane("ERROR");

                    }
                }
            }
        });
    }
}
