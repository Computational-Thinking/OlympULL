package interfaz.administrador;

import usuarios.Administrador;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaMonitor extends JFrame {
    JButton botonEstablecerBaremo;
    JButton botonPuntuar;
    JButton botonDesconectar;
    JPanel buttonsPanel;
    JLabel welcomeLabel;

    public VentanaMonitor(Monitor monitor) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Menú Administrador");

        botonEstablecerBaremo = new JButton("Establecer baremo de ejercicio");
        botonEstablecerBaremo.setPreferredSize(new Dimension(200, 30));
        botonPuntuar = new JButton("Puntuar ejercicio");
        botonPuntuar.setPreferredSize(new Dimension(200, 30));
        botonDesconectar = new JButton("Desconectar");
        botonEstablecerBaremo.setPreferredSize(new Dimension(200, 30));

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(4, 1));
        buttonsPanel.add(botonEstablecerBaremo);
        buttonsPanel.add(botonPuntuar);
        buttonsPanel.add(botonDesconectar);

        welcomeLabel = new JLabel("¡Bienvenido al panel de monitor de OlympULL!");
        welcomeLabel.setPreferredSize(new Dimension(200, 50));

        add(welcomeLabel, BorderLayout.NORTH);
        add(buttonsPanel, BorderLayout.CENTER);

        botonEstablecerBaremo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        botonPuntuar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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