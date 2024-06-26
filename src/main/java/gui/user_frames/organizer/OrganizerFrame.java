package gui.user_frames.organizer;

import com.jcraft.jsch.JSchException;
import gui.*;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomLabel;
import gui.custom_components.labels.CustomSubtitleLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.template_pattern.UserFrameTemplate;
import users.Organizer;

import java.awt.*;
import java.sql.SQLException;

public class OrganizerFrame extends UserFrameTemplate {
    // Botones
    CustomButton assignExerciseToItinerary;
    CustomButton checkAssignationExIt;
    CustomButton cambioContrasea;

    // Etiquetas
    CustomLabel gestionItinerarioLabel;
    CustomLabel otrasGestionesLabel;

    // Paneles
    CustomPanel punctuationButtonsPanel;
    CustomPanel usuariosButtonsPanel;
    CustomPanel gestionItinerario;
    CustomPanel otrasGestiones;

    public OrganizerFrame(Organizer organizador) {
        super(365, "Panel Organizador", "¡Bienvenido al panel de organizador de OlympULL!");

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        assignExerciseToItinerary.addActionListener(e -> {
            try {
                new NewExItAssignationFrame(organizador);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        checkAssignationExIt.addActionListener(e -> {
            try {
                new CheckExItAssignationFrame(organizador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        cambioContrasea.addActionListener(e -> {
            new ChangePasswordFrame(organizador);
            dispose();

        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        gestionItinerarioLabel = new CustomSubtitleLabel("Gestión de itinerarios");

        assignExerciseToItinerary = new CustomButton("Asignar ejercicio a itinerario");
        checkAssignationExIt = new CustomButton("Consultar asignaciones de ejercicios a itinerarios");

        punctuationButtonsPanel = new CustomPanel();
        punctuationButtonsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        punctuationButtonsPanel.add(assignExerciseToItinerary);
        punctuationButtonsPanel.add(checkAssignationExIt);

        gestionItinerario = new CustomPanel();
        gestionItinerario.setLayout(new BorderLayout());
        gestionItinerario.setBorder(border);
        gestionItinerario.add(gestionItinerarioLabel, BorderLayout.NORTH);
        gestionItinerario.add(punctuationButtonsPanel, BorderLayout.CENTER);

        return gestionItinerario;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        otrasGestionesLabel = new CustomSubtitleLabel("Otras gestiones");
        otrasGestionesLabel.setBorder(border);

        cambioContrasea = new CustomButton("Cambiar contraseña");

        usuariosButtonsPanel = new CustomPanel();
        usuariosButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));
        usuariosButtonsPanel.add(cambioContrasea);

        otrasGestiones = new CustomPanel();
        otrasGestiones.setLayout(new BorderLayout());
        otrasGestiones.add(otrasGestionesLabel, BorderLayout.NORTH);
        otrasGestiones.add(usuariosButtonsPanel, BorderLayout.CENTER);

        return otrasGestiones;
    }
}

