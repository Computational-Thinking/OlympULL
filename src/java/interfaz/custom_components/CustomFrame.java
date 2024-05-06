package interfaz.custom_components;

import javax.swing.*;
import java.awt.*;

public abstract class CustomFrame extends JFrame implements Borders, Icons {
    public CustomFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setIconImage(iconoVentana);
    }

    protected JPanel buildUpperBar(String title, CustomButton goBackButton) {
        CustomTitleLabel frameTitle = new CustomTitleLabel(title);

        CustomPanel upperBar = new CustomPanel();
        upperBar.setLayout(new BorderLayout(5, 5));

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(goBackButton, BorderLayout.EAST);

        return upperBar;
    }
}
