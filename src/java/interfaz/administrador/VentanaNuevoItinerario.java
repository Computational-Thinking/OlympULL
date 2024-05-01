package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaNuevoItinerario extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton botonCrearItinerario;
    JButton goBackButton;

    // Etiquetas
    JLabel introduceData;
    JLabel codigoItinerario;
    JLabel nombreItinerario;
    JLabel descripcionItinerario;
    JLabel olimpiadaItinerario;

    // Campos de texto
    JTextField campoCodigoItinerario;
    JTextField campoNombreItinerario;
    JTextField campoDescripcionItinerario;
    JComboBox<String> campoOlimpiadaItinerario;

    // Paneles
    JPanel upperPanel;
    JPanel inputPanel;

    public VentanaNuevoItinerario(Administrador administrador) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Nuevo itinerario");
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        // Configuración del panel superior
        introduceData = new JLabel("Nuevo itinerario");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Configuración del panel de inputs
        codigoItinerario = new JLabel("Código (*)");
        codigoItinerario.setFont(fuenteBotonesEtiquetas);

        nombreItinerario = new JLabel("Nombre (*)");
        nombreItinerario.setFont(fuenteBotonesEtiquetas);

        descripcionItinerario = new JLabel("Descripción");
        descripcionItinerario.setFont(fuenteBotonesEtiquetas);

        olimpiadaItinerario = new JLabel("Olimpiada (*)");
        olimpiadaItinerario.setFont(fuenteBotonesEtiquetas);

        campoCodigoItinerario = new JTextField();
        campoCodigoItinerario.setFont(fuenteCampoTexto);

        campoNombreItinerario = new JTextField();
        campoNombreItinerario.setFont(fuenteCampoTexto);

        campoDescripcionItinerario = new JTextField();
        campoDescripcionItinerario.setFont(fuenteCampoTexto);

        ArrayList<String> codigosOlimpiadas = new ArrayList<>();
        campoOlimpiadaItinerario = new JComboBox<String>();
        campoOlimpiadaItinerario.setFont(fuenteCampoTexto);

        // Se obtienen los códigos de las olimpiadas existentes
        ResultSet olympCodes = administrador.selectCol("T_OLIMPIADAS", "CODIGO");

        // Se añaden los códigos al ArrayList
        while (olympCodes.next()) {
            String registro = olympCodes.getString("CODIGO");
            codigosOlimpiadas.add(registro);
        }

        // Utilizamos los códigos para meterlos en el combo box
        for (String codigosOlimpiada : codigosOlimpiadas) {
            campoOlimpiadaItinerario.addItem(codigosOlimpiada);
        }

        olympCodes.close();

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(borde);

        inputPanel.add(codigoItinerario);
        inputPanel.add(campoCodigoItinerario);
        inputPanel.add(nombreItinerario);
        inputPanel.add(campoNombreItinerario);
        inputPanel.add(descripcionItinerario);
        inputPanel.add(campoDescripcionItinerario);
        inputPanel.add(olimpiadaItinerario);
        inputPanel.add(campoOlimpiadaItinerario);

        // Configuración del panel de crear el itinerario
        botonCrearItinerario = new JButton("Crear itinerario");
        botonCrearItinerario.setFont(fuenteBotonesEtiquetas);
        botonCrearItinerario.setPreferredSize(new Dimension(150, 30));

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(botonCrearItinerario);

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        // Botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        // Botón de crear itinerario
        botonCrearItinerario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (campoCodigoItinerario.getText().matches("^\\s*$")
                        || campoNombreItinerario.getText().matches("^\\s*$")
                        || Objects.requireNonNull(campoOlimpiadaItinerario.getSelectedItem()).toString().matches("^\\s*$")) {
                    new CustomJOptionPane("Los campos Código, Nombre y Olimpiada son obligatorios");

                } else {
                    String code = campoCodigoItinerario.getText();
                    String name = campoNombreItinerario.getText();
                    String desc = campoDescripcionItinerario.getText();
                    String olymp = (String) campoOlimpiadaItinerario.getSelectedItem();

                    try {
                        if (administrador.createItinerario(code, name, desc, olymp) == 0) {
                            campoCodigoItinerario.setText("");
                            campoNombreItinerario.setText("");
                            campoDescripcionItinerario.setText("");
                            campoOlimpiadaItinerario.setSelectedItem(campoOlimpiadaItinerario.getItemAt(0));
                        }

                    } catch (JSchException | SQLException exc) {
                        new CustomJOptionPane("ERROR");
                    }
                }
            }
        });
    }

}
