package gui.custom_components;

import gui.custom_components.predefined_elements.Borders;

import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel implements Borders {
    public CustomPanel() {
        this.setBorder(border);
        this.setBackground(new Color(237, 237, 237));
    }
}
