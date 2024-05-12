package gui.custom_components;

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
