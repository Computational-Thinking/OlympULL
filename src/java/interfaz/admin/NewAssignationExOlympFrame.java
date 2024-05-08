package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.NewRegistrationFrameTemplate;
import users.Admin;

import java.awt.*;
import java.sql.*;

public class NewAssignationExOlympFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
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
        super(290, "Nueva asignación");

        this.admin = administrador;

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
                String itinerario = (String) itinerarioCodeField.getSelectedItem();

                if (administrador.assignExerciseToOlympiad(exercise, olympiad, itinerario) == 0) {
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
