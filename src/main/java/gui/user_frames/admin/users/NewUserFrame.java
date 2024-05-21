package gui.user_frames.admin.users;

import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.custom_components.text_fields.CustomPasswordField;
import gui.custom_components.text_fields.CustomTextField;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NewUserFrame extends NewRegistrationFrameTemplate {
    // Etiquetas
    CustomFieldLabel userNameLabel;
    CustomFieldLabel userPasswordLabel;
    CustomFieldLabel userTypeLabel;

    // Campos de texto
    CustomTextField userNameField;
    CustomPasswordField userPasswordField;

    // Combo boxes
    JComboBox<String> userTypeComboBox;

    // Botones
    CustomButton createUserButton;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    Admin user;

    public NewUserFrame(Admin administrador) {
        super(280, "Nuevo usuario");

        user = administrador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        createUserButton.addActionListener(e -> {
            if (userNameField.getText().matches("^\\s*$")
                    || userPasswordField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Nombre y Password son obligatorios");

            } else {
                String name = userNameField.getText();
                String password = userPasswordField.getText();
                String type = String.valueOf(userTypeComboBox.getSelectedItem());

                String table = "T_USUARIOS";
                String data = "'" + name + "', '" + password + "', '" + type + "'";

                if (administrador.createRegister(table, data) == 0) {
                    userNameField.setText("");
                    userPasswordField.setText("");
                    userTypeComboBox.setSelectedItem(userTypeComboBox.getItemAt(0));
                }

            }
        });
    }

    @Override
    protected JPanel createCenterPanel() {
        userNameLabel = new CustomFieldLabel("Nombre de usuario (*)");
        userPasswordLabel = new CustomFieldLabel("Contraseña (*)");
        userTypeLabel = new CustomFieldLabel("Tipo de usuario (*)");
        userNameField = new CustomTextField("");
        userPasswordField = new CustomPasswordField("");

        ArrayList<String> userTypes = new ArrayList<>(Arrays.asList("ADMINISTRADOR", "ORGANIZADOR", "MONITOR"));
        userTypeComboBox = new CustomComboBox(userTypes);

        // Configuración del panel de input del usuario
        inputPanel = new CustomPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputPanel.add(userNameLabel);
        inputPanel.add(userNameField);
        inputPanel.add(userPasswordLabel);
        inputPanel.add(userPasswordField);
        inputPanel.add(userTypeLabel);
        inputPanel.add(userTypeComboBox);

        return inputPanel;
    }

    @Override
    protected JPanel createSouthPanel() {
        createUserButton = new CustomButton("Crear usuario", 175, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.setLayout(new FlowLayout());

        createButtonPanel.add(createUserButton);

        return createButtonPanel;
    }
}
