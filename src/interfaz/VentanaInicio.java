package interfaz;

import javax.swing.*;
import java.awt.*;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;

public class VentanaInicio extends JFrame {
    JPanel logoPanel;
    JLabel olympullLogo;

    public VentanaInicio() {
        setSize(500, 500);
        getContentPane().setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Bienvenido a OlympULL");
        logoPanel = new JPanel();
        logoPanel.setBackground(new Color(249, 233, 93));

        // Add logo to the label that will be on the top of the window
        ImageIcon logo = new ImageIcon("images/logo olympull.png");
        Image originalIcon = logo.getImage();
        Image escalatedLogo = originalIcon.getScaledInstance(536, 112, Image.SCALE_SMOOTH);
        logo = new ImageIcon(escalatedLogo);
        olympullLogo = new JLabel(logo);
        logoPanel.add(olympullLogo, BorderLayout.CENTER);
        add(logoPanel);
        this.setVisible(true);
    }

}
