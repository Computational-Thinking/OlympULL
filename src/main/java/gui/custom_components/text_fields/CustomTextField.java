package gui.custom_components.text_fields;

import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;

import javax.swing.*;
import java.awt.*;

public abstract class CustomTextField extends JTextField implements Borders, Fonts {

    public CustomTextField(String text) {
        this.setText(text);
        this.setBackground(new Color(255, 255, 255));
        this.setBorder(bordeCampoTexto);
        this.setFont(fuenteCampoTexto);

    }

}
