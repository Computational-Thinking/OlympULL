package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaOrganizador extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton assignExerciseToItinerary;
    JButton consultarAsignacionEjerciciosItinerarios;
    JButton goBackButton;

    // Etiquetas
    JLabel welcomeLabel;

    // Paneles
    JPanel olimpiadaButtonsPanel;
    JPanel usuariosButtonsPanel;
    JPanel upperBar;
    JPanel gestionOlimpiada;
    JPanel gestionUsuarios;

    public VentanaOrganizador(Organizador organizador) {
        setSize(825, 650);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Panel Administrador");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        goBackButton = new JButton("< Desconectar");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(120, 30));

        assignExerciseToItinerary = new JButton("Asignar ejercicio a olimpiada");
        assignExerciseToItinerary.setPreferredSize(new Dimension(200, 30));
        assignExerciseToItinerary.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionEjerciciosItinerarios = new JButton("Consultar asignaciones de ejercicios a olimpiadas");
        consultarAsignacionEjerciciosItinerarios.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionEjerciciosItinerarios.setFont(fuenteBotonesEtiquetas);

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);
        upperBar.add(goBackButton, BorderLayout.EAST);

        olimpiadaButtonsPanel = new JPanel();
        olimpiadaButtonsPanel.setBorder(borde);
        olimpiadaButtonsPanel.setLayout(new GridLayout(6, 2, 15, 15));
        olimpiadaButtonsPanel.add(assignExerciseToItinerary);
        olimpiadaButtonsPanel.add(consultarAsignacionEjerciciosItinerarios);

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(3, 2, 15, 15));

        JLabel gestionOlimpiadaLabel = new JLabel("Gestión de olimpiadas");
        gestionOlimpiadaLabel.setFont(fuenteSubtitulo);
        gestionOlimpiadaLabel.setBorder(borde);


        JLabel gestionUsuariosLabel = new JLabel("Gestión de usuarios");
        gestionUsuariosLabel.setFont(fuenteSubtitulo);
        gestionUsuariosLabel.setBorder(borde);

        gestionOlimpiada = new JPanel();
        gestionOlimpiada.setLayout(new BorderLayout());
        gestionOlimpiada.setBorder(borde);

        gestionOlimpiada.add(gestionOlimpiadaLabel, BorderLayout.NORTH);
        gestionOlimpiada.add(olimpiadaButtonsPanel, BorderLayout.CENTER);

        gestionUsuarios = new JPanel();
        gestionUsuarios.setLayout(new BorderLayout());
        gestionUsuarios.setBorder(borde);
        gestionUsuarios.add(gestionUsuariosLabel, BorderLayout.NORTH);
        gestionUsuarios.add(usuariosButtonsPanel, BorderLayout.CENTER);

        welcomeLabel = new JLabel("¡Bienvenido al panel de organizador de OlympULL!");
        welcomeLabel.setFont(fuenteTitulo);
        //welcomeLabel.setPreferredSize(new Dimension(200, 50));
        upperBar.add(welcomeLabel, BorderLayout.CENTER);

        add(upperBar, BorderLayout.NORTH);
        add(gestionOlimpiada, BorderLayout.CENTER);
        add(gestionUsuarios, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new VentanaInicio();
            dispose();
        });

        assignExerciseToItinerary.addActionListener(e -> {
            try {
                new VentanaNuevaAsignacionEjIt(organizador);

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        consultarAsignacionEjerciciosItinerarios.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjIt(organizador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        this.setVisible(true);
    }
}

