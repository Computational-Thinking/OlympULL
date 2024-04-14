package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.VentanaInicio;
import usuarios.Administrador;

import javax.swing.*;
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
    JButton crearEquipo;
    JButton consultarEquipos;
    JButton createUser;
    JButton consultarUsuarios;
    JButton deleteUser;
    JButton assignExerciseToUser;
    JLabel welcomeLabel;
    JPanel buttonsPanel;
    JButton goBackButton;
    JPanel upperBar;
    JPanel upperBarWelcomeLabel;

    public VentanaAdministrador(Administrador administrador) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Menú Administrador");

        goBackButton = new JButton("< Volver");

        createOlympiad = new JButton("Crear nueva olimpiada");
        createOlympiad.setPreferredSize(new Dimension(200, 30));
        consultarOlimpiadas = new JButton("Consultar olimpiadas");
        consultarOlimpiadas.setPreferredSize(new Dimension(200, 30));
        crearItinerario = new JButton("Crear nuevo itinerario");
        crearItinerario.setPreferredSize(new Dimension(200, 30));
        consultarItinerario = new JButton("Consultar itinerarios");
        consultarItinerario.setPreferredSize(new Dimension(200, 30));
        createExercise = new JButton("Crear nuevo ejercicio");
        createExercise.setPreferredSize(new Dimension(200, 30));
        consultarEjercicios = new JButton("Consultar ejercicios");
        consultarEjercicios.setPreferredSize(new Dimension(200, 30));
        crearEquipo = new JButton("Crear nuevo equipo");
        crearEquipo.setPreferredSize(new Dimension(200, 30));
        consultarEquipos = new JButton("Crear nuevo equipo");
        consultarEquipos.setPreferredSize(new Dimension(200, 30));
        createUser = new JButton("Crear nuevo usuario");
        createUser.setPreferredSize(new Dimension(200, 30));
        consultarUsuarios = new JButton("Consultar usuarios");
        consultarUsuarios.setPreferredSize(new Dimension(200, 30));

        assignExerciseToUser = new JButton("Asignar ejercicio a monitor");
        assignExerciseToUser.setPreferredSize(new Dimension(200, 30));

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.add(goBackButton);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(6, 2));
        buttonsPanel.add(createOlympiad);
        buttonsPanel.add(consultarOlimpiadas);
        buttonsPanel.add(crearItinerario);
        buttonsPanel.add(consultarItinerario);
        buttonsPanel.add(createExercise);
        buttonsPanel.add(consultarEjercicios);
        buttonsPanel.add(crearEquipo);
        buttonsPanel.add(consultarEquipos);
        buttonsPanel.add(createUser);
        buttonsPanel.add(consultarUsuarios);
        buttonsPanel.add(assignExerciseToUser);

        welcomeLabel = new JLabel("¡Bienvenido al panel de administrador de OlympULL!");
        welcomeLabel.setPreferredSize(new Dimension(200, 50));

        upperBarWelcomeLabel = new JPanel();
        upperBarWelcomeLabel.setLayout(new GridLayout(2, 1));
        upperBarWelcomeLabel.add(welcomeLabel);
        upperBarWelcomeLabel.add(upperBar);

        add(upperBarWelcomeLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

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
            }
        });

        createExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoEjercicio(administrador);
            }
        });

        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoUsuario(administrador);
            }
        });

        /**
        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaEliminarUsuario(administrador);
            }
        });
         */

        assignExerciseToUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAsignacionUsuario(administrador);
            }
        });

        this.setVisible(true);
    }
}
