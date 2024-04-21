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
import java.util.Arrays;

public class VentanaConsultaEquipos extends JFrame implements Bordes, Fuentes, Iconos, MouseListener {
    // Panel superior (título y botón de volver)
    JPanel upperPanel;
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Título de ventana
    JLabel consultaRubricas;
    // Botón de volver
    JButton goBackButton;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    Administrador administrador;

    // Constructor
    public VentanaConsultaEquipos(Administrador administrador) throws JSchException, SQLException {
        // Configuración de ventana
        setSize(750, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Consulta de equipos");
        setIconImage(iconoVentana);
        setLocationRelativeTo(null);

        this.administrador = administrador;

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Definición de etiqueta de título
        consultaRubricas = new JLabel("Consulta de tabla T_EQUIPOS");
        consultaRubricas.setFont(fuenteSubtitulo);

        // Configuración de panel superior
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(consultaRubricas, BorderLayout.CENTER);
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

        ResultSet tableContent = administrador.selectRows("T_EQUIPOS", "CODIGO");
        ResultSetMetaData data = tableContent.getMetaData();

        // Se insertan las filas traídas de la MV en la tabla de la ventana
        for (int i = 1; i <= 4; ++i) {
            modeloTabla.addColumn(data.getColumnName(i));
        }

        modeloTabla.addColumn(""); // Columna de editar
        modeloTabla.addColumn(""); // Columna de duplicar
        modeloTabla.addColumn(""); // Columna de eliminar

        while (tableContent.next()) {
            Object[] fila = new Object[4];
            for (int i = 1; i <= 4; ++i) {
                fila [i - 1] = tableContent.getObject(i);
            }
            modeloTabla.addRow(fila);
        }

        tableContent.close();

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
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setCellRenderer(new ButtonPanelRenderer(3));
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 3).setMaxWidth(30);

        // Columna de duplicar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setCellRenderer(new ButtonPanelRenderer(2));
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMaxWidth(30);

        // Columna de eliminar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new ButtonPanelRenderer(1));
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

        // Se añaden los paneles a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        // Acción del botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaAdministrador(administrador);
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

        String codigo = (String) modeloTabla.getValueAt(row, 0);
        String name = (String) modeloTabla.getValueAt(row, 1);
        String school = (String) modeloTabla.getValueAt(row, 2);
        String itinerario = (String) modeloTabla.getValueAt(row, 3);

        if (columna == tabla.getColumnCount() - 3) {
            try {
                new VentanaModificarEquipo(administrador, codigo, name, school, itinerario);

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");

            }
            dispose();

        } else if (columna == tabla.getColumnCount() - 2) {
            codigo = "Copia de " + codigo;
            name = "Copia de " + name;

            try {
                administrador.createTeam(codigo, name, school, itinerario);
                new VentanaConsultaEquipos(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR");

            }

        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (administrador.deleteTeam(codigo) == 0) {
                    new CustomJOptionPane("Se ha eliminado el equipo");
                    new VentanaConsultaEquipos(administrador);
                    dispose();
                }

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

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

            switch (columna) {
                case 3 -> image = iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                case 2 -> image = iconoDuplicar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                case 1 -> image = iconoEliminar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                default -> {
                }
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
