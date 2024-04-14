package interfaz.monitor;

import interfaz.administrador.VentanaBaremo;
import interfaz.administrador.VentanaCambioContrasea;
import interfaz.monitor.VentanaPuntuarEjercicio;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMonitor extends JFrame {
    JButton botonEstablecerBaremo;
    JButton botonPuntuar;
    JButton botonDesconectar;
    JButton botonCambioContrasena;
    JPanel buttonsPanel;
    JLabel welcomeLabel;

    public VentanaMonitor(Monitor monitor) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Menú Monitor");

        botonEstablecerBaremo = new JButton("Establecer baremo de ejercicio");
        botonEstablecerBaremo.setPreferredSize(new Dimension(200, 30));
        botonPuntuar = new JButton("Puntuar ejercicio");
        botonPuntuar.setPreferredSize(new Dimension(200, 30));
        botonDesconectar = new JButton("Desconectar");
        botonEstablecerBaremo.setPreferredSize(new Dimension(200, 30));
        botonCambioContrasena = new JButton("Cambiar contraseña");

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));

        buttonsPanel.add(botonEstablecerBaremo);
        buttonsPanel.add(botonPuntuar);
        buttonsPanel.add(botonCambioContrasena);
        buttonsPanel.add(botonDesconectar);

        welcomeLabel = new JLabel("¡Bienvenido al panel de monitor de OlympULL!");
        welcomeLabel.setPreferredSize(new Dimension(200, 50));

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

        botonEstablecerBaremo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaBaremo ventana = new VentanaBaremo(monitor);
            }
        });

        botonPuntuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaPuntuarEjercicio ventana = new VentanaPuntuarEjercicio(monitor);
            }
        });

        botonCambioContrasena.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaCambioContrasea ventana = new VentanaCambioContrasea(monitor);
            }
        });

        botonDesconectar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.setVisible(true);
    }
}