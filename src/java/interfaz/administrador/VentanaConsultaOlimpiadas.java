package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VentanaConsultaOlimpiadas extends JFrame {
    JPanel upperPanel;
    JPanel tablePanel;
    JLabel consultaOlimpiadas;
    JButton goBackButton;
    DefaultTableModel modeloTabla;
    JTable tabla;

    public VentanaConsultaOlimpiadas(Administrador administrador) throws JSchException, SQLException {
        setSize(750, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Consulta de olimpiadas");
        setLocationRelativeTo(null);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita2 = new Font("Argentum Sans Bold", Font.PLAIN, 18);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        consultaOlimpiadas = new JLabel("Consulta de tabla T_OLIMPIADAS");
        consultaOlimpiadas.setFont(fuenteNegrita2);

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(consultaOlimpiadas, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);

        JScrollPane tablaScrollPane = new JScrollPane(tabla);

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
        Session session = jsch.getSession(sshUser, sshHost, sshPort);
        session.setPassword(sshPassword);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        // Debugger
        System.out.println("Conexión con la máquina establecida");

        // Abrir un túnel SSH al puerto MySQL en la máquina remota
        session.setPortForwardingL(localPort, remoteHost, remotePort);

        // Conexión a MySQL a través del túnel SSH
        String dbUrl = "jdbc:mysql://localhost:" + localPort + "/OLYMPULL_DB";
        String dbUser = "root";
        String dbPassword = "root";
        Connection conn;
        conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);

        String consulta = "SELECT * FROM T_OLIMPIADAS ORDER BY CODIGO ASC;";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(consulta);

        ResultSetMetaData metaData = rs.getMetaData();
        int numeroColumnas = metaData.getColumnCount();

        for (int i = 1; i <= numeroColumnas; ++i) {
            modeloTabla.addColumn(metaData.getColumnName(i));
        }

        while (rs.next()) {
            Object[] fila = new Object[numeroColumnas];
            for (int i = 1; i <= numeroColumnas; ++i) {
                fila [i - 1] = rs.getObject(i);
            }
            modeloTabla.addRow(fila);
        }

        rs.close();
        stmt.close();
        conn.close();
        session.disconnect();

        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        tabla.getTableHeader().setFont(fuenteNegrita3);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(fuenteNegrita1);
                return this;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); ++i) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                dispose();
            }
        });

        setVisible(true);
    }
}
