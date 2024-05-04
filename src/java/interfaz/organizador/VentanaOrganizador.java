package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import interfaz.custom_components.Bordes;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fuentes;
import interfaz.custom_components.Iconos;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaOrganizador extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton assignExerciseToItinerary;
    JButton consultarAsignacionEjerciciosItinerarios;
    JButton cambioContrasea;
    JButton goBackButton;

    // Etiquetas
    JLabel welcomeLabel;

    // Paneles
    JPanel olimpiadaButtonsPanel;
    JPanel usuariosButtonsPanel;
    JPanel upperBar;
    JPanel gestionItinerario;
    JPanel otrasGestiones;

    public VentanaOrganizador(Organizador organizador) {
        setSize(725, 375);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Panel Organizador");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        goBackButton = new JButton("< Desconectar");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(120, 30));

        assignExerciseToItinerary = new JButton("Asignar ejercicio a itinerario");
        assignExerciseToItinerary.setPreferredSize(new Dimension(200, 30));
        assignExerciseToItinerary.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionEjerciciosItinerarios = new JButton("Consultar asignaciones de ejercicios a itinerarios");
        consultarAsignacionEjerciciosItinerarios.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionEjerciciosItinerarios.setFont(fuenteBotonesEtiquetas);

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);
        upperBar.add(goBackButton, BorderLayout.EAST);

        olimpiadaButtonsPanel = new JPanel();
        olimpiadaButtonsPanel.setBorder(borde);
        olimpiadaButtonsPanel.setLayout(new GridLayout(2, 1, 15, 15));
        olimpiadaButtonsPanel.add(assignExerciseToItinerary);
        olimpiadaButtonsPanel.add(consultarAsignacionEjerciciosItinerarios);

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(1, 1, 15, 15));

        JLabel gestionItinerarioLabel = new JLabel("Gestión de itinerarios");
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
        gestionItinerario.add(olimpiadaButtonsPanel, BorderLayout.CENTER);

        otrasGestiones = new JPanel();
        otrasGestiones.setLayout(new BorderLayout());
        otrasGestiones.setBorder(borde);
        otrasGestiones.add(otrasGestionesLabel, BorderLayout.NORTH);
        otrasGestiones.add(usuariosButtonsPanel, BorderLayout.CENTER);

        welcomeLabel = new JLabel("¡Bienvenido al panel de organizador de OlympULL!");
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

        this.setVisible(true);
    }
}

