package gui.user_frames.admin.rubrics;

import com.jcraft.jsch.JSchException;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.custom_components.text_fields.CustomTextField;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class ModifyRubricFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
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
    CustomButton modificarRubrica; // Botón para crear rúbrica

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
    int nScales; // Variable de seguridad que indica el número de puntos que hay por el momento en la rúbrica
    Admin admin;
    String oldCode, oldName, oldDesc;
    int[] oldPoints;
    String[] oldTags;

    // Constructor
    public ModifyRubricFrame(Admin administrador, String code, String name, String desc, int[] points, String[] tags) {
        super("Modificar rúbrica");

        admin = administrador;
        oldCode = code;
        oldName = name;
        oldDesc = desc;
        oldPoints = points;
        oldTags = tags;
        nScales = points.length;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        pack();
        setLocationRelativeTo(null);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckRubricsFrame(administrador);
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
            dispose();
        });

        addNewPunctuationButton.addActionListener(e -> {
            if (nScales < 11) {
                JPanel newMark = new JPanel();
                newMark.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
                newMark.setLayout(new GridLayout(1, 2, 10, 0));

                // Campo de puntos
                CustomTextField newPunctuation = new CustomTextField("");
                newPunctuation.setPreferredSize(new Dimension(200, 30));
                newPunctuation.setFont(fuenteCampoTexto);

                // Campo de etiqueta de puntos
                CustomTextField newTag = new CustomTextField("");
                newTag.setPreferredSize(new Dimension(200, 30));
                newTag.setFont(fuenteCampoTexto);

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
                new ErrorJOptionPane("No es posible añadir más filas");
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
                new ErrorJOptionPane("No hay registros que borrar");
            }

        });

        modificarRubrica.addActionListener(e -> {
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
            System.out.println(Arrays.toString(scalePoints));
            System.out.println(Arrays.toString(scaleTags));

            if (exit == 1) {
                new ErrorJOptionPane("Los valores de rúbrica introducidos no son válidos." +
                        " Los puntos deben ser números enteros entre 0 y 10." +
                        " Las etiquetas de puntos deben ser cadenas de caracteres sin comas.");
            } else {
                // Se comprueba que el código no es vacío
                if (codeField.getText().matches("^\\s*$")) {
                    new ErrorJOptionPane("El campo Código es obligatorio.");

                } else {
                    String code1 = codeField.getText();
                    String name1 = nameField.getText();
                    String desc1 = descriptionField.getText();
                    String values = Arrays.toString(scalePoints);
                    String tags1 = Arrays.toString(scaleTags);

                    try {
                        if (administrador.modifyRubric(oldCode, code1, name1, desc1, values, tags1) == 0) {
                            new CheckRubricsFrame(administrador);
                            dispose();

                        }

                    } catch (SQLException | JSchException ex) {
                        new ErrorJOptionPane(ex.getMessage());

                    }
                }
            }
        });

        this.setVisible(true);
    }

    @Override
    protected JPanel createCenterPanel() {
        codeLabel = new CustomFieldLabel("Código (*)"); // Campo obligatorio
        codeField = new CustomTextField(oldCode);
        nameLabel = new CustomFieldLabel("Nombre");
        nameField = new CustomTextField(oldName);
        descriptionLabel = new CustomFieldLabel("Descripción");
        descriptionField = new CustomTextField(oldDesc);

        minPunctuation = new CustomFieldLabel("0");

        minPunctuationTagField = new CustomTextField(oldTags[0].substring(1));
        minPunctuationTagField.setPreferredSize(new Dimension(175, 30));
        minPunctuationTagField.setFont(fuenteCampoTexto);

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

        // Se añaden los paneles auxiliares necesarios al panel de valores personalizados
        for (int i = 1; i < oldPoints.length - 1; ++i) {
            // Panel auxiliar
            JPanel newMark = new JPanel();
            newMark.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
            newMark.setLayout(new GridLayout(1, 2, 10, 0));

            // Campo de puntos
            CustomTextField newPunctuation = new CustomTextField(String.valueOf(oldPoints[i]));
            newPunctuation.setPreferredSize(new Dimension(200, 30));

            // Campo de etiqueta de puntos
            CustomTextField newTag = new CustomTextField(oldTags[i].substring(1));
            newTag.setPreferredSize(new Dimension(200, 30));

            // Se añaden los campos nuevos al panel
            newMark.add(newPunctuation);
            newMark.add(newTag);

            // Se añade el panel al vector de paneles personalizados
            newFields.add(newMark);

            // Se añade el panel al panel de campos personalizados
            customValuesPanel.add(newMark);
        }

        // Valor máximo por defecto (Panel 6.2)
        maxPunctuation = new CustomFieldLabel("10");
        maxPunctuation.setPreferredSize(new Dimension(200, 30));
        maxPunctuationTagField = new CustomTextField(oldTags[oldPoints.length - 1].substring(1, oldTags[oldPoints.length - 1].length() - 1));
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
        modificarRubrica = new CustomButton("Modificar rúbrica");

        createRubricPanel = new JPanel();
        createRubricPanel.setLayout(new FlowLayout());
        createRubricPanel.setBorder(borde);

        createRubricPanel.add(modificarRubrica);

        return createRubricPanel;
    }
}
