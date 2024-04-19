package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.VentanaInicio;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaAdministrador extends JFrame {
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
    JButton createUser;
    JButton consultarUsuarios;
    JButton assignExerciseToUser;
    JButton asignarItinerarioAOrganizador;
    JLabel welcomeLabel;
    JPanel olimpiadaButtonsPanel;
    JPanel usuariosButtonsPanel;
    JButton goBackButton;
    JPanel upperBar;
    JPanel gestionOlimpiada;
    JPanel gestionUsuarios;

    public VentanaAdministrador(Administrador administrador) {
        setSize(750, 560);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Panel Administrador");
        setLocationRelativeTo(null);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Bold", Font.PLAIN, 18);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        createOlympiad = new JButton("Crear nueva olimpiada");
        createOlympiad.setPreferredSize(new Dimension(200, 30));
        createOlympiad.setFont(fuenteNegrita3);

        consultarOlimpiadas = new JButton("Consultar olimpiadas");
        consultarOlimpiadas.setPreferredSize(new Dimension(200, 30));
        consultarOlimpiadas.setFont(fuenteNegrita3);

        crearItinerario = new JButton("Crear nuevo itinerario");
        crearItinerario.setPreferredSize(new Dimension(200, 30));
        crearItinerario.setFont(fuenteNegrita3);

        consultarItinerario = new JButton("Consultar itinerarios");
        consultarItinerario.setPreferredSize(new Dimension(200, 30));
        consultarItinerario.setFont(fuenteNegrita3);

        createExercise = new JButton("Crear nuevo ejercicio");
        createExercise.setPreferredSize(new Dimension(200, 30));
        createExercise.setFont(fuenteNegrita3);

        consultarEjercicios = new JButton("Consultar ejercicios");
        consultarEjercicios.setPreferredSize(new Dimension(200, 30));
        consultarEjercicios.setFont(fuenteNegrita3);

        crearEquipo = new JButton("Crear nuevo equipo");
        crearEquipo.setPreferredSize(new Dimension(200, 30));
        crearEquipo.setFont(fuenteNegrita3);

        consultarEquipos = new JButton("Consultar equipos");
        consultarEquipos.setPreferredSize(new Dimension(200, 30));
        consultarEquipos.setFont(fuenteNegrita3);

        crearRubrica = new JButton("Crear nueva rúbrica");
        crearRubrica.setPreferredSize(new Dimension(200, 30));
        crearRubrica.setFont(fuenteNegrita3);

        consultarRubricas = new JButton("Consultar rúbricas");
        consultarRubricas.setPreferredSize(new Dimension(200, 30));
        consultarRubricas.setFont(fuenteNegrita3);

        createUser = new JButton("Crear nuevo usuario");
        createUser.setPreferredSize(new Dimension(200, 30));
        createUser.setFont(fuenteNegrita3);

        consultarUsuarios = new JButton("Consultar usuarios");
        consultarUsuarios.setPreferredSize(new Dimension(200, 30));
        consultarUsuarios.setFont(fuenteNegrita3);

        assignExerciseToUser = new JButton("Asignar ejercicio a monitor");
        assignExerciseToUser.setPreferredSize(new Dimension(200, 30));
        assignExerciseToUser.setFont(fuenteNegrita3);

        asignarItinerarioAOrganizador = new JButton("Asignar itinerario a organizador");
        asignarItinerarioAOrganizador.setPreferredSize(new Dimension(200, 30));
        asignarItinerarioAOrganizador.setFont(fuenteNegrita3);

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);
        upperBar.add(goBackButton, BorderLayout.EAST);

        olimpiadaButtonsPanel = new JPanel();
        olimpiadaButtonsPanel.setBorder(borde);
        olimpiadaButtonsPanel.setLayout(new GridLayout(5, 2, 15, 15));
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

        usuariosButtonsPanel = new JPanel();
        usuariosButtonsPanel.setBorder(borde);
        usuariosButtonsPanel.setLayout(new GridLayout(2, 2, 15, 15));
        usuariosButtonsPanel.add(createUser);
        usuariosButtonsPanel.add(consultarUsuarios);
        usuariosButtonsPanel.add(assignExerciseToUser);
        usuariosButtonsPanel.add(asignarItinerarioAOrganizador);

        JLabel gestionOlimpiadaLabel = new JLabel("Gestión de olimpiadas");
        gestionOlimpiadaLabel.setFont(fuenteNegrita2);
        gestionOlimpiadaLabel.setBorder(borde);


        JLabel gestionUsuariosLabel = new JLabel("Gestión de usuarios");
        gestionUsuariosLabel.setFont(fuenteNegrita2);
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
        welcomeLabel.setFont(fuenteNegrita1);
        //welcomeLabel.setPreferredSize(new Dimension(200, 50));
        upperBar.add(welcomeLabel, BorderLayout.CENTER);

        add(upperBar, BorderLayout.NORTH);
        add(gestionOlimpiada, BorderLayout.CENTER);
        add(gestionUsuarios, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaInicio ventana = new VentanaInicio();
                dispose();
            }
        });

        createOlympiad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevaOlimpiada(administrador);
                dispose();
            }
        });

        consultarOlimpiadas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VentanaConsultaOlimpiadas ventana = new VentanaConsultaOlimpiadas(administrador);

                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
                dispose();
            }
        });

        crearItinerario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoItinerario(administrador);
                dispose();
            }
        });

        consultarItinerario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VentanaConsultaItinerarios ventana = new VentanaConsultaItinerarios(administrador);

                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
                dispose();
            }
        });

        createExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoEjercicio(administrador);
                dispose();
            }
        });

        consultarEjercicios.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VentanaConsultaEjercicios ventana = new VentanaConsultaEjercicios(administrador);
                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
                dispose();
            }
        });

        crearRubrica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevaRubrica(administrador);
                dispose();
            }
        });

        consultarRubricas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new VentanaConsultaRubricas(administrador);
                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
                dispose();
            }
        });

        crearEquipo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoEquipo(administrador);
                dispose();
            }
        });

        consultarEquipos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new VentanaConsultaEquipos(administrador);
                } catch (SQLException | JSchException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error");
                }
                dispose();
            }
        });

        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoUsuario(administrador);
            }
        });

        assignExerciseToUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAsignacionUsuario(administrador);
            }
        });

        this.setVisible(true);
    }
}
