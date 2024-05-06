package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewAssignationItOrgFrame extends JFrame implements Borders, Fonts, Icons {
    // Etiquetas
    JLabel titleLabel;
    JLabel organizerLabel;
    JLabel itineraryLabel;
    JLabel olympLabel;
    JLabel olympField;

    // Comboboxes
    JComboBox<String> organizerComboBox;
    JComboBox<String> itineraryField;

    // Botones
    JButton assignButton;
    JButton goBackButton;

    // Paneles
    JPanel upperPanel;
    JPanel inputPanel;
    JPanel createAssignationPanel;

    public NewAssignationItOrgFrame(Admin administrador) throws JSchException, SQLException {
        // Configuración de la ventana
        setSize(500, 265);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Asignar itinerario a organizador");
        setIconImage(iconoVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Panel superior
        titleLabel = new JLabel("Nueva asignación");
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
        organizerLabel = new JLabel("Organizador (*)");
        organizerLabel.setFont(fuenteBotonesEtiquetas);
        organizerLabel.setPreferredSize(new Dimension(100, 30));

        itineraryLabel = new JLabel("Itinerario (*)");
        itineraryLabel.setFont(fuenteBotonesEtiquetas);
        itineraryLabel.setPreferredSize(new Dimension(100, 30));

        olympLabel = new JLabel("Olimpiada");
        olympLabel.setFont(fuenteBotonesEtiquetas);
        olympLabel.setPreferredSize(new Dimension(100, 30));

        organizerComboBox = new JComboBox<>();
        organizerComboBox.setFont(fuenteCampoTexto);
        organizerComboBox.setPreferredSize(new Dimension(100, 30));

        itineraryField = new JComboBox<>();
        itineraryField.setFont(fuenteCampoTexto);
        itineraryField.setPreferredSize(new Dimension(100, 30));

        olympField = new JLabel("");
        olympField.setFont(fuenteCampoTexto);
        olympField.setPreferredSize(new Dimension(100, 30));

        // Nombres de los organizadores
        String whereClause = "WHERE TIPO='ORGANIZADOR';";
        ResultSet comboBoxesItems = administrador.selectCol("T_USUARIOS", "NOMBRE", whereClause);

        while (comboBoxesItems.next()) {
            String register = comboBoxesItems.getString("NOMBRE");
            organizerComboBox.addItem(register);
        }

        // Códigos de los itinerarios
        comboBoxesItems = administrador.selectCol("T_ITINERARIOS", "CODIGO");

        while (comboBoxesItems.next()) {
            String register = comboBoxesItems.getString("CODIGO");
            itineraryField.addItem(register);
        }

        comboBoxesItems.close();

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
        inputPanel.setBorder(borde);

        inputPanel.add(itineraryLabel);
        inputPanel.add(itineraryField);

        inputPanel.add(olympLabel);
        inputPanel.add(olympField);

        inputPanel.add(organizerLabel);
        inputPanel.add(organizerComboBox);

        assignButton = new JButton("Asignar");
        assignButton.setFont(fuenteBotonesEtiquetas);

        createAssignationPanel = new JPanel();
        createAssignationPanel.setLayout(new FlowLayout());
        createAssignationPanel.setBorder(borde);

        createAssignationPanel.add(assignButton);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createAssignationPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();

        });

        itineraryField.addActionListener(e -> {
            olympField.setText("");

            String whereClause1 = "WHERE CODIGO='" + itineraryField.getSelectedItem() + "'";
            ResultSet exerTitles = administrador.selectCol("T_ITINERARIOS", "OLIMPIADA", whereClause1);

            try {
                assert exerTitles != null;
                if (exerTitles.next()) {
                    olympField.setText(exerTitles.getString("OLIMPIADA"));

                }

            } catch (SQLException ex) {
                new ErrorJOptionPane("No se ha podido obtener la olimpiada");
                new AdminFrame(administrador);
                dispose();

            }

        });

        assignButton.addActionListener(e -> {
            String organizador = (String) organizerComboBox.getSelectedItem();
            String itineraryCode = (String) itineraryField.getSelectedItem();

            if (administrador.assignItineraryToOrganiser(organizador, itineraryCode) == 0) {
                organizerComboBox.setSelectedItem(organizerComboBox.getItemAt(0));
                itineraryField.setSelectedItem(itineraryField.getItemAt(0));
                olympField.setText("");

            }
        });

    }
}