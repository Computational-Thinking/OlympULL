package interfaz.template;

import interfaz.custom_components.Borders;
import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomFrame;

import javax.swing.*;
import java.awt.*;

public abstract class CheckTableFrameTemplate extends CustomFrame implements Borders {
    // Go back button
    CustomButton goBackButton;

    public CheckTableFrameTemplate(int height, String titleLabel) {
        this.setSize(875, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Consulta de tabla");

        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

    }

    protected CustomButton getGoBackButton() {
        return goBackButton;
    }

    protected abstract JScrollPane createJScrollPane();
}
