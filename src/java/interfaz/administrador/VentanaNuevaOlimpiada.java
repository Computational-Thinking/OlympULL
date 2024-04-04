package interfaz.administrador;

import com.jcraft.jsch.JSchException;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class VentanaNuevaOlimpiada extends JFrame {
    JLabel olympName;
    JLabel olympDesc;
    JLabel olympYear;
    JLabel olympEnMentions;
    JLabel olympEnTeams;
    JLabel olympDesMentions;
    JLabel olympDesTeams;
    JTextField olympNameField;
    JTextField olympDescField;
    JTextField olympYearField;
    JTextField olympEnMentionsField;
    JTextField olympEnTeamsField;
    JTextField olympDesMentionsField;
    JTextField olympDesTeamsField;
    JButton createOlympButton;
    JPanel inputPanel;

    public VentanaNuevaOlimpiada(Administrador administrador) {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Nueva olimpiada");
        this.setVisible(true);

        olympName = new JLabel("Nombre de la olimpiada");
        olympDesc = new JLabel("Descripción");
        olympYear = new JLabel("Año");
        olympEnMentions = new JLabel("Nº de menciones de activiades enchufadas");
        olympEnTeams = new JLabel("Nº de equipos de activiades enchufadas");
        olympDesMentions = new JLabel("Nº de menciones de activiades desenchufadas");
        olympDesTeams = new JLabel("Nº de equipos de activiades desenchufadas");

        olympNameField = new JTextField();
        olympDescField = new JTextField();
        olympYearField = new JTextField();
        olympEnMentionsField = new JTextField();
        olympEnTeamsField = new JTextField();
        olympDesMentionsField = new JTextField();
        olympDesTeamsField = new JTextField();

        createOlympButton = new JButton("Crear olimpiada");

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2));

        inputPanel.add(olympName);
        inputPanel.add(olympNameField);
        inputPanel.add(olympDesc);
        inputPanel.add(olympDescField);
        inputPanel.add(olympYear);
        inputPanel.add(olympYearField);
        inputPanel.add(olympEnMentions);
        inputPanel.add(olympEnMentionsField);
        inputPanel.add(olympEnTeams);
        inputPanel.add(olympEnTeamsField);
        inputPanel.add(olympDesMentions);
        inputPanel.add(olympDesMentionsField);
        inputPanel.add(olympDesTeams);
        inputPanel.add(olympDesTeamsField);

        add(inputPanel);
        add(createOlympButton);

        createOlympButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = olympNameField.getText();
                String desc = olympDescField.getText();
                int year = Integer.parseInt(olympYearField.getText());
                int enMentions = Integer.parseInt(olympEnMentionsField.getText());
                int enTeams = Integer.parseInt(olympEnTeamsField.getText());
                int desMentions = Integer.parseInt(olympDesMentionsField.getText());
                int desTeams = Integer.parseInt(olympDesTeamsField.getText());
                try {
                    administrador.createOlympiad(name, desc, year, desMentions, desTeams, enMentions, enTeams);
                    setVisible(false);
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
