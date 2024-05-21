package gui.user_frames.admin.teams;

import com.jcraft.jsch.JSchException;
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

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class NewTeamFrame extends NewRegistrationFrameTemplate {
    // Botones
    CustomButton createExerButton;

    // Campos de texto
    CustomFieldLabel teamCode;
    CustomFieldLabel teamName;
    CustomFieldLabel teamSchool;
    CustomFieldLabel teamItinerario;
    CustomTextField teamCodeField;
    CustomTextField teamNameField;
    CustomTextField teamSchoolField;

    // Combo box
    CustomComboBox teamItinerarioField;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    // Otros
    Admin admin;

    public NewTeamFrame(Admin administrador) throws JSchException, SQLException {
        super(325, "Nuevo equipo");

        this.admin= administrador;

        this.add(createCenterPanel(), BorderLayout.CENTER);
        this.add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        createExerButton.addActionListener(e -> {
            if (teamCodeField.getText().matches("^\\s*$")
                    || teamNameField.getText().matches("^\\s*$")
                    || teamSchoolField.getText().matches("^\\s*$")
                    || Objects.requireNonNull(teamItinerarioField.getSelectedItem()).toString().matches("^\\s*$")) {
                new ErrorJOptionPane("Todos los campos son obligatorios");

            } else {
                String code = teamCodeField.getText();
                String name = teamNameField.getText();
                String school = teamSchoolField.getText();
                String itinerary = (String) teamItinerarioField.getSelectedItem();

                String table = "T_EQUIPOS";
                String data = "'" + code + "', '" + name + "', '" + school + "', '" + itinerary + "'";
                String selection = "(CODIGO, NOMBRE, CENTRO_EDUCATIVO, ITINERARIO)";

                if (administrador.createRegister(table, data, selection) == 0) {
                    teamCodeField.setText("");
                    teamNameField.setText("");
                    teamSchoolField.setText("");
                    teamItinerarioField.setSelectedItem(teamItinerarioField.getItemAt(0));

                }

            }
        });

    }

    @Override
    protected JPanel createCenterPanel() {
        try {
            teamCode = new CustomFieldLabel("Código (*)");
            teamName = new CustomFieldLabel("Nombre (*)");
            teamSchool = new CustomFieldLabel("Centro educativo (*)");
            teamItinerario = new CustomFieldLabel("Itinerario (*)");
            teamCodeField = new CustomTextField("");
            teamNameField = new CustomTextField("");
            teamSchoolField = new CustomTextField("");
            teamItinerarioField = new CustomComboBox();

            ResultSet itCodes = admin.selectCol("T_ITINERARIOS", "CODIGO");

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (itCodes.next()) {
                String registro = itCodes.getString("CODIGO");
                teamItinerarioField.addItem(registro);
            }

            itCodes.close();

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

            inputPanel.add(teamCode);
            inputPanel.add(teamCodeField);
            inputPanel.add(teamName);
            inputPanel.add(teamNameField);
            inputPanel.add(teamSchool);
            inputPanel.add(teamSchoolField);
            inputPanel.add(teamItinerario);
            inputPanel.add(teamItinerarioField);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }
        
        return inputPanel;
    }

    @Override
    protected JPanel createSouthPanel() {
        createExerButton = new CustomButton("Crear equipo", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(createExerButton);

        return createButtonPanel;
    }
}
