package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.security.DigestException;
import java.sql.*;

public class VentanaConsultaOlimpiadas extends JFrame implements MouseListener {
    // Panel superior (título y botón de volver)
    JPanel upperPanel;
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Título de ventana
    JLabel consultaOlimpiadas;
    // Botón de volver
    JButton goBackButton;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;

    // Constructor
    public VentanaConsultaOlimpiadas(Administrador administrador) throws JSchException, SQLException {
        // Configuración de ventana
        setSize(750, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Consulta de olimpiadas");
        setLocationRelativeTo(null);

        Image icon = new ImageIcon("images/icono-ull-original.png").getImage();
        setIconImage(icon);

        // Declaración de fuentes de texto y borde
        Border borde = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        Font fuenteNegrita1 = new Font("Argentum Sans Light", Font.PLAIN, 12);
        Font fuenteNegrita2 = new Font("Argentum Sans Bold", Font.PLAIN, 18);
        Font fuenteNegrita3 = new Font("Argentum Sans Bold", Font.PLAIN, 12);

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteNegrita3);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Definición de etiqueta de título
        consultaOlimpiadas = new JLabel("Consulta de tabla T_OLIMPIADAS");
        consultaOlimpiadas.setFont(fuenteNegrita2);

        // Configuración de panel superior
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(consultaOlimpiadas, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);

        // Definición del modelo de tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos la tabla con el modelo
        tabla = new JTable(modeloTabla);

        tablaScrollPane = new JScrollPane(tabla);

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

        // Se insertan las filas traídas de la MV en la tabla de la ventana
        for (int i = 1; i <= numeroColumnas; ++i) {
            modeloTabla.addColumn(metaData.getColumnName(i));
        }

        modeloTabla.addColumn(""); // Columna de acciones

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

        // Esto es para establecer la fuente del contenido de la tabla
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

        // Altura de la fila
        tabla.setRowHeight(30);

        // Esto es para que se pueda pulsar los botones
        tabla.addMouseListener(this);

        // Esto es para insertar los botones en la última columna de la tabla (cambiar por tres columnas distintas)
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new ButtonPanelRenderer());

        //tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(150);
        //tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(150);

        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        // Acción del botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAdministrador ventana = new VentanaAdministrador(administrador);
                dispose();
            }
        });

        setVisible(true);
    }

    // Acciones del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());

        if (columna == tabla.getColumnCount() - 1) {
            JOptionPane.showMessageDialog(null, "Prueba");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Esto es para añadir los botones a la última columna de la tabla (no necesario y creo que ni siquiera hace falta hacerlo tan complicado)
    class ButtonPanelRenderer extends JPanel implements TableCellRenderer {
        private JButton duplicateButton;
        private JButton editButton;
        private JButton deleteButton;

        public ButtonPanelRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            Image editarIcon = new ImageIcon("images/edit icon.png").getImage();
            editarIcon = editarIcon.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            ImageIcon editarScalatedIcon = new ImageIcon(editarIcon);

            Image duplicarIcon = new ImageIcon("images/duplicate icon.png").getImage();
            duplicarIcon = duplicarIcon.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            ImageIcon duplicarScalatedIcon = new ImageIcon(duplicarIcon);

            Image eliminarIcon = new ImageIcon("images/delete icon.png").getImage();
            eliminarIcon = eliminarIcon.getScaledInstance(15, 15, Image.SCALE_SMOOTH);
            ImageIcon eliminarScalatedIcon = new ImageIcon(eliminarIcon);

            editButton = new JButton(editarScalatedIcon);
            editButton.setPreferredSize(new Dimension(20, 20));
            duplicateButton = new JButton(duplicarScalatedIcon);
            duplicateButton.setPreferredSize(new Dimension(20, 20));
            deleteButton = new JButton(eliminarScalatedIcon);
            deleteButton.setPreferredSize(new Dimension(20, 20));

            // Se añaden las acciones que realiza cada botón
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Se abre ventana para crear olimpiada, pero con los datos precargados
                }
            });

            duplicateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Se crea una nueva olimpiada con los mismos valores de la olimpiada seleccionada, pero con la cadena "Copia de" al comienzo del código
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // JOptionPane para solicitar confirmación de eliminar olimpiada
                    JOptionPane.showConfirmDialog(null, "¿Está seguro de querer eliminar esta olimpiada?");
                }
            });

            // Se añaden estos botones al modelo de tabla
            add(editButton);
            add(duplicateButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            return this;
        }
    }

}
