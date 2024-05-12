package gui.admin;

import com.jcraft.jsch.JSchException;
import gui.custom_components.*;
import gui.template_pattern.CheckTableFrameTemplate;
import users.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CheckRubricsFrame extends CheckTableFrameTemplate implements Borders, Fonts, Icons, MouseListener {
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    Admin administrador;
    // File path
    String fileName = ConfigReader.getDataFilesPath() + "/" + ConfigReader.getRubricsFileName();

    // Constructor
    public CheckRubricsFrame(Admin administrador) throws JSchException, SQLException {
        super(administrador, "Consulta de tabla T_RUBRICAS");

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
                ArrayList<String> data = new ArrayList<>();

                ResultSet dataSet = administrador.selectRows("T_RUBRICAS", "CODIGO");

                while(dataSet.next()) {
                    String code = "'" + dataSet.getString(1) + "'";
                    String name = "'" + dataSet.getString(2) + "'";
                    String desc = "'" + dataSet.getString(3) + "'";
                    String points = "'" + dataSet.getString(4) + "'";
                    String tags = "'" + dataSet.getString(5) + "'";

                    data.add(code + ", " + name + ", " + desc + ", " + points + ", " + tags);
                }

                FileWriter writer = new FileWriter(fileName, "T_RUBRICAS", data);

                new MessageJOptionPane("Se han guardado los registros en " + fileName);

                dataSet.close();
                writer.close();
            } catch (IOException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());
            }
        });

        getImportButton().addActionListener(e -> {
            try {
                FileReader reader = new FileReader(fileName);
                String tableName = reader.readTableName();
                ArrayList<String> tableTuples = reader.readTableRegisters();
                int insertions = 0;

                for (String tableTuple : tableTuples) {
                    String[] values = tableTuple.split(", ");
                    String where = "WHERE CODIGO=" + values[0];
                    if (administrador.importData(tableName, tableTuple, where) == 0) ++insertions;
                }

                reader.close();

                new MessageJOptionPane("Se han insertado " + insertions + " registros nuevos en " + tableName);

                if (insertions > 0) {
                    new CheckRubricsFrame(administrador);
                    dispose();
                }

            } catch (FileNotFoundException ex) {
                new ErrorJOptionPane("No se ha encontrado ningún archivo " + fileName);
            } catch (IOException ex) {
                new ErrorJOptionPane(ex.getMessage());
            } catch (Exception ex) {
                new ErrorJOptionPane("El formato de alguna línea del archivo de datos no es válido");
            }
        });
    }

    // Acciones del ratón
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tabla.rowAtPoint(e.getPoint());
        int columna = tabla.columnAtPoint(e.getPoint());

        String codigo = (String) modeloTabla.getValueAt(row, 0);
        String titulo = (String) modeloTabla.getValueAt(row, 1);
        String descripcion = (String) modeloTabla.getValueAt(row, 2);
        String values = (String) modeloTabla.getValueAt(row, 3);
        String tags = (String) modeloTabla.getValueAt(row, 4);

        String[] separatedTags = tags.split(",");
        int[] separatedValuesInt = new int[separatedTags.length];
        String[] separatedValues = values.split(",");

        // Convertir cada elemento del array de Strings a un entero y almacenarlo en el array de enteros
        for (int i = 0; i < separatedValues.length; i++) {
            if (i == 0) {
                separatedValuesInt[i] = Integer.parseInt(separatedValues[i].substring(1));
            } else if (i == separatedTags.length - 1) {
                separatedValuesInt[i] = Integer.parseInt(separatedValues[i].substring(1, separatedValues[i].length() - 1));
            } else {
                separatedValuesInt[i] = Integer.parseInt(separatedValues[i].substring(1));
            }
        }

        if (columna == tabla.getColumnCount() - 3) {
            new ModifyRubricFrame(administrador, codigo, titulo, descripcion, separatedValuesInt, separatedTags);
            dispose();

        } else if (columna == tabla.getColumnCount() - 2) {
            codigo = "Copia de " + modeloTabla.getValueAt(row, 0);
            try {
                administrador.createRubric(codigo, titulo, descripcion, values, tags);
                new CheckRubricsFrame(administrador);
                dispose();

            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);

            }

        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (administrador.deleteRubric(codigo) == 0) {
                    new CheckRubricsFrame(administrador);
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

            ResultSet tableContent = administrador.selectRows("T_RUBRICAS", "CODIGO");
            ResultSetMetaData data = tableContent.getMetaData();
            int nCols = data.getColumnCount();

            // Se insertan las filas traídas de la MV en la tabla de la ventana
            for (int i = 1; i <= nCols; ++i) {
                modeloTabla.addColumn(data.getColumnName(i));
            }

            modeloTabla.addColumn(""); // Columna de editar
            modeloTabla.addColumn(""); // Columna de duplicar
            modeloTabla.addColumn(""); // Columna de eliminar

            while (tableContent.next()) {
                Object[] fila = new Object[nCols];
                for (int i = 1; i <= nCols; ++i) {
                    fila[i - 1] = tableContent.getObject(i);
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

            // Insertar los botones de edición, duplicación y eliminación
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

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return tablaScrollPane;
    }
}