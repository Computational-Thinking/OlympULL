package interfaz.mvc;

import interfaz.MainFrame;
import interfaz.custom_components.*;

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

    // Panels
    CustomPanel upperPanel;
    CustomPanel filtersPanel;
    CustomPanel rankingPanel;

    // Controller
    Controller controller;

    public View() {
        this.setSize(1200, 700);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setTitle("OlympULL");

        controller = new Controller();

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

        filtersPanel = new CustomPanel();
        filtersPanel.setLayout(new GridLayout(8, 1, 10, 10));
        filtersPanel.add(filtersTitle);
        filtersPanel.add(olympiadLabel);
        filtersPanel.add(controller.getOlympiadComboBox());
        filtersPanel.add(itineraryLabel);
        filtersPanel.add(controller.getItineraryComboBox());
        filtersPanel.add(conceptLabel);
        filtersPanel.add(controller.getConceptComboBox());
        filtersPanel.add(controller.getViewRanking());

        return filtersPanel;
    }

    public CustomPanel createRankingPanel() {
        rankingPanel = new CustomPanel();
        rankingPanel.setLayout(new GridLayout(2, 2));

        return rankingPanel;
    }
}
