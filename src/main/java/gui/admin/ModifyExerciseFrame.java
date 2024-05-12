package gui.admin;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class ModifyExerciseFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton modifyExerButton;
    
    // Etiquetas
    CustomFieldLabel exerCode;
    CustomFieldLabel exerName;
    CustomFieldLabel exerDesc;
    CustomFieldLabel exerConcept;
    CustomFieldLabel exerResources;
    CustomFieldLabel exerType;
    CustomFieldLabel exerRubrica;
    
    // Campos de texto
    CustomTextField exerCodeField;
    CustomTextField exerNameField;
    CustomTextField exerDescField;
    
    // Combo boxes
    CustomComboBox exerConceptField;
    CustomComboBox exerResourcesField;
    CustomComboBox exerTypeField;
    CustomComboBox exerRubricaField;
    
    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    // Otros
    Admin admin;
    String oldCode, oldTitle, oldDes, oldConcept, oldResource, oldType;

    public ModifyExerciseFrame(Admin administrador, String codigo, String titulo, String desc, String concepto, String recurso, String tipo) throws JSchException, SQLException {
        super(475, "Modificar ejercicio");

        admin = administrador;
        oldCode = codigo;
        oldTitle = titulo;
        oldDes = desc;
        oldConcept = concepto;
        oldResource = recurso;
        oldType = tipo;
        
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckExercisesFrame(administrador);
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        // 
        modifyExerButton.addActionListener(e -> {
            if (exerCodeField.getText().matches("^\\s*$")
                    || exerNameField.getText().matches("^\\s*$")) {
                new ErrorJOptionPane("Los campos Código, Nombre, Concepto, Recursos, Tipo y Rúbrica son obligatorios");

            } else {
                String code = exerCodeField.getText();
                String name = exerNameField.getText();
                String desc1 = exerDescField.getText();
                String concept = (String) exerConceptField.getSelectedItem();
                String resources = (String) exerResourcesField.getSelectedItem();
                String type = (String) exerTypeField.getSelectedItem();
                String rubric = (String) exerRubricaField.getSelectedItem();

                try {
                    if (administrador.modifyExercise(oldCode, code, name, desc1, concept, resources, type, rubric) == 0) {
                        new CheckExercisesFrame(administrador);
                        dispose();

                    }

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());
                }
            }
        });

    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            exerCode = new CustomFieldLabel("Código (*)");
            exerName = new CustomFieldLabel("Nombre (*)");
            exerDesc = new CustomFieldLabel("Descripción");
            exerConcept = new CustomFieldLabel("Categoría (*)");
            exerResources = new CustomFieldLabel("Recursos (*)");
            exerType = new CustomFieldLabel("Tipo (*)");
            exerRubrica = new CustomFieldLabel("Rúbrica (*)");
            exerCodeField = new CustomTextField(oldCode);
            exerNameField = new CustomTextField(oldTitle);
            exerDescField = new CustomTextField(oldDes);

            ArrayList<String> exerConcepts = new ArrayList<>(new ArrayList<>(Arrays.asList("Abstracción", "Algoritmos",
                    "Bucles", "Condicionales", "Descomposición", "Funciones", "IA", "Reconocimiento de patrones",
                    "Secuencias", "Secuencias y bucles", "Variables", "Variables y funciones", "Otro")));

            exerConceptField = new CustomComboBox(exerConcepts);

            if (!oldConcept.equals("IA")) {
                String substring = oldConcept.substring(1);
                substring = substring.toLowerCase();
                oldConcept = oldConcept.charAt(0) + substring;
            }

            exerConceptField.setSelectedItem(oldConcept);

            ArrayList<String> exerResource = new ArrayList<>(new ArrayList<>(Arrays.asList("INICIAL", "INTERMEDIO")));
            exerResourcesField = new CustomComboBox(exerResource);
            exerResourcesField.setSelectedItem(oldResource);

            ArrayList<String> exerTypes = new ArrayList<>(new ArrayList<>(Arrays.asList("Desenchufada", "Enchufada")));
            exerTypeField = new CustomComboBox(exerTypes);

            String substring = oldType.substring(1);
            substring = substring.toLowerCase();
            oldType = oldType.charAt(0) + substring;
            exerTypeField.setSelectedItem(oldType);

            exerRubricaField = new CustomComboBox();

            ResultSet rubricCodes = admin.selectCol("T_RUBRICAS", "CODIGO");

            // Se añaden los registros al combo box
            while (rubricCodes.next()) {
                String registro = rubricCodes.getString("CODIGO");
                exerRubricaField.addItem(registro);

            }

            rubricCodes.close();

            inputPanel = new CustomPanel();
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

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        modifyExerButton = new CustomButton("Modificar ejercicio", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(modifyExerButton);

        return createButtonPanel;
    }
}
