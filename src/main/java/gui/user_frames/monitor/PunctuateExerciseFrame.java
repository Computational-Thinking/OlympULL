package gui.user_frames.monitor;

import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.template_pattern.NewRegistrationFrameTemplate;
import operations.TransformPunctuationColumnName;
import users.Monitor;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class PunctuateExerciseFrame extends NewRegistrationFrameTemplate implements TransformPunctuationColumnName {
    // Labels
    CustomFieldLabel exerciseSelectionLabel;
    CustomFieldLabel teamSelectionLabel;
    CustomFieldLabel punctuationSelectionLabel;

    // ComboBoxes
    CustomComboBox exerciseSelectionComboBox;
    CustomComboBox teamSelectionComboBox;
    CustomComboBox punctuationComboBox;

    // Paneles
    CustomPanel inputsPanel;
    CustomPanel punctuateButtonPanel;

    // Botones
    CustomButton punctuateButton;

    int punctuation;
    String itinerary;
    Monitor user;

    public PunctuateExerciseFrame(Monitor monitor) throws SQLException {
        super(275, "Puntuación de equipos");
        
        user = monitor;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);
        
        // Acción del botón de volver
        getGoBackButton().addActionListener(e -> {
            new MonitorFrame(monitor);
            dispose();
        });

        punctuateButton.addActionListener(e -> {
            try {
                String concepto = "";
                punctuation = Integer.parseInt(Objects.requireNonNull(punctuationComboBox.getSelectedItem()).toString().substring(0, 2).trim());

                ResultSet data = monitor.selectCol("T_EJERCICIOS", "CONCEPTO", "WHERE CODIGO='" + exerciseSelectionComboBox.getSelectedItem() + "'");

                if (data.next()) {
                    concepto = data.getString("CONCEPTO");
                }

                String punctuationColumn = transformColumnName(concepto);
                
                // Se comprueba que no se ha puntuado ya al equipo
                String wherePrueba = "WHERE NOMBRE='" + teamSelectionComboBox.getSelectedItem() + "' AND ITINERARIO='" + itinerary + "'";
                ResultSet prueba = monitor.selectCol("T_EQUIPOS", punctuationColumn, wherePrueba);

                if (prueba.next()) {
                    if (prueba.getString(punctuationColumn) != null) {
                        new ErrorJOptionPane("Este equipo ya ha recibido una puntuación para el ejercicio seleccionado");
                    } else {
                        String table = "T_EQUIPOS";
                        String tuple = "SET " + punctuationColumn + "=" + punctuation;
                        String where = "WHERE NOMBRE='" + teamSelectionComboBox.getSelectedItem() + "' AND ITINERARIO='" + itinerary + "'";

                        // Se puntúa al equipo
                        monitor.modifyRegister(table, tuple, where, "Se ha puntuado al equipo");
                    }
                }

                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        exerciseSelectionComboBox.addActionListener(e -> {
            teamSelectionComboBox.removeAllItems();
            punctuationComboBox.removeAllItems();

            try {
                String olympiad = "";

                ResultSet data = monitor.selectCol("T_MONITORES", "OLIMPIADA", "WHERE NOMBRE='" + monitor.getUserName() + "'");

                if (data.next()) {
                    olympiad = data.getString("OLIMPIADA");
                }

                itinerary = "";

                String whereClause = "WHERE EJERCICIO='" + exerciseSelectionComboBox.getSelectedItem() + "' AND OLIMPIADA='" + olympiad + "'";
                data = monitor.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", whereClause);

                if (data.next()) {
                    itinerary = data.getString("ITINERARIO");
                }

                data = monitor.selectCol("T_EQUIPOS", "NOMBRE", "WHERE ITINERARIO='" + itinerary + "'");

                while (data.next()) {
                    teamSelectionComboBox.addItem(data.getString("NOMBRE"));
                }

                String rubrica = null;
                data = monitor.selectCol("T_EJERCICIOS", "RUBRICA", "WHERE CODIGO='" + exerciseSelectionComboBox.getSelectedItem() + "'");

                if (data.next()) rubrica = data.getString("RUBRICA");

                data = monitor.selectCol("T_RUBRICAS", "PUNTOS_RUBRICA, ETIQUETAS_RUBRICA", "WHERE CODIGO='" + rubrica + "'");

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

                separatedValues[separatedValues.length - 1] = separatedValues[separatedValues.length - 1].substring(1, (separatedValues[separatedValues.length - 1]).length() - 1);
                separatedTags[separatedTags.length - 1] = separatedTags[separatedTags.length - 1].substring(1, (separatedTags[separatedTags.length - 1]).length() - 1);

                // Array donde se va a almacenar la escala
                ArrayList<String> scale = new ArrayList<>();

                for (int i = 0; i < separatedValues.length; ++i) {
                    scale.add(separatedValues[i] + " - " + separatedTags[i]);
                }

                for (String s : scale) {
                    punctuationComboBox.addItem(s);
                }

                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }

        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        exerciseSelectionLabel = new CustomFieldLabel("Ejercicio (*)");
        teamSelectionLabel = new CustomFieldLabel("Equipo (*)");
        punctuationSelectionLabel = new CustomFieldLabel("Puntuación (*)");
        exerciseSelectionComboBox = new CustomComboBox();
        teamSelectionComboBox = new CustomComboBox();
        punctuationComboBox = new CustomComboBox();

        for (int i = 0; i < user.getExerciseCode().size(); ++i) {
            exerciseSelectionComboBox.addItem(user.getExerciseCode().get(i));
        }

        inputsPanel = new CustomPanel();
        inputsPanel.setLayout(new GridLayout(3, 2, 10, 10));
        inputsPanel.add(exerciseSelectionLabel);
        inputsPanel.add(exerciseSelectionComboBox);
        inputsPanel.add(teamSelectionLabel);
        inputsPanel.add(teamSelectionComboBox);
        inputsPanel.add(punctuationSelectionLabel);
        inputsPanel.add(punctuationComboBox);
        
        return inputsPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        punctuateButton = new CustomButton("Puntuar");
        
        punctuateButtonPanel = new CustomPanel();
        punctuateButtonPanel.setLayout(new FlowLayout());
        
        punctuateButtonPanel.add(punctuateButton);
        
        return punctuateButtonPanel;
    }
}
