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

public class VentanaEditarEjercicio extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel exerCode;
    JLabel exerName;
    JLabel exerDesc;
    JLabel exerConcept;
    JLabel exerResources;
    JLabel exerType;
    JLabel exerRubrica;
    JTextField exerCodeField;
    JTextField exerNameField;
    JTextField exerDescField;
    JComboBox<String> exerConceptField;
    JComboBox<String> exerResourcesField;
    JComboBox<String> exerTypeField;
    JComboBox<String> exerRubricaField;
    JButton editExerButton;
    JPanel inputPanel;
    JPanel upperPanel;

    public VentanaEditarEjercicio(Administrador administrador, String codigo, String titulo, String desc, String concepto, String recurso, String tipo) {
        setSize(500, 475);
        getContentPane().setLayout(new BorderLayout(5, 5));
        this.setTitle("Editar ejercicio olímpico");
        this.setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String oldCode = codigo;

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
        Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        introduceData = new JLabel("Edición de ejercicio");
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

        exerRubrica = new JLabel("Rúbrica (*)");
        exerRubrica.setFont(fuenteNegrita3);

        exerCodeField = new JTextField(codigo);
        exerCodeField.setFont(fuenteNegrita2);

        exerNameField = new JTextField(titulo);
        exerNameField.setFont(fuenteNegrita2);

        exerDescField = new JTextField(desc);
        exerDescField.setFont(fuenteNegrita2);

        String[] exerConcepts = {"Abstracción", "Algoritmos", "Bucles", "Condicionales", "Descomposición", "Funciones",
                "IA", "Reconocimiento de patrones", "Secuencias", "Secuencias y bucles", "Variables", "Variables y funciones", "Otro"};

        exerConceptField = new JComboBox<>(exerConcepts);
        exerConceptField.setFont(fuenteNegrita2);
        if (!concepto.equals("IA")) {
            String substring = concepto.substring(1);
            substring = substring.toLowerCase();
            concepto = concepto.charAt(0) + substring;
        }
        exerConceptField.setSelectedItem(concepto);

        String[] exerResource = {"INICIAL", "INTERMEDIO"};
        exerResourcesField = new JComboBox<>(exerResource);
        exerResourcesField.setFont(fuenteNegrita2);
        exerResourcesField.setSelectedItem(recurso);

        String[] exerTypes = {"Desenchufada", "Enchufada"};
        exerTypeField = new JComboBox<>(exerTypes);
        exerTypeField.setFont(fuenteNegrita2);

        String substring = tipo.substring(1);
        substring = substring.toLowerCase();
        tipo = tipo.charAt(0) + substring;

        exerTypeField.setSelectedItem(tipo);

        ArrayList<String> rubricas = new ArrayList<>();
        exerRubricaField = new JComboBox<>();
        exerRubricaField.setFont(fuenteNegrita2);


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
        String sql = "SELECT CODIGO FROM T_RUBRICAS";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Iterar sobre el resultado y añadir los registros al ArrayList
            while (rs.next()) {
                String registro = rs.getString("CODIGO");
                rubricas.add(registro);
            }

            // Utilizamos los años para meterlos en el combo box
            for (int i = 0; i < rubricas.size(); ++i) {
                exerRubricaField.addItem(rubricas.get(i));
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

        editExerButton = new JButton("Modificar ejercicio olímpico");
        editExerButton.setPreferredSize(new Dimension(250, 30));
        editExerButton.setFont(fuenteNegrita3);

        JPanel createButtonPanel = new JPanel();
        createButtonPanel.setBorder(borde);
        createButtonPanel.add(editExerButton);

        inputPanel = new JPanel();
        inputPanel.setBorder(borde);
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10));

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
        inputPanel.add(exerRubrica);
        inputPanel.add(exerRubricaField);

        add(upperPanel, BorderLayout.NORTH);
        add(inputPanel, BorderLayout.CENTER);
        add(createButtonPanel, BorderLayout.SOUTH);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    VentanaConsultaEjercicios ventana = new VentanaConsultaEjercicios(administrador);
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
                dispose();
            }
        });

        editExerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String code = exerCodeField.getText();
                String name = exerNameField.getText();
                String desc = exerDescField.getText();
                String concept = String.valueOf(exerConceptField.getSelectedItem());
                String resources = String.valueOf(exerResourcesField.getSelectedItem());
                String type = String.valueOf(exerTypeField.getSelectedItem());
                String rubrica = String.valueOf(exerRubricaField.getSelectedItem());
                try {
                    administrador.modifyExercise(oldCode, code, name, desc, concept, resources, type, rubrica);
                    VentanaConsultaEjercicios ventana = new VentanaConsultaEjercicios(administrador);
                    dispose();
                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

    }
}
