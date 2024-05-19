package gui.user_frames.admin.assignations;

import com.jcraft.jsch.JSchException;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.custom_components.text_fields.CustomPresetTextField;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyAssignationExMonitorFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
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
    String oldName, oldExer, oldOlymp, oldIt;

    public ModifyAssignationExMonitorFrame(Admin administrador, String name, String exer, String olymp, String itinerary) throws JSchException, SQLException {
        super(335, "Modificar asignación");

        admin = administrador;
        oldName = name;
        oldExer = exer;
        oldOlymp = olymp;
        oldIt = itinerary;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckExMonitorAssignationsFrame(administrador);
                dispose();
            } catch (JSchException | SQLException ex){
                new ErrorJOptionPane(ex.getMessage());
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
            String itCode = itineraryField.getText();

            if (administrador.modifyAssignationExUser(name, exer, olymp, monitor, exerCode, olympCode, itCode) == 0) {
                try {
                    new CheckExMonitorAssignationsFrame(administrador);
                    dispose();

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());
                }

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
            exerField.setSelectedItem(oldExer);

            tituloEjercicioLabel = new CustomFieldLabel("Título del ejercicio");
            tituloEjercicioField = new CustomPresetTextField();
            olympLabel = new CustomFieldLabel("Olimpiada (*)");
            olympField = new CustomComboBox();
            itineraryLabel = new CustomFieldLabel("Itinerario");
            itineraryField = new CustomPresetTextField(oldIt);

            // Nombres de los monitores
            String whereClause = "WHERE TIPO='MONITOR';";
            ResultSet comboBoxesItems = admin.selectCol("T_USUARIOS", "NOMBRE", whereClause);

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("NOMBRE");
                monitorComboBox.addItem(register);
            }

            monitorComboBox.setSelectedItem(oldName);

            // Códigos de los ejercicios
            comboBoxesItems = admin.selectCol("T_EJERCICIOS", "CODIGO");

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("CODIGO");
                exerField.addItem(register);
            }

            exerField.setSelectedItem(oldExer);

            // Título del ejercicio
            comboBoxesItems = admin.selectCol("T_EJERCICIOS", "TITULO", "WHERE CODIGO='" + oldExer + "'");

            if (comboBoxesItems.next()) {
                tituloEjercicioField.setText(comboBoxesItems.getString("TITULO"));
            }

            // Olimpiadas
            String where = "WHERE EJERCICIO='" + oldExer + "'";
            comboBoxesItems = admin.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "OLIMPIADA", where);

            while (comboBoxesItems.next()) {
                olympField.addItem(comboBoxesItems.getString("OLIMPIADA"));
            }

            olympField.setSelectedItem(oldOlymp);

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
        okButton = new CustomButton("Modificar asignación");

        createAssignationPanel = new CustomPanel();
        createAssignationPanel.setLayout(new FlowLayout());

        createAssignationPanel.add(okButton);

        return createAssignationPanel;
    }
}
