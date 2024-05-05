package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Objects;

public class ModifyItineraryFrame extends JFrame implements Borders, Fonts, Icons {
    // Botones
    JButton botonModificarItinerario;
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

    // Otros
    String oldCode;

    public ModifyItineraryFrame(Admin administrador, String codigo, String titulo, String descripcion, String olimpiada) throws JSchException, SQLException {
        // Configuración de la ventana    
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Modificar itinerario");
        setIconImage(iconoVentana);
        setVisible(true);
        setLocationRelativeTo(null);

        oldCode = codigo;

        // Panel superior
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

        // Panel de inputs
        codigoItinerario = new JLabel("Código (*)");
        codigoItinerario.setFont(fuenteBotonesEtiquetas);
        
        nombreItinerario = new JLabel("Nombre (*)");
        nombreItinerario.setFont(fuenteBotonesEtiquetas);
        
        descripcionItinerario = new JLabel("Descripción");
        descripcionItinerario.setFont(fuenteBotonesEtiquetas);
        
        olimpiadaItinerario = new JLabel("Olimpiada (*)");
        olimpiadaItinerario.setFont(fuenteBotonesEtiquetas);

        campoCodigoItinerario = new JTextField(codigo);
        campoCodigoItinerario.setFont(fuenteCampoTexto);

        campoNombreItinerario = new JTextField(titulo);
        campoNombreItinerario.setFont(fuenteCampoTexto);

        campoDescripcionItinerario = new JTextField(descripcion);
        campoDescripcionItinerario.setFont(fuenteCampoTexto);

        campoOlimpiadaItinerario = new JComboBox<>();
        campoOlimpiadaItinerario.setFont(fuenteCampoTexto);
        
        ResultSet olympCodes = administrador.selectCol("T_OLIMPIADAS", "CODIGO");

        // Se añaden los códigos de olimpiada al combo box
        while (olympCodes.next()) {
            String registro = olympCodes.getString("CODIGO");
            campoOlimpiadaItinerario.addItem(registro);
        }
        
        olympCodes.close();

        campoOlimpiadaItinerario.setSelectedItem(olimpiada);

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
        

        // Panel de botón modificar
        botonModificarItinerario = new JButton("Modificar itinerario");
        botonModificarItinerario.setFont(fuenteBotonesEtiquetas);
        botonModificarItinerario.setPreferredSize(new Dimension(175, 30));

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(botonModificarItinerario);
        
        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        // Botón de volver
        goBackButton.addActionListener(e -> {
            try {
                new CheckItinerariesFrame(administrador);

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        // Botón modificar
        botonModificarItinerario.addActionListener(e -> {
            if (campoCodigoItinerario.getText().matches("^\\s*$")
                    || campoNombreItinerario.getText().matches("^\\s*$")
                    || Objects.requireNonNull(campoOlimpiadaItinerario.getSelectedItem()).toString().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Nombre y Olimpiada son obligatorios");

            } else {
                String code = campoCodigoItinerario.getText();
                String name = campoNombreItinerario.getText();
                String desc = campoDescripcionItinerario.getText();
                String olymp = (String) campoOlimpiadaItinerario.getSelectedItem();

                try {
                    if (administrador.modifyItinerario(oldCode, code, name, desc, olymp) == 0) {
                        new CheckItinerariesFrame(administrador);
                        dispose();

                    }

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());

                }

            }
        });
    }

}


