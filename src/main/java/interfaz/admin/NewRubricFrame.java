package interfaz.admin;

import interfaz.custom_components.*;
import interfaz.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Vector;

public class NewRubricFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Etiquetas
    CustomFieldLabel codeLabel; // Etiqueta de código de la rúbrica
    CustomFieldLabel nameLabel; // Etiqueta de nombre de la rúbrica
    CustomFieldLabel descriptionLabel; // Etiqueta de la descripción de la rúbrica
    CustomFieldLabel minPunctuation; // Etiqueta de puntuación mínima de la rúbrica
    CustomFieldLabel maxPunctuation; // Etiqueta de la puntuación máxima de la rúbrica
    
    // Campos de texto
    CustomTextField codeField; // Campo de código de la rúbrica
    CustomTextField nameField; // Campo de nombre de la rúbrica
    CustomTextField descriptionField; // Campo de descripción de la rúbrica
    CustomTextField minPunctuationTagField; // Campo de etiqueta de puntuación mínima de la rúbrica
    CustomTextField maxPunctuationTagField; // Campo de etiqueta de puntuación máxima de la rúbrica
    
    // Botones
    CustomButton addNewPunctuationButton; // Botón para añadir nuevo valor a la rúbrica
    CustomButton deletePunctuationButton; // Botón para eliminar valor de la rúbrica creado por el usuario
    CustomButton crearRubrica; // Botón para crear rúbrica
    
    // Paneles
    JPanel rubricDataPanel; // Panel de información de la rúbrica (Panel 2)
    JPanel basicInformationPanel; // Panel de código, nombre, descripción y valor mínimo de la rúbrica (Panel 3)
    JPanel customInformationPanel; // Panel que contendrá los valores personalizados del usuario, así como el valor máximo (Panel 6)
    JPanel customValuesPanel; // Panel que contentrá los valores personalizados (Panel 6.1)
    JPanel maxValuePanel; // Panel que contendrá el valor máximo de la rúbrica (Panel 6.2)
    JPanel createDeletePanel; // Panel que contendrá los botones para crear o eliminar valores personalizados de la rúbrica (Panel 4)
    JPanel createRubricPanel; // Panel para crear la rúbrica
    Vector<JPanel> newFields; // Vector de paneles que contendrán los valores personalizados de la rúbrica
    
    // Otros
    int nScales;
    Admin admin;

    // Constructor
    public NewRubricFrame(Admin administrador) {
        super("Nueva rúbrica");
        
        this.admin = administrador;
        
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        nScales = 2;

        pack();
        setLocationRelativeTo(null);

        addNewPunctuationButton.addActionListener(e -> {
            if (nScales < 11) {
                JPanel newMark = new JPanel();
                newMark.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                newMark.setLayout(new GridLayout(1, 2, 10, 0));

                // Campo de puntos
                CustomTextField newPunctuation = new CustomTextField("");
                newPunctuation.setPreferredSize(new Dimension(200, 30));

                // Campo de etiqueta de puntos
                CustomTextField newTag = new CustomTextField("");
                newTag.setPreferredSize(new Dimension(200, 30));

                // Se añaden los campos nuevos al panel
                newMark.add(newPunctuation);
                newMark.add(newTag);

                // Se añade el panel al vector de paneles personalizados
                newFields.add(newMark);

                // Se añade el panel al panel de campos personalizados
                customValuesPanel.add(newMark);

                // Valor de seguridad
                nScales += 1;

                // Esto es para que se actualice la ventana con los nuevos campos personalizados
                customValuesPanel.revalidate();
                customValuesPanel.repaint();
                pack();
                setLocationRelativeTo(null);

            } else {
                new ErrorJOptionPane("No es posible añadir más campos de puntuación a la rúbrica");

            }
        });

        deletePunctuationButton.addActionListener(e -> {
            Component[] components = customValuesPanel.getComponents();
            if (components.length > 0) {
                Component lastComponent = components[components.length - 1];
                customValuesPanel.remove(lastComponent);
                customValuesPanel.revalidate();
                customValuesPanel.repaint();
                pack();
                setLocationRelativeTo(null);
                nScales -= 1;

            } else {
                new ErrorJOptionPane("No hay puntuaciones que borrar");

            }
        });

        crearRubrica.addActionListener(e -> {
            // Se comprueba si los valores introducidos tienen el formato correcto
            int[] scalePoints = new int[nScales];      // Puntos de la rúbrica
            String[] scaleTags = new String[nScales];  // Etiquetas de la rúbrica

            scalePoints[0] = Integer.parseInt(minPunctuation.getText());
            scaleTags[0] = minPunctuationTagField.getText();

            Component[] registers = customValuesPanel.getComponents();
            int counter = 1;
            int exit = 0;

            // Se almacenan los valores de los paneles auxiliares
            for (Component register : registers) {
                JPanel dummy = (JPanel) register;
                Component[] textFields = dummy.getComponents();
                CustomTextField value = (CustomTextField) textFields[0];
                CustomTextField tag = (CustomTextField) textFields[1];

                if (value.getText().matches("^[0-9]$")) {
                    if (Integer.parseInt(value.getText()) > 0) {
                        scalePoints[counter] = Integer.parseInt(value.getText());

                    } else {
                        exit = 1;

                    }

                } else {
                    exit = 1;
                    break;

                }

                if (tag.getText().matches(".*[,].*")) {
                    exit = 1;
                    break;

                } else {
                    scaleTags[counter] = tag.getText();

                }

                ++counter;
            }

            scalePoints[scalePoints.length - 1] = Integer.parseInt(maxPunctuation.getText());
            scaleTags[scaleTags.length - 1] = maxPunctuationTagField.getText();

            if (scaleTags[0].matches(".*[,].*") || scaleTags[scaleTags.length - 1].matches(".*[,].*")) {
                exit = 1;

            }

            if (exit == 1) {
                new ErrorJOptionPane("""
                        Los valores de la rúbrica introducidos no son válidos.
                        Los puntos deben ser números enteros entre 0 y 10.
                        Las etiquetas de puntos deben ser cadenas de caracteres sin comas.""");
            } else {
                // Se comprueba que el código no es vacío
                if (codeField.getText().matches("^\\s*$")) {
                    new ErrorJOptionPane("El campo Código es obligatorio.");

                } else {
                    String code = codeField.getText();
                    String name = nameField.getText();
                    String desc = descriptionField.getText();
                    String values = Arrays.toString(scalePoints);
                    String tags = Arrays.toString(scaleTags);

                    if (administrador.createRubric(code, name, desc, values, tags) == 0) {
                        System.out.println("creada");
                        codeField.setText("");
                        nameField.setText("");
                        descriptionField.setText("");
                        minPunctuationTagField.setText("");
                        maxPunctuationTagField.setText("");
                        customValuesPanel.removeAll();
                        customValuesPanel.revalidate();
                        customValuesPanel.repaint();
                        pack();
                        setLocationRelativeTo(null);
                    }
                }
            }
        });
    }

    @Override
    protected JPanel createCenterPanel() {
        // Elementos de Panel 3
        codeLabel = new CustomFieldLabel("Código (*)"); // Campo obligatorio
        codeField = new CustomTextField("");
        nameLabel = new CustomFieldLabel("Nombre");
        nameField = new CustomTextField("");
        descriptionLabel = new CustomFieldLabel("Descripción");
        descriptionField = new CustomTextField("");

        minPunctuation = new CustomFieldLabel("0");
        minPunctuation.setPreferredSize(new Dimension(20, 30));
        minPunctuationTagField = new CustomTextField("");
        minPunctuationTagField.setPreferredSize(new Dimension(175, 30));

        // Configurar y añadir elementos a panel de información básica (Panel 3)
        basicInformationPanel = new JPanel();
        basicInformationPanel.setLayout(new GridLayout(4, 2, 10, 10));
        basicInformationPanel.setBorder(bordeRubricBasicInfo);
        basicInformationPanel.add(codeLabel);
        basicInformationPanel.add(codeField);
        basicInformationPanel.add(nameLabel);
        basicInformationPanel.add(nameField);
        basicInformationPanel.add(descriptionLabel);
        basicInformationPanel.add(descriptionField);
        basicInformationPanel.add(minPunctuation);
        basicInformationPanel.add(minPunctuationTagField);

        // Panel de campos personalizados (Panel 6.1)
        newFields = new Vector<>();

        // Valores intermedios introducidos por el usuario
        customValuesPanel = new JPanel();
        customValuesPanel.setLayout(new GridLayout(0, 1, 10, 5));
        customValuesPanel.setMinimumSize(new Dimension(500, 0));
        customValuesPanel.setMaximumSize(new Dimension(500, 300));

        // Valor máximo por defecto (Panel 6.2)
        maxPunctuation = new CustomFieldLabel("10");
        maxPunctuation.setPreferredSize(new Dimension(200, 30));
        maxPunctuationTagField = new CustomTextField("");
        maxPunctuationTagField.setPreferredSize(new Dimension(200, 30));

        maxValuePanel = new JPanel();
        maxValuePanel.setLayout(new GridLayout(1, 2, 10, 0));
        maxValuePanel.add(maxPunctuation);
        maxValuePanel.add(maxPunctuationTagField);

        // Botones para añadir y eliminar filas personalizadas
        addNewPunctuationButton = new CustomButton("+");
        addNewPunctuationButton.setPreferredSize(new Dimension(20, 30));

        deletePunctuationButton = new CustomButton("-");
        deletePunctuationButton.setPreferredSize(new Dimension(20, 30));

        createDeletePanel = new JPanel();
        createDeletePanel.setLayout(new GridLayout(1, 2, 10, 5));
        createDeletePanel.setBorder(borde);

        createDeletePanel.add(addNewPunctuationButton);
        createDeletePanel.add(deletePunctuationButton);

        // Panel 6
        customInformationPanel = new JPanel();
        customInformationPanel.setLayout(new BorderLayout(5, 5));
        customInformationPanel.setBorder(bordeRubricBasicInfo);

        customInformationPanel.add(customValuesPanel, BorderLayout.CENTER);
        customInformationPanel.add(maxValuePanel, BorderLayout.SOUTH);

        // Panel de información de la rúbrica en general (Panel 2)
        rubricDataPanel = new JPanel();
        rubricDataPanel.setLayout(new BorderLayout(5, 5));
        rubricDataPanel.add(basicInformationPanel, BorderLayout.NORTH);
        rubricDataPanel.add(customInformationPanel, BorderLayout.CENTER);
        rubricDataPanel.add(createDeletePanel, BorderLayout.SOUTH);

        return rubricDataPanel;
    }

    @Override
    protected JPanel createSouthPanel() {
        crearRubrica = new CustomButton("Crear rúbrica");
        crearRubrica.setFont(fuenteBotonesEtiquetas);

        createRubricPanel = new JPanel();
        createRubricPanel.setLayout(new FlowLayout());
        createRubricPanel.setBorder(borde);

        createRubricPanel.add(crearRubrica);

        return createRubricPanel;
    }
}
