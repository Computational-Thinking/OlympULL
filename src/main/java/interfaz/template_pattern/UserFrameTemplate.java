package interfaz.template_pattern;

import interfaz.MainFrame;
import interfaz.custom_components.Borders;
import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomFrame;

import javax.swing.*;
import java.awt.*;

public abstract class UserFrameTemplate extends CustomFrame implements Borders {
    // Go back button
    CustomButton goBackButton;

    public UserFrameTemplate(int height, String title, String titleLabel) {
        this.setSize(825, height);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        this.setTitle(title);

        goBackButton = new CustomButton("< Desconectar");
        this.add(buildUpperBar(titleLabel, goBackButton), BorderLayout.NORTH);

        goBackButton.addActionListener(e -> {
            new MainFrame();
            dispose();
        });
    }

    protected abstract JPanel createCenterPanel();

    protected abstract JPanel createSouthPanel();

}
