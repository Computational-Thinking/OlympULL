package interfaz.monitor;

import interfaz.*;
import usuarios.Monitor;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class VentanaConsultaPuntuaciones extends JFrame implements Bordes, Fuentes, Iconos, MouseListener {
    // Labels
    JLabel titleLabel;
    JLabel filterLabel;

    // Botones
    JButton goBackButton;

    // Combo boxes
    JComboBox<String> exerciseComboBox;

    // Paneles
    JPanel upperPanel;
    JPanel filterPanel;

    // Otros
    DefaultTableModel modeloTabla;
    JTable tabla;
    JScrollPane tablaScrollPane;
    Monitor monitor;

    public VentanaConsultaPuntuaciones(Monitor monitor, String ejercicio) throws SQLException {
        // Configuración de la ventana
        setSize(750, 600);
        getContentPane().setLayout(new BorderLayout());
        setTitle("Consulta de puntuaciones");
        setIconImage(iconoVentana);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        this.monitor = monitor;

        // Panel superior
        titleLabel = new JLabel("Consulta de tabla T_EQUIPOS");
        titleLabel.setFont(fuenteTitulo);

        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        filterLabel = new JLabel("Seleccionar ejercicio ");
        filterLabel.setFont(fuenteBotonesEtiquetas);

        exerciseComboBox = new JComboBox<>();
        for (int i = 0; i < monitor.getExerciseCode().size(); ++i) {
            exerciseComboBox.addItem(monitor.getExerciseCode().get(i));
        }
        exerciseComboBox.setFont(fuenteCampoTexto);
        exerciseComboBox.setSelectedItem(ejercicio);

        filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout());
        filterPanel.setBorder(borde);
        filterPanel.add(filterLabel);
        filterPanel.add(exerciseComboBox);

        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.add(titleLabel, BorderLayout.CENTER);
        upperPanel.add(goBackButton, BorderLayout.EAST);
        upperPanel.add(filterPanel, BorderLayout.SOUTH);
        upperPanel.setBorder(borde);

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

        String columnaPuntuacion = null;

        String concepto = null;

        String where = "WHERE CODIGO='" + exerciseComboBox.getSelectedItem() + "'";
        ResultSet exerciseConcept = monitor.selectCol("T_EJERCICIOS", "CONCEPTO", where);

        if (exerciseConcept.next()) {
            concepto = exerciseConcept.getString("CONCEPTO");
        } else {
            new ErrorJOptionPane("Ha ocurrido un error inesperado. Abortando...");
            dispose();
        }

        switch(concepto) {
            case "ABSTRACCION" -> columnaPuntuacion = "P_ABSTRACCION";
            case "ALGORITMOS" -> columnaPuntuacion = "P_ALGORITMOS";
            case "BUCLES" -> columnaPuntuacion = "P_BUCLES";
            case "CONDICIONALES" -> columnaPuntuacion = "P_CONDICIONALES";
            case "DESCOMPOSICION" -> columnaPuntuacion = "P_DESCOMPOSICION";
            case "FUNCIONES" -> columnaPuntuacion = "P_FUNCIONES";
            case "IA" -> columnaPuntuacion = "P_IA";
            case "RECONOCIMIENTO DE PATRONES" -> columnaPuntuacion = "P_REC_PATRONES";
            case "SECUENCIAS" -> columnaPuntuacion = "P_SECUENCIAS";
            case "SECUENCIAS Y BUCLES" -> columnaPuntuacion = "P_SECUENCIAS_Y_BUCLES";
            case "VARIABLES" -> columnaPuntuacion = "P_VARIABLES";
            case "VARIABLES Y FUNCIONES" -> columnaPuntuacion = "P_VARIABLES_Y_FUNC";
            case "OTROS" -> columnaPuntuacion = "P_OTROS";
        }

        String itinerario = null;

        ResultSet itinerarioCheck = monitor.selectCol("T_EJERCICIOS_OLIMPIADA_ITINERARIO", "ITINERARIO", "WHERE EJERCICIO='" + exerciseComboBox.getSelectedItem() + "'");

        if (itinerarioCheck.next()) {
            itinerario = itinerarioCheck.getString("ITINERARIO");
        }

        ResultSet data = monitor.selectRows("T_EQUIPOS", "NOMBRE, " + columnaPuntuacion, "NOMBRE", "WHERE ITINERARIO='" + itinerario + "'");
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
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setCellRenderer(new VentanaConsultaPuntuaciones.ButtonPanelRenderer());
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMinWidth(30);
        tabla.getColumnModel().getColumn(modeloTabla.getColumnCount() - 1).setMaxWidth(30);

        // Se añaden los elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        goBackButton.addActionListener(e -> {
            new VentanaMonitor(monitor);
            dispose();

        });

        exerciseComboBox.addActionListener(e -> {
            try {
                new VentanaConsultaPuntuaciones(monitor, (String) exerciseComboBox.getSelectedItem());
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
                new VentanaModificarPuntuacion(monitor, ejercicio, equipo);

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

    static class ButtonPanelRenderer extends JPanel implements TableCellRenderer {

        public ButtonPanelRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            Image image = iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            ImageIcon buttonIcon = new ImageIcon(image);
            JButton actionButton = new JButton(buttonIcon);
            actionButton.setPreferredSize(new Dimension(25, 25));

            // Se añade el botón al modelo de tabla
            add(actionButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
}
