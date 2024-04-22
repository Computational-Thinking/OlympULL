package interfaz.monitor;

import interfaz.*;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSeleccionarEjercicio extends JFrame implements Bordes, Fuentes, Iconos {
    // Labels
    JLabel titleLabel;
    JLabel exerCodeLabel;
    JLabel exerNameLabel;
    JLabel exerNameField;

    //JCombobox
    JComboBox<String> exerField;

    // Botones
    JButton goBackButton;
    JButton selectButton;

    // Paneles
    JPanel upperPanel;
    JPanel inputPanel;
    JPanel buttonsPanel;

    public VentanaSeleccionarEjercicio(Monitor monitor) {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Panel Monitor");
        setIconImage(iconoVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Panel superior
        titleLabel = new JLabel("Seleccionar ejercicio");
        titleLabel.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(titleLabel, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Panel de inputs
        exerCodeLabel = new JLabel("Código del ejercicio");
        exerCodeLabel.setFont(fuenteBotonesEtiquetas);

        exerField = new JComboBox<>();
        exerField.setFont(fuenteCampoTexto);

        exerNameLabel = new JLabel("Título del ejercicio");
        exerNameLabel.setFont(fuenteBotonesEtiquetas);

        exerNameField = new JLabel();
        exerNameField.setFont(fuenteCampoTexto);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 2, 5, 5));
        inputPanel.setBorder(borde);

        inputPanel.add(exerCodeLabel);
        inputPanel.add(exerField);
        inputPanel.add(exerNameLabel);
        inputPanel.add(exerNameField);

        // Panel de botón
        selectButton = new JButton("Seleccionar");
        selectButton.setPreferredSize(new Dimension(200, 30));
        selectButton.setFont(fuenteBotonesEtiquetas);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout());
        buttonsPanel.setBorder(borde);

        buttonsPanel.add(selectButton);

        // Se añaden los elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new VentanaMonitor(monitor);
            dispose();

        });

        this.setVisible(true);
    }
}
