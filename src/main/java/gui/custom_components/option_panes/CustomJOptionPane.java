package gui.custom_components.option_panes;

import gui.custom_components.text_fields.CustomPresetTextField;
import gui.custom_components.predefined_elements.Fonts;

import javax.swing.*;

public abstract class CustomJOptionPane implements Fonts {
    JPanel panel;
    CustomPresetTextField label;
    public CustomJOptionPane(String mensaje) {
        panel = new JPanel();
        label = new CustomPresetTextField(mensaje);
        panel.add(label);

        // Crear un JOptionPane con el JPanel
        JOptionPane.showMessageDialog(null, panel);
    }

}
