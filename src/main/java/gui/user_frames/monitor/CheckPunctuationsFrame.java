package gui.user_frames.monitor;

import gui.custom_components.*;
import gui.custom_components.buttons.ButtonPanelRenderer;
import gui.custom_components.labels.CustomFieldLabel;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.template_pattern.CheckTableFrameTemplate;
import operations.TransformPunctuationColumnName;
import users.Monitor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class CheckPunctuationsFrame extends CheckTableFrameTemplate implements TransformPunctuationColumnName {
    // Labels
    CustomFieldLabel filterLabel;

    // Botones

    // Combo boxes
    CustomComboBox exerciseComboBox;

    // Paneles
    CustomPanel filterPanel;

    // Otros
    DefaultTableModel modeloTabla;
    JTable tabla;
    JScrollPane tablaScrollPane;
    Monitor user;

    public CheckPunctuationsFrame(Monitor monitor, String ejercicio) throws SQLException {
        super(monitor, "Consulta de tabla T_EQUIPOS");
        
        user = monitor;

        filterLabel = new CustomFieldLabel("Seleccionar ejercicio ");
        filterLabel.setFont(buttonAndLabelFont);

        exerciseComboBox = new CustomComboBox();
        for (int i = 0; i < monitor.getExerciseCode().size(); ++i) {
            exerciseComboBox.addItem(monitor.getExerciseCode().get(i));
        }

        exerciseComboBox.setSelectedItem(ejercicio);

        filterPanel = new CustomPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.add(filterLabel);
        filterPanel.add(exerciseComboBox);

        getUpperBar().add(filterPanel, BorderLayout.SOUTH);

        add(createJScrollPane(), BorderLayout.CENTER);
        this.setVisible(true);

        getGoBackButton().addActionListener(e -> {
            new MonitorFrame(monitor);
            dispose();

        });

        exerciseComboBox.addActionListener(e -> {
            try {
                new CheckPunctuationsFrame(monitor, (String) exerciseComboBox.getSelectedItem());
                dispose();

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }
        });

    }

    // Acciones del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());

        String ejercicio = (String) exerciseComboBox.getSelectedItem();
        String equipo = (String) modeloTabla.getValueAt(row, 0);

        if (columna == tabla.getColumnCount() - 1) {
            try {
                new ModifyPunctuationFrame(user, ejercicio, equipo);

            } catch (SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }
            dispose();

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

            String concepto = null;

            String where = "WHERE CODIGO='" + exerciseComboBox.getSelectedItem() + "'";
            ResultSet exerciseConcept = user.selectCol("T_EJERCICIOS", "CONCEPTO", where);

            if (exerciseConcept.next()) {
                concepto = exerciseConcept.getString("CONCEPTO");
            } else {
                new ErrorJOptionPane("Ha ocurrido un error inesperado. Abortando...");
                dispose();
            }

            assert concepto != null;
            String columnaPuntuacion = transformColumnName(concepto);

            String itinerario = null;

            String olymp = null;

            ResultSet monitorOlympiad = user.selectCol("T_MONITORES", "OLIMPIADA", "WHERE NOMBRE='" + user.getUserName() + "'");

            if (monitorOlympiad.next()) {
                olymp = monitorOlympiad.getString("OLIMPIADA");
            }

            ResultSet itinerarioCheck = user.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", "WHERE EJERCICIO='" + exerciseComboBox.getSelectedItem() + "' AND OLIMPIADA='" + olymp + "'");

            if (itinerarioCheck.next()) {
                itinerario = itinerarioCheck.getString("ITINERARIO");
            }

            ResultSet data = user.selectRows("T_EQUIPOS", "NOMBRE, " + columnaPuntuacion, "NOMBRE", "WHERE ITINERARIO='" + itinerario + "'");
            ResultSetMetaData metaData = data.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            // Se insertan las filas traídas de la MV en la tabla de la ventana
            for (int i = 1; i <= numeroColumnas; ++i) {
                modeloTabla.addColumn(metaData.getColumnName(i));

            }

            modeloTabla.addColumn(""); // Columna de editar

            while (data.next()) {
                Object[] fila = new Object[numeroColumnas];
                for (int i = 1; i <= numeroColumnas; ++i) {
                    fila [i - 1] = data.getObject(i);

                }
                modeloTabla.addRow(fila);
            }

            data.close();

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
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new ButtonPanelRenderer(3));
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
            tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return tablaScrollPane;
    }

}
