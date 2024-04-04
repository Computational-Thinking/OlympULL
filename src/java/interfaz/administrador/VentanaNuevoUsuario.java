package interfaz.administrador;

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
    JPanel inputPanel;

    public VentanaNuevoUsuario(Administrador administrador) {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
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

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(userNameLabel);
        inputPanel.add(userNameField);
        inputPanel.add(userPasswordLabel);
        inputPanel.add(userPasswordField);
        inputPanel.add(userTypeLabel);
        inputPanel.add(userTypeComboBox);

        add(inputPanel);
        add(createUserButton);

        createUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userNameField.getText();
                String password = userPasswordField.getText();
                String type = String.valueOf(userTypeComboBox.getSelectedItem());
                try {
                    administrador.createUser(name, password, type);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
