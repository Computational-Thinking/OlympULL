package interfaz.monitor;

import interfaz.custom_components.*;
import interfaz.template_pattern.NewRegistrationFrameTemplate;
import users.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class PunctuateExerciseFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
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
    JButton punctuateButton;

    int puntuacion;
    String itinerario;
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
                puntuacion = Integer.parseInt(Objects.requireNonNull(punctuationComboBox.getSelectedItem()).toString().substring(0, 2).trim());

                ResultSet data = monitor.selectCol("T_EJERCICIOS", "CONCEPTO", "WHERE CODIGO='" + exerciseSelectionComboBox.getSelectedItem() + "'");

                if (data.next()) {
                    concepto = data.getString("CONCEPTO");
                }

                // Se puntúa al equipo
                monitor.puntuarEquipo(puntuacion, concepto, (String) teamSelectionComboBox.getSelectedItem(), itinerario);

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        exerciseSelectionComboBox.addActionListener(e -> {
            try {
                itinerario = "";

                String whereClause = "WHERE EJERCICIO='" + exerciseSelectionComboBox.getSelectedItem() + "'";
                ResultSet data = monitor.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", whereClause);

                if (data.next()) {
                    itinerario = data.getString("ITINERARIO");
                }

                data = monitor.selectCol("T_EQUIPOS", "NOMBRE", "WHERE ITINERARIO='" + itinerario + "'");

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
        punctuateButton = new JButton("Puntuar");
        
        punctuateButtonPanel = new CustomPanel();
        punctuateButtonPanel.setLayout(new FlowLayout());
        
        punctuateButtonPanel.add(punctuateButton);
        
        return punctuateButtonPanel;
    }
}
