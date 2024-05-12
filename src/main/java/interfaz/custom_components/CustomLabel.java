package interfaz.custom_components;

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
