package gui.user_frames.admin.assignations.it_org;

import com.jcraft.jsch.JSchException;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.text_fields.CustomPresetTextField;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyAssignationItOrgFrame extends ModifyRegistrationFrameTemplate {
    // Etiquetas
    CustomFieldLabel organizerLabel;
    CustomFieldLabel itineraryLabel;
    CustomFieldLabel olympLabel;
    CustomPresetTextField olympField;

    // Comboboxes
    CustomComboBox organizerComboBox;
    CustomComboBox itineraryField;

    // Botones
    CustomButton assignButton;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createAssignationPanel;

    // Otros
    Admin admin;
    String oldOrg, oldIt;

    public ModifyAssignationItOrgFrame(Admin administrador, String oldOrganizador, String oldItinerario) throws SQLException {
        super(265, "Modificar asignación");

        admin = administrador;
        oldOrg = oldOrganizador;
        oldIt = oldItinerario;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckItOrgAssignationsFrame(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }

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

            String table = "T_ORGANIZADORES";
            String setClause = "SET ORGANIZADOR='" + organizador + "', ITINERARIO='" + itineraryCode + "'";
            String whereClause = "WHERE ORGANIZADOR='" + oldOrg + "' AND ITINERARIO='" + oldIt + "'";

            if (administrador.modifyRegister(table, setClause, whereClause) == 0) {
                try {
                    new CheckItOrgAssignationsFrame(administrador);
                    dispose();

                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());
                }

            }
        });
        setVisible(true);
    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            organizerLabel = new CustomFieldLabel("Organizador (*)");
            itineraryLabel = new CustomFieldLabel("Itinerario (*)");
            olympLabel = new CustomFieldLabel("Olimpiada");
            organizerComboBox = new CustomComboBox();
            itineraryField = new CustomComboBox();
            olympField = new CustomPresetTextField("");

            // Nombres de los organizadores
            String whereClause = "WHERE TIPO='ORGANIZADOR';";
            ResultSet comboBoxesItems = admin.selectCol("T_USUARIOS", "NOMBRE", whereClause);

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("NOMBRE");
                organizerComboBox.addItem(register);
            }

            organizerComboBox.setSelectedItem(oldOrg);

            // Códigos de los itinerarios
            comboBoxesItems = admin.selectCol("T_ITINERARIOS", "CODIGO");

            while (comboBoxesItems.next()) {
                String register = comboBoxesItems.getString("CODIGO");
                itineraryField.addItem(register);
            }

            itineraryField.setSelectedItem(oldIt);

            // Códigos de los itinerarios
            whereClause = "WHERE CODIGO='" + oldIt + "'";
            comboBoxesItems = admin.selectCol("T_ITINERARIOS", "OLIMPIADA", whereClause);

            if (comboBoxesItems.next()) {
                olympField.setText(comboBoxesItems.getString("OLIMPIADA"));
            }

            comboBoxesItems.close();

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 5, 5));
            inputPanel.add(itineraryLabel);
            inputPanel.add(itineraryField);
            inputPanel.add(olympLabel);
            inputPanel.add(olympField);
            inputPanel.add(organizerLabel);
            inputPanel.add(organizerComboBox);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        assignButton = new CustomButton("Modificar");

        createAssignationPanel = new CustomPanel();
        createAssignationPanel.setLayout(new FlowLayout());

        createAssignationPanel.add(assignButton);

        return createAssignationPanel;
    }
}

