package gui.custom_components.labels;

import gui.custom_components.predefined_elements.Fonts;

import javax.swing.*;
import java.awt.*;

public abstract class CustomLabel extends JLabel implements Fonts {
    public CustomLabel(Font fuente) {
        this.setFont(fuente);
    }
    public CustomLabel(String text, Font fuente) {
        this.setText(text);
        this.setFont(fuente);
    }
}
