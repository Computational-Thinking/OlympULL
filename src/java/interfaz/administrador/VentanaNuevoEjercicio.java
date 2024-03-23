package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaNuevoEjercicio extends JFrame {
    JLabel exerCode;
    JLabel exerName;
    JLabel exerCategory;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerMaterial;
    JLabel exerYear;
    JTextField exerCodeField;
    JTextField exerNameField;
    JComboBox exerCategoryField;
    JComboBox exerResourcesField;
    JComboBox exerTypeField;
    JComboBox exerMaterialField;
    JTextField exerYearField;
    JButton createExerButton;
    JPanel inputPanel;

    public VentanaNuevoEjercicio() {
        Administrador currentAdmin = new Administrador();
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Nuevo ejercicio olímpico");
        this.setVisible(true);

        exerCode = new JLabel("Código");
        exerName = new JLabel("Nombre");
        exerCategory = new JLabel("Categoría");
        exerResources = new JLabel("Recursos");
        exerType = new JLabel("Tipo");
        exerMaterial = new JLabel("Material");
        exerYear = new JLabel("Año");

        exerCodeField = new JTextField();
        exerNameField = new JTextField();
        String[] exerCategories = {"Bucles", "Condicionales", "Funciones", "IA", "Matrices", "Secuencias", "Secuencias y bubles", "Variables"};
        exerCategoryField = new JComboBox<>(exerCategories);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);

        String[] exerTypes = {"Enchufada", "Desenchufada"};
        exerTypeField = new JComboBox<>(exerTypes);

        String[] exerMaterials = {"Papel", "PC"};
        exerMaterialField = new JComboBox<>(exerMaterials);

        exerYearField = new JTextField("Año");

        createExerButton = new JButton("Crear ejercicio olímpico");

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));

        inputPanel.add(exerCode);
        inputPanel.add(exerCodeField);
        inputPanel.add(exerName);
        inputPanel.add(exerNameField);
        inputPanel.add(exerCategory);
        inputPanel.add(exerCategoryField);
        inputPanel.add(exerResources);
        inputPanel.add(exerResourcesField);
        inputPanel.add(exerType);
        inputPanel.add(exerTypeField);
        inputPanel.add(exerMaterial);
        inputPanel.add(exerMaterialField);
        inputPanel.add(exerYear);
        inputPanel.add(exerYearField);

        add(inputPanel);
        add(createExerButton);

        createExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = exerCodeField.getText();
                String name = exerNameField.getText();
                String category = String.valueOf(exerCategoryField.getSelectedItem());
                String resources = String.valueOf(exerResourcesField.getSelectedItem());
                String type = String.valueOf(exerTypeField.getSelectedItem());
                String material = String.valueOf(exerMaterialField.getSelectedItem());
                int year = Integer.parseInt(exerYearField.getText());
                try {
                    currentAdmin.createExercise(code, name, category, resources, type, material, year);
                    setVisible(false);
                    dispose();
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
