package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaModificarAsignacionEjIt extends JFrame implements Bordes, Fuentes, Iconos {
    // Botones
    JButton goBackButton;
    JButton assignExercise;

    // Etiquetas
    JLabel introduceData;
    JLabel exerCode;
    JLabel olympCode;
    JLabel oldItCode;

    // Combo boxes
    JComboBox<String> exerCodeField;
    JComboBox<String> olympCodeField;
    JComboBox<String> oldItCodeField;

    // Paneles
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaModificarAsignacionEjIt(Organizador organizador, String oldEx, String oldOlymp, String oldIt) throws JSchException, SQLException {
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

        oldItCode = new JLabel("Itinerario (*)");
        oldItCode.setFont(fuenteBotonesEtiquetas);

        exerCodeField = new JComboBox<String>();
        exerCodeField.setFont(fuenteCampoTexto);

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        oldItCodeField = new JComboBox<>();
        oldItCodeField.setFont(fuenteCampoTexto);

        exerCodeField = new JComboBox<>();
        exerCodeField.setFont(fuenteCampoTexto);

        ResultSet codes = organizador.selectCol("T_EJERCICIOS", "CODIGO", "");

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            exerCodeField.addItem(registro);
        }

        exerCodeField.setSelectedItem(oldEx);

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        codes = organizador.selectCol("T_OLIMPIADAS", "CODIGO", "");

        // Iterar sobre el resultado y añadir los registros al ArrayList
        while (codes.next()) {
            String registro = codes.getString("CODIGO");
            olympCodeField.addItem(registro);
        }

        olympCodeField.setSelectedItem(oldOlymp);

        oldItCodeField = new JComboBox<>();
        oldItCodeField.setFont(fuenteCampoTexto);

        for (int i = 0; i < organizador.getItinerarios().size(); ++i) {
            oldItCodeField.addItem(organizador.getItinerarios().get(i));
        }

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
        inputPanel.add(oldItCode);
        inputPanel.add(oldItCodeField);
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjIt(organizador);
                dispose();

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }
        });

        olympCodeField.addActionListener(e -> {
            oldItCodeField.removeAllItems();

            try {
                String where1 = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
                ResultSet codes1 = organizador.selectCol("T_ITINERARIOS", "CODIGO", where1);

                while (codes1.next()) {
                    String registro = codes1.getString("CODIGO");
                    oldItCodeField.addItem(registro);

                }

                codes1.close();

            } catch (RuntimeException | SQLException ex) {
                new CustomJOptionPane("ERROR - " + ex.getMessage());

            }
        });

        assignExercise.addActionListener(e -> {
            if (oldItCodeField.getItemCount() == 0) {
                new CustomJOptionPane("ERROR - Debe seleccionar un oldIt");

            } else {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = (String) olympCodeField.getSelectedItem();
                String itinerary = (String) oldItCodeField.getSelectedItem();

                try {
                    if (organizador.modifyAssignationExIt(oldEx, oldOlymp, oldIt, exercise, olympiad, itinerary) == 0) {
                        new VentanaConsultaAsignacionEjIt(organizador);
                        dispose();
                    }

                } catch (JSchException | SQLException ex) {
                    new CustomJOptionPane("ERROR - " + ex.getMessage());

                }
            }
        });

    }
}
