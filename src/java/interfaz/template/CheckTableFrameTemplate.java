package interfaz.template;

import interfaz.custom_components.*;
import users.Admin;
import users.User;

import javax.swing.*;
import java.awt.*;

public abstract class CheckTableFrameTemplate extends CustomFrame implements Borders, Icons {
    // Go back button
    CustomButton goBackButton;
    CustomButton exportButton;
    CustomButton importButton;
    JPanel buttonsPanel;

    public CheckTableFrameTemplate(User user, int height, String titleLabel) {
        this.setSize(875, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Consulta de tabla");

        buttonsPanel = new CustomPanel();

        if (user.getClass() == Admin.class) {
            ImageIcon exportIcon = new ImageIcon(iconoExportar.getScaledInstance(15, 15, Image.SCALE_SMOOTH));
            ImageIcon importIcon = new ImageIcon(iconoImportar.getScaledInstance(15, 15, Image.SCALE_SMOOTH));

            exportButton = new CustomButton("Exportar datos", exportIcon);
            importButton = new CustomButton("Importar datos", importIcon);

            buttonsPanel.add(exportButton);
            buttonsPanel.add(importButton);
        }

        goBackButton = new CustomButton("< Volver");

        buttonsPanel.add(goBackButton);

        this.add(buildUpperBar(titleLabel, buttonsPanel), BorderLayout.NORTH);
    }

    protected CustomButton getGoBackButton() {
        return goBackButton;
    }

    protected CustomButton getExportButton() {
        return exportButton;
    }

    protected CustomButton getImportButton() {
        return importButton;
    }

    protected abstract JScrollPane createJScrollPane();
}
