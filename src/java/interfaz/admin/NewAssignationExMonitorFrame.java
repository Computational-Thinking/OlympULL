package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;

public class NewAssignationExMonitorFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Etiquetas
    CustomFieldLabel monitorLabel;
    CustomFieldLabel exerCodeLabel;
    CustomFieldLabel tituloEjercicioLabel;
    CustomPresetTextField tituloEjercicioField;
    CustomFieldLabel olympLabel;
    CustomFieldLabel itineraryLabel;
    CustomPresetTextField itineraryField;
    
    // Comboboxes
    CustomComboBox monitorComboBox;
    CustomComboBox exerField;
    CustomComboBox olympField;

    // Botones
    CustomButton okButton;

    // Paneles
    CustomPanel createAssignationPanel;
    CustomPanel inputPanel;

    // Otros
    Admin admin;

    public NewAssignationExMonitorFrame(Admin administrador) throws JSchException, SQLException {
        super(335, "Nueva asignación");

        this.admin = administrador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();

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
                new ErrorJOptionPane("No se ha podido obtener el título del ejercicio");
                new AdminFrame(administrador);
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
                new ErrorJOptionPane("No se ha podido obtener las olimpiadas");
                new AdminFrame(administrador);
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
                new ErrorJOptionPane("No se ha podido obtener el itinerario");
                new AdminFrame(administrador);
                dispose();

            }
        });

        okButton.addActionListener(e -> {
            String monitor = (String) monitorComboBox.getSelectedItem();
            String exerCode = (String) exerField.getSelectedItem();
            String olympCode = (String) olympField.getSelectedItem();
            String itineraryCode = itineraryField.getText();

            if (administrador.assignExerciseToUser(monitor, exerCode, olympCode, itineraryCode) == 0) {
                monitorComboBox.setSelectedItem(monitorComboBox.getItemAt(0));
                exerField.setSelectedItem(exerField.getItemAt(0));
                tituloEjercicioField.setText("");
                itineraryField.setText("");

            }
        });

    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            monitorLabel = new CustomFieldLabel("Monitor (*)");
            exerCodeLabel = new CustomFieldLabel("Ejercicio (*)");
            monitorComboBox = new CustomComboBox();
            exerField = new CustomComboBox();
            tituloEjercicioLabel = new CustomFieldLabel("Título del ejercicio");
            tituloEjercicioField = new CustomPresetTextField("");
            olympLabel = new CustomFieldLabel("Olimpiada (*)");
            olympField = new CustomComboBox();
            itineraryLabel = new CustomFieldLabel("Itinerario");
            itineraryField = new CustomPresetTextField("");

            // Nombres de los monitores
            String whereClause = "WHERE TIPO='MONITOR';";
            ResultSet comboBoxesItems = admin.selectCol("T_USUARIOS", "NOMBRE", whereClause);

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("NOMBRE");
                monitorComboBox.addItem(register);
            }

            // Códigos de los ejercicios
            comboBoxesItems = admin.selectCol("T_EJERCICIOS", "CODIGO");

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("CODIGO");
                exerField.addItem(register);
            }

            comboBoxesItems.close();

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(5, 2, 5, 5));
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

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        okButton = new CustomButton("Asignar");

        createAssignationPanel = new CustomPanel();
        createAssignationPanel.setLayout(new FlowLayout());

        createAssignationPanel.add(okButton);

        return createAssignationPanel;
    }
}
