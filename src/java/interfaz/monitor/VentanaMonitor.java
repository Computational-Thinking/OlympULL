package interfaz.monitor;

import interfaz.*;
import interfaz.custom_components.Bordes;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fuentes;
import interfaz.custom_components.Iconos;
import interfaz.template.VentanaUsuario;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaMonitor extends VentanaUsuario implements Bordes, Fuentes, Iconos {
    // Botones
    JButton punctuateExercise;
    JButton checkPunctuations;
    JButton cambioContrasea;

    // Paneles
    JPanel punctuationsPanel;
    JPanel usuariosButtonsPanel;
    JPanel gestionItinerario;
    JPanel otrasGestiones;

    public VentanaMonitor(Monitor monitor) {
        super(725, 375, "Panel Monitor", "¡Bienvenido al panel de monitor de OlympULL!");

        punctuateExercise = new JButton("Puntuar equipo");
        punctuateExercise.setPreferredSize(new Dimension(200, 30));
        punctuateExercise.setFont(fuenteBotonesEtiquetas);

        checkPunctuations = new JButton("Consultar puntuaciones realizadas");
        checkPunctuations.setPreferredSize(new Dimension(200, 30));
        checkPunctuations.setFont(fuenteBotonesEtiquetas);

        punctuationsPanel = new JPanel();
        punctuationsPanel.setBorder(borde);
        punctuationsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        punctuationsPanel.add(punctuateExercise);
        punctuationsPanel.add(checkPunctuations);

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));

        JLabel gestionItinerarioLabel = new JLabel("Gestión de puntuación de equipos");
        gestionItinerarioLabel.setFont(fuenteSubtitulo);
        gestionItinerarioLabel.setBorder(borde);

        JLabel otrasGestionesLabel = new JLabel("Otras gestiones");
        otrasGestionesLabel.setFont(fuenteSubtitulo);
        otrasGestionesLabel.setBorder(borde);

        cambioContrasea = new JButton("Cambiar contraseña");
        cambioContrasea.setFont(fuenteBotonesEtiquetas);
        cambioContrasea.setPreferredSize(new Dimension(120, 30));

        usuariosButtonsPanel.add(cambioContrasea);

        gestionItinerario = new JPanel();
        gestionItinerario.setLayout(new BorderLayout());
        gestionItinerario.setBorder(borde);

        gestionItinerario.add(gestionItinerarioLabel, BorderLayout.NORTH);
        gestionItinerario.add(punctuationsPanel, BorderLayout.CENTER);

        otrasGestiones = new JPanel();
        otrasGestiones.setLayout(new BorderLayout());
        otrasGestiones.setBorder(borde);
        otrasGestiones.add(otrasGestionesLabel, BorderLayout.NORTH);
        otrasGestiones.add(usuariosButtonsPanel, BorderLayout.CENTER);

        add(gestionItinerario, BorderLayout.CENTER);
        add(otrasGestiones, BorderLayout.SOUTH);

        punctuateExercise.addActionListener(e -> {
            try {
                new VentanaPuntuarEjercicio(monitor);
                dispose();
            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        checkPunctuations.addActionListener(e -> {
            try {
                new VentanaConsultaPuntuaciones(monitor, monitor.getExerciseCode().get(0));
                dispose();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }

        });

        cambioContrasea.addActionListener(e -> {
            new VentanaCambioContrasea(monitor);
            dispose();
        });

        this.setVisible(true);
    }
}
