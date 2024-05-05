package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class CustomComboBox extends JComboBox<String> implements Borders, Fonts {
    public CustomComboBox() {
        this.setFont(fuenteCampoTexto);
        this.setBackground(new Color(237, 237, 237));
        this.setBorder(bordeCampoTexto);
    }
    public CustomComboBox(ArrayList<String> items) {
        this.setFont(fuenteCampoTexto);
    }
}
