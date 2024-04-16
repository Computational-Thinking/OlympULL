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

public class VentanaNuevoEjercicio extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel exerCode;
    JLabel exerName;
    JLabel exerDesc;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerOlymp;
    JLabel exerItinerario;
    JTextField exerCodeField;
    JTextField exerNameField;
    JTextField exerDescField;
    JComboBox<String> exerConceptField;
    JComboBox<String> exerResourcesField;
    JComboBox<String> exerTypeField;
    JComboBox<String> exerOlympField;
    JComboBox<String> exerItinerarioField;
    JButton createExerButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaNuevoEjercicio(Administrador administrador) {
        setSize(500, 425);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Nuevo ejercicio olímpico");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Nuevo ejercicio");
        introduceData.setFont(fuenteNegrita1);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(introduceData, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.setBorder(borde);

        exerCode = new JLabel("Código (*)");
        exerCode.setFont(fuenteNegrita3);

        exerName = new JLabel("Nombre (*)");
        exerName.setFont(fuenteNegrita3);

        exerDesc = new JLabel("Descripción");
        exerDesc.setFont(fuenteNegrita3);

        exerConcept = new JLabel("Categoría (*)");
        exerConcept.setFont(fuenteNegrita3);

        exerResources = new JLabel("Recursos (*)");
        exerResources.setFont(fuenteNegrita3);

        exerType = new JLabel("Tipo (*)");
        exerType.setFont(fuenteNegrita3);

        exerOlymp = new JLabel("Olimpiada (*)");
        exerOlymp.setFont(fuenteNegrita3);

        exerItinerario = new JLabel("Itinerario (*)");
        exerItinerario.setFont(fuenteNegrita3);


        exerCodeField = new JTextField();
        exerCodeField.setFont(fuenteNegrita2);

        exerNameField = new JTextField();
        exerNameField.setFont(fuenteNegrita2);

        exerDescField = new JTextField();
        exerDescField.setFont(fuenteNegrita2);

        String[] exerConcepts = {"Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables", "Variables y funciones"};

        exerConceptField = new JComboBox<>(exerConcepts);
        exerConceptField.setFont(fuenteNegrita2);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);
        exerResourcesField.setFont(fuenteNegrita2);

        String[] exerTypes = {"Enchufada", "Desenchufada"};
        exerTypeField = new JComboBox<>(exerTypes);
        exerTypeField.setFont(fuenteNegrita2);

        exerOlympField = new JComboBox<>();
        exerOlympField.setFont(fuenteNegrita2);

        ArrayList<String> olimpiadas = new ArrayList<>();

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
        String sql = "SELECT CODIGO FROM T_OLIMPIADAS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("CODIGO");
                olimpiadas.add(registro);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < olimpiadas.size(); ++i) {
                exerOlympField.addItem(olimpiadas.get(i));
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

        exerItinerarioField = new JComboBox<>();
        exerItinerarioField.setFont(fuenteNegrita2);

        createExerButton = new JButton("Crear ejercicio olímpico");
        createExerButton.setPreferredSize(new Dimension(200, 30));
        createExerButton.setFont(fuenteNegrita3);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(createExerButton);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(8, 2));

        inputPanel.add(exerCode);
        inputPanel.add(exerCodeField);
        inputPanel.add(exerName);
        inputPanel.add(exerNameField);
        inputPanel.add(exerDesc);
        inputPanel.add(exerDescField);
        inputPanel.add(exerConcept);
        inputPanel.add(exerConceptField);
        inputPanel.add(exerResources);
        inputPanel.add(exerResourcesField);
        inputPanel.add(exerType);
        inputPanel.add(exerTypeField);
        inputPanel.add(exerOlymp);
        inputPanel.add(exerOlympField);
        inputPanel.add(exerItinerario);
        inputPanel.add(exerItinerarioField);

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


        exerOlympField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exerItinerarioField.removeAllItems();

                ArrayList<String> itinerarios = new ArrayList<>();

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
                String sql = "SELECT CODIGO FROM T_ITINERARIOS WHERE OLIMPIADA='" + exerOlympField.getSelectedItem() + "';";

                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    // Iterar sobre el resultado y añadir los registros al ArrayList
                    while (rs.next()) {
                        String registro = rs.getString("CODIGO");
                        itinerarios.add(registro);
                    }

                    // Utilizamos los años para meterlos en el combo box
                    for (int i = 0; i < itinerarios.size(); ++i) {
                        exerItinerarioField.addItem(itinerarios.get(i));
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

        createExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = exerCodeField.getText();
                String name = exerNameField.getText();
                String desc = exerDescField.getText();
                String concept = String.valueOf(exerConceptField.getSelectedItem());
                String resources = String.valueOf(exerResourcesField.getSelectedItem());
                String type = String.valueOf(exerTypeField.getSelectedItem());
                String olymp = String.valueOf(exerOlympField.getSelectedItem());
                String itinerario = String.valueOf(exerItinerarioField.getSelectedItem());

                try {
                    administrador.createExercise(code, name, desc, concept, resources, type, olymp, itinerario);
                    VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                    dispose();
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
}
