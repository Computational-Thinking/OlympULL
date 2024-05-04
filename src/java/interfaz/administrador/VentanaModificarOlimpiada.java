package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Objects;

public class VentanaModificarOlimpiada extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton goBackButton;
    JButton modifyOlympButton;
    
    // Etiquetas
    JLabel introduceData;
    JLabel olympCode;
    JLabel olympName;
    JLabel olympDesc;
    JLabel olympYear;
    
    // Campos de texto
    JTextField olympCodeField;
    JTextField olympNameField;
    JTextField olympDescField;
    JTextField olympYearField;
    
    // Paneles
    JPanel upperPanel;
    JPanel inputPanel;
    
    // Otros
    String oldCode;

    public VentanaModificarOlimpiada(Administrador administrador, String codigo, String titulo, String descripcion, String year) {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Modificar olimpiada");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        oldCode = codigo;

        // Panel superior
        introduceData = new JLabel("Modificar olimpiada");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Elementos del panel de inputs
        olympCode = new JLabel("Código (*)");
        olympCode.setFont(fuenteBotonesEtiquetas);
        
        olympName = new JLabel("Nombre (*)");
        olympName.setFont(fuenteBotonesEtiquetas);
        
        olympDesc = new JLabel("Descripción");
        olympDesc.setFont(fuenteBotonesEtiquetas);
        
        olympYear = new JLabel("Año (*)");
        olympYear.setFont(fuenteBotonesEtiquetas);

        olympCodeField = new JTextField(codigo);
        olympCodeField.setFont(fuenteCampoTexto);
        
        olympNameField = new JTextField(titulo);
        olympNameField.setFont(fuenteCampoTexto);
        
        olympDescField = new JTextField(descripcion);
        olympDescField.setFont(fuenteCampoTexto);
        
        olympYearField = new JTextField(year);
        olympYearField.setFont(fuenteCampoTexto);
        
        modifyOlympButton = new JButton("Modificar olimpiada");
        modifyOlympButton.setFont(fuenteBotonesEtiquetas);
        modifyOlympButton.setPreferredSize(new Dimension(175, 30));

        // Botón de modificar la olimpiada
        JPanel modifyButtonPanel = new JPanel();
        modifyButtonPanel.setBorder(borde);
        modifyButtonPanel.add(modifyOlympButton);

        // Se añaden los elementos del panel de inputs
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(borde);

        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(olympName);
        inputPanel.add(olympNameField);
        inputPanel.add(olympDesc);
        inputPanel.add(olympDescField);
        inputPanel.add(olympYear);
        inputPanel.add(olympYearField);

        // Se añaden los elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(modifyButtonPanel, BorderLayout.SOUTH);

        // Botón de volver
        goBackButton.addActionListener(e -> {
            try {
                new VentanaConsultaOlimpiadas(administrador);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }

            dispose();
        });

        // Botón de modificar
        modifyOlympButton.addActionListener(e -> {
            if (olympCodeField.getText().matches("^\\s*$")
                    || olympNameField.getText().matches("^\\s*$")
                    || olympYearField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Título y Año son obligatorios");

            } else {
                String code = olympCodeField.getText();
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                String year1 = olympYearField.getText();

                if (year1.matches("[0-9]*") && Integer.parseInt(year1) > 2000 && Integer.parseInt(year1) < 3000) {
                    try {
                        if (administrador.modifyOlympiad(oldCode, code, name, desc, Integer.parseInt(year1)) == 0) {
                            new VentanaConsultaOlimpiadas(administrador);
                            dispose();

                        }

                    } catch (JSchException | SQLException ex) {
                        new ErrorJOptionPane(ex.getMessage());

                    }

                } else {
                    new ErrorJOptionPane("El campo Año debe ser un número entero y tener un valor válido");

                }
            }
        });
    }
}
