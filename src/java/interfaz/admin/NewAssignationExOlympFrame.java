package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class NewAssignationExOlympFrame extends JFrame implements Borders, Fonts, Icons {
    // Botones
    JButton goBackButton;
    JButton assignExercise;
    
    // Etiquetas
    JLabel introduceData;
    JLabel exerCode;
    JLabel olympCode;
    JLabel itinerarioCode;
    
    // Combo boxes
    JComboBox<String> exerCodeField;
    JComboBox<String> olympCodeField;
    JComboBox<String> itinerarioCodeField;
    
    // Paneles
    JPanel inputPanel;
    JPanel upperPanel;

    public NewAssignationExOlympFrame(Admin administrador) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 290);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Nueva asignación");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icono de la ventana
        setIconImage(iconoVentana);

        introduceData = new JLabel("Nueva asignación");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        exerCode = new JLabel("Ejercicio (*)");
        exerCode.setFont(fuenteBotonesEtiquetas);

        olympCode = new JLabel("Olimpiada (*)");
        olympCode.setFont(fuenteBotonesEtiquetas);

        itinerarioCode = new JLabel("Itinerario (*)");
        itinerarioCode.setFont(fuenteBotonesEtiquetas);

        exerCodeField = new JComboBox<String>();
        exerCodeField.setFont(fuenteCampoTexto);

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        itinerarioCodeField = new JComboBox<>();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        exerCodeField = new JComboBox<>();
        exerCodeField.setFont(fuenteCampoTexto);

        ResultSet codes = administrador.selectCol("T_EJERCICIOS", "CODIGO");

        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            exerCodeField.addItem(registro);
        }

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        codes = administrador.selectCol("T_OLIMPIADAS", "CODIGO");

        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            olympCodeField.addItem(registro);
        }

        itinerarioCodeField = new JComboBox<>();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        String where = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
        codes = administrador.selectCol("T_ITINERARIOS", "CODIGO", where);

        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            itinerarioCodeField.addItem(registro);
        }

        codes.close();

        assignExercise = new JButton("Asignar");
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
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(itinerarioCode);
        inputPanel.add(itinerarioCodeField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame(administrador);
                dispose();
            }
        });

        olympCodeField.addActionListener(e -> {
            itinerarioCodeField.removeAllItems();

            try {
                String where1 = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
                ResultSet codes1 = administrador.selectCol("T_ITINERARIOS", "CODIGO", where1);

                while (codes1.next()) {
                    String registro = codes1.getString("CODIGO");
                    itinerarioCodeField.addItem(registro);

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
                String olympiad = (String) olympCodeField.getSelectedItem();
                String itinerario = (String) itinerarioCodeField.getSelectedItem();

                if (administrador.assignExerciseToOlympiad(exercise, olympiad, itinerario) == 0) {
                    exerCodeField.setSelectedItem(exerCodeField.getItemAt(0));
                    olympCodeField.setSelectedItem(olympCodeField.getItemAt(0));
                    itinerarioCodeField.removeAllItems();

                }

            }
        });

    }
}
