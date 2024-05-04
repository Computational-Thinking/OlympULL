package interfaz.custom_components;

import javax.swing.*;

public abstract class CustomJOptionPane implements Fuentes {
    JPanel panel;
    JLabel label;
    public CustomJOptionPane(String mensaje) {
        panel = new JPanel();
        label = new JLabel(mensaje);
        label.setFont(fuenteJOptionPanes);
        panel.add(label);

        // Crear un JOptionPane con el JPanel
        JOptionPane.showMessageDialog(null, panel);
    }
}
