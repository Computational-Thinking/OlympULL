package interfaz.administrador;

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
    JPanel inputsPanel;
    JPanel buttonPanel;

    public VentanaEliminarUsuario(Administrador administrador) {
        setSize(500, 200);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Eliminar usuario");
        this.setVisible(true);

        userNameLabel = new JLabel("Nombre de usuario");

        userNameField = new JTextField();

        deleteUserButton = new JButton("Eliminar usuario");

        inputsPanel = new JPanel();
        inputsPanel.setLayout(new GridLayout(1, 2));
        inputsPanel.add(userNameLabel);
        inputsPanel.add(userNameField);

        add(inputsPanel);
        add(deleteUserButton);

        deleteUserButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que quiere eliminar al usuario " + userNameField.getText() + "?");
                    if (JOptionPane.OK_OPTION == confirm)
                        administrador.deleteUser(userNameField.getText());
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
