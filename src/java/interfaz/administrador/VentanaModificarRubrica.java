package interfaz.administrador;

import com.jcraft.jsch.JSchException;
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
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Vector;

public class VentanaModificarRubrica extends JFrame implements Bordes, Fuentes, Iconos {
    // Etiquetas
    JLabel title; // Título de la ventana
    JLabel codeLabel; // Etiqueta de código de la rúbrica
    JLabel nameLabel; // Etiqueta de nombre de la rúbrica
    JLabel descriptionLabel; // Etiqueta de la descripción de la rúbrica
    JLabel minPunctuation; // Etiqueta de puntuación mínima de la rúbrica
    JLabel maxPunctuation; // Etiqueta de la puntuación máxima de la rúbrica

    // Campos de texto
    JTextField codeField; // Campo de código de la rúbrica
    JTextField nameField; // Campo de nombre de la rúbrica
    JTextField descriptionField; // Campo de descripción de la rúbrica
    JTextField minPunctuationTagField; // Campo de etiqueta de puntuación mínima de la rúbrica
    JTextField maxPunctuationTagField; // Campo de etiqueta de puntuación máxima de la rúbrica

    // Botones
    JButton goBackButton; // Botón para volver atrás
    JButton addNewPunctuationButton; // Botón para añadir nuevo valor a la rúbrica
    JButton deletePunctuationButton; // Botón para eliminar valor de la rúbrica creado por el usuario
    JButton modificarRubrica; // Botón para crear rúbrica

    // Paneles
    JPanel upperPanel; // Panel superior de título y botón de volver atrás (Panel 1)
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

