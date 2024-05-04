package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.*;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class VentanaModificarAsignacionEjOlimp extends JFrame implements Bordes, Fuentes, Iconos {
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

    public VentanaModificarAsignacionEjOlimp(Administrador administrador, String oldEx, String oldOlymp, String oldIt) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 290);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Modificar asignación");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        introduceData = new JLabel("Modificar asignación");
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
        
         // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            exerCodeField.addItem(registro);
        }

        exerCodeField.setSelectedItem(oldEx);

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        codes = administrador.selectCol("T_OLIMPIADAS", "CODIGO");

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            olympCodeField.addItem(registro);
        }
        
        olympCodeField.setSelectedItem(oldOlymp);
        
        itinerarioCodeField = new JComboBox<>();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        String where = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
        codes = administrador.selectCol("T_ITINERARIOS", "CODIGO", where);

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            itinerarioCodeField.addItem(registro);
        }

        itinerarioCodeField.setSelectedItem(oldIt);
        
        codes.close();

        assignExercise = new JButton("Modificar asignación");
        assignExercise.setPreferredSize(new Dimension(175, 30));
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

        goBackButton.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjOlimp(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

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
                String itinerary = (String) itinerarioCodeField.getSelectedItem();

                try {
                    if (administrador.modifyAssignationExOlymp(oldEx, oldOlymp, oldIt, exercise, olympiad, itinerary) == 0) {
                        new VentanaConsultaAsignacionEjOlimp(administrador);
                        dispose();
                    }

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());

                }
            }
        });

    }
}