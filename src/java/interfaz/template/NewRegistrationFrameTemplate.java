package interfaz.template;

import interfaz.custom_components.Borders;
import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomFrame;

import javax.swing.*;
import java.awt.*;

public abstract class NewRegistrationFrameTemplate extends CustomFrame implements Borders {
    // Go back button
    CustomButton goBackButton;

    public NewRegistrationFrameTemplate(int height, String titleLabel) {
        this.setSize(500, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Nuevo registro");

        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

    }

    public NewRegistrationFrameTemplate(String titleLabel) {
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Nuevo registro");

        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

    }

    protected CustomButton getGoBackButton() {
        return goBackButton;
    }

    protected abstract JPanel createCenterPanel();

    protected abstract JPanel createSouthPanel();

}
