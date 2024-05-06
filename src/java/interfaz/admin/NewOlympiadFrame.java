package interfaz.admin;

import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewOlympiadFrame extends JFrame implements Borders, Fonts, Icons {
    // Botones
    JButton createOlympButton;
    JButton goBackButton;
    
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

    public NewOlympiadFrame(Admin administrador) {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Nueva olimpiada");
        setIconImage(iconoVentana);
        setVisible(true);
        setLocationRelativeTo(null);

        // Configuración del panel superior
        introduceData = new JLabel("Nueva olimpiada");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Etiquetas del panel input
        olympCode = new JLabel("Código (*)");
        olympCode.setFont(fuenteBotonesEtiquetas);

        olympName = new JLabel("Nombre (*)");
        olympName.setFont(fuenteBotonesEtiquetas);

        olympDesc = new JLabel("Descripción");
        olympDesc.setFont(fuenteBotonesEtiquetas);

        olympYear = new JLabel("Año (*)");
        olympYear.setFont(fuenteBotonesEtiquetas);

        // Campos del panel de input
        olympCodeField = new JTextField();
        olympCodeField.setFont(fuenteCampoTexto);

        olympNameField = new JTextField();
        olympNameField.setFont(fuenteCampoTexto);

        olympDescField = new JTextField();
        olympDescField.setFont(fuenteCampoTexto);

        olympYearField = new JTextField();
        olympYearField.setFont(fuenteCampoTexto);

        // Botón de crear olimpiada
        createOlympButton = new JButton("Crear olimpiada");
        createOlympButton.setFont(fuenteBotonesEtiquetas);
        createOlympButton.setPreferredSize(new Dimension(150, 30));

        // Panel del botón de crear olimpiada
        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(createOlympButton);

        // Panel de input del usuario
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(borde);

        // Se añaden los elementos a los paneles
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(olympName);
        inputPanel.add(olympNameField);
        inputPanel.add(olympDesc);
        inputPanel.add(olympDescField);
        inputPanel.add(olympYear);
        inputPanel.add(olympYearField);

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        // Botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame(administrador);
                dispose();
            }
        });

        // Botón de crear olimpiada
        createOlympButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (olympCodeField.getText().matches("^\\s*$")
                        || olympNameField.getText().matches("^\\s*$")
                        || olympYearField.getText().matches("^\\s*$")) {
                    new ErrorJOptionPane("Los campos Código, Título y Año son obligatorios");

                } else {
                    String code = olympCodeField.getText();
                    String name = olympNameField.getText();
                    String desc = olympDescField.getText();
                    String year = olympYearField.getText();

                    if (year.matches("[0-9]*") && Integer.parseInt(year) > 2000 && Integer.parseInt(year) < 3000) {
                        if (administrador.createOlympiad(code, name, desc, Integer.parseInt(year)) == 0) {
                            olympCodeField.setText("");
                            olympNameField.setText("");
                            olympDescField.setText("");
                            olympYearField.setText("");
                        }

                    } else {
                        new ErrorJOptionPane("El campo Año debe ser un número entero y tener un valor válido");
                    }
                }
            }
        });
    }
}