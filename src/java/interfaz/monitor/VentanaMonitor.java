package interfaz.monitor;

import interfaz.*;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaMonitor extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton punctuateExercise;
    JButton checkPunctuations;
    JButton cambioContrasea;
    JButton goBackButton;

    // Etiquetas
    JLabel welcomeLabel;

    // Paneles
    JPanel punctuationsPanel;
    JPanel usuariosButtonsPanel;
    JPanel upperBar;
    JPanel gestionItinerario;
    JPanel otrasGestiones;

    public VentanaMonitor(Monitor monitor) {
        setSize(725, 375);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Panel Monitor");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        goBackButton = new JButton("< Desconectar");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(120, 30));

        punctuateExercise = new JButton("Puntuar equipo");
        punctuateExercise.setPreferredSize(new Dimension(200, 30));
        punctuateExercise.setFont(fuenteBotonesEtiquetas);

        checkPunctuations = new JButton("Consultar puntuaciones realizadas");
        checkPunctuations.setPreferredSize(new Dimension(200, 30));
        checkPunctuations.setFont(fuenteBotonesEtiquetas);

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);
        upperBar.add(goBackButton, BorderLayout.EAST);

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

        welcomeLabel = new JLabel("¡Bienvenido al panel de monitor de OlympULL!");
        welcomeLabel.setFont(fuenteTitulo);
        //welcomeLabel.setPreferredSize(new Dimension(200, 50));
        upperBar.add(welcomeLabel, BorderLayout.CENTER);

        add(upperBar, BorderLayout.NORTH);
        add(gestionItinerario, BorderLayout.CENTER);
        add(otrasGestiones, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new VentanaInicio();
            dispose();
        });

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
