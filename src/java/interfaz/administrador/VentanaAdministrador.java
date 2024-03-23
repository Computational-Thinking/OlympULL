package interfaz.administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdministrador extends JFrame {
    JButton createOlympiad;
    JButton createExercise;
    JButton createUser;
    JButton deleteUser;
    JLabel welcomeLabel;
    JPanel buttonsPanel;

    public VentanaAdministrador() {
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

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));
        buttonsPanel.add(createOlympiad);
        buttonsPanel.add(createExercise);
        buttonsPanel.add(createUser);
        buttonsPanel.add(deleteUser);

        welcomeLabel = new JLabel("¡Bienvenido al panel de administrador de OlympULL!");
        welcomeLabel.setPreferredSize(new Dimension(200, 50));

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

        createOlympiad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevaOlimpiada();
            }
        });

        createExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoEjercicio();
            }
        });

        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaNuevoUsuario();
            }
        });

        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaEliminarUsuario();
            }
        });

        this.setVisible(true);
    }
}
