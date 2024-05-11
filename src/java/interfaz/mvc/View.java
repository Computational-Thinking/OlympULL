package interfaz.mvc;

import interfaz.MainFrame;
import interfaz.custom_components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class View extends CustomFrame {
    // Labels
    CustomTitleLabel title;
    CustomSubtitleLabel filtersTitle;
    CustomFieldLabel olympiadLabel;
    CustomFieldLabel itineraryLabel;
    CustomFieldLabel conceptLabel;

    // Custom Combo boxes
    CustomComboBox olympiadsComboBox;
    CustomComboBox itineraryComboBox;
    CustomComboBox conceptComboBox;

    // Buttons
    CustomButton goBackButton;
    CustomButton viewRanking;

    // Panels
    CustomPanel upperPanel;
    CustomPanel filtersPanel;
    CustomPanel rankingPanel;

    // Model
    Model model;

    public View() {
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("OlympULL");

        model = new Model();

        // Se añaden los paneles a la ventana
        add(createUpperPanel(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);
        add(createRankingPanel(), BorderLayout.CENTER);

        this.setVisible(true);

        // Action listeners
    }

    public CustomPanel createUpperPanel() {
        title = new CustomTitleLabel("Consulta de rankings");
        goBackButton = new CustomButton("< Volver");

        upperPanel = new CustomPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(title, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        goBackButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });

        return upperPanel;
    }

    public CustomPanel createFilterPanel() {
        filtersTitle = new CustomSubtitleLabel("Criterios de selección");
        olympiadLabel = new CustomFieldLabel("Olimpiada (*)");
        itineraryLabel = new CustomFieldLabel("Itinerario (*)");
        conceptLabel = new CustomFieldLabel("Concepto");

        olympiadsComboBox = new CustomComboBox();
        olympiadsComboBox.addItem("");
        itineraryComboBox = new CustomComboBox();
        itineraryComboBox.addItem("");
        conceptComboBox = new CustomComboBox();
        conceptComboBox.addItem("");

        viewRanking = new CustomButton("Ver ranking(s)");

        try {
            ResultSet data = model.selectOlympiads();
            while (data.next()) {
                olympiadsComboBox.addItem(data.getString("TITULO"));
            }
            data.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        olympiadsComboBox.addActionListener(e -> {
            try {
                ResultSet data = model.selectItineraries((String) olympiadsComboBox.getSelectedItem());
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
                String selectedOlymp = (String) olympiadsComboBox.getSelectedItem();
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

        filtersPanel = new CustomPanel();
        filtersPanel.setLayout(new GridLayout(8, 1));
        filtersPanel.add(filtersTitle);
        filtersPanel.add(olympiadLabel);
        filtersPanel.add(olympiadsComboBox);
        filtersPanel.add(itineraryLabel);
        filtersPanel.add(itineraryComboBox);
        filtersPanel.add(conceptLabel);
        filtersPanel.add(conceptComboBox);
        filtersPanel.add(viewRanking);

        return filtersPanel;
    }

    public CustomPanel createRankingPanel() {
        rankingPanel = new CustomPanel();

        return rankingPanel;
    }
}
