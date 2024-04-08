package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import usuarios.Monitor;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

public class VentanaPuntuarEjercicio extends JFrame {
    JLabel teamSelectionLabel;
    JComboBox<String> teamSelectionComboBox;
    JLabel punctuationSelectionLabel;
    JPanel punctuationsPanel;
    JButton punctuateButton;

    public VentanaPuntuarEjercicio(Monitor monitor) {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Puntuar equipo");
        this.setVisible(true);

        teamSelectionLabel = new JLabel("Selecciona el equipo al que quieres puntuar:");

        teamSelectionComboBox = new JComboBox<>();

        punctuationSelectionLabel = new JLabel("Selecciona la puntuación que quieres darle al equipo");

        punctuationsPanel = new JPanel();

        punctuateButton = new JButton("Puntuar");

        ArrayList<String> teams = new ArrayList<>();

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
        String teamNames = "SELECT NOMBRE FROM T_EQUIPOS WHERE MENCION=(SELECT CODIGO_MENCION FROM T_EJERCICIOS_MENCIONES WHERE CODIGO_EJERCICIO=" + monitor.getExerciseCode() + ");";
        String scaleValues = "SELECT ETIQUETAS_BAREMO, VALORES_BAREMO FROM T_BAREMOS WHERE CODIGO_EJERCICIO=" + monitor.getExerciseCode() + ";";

        // Almacenar nombres de los equipos en combo box
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(teamNames)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String team = (rs.getString("NOMBRE"));
                teams.add(team);
            }

            // Utilizamos los años para meterlos en el combo box
            for (String team : teams) {
                teamSelectionComboBox.addItem(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Recoger valores del baremo
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(scaleValues)) {

            if (rs.next()) {
                String values = rs.getString("VALORES_BAREMO");
                String tags = rs.getString("ETIQUETAS_BAREMO");

                // Se separa por comas
                String[] separatedValues = values.split(",");
                String[] separatedTags = tags.split(",");

                // Array donde se va a almacenar la escala
                ArrayList<String> scale = new ArrayList<>();

                // Utilizamos los años para meterlos en el combo box
                for (int i = 0; i < separatedValues.length; ++i) {
                    scale.add(separatedValues[i] + " - " + separatedTags[i]);
                }

                for (int i = 0; i < scale.size(); ++i) {
                    JRadioButton rButton = new JRadioButton(scale.get(i), false);
                    punctuationsPanel.add(rButton);
                }

            } else {
                JOptionPane.showMessageDialog(null, "No se ha establecido el baremo para este ejercicio. Hágalo y vuelva a intentarlo.");
                dispose();
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

        add(teamSelectionLabel);
        add(teamSelectionComboBox);
        add(punctuationSelectionLabel);
        add(punctuationsPanel);
        add(punctuateButton);
    }


}
