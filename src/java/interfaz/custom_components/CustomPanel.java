package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public class CustomPanel extends JPanel implements Borders {
    public CustomPanel() {
        this.setBorder(borde);
        this.setBackground(new Color(237, 237, 237));
    }
}
