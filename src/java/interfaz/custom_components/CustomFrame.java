package interfaz.custom_components;

import javax.swing.*;

public abstract class CustomFrame extends JFrame implements Icons {
    public CustomFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setIconImage(iconoVentana);
    }
}
