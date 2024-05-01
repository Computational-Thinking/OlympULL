package interfaz.monitor;

import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import interfaz.administrador.VentanaConsultaEquipos;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaModificarPuntuacion extends JFrame implements Bordes, Fuentes, Iconos {
    // Labels
    JLabel exerciseSelectionLabel;
    JLabel teamSelectionLabel;
    JLabel punctuationSelectionLabel;
    JLabel title;
    JLabel exerciseSelectionField;
    JLabel teamSelectionField;

    // ComboBoxes
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

    public VentanaModificarPuntuacion(Monitor monitor, String exercise, String team) throws SQLException {
        // Configuración de ventana
        setSize(725, 275);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Modificar puntuación");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);
        setVisible(true);

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        title = new JLabel("Modificar puntuación de equipo");
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

        exerciseSelectionField = new JLabel(exercise);
        exerciseSelectionField.setFont(fuenteCampoTexto);

        punctuationsPanel = new JPanel();

        punctuateButton = new JButton("Modificar");
        punctuateButton.setFont(fuenteBotonesEtiquetas);

        exerciseSelectionLabel.setFont(fuenteBotonesEtiquetas);

        teamSelectionField = new JLabel(team);
        teamSelectionField.setFont(fuenteCampoTexto);

        punctuationComboBox = new JComboBox<>();
        punctuationComboBox.setFont(fuenteCampoTexto);

        String rubrica = null;
        ResultSet data = monitor.selectCol("T_EJERCICIOS", "RUBRICA", "WHERE CODIGO='" + exerciseSelectionField.getText() + "'");

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

        inputsPanel = new JPanel();
        inputsPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputsPanel.setBorder(borde);

        punctuateButtonPanel = new JPanel();
        punctuateButtonPanel.setBorder(borde);
        punctuateButtonPanel.setLayout(new FlowLayout());

        upperPanel.add(title, BorderLayout.WEST);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        inputsPanel.add(exerciseSelectionLabel);
        inputsPanel.add(exerciseSelectionField);
        inputsPanel.add(teamSelectionLabel);
        inputsPanel.add(teamSelectionField);
        inputsPanel.add(punctuationSelectionLabel);
        inputsPanel.add(punctuationComboBox);

        punctuateButtonPanel.add(punctuateButton);

        add(upperPanel, BorderLayout.NORTH);
        add(inputsPanel, BorderLayout.CENTER);
        add(punctuateButtonPanel, BorderLayout.SOUTH);

        // Acción del botón de volver
        goBackButton.addActionListener(e -> {
            try {
                new VentanaConsultaPuntuaciones(monitor, exercise);
                dispose();

            } catch (SQLException ex) {
                new CustomJOptionPane("ERROR - " + ex.getMessage());
            }
        });

        punctuateButton.addActionListener(e -> {
            try {
                itinerario = "";

                String whereClause = "WHERE EJERCICIO='" + exerciseSelectionField.getText() + "'";
                ResultSet newData = monitor.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", whereClause);

                if (newData.next()) {
                    itinerario = newData.getString("ITINERARIO");
                }

                String concepto = "";
                puntuacion = Integer.parseInt(Objects.requireNonNull(punctuationComboBox.getSelectedItem()).toString().substring(0, 2).trim());

                newData = monitor.selectCol("T_EJERCICIOS", "CONCEPTO", "WHERE CODIGO='" + exerciseSelectionField.getText() + "'");

                if (newData.next()) {
                    concepto = newData.getString("CONCEPTO");
                }

                System.out.println(puntuacion + " " + concepto + " " + teamSelectionField.getText() + " " + itinerario);

                // Se puntúa al equipo
                monitor.modificarPuntuacion(puntuacion, concepto, teamSelectionField.getText(), itinerario);

                new VentanaConsultaPuntuaciones(monitor, exercise);
                dispose();

            } catch (SQLException ex) {
                new CustomJOptionPane("ERROR - " + ex.getMessage());
            }
        });
    }
}
