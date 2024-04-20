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

public class VentanaNuevaAsignacionEjOlimp extends JFrame {
    // Botones
    JButton goBackButton;
    JButton assignExercise;
    
    // Etiquetas
    JLabel introduceData;
    JLabel exerCode;
    JLabel olympCode;
    JLabel itinerarioCode;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerRubricaLabel;
    
    // Combo boxes
    JComboBox<String> exerCodeField;
    JComboBox<String> olympCodeField;
    JComboBox<String> itinerarioCodeField;
    
    // Paneles
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaNuevaAsignacionEjOlimp(Administrador administrador) {
        // Configuración de la ventana
        setSize(500, 290);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Nueva asignación");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Icono de la ventana
        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        // Definición de borde de paneles
        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        
        // Definición de fuentes
        Font fuenteTitulo = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteCampoTexto = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteBotonesEtiquetas = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Nueva asignación");
        introduceData.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        exerCode = new JLabel("Ejercicio (*)");
        exerCode.setFont(fuenteBotonesEtiquetas);

        olympCode = new JLabel("Olimpiada (*)");
        olympCode.setFont(fuenteBotonesEtiquetas);

        itinerarioCode = new JLabel("Itinerario (*)");
        itinerarioCode.setFont(fuenteBotonesEtiquetas);

        exerCodeField = new JComboBox<String>();
        exerCodeField.setFont(fuenteCampoTexto);

        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        itinerarioCodeField = new JComboBox<>();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        ArrayList<String> ejercicios = new ArrayList<>();
        exerCodeField = new JComboBox<>();
        exerCodeField.setFont(fuenteCampoTexto);

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
        String sql = "SELECT CODIGO FROM T_EJERCICIOS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("CODIGO");
                ejercicios.add(registro);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < ejercicios.size(); ++i) {
                exerCodeField.addItem(ejercicios.get(i));
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

        ArrayList<String> olimpiadas = new ArrayList<>();
        olympCodeField = new JComboBox<>();
        olympCodeField.setFont(fuenteCampoTexto);

        // Valores para conexión a MV remota
        sshHost = "10.6.130.204";
        sshUser = "usuario";
        sshPassword = "Usuario";

        // Conexión SSH a la MV remota
        jsch = new JSch();
        session = null;
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
        dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        dbUser = "root";
        dbPassword = "root";
        try {
            conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Ejecutar consulta para añadir nuevo ejercicio
        sql = "SELECT CODIGO FROM T_OLIMPIADAS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("CODIGO");
                olimpiadas.add(registro);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < olimpiadas.size(); ++i) {
                olympCodeField.addItem(olimpiadas.get(i));
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

        ArrayList<String> itinerarios = new ArrayList<>();
        itinerarioCodeField = new JComboBox<>();
        itinerarioCodeField.setFont(fuenteCampoTexto);

        assignExercise = new JButton("Asignar");
        assignExercise.setPreferredSize(new Dimension(100, 30));
        assignExercise.setFont(fuenteBotonesEtiquetas);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(assignExercise);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(3, 2, 10, 10));

        inputPanel.add(exerCode);
        inputPanel.add(exerCodeField);
        inputPanel.add(olympCode);
        inputPanel.add(olympCodeField);
        inputPanel.add(itinerarioCode);
        inputPanel.add(itinerarioCodeField);

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

        olympCodeField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                itinerarioCodeField.removeAllItems();

                // Valores para conexión a MV remota
                String sshHost = "10.6.130.204";
                String sshUser = "usuario";
                String sshPassword = "Usuario";

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
                String sql = "SELECT CODIGO FROM T_ITINERARIOS WHERE OLIMPIADA='" + olympCodeField.getSelectedItem() + "';";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    // Iterar sobre el resultado y añadir los registros al ArrayList
                    while (rs.next()) {
                        String registro = rs.getString("CODIGO");
                        itinerarios.add(registro);
                    }

                    // Utilizamos los años para meterlos en el combo box
                    for (int i = 0; i < itinerarios.size(); ++i) {
                        itinerarioCodeField.addItem(itinerarios.get(i));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                try {
                    conn.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                session.disconnect();
            }
        });

        assignExercise.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String exercise = (String) exerCodeField.getSelectedItem();
                String olympiad = (String) olympCodeField.getSelectedItem();
                String itinerario = (String) itinerarioCodeField.getSelectedItem();
                administrador.assignExerciseToOlympiad(exercise, olympiad, itinerario);
                new VentanaAdministrador(administrador);
                dispose();
            }
        });

    }
}
