package gui.custom_components;

import javax.swing.*;
import java.awt.*;

public abstract class CustomFrame extends JFrame implements Borders, Icons {
    CustomPanel upperBar;

    public CustomFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setIconImage(iconoVentana);
    }

    protected CustomPanel getUpperBar() {
        return upperBar;
    }

    protected JPanel buildUpperBar(String title, CustomButton button) {
        CustomTitleLabel frameTitle = new CustomTitleLabel(title);

        upperBar = new CustomPanel();
        upperBar.setLayout(new BorderLayout(5, 5));

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(button, BorderLayout.EAST);

        return upperBar;
    }

    protected JPanel buildUpperBar(String title, JPanel panel) {
        CustomTitleLabel frameTitle = new CustomTitleLabel(title);

        upperBar = new CustomPanel();
        upperBar.setLayout(new BorderLayout(5, 5));

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(panel, BorderLayout.EAST);

        return upperBar;
    }
}
