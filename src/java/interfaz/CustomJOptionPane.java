package interfaz;

import javax.swing.*;

public class CustomJOptionPane implements Fuentes {
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
