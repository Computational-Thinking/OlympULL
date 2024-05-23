package gui.user_frames.organizer;

import com.jcraft.jsch.JSchException;
import gui.custom_components.buttons.ButtonPanelRenderer;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.template_pattern.CheckTableFrameTemplate;
import users.Organizer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;

public class CheckExItAssignationFrame extends CheckTableFrameTemplate {
    // Paneles
    JScrollPane tablaScrollPane;

    // Modelo de tabla
    DefaultTableModel modeloTabla;

    // Tabla
    JTable tabla;

    // Administrador
    Organizer organizador;

    // Constructor
    public CheckExItAssignationFrame(Organizer organizador) throws JSchException, SQLException {
        super(organizador, "Consulta de tabla T_EJERCICIOS_OLIMPIADA_ITINERARIO");

        this.organizador = organizador;

        add(createJScrollPane(), BorderLayout.CENTER);

        this.setVisible(true);

        // Acción del botón de volver
        getGoBackButton().addActionListener(e -> {
            new OrganizerFrame(organizador);
            dispose();
        });

        setVisible(true);
    }

    // Acciones del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());

        String exercise = (String) modeloTabla.getValueAt(row, 0);
        String olympiad = (String) modeloTabla.getValueAt(row, 1);
        String itinerary = (String) modeloTabla.getValueAt(row, 2);

        String table = "T_EJERCICIOS_OLIMPIADA_ITINERARIO";
        String where = "WHERE EJERCICIO='" + exercise +  "' AND OLIMPIADA='" + olympiad + "' AND ITINERARIO='" + itinerary + "';";

        if (columna == tabla.getColumnCount() - 2) {
            try {
                new ModifyExItAssignationFrame(organizador, exercise, olympiad, itinerary);
                dispose();

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }

        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (organizador.deleteRegister(table, where) == 0) {
                    new CheckExItAssignationFrame(organizador);
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
                    fila[i - 1] = tableContent.getObject(i);
                }

                for (int j = 0; j < organizador.getItineraries().size(); ++j) {
                    if (Objects.equals(organizador.getItineraries().get(j), fila[2].toString())) {
                        modeloTabla.addRow(fila);

                    }
                }
            }

            tableContent.close();

            // Esto es para establecer la fuente del contenido de la tabla
            DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) tabla.getTableHeader().getDefaultRenderer();
            headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            tabla.getTableHeader().setFont(buttonAndLabelFont);

            DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setFont(textFieldFont);
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
