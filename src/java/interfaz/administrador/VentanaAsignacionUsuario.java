package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import usuarios.Administrador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;

public class VentanaAsignacionUsuario extends JFrame {
    JLabel yearLabel;
    JComboBox<Integer> yearComboBox;
    JLabel usuarioLabel;
    JLabel tituloEjercicioLabel;
    JComboBox<String> usuarioComboBox;
    JComboBox<String> tituloEjercicioComboBox;
    JButton okButton;
    JButton asignarEjercicioAUsuario;
    JPanel yearSelectionPanel;
    JPanel inputPanel;

    public VentanaAsignacionUsuario(Administrador administrador) {
        setSize(500, 250);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        this.setTitle("Asignar ejercicio a monitor");
        this.setVisible(true);

        yearLabel = new JLabel("Seleccione la olimpiada a la que pertenece el ejercicio que quiere asignar: ");
        yearLabel.setPreferredSize(new Dimension(200, 30));
        ArrayList<Integer> years = new ArrayList<>();
        yearComboBox = new JComboBox<>();
        yearComboBox.setPreferredSize(new Dimension(200, 30));
        okButton = new JButton("OK");
        okButton.setPreferredSize(new Dimension(200, 30));
        asignarEjercicioAUsuario = new JButton("Asignar ejercicio a usuario");
        yearSelectionPanel = new JPanel();
        yearSelectionPanel.setLayout(new GridLayout(3, 1));
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

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
        String sql = "SELECT YEAR FROM T_OLIMPIADAS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("YEAR");
                int year = Integer.parseInt(registro);
                years.add(year);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < years.size(); ++i) {
                yearComboBox.addItem(years.get(i));
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

        yearSelectionPanel.add(yearLabel);
        yearSelectionPanel.add(yearComboBox);
        yearSelectionPanel.add(okButton);
        add(yearSelectionPanel);

        usuarioLabel = new JLabel("Monitor");
        usuarioLabel.setPreferredSize(new Dimension(200, 30));

        tituloEjercicioLabel = new JLabel("Ejercicio");
        tituloEjercicioLabel.setPreferredSize(new Dimension(200, 30));

        usuarioComboBox = new JComboBox<>();
        tituloEjercicioComboBox = new JComboBox<>();

        ArrayList<Integer> codigos = new ArrayList<>();

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }
                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                try {
                    session.connect();
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }

                // Debugger
                System.out.println("Conexión con la máquina establecida");

                // Abrir un túnel SSH al puerto MySQL en la máquina remota
                try {
                    session.setPortForwardingL(localPort, remoteHost, remotePort);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }

                // Conexión a MySQL a través del túnel SSH
                String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
                String dbUser = "root";
                String dbPassword = "root";
                Connection conn;
                try {
                    conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // Ejecutar consulta para añadir nuevo ejercicio
                String sql = "SELECT * FROM T_EJERCICIOS WHERE YEAR = '" + yearComboBox.getSelectedItem() + "';";
                String sql2 = "SELECT * FROM T_USUARIOS WHERE TIPO_USUARIO = 'MONITOR';";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {
                    // Iterar sobre el resultado y añadir los registros al ArrayList
                    while (rs.next()) {
                        int codigo = Integer.parseInt(rs.getString("CODIGO"));
                        codigos.add(codigo);
                        String registro = rs.getString("TITULO");
                        tituloEjercicioComboBox.addItem(registro);
                    }
                } catch (SQLException q) {
                    q.printStackTrace();
                }


                try (Statement stmt = conn.createStatement();
                     ResultSet rs2 = stmt.executeQuery(sql2)) {
                    // Iterar sobre el resultado y añadir los registros al ArrayList
                    while (rs2.next()) {
                        String registro = rs2.getString("NOMBRE_USUARIO");
                        usuarioComboBox.addItem(registro);
                    }
                } catch (SQLException q) {
                    q.printStackTrace();
                }

                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                session.disconnect();

                inputPanel.add(usuarioLabel);
                inputPanel.add(usuarioComboBox);
                inputPanel.add(tituloEjercicioLabel);
                inputPanel.add(tituloEjercicioComboBox);
                inputPanel.add(asignarEjercicioAUsuario);
                add(inputPanel);
            }
        });

        asignarEjercicioAUsuario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }
                session.setPassword(sshPassword);
                session.setConfig("StrictHostKeyChecking", "no");
                try {
                    session.connect();
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }

                // Debugger
                System.out.println("Conexión con la máquina establecida");

                // Abrir un túnel SSH al puerto MySQL en la máquina remota
                try {
                    session.setPortForwardingL(localPort, remoteHost, remotePort);
                } catch (JSchException ex) {
                    throw new RuntimeException(ex);
                }

                // Conexión a MySQL a través del túnel SSH
                String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
                String dbUser = "root";
                String dbPassword = "root";
                Connection conn;
                try {
                    conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // Ejecutar consulta para añadir nuevo ejercicio
                String sql = "INSERT INTO T_MONITORES VALUES('" + usuarioComboBox.getSelectedItem() + "', " + codigos.get(0) + ", '" + tituloEjercicioComboBox.getSelectedItem() + "');";

                Statement stmt = null;
                try {
                    stmt = conn.createStatement();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                int rowsAffected = 0;
                try {
                    rowsAffected = stmt.executeUpdate(sql);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Se ha creado el ejercicio.");
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha podido crear el ejercicio.");
                }


                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                session.disconnect();
            }

        });

    }
}
