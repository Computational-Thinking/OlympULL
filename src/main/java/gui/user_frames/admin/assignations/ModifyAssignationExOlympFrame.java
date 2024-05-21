package gui.user_frames.admin.assignations;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
import gui.template_pattern.ModifyRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;

public class ModifyAssignationExOlympFrame extends ModifyRegistrationFrameTemplate {
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
    String oldEx, oldOlymp, oldIt;

    public ModifyAssignationExOlympFrame(Admin administrador, String oldEx, String oldOlymp, String oldIt) throws JSchException, SQLException {
        super(290, "Modificar asignación");

        this.admin = administrador;
        this.oldEx = oldEx;
        this.oldOlymp = oldOlymp;
        this.oldIt = oldIt;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckExOlympAssignationsFrame(administrador);
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
                        new CheckExOlympAssignationsFrame(administrador);
                        dispose();
                    }
                } catch (JSchException | SQLException ex) {
                    new ErrorJOptionPane(ex.getMessage());
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

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                exerCodeField.addItem(registro);
            }

            exerCodeField.setSelectedItem(oldEx);

            olympCodeField = new CustomComboBox();

            codes = admin.selectCol("T_OLIMPIADAS", "CODIGO");

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                olympCodeField.addItem(registro);
            }

            olympCodeField.setSelectedItem(oldOlymp);

            itinerarioCodeField = new CustomComboBox();

            String where = "WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "'";
            codes = admin.selectCol("T_ITINERARIOS", "CODIGO", where);

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                itinerarioCodeField.addItem(registro);
            }

            itinerarioCodeField.setSelectedItem(oldIt);

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
        assignExercise = new CustomButton("Modificar asignación", 175, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(assignExercise);

        return createButtonPanel;
    }
}