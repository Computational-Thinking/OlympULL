package gui.mvc_pattern;

import gui.MainFrame;
import gui.custom_components.*;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.labels.CustomSubtitleLabel;
import gui.custom_components.labels.CustomTitleLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import java.awt.*;

public class View extends CustomFrame {
    // Labels
    CustomTitleLabel title;
    CustomSubtitleLabel filtersTitle;
    CustomFieldLabel olympiadLabel;
    CustomFieldLabel itineraryLabel;
    CustomFieldLabel conceptLabel;

    // Buttons
    CustomButton goBackButton;
    CustomButton rankingButton;

    // Panels
    CustomPanel upperPanel;
    CustomPanel filtersPanel;
    ChartPanel rankingPanel;

    // Controller
    Controller controller;

    public View() {
        this.setSize(1350, 800);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("Consulta de rankings");

        controller = new Controller();

        // Se añaden los paneles a la ventana
        add(createUpperPanel(), BorderLayout.NORTH);
        add(createFilterPanel(), BorderLayout.WEST);

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
        olympiadLabel = new CustomFieldLabel("Olimpiada (*)");
        itineraryLabel = new CustomFieldLabel("Itinerario (*)");
        conceptLabel = new CustomFieldLabel("Concepto(*)");
        rankingButton = new CustomButton("Ver ranking");

        filtersPanel = new CustomPanel();
        filtersPanel.setLayout(new GridLayout(17, 1, 10, 10));
        filtersPanel.add(olympiadLabel);
        filtersPanel.add(controller.getOlympiadComboBox());
        filtersPanel.add(itineraryLabel);
        filtersPanel.add(controller.getItineraryComboBox());
        filtersPanel.add(conceptLabel);
        filtersPanel.add(controller.getConceptComboBox());
        filtersPanel.add(rankingButton);

        rankingButton.addActionListener(e -> {
            if (controller.getOlympiadComboBox().getSelectedItem() == "" ||
                    controller.getItineraryComboBox().getSelectedItem() == "" ||
                    controller.getConceptComboBox().getSelectedItem() == "") {
                new ErrorJOptionPane("Los campos Olimpiada, Itinerario y Concepto son obligatorios");
            } else {
                createRankingPanel();
            }
        });

        return filtersPanel;
    }

    public void createRankingPanel() {
        if (rankingPanel != null) remove(rankingPanel);

        JFreeChart ranking = controller.generateRanking();

        rankingPanel = new ChartPanel(ranking);
        rankingPanel.setBorder(borde);
        add(rankingPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }
}
