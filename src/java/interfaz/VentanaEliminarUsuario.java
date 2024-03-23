package interfaz;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

public class VentanaEliminarUsuario extends JFrame {
    JLabel userNameLabel;
    JLabel adminPasswordLabel;
    JTextField userNameField;
    JTextField adminPasswordField;
    JButton deleteUserButton;

    public VentanaEliminarUsuario() {
        Administrador currentAdmin = new Administrador();
        setSize(500, 250);
        getContentPane().setLayout(new GridLayout(2, 2));
        this.setTitle("Nuevo usuario");
        this.setVisible(true);

        userNameLabel = new JLabel("Nombre de usuario");
        adminPasswordLabel = new JLabel("Contraseña");

        userNameField = new JTextField();
        adminPasswordField = new JTextField();

        deleteUserButton = new JButton("Eliminar usuario");

        add(userNameLabel);
        add(userNameField);
        add(adminPasswordLabel);
        add(adminPasswordField);
        add(deleteUserButton);

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userNameField.getText();
                String password = adminPasswordField.getText();
                try {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que quiere eliminar al usuario " + userNameField.getText() + "?");
                    if (JOptionPane.OK_OPTION == confirm)
                        currentAdmin.deleteUser(userNameField.getText());
                    else
                        JOptionPane.showMessageDialog(null, "Usuario no eliminado.");
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
