package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class ModifyTeamFrame extends JFrame implements Borders, Fonts, Icons {
    JButton goBackButton;
    JLabel introduceData;
    JLabel teamCode;
    JLabel teamName;
    JLabel teamSchool;
    JLabel teamItinerario;
    JTextField teamCodeField;
    JTextField teamNameField;
    JTextField teamSchoolField;
    JComboBox<String> teamItinerarioField;
    JButton modifyTeamButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public ModifyTeamFrame(Admin administrador, String code, String name, String school, String itinerario) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setTitle("Modificar equipo");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(iconoVentana);

        String oldCode = code;

        introduceData = new JLabel("Modificar equipo");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        teamCode = new JLabel("Código (*)");
        teamCode.setFont(fuenteBotonesEtiquetas);

        teamName = new JLabel("Nombre (*)");
        teamName.setFont(fuenteBotonesEtiquetas);

        teamSchool = new JLabel("Centro educativo (*)");
        teamSchool.setFont(fuenteBotonesEtiquetas);

        teamItinerario = new JLabel("Itinerario (*)");
        teamItinerario.setFont(fuenteBotonesEtiquetas);

        teamCodeField = new JTextField(oldCode);
        teamCodeField.setFont(fuenteCampoTexto);

        teamNameField = new JTextField(name);
        teamNameField.setFont(fuenteCampoTexto);

        teamSchoolField = new JTextField(school);
        teamSchoolField.setFont(fuenteCampoTexto);

        teamItinerarioField = new JComboBox<>();
        teamItinerarioField.setFont(fuenteCampoTexto);

        ResultSet itCodes = administrador.selectCol("T_ITINERARIOS", "CODIGO");

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (itCodes.next()) {
            String registro = itCodes.getString("CODIGO");
            teamItinerarioField.addItem(registro);
        }

        itCodes.close();
        
        teamItinerarioField.setSelectedItem(itinerario);

        modifyTeamButton = new JButton("Modificar equipo");
        modifyTeamButton.setPreferredSize(new Dimension(150, 30));
        modifyTeamButton.setFont(fuenteBotonesEtiquetas);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(modifyTeamButton);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputPanel.add(teamCode);
        inputPanel.add(teamCodeField);
        inputPanel.add(teamName);
        inputPanel.add(teamNameField);
        inputPanel.add(teamSchool);
        inputPanel.add(teamSchoolField);
        inputPanel.add(teamItinerario);
        inputPanel.add(teamItinerarioField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
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
}