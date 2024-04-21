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

public class VentanaModificarEjercicio extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton modifyExerButton;
    JButton goBackButton;
    
    // Etiquetas
    JLabel introduceData;
    JLabel exerCode;
    JLabel exerName;
    JLabel exerDesc;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerRubrica;
    
    // Campos de texto
    JTextField exerCodeField;
    JTextField exerNameField;
    JTextField exerDescField;
    
    // Combo boxes
    JComboBox<String> exerConceptField;
    JComboBox<String> exerResourcesField;
    JComboBox<String> exerTypeField;
    JComboBox<String> exerRubricaField;
    
    // Paneles
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaModificarEjercicio(Administrador administrador, String codigo, String titulo, String desc, String concepto, String recurso, String tipo) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 475);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setTitle("Modificar ejercicio olímpico");
        setTitle("Modificar ejercicio olímpico");
        setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String oldCode = codigo;

        // Panel superior
        introduceData = new JLabel("Edición de ejercicio");
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
        exerCode = new JLabel("Código (*)");
        exerCode.setFont(fuenteBotonesEtiquetas);

        exerName = new JLabel("Nombre (*)");
        exerName.setFont(fuenteBotonesEtiquetas);

        exerDesc = new JLabel("Descripción");
        exerDesc.setFont(fuenteBotonesEtiquetas);

        exerConcept = new JLabel("Categoría (*)");
        exerConcept.setFont(fuenteBotonesEtiquetas);

        exerResources = new JLabel("Recursos (*)");
        exerResources.setFont(fuenteBotonesEtiquetas);

        exerType = new JLabel("Tipo (*)");
        exerType.setFont(fuenteBotonesEtiquetas);

        exerRubrica = new JLabel("Rúbrica (*)");
        exerRubrica.setFont(fuenteBotonesEtiquetas);

        exerCodeField = new JTextField(codigo);
        exerCodeField.setFont(fuenteCampoTexto);

        exerNameField = new JTextField(titulo);
        exerNameField.setFont(fuenteCampoTexto);

        exerDescField = new JTextField(desc);
        exerDescField.setFont(fuenteCampoTexto);

        String[] exerConcepts = {"Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables", "Variables y funciones", "Otro"};

        exerConceptField = new JComboBox<>(exerConcepts);
        exerConceptField.setFont(fuenteCampoTexto);
        if (!concepto.equals("IA")) {
            String substring = concepto.substring(1);
            substring = substring.toLowerCase();
            concepto = concepto.charAt(0) + substring;
        }
        exerConceptField.setSelectedItem(concepto);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);
        exerResourcesField.setFont(fuenteCampoTexto);
        exerResourcesField.setSelectedItem(recurso);

        String[] exerTypes = {"Desenchufada", "Enchufada"};
        exerTypeField = new JComboBox<>(exerTypes);
        exerTypeField.setFont(fuenteCampoTexto);

        String substring = tipo.substring(1);
        substring = substring.toLowerCase();
        tipo = tipo.charAt(0) + substring;
        exerTypeField.setSelectedItem(tipo);

        exerRubricaField = new JComboBox<>();
        exerRubricaField.setFont(fuenteCampoTexto);

        ResultSet rubricCodes = administrador.selectCol("T_RUBRICAS", "CODIGO");

        // Se añaden los registros al combo box
        while (rubricCodes.next()) {
            String registro = rubricCodes.getString("CODIGO");
            exerRubricaField.addItem(registro);

        }

        rubricCodes.close();

        // Panel de botón modificar
        modifyExerButton = new JButton("Modificar ejercicio olímpico");
        modifyExerButton.setPreferredSize(new Dimension(250, 30));
        modifyExerButton.setFont(fuenteBotonesEtiquetas);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(modifyExerButton);

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
        inputPanel.add(exerRubrica);
        inputPanel.add(exerRubricaField);

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        // Botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new VentanaConsultaEjercicios(administrador);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        // 
        modifyExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (exerCodeField.getText().matches("^\\s*$")
                        || exerNameField.getText().matches("^\\s*$")) {
                    new CustomJOptionPane("Los campos Código, Nombre, Concepto, Recursos, Tipo y Rúbrica son obligatorios");

                } else {
                    String code = exerCodeField.getText();
                    String name = exerNameField.getText();
                    String desc = exerDescField.getText();
                    String concept = (String) exerConceptField.getSelectedItem();
                    String resources = (String) exerResourcesField.getSelectedItem();
                    String type = (String) exerTypeField.getSelectedItem();
                    String rubric = (String) exerRubricaField.getSelectedItem();

                    try {
                        administrador.modifyExercise(oldCode, code, name, desc, concept, resources, type, rubric);
                        new VentanaConsultaEjercicios(administrador);
                        dispose();

                    } catch (JSchException | SQLException ex) {
                        new CustomJOptionPane("ERROR");
                    }
                }
            }
        });

    }
}
