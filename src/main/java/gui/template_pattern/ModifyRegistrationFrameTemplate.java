package gui.template_pattern;

import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.buttons.CustomButton;
import gui.custom_components.CustomFrame;

import javax.swing.*;
import java.awt.*;

public abstract class ModifyRegistrationFrameTemplate extends CustomFrame implements Borders {
    // Go back button
    CustomButton goBackButton;

    public ModifyRegistrationFrameTemplate(int height, String titleLabel) {
        this.setSize(500, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Modificar registro");

        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

    }

    public ModifyRegistrationFrameTemplate(String titleLabel) {
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle("Modificar registro");

        goBackButton = new CustomButton("< Volver");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

    }

    protected CustomButton getGoBackButton() {
        return goBackButton;
    }

    protected abstract JPanel createCenterPanel();

    protected abstract JPanel createSouthPanel();
}
