package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import interfaz.VentanaInicio;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class VentanaNuevaOlimpiada extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel olympCode;
    JLabel olympName;
    JLabel olympDesc;
    JLabel olympYear;
    JTextField olympCodeField;
    JTextField olympNameField;
    JTextField olympDescField;
    JTextField olympYearField;
    JButton createOlympButton;
    JPanel upperPanel;
    JPanel inputPanel;

    public VentanaNuevaOlimpiada(Administrador administrador) {
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Nueva olimpiada");
        this.setVisible(true);
        setLocationRelativeTo(null);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Nueva olimpiada");
        introduceData.setFont(fuenteNegrita1);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        olympCode = new JLabel("Código");
        olympCode.setFont(fuenteNegrita3);
        olympName = new JLabel("Nombre");
        olympName.setFont(fuenteNegrita3);
        olympDesc = new JLabel("Descripción");
        olympDesc.setFont(fuenteNegrita3);
        olympYear = new JLabel("Año");
        olympYear.setFont(fuenteNegrita3);

        olympCodeField = new JTextField();
        olympCodeField.setFont(fuenteNegrita2);
        olympNameField = new JTextField();
        olympNameField.setFont(fuenteNegrita2);
        olympDescField = new JTextField();
        olympDescField.setFont(fuenteNegrita2);
        olympYearField = new JTextField();
        olympYearField.setFont(fuenteNegrita2);

        createOlympButton = new JButton("Crear olimpiada");
        createOlympButton.setFont(fuenteNegrita3);
        createOlympButton.setPreferredSize(new Dimension(150, 30));

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(createOlympButton);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(borde);

        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(olympName);
        inputPanel.add(olympNameField);
        inputPanel.add(olympDesc);
        inputPanel.add(olympDescField);
        inputPanel.add(olympYear);
        inputPanel.add(olympYearField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                dispose();
            }
        });

        createOlympButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = olympCodeField.getText();
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                int year = Integer.parseInt(olympYearField.getText());
                try {
                    administrador.createOlympiad(code, name, desc, year);
                    VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                    dispose();
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
