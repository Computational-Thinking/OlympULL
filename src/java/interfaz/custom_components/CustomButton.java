package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton implements Borders, Fonts {
    // Botón sin tamaño
    public CustomButton(String text) {
        this.setText(text);
        this.setFont(fuenteBotonesEtiquetas);
    }

    public CustomButton(String text, ImageIcon icon) {
        this.setText(text);
        this.setIcon(icon);
        this.setFont(fuenteBotonesEtiquetas);
    }

    public CustomButton(String text, int width, int height) {
        this.setText(text);
        this.setFont(fuenteBotonesEtiquetas);
        this.setPreferredSize(new Dimension(width, height));
    }

}

