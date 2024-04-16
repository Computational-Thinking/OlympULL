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
import java.util.Objects;

public class VentanaEditarItinerario extends JFrame {
    JButton goBackButton;
    JLabel introduceData;
    JLabel codigoItinerario;
    JLabel nombreItinerario;
    JLabel descripcionItinerario;
    JLabel olimpiadaItinerario;
    JTextField campoCodigoItinerario;
    JTextField campoNombreItinerario;
    JTextField campoDescripcionItinerario;
    JComboBox<String> campoOlimpiadaItinerario;
    JButton botonEditarItinerario;
    JPanel upperPanel;
    JPanel inputPanel;
    String oldCode;

    public VentanaEditarItinerario(Administrador administrador, String codigo, String titulo, String descripcion, String olimpiada) {
            setSize(500, 335);
            getContentPane().setLayout(new BorderLayout(5, 5));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setTitle("Nuevo itinerario");
            this.setVisible(true);
            setLocationRelativeTo(null);

            oldCode = codigo;

            Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
            setIconImage(icon);

            Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
            Font fuenteNegrita1 = new Font("Argentum Sans Bold", Font.PLAIN, 20);
            Font fuenteNegrita2 = new Font("Argentum Sans Light", Font.PLAIN, 12);
            Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

            introduceData = new JLabel("Nuevo itinerario");
            introduceData.setFont(fuenteNegrita1);

            goBackButton = new JButton("< Volver");
            goBackButton.setFont(fuenteNegrita3);
            goBackButton.setPreferredSize(new Dimension(90, 30));

            upperPanel = new JPanel();
            upperPanel.setLayout(new BorderLayout(5, 5));
            upperPanel.add(introduceData, BorderLayout.CENTER);
            upperPanel.add(goBackButton, BorderLayout.EAST);
            upperPanel.setBorder(borde);

            codigoItinerario = new JLabel("Código (*)");
            codigoItinerario.setFont(fuenteNegrita3);
            nombreItinerario = new JLabel("Nombre (*)");
            nombreItinerario.setFont(fuenteNegrita3);
            descripcionItinerario = new JLabel("Descripción");
            descripcionItinerario.setFont(fuenteNegrita3);
            olimpiadaItinerario = new JLabel("Olimpiada (*)");
            olimpiadaItinerario.setFont(fuenteNegrita3);

            campoCodigoItinerario = new JTextField(codigo);
            campoCodigoItinerario.setFont(fuenteNegrita2);

            campoNombreItinerario = new JTextField(titulo);
            campoNombreItinerario.setFont(fuenteNegrita2);

            campoDescripcionItinerario = new JTextField(descripcion);
            campoDescripcionItinerario.setFont(fuenteNegrita2);

            ArrayList<String> codigosOlimpiadas = new ArrayList<>();
            campoOlimpiadaItinerario = new JComboBox<String>();
            campoOlimpiadaItinerario.setFont(fuenteNegrita2);

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
                    codigosOlimpiadas.add(registro);
                }

                // Utilizamos los años para meterlos en el combo box
                for (int i = 0; i < codigosOlimpiadas.size(); ++i) {
                    campoOlimpiadaItinerario.addItem(codigosOlimpiadas.get(i));
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

            campoOlimpiadaItinerario.setSelectedItem(olimpiada);

            botonEditarItinerario = new JButton("Editar itinerario");
            botonEditarItinerario.setFont(fuenteNegrita3);
            botonEditarItinerario.setPreferredSize(new Dimension(150, 30));

            JPanel createButtonPanel = new JPanel();
            createButtonPanel.setBorder(borde);
            createButtonPanel.add(botonEditarItinerario);

            inputPanel = new JPanel();
            inputPanel.setLayout(new GridLayout(4, 2, 10, 10));
            inputPanel.setBorder(borde);

            inputPanel.add(codigoItinerario);
            inputPanel.add(campoCodigoItinerario);
            inputPanel.add(nombreItinerario);
            inputPanel.add(campoNombreItinerario);
            inputPanel.add(descripcionItinerario);
            inputPanel.add(campoDescripcionItinerario);
            inputPanel.add(olimpiadaItinerario);
            inputPanel.add(campoOlimpiadaItinerario);

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

            botonEditarItinerario.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String code = campoCodigoItinerario.getText();
                    String name = campoNombreItinerario.getText();
                    String desc = campoDescripcionItinerario.getText();
                    String olymp = (String) Objects.requireNonNull(campoOlimpiadaItinerario.getSelectedItem());
                    try {
                        administrador.modifyItinerario(oldCode, code, name, desc, olymp);
                        VentanaConsultaItinerarios ventana = new VentanaConsultaItinerarios(administrador);
                        dispose();
                    } catch (JSchException | SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
        }

    }


