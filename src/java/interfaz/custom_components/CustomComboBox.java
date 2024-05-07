package interfaz.custom_components;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.util.ArrayList;

public class CustomComboBox extends JComboBox<String> implements Borders, Fonts, Icons {
    public CustomComboBox() {
        this.setFont(fuenteCampoTexto);
        this.setBackground(new Color(255, 255, 255));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new BasicArrowButton(SwingConstants.SOUTH, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder()); // Eliminar borde del botón
                return button;
            }
        });
    }
    public CustomComboBox(ArrayList<String> items) {
        this.setFont(fuenteCampoTexto);
        this.setBackground(new Color(255, 255, 255));
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton button = new BasicArrowButton(SwingConstants.SOUTH, Color.BLACK, Color.BLACK, Color.BLACK, Color.BLACK);
                button.setBackground(Color.WHITE);
                button.setBorder(BorderFactory.createEmptyBorder()); // Eliminar borde del botón
                return button;
            }
        });

        for (String item : items) {
            this.addItem(item);
        }
    }



}