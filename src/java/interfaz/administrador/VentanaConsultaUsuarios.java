package interfaz.administrador;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import usuarios.Administrador;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class VentanaConsultaUsuarios extends JFrame implements Bordes, Fuentes, Iconos, MouseListener {
    // Panel superior (título y botón de volver)
    JPanel upperPanel;
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Título de ventana
    JLabel consultaItinerarios;
    // Botón de volver
    JButton goBackButton;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    Administrador administrador;

    // Constructor
    public VentanaConsultaUsuarios(Administrador administrador) throws JSchException, SQLException {
        // Configuración de ventana
        setSize(950, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Consulta de usuarios");
        setLocationRelativeTo(null);

        this.administrador = administrador;

        setIconImage(iconoVentana);

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Definición de etiqueta de título
        consultaItinerarios = new JLabel("Consulta de tabla T_USUARIOS");
        consultaItinerarios.setFont(fuenteTitulo);

        // Configuración de panel superior
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(consultaItinerarios, BorderLayout.CENTER);
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

        String consulta = "SELECT * FROM T_USUARIOS ORDER BY NOMBRE ASC;";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(consulta);

        ResultSetMetaData metaData = rs.getMetaData();
        int numeroColumnas = metaData.getColumnCount();

        // Se insertan las filas traídas de la MV en la tabla de la ventana
        for (int i = 1; i <= numeroColumnas; ++i) {
            modeloTabla.addColumn(metaData.getColumnName(i));
        }

        modeloTabla.addColumn(""); // Columna de editar
        modeloTabla.addColumn(""); // Columna de duplicar
        modeloTabla.addColumn(""); // Columna de eliminar

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
        tabla.getTableHeader().setFont(fuenteBotonesEtiquetas);

        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setFont(fuenteCampoTexto);
                return this;
            }
        };

        for (int i = 0; i < tabla.getColumnCount(); ++i) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }

        // Altura de la fila
        tabla.setRowHeight(35);

        // Esto es para que se pueda pulsar los botones
        tabla.addMouseListener(this);

        // Esto es para insertar los botones en la última columna de la tabla (cambiar por tres columnas distintas)
        // Columna de editar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setCellRenderer(new VentanaConsultaUsuarios.ButtonPanelRenderer(3));
        // Columna de duplicar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setCellRenderer(new VentanaConsultaUsuarios.ButtonPanelRenderer(2));
        // Columna de eliminar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new VentanaConsultaUsuarios.ButtonPanelRenderer(1));

        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setMaxWidth(30);

        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMaxWidth(30);

        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

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

        String nombre = (String) modeloTabla.getValueAt(row, 0);
        String password = (String) modeloTabla.getValueAt(row, 1);
        String type = (String) modeloTabla.getValueAt(row, 2);

        if (columna == tabla.getColumnCount() - 3) {
            new VentanaModificarUsuario(administrador, nombre, password, type);
            dispose();
        } else if (columna == tabla.getColumnCount() - 2) {
            nombre = "Copia de " + modeloTabla.getValueAt(row, 0);
            try {
                administrador.createUser(nombre, password, type);
                new VentanaConsultaUsuarios(administrador);
                dispose();
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (administrador.deleteUser(nombre) == 0) {
                    new CustomJOptionPane("Se ha eliminado el usuario");
                    new VentanaConsultaUsuarios(administrador);
                    dispose();
                }
            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");
            }
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
        private JButton actionButton;
        private Image image;
        private ImageIcon buttonIcon;

        public ButtonPanelRenderer(int columna) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            switch(columna) {
                case 3:
                    image = iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    break;
                case 2:
                    image = iconoDuplicar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    break;
                case 1:
                    image = iconoEliminar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                    break;
                default:
                    break;
            }

            buttonIcon = new ImageIcon(image);
            actionButton = new JButton(buttonIcon);
            actionButton.setPreferredSize(new Dimension(25, 25));

            // Se añaden estos botones al modelo de tabla
            add(actionButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
            return this;
        }
    }
}
