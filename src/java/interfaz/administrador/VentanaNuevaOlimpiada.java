package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.Objects;

public class VentanaNuevaOlimpiada extends JFrame implements Bordes, Fuentes, Iconos {
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

    public VentanaNuevaOlimpiada(Administrador administrador) {
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
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        // Botón de crear olimpiada
        createOlympButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(olympCodeField.getText(), "")
                        || Objects.equals(olympNameField.getText(), "")
                        || Objects.equals(olympYearField.getText(), "")) {
                    new CustomJOptionPane("Los campos Código, Título y Año son obligatorios");

                } else {
                    String code = olympCodeField.getText();
                    String name = olympNameField.getText();
                    String desc = olympDescField.getText();
                    String year = olympYearField.getText();

                    if (year.matches("[0-9]*") && Integer.parseInt(year) > 2000 && Integer.parseInt(year) < 3000) {
                        try {
                            if (administrador.createOlympiad(code, name, desc, Integer.parseInt(year)) == 0) {
                                new VentanaAdministrador(administrador);
                                dispose();
                            }

                        } catch (JSchException | SQLException ex) {
                            throw new RuntimeException(ex);
                        }

                    } else {
                        new CustomJOptionPane("El campo Año debe ser un número entero y tener un valor válido");
                    }
                }
            }
        });
    }
}
