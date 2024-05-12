package gui.admin;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NewExerciseFrame extends NewRegistrationFrameTemplate {
    // Botones
    CustomButton createExerButton;

    // Etiquetas
    CustomFieldLabel exerCode;
    CustomFieldLabel exerName;
    CustomFieldLabel exerDesc;
    CustomFieldLabel exerConcept;
    CustomFieldLabel exerResources;
    CustomFieldLabel exerType;
    CustomFieldLabel exerRubricaLabel;

    // Campos de texto
    CustomTextField exerCodeField;
    CustomTextField exerNameField;
    CustomTextField exerDescField;

    // Combo boxes
    CustomComboBox exerConceptField;
    CustomComboBox exerResourcesField;
    CustomComboBox exerTypeField;
    CustomComboBox exerRubrica;

    // Paneles
    CustomPanel createButtonPanel;
    CustomPanel inputPanel;
    
    // Otros
    Admin admin;

    public NewExerciseFrame(Admin administrador) throws JSchException, SQLException {
        super(445, "Nuevo ejercicio");

        this.admin = administrador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        // Botón volver
        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        // Botón crear ejercicio
        createExerButton.addActionListener(e -> {
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
            exerRubricaLabel = new CustomFieldLabel("Rúbrica (*)");
            exerCodeField = new CustomTextField("");
            exerNameField = new CustomTextField("");
            exerDescField = new CustomTextField("");

            ArrayList<String> exerConcepts = new ArrayList<>(Arrays.asList("Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                    "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables",
                    "Variables y funciones"));

            exerConceptField = new CustomComboBox(exerConcepts);

            ArrayList<String> exerResource = new ArrayList<>(Arrays.asList("INICIAL", "INTERMEDIO"));
            exerResourcesField = new CustomComboBox(exerResource);

            ArrayList<String> exerTypes = new ArrayList<>(Arrays.asList("Desenchufada", "Enchufada"));
            exerTypeField = new CustomComboBox(exerTypes);

            exerRubrica = new CustomComboBox();

            ResultSet codeCol = admin.selectCol("T_RUBRICAS", "CODIGO");

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (codeCol.next()) {
                String registro = codeCol.getString("CODIGO");
                exerRubrica.addItem(registro);
            }

            codeCol.close();

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
            inputPanel.add(exerRubricaLabel);
            inputPanel.add(exerRubrica);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }
        
        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        createExerButton = new CustomButton("Crear ejercicio", 200, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(createExerButton);

        return createButtonPanel;
    }
}
