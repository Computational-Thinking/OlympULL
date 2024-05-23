package gui.custom_components.text_fields;

import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField implements Borders, Fonts {

    public CustomTextField(String text) {
        this.setText(text);
        this.setBackground(new Color(255, 255, 255));
        this.setBorder(textFieldBorder);
        this.setFont(textFieldFont);

    }

}
