package gui.user_frames.admin.olympiads;

import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.custom_components.text_fields.CustomTextField;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;

public class NewOlympiadFrame extends NewRegistrationFrameTemplate {
    // Botones
    CustomButton createOlympButton;
    
    CustomFieldLabel olympCode;
    CustomFieldLabel olympName;
    CustomFieldLabel olympDesc;
    CustomFieldLabel olympYear;
    
    // Campos de texto
    CustomTextField olympCodeField;
    CustomTextField olympNameField;
    CustomTextField olympDescField;
    CustomTextField olympYearField;
    
    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;
    
    // Otros
    Admin admin;

    public NewOlympiadFrame(Admin administrador) {
        super(325, "Nueva olimpiada");
        
        this.admin = administrador;
        
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        // Botón de crear olimpiada
        createOlympButton.addActionListener(e -> {
            if (olympCodeField.getText().matches("^\\s*$")
                    || olympNameField.getText().matches("^\\s*$")
                    || olympYearField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Título y Año son obligatorios");

            } else {
                String code = olympCodeField.getText();
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                String year = olympYearField.getText();

                if (year.matches("[0-9]*") && Integer.parseInt(year) > 2000 && Integer.parseInt(year) < 3000) {
                    if (administrador.createOlympiad(code, name, desc, Integer.parseInt(year)) == 0) {
                        olympCodeField.setText("");
                        olympNameField.setText("");
                        olympDescField.setText("");
                        olympYearField.setText("");
                    }

                } else {
                    new ErrorJOptionPane("El campo Año debe ser un número entero y tener un valor válido");
                }
            }
        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        olympCode = new CustomFieldLabel("Código (*)");
        olympName = new CustomFieldLabel("Nombre (*)");
        olympDesc = new CustomFieldLabel("Descripción");
        olympYear = new CustomFieldLabel("Año (*)");
        olympCodeField = new CustomTextField("");
        olympNameField = new CustomTextField("");
        olympDescField = new CustomTextField("");
        olympYearField = new CustomTextField("");

        inputPanel = new CustomPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(olympName);
        inputPanel.add(olympNameField);
        inputPanel.add(olympDesc);
        inputPanel.add(olympDescField);
        inputPanel.add(olympYear);
        inputPanel.add(olympYearField);
        
        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        createOlympButton = new CustomButton("Crear olimpiada", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(createOlympButton);

        return createButtonPanel;
    }
}
