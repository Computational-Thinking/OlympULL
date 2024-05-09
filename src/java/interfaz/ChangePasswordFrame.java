package interfaz;

import interfaz.admin.AdminFrame;
import interfaz.custom_components.*;
import interfaz.monitor.MonitorFrame;
import interfaz.organizer.OrganizerFrame;
import users.Admin;
import users.Monitor;
import users.Organizer;
import users.User;

import java.awt.*;
import java.util.Objects;

public class ChangePasswordFrame extends CustomFrame implements Borders, Fonts, Icons {
    // Etiquetas
    CustomFieldLabel usuarioLabel;
    CustomFieldLabel newPassLabel;
    CustomFieldLabel confirmNewPassLabel;
    CustomPresetTextField usuarioField;

    // Password field
    CustomPasswordField newPassField;
    CustomPasswordField confirmNewPassField;

    // Botones
    CustomButton confirmButton;
    CustomButton goBackButton;

    // Paneles
    CustomPanel fieldsPanel;
    CustomPanel buttonPanel;

    public ChangePasswordFrame(User usuario) {
        super();
        this.setSize(475, 265);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("Cambio de contraseña");

        // Panel superior
        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar("Cambiar contraseña", goBackButton), BorderLayout.NORTH);

        // Panel de inputs
        usuarioLabel = new CustomFieldLabel("Usuario");
        usuarioField = new CustomPresetTextField(usuario.getUserName());
        newPassLabel = new CustomFieldLabel("Nueva contraseña");
        newPassField = new CustomPasswordField("");
        confirmNewPassLabel = new CustomFieldLabel("Confirmar nueva contraseña");
        confirmNewPassField = new CustomPasswordField("");

        fieldsPanel = new CustomPanel();
        fieldsPanel.setLayout(new GridLayout(3, 2, 10, 10));

        fieldsPanel.add(usuarioLabel);
        fieldsPanel.add(usuarioField);
        fieldsPanel.add(newPassLabel);
        fieldsPanel.add(newPassField);
        fieldsPanel.add(confirmNewPassLabel);
        fieldsPanel.add(confirmNewPassField);

        // Panel de botón
        confirmButton = new CustomButton("Cambiar contraseña");

        buttonPanel = new CustomPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttonPanel.add(confirmButton);

        add(fieldsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            if (Objects.equals(usuario.getUserType(), "administrador")) {
                new AdminFrame((Admin) usuario);
                dispose();

            } else if (Objects.equals(usuario.getUserType(), "organizador")) {
                new OrganizerFrame((Organizer) usuario);
                dispose();

            } else if (Objects.equals(usuario.getUserType(), "monitor")) {
               new MonitorFrame((Monitor) usuario);
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
                    new AdminFrame((Admin) usuario);
                    dispose();

                } else if (Objects.equals(usuario.getUserType(), "organizador")) {
                    new OrganizerFrame((Organizer) usuario);
                    dispose();

                } else if (Objects.equals(usuario.getUserType(), "monitor")) {
                    new MonitorFrame((Monitor) usuario);
                    dispose();

                } else {
                    new ErrorJOptionPane("Ha ocurrido un error inesperado. Abortando...");
                    dispose();

                }

            } else {
                new ErrorJOptionPane("Las contraseñas no coinciden");

            }

        });

        setVisible(true);
    }
}
