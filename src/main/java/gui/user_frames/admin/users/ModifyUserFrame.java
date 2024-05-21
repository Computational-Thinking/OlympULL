package gui.user_frames.admin.users;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.custom_components.text_fields.CustomPasswordField;
import gui.custom_components.text_fields.CustomTextField;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifyUserFrame extends ModifyRegistrationFrameTemplate {
    // Etiquetas
    CustomFieldLabel userNameLabel;
    CustomFieldLabel userPasswordLabel;
    CustomFieldLabel userTypeLabel;

    // Campos de texto
    CustomTextField userNameField;
    CustomPasswordField userPasswordField;

    // Combo boxes
    CustomComboBox userTypeComboBox;

    // Botones
    CustomButton modifyUserButton;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;
    
    // Otros
    Admin admin;
    String oldName;
    String oldPassword;
    String oldType;

    public ModifyUserFrame(Admin administrador, String nombre, String password, String tipo) {
        super(270, "Modificar usuario");

        admin = administrador;
        oldName = nombre;
        oldPassword = password;
        oldType = tipo;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckUsersFrame(administrador);
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        modifyUserButton.addActionListener(e -> {
            if (userNameField.getText().matches("^\\s*$")
                    || userPasswordField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Nombre y Password son obligatorios");

            } else {
                String name = userNameField.getText();
                String password1 = userPasswordField.getText();
                String type = String.valueOf(userTypeComboBox.getSelectedItem());

                String table = "T_USUARIOS";
                String setClause = "SET NOMBRE='" + name + "', PASSWORD='" + password + "', TIPO='" + type + "'";
                String whereClause = "WHERE NOMBRE='" + oldName + "';";

                try {
                    if (administrador.modifyRegister(table, setClause, whereClause) == 0) {
                        new CheckUsersFrame(administrador);
                        dispose();

                    }

                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);

                }
            }

        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        userNameLabel = new CustomFieldLabel("Nombre de usuario (*)");
        userPasswordLabel = new CustomFieldLabel("Contraseña (*)");
        userTypeLabel = new CustomFieldLabel("Tipo de usuario (*)");
        userNameField = new CustomTextField(oldName);
        userPasswordField = new CustomPasswordField(oldPassword);

        ArrayList<String> userTypes = new ArrayList<>(Arrays.asList("ADMINISTRADOR", "ORGANIZADOR", "MONITOR"));
        userTypeComboBox = new CustomComboBox(userTypes);
        userTypeComboBox.setSelectedItem(oldType);

        inputPanel = new CustomPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.add(userNameLabel);
        inputPanel.add(userNameField);
        inputPanel.add(userPasswordLabel);
        inputPanel.add(userPasswordField);
        inputPanel.add(userTypeLabel);
        inputPanel.add(userTypeComboBox);

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        modifyUserButton = new CustomButton("Modificar usuario", 175, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.setLayout(new FlowLayout());
        createButtonPanel.add(modifyUserButton);

        return createButtonPanel;
    }
}
