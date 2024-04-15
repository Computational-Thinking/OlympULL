package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class VentanaEditarOlimpiada extends JFrame {
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
    JButton modifyOlympButton;
    JPanel upperPanel;
    JPanel inputPanel;
    String oldCode;

    public VentanaEditarOlimpiada(Administrador administrador, String codigo, String titulo, String descripcion, int year) {
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Modificar olimpiada");
        this.setVisible(true);
        setLocationRelativeTo(null);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Modificar olimpiada");
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

        olympCodeField = new JTextField(codigo);
        olympCodeField.setFont(fuenteNegrita2);
        olympNameField = new JTextField(titulo);
        olympNameField.setFont(fuenteNegrita2);
        olympDescField = new JTextField(descripcion);
        olympDescField.setFont(fuenteNegrita2);
        olympYearField = new JTextField(Integer.toString(year));
        olympYearField.setFont(fuenteNegrita2);

        oldCode = olympCodeField.getText();

        modifyOlympButton = new JButton("Modificar olimpiada");
        modifyOlympButton.setFont(fuenteNegrita3);
        modifyOlympButton.setPreferredSize(new Dimension(175, 30));

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(modifyOlympButton);

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
                try {
                    VentanaConsultaOlimpiadas ventana = new VentanaConsultaOlimpiadas(administrador);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        modifyOlympButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = olympCodeField.getText();
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                int year = Integer.parseInt(olympYearField.getText());
                try {
                    administrador.modifyOlympiad(oldCode, code, name, desc, year);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    VentanaConsultaOlimpiadas ventana = new VentanaConsultaOlimpiadas(administrador);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });
    }
}
