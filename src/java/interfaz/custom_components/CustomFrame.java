package interfaz.custom_components;

import interfaz.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class CustomFrame extends JFrame implements Borders, Icons {
    public CustomFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        setIconImage(iconoVentana);
    }

    protected JPanel buildUpperBar(String title, CustomButton goBackButton) {
        CustomTitleLabel frameTitle = new CustomTitleLabel(title);

        JPanel upperBar = new JPanel();
        upperBar.setBackground(new Color(255, 255, 255));
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(goBackButton, BorderLayout.EAST);

        return upperBar;
    }
}
