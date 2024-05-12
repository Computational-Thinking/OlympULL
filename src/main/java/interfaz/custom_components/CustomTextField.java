package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomTextField extends JTextField implements Borders, Fonts {

    public CustomTextField(String text) {
        this.setText(text);
        this.setBackground(new Color(255, 255, 255));
        this.setBorder(bordeCampoTexto);
        this.setFont(fuenteCampoTexto);

    }

}
