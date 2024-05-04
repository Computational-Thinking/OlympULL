package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomFieldLabel extends JLabel implements Fuentes {
    public CustomFieldLabel(String text) {
        this.setText(text);
        this.setFont(fuenteBotonesEtiquetas);
        // this.setHorizontalAlignment(SwingConstants.RIGHT);
        // this.setForeground(new Color(86, 6, 140));
    }
}
