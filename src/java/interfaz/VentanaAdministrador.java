package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdministrador extends JFrame {
    JButton createOlympiad;
    JButton createExercise;
    JButton createUser;
    JButton deleteUser;

    public VentanaAdministrador() {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Menú Administrador");

        createOlympiad = new JButton("Crear nueva olimpiada");
        createExercise = new JButton("Crear nuevo ejercicio");
        createUser = new JButton("Crear nuevo usuario");
        deleteUser = new JButton("Eliminar usuario");

        add(createOlympiad);
        add(createExercise);
        add(createUser);
        add(deleteUser);

        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaNuevoUsuario ventana = new VentanaNuevoUsuario();
            }
        });

        deleteUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaEliminarUsuario ventana = new VentanaEliminarUsuario();
            }
        });
        this.setVisible(true);
    }
}
