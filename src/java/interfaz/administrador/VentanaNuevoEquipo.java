package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.*;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaNuevoEquipo extends JFrame implements Bordes, Fuentes, Iconos {
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
    JButton createExerButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaNuevoEquipo(Administrador administrador) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setTitle("Nuevo equipo");
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(iconoVentana);

        // Panel superior
        introduceData = new JLabel("Nuevo equipo");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Panel de inputs
        teamCode = new JLabel("Código (*)");
        teamCode.setFont(fuenteBotonesEtiquetas);

        teamName = new JLabel("Nombre (*)");
        teamName.setFont(fuenteBotonesEtiquetas);

        teamSchool = new JLabel("Centro educativo (*)");
        teamSchool.setFont(fuenteBotonesEtiquetas);

        teamItinerario = new JLabel("Itinerario (*)");
        teamItinerario.setFont(fuenteBotonesEtiquetas);

        teamCodeField = new JTextField();
        teamCodeField.setFont(fuenteCampoTexto);

        teamNameField = new JTextField();
        teamNameField.setFont(fuenteCampoTexto);

        teamSchoolField = new JTextField();
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

        // Panel de creación de equipo
        createExerButton = new JButton("Crear equipo");
        createExerButton.setPreferredSize(new Dimension(150, 30));
        createExerButton.setFont(fuenteBotonesEtiquetas);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(createExerButton);

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

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        createExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                    if (administrador.createTeam(code, name, school, itinerary) == 0) {
                        teamCodeField.setText("");
                        teamNameField.setText("");
                        teamSchoolField.setText("");
                        teamItinerarioField.setSelectedItem(teamItinerarioField.getItemAt(0));

                    }

                }
            }
        });

    }
}
