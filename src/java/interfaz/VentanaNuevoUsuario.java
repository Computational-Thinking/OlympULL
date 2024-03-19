package interfaz;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaNuevoUsuario extends JFrame {
    JLabel userNameLabel;
    JLabel userPasswordLabel;
    JLabel userTypeLabel;
    JTextField userNameField;
    JTextField userPasswordField;
    JComboBox userTypeComboBox;

    JButton createUserButton;
    public VentanaNuevoUsuario() {
        Administrador currentAdmin = new Administrador();
        setSize(500, 250);
        getContentPane().setLayout(new GridLayout(2, 2));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Nuevo usuario");
        this.setVisible(true);

        userNameLabel = new JLabel("Nombre de usuario");
        userPasswordLabel = new JLabel("Contraseña");
        userTypeLabel = new JLabel("Tipo de usuario");

        userNameField = new JTextField();
        userPasswordField = new JTextField();
        String[] userTypes = {"ADMINISTRADOR", "ORGANIZADOR", "MONITOR"};
        userTypeComboBox = new JComboBox<>(userTypes);

        createUserButton = new JButton("Crear usuario");

        add(userNameLabel);
        add(userNameField);
        add(userPasswordLabel);
        add(userPasswordField);
        add(userTypeLabel);
        add(userTypeComboBox);
        add(createUserButton);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userNameField.getText();
                String password = userPasswordField.getText();
                String type = String.valueOf(userTypeComboBox.getSelectedItem());
                try {
                    currentAdmin.createUser(name, password, type);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
