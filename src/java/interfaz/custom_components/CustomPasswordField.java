package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomPasswordField extends JPasswordField implements Bordes, Fuentes {
    public CustomPasswordField(String password) {
        this.setText(password);
        this.setBackground(new Color(237, 237, 237));
        this.setBorder(bordeCampoTexto);
    }
}
