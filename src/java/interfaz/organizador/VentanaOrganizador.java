package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import interfaz.custom_components.*;
import interfaz.template.VentanaUsuario;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaOrganizador extends VentanaUsuario implements Bordes, Fuentes, Iconos {
    // Botones
    CustomButton assignExerciseToItinerary;
    CustomButton consultarAsignacionEjerciciosItinerarios;
    CustomButton cambioContrasea;
    CustomButton goBackButton;

    // Etiquetas
    CustomLabel welcomeLabel;
    CustomLabel gestionItinerarioLabel;
    CustomLabel otrasGestionesLabel;

    // Paneles
    JPanel olimpiadaButtonsPanel;
    JPanel usuariosButtonsPanel;
    JPanel upperBar;
    JPanel gestionItinerario;
    JPanel otrasGestiones;

    public VentanaOrganizador(Organizador organizador) {
        super(725, 375, "Panel Organizador", "¡Bienvenido al panel de organizador de OlympULL!");

        assignExerciseToItinerary = new CustomButton("Asignar ejercicio a itinerario");
        assignExerciseToItinerary.setPreferredSize(new Dimension(200, 30));
        assignExerciseToItinerary.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionEjerciciosItinerarios = new CustomButton("Consultar asignaciones de ejercicios a itinerarios");
        consultarAsignacionEjerciciosItinerarios.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionEjerciciosItinerarios.setFont(fuenteBotonesEtiquetas);

        olimpiadaButtonsPanel = new JPanel();
        olimpiadaButtonsPanel.setBorder(borde);
        olimpiadaButtonsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        olimpiadaButtonsPanel.add(assignExerciseToItinerary);
        olimpiadaButtonsPanel.add(consultarAsignacionEjerciciosItinerarios);

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));

        gestionItinerarioLabel = new CustomSubtitleLabel("Gestión de itinerarios");
        gestionItinerarioLabel.setFont(fuenteSubtitulo);
        gestionItinerarioLabel.setBorder(borde);

        otrasGestionesLabel = new CustomSubtitleLabel("Otras gestiones");
        otrasGestionesLabel.setFont(fuenteSubtitulo);
        otrasGestionesLabel.setBorder(borde);
        
        cambioContrasea = new CustomButton("Cambiar contraseña");
        cambioContrasea.setFont(fuenteBotonesEtiquetas);
        cambioContrasea.setPreferredSize(new Dimension(120, 30));

        usuariosButtonsPanel.add(cambioContrasea);

        gestionItinerario = new JPanel();
        gestionItinerario.setLayout(new BorderLayout());
        gestionItinerario.setBorder(borde);

        gestionItinerario.add(gestionItinerarioLabel, BorderLayout.NORTH);
        gestionItinerario.add(olimpiadaButtonsPanel, BorderLayout.CENTER);

        otrasGestiones = new JPanel();
        otrasGestiones.setLayout(new BorderLayout());
        otrasGestiones.setBorder(borde);
        otrasGestiones.add(otrasGestionesLabel, BorderLayout.NORTH);
        otrasGestiones.add(usuariosButtonsPanel, BorderLayout.CENTER);

        add(gestionItinerario, BorderLayout.CENTER);
        add(otrasGestiones, BorderLayout.SOUTH);

        assignExerciseToItinerary.addActionListener(e -> {
            try {
                new VentanaNuevaAsignacionEjIt(organizador);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        consultarAsignacionEjerciciosItinerarios.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjIt(organizador);

            } catch (SQLException | JSchException ex) {
                new ErrorJOptionPane("No se ha podido recuperar la información de la base de datos");

            }

            dispose();
        });

        cambioContrasea.addActionListener(e -> {
            new VentanaCambioContrasea(organizador);
            dispose();

        });
    }
}

