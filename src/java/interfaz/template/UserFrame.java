package interfaz.template;

import interfaz.MainFrame;
import interfaz.custom_components.Borders;
import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomFrame;
import interfaz.custom_components.CustomTitleLabel;
import users.User;

import javax.swing.*;
import java.awt.*;

public abstract class UserFrame extends CustomFrame implements Borders {
    // Title label
    CustomTitleLabel frameTitle;

    // Go back button
    CustomButton goBackButton;

    //
    JPanel upperBar;

    public UserFrame(int width, int height, String title, String titleLabel, String goBackText) {
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle(title);

        frameTitle = new CustomTitleLabel(titleLabel);
        goBackButton = new CustomButton(goBackText);

        upperBar = new JPanel();
        upperBar.setBackground(new Color(255, 255, 255));
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(goBackButton, BorderLayout.EAST);

        this.add(upperBar, BorderLayout.NORTH);

        goBackButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });
    }

    protected abstract JPanel createCenterPanel();

    protected abstract JPanel createSouthPanel();

}
