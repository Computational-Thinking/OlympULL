package interfaz.organizer;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import interfaz.custom_components.*;
import interfaz.template.UserFrame;
import users.Organizer;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class OrganizerFrame extends UserFrame implements Borders, Fonts, Icons {
    // Botones
    CustomButton assignExerciseToItinerary;
    CustomButton checkAssignationExIt;
    CustomButton cambioContrasea;

    // Etiquetas
    CustomLabel gestionItinerarioLabel;
    CustomLabel otrasGestionesLabel;

    // Paneles
    JPanel punctuationButtonsPanel;
    JPanel usuariosButtonsPanel;
    JPanel gestionItinerario;
    JPanel otrasGestiones;

    public OrganizerFrame(Organizer organizador) {
        super(725, 375, "Panel Organizador", "¡Bienvenido al panel de organizador de OlympULL!", "< Desconectar");

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

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
    protected JPanel createCenterPanel() {
        gestionItinerarioLabel = new CustomSubtitleLabel("Gestión de itinerarios");
        gestionItinerarioLabel.setBorder(borde);

        assignExerciseToItinerary = new CustomButton("Asignar ejercicio a itinerario");
        checkAssignationExIt = new CustomButton("Consultar asignaciones de ejercicios a itinerarios");

        punctuationButtonsPanel = new JPanel();
        punctuationButtonsPanel.setBorder(borde);
        punctuationButtonsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        punctuationButtonsPanel.add(assignExerciseToItinerary);
        punctuationButtonsPanel.add(checkAssignationExIt);

        gestionItinerario = new JPanel();
        gestionItinerario.setLayout(new BorderLayout());
        gestionItinerario.setBorder(borde);
        gestionItinerario.add(gestionItinerarioLabel, BorderLayout.NORTH);
        gestionItinerario.add(punctuationButtonsPanel, BorderLayout.CENTER);

        return gestionItinerario;
    }

    @Override
    protected JPanel createSouthPanel() {
        otrasGestionesLabel = new CustomSubtitleLabel("Otras gestiones");
        otrasGestionesLabel.setBorder(borde);

        cambioContrasea = new CustomButton("Cambiar contraseña");

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));
        usuariosButtonsPanel.add(cambioContrasea);

        otrasGestiones = new JPanel();
        otrasGestiones.setLayout(new BorderLayout());
        otrasGestiones.setBorder(borde);
        otrasGestiones.add(otrasGestionesLabel, BorderLayout.NORTH);
        otrasGestiones.add(usuariosButtonsPanel, BorderLayout.CENTER);

        return otrasGestiones;
    }
}

