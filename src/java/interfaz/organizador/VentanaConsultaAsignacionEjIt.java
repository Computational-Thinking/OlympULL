package interfaz.organizador;

import com.jcraft.jsch.JSchException;
import interfaz.Bordes;
import interfaz.CustomJOptionPane;
import interfaz.Fuentes;
import interfaz.Iconos;
import interfaz.administrador.VentanaAdministrador;
import interfaz.administrador.VentanaModificarAsignacionEjOlimp;
import usuarios.Administrador;
import usuarios.Organizador;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

public class VentanaConsultaAsignacionEjIt extends JFrame implements Bordes, Fuentes, Iconos, MouseListener {
    // Paneles
    JPanel upperPanel;
    JScrollPane tablaScrollPane;

    // Etiquetas
    JLabel consultaItinerarios;

    // Botones
    JButton goBackButton;

    // Modelo de tabla
    DefaultTableModel modeloTabla;

    // Tabla
    JTable tabla;

    // Administrador
    Organizador organizador;

    // Constructor
    public VentanaConsultaAsignacionEjIt(Organizador organizador) throws JSchException, SQLException {
        // Configuración de ventana
        setSize(950, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Consulta de ejercicios");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        this.organizador = organizador;

        // Definición del botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Definición de etiqueta de título
        consultaItinerarios = new JLabel("Consulta de tabla T_EJERCICIOS_OLIMPIADA_ITINERARIO");
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

        ResultSet tableContent = organizador.selectRows("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "EJERCICIO");
        ResultSetMetaData data = tableContent.getMetaData();
        int nCols = data.getColumnCount();

        // Se insertan las filas traídas de la MV en la tabla de la ventana
        for (int i = 1; i <= nCols; ++i) {
            modeloTabla.addColumn(data.getColumnName(i));
        }

        modeloTabla.addColumn(""); // Columna de editar
        modeloTabla.addColumn(""); // Columna de eliminar

        while (tableContent.next()) {
            Object[] fila = new Object[nCols];
            for (int i = 1; i <= nCols; ++i) {
                fila [i - 1] = tableContent.getObject(i);
            }

            for (int j = 0; j < organizador.getItinerarios().size(); ++j) {
                System.out.println(organizador.getItinerarios().get(j));
                if (Objects.equals(organizador.getItinerarios().get(j), fila[2].toString())) {
                    modeloTabla.addRow(fila);

                }
            }
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

        // Esto es para insertar los botones en la última columna de la tabla
        // Columna de editar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setCellRenderer(new interfaz.organizador.VentanaConsultaAsignacionEjIt.ButtonPanelRenderer(2));
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMaxWidth(30);

        // Columna de eliminar
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new interfaz.organizador.VentanaConsultaAsignacionEjIt.ButtonPanelRenderer(1));
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        // Acción del botón de volver
        goBackButton.addActionListener(e -> {
            new VentanaOrganizador(organizador);
            dispose();
        });

        setVisible(true);
    }

    // Acciones del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());

        String ejercicio = (String) modeloTabla.getValueAt(row, 0);
        String olimpiada = (String) modeloTabla.getValueAt(row, 1);
        String itinerario = (String) modeloTabla.getValueAt(row, 2);

        if (columna == tabla.getColumnCount() - 2) {
            try {
                new VentanaModificarAsignacionEjIt(organizador, ejercicio, olimpiada, itinerario);
                dispose();

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - " + ex.getMessage());

            }

        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (organizador.deleteAssignationEjIt(ejercicio, olimpiada, itinerario) == 0) {
                    new interfaz.organizador.VentanaConsultaAsignacionEjIt(organizador);
                    dispose();

                }

            } catch (JSchException | SQLException ex) {
                new CustomJOptionPane("ERROR - " + ex.getMessage());
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

    // Esto es para añadir los botones a la última columna de la tabla
    static class ButtonPanelRenderer extends JPanel implements TableCellRenderer {
        private Image image;

        public ButtonPanelRenderer(int columna) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            switch (columna) {
                case 2 -> image = iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                case 1 -> image = iconoEliminar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                default -> {
                }
            }

            ImageIcon buttonIcon = new ImageIcon(image);
            JButton actionButton = new JButton(buttonIcon);
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