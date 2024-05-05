package interfaz.monitor;

import interfaz.*;
import interfaz.custom_components.*;
import interfaz.template.UserFrame;
import users.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MonitorFrame extends UserFrame implements Borders, Fonts, Icons {
    // Botones
    CustomButton punctuateExercise;
    CustomButton checkPunctuations;
    CustomButton changePassword;

    // Labels
    CustomSubtitleLabel itineraryManagementLabel;
    CustomSubtitleLabel otherManagementsLabel;
    
    // Paneles
    JPanel punctuationsPanel;
    JPanel userButtonsPanel;
    JPanel itineraryManagement;
    JPanel otherManagements;

    public MonitorFrame(Monitor monitor) {
        super(725, 375, "Panel Monitor", "¡Bienvenido al panel de monitor de OlympULL!", "< Desconectar");

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
    protected JPanel createCenterPanel() {
        itineraryManagementLabel = new CustomSubtitleLabel("Gestión de puntuación de equipos");
        punctuateExercise = new CustomButton("Puntuar equipo");
        checkPunctuations = new CustomButton("Consultar puntuaciones realizadas");

        punctuationsPanel = new JPanel();
        punctuationsPanel.setBorder(borde);
        punctuationsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        punctuationsPanel.add(punctuateExercise);
        punctuationsPanel.add(checkPunctuations);

        itineraryManagement = new JPanel();
        itineraryManagement.setLayout(new BorderLayout());
        itineraryManagement.setBorder(borde);
        itineraryManagement.add(itineraryManagementLabel, BorderLayout.NORTH);
        itineraryManagement.add(punctuationsPanel, BorderLayout.CENTER);
        
        return itineraryManagement;
    }

    @Override
    protected JPanel createSouthPanel() {
        otherManagementsLabel = new CustomSubtitleLabel("Otras gestiones");

        changePassword = new CustomButton("Cambiar contraseña");
        changePassword.setFont(fuenteBotonesEtiquetas);
        changePassword.setPreferredSize(new Dimension(120, 30));

        userButtonsPanel = new JPanel();
        userButtonsPanel.setBorder(borde);
        userButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));
        userButtonsPanel.add(changePassword);

        otherManagements = new JPanel();
        otherManagements.setLayout(new BorderLayout());
        otherManagements.setBorder(borde);
        otherManagements.add(otherManagementsLabel, BorderLayout.NORTH);
        otherManagements.add(userButtonsPanel, BorderLayout.CENTER);
        
        return otherManagements;
    }
}
