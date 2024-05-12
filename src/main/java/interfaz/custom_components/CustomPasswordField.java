package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomPasswordField extends JPasswordField implements Borders, Fonts {
    public CustomPasswordField(String password) {
        this.setText(password);
        this.setBackground(new Color(255, 255, 255));
        this.setBorder(bordeCampoTexto);
    }
}
