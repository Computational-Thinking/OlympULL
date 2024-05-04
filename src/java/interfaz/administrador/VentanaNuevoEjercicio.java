package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.CustomJOptionPane;
import interfaz.ErrorJOptionPane;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class VentanaNuevoEjercicio extends JFrame {
    // Botones
    JButton goBackButton;
    JButton createExerButton;

    // Etiquetas
    JLabel introduceData;
    JLabel exerCode;
    JLabel exerName;
    JLabel exerDesc;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerRubricaLabel;

    // Campos de texto
    JTextField exerCodeField;
    JTextField exerNameField;
    JTextField exerDescField;

    // Combo boxes
    JComboBox<String> exerConceptField;
    JComboBox<String> exerResourcesField;
    JComboBox<String> exerTypeField;
    JComboBox<String> exerRubrica;

    // Paneles
    JPanel createButtonPanel;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaNuevoEjercicio(Administrador administrador) throws JSchException, SQLException {
        setSize(500, 475);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Nuevo ejercicio olímpico");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        // Panel superior
        introduceData = new JLabel("Nuevo ejercicio");
        introduceData.setFont(fuenteNegrita1);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Panel de input
        exerCode = new JLabel("Código (*)");
        exerCode.setFont(fuenteNegrita3);

        exerName = new JLabel("Nombre (*)");
        exerName.setFont(fuenteNegrita3);

        exerDesc = new JLabel("Descripción");
        exerDesc.setFont(fuenteNegrita3);

        exerConcept = new JLabel("Categoría (*)");
        exerConcept.setFont(fuenteNegrita3);

        exerResources = new JLabel("Recursos (*)");
        exerResources.setFont(fuenteNegrita3);

        exerType = new JLabel("Tipo (*)");
        exerType.setFont(fuenteNegrita3);

        exerRubricaLabel = new JLabel("Rúbrica (*)");
        exerType.setFont(fuenteNegrita3);

        exerCodeField = new JTextField();
        exerCodeField.setFont(fuenteNegrita2);

        exerNameField = new JTextField();
        exerNameField.setFont(fuenteNegrita2);

        exerDescField = new JTextField();
        exerDescField.setFont(fuenteNegrita2);

        String[] exerConcepts = {"Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                                 "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables",
                                 "Variables y funciones"};

        exerConceptField = new JComboBox<>(exerConcepts);
        exerConceptField.setFont(fuenteNegrita2);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);
        exerResourcesField.setFont(fuenteNegrita2);

        String[] exerTypes = {"Desenchufada", "Enchufada"};
        exerTypeField = new JComboBox<>(exerTypes);
        exerTypeField.setFont(fuenteNegrita2);

        exerRubrica = new JComboBox<>();

        ResultSet codeCol = administrador.selectCol("T_RUBRICAS", "CODIGO");

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codeCol.next()) {
            String registro = codeCol.getString("CODIGO");
            exerRubrica.addItem(registro);
        }

        codeCol.close();

        exerRubrica.setFont(fuenteNegrita2);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10));

        inputPanel.add(exerCode);
        inputPanel.add(exerCodeField);
        inputPanel.add(exerName);
        inputPanel.add(exerNameField);
        inputPanel.add(exerDesc);
        inputPanel.add(exerDescField);
        inputPanel.add(exerConcept);
        inputPanel.add(exerConceptField);
        inputPanel.add(exerResources);
        inputPanel.add(exerResourcesField);
        inputPanel.add(exerType);
        inputPanel.add(exerTypeField);
        inputPanel.add(exerRubricaLabel);
        inputPanel.add(exerRubrica);

        // Panel de botón crear
        createExerButton = new JButton("Crear ejercicio");
        createExerButton.setPreferredSize(new Dimension(200, 30));
        createExerButton.setFont(fuenteNegrita3);

        createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(createExerButton);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        // Botón volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        // Botón crear ejercicio
        createExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exerCodeField.getText().matches("^\\s*$")
                        || exerNameField.getText().matches("^\\s*$")) {
                    new ErrorJOptionPane("Los campos Código, Nombre, Concepto, Recursos, Tipo y Rúbrica son obligatorios");

                } else {
                    String code = exerCodeField.getText();
                    String name = exerNameField.getText();
                    String desc = exerDescField.getText();
                    String concept = (String) exerConceptField.getSelectedItem();
                    String resources = (String) exerResourcesField.getSelectedItem();
                    String type = (String) exerTypeField.getSelectedItem();
                    String rubric = (String) exerRubrica.getSelectedItem();

                    if (administrador.createExercise(code, name, desc, concept, resources, type, rubric) == 0) {
                        exerCodeField.setText("");
                        exerNameField.setText("");
                        exerDescField.setText("");
                        exerConceptField.setSelectedItem(exerConceptField.getItemAt(0));
                        exerResourcesField.setSelectedItem(exerResourcesField.getItemAt(0));
                        exerTypeField.setSelectedItem(exerTypeField.getItemAt(0));
                        exerRubrica.setSelectedItem(exerRubrica.getItemAt(0));

                    }

                }
            }
        });

    }
}
