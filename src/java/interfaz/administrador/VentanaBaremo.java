package interfaz.administrador;

import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class VentanaBaremo extends JFrame {
    JTextField minPunctuationField;
    JTextField maxPunctuationField;
    JTextField minPunctuationTagField;
    JTextField maxPunctuationTagField;
    JButton addPunctuationButton;
    JButton deletePunctuationButton;
    JButton stablishScaleButton;
    JPanel scalePanel;
    JPanel minScalePanel;
    JPanel maxScalePanel;
    JPanel extensibleScalePanel;
    JPanel addDeletePanel;
    Vector<JPanel> newFields;
    int nScales;

    public VentanaBaremo(Monitor monitor) {
        setSize(500, 250);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Establecer baremo de ejercicio");

        newFields = new Vector<>();

        minPunctuationField = new JTextField();
        minPunctuationField.setPreferredSize(new Dimension(20, 30));
        maxPunctuationField = new JTextField();
        maxPunctuationField.setPreferredSize(new Dimension(20, 30));

        minPunctuationTagField = new JTextField();
        minPunctuationTagField.setPreferredSize(new Dimension(175, 30));

        maxPunctuationTagField = new JTextField();
        maxPunctuationTagField.setPreferredSize(new Dimension(175, 30));

        extensibleScalePanel = new JPanel();
        extensibleScalePanel.setLayout(new GridLayout(8, 2));

        scalePanel = new JPanel();
        scalePanel.setLayout(new BorderLayout());

        minScalePanel = new JPanel();
        minScalePanel.setLayout(new GridLayout(1, 2));

        minScalePanel.add(minPunctuationField);
        minScalePanel.add(minPunctuationTagField);

        maxScalePanel = new JPanel();
        maxScalePanel.setLayout(new GridLayout(1, 2));

        maxScalePanel.add(maxPunctuationField);
        maxScalePanel.add(maxPunctuationTagField);

        scalePanel.add(minScalePanel, BorderLayout.NORTH);
        scalePanel.add(extensibleScalePanel, BorderLayout.CENTER);
        scalePanel.add(maxScalePanel, BorderLayout.SOUTH);

        addPunctuationButton = new JButton("+");
        addPunctuationButton.setPreferredSize(new Dimension(20, 30));

        deletePunctuationButton = new JButton("-");
        deletePunctuationButton.setPreferredSize(new Dimension(20, 30));

        addDeletePanel = new JPanel();
        addDeletePanel.setLayout(new GridLayout(1, 2));

        addDeletePanel.add(addPunctuationButton);
        addDeletePanel.add(deletePunctuationButton);

        stablishScaleButton = new JButton("Establecer baremo");

        add(scalePanel, BorderLayout.NORTH);
        add(addDeletePanel, BorderLayout.CENTER);
        add(stablishScaleButton, BorderLayout.SOUTH);

        nScales = 2;

        addPunctuationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (nScales < 10) {
                    JPanel newMark = new JPanel();
                    newMark.setLayout(new GridLayout(1, 2));
                    newMark.add(new JTextField(20));
                    newMark.add(new JTextField(175));
                    newFields.add(newMark);
                    extensibleScalePanel.add(newMark);
                    nScales += 1;
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "No es posible añadir más escalas");
                }

            }
        });

        deletePunctuationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFields.remove(newFields.size() - 1);
            }
        });

        this.setVisible(true);
    }
}
