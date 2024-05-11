package interfaz.mvc;

import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomComboBox;
import interfaz.custom_components.ErrorJOptionPane;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {
    // Filters
    CustomComboBox olympiadComboBox;
    CustomComboBox itineraryComboBox;
    CustomComboBox conceptComboBox;
    CustomButton viewRanking;

    // Model
    Model model;

    public Controller() {
        model = new Model();

        olympiadComboBox = new CustomComboBox();
        olympiadComboBox.addItem("");
        itineraryComboBox = new CustomComboBox();
        itineraryComboBox.addItem("");
        conceptComboBox = new CustomComboBox();
        conceptComboBox.addItem("");

        viewRanking = new CustomButton("Ver ranking(s)");

        try {
            ResultSet data = model.selectOlympiads();
            while (data.next()) {
                olympiadComboBox.addItem(data.getString("TITULO"));
            }
            data.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        olympiadComboBox.addActionListener(e -> {
            try {
                ResultSet data = model.selectItineraries((String) olympiadComboBox.getSelectedItem());
                while (data.next()) {
                    itineraryComboBox.addItem(data.getString("TITULO"));
                }
                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        itineraryComboBox.addActionListener(e -> {
            try {
                String selectedOlymp = (String) olympiadComboBox.getSelectedItem();
                String selectedIt = (String) itineraryComboBox.getSelectedItem();
                ResultSet data = model.selectExercises(selectedOlymp, selectedIt);
                while (data.next()) {
                    conceptComboBox.addItem(data.getString("CONCEPTO"));
                }
                data.close();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        viewRanking.addActionListener(e -> {

        });
    }

    public CustomComboBox getOlympiadComboBox() {
        return olympiadComboBox;
    }

    public CustomComboBox getItineraryComboBox() {
        return itineraryComboBox;
    }

    public CustomComboBox getConceptComboBox() {
        return conceptComboBox;
    }

    public CustomButton getViewRanking() {
        return viewRanking;
    }
}
