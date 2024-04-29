package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VentanaModificarAsignacionEjMonitor extends JFrame implements Bordes, Fuentes, Iconos {
    // Etiquetas
    JLabel monitorLabel;
    JLabel exerCodeLabel;
    JLabel tituloEjercicioLabel;
    JLabel tituloEjercicioField;
    JLabel titleLabel;
    JLabel olympLabel;
    JLabel itineraryLabel;
    JLabel itineraryField;

    // Comboboxes
    JComboBox<String> monitorComboBox;
    JComboBox<String> exerField;
    JComboBox<String> olympField;

    // Botones
    JButton okButton;
    JButton goBackButton;

    // Paneles
    JPanel upperPanel;
    JPanel createAssignationPanel;
    JPanel inputPanel;

    public VentanaModificarAsignacionEjMonitor(Administrador administrador, String name, String exer, String olymp, String itinerary) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Modificar asignación de ejercicio a monitor");
        setIconImage(iconoVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Panel superior
        titleLabel = new JLabel("Modificar asignación");
        titleLabel.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(titleLabel, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        // Input panel
        monitorLabel = new JLabel("Monitor (*)");
        monitorLabel.setFont(fuenteBotonesEtiquetas);
        monitorLabel.setPreferredSize(new Dimension(100, 30));

        exerCodeLabel = new JLabel("Ejercicio (*)");
        exerCodeLabel.setFont(fuenteBotonesEtiquetas);
        exerCodeLabel.setPreferredSize(new Dimension(100, 30));

        monitorComboBox = new JComboBox<>();
        monitorComboBox.setFont(fuenteCampoTexto);
        monitorComboBox.setPreferredSize(new Dimension(100, 30));

        exerField = new JComboBox<>();
        exerField.setFont(fuenteCampoTexto);
        exerField.setPreferredSize(new Dimension(100, 30));
        exerField.setSelectedItem(exer);

        tituloEjercicioLabel = new JLabel("Título del ejercicio");
        tituloEjercicioLabel.setFont(fuenteBotonesEtiquetas);
        tituloEjercicioLabel.setPreferredSize(new Dimension(100, 30));

        tituloEjercicioField = new JLabel();
        tituloEjercicioField.setFont(fuenteCampoTexto);
        tituloEjercicioField.setPreferredSize(new Dimension(100, 30));

        olympLabel = new JLabel("Olimpiada (*)");
        olympLabel.setFont(fuenteBotonesEtiquetas);
        olympLabel.setPreferredSize(new Dimension(100, 30));

        olympField = new JComboBox<>();
        olympField.setFont(fuenteCampoTexto);
        olympField.setPreferredSize(new Dimension(100, 30));

        itineraryLabel = new JLabel("Itinerario");
        itineraryLabel.setFont(fuenteBotonesEtiquetas);
        itineraryLabel.setPreferredSize(new Dimension(100, 30));

        itineraryField = new JLabel(itinerary);
        itineraryField.setFont(fuenteCampoTexto);
        itineraryField.setPreferredSize(new Dimension(100, 30));

        // Nombres de los monitores
        String whereClause = "WHERE TIPO='MONITOR';";
        ResultSet comboBoxesItems = administrador.selectCol("T_USUARIOS", "NOMBRE", whereClause);

        while (comboBoxesItems.next()) {
            String register = comboBoxesItems.getString("NOMBRE");
            monitorComboBox.addItem(register);
        }

        monitorComboBox.setSelectedItem(name);

        // Códigos de los ejercicios
        comboBoxesItems = administrador.selectCol("T_EJERCICIOS", "CODIGO", "");

        while (comboBoxesItems.next()) {
            String register = comboBoxesItems.getString("CODIGO");
            exerField.addItem(register);
        }

        exerField.setSelectedItem(exer);

        // Título del ejercicio
        comboBoxesItems = administrador.selectCol("T_EJERCICIOS", "TITULO", "WHERE CODIGO='" + exer + "'");

        if (comboBoxesItems.next()) {
            tituloEjercicioField.setText(comboBoxesItems.getString("TITULO"));

        }

        // Olimpiadas
        comboBoxesItems = administrador.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "OLIMPIADA", "WHERE EJERCICIO='" + exer + "'");

        while (comboBoxesItems.next()) {
            olympField.addItem(comboBoxesItems.getString("OLIMPIADA"));

        }

        olympField.setSelectedItem(olymp);

        comboBoxesItems.close();

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(5, 2, 5, 5));
        inputPanel.setBorder(borde);

        inputPanel.add(exerCodeLabel);
        inputPanel.add(exerField);

        inputPanel.add(tituloEjercicioLabel);
        inputPanel.add(tituloEjercicioField);

        inputPanel.add(olympLabel);
        inputPanel.add(olympField);

        inputPanel.add(itineraryLabel);
        inputPanel.add(itineraryField);

        inputPanel.add(monitorLabel);
        inputPanel.add(monitorComboBox);

        okButton = new JButton("Modificar asignación");
        okButton.setFont(fuenteBotonesEtiquetas);

        createAssignationPanel = new JPanel();
        createAssignationPanel.setLayout(new FlowLayout());
        createAssignationPanel.setBorder(borde);

        createAssignationPanel.add(okButton);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createAssignationPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            try {
                new VentanaConsultaAsignacionEjMonitor(administrador);
                dispose();
            } catch (JSchException | SQLException ex){
                new CustomJOptionPane("ERROR - " + ex.getMessage());
            }
        });

        exerField.addActionListener(e -> {
            olympField.removeAllItems();
            itineraryField.setText("");

            String whereClause1 = "WHERE CODIGO='" + exerField.getSelectedItem() + "'";
            ResultSet exerTitles = administrador.selectCol("T_EJERCICIOS", "TITULO", whereClause1);

            try {
                assert exerTitles != null;
                if (exerTitles.next()) {
                    tituloEjercicioField.setText(exerTitles.getString("TITULO"));

                }

            } catch (SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido obtener el título del ejercicio");
                new VentanaAdministrador(administrador);
                dispose();

            }

            // Olimpiadas a las que está asociado
            whereClause1 = "WHERE EJERCICIO='" + exerField.getSelectedItem() + "'";
            exerTitles = administrador.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "OLIMPIADA", whereClause1);

            try {
                assert exerTitles != null;
                while (exerTitles.next()) {
                    olympField.addItem(exerTitles.getString("OLIMPIADA"));

                }

            } catch (SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido obtener las olimpiadas");
                new VentanaAdministrador(administrador);
                dispose();

            }
        });

        olympField.addActionListener(e -> {
            String whereClause1 = "WHERE EJERCICIO='" + exerField.getSelectedItem() + "' AND OLIMPIADA='" + olympField.getSelectedItem() + "';";
            ResultSet exerTitles = administrador.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", whereClause1);

            try {
                assert exerTitles != null;
                if (exerTitles.next()) {
                    itineraryField.setText(exerTitles.getString("ITINERARIO"));

                }

            } catch (SQLException ex) {
                new CustomJOptionPane("ERROR - No se ha podido obtener el itinerario");
                new VentanaAdministrador(administrador);
                dispose();

            }
        });

        okButton.addActionListener(e -> {
            String monitor = (String) monitorComboBox.getSelectedItem();
            String exerCode = (String) exerField.getSelectedItem();
            String olympCode = (String) olympField.getSelectedItem();
            String itCode = itineraryField.getText();

            if (administrador.modifyAssignationExUser(name, exer, olymp, monitor, exerCode, olympCode, itCode) == 0) {
                try {
                    new VentanaConsultaAsignacionEjMonitor(administrador);
                    dispose();

                } catch (JSchException | SQLException ex) {
                    new CustomJOptionPane("ERROR - " + ex.getMessage());
                }

            }
        });

    }
}
