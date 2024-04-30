package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class VentanaAdministrador extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton createOlympiad;
    JButton consultarOlimpiadas;
    JButton crearItinerario;
    JButton consultarItinerario;
    JButton createExercise;
    JButton consultarEjercicios;
    JButton crearRubrica;
    JButton consultarRubricas;
    JButton crearEquipo;
    JButton consultarEquipos;
    JButton assignExerciseToOlympiad;
    JButton consultarAsignacionEjerciciosOlimpiadas;
    JButton createUser;
    JButton consultarUsuarios;
    JButton assignExerciseToUser;
    JButton consultarAsignacionEjerciciosMonitores;
    JButton asignarItinerarioAOrganizador;
    JButton consultarAsignacionItinerarioOrganizador;
    JButton goBackButton;
    JButton changePassword;

    // Etiquetas
    JLabel welcomeLabel;

    // Paneles
    JPanel olimpiadaButtonsPanel;
    JPanel usuariosButtonsPanel;
    JPanel upperBar;
    JPanel gestionOlimpiada;
    JPanel gestionUsuarios;

    public VentanaAdministrador(Administrador administrador) {
        setSize(825, 690);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Panel Administrador");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        goBackButton = new JButton("< Desconectar");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(120, 30));

        createOlympiad = new JButton("Crear nueva olimpiada");
        createOlympiad.setPreferredSize(new Dimension(200, 30));
        createOlympiad.setFont(fuenteBotonesEtiquetas);

        consultarOlimpiadas = new JButton("Consultar olimpiadas");
        consultarOlimpiadas.setPreferredSize(new Dimension(200, 30));
        consultarOlimpiadas.setFont(fuenteBotonesEtiquetas);

        crearItinerario = new JButton("Crear nuevo itinerario");
        crearItinerario.setPreferredSize(new Dimension(200, 30));
        crearItinerario.setFont(fuenteBotonesEtiquetas);

        consultarItinerario = new JButton("Consultar itinerarios");
        consultarItinerario.setPreferredSize(new Dimension(200, 30));
        consultarItinerario.setFont(fuenteBotonesEtiquetas);

        createExercise = new JButton("Crear nuevo ejercicio");
        createExercise.setPreferredSize(new Dimension(200, 30));
        createExercise.setFont(fuenteBotonesEtiquetas);

        consultarEjercicios = new JButton("Consultar ejercicios");
        consultarEjercicios.setPreferredSize(new Dimension(200, 30));
        consultarEjercicios.setFont(fuenteBotonesEtiquetas);

        crearEquipo = new JButton("Crear nuevo equipo");
        crearEquipo.setPreferredSize(new Dimension(200, 30));
        crearEquipo.setFont(fuenteBotonesEtiquetas);

        consultarEquipos = new JButton("Consultar equipos");
        consultarEquipos.setPreferredSize(new Dimension(200, 30));
        consultarEquipos.setFont(fuenteBotonesEtiquetas);

        crearRubrica = new JButton("Crear nueva rúbrica");
        crearRubrica.setPreferredSize(new Dimension(200, 30));
        crearRubrica.setFont(fuenteBotonesEtiquetas);

        consultarRubricas = new JButton("Consultar rúbricas");
        consultarRubricas.setPreferredSize(new Dimension(200, 30));
        consultarRubricas.setFont(fuenteBotonesEtiquetas);

        assignExerciseToOlympiad = new JButton("Asignar ejercicio a olimpiada");
        assignExerciseToOlympiad.setPreferredSize(new Dimension(200, 30));
        assignExerciseToOlympiad.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionEjerciciosOlimpiadas = new JButton("Consultar asignaciones de ejercicios a olimpiadas");
        consultarAsignacionEjerciciosOlimpiadas.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionEjerciciosOlimpiadas.setFont(fuenteBotonesEtiquetas);

        createUser = new JButton("Crear nuevo usuario");
        createUser.setPreferredSize(new Dimension(200, 30));
        createUser.setFont(fuenteBotonesEtiquetas);

        consultarUsuarios = new JButton("Consultar usuarios");
        consultarUsuarios.setPreferredSize(new Dimension(200, 30));
        consultarUsuarios.setFont(fuenteBotonesEtiquetas);

        assignExerciseToUser = new JButton("Asignar ejercicio a monitor");
        assignExerciseToUser.setPreferredSize(new Dimension(200, 30));
        assignExerciseToUser.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionEjerciciosMonitores = new JButton("Consultar asignaciones de ejercicios a monitores");
        consultarAsignacionEjerciciosMonitores.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionEjerciciosMonitores.setFont(fuenteBotonesEtiquetas);

        asignarItinerarioAOrganizador = new JButton("Asignar itinerario a organizador");
        asignarItinerarioAOrganizador.setPreferredSize(new Dimension(200, 30));
        asignarItinerarioAOrganizador.setFont(fuenteBotonesEtiquetas);

        consultarAsignacionItinerarioOrganizador = new JButton("Consultar asignaciones de itinerarios a organizadores");
        consultarAsignacionItinerarioOrganizador.setPreferredSize(new Dimension(200, 30));
        consultarAsignacionItinerarioOrganizador.setFont(fuenteBotonesEtiquetas);

        changePassword = new JButton("Cambiar contraseña");
        changePassword.setPreferredSize(new Dimension(200, 30));
        changePassword.setFont(fuenteBotonesEtiquetas);

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);
        upperBar.add(goBackButton, BorderLayout.EAST);

        olimpiadaButtonsPanel = new JPanel();
        olimpiadaButtonsPanel.setBorder(borde);
        olimpiadaButtonsPanel.setLayout(new GridLayout(6, 2, 15, 15));
        olimpiadaButtonsPanel.add(createOlympiad);
        olimpiadaButtonsPanel.add(consultarOlimpiadas);
        olimpiadaButtonsPanel.add(crearItinerario);
        olimpiadaButtonsPanel.add(consultarItinerario);
        olimpiadaButtonsPanel.add(createExercise);
        olimpiadaButtonsPanel.add(consultarEjercicios);
        olimpiadaButtonsPanel.add(crearRubrica);
        olimpiadaButtonsPanel.add(consultarRubricas);
        olimpiadaButtonsPanel.add(crearEquipo);
        olimpiadaButtonsPanel.add(consultarEquipos);
        olimpiadaButtonsPanel.add(assignExerciseToOlympiad);
        olimpiadaButtonsPanel.add(consultarAsignacionEjerciciosOlimpiadas);

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(4, 2, 15, 15));
        usuariosButtonsPanel.add(createUser);
        usuariosButtonsPanel.add(consultarUsuarios);
        usuariosButtonsPanel.add(assignExerciseToUser);
        usuariosButtonsPanel.add(consultarAsignacionEjerciciosMonitores);
        usuariosButtonsPanel.add(asignarItinerarioAOrganizador);
        usuariosButtonsPanel.add(consultarAsignacionItinerarioOrganizador);
        usuariosButtonsPanel.add(changePassword);

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

        welcomeLabel = new JLabel("¡Bienvenido al panel de administrador de OlympULL!");
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

        createOlympiad.addActionListener(e -> {
            new VentanaNuevaOlimpiada(administrador);
            dispose();
        });

        consultarOlimpiadas.addActionListener(e -> {
            try {
                new VentanaConsultaOlimpiadas(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");
            }
            dispose();
        });

        crearItinerario.addActionListener(e -> {
            try {
                new VentanaNuevoItinerario(administrador);

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        consultarItinerario.addActionListener(e -> {
            try {
                new VentanaConsultaItinerarios(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        createExercise.addActionListener(e -> {
            try {
                new VentanaNuevoEjercicio(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }

            dispose();
        });

        consultarEjercicios.addActionListener(e -> {
            try {
                new VentanaConsultaEjercicios(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        crearRubrica.addActionListener(e -> {
            new VentanaNuevaRubrica(administrador);
            dispose();
        });

        consultarRubricas.addActionListener(e -> {
            try {
                new VentanaConsultaRubricas(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        crearEquipo.addActionListener(e -> {
            try {
                new VentanaNuevoEquipo(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }

            dispose();
        });

        consultarEquipos.addActionListener(e -> {
            try {
                new VentanaConsultaEquipos(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        assignExerciseToOlympiad.addActionListener(e -> {
            try {
                new VentanaNuevaAsignacionEjOlimp(administrador);

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");


            }

            dispose();
        });

        consultarAsignacionEjerciciosOlimpiadas.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjOlimp(administrador);

            } catch (SQLException | JSchException ex) {
                new CustomJOptionPane("ERROR");

            }

            dispose();
        });

        createUser.addActionListener(e -> {
            new VentanaNuevoUsuario(administrador);
            dispose();
        });

        consultarUsuarios.addActionListener(e -> {
            try {
                new VentanaConsultaUsuarios(administrador);
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        assignExerciseToUser.addActionListener(e -> {
            try {
                new VentanaNuevaAsignacionEjMonitor(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido recuperar la información de la base de datos");

            }
        });

        consultarAsignacionEjerciciosMonitores.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjMonitor(administrador);
                dispose();
            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido recuperar la información de la base de datos");
            }
        });

        asignarItinerarioAOrganizador.addActionListener(e -> {
            try {
                new VentanaNuevaAsignacionItOrg(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido recuperar la información de la base de datos");

            }
        });

        consultarAsignacionItinerarioOrganizador.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionItOrg(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido recuperar la información de la base de datos");

            }
        });

        changePassword.addActionListener(e -> {
            new VentanaCambioContrasea(administrador);
            dispose();
        });

        this.setVisible(true);
    }
}
