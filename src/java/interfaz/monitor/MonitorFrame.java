package interfaz.monitor;

import interfaz.*;
import interfaz.custom_components.*;
import interfaz.template.UserFrameTemplate;
import users.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MonitorFrame extends UserFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton punctuateExercise;
    CustomButton checkPunctuations;
    CustomButton changePassword;

    // Labels
    CustomSubtitleLabel itineraryManagementLabel;
    CustomSubtitleLabel otherManagementsLabel;
    
    // Paneles
    CustomPanel punctuationsPanel;
    CustomPanel userButtonsPanel;
    CustomPanel itineraryManagement;
    CustomPanel otherManagements;

    public MonitorFrame(Monitor monitor) {
        super(375, "Panel Monitor", "¡Bienvenido al panel de monitor de OlympULL!");

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        punctuateExercise.addActionListener(e -> {
            try {
                new PunctuateExerciseFrame(monitor);
                dispose();
            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        checkPunctuations.addActionListener(e -> {
            try {
                new CheckPunctuationsFrame(monitor, monitor.getExerciseCode().get(0));
                dispose();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }

        });

        changePassword.addActionListener(e -> {
            new ChangePasswordFrame(monitor);
            dispose();
        });

        this.setVisible(true);
    }

    @Override
    protected CustomPanel createCenterPanel() {
        itineraryManagementLabel = new CustomSubtitleLabel("Gestión de puntuación de equipos");
        punctuateExercise = new CustomButton("Puntuar equipo");
        checkPunctuations = new CustomButton("Consultar puntuaciones realizadas");

        punctuationsPanel = new CustomPanel();
        punctuationsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        punctuationsPanel.add(punctuateExercise);
        punctuationsPanel.add(checkPunctuations);

        itineraryManagement = new CustomPanel();
        itineraryManagement.setLayout(new BorderLayout());
        itineraryManagement.add(itineraryManagementLabel, BorderLayout.NORTH);
        itineraryManagement.add(punctuationsPanel, BorderLayout.CENTER);
        
        return itineraryManagement;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        otherManagementsLabel = new CustomSubtitleLabel("Otras gestiones");

        changePassword = new CustomButton("Cambiar contraseña");
        changePassword.setFont(fuenteBotonesEtiquetas);
        changePassword.setPreferredSize(new Dimension(120, 30));

        userButtonsPanel = new CustomPanel();
        userButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));
        userButtonsPanel.add(changePassword);

        otherManagements = new CustomPanel();
        otherManagements.setLayout(new BorderLayout());
        otherManagements.add(otherManagementsLabel, BorderLayout.NORTH);
        otherManagements.add(userButtonsPanel, BorderLayout.CENTER);
        
        return otherManagements;
    }
}
