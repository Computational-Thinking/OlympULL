package interfaz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdministrador extends JFrame {
    JButton createOlympiad;
    JButton createExercise;
    JButton createUser;

    public VentanaAdministrador() {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Menú Administrador");

        createOlympiad = new JButton("Crear nueva olimpiada");
        createExercise = new JButton("Crear nuevo ejercicio");
        createUser = new JButton("Crear nuevo usuario");

        add(createOlympiad);
        add(createExercise);
        add(createUser);

        createUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaNuevoUsuario ventana = new VentanaNuevoUsuario();
            }
        });
        this.setVisible(true);
    }
}