    // Constructor
    public VentanaModificarRubrica(Administrador administrador, String code, String name, String desc, int[] points, String[] tags) {
        // Configuración de la ventana
        // setSize(500, 400);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Modificar rúbrica");
        setIconImage(iconoVentana);

        String oldCode = code;

        nScales = points.length;

        // Botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Etiqueta de título
        title = new JLabel("Modificar rúbrica " + code);
        title.setFont(fuenteTitulo);

        // Configurar y añadir elementos a panel superior (Panel 1)
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(title, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        // Elementos de Panel 3
        codeLabel = new JLabel("Código (*)"); // Campo obligatorio
        codeField = new JTextField(code);

        codeLabel.setFont(fuenteBotonesEtiquetas);
        codeField.setFont(fuenteCampoTexto);

        nameLabel = new JLabel("Nombre");
        nameField = new JTextField(name);

        nameLabel.setFont(fuenteBotonesEtiquetas);
        nameField.setFont(fuenteCampoTexto);

        descriptionLabel = new JLabel("Descripción");
        descriptionField = new JTextField(desc);

        descriptionLabel.setFont(fuenteBotonesEtiquetas);
        descriptionField.setFont(fuenteCampoTexto);

        minPunctuation = new JLabel("0");
        minPunctuation.setPreferredSize(new Dimension(20, 30));
        minPunctuation.setFont(fuenteBotonesEtiquetas);

        minPunctuationTagField = new JTextField(tags[0].substring(1));
        minPunctuationTagField.setPreferredSize(new Dimension(175, 30));
        minPunctuationTagField.setFont(fuenteCampoTexto);

        // Configurar y añadir elementos a panel de información básica (Panel 3)
        basicInformationPanel = new JPanel();
        basicInformationPanel.setLayout(new GridLayout(4, 2, 5, 5));
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
        customValuesPanel.setLayout(new GridLayout(0, 1, 5, 5));
        customValuesPanel.setMinimumSize(new Dimension(500, 0));
        customValuesPanel.setMaximumSize(new Dimension(500, 300));

        // Se añaden los paneles auxiliares necesarios al panel de valores personalizados
        for (int i = 1; i < points.length - 1; ++i) {
            // Panel auxiliar
            JPanel newMark = new JPanel();
            newMark.setLayout(new GridLayout(1, 2, 5, 5));

            // Campo de puntos
            JTextField newPunctuation = new JTextField(String.valueOf(points[i]));
            newPunctuation.setPreferredSize(new Dimension(200, 30));
            newPunctuation.setFont(fuenteCampoTexto);

            // Campo de etiqueta de puntos
            JTextField newTag = new JTextField(tags[i].substring(1));
            newTag.setPreferredSize(new Dimension(200, 30));
            newTag.setFont(fuenteCampoTexto);

            // Se añaden los campos nuevos al panel
            newMark.add(newPunctuation);
            newMark.add(newTag);

            // Se añade el panel al vector de paneles personalizados
            newFields.add(newMark);

            // Se añade el panel al panel de campos personalizados
            customValuesPanel.add(newMark);
        }

        // Valor máximo por defecto (Panel 6.2)
        maxPunctuation = new JLabel("10");
        maxPunctuation.setFont(fuenteBotonesEtiquetas);
        maxPunctuation.setPreferredSize(new Dimension(200, 30));
        maxPunctuationTagField = new JTextField(tags[points.length - 1].substring(1, tags[points.length - 1].length() - 1));
        maxPunctuationTagField.setFont(fuenteCampoTexto);
        maxPunctuationTagField.setPreferredSize(new Dimension(200, 30));

        maxValuePanel = new JPanel();
        maxValuePanel.setLayout(new GridLayout(1, 2, 5, 5));

        maxValuePanel.add(maxPunctuation);
        maxValuePanel.add(maxPunctuationTagField);

        // Botones para añadir y eliminar filas personalizadas
        addNewPunctuationButton = new JButton("+");
        addNewPunctuationButton.setFont(fuenteBotonesEtiquetas);
        addNewPunctuationButton.setPreferredSize(new Dimension(20, 30));

        deletePunctuationButton = new JButton("-");
        deletePunctuationButton.setFont(fuenteBotonesEtiquetas);
        deletePunctuationButton.setPreferredSize(new Dimension(20, 30));

        createDeletePanel = new JPanel();
        createDeletePanel.setLayout(new GridLayout(1, 2, 5, 5));
        createDeletePanel.setBorder(borde);

        createDeletePanel.add(addNewPunctuationButton);
        createDeletePanel.add(deletePunctuationButton);

        // Panel 6
        customInformationPanel = new JPanel();
        customInformationPanel.setLayout(new BorderLayout(5, 5));
        customInformationPanel.setBorder(bordeRubricMaxField);

        customInformationPanel.add(customValuesPanel, BorderLayout.CENTER);
        customInformationPanel.add(maxValuePanel, BorderLayout.SOUTH);

        // Panel de información de la rúbrica en general (Panel 2)
        rubricDataPanel = new JPanel();
        rubricDataPanel.setLayout(new BorderLayout(5, 5));
        rubricDataPanel.add(basicInformationPanel, BorderLayout.NORTH);
        rubricDataPanel.add(customInformationPanel, BorderLayout.CENTER);

        // Panel de botón para modificar rúbrica (Panel 5)
        // Elementos
        modificarRubrica = new JButton("Modificar rúbrica");
        modificarRubrica.setFont(fuenteBotonesEtiquetas);

        createRubricPanel = new JPanel();
        createRubricPanel.setLayout(new FlowLayout());
        createRubricPanel.setBorder(borde);

        createRubricPanel.add(modificarRubrica);

        rubricDataPanel.add(createDeletePanel, BorderLayout.SOUTH);

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(rubricDataPanel, BorderLayout.CENTER);
        add(createRubricPanel, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new VentanaConsultaRubricas(administrador);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        addNewPunctuationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nScales < 11) {
                    JPanel newMark = new JPanel();
                    newMark.setLayout(new GridLayout(1, 2, 5, 5));

                    // Campo de puntos
                    JTextField newPunctuation = new JTextField();
                    newPunctuation.setPreferredSize(new Dimension(200, 30));
                    newPunctuation.setFont(fuenteCampoTexto);

                    // Campo de etiqueta de puntos
                    JTextField newTag = new JTextField();
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
                    JOptionPane.showMessageDialog(null, "No es posible añadir más filas");
                }

            }
        });

        deletePunctuationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    JOptionPane.showMessageDialog(null, "No hay registros que borrar");
                }

            }
        });

        modificarRubrica.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                    JTextField value = (JTextField) textFields[0];
                    JTextField tag = (JTextField) textFields[1];

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
                        scaleTags[counter] = value.getText();

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
                    new CustomJOptionPane("Los valores de la rúbrica introducidos no son válidos." +
                            " Los puntos deben ser números enteros entre 0 y 10." +
                            " Las etiquetas de puntos deben ser cadenas de caracteres sin comas.");
                } else {
                    // Se comprueba que el código no es vacío
                    if (codeField.getText().matches("^\\s*$")) {
                        new CustomJOptionPane("El campo Código es obligatorio.");

                    } else {
                        String code = codeField.getText();
                        String name = nameField.getText();
                        String desc = descriptionField.getText();
                        String values = Arrays.toString(scalePoints);
                        String tags = Arrays.toString(scaleTags);

                        try {
                            if (administrador.modifyRubric(oldCode, code, name, desc, values, tags) == 0) {
                                new CustomJOptionPane("Se ha modificado la rúbrica");
                                new VentanaConsultaRubricas(administrador);
                                dispose();

                            }

                        } catch (SQLException | JSchException ex) {
                            new CustomJOptionPane("ERROR");

                        }
                    }
                }
            }
        });

        this.setVisible(true);
    }
}
