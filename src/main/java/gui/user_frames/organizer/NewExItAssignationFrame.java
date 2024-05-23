package gui.user_frames.organizer;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.labels.CustomLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.text_fields.CustomPresetTextField;
import gui.template_pattern.NewRegistrationFrameTemplate;
import users.Organizer;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewExItAssignationFrame extends NewRegistrationFrameTemplate {
    // Botones
    CustomButton assignExercise;

    // Etiquetas
    CustomLabel exerCode;
    CustomLabel olympCode;
    CustomPresetTextField olympCodeField;
    CustomLabel itineraryCode;

    // Combo boxes
    CustomComboBox exerCodeField;
    CustomComboBox itineraryCodeField;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;
    
    // Otros
    Organizer user;

    public NewExItAssignationFrame(Organizer organizador) throws JSchException, SQLException {
        super(280, "Nueva asignación");
        
        user = organizador;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new OrganizerFrame(organizador);
            dispose();
        });

        itineraryCodeField.addActionListener(e -> {
            try {
                String where = "WHERE CODIGO='" + itineraryCodeField.getSelectedItem() + "'";
                ResultSet codes1 = organizador.selectCol("T_ITINERARIOS", "OLIMPIADA", where);

                if (codes1.next()) {
                    olympCodeField.setText(codes1.getString("OLIMPIADA"));

                }

                codes1.close();

            } catch (RuntimeException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }
        });

        assignExercise.addActionListener(e -> {
            if (itineraryCodeField.getItemCount() == 0) {
                new ErrorJOptionPane("Debe seleccionar un itinerario");

            } else {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = olympCodeField.getText();
                String itinerary = (String) itineraryCodeField.getSelectedItem();

                String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
                String data = "'" + exercise + "', '" + olympiad + "', '" + itinerary + "'";

                if (organizador.createRegister(table, data) == 0) {
                    exerCodeField.setSelectedItem(exerCodeField.getItemAt(0));
                    itineraryCodeField.setSelectedItem(itineraryCodeField.getItemAt(0));
                    olympCodeField.setText("");
                }
            }
        });
    }

    @Override
    protected CustomPanel createCenterPanel() {
        try {
            exerCode = new CustomFieldLabel("Ejercicio (*)");
            olympCode = new CustomFieldLabel("Olimpiada (*)");
            itineraryCode = new CustomFieldLabel("Itinerario (*)");
            exerCodeField = new CustomComboBox();
            olympCodeField = new CustomPresetTextField();
            itineraryCodeField = new CustomComboBox();

            ResultSet codes = user.selectCol("T_EJERCICIOS", "CODIGO");

            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                exerCodeField.addItem(registro);
            }

            itineraryCodeField = new CustomComboBox();
            itineraryCodeField.setFont(textFieldFont);

            for (int i = 0; i < user.getItineraries().size(); ++i) {
                itineraryCodeField.addItem(user.getItineraries().get(i));
            }

            codes.close();

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

            inputPanel.add(exerCode);
            inputPanel.add(exerCodeField);
            inputPanel.add(itineraryCode);
            inputPanel.add(itineraryCodeField);
            inputPanel.add(olympCode);
            inputPanel.add(olympCodeField);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        assignExercise = new CustomButton("Asignar", 120, 30);

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(assignExercise);

        return createButtonPanel;
    }
}
