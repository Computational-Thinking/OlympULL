package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.*;
import interfaz.template_pattern.CheckTableFrameTemplate;
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

public class CheckOlympiadsFrame extends CheckTableFrameTemplate implements Borders, Fonts, Icons, MouseListener {
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    // Administrador
    Admin administrador;
    // Archivo de datos
    String fileName = ConfigReader.getDataFilesPath() + "/" + ConfigReader.getOlympiadsFileName();

    // Constructor
    public CheckOlympiadsFrame(Admin administrador) throws JSchException, SQLException {
        super(administrador, 465, "Consulta de tabla T_OLIMPIADAS");

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

                ResultSet dataSet = administrador.selectRows("T_OLIMPIADAS", "CODIGO");

                while(dataSet.next()) {
                    String code = "'" + dataSet.getString(1) + "'";
                    String title = "'" + dataSet.getString(2) + "'";
                    String desc = "'" + dataSet.getString(3) + "'";
                    int year = Integer.parseInt(dataSet.getString(4));

                    data.add(code + ", " + title + ", " + desc + ", " + year);
                }

                FileWriter writer = new FileWriter(fileName, "T_OLIMPIADAS", data);

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
                    new CheckOlympiadsFrame(administrador);
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
        int column = tabla.columnAtPoint(e.getPoint());

        String code = (String) modeloTabla.getValueAt(row, 0);
        String title = (String) modeloTabla.getValueAt(row, 1);
        String desc = (String) modeloTabla.getValueAt(row, 2);
        String year = Integer.toString((Integer) modeloTabla.getValueAt(row, 3));

        if (column == tabla.getColumnCount() - 3) {
            new ModifyOlympiadFrame(administrador, code, title, desc, year);
            dispose();

        } else if (column == tabla.getColumnCount() - 2) {
            code = "Copia de " + modeloTabla.getValueAt(row, 0);

            if (year.matches("[0-9]*") && Integer.parseInt(year) > 1999 && Integer.parseInt(year) < 3001) {
                try {
                    if (administrador.createOlympiad(code, title, desc, Integer.parseInt(year)) == 0) {
                        new CheckOlympiadsFrame(administrador);
                        dispose();
                    }

                } catch (JSchException | SQLException ex) {
                    throw new RuntimeException(ex);
                }

            } else {
                new ErrorJOptionPane("El campo Año debe ser un número entero y tener un valor válido");
            }

        } else if (column == tabla.getColumnCount() - 1) {
            try {
                if (administrador.deleteOlympiad((String) modeloTabla.getValueAt(row, 0)) == 0) {
                    new CheckOlympiadsFrame(administrador);
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

            // Se almacena el conjunto de datos
            ResultSet tableContents = administrador.selectRows("T_OLIMPIADAS", "CODIGO");
            ResultSetMetaData metaData = tableContents.getMetaData();
            int nCols = metaData.getColumnCount();

            // Se insertan las filas traídas de la MV en la tabla de la ventana
            for (int i = 1; i <= nCols; ++i) {
                modeloTabla.addColumn(metaData.getColumnName(i));
            }

            // Se añaden las columnas de botones
            modeloTabla.addColumn(""); // Columna de editar
            modeloTabla.addColumn(""); // Columna de duplicar
            modeloTabla.addColumn(""); // Columna de eliminar

            // Se añaden las filas a la tabla de la ventana
            while (tableContents.next()) {
                Object[] fila = new Object[nCols];

                for (int i = 1; i <= nCols; ++i) {
                    fila[i - 1] = tableContents.getObject(i);
                }

                modeloTabla.addRow(fila);
            }

            tableContents.close();

            // Se establece la fuente de texto al modelo de tabla
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

            // Altura de las filas de la tabla
            tabla.setRowHeight(35);

            // Esto es para que se pueda pulsar los botones
            tabla.addMouseListener(this);

            // Esto es para insertar los botones en las columnas de botones
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
