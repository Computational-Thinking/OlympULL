package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template.CheckTableFrameTemplate;
import users.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CheckExOlympAssignationsFrame extends CheckTableFrameTemplate implements Borders, Fonts, Icons, MouseListener {
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    // Administrador
    Admin administrador;

    public CheckExOlympAssignationsFrame(Admin administrador) throws JSchException, SQLException {
        super(administrador, 465, "Consulta de tabla");

        this.administrador = administrador;

        // Se añaden los paneles a la ventana
        add(createJScrollPane(), BorderLayout.CENTER);

        setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new AdminFrame(administrador);
            dispose();
        });

        getExportButton().addActionListener(e -> {
            try {
                String fileName = "data_files/exercises-olympiads.olympull";
                ArrayList<String> data = new ArrayList<>();

                ResultSet dataSet = administrador.selectRows("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "EJERCICIO");

                while(dataSet.next()) {
                    String exercise = "'" + dataSet.getString(1) + "'";
                    String olymp = "'" + dataSet.getString(2) + "'";
                    String it = "'" + dataSet.getString(3) + "'";

                    data.add("(" + exercise + ", " + olymp + ", " + it + ")");
                }

                FileWriter writer = new FileWriter(fileName, "T_EJERCICIOS_OLIMPIADA_ITINERARIO", data);

                new MessageJOptionPane("Se han guardado los registros en " + fileName);

                dataSet.close();
                writer.close();
            } catch (IOException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });
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
                new ModifyAssignationExOlympFrame(administrador, ejercicio, olimpiada, itinerario);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (administrador.deleteAssignationEjOlimp(ejercicio, olimpiada) == 0) {
                    new CheckExOlympAssignationsFrame(administrador);
                    dispose();
                }
            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
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

    @Override
    protected JScrollPane createJScrollPane() {
        try {
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

            ResultSet tableContent = administrador.selectRows("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "EJERCICIO");
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

            // Esto es para insertar los botones en la última columna de la tabla
            // Columna de editar
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setCellRenderer(new ButtonPanelRenderer(3));
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMinWidth(30);
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 2).setMaxWidth(30);

            // Columna de eliminar
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new ButtonPanelRenderer(1));
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return tablaScrollPane;
    }
}
