package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Monitor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
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
                    extensibleScalePanel.revalidate();
                    extensibleScalePanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(null, "No es posible añadir más filas");
                }

            }
        });

        deletePunctuationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Component[] components = extensibleScalePanel.getComponents();
                if (components.length > 0) {
                    Component lastComponent = components[components.length - 1];
                    extensibleScalePanel.remove(lastComponent);
                    extensibleScalePanel.revalidate();
                    extensibleScalePanel.repaint();
                    nScales -= 1;
                } else {
                    JOptionPane.showMessageDialog(null, "No hay registros que borrar");
                }
            }
        });

        stablishScaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] scalePoints = new int[nScales];
                String[] scaleTags = new String[nScales];

                scalePoints[0] = Integer.parseInt(minPunctuationField.getText());
                scaleTags[0] = minPunctuationTagField.getText();

                Component[] registers = extensibleScalePanel.getComponents();
                int counter = 1;

                if (registers.length > 0) {
                    for (int i = 0; i < registers.length; ++i) {
                        JPanel dummy = (JPanel) registers[i];
                        Component[] textFields = dummy.getComponents();
                        JTextField value = (JTextField) textFields[0];
                        JTextField tag = (JTextField) textFields[1];

                        scalePoints[counter] = Integer.parseInt(value.getText());
                        scaleTags[counter] = (tag.getText());

                        ++counter;
                    }
                }

                scalePoints[scalePoints.length - 1] = Integer.parseInt(maxPunctuationField.getText());
                scaleTags[scaleTags.length - 1] = maxPunctuationTagField.getText();

                try {
                    monitor.stablishScale(scalePoints, scaleTags);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        this.setVisible(true);
    }
}
