package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class VentanaEditarEjercicio extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel exerCode;
    JLabel exerName;
    JLabel exerDesc;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JTextField exerCodeField;
    JTextField exerNameField;
    JTextField exerDescField;
    JComboBox<String> exerConceptField;
    JComboBox<String> exerResourcesField;
    JComboBox<String> exerTypeField;
    JButton editExerButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaEditarEjercicio(Administrador administrador, String codigo, String titulo, String desc, String concepto, String recurso, String tipo) {
        setSize(500, 425);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Editar ejercicio olímpico");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String oldCode = codigo;

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Edición de ejercicio");
        introduceData.setFont(fuenteNegrita1);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

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

        exerCodeField = new JTextField(codigo);
        exerCodeField.setFont(fuenteNegrita2);

        exerNameField = new JTextField(titulo);
        exerNameField.setFont(fuenteNegrita2);

        exerDescField = new JTextField(desc);
        exerDescField.setFont(fuenteNegrita2);

        String[] exerConcepts = {"Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables", "Variables y funciones", "Otro"};

        exerConceptField = new JComboBox<>(exerConcepts);
        exerConceptField.setFont(fuenteNegrita2);
        if (!concepto.equals("IA")) {
            String substring = concepto.substring(1);
            substring = substring.toLowerCase();
            concepto = concepto.charAt(0) + substring;
        }
        exerConceptField.setSelectedItem(concepto);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);
        exerResourcesField.setFont(fuenteNegrita2);
        exerResourcesField.setSelectedItem(recurso);

        String[] exerTypes = {"Desenchufada", "Enchufada"};
        exerTypeField = new JComboBox<>(exerTypes);
        exerTypeField.setFont(fuenteNegrita2);

        String substring = tipo.substring(1);
        substring = substring.toLowerCase();
        tipo = tipo.charAt(0) + substring;

        exerTypeField.setSelectedItem(tipo);

        editExerButton = new JButton("Modificar ejercicio olímpico");
        editExerButton.setPreferredSize(new Dimension(200, 30));
        editExerButton.setFont(fuenteNegrita3);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(editExerButton);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(6, 2, 10, 10));

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

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VentanaConsultaEjercicios ventana = new VentanaConsultaEjercicios(administrador);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        editExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = exerCodeField.getText();
                String name = exerNameField.getText();
                String desc = exerDescField.getText();
                String concept = String.valueOf(exerConceptField.getSelectedItem());
                String resources = String.valueOf(exerResourcesField.getSelectedItem());
                String type = String.valueOf(exerTypeField.getSelectedItem());

                try {
                    administrador.modifyExercise(oldCode, code, name, desc, concept, resources, type);
                    VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                    dispose();
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
}
