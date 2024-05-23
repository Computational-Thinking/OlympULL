package gui.custom_components.buttons;

import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton implements Borders, Fonts {
    // Botón sin tamaño
    public CustomButton(String text) {
        this.setText(text);
        this.setFont(buttonAndLabelFont);
    }

    public CustomButton(String text, ImageIcon icon) {
        this.setText(text);
        this.setIcon(icon);
        this.setFont(buttonAndLabelFont);
    }

    public CustomButton(String text, int width, int height) {
        this.setText(text);
        this.setFont(buttonAndLabelFont);
        this.setPreferredSize(new Dimension(width, height));
    }

}

