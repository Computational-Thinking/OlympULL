package interfaz.organizer;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.NewRegistrationFrameTemplate;
import users.Organizer;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NewExItAssignationFrame extends NewRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton assignExercise;

    // Etiquetas
    CustomLabel exerCode;
    CustomLabel olympCode;
    CustomLabel olympCodeField;
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
                new ErrorJOptionPane("Debe seleccionar un itinerary");

            } else {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = olympCodeField.getText();
                String itinerary = (String) itineraryCodeField.getSelectedItem();

                if (organizador.createAssignationExIt(exercise, olympiad, itinerary) == 0) {
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
            olympCodeField = new CustomFieldLabel();
            itineraryCodeField = new CustomComboBox();

            ResultSet codes = user.selectCol("T_EJERCICIOS", "CODIGO");

            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                exerCodeField.addItem(registro);
            }

            itineraryCodeField = new CustomComboBox();
            itineraryCodeField.setFont(fuenteCampoTexto);

            for (int i = 0; i < user.getItinerarios().size(); ++i) {
                itineraryCodeField.addItem(user.getItinerarios().get(i));
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
