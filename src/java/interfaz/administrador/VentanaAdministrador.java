package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaAdministrador extends JFrame {
    JButton createOlympiad;
    JButton createExercise;
    JButton createUser;
    JButton deleteUser;
    JButton assignExerciseToUser;
    JLabel welcomeLabel;
    JPanel buttonsPanel;

    public VentanaAdministrador(Administrador administrador) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Menú Administrador");

        createOlympiad = new JButton("Crear nueva olimpiada");
        createOlympiad.setPreferredSize(new Dimension(200, 30));
        createExercise = new JButton("Crear nuevo ejercicio");
        createExercise.setPreferredSize(new Dimension(200, 30));
        createUser = new JButton("Crear nuevo usuario");
        createUser.setPreferredSize(new Dimension(200, 30));
        deleteUser = new JButton("Eliminar usuario");
        deleteUser.setPreferredSize(new Dimension(200, 30));

        assignExerciseToUser = new JButton("Asignar ejercicio a monitor");
        assignExerciseToUser.setPreferredSize(new Dimension(200, 30));

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));
        buttonsPanel.add(createOlympiad);
        buttonsPanel.add(createExercise);
        buttonsPanel.add(createUser);
        buttonsPanel.add(deleteUser);
        buttonsPanel.add(assignExerciseToUser);

        welcomeLabel = new JLabel("¡Bienvenido al panel de administrador de OlympULL!");
        welcomeLabel.setPreferredSize(new Dimension(200, 50));

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

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

        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaEliminarUsuario(administrador);
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
