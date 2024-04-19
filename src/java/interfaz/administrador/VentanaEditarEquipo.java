package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class VentanaEditarEquipo extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel teamCode;
    JLabel teamName;
    JLabel teamSchool;
    JLabel teamItinerario;
    JTextField teamCodeField;
    JTextField teamNameField;
    JTextField teamSchoolField;
    JComboBox<String> teamItinerarioField;
    JButton modifyTeamButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaEditarEquipo(Administrador administrador, String oldCode, String name, String school, String itinerario) {
        setSize(500, 335);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Editar equipo");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteTitulo = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteCampoTexto = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteBotonEtiqueta = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Editar equipo");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonEtiqueta);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        teamCode = new JLabel("Código (*)");
        teamCode.setFont(fuenteBotonEtiqueta);

        teamName = new JLabel("Nombre (*)");
        teamName.setFont(fuenteBotonEtiqueta);

        teamSchool = new JLabel("Centro educativo (*)");
        teamSchool.setFont(fuenteBotonEtiqueta);

        teamItinerario = new JLabel("Itinerario (*)");
        teamItinerario.setFont(fuenteBotonEtiqueta);

        teamCodeField = new JTextField(oldCode);
        teamCodeField.setFont(fuenteCampoTexto);

        teamNameField = new JTextField(name);
        teamNameField.setFont(fuenteCampoTexto);

        teamSchoolField = new JTextField(school);
        teamSchoolField.setFont(fuenteCampoTexto);

        ArrayList<String> itinerarios = new ArrayList<>();
        teamItinerarioField = new JComboBox<>();
        teamItinerarioField.setFont(fuenteCampoTexto);

        // Valores para conexión a MV remota
        String sshHost = "10.6.130.204";
        String sshUser = "usuario";
        String sshPassword = "Usuario";
        int sshPort = 22; // Puerto SSH por defecto
        int localPort = 3307; // Puerto local para el túnel SSH
        String remoteHost = "localhost"; // La conexión MySQL se hará desde la máquina remota
        int remotePort = 3306; // Puerto MySQL en la máquina remota

        // Conexión SSH a la MV remota
        JSch jsch = new JSch();
        Session session = null;
        try {
            session = jsch.getSession(sshUser, sshHost, sshPort);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        try {
            session.connect();
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        try {
            session.setPortForwardingL(localPort, remoteHost, remotePort);
        } catch (JSchException e) {
            throw new RuntimeException(e);
        }

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ejecutar consulta para añadir nuevo ejercicio
        String sql = "SELECT CODIGO FROM T_ITINERARIOS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("CODIGO");
                itinerarios.add(registro);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < itinerarios.size(); ++i) {
                teamItinerarioField.addItem(itinerarios.get(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        session.disconnect();
        
        teamItinerarioField.setSelectedItem(itinerario);

        modifyTeamButton = new JButton("Modificar equipo");
        modifyTeamButton.setPreferredSize(new Dimension(150, 30));
        modifyTeamButton.setFont(fuenteBotonEtiqueta);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(modifyTeamButton);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(4, 2, 10, 10));

        inputPanel.add(teamCode);
        inputPanel.add(teamCodeField);
        inputPanel.add(teamName);
        inputPanel.add(teamNameField);
        inputPanel.add(teamSchool);
        inputPanel.add(teamSchoolField);
        inputPanel.add(teamItinerario);
        inputPanel.add(teamItinerarioField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

        modifyTeamButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = teamCodeField.getText();
                String name = teamNameField.getText();
                String desc = teamSchoolField.getText();
                String itinerario = String.valueOf(teamItinerarioField.getSelectedItem());
                try {
                    administrador.modifyTeam(oldCode, code, name, desc, itinerario);
                    new VentanaConsultaEquipos(administrador);
                    dispose();
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
}
