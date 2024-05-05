package interfaz.template;

import interfaz.VentanaInicio;
import interfaz.custom_components.Bordes;
import interfaz.custom_components.CustomButton;
import interfaz.custom_components.CustomFrame;
import interfaz.custom_components.CustomTitleLabel;

import javax.swing.*;
import java.awt.*;

public abstract class VentanaUsuario extends CustomFrame implements Bordes {
    // Title label
    CustomTitleLabel frameTitle;

    // Go back button
    CustomButton goBackButton;

    //
    JPanel upperBar;

    public VentanaUsuario(int width, int height, String title, String titleLabel) {
        this.setSize(width, height);
        setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(5, 5));
        setTitle(title);

        frameTitle = new CustomTitleLabel(titleLabel);
        goBackButton = new CustomButton("< Desconectar");

        upperBar = new JPanel();
        upperBar.setLayout(new BorderLayout(5, 5));
        upperBar.setBorder(borde);

        upperBar.add(frameTitle, BorderLayout.CENTER);
        upperBar.add(goBackButton, BorderLayout.EAST);

        this.add(upperBar, BorderLayout.NORTH);

        goBackButton.addActionListener(e -> {
            new VentanaInicio();
            dispose();
        });
    }

}
