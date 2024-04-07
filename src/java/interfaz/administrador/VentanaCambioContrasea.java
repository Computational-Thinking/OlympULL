package interfaz.administrador;

import usuarios.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class VentanaCambioContrasea extends JFrame {
    JLabel newPassLabel;
    JLabel confirmNewPassLabel;
    JPasswordField newPassField;
    JPasswordField confirmNewPassField;
    JButton confirmButton;
    JPanel fieldsPanel;
    JPanel buttonPanel;

    public VentanaCambioContrasea(Usuario usuario) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Cambio de contraseña");

        newPassLabel = new JLabel("Nueva contraseña");
        newPassLabel.setPreferredSize(new Dimension(200, 30));

        confirmNewPassLabel = new JLabel("Confirmar nueva contraseña");
        confirmNewPassLabel.setPreferredSize(new Dimension(200, 30));

        newPassField = new JPasswordField();
        newPassField.setPreferredSize(new Dimension(200, 30));

        confirmNewPassField = new JPasswordField();
        confirmNewPassField.setPreferredSize(new Dimension(200, 30));

        confirmButton = new JButton("Confirmar");
        confirmButton.setPreferredSize(new Dimension(200, 30));

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridLayout(2, 2));

        fieldsPanel.add(newPassLabel);
        fieldsPanel.add(newPassField);
        fieldsPanel.add(confirmNewPassLabel);
        fieldsPanel.add(confirmNewPassField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));

        buttonPanel.add(confirmButton);

        this.add(fieldsPanel, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                char[] dummy = new char[0];
                if (Arrays.equals(newPassField.getPassword(), confirmNewPassField.getPassword()) && !Arrays.equals(newPassField.getPassword(), dummy)) {
                    System.out.println(newPassField.getPassword());
                    String newPassword = "";
                    for (int i = 0; i < newPassField.getPassword().length; ++i) {
                        newPassword += newPassField.getPassword()[i];
                    }
                    usuario.changePassword(newPassword);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Las contraseñas no coinciden o se ha dejado en blanco algún campo");
                }
            }
        });

        this.setVisible(true);
    }
}
