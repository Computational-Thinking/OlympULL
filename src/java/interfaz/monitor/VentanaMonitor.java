package interfaz.monitor;

import interfaz.*;
import interfaz.administrador.VentanaNuevaRubrica;
import interfaz.administrador.VentanaCambioContrasea;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMonitor extends JFrame implements Bordes, Fuentes, Iconos {
    // Labels
    JLabel titleLabel;

    // Botones
    JButton goBackButton;
    JButton botonPuntuar;
    JButton botonConsultaPuntuaciones;

    // Paneles
    JPanel upperPanel;
    JPanel buttonsPanel;

    public VentanaMonitor(Monitor monitor) {
        // Configuración de la ventana
        setSize(750, 300);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Panel Monitor");
        setIconImage(iconoVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Panel superior
        titleLabel = new JLabel("¡Bienvenido al panel de monitor de OlympULL!");
        titleLabel.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(titleLabel, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Panel de botones
        botonPuntuar = new JButton("Puntuar equipo");
        botonPuntuar.setPreferredSize(new Dimension(200, 30));
        botonPuntuar.setFont(fuenteBotonesEtiquetas);

        botonConsultaPuntuaciones = new JButton("Consultar puntuaciones");
        botonConsultaPuntuaciones.setPreferredSize(new Dimension(200, 30));
        botonConsultaPuntuaciones.setFont(fuenteBotonesEtiquetas);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(2, 1, 5, 5));
        buttonsPanel.setBorder(borde);

        buttonsPanel.add(botonPuntuar);
        buttonsPanel.add(botonConsultaPuntuaciones);

        // Se añaden los elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new VentanaInicio();
            dispose();

        });

        botonPuntuar.addActionListener(e -> {
            new VentanaPuntuarEjercicio(monitor);
            dispose();
        });

        botonConsultaPuntuaciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (monitor.getExerciseCode().isEmpty()) {
                    new CustomJOptionPane("No tiene ejercicios que puntuar. Póngase en contacto con un administrador.");

                } else if (monitor.getExerciseCode().size() == 1) {
                    new VentanaPuntuarEjercicio(monitor);
                    dispose();

                } else {
                    new VentanaSeleccionarEjercicio(monitor);
                    dispose();
                }
            }
        });


        this.setVisible(true);
    }
}