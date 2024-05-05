package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton implements Bordes, Fuentes {
    // Botón sin tamaño
    public CustomButton(String text) {
        this.setText(text);
        this.setFont(fuenteBotonesEtiquetas);
        // this.setBackground(new Color(87, 6, 140));
        // this.setForeground(new Color(237, 237, 237));
    }

    public CustomButton(String text, int width, int height) {
        this.setText(text);
        this.setFont(fuenteBotonesEtiquetas);
        this.setPreferredSize(new Dimension(width, height));
        // this.setBackground(new Color(87, 6, 140));
        // this.setForeground(new Color(237, 237, 237));
    }

}

