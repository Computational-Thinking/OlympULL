package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.ModifyRegistrationFrameTemplate;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class ModifyTeamFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
    CustomFieldLabel teamCode;
    CustomFieldLabel teamName;
    CustomFieldLabel teamSchool;
    CustomFieldLabel teamItinerario;
    CustomTextField teamCodeField;
    CustomTextField teamNameField;
    CustomTextField teamSchoolField;
    CustomComboBox teamItinerarioField;
    CustomButton modifyTeamButton;
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    // Otros
    Admin admin;
    String oldCode;
    String oldName;
    String oldSchool;
    String oldIt;

    public ModifyTeamFrame(Admin administrador, String code, String name, String school, String itinerario) throws JSchException, SQLException {
        super(335, "Modificar equipo");

        admin = administrador;
        oldCode = code;
        oldName = name;
        oldSchool = school;
        oldIt = itinerario;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        modifyTeamButton.addActionListener(e -> {
            if (teamCodeField.getText().matches("^\\s*$")
                    || teamNameField.getText().matches("^\\s*$")
                    || teamSchoolField.getText().matches("^\\s*$")
                    || Objects.requireNonNull(teamItinerarioField.getSelectedItem()).toString().matches("^\\s*$")) {
                new ErrorJOptionPane("Todos los campos son obligatorios");

            } else {
                String code1 = teamCodeField.getText();
                String name1 = teamNameField.getText();
                String school1 = teamSchoolField.getText();
                String itinerary = (String) teamItinerarioField.getSelectedItem();

                try {
                    if (administrador.modifyTeam(oldCode, code1, name1, school1, itinerary) == 0) {
                        new CheckTeamsFrame(administrador);
                        dispose();
                    }

                } catch (JSchException | SQLException exc) {
                    new ErrorJOptionPane(exc.getMessage());

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
            teamCodeField = new CustomTextField(oldCode);
            teamNameField = new CustomTextField(oldName);
            teamSchoolField = new CustomTextField(oldSchool);
            teamItinerarioField = new CustomComboBox();

            ResultSet itCodes = admin.selectCol("T_ITINERARIOS", "CODIGO");

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (itCodes.next()) {
                String registro = itCodes.getString("CODIGO");
                teamItinerarioField.addItem(registro);
            }

            itCodes.close();

            teamItinerarioField.setSelectedItem(oldIt);

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
        modifyTeamButton = new CustomButton("Modificar equipo", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(modifyTeamButton);

        return createButtonPanel;
    }
}
