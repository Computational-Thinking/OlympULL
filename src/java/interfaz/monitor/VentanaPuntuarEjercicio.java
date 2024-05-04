package interfaz.monitor;

import interfaz.*;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaPuntuarEjercicio extends JFrame implements Bordes, Fuentes, Iconos {
    // Labels
    JLabel exerciseSelectionLabel;
    JLabel teamSelectionLabel;
    JLabel punctuationSelectionLabel;
    JLabel title;

    // ComboBoxes
    JComboBox<String> exerciseSelectionComboBox;
    JComboBox<String> teamSelectionComboBox;
    JComboBox<String> punctuationComboBox;

    // Paneles
    JPanel upperPanel;
    JPanel punctuationsPanel;
    JPanel inputsPanel;
    JPanel punctuateButtonPanel;

    // Botones
    JButton punctuateButton;
    JButton goBackButton;

    int puntuacion;
    String itinerario;

    public VentanaPuntuarEjercicio(Monitor monitor) throws SQLException {
        // Configuración de ventana
        setSize(725, 275);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Puntuación de equipo");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);
        setVisible(true);

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        title = new JLabel("Puntuación de equipos");
        title.setFont(fuenteTitulo);

        upperPanel = new JPanel();
        upperPanel.setBorder(borde);
        upperPanel.setLayout(new BorderLayout());

        exerciseSelectionLabel = new JLabel("Ejercicio (*)");
        exerciseSelectionLabel.setFont(fuenteBotonesEtiquetas);

        teamSelectionLabel = new JLabel("Equipo (*)");
        teamSelectionLabel.setFont(fuenteBotonesEtiquetas);

        punctuationSelectionLabel = new JLabel("Puntuación (*)");
        punctuationSelectionLabel.setFont(fuenteBotonesEtiquetas);

        exerciseSelectionComboBox = new JComboBox<>();
        exerciseSelectionComboBox.setFont(fuenteCampoTexto);

        for (int i = 0; i < monitor.getExerciseCode().size(); ++i) {
            exerciseSelectionComboBox.addItem(monitor.getExerciseCode().get(i));
        }

        punctuationsPanel = new JPanel();

        punctuateButton = new JButton("Puntuar");
        punctuateButton.setFont(fuenteBotonesEtiquetas);

        exerciseSelectionLabel.setFont(fuenteBotonesEtiquetas);

        teamSelectionComboBox = new JComboBox<>();
        teamSelectionComboBox.setFont(fuenteCampoTexto);

        punctuationComboBox = new JComboBox<>();
        punctuationComboBox.setFont(fuenteCampoTexto);

        inputsPanel = new JPanel();
        inputsPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputsPanel.setBorder(borde);

        punctuateButtonPanel = new JPanel();
        punctuateButtonPanel.setBorder(borde);
        punctuateButtonPanel.setLayout(new FlowLayout());

        upperPanel.add(title, BorderLayout.WEST);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        inputsPanel.add(exerciseSelectionLabel);
        inputsPanel.add(exerciseSelectionComboBox);
        inputsPanel.add(teamSelectionLabel);
        inputsPanel.add(teamSelectionComboBox);
        inputsPanel.add(punctuationSelectionLabel);
        inputsPanel.add(punctuationComboBox);

        punctuateButtonPanel.add(punctuateButton);

        add(upperPanel, BorderLayout.NORTH);
        add(inputsPanel, BorderLayout.CENTER);
        add(punctuateButtonPanel, BorderLayout.SOUTH);

        exerciseSelectionComboBox.addActionListener(e -> {

        });

        // Acción del botón de volver
        goBackButton.addActionListener(e -> {
            new VentanaMonitor(monitor);
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
}
