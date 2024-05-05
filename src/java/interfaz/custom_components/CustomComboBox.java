package interfaz.custom_components;

import javax.swing.*;
import java.util.ArrayList;

public class CustomComboBox extends JComboBox<String> implements Fuentes {
    public CustomComboBox() {
        this.setFont(fuenteCampoTexto);
    }
    public CustomComboBox(ArrayList<String> items) {
        this.setFont(fuenteCampoTexto);
    }
}
