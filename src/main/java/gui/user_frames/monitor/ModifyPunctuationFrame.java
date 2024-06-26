package gui.user_frames.monitor;

import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.text_fields.CustomPresetTextField;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import operations.TransformPunctuationColumnName;
import users.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class ModifyPunctuationFrame extends ModifyRegistrationFrameTemplate implements TransformPunctuationColumnName {
    // Labels
    CustomFieldLabel exerciseSelectionLabel;
    CustomFieldLabel teamSelectionLabel;
    CustomFieldLabel punctuationSelectionLabel;
    CustomPresetTextField exerciseSelectionField;
    CustomPresetTextField teamSelectionField;

    // ComboBoxes
    JComboBox<String> punctComboBox;

    // Paneles
    CustomPanel inputsPanel;
    CustomPanel punctuateButtonPanel;

    // Botones
    CustomButton punctuateButton;

    int punct;
    String itinerary;
    Monitor user;
    String oldEx;
    String oldTeam;

    public ModifyPunctuationFrame(Monitor monitor, String oldEx, String oldTeam) throws SQLException {
        super(275, "Modificar puntuación de equipo");

        user = monitor;
        this.oldEx = oldEx;
        this.oldTeam = oldTeam;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
        
        getGoBackButton().addActionListener(e -> {
            try {
                new CheckPunctuationsFrame(monitor, oldEx);
                dispose();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });
        
        punctuateButton.addActionListener(e -> {
            try {
                itinerary = "";

                String olympiad = null;

                ResultSet monitorOlympiad = monitor.selectCol("T_MONITORES", "OLIMPIADA", "WHERE NOMBRE='" + monitor.getUserName() + "'");

                if (monitorOlympiad.next()) {
                    olympiad = monitorOlympiad.getString("OLIMPIADA");
                }

                String where = "WHERE EJERCICIO='" + exerciseSelectionField.getText() + "' AND OLIMPIADA='" + olympiad + "'";
                ResultSet newData = monitor.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", where);

                if (newData.next()) {
                    itinerary = newData.getString("ITINERARIO");
                }

                String concepto = "";
                punct = Integer.parseInt(Objects.requireNonNull(punctComboBox.getSelectedItem()).toString().substring(0, 2).trim());

                where = "WHERE CODIGO='" + exerciseSelectionField.getText() + "'";
                newData = monitor.selectCol("T_EJERCICIOS", "CONCEPTO", where);

                if (newData.next()) {
                    concepto = newData.getString("CONCEPTO");
                }

                String punctuationColumn = transformColumnName(concepto);

                String table = "T_EQUIPOS";
                String data = "SET " + punctuationColumn + "=" + punct;
                where = "WHERE NOMBRE='" + teamSelectionField.getText() + "' AND ITINERARIO='" + itinerary + "'";

                // Se puntúa al equipo
                monitor.modifyRegister(table, data, where);

                new CheckPunctuationsFrame(monitor, oldEx);
                dispose();

                newData.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            exerciseSelectionLabel = new CustomFieldLabel("Ejercicio (*)");
            teamSelectionLabel = new CustomFieldLabel("Equipo (*)");
            punctuationSelectionLabel = new CustomFieldLabel("Puntuación (*)");
            exerciseSelectionField = new CustomPresetTextField(oldEx);
            teamSelectionField = new CustomPresetTextField(oldTeam);
            punctComboBox = new CustomComboBox();

            String rubrica = null;
            String where = "WHERE CODIGO='" + exerciseSelectionField.getText() + "'";
            ResultSet data = user.selectCol("T_EJERCICIOS", "RUBRICA", where);

            if (data.next()) rubrica = data.getString("RUBRICA");

            where = "WHERE CODIGO='" + rubrica + "'";
            data = user.selectCol("T_RUBRICAS", "PUNTOS_RUBRICA, ETIQUETAS_RUBRICA", where);

            String values = "";
            String tags = "";

            if (data.next()) {
                values = data.getString("PUNTOS_RUBRICA");
                tags = data.getString("ETIQUETAS_RUBRICA");

            }

            // Se separa por comas
            String[] separatedValues = values.split(",");
            String[] separatedTags = tags.split(",");

            separatedValues[0] = separatedValues[0].substring(1);
            separatedTags[0] = separatedTags[0].substring(1);

            separatedValues[separatedValues.length - 1] = separatedValues[separatedValues.length - 1].substring(1, 
                    (separatedValues[separatedValues.length - 1]).length() - 1);
            separatedTags[separatedTags.length - 1] = separatedTags[separatedTags.length - 1].substring(1, 
                    (separatedTags[separatedTags.length - 1]).length() - 1);

            // Array donde se va a almacenar la escala
            ArrayList<String> scale = new ArrayList<>();

            for (int i = 0; i < separatedValues.length; ++i) {
                scale.add(separatedValues[i] + " - " + separatedTags[i]);
            }

            for (String s : scale) {
                punctComboBox.addItem(s);
            }

            inputsPanel = new CustomPanel();
            inputsPanel.setLayout(new GridLayout(3, 2, 10, 10));

            inputsPanel.add(exerciseSelectionLabel);
            inputsPanel.add(exerciseSelectionField);
            inputsPanel.add(teamSelectionLabel);
            inputsPanel.add(teamSelectionField);
            inputsPanel.add(punctuationSelectionLabel);
            inputsPanel.add(punctComboBox);

            data.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputsPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        punctuateButton = new CustomButton("Modificar");

        punctuateButtonPanel = new CustomPanel();
        punctuateButtonPanel.setLayout(new FlowLayout());

        punctuateButtonPanel.add(punctuateButton);
        
        return punctuateButtonPanel;
    }
}
