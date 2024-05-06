package interfaz.organizer;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.ModifyRegistrationFrameTemplate;
import users.Organizer;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifyExItAssignationFrame extends ModifyRegistrationFrameTemplate implements Borders, Fonts, Icons {
    // Botones
    CustomButton assignExercise;

    // Etiquetas
    CustomFieldLabel exerCode;
    CustomFieldLabel olympCode;
    CustomFieldLabel itCode;
    CustomPresetTextField olympCodeField;

    // Combo boxes
    CustomComboBox exerCodeField;
    CustomComboBox itCodeField;

    // Paneles
    CustomPanel inputPanel;
    CustomPanel createButtonPanel;
    
    // Otros
    Organizer user;
    String oldEx;
    String oldOlymp;

    public ModifyExItAssignationFrame(Organizer organizador, String oldEx, String oldOlymp, String oldIt) throws JSchException, SQLException {
        super(280, "Modificar asignación");

        user = organizador;
        this.oldEx = oldEx;
        this.oldOlymp = oldOlymp;

        add(createCenterPanel(), BorderLayout.CENTER);
        add(createSouthPanel(), BorderLayout.SOUTH);

        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            try {
                new CheckExItAssignationFrame(organizador);
                dispose();

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }
        });

        itCodeField.addActionListener(e -> {
            try {
                String where1 = "WHERE CODIGO='" + itCodeField.getSelectedItem() + "'";
                ResultSet codes1 = organizador.selectCol("T_ITINERARIOS", "OLIMPIADA", where1);

                while (codes1.next()) {
                    String registro = codes1.getString("OLIMPIADA");
                    itCodeField.addItem(registro);

                }

                codes1.close();

            } catch (RuntimeException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }
        });

        assignExercise.addActionListener(e -> {
            if (itCodeField.getItemCount() == 0) {
                new ErrorJOptionPane("Debe seleccionar un itinerario");

            } else {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = olympCodeField.getText();
                String itinerary = (String) itCodeField.getSelectedItem();

                try {
                    if (organizador.modifyAssignationExIt(oldEx, oldOlymp, oldIt, exercise, olympiad, itinerary) == 0) {
                        new CheckExItAssignationFrame(organizador);
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
            itCode = new CustomFieldLabel("Itinerario (*)");
            exerCodeField = new CustomComboBox();
            olympCodeField = new CustomPresetTextField(oldOlymp);
            itCodeField = new CustomComboBox();
            exerCodeField = new CustomComboBox();

            ResultSet codes = user.selectCol("T_EJERCICIOS", "CODIGO");

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (codes.next()) {
                String registro = codes.getString("CODIGO");
                exerCodeField.addItem(registro);
            }

            exerCodeField.setSelectedItem(oldEx);

            for (int i = 0; i < user.getItinerarios().size(); ++i) {
                itCodeField.addItem(user.getItinerarios().get(i));
            }

            inputPanel = new CustomPanel();
            inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

            inputPanel.add(exerCode);
            inputPanel.add(exerCodeField);
            inputPanel.add(itCode);
            inputPanel.add(itCodeField);
            inputPanel.add(olympCode);
            inputPanel.add(olympCodeField);

            codes.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());

        }

        return inputPanel;
    }

    @Override
    protected CustomPanel createSouthPanel() {
        assignExercise = new CustomButton("Modificar");

        createButtonPanel = new CustomPanel();
        createButtonPanel.add(assignExercise);

        return createButtonPanel;
    }
}
