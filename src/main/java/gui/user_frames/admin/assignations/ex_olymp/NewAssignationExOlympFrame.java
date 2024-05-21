package gui.user_frames.admin.assignations.ex_olymp;

import com.jcraft.jsch.JSchException;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;

public class NewAssignationExOlympFrame extends NewRegistrationFrameTemplate {
    // Botones
    CustomButton assignExercise;
    
    // Etiquetas
    CustomFieldLabel exerCode;
    CustomFieldLabel olympCode;
    CustomFieldLabel itinerarioCode;
    
    // Combo boxes
    CustomComboBox exerCodeField;
    CustomComboBox olympCodeField;
    CustomComboBox itinerarioCodeField;
    
    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;

    // Otros
    Admin admin;

    public NewAssignationExOlympFrame(Admin administrador) throws JSchException, SQLException {
        super(280, "Nueva asignación");

        this.admin = administrador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
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

                String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
                String data = "'" + exercise + "', '" + olympiad + "', '" + itinerary + "'";

                if (administrador.createRegister(table, data) == 0) {
                    exerCodeField.setSelectedItem(exerCodeField.getItemAt(0));
                    olympCodeField.setSelectedItem(olympCodeField.getItemAt(0));
                    itinerarioCodeField.removeAllItems();
                }
            }
        });

    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            exerCode = new CustomFieldLabel("Ejercicio (*)");
            olympCode = new CustomFieldLabel("Olimpiada (*)");
            itinerarioCode = new CustomFieldLabel("Itinerario (*)");
            exerCodeField = new CustomComboBox();
            olympCodeField = new CustomComboBox();
            itinerarioCodeField = new CustomComboBox();
            exerCodeField = new CustomComboBox();

            ResultSet codes = admin.selectCol("T_EJERCICIOS", "CODIGO");

            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                exerCodeField.addItem(registro);
            }

            olympCodeField = new CustomComboBox();

            codes = admin.selectCol("T_OLIMPIADAS", "CODIGO");

            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                olympCodeField.addItem(registro);
            }

            itinerarioCodeField = new CustomComboBox();

            String where = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
            codes = admin.selectCol("T_ITINERARIOS", "CODIGO", where);

            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                itinerarioCodeField.addItem(registro);
            }

            codes.close();

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10));
            inputPanel.add(exerCode);
            inputPanel.add(exerCodeField);
            inputPanel.add(olympCode);
            inputPanel.add(olympCodeField);
            inputPanel.add(itinerarioCode);
            inputPanel.add(itinerarioCodeField);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        assignExercise = new CustomButton("Asignar", 150, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(assignExercise);

        return createButtonPanel;
    }
}
