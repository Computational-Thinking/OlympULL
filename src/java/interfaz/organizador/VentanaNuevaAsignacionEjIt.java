package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaNuevaAsignacionEjIt extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    CustomButton goBackButton;
    CustomButton assignExercise;

    // Etiquetas
    CustomLabel title;
    CustomLabel exerCode;
    CustomLabel olympCode;
    CustomLabel olympCodeField;
    CustomLabel itinerarioCode;

    // Combo boxes
    CustomComboBox exerCodeField;
    CustomComboBox itinerarioCodeField;

    // Paneles
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaNuevaAsignacionEjIt(Organizador organizador) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 290);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Nueva asignación");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icono de la ventana
        setIconImage(iconoVentana);

        title = new CustomTitleLabel("Nueva asignación");

        goBackButton = new CustomButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(title, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        exerCode = new CustomFieldLabel("Ejercicio (*)");

        olympCode = new CustomFieldLabel("Olimpiada (*)");

        itinerarioCode = new CustomFieldLabel("Itinerario (*)");

        exerCodeField = new CustomComboBox();

        olympCodeField = new CustomFieldLabel();

        itinerarioCodeField = new CustomComboBox();

        exerCodeField = new CustomComboBox();

        ResultSet codes = organizador.selectCol("T_EJERCICIOS", "CODIGO");

        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            exerCodeField.addItem(registro);
        }

        itinerarioCodeField = new CustomComboBox();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        for (int i = 0; i < organizador.getItinerarios().size(); ++i) {
            itinerarioCodeField.addItem(organizador.getItinerarios().get(i));
        }

        codes.close();

        assignExercise = new CustomButton("Asignar");
        assignExercise.setPreferredSize(new Dimension(100, 30));
        assignExercise.setFont(fuenteBotonesEtiquetas);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(assignExercise);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(exerCode);
        inputPanel.add(exerCodeField);
        inputPanel.add(itinerarioCode);
        inputPanel.add(itinerarioCodeField);
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new VentanaOrganizador(organizador);
            dispose();
        });

        itinerarioCodeField.addActionListener(e -> {
            try {
                String where = "WHERE CODIGO='" + itinerarioCodeField.getSelectedItem() + "'";
                ResultSet codes1 = organizador.selectCol("T_ITINERARIOS", "OLIMPIADA", where);

                if (codes1.next()) {
                    olympCodeField.setText(codes1.getString("OLIMPIADA"));

                }

                codes1.close();

            } catch (RuntimeException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }
        });

        assignExercise.addActionListener(e -> {
            if (itinerarioCodeField.getItemCount() == 0) {
                new ErrorJOptionPane("Debe seleccionar un itinerario");

            } else {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = olympCodeField.getText();
                String itinerario = (String) itinerarioCodeField.getSelectedItem();

                if (organizador.createAssignationExIt(exercise, olympiad, itinerario) == 0) {
                    exerCodeField.setSelectedItem(exerCodeField.getItemAt(0));
                    itinerarioCodeField.setSelectedItem(itinerarioCodeField.getItemAt(0));
                    olympCodeField.setText("");

                }

            }
        });

    }
}
