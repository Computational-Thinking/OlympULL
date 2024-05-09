package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.SQLException;

public class ModifyOlympiadFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton modifyOlympButton;
    
    // Etiquetas
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
    CustomPanel modifyButtonPanel;
    
    // Otros
    Admin admin;
    String oldCode, oldTitle, oldDesc, oldYear;
    
    public ModifyOlympiadFrame(Admin administrador, String codigo, String titulo, String descripcion, String year) {
        super(325, "Modificar olimpiada");
        
        admin = administrador;
        oldCode = codigo;
        oldTitle = titulo;
        oldDesc = descripcion;
        oldYear = year;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckOlympiadsFrame(admin);
                dispose();

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }
        });

        // Botón de modificar
        modifyOlympButton.addActionListener(e -> {
            if (olympCodeField.getText().matches("^\\s*$")
                    || olympNameField.getText().matches("^\\s*$")
                    || olympYearField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Título y Año son obligatorios");

            } else {
                String code = olympCodeField.getText();
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                String yr = olympYearField.getText();

                if (yr.matches("[0-9]*") && Integer.parseInt(yr) > 2000 && Integer.parseInt(yr) < 3000) {
                    try {
                        if (administrador.modifyOlympiad(oldCode, code, name, desc, Integer.parseInt(yr)) == 0) {
                            new CheckOlympiadsFrame(administrador);
                            dispose();

                        }

                    } catch (JSchException | SQLException ex) {
                        new ErrorJOptionPane(ex.getMessage());

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
        olympCodeField = new CustomTextField(oldCode);
        olympNameField = new CustomTextField(oldTitle);
        olympDescField = new CustomTextField(oldDesc);
        olympYearField = new CustomTextField(oldYear);

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
        modifyOlympButton = new CustomButton("Modificar olimpiada", 175, 30);

        modifyButtonPanel = new CustomPanel();
        modifyButtonPanel.add(modifyOlympButton);

        return modifyButtonPanel;
    }
}
