package gui.user_frames.admin.exercises;

import com.jcraft.jsch.JSchException;
import file_management.FileReader;
import file_management.FileWriter;
import file_management.PropertiesReader;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.buttons.ButtonPanelRenderer;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.option_panes.MessageJOptionPane;
import gui.template_pattern.CheckTableFrameTemplate;
import users.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CheckExercisesFrame extends CheckTableFrameTemplate {
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    // Administrador
    Admin administrador;
    // File path
    String fileName = "./" + PropertiesReader.getExercisesFileName();

    // Constructor
    public CheckExercisesFrame(Admin administrador) throws JSchException, SQLException {
        super(administrador, "Consulta de tabla T_EJERCICIOS");

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

                ResultSet dataSet = administrador.selectRows("T_EJERCICIOS", "CODIGO");

                while(dataSet.next()) {
                    String code = "'" + dataSet.getString(1) + "'";
                    String title = "'" + dataSet.getString(2) + "'";
                    String desc = "'" + dataSet.getString(3) + "'";
                    String concept = "'" + dataSet.getString(4) + "'";
                    String resources = "'" + dataSet.getString(5) + "'";
                    String type = "'" + dataSet.getString(6) + "'";
                    String rubric = "'" + dataSet.getString(7) + "'";

                    data.add(code + ", " + title + ", " + desc + ", " + concept  + ", " + resources  + ", " + type  + ", " + rubric);
                }

                FileWriter writer = new FileWriter(fileName, "T_EJERCICIOS", data);

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
                    new CheckExercisesFrame(administrador);
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

        String code = (String) modeloTabla.getValueAt(row, 0);
        String title = (String) modeloTabla.getValueAt(row, 1);
        String description = (String) modeloTabla.getValueAt(row, 2);
        String concept = (String) modeloTabla.getValueAt(row, 3);
        String resources = (String) modeloTabla.getValueAt(row, 4);
        String type = (String) modeloTabla.getValueAt(row, 5);
        String rubric = (String) modeloTabla.getValueAt(row, 6);

        if (columna == tabla.getColumnCount() - 3) {
            try {
                new ModifyExerciseFrame(administrador, code, title, description, concept, resources, type);

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }

            dispose();

        } else if (columna == tabla.getColumnCount() - 2) {
            code = "Copia de " + code;

            String table = "T_EJERCICIOS";
            String data = "'" + code + "', '" + title + "', '" + description + "', '"
                              + concept + "', '" + resources + "', '" + type + "', '" + rubric + "'";

            try {
                if (administrador.createRegister(table, data) == 0) {
                    new CheckExercisesFrame(administrador);
                    dispose();
                }

            } catch (JSchException | SQLException ex) {
                new ErrorJOptionPane(ex.getMessage());

            }

        } else if (columna == tabla.getColumnCount() - 1) {
            String table = "T_EJERCICIOS";
            String whereClause = "WHERE CODIGO='" + code + "';";

            try {
                if (administrador.deleteRegister(table, whereClause) == 0) {
                    new CheckExercisesFrame(administrador);
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

            ResultSet tableContents = administrador.selectRows("T_EJERCICIOS", "CODIGO");
            ResultSetMetaData data = tableContents.getMetaData();
            int nCols = data.getColumnCount();

            for (int i = 1; i <= nCols; ++i) {
                modeloTabla.addColumn(data.getColumnName(i));
            }

            // Se añaden las columnas de botones
            modeloTabla.addColumn(""); // Columna de editar
            modeloTabla.addColumn(""); // Columna de duplicar
            modeloTabla.addColumn(""); // Columna de eliminar

            // Se añaden las filas a la tabla de la ventana
            while (tableContents.next()) {
                Object[] fila = new Object[nCols];

                for (int i = 1; i <= nCols; ++i) {
                    fila [i - 1] = tableContents.getObject(i);
                }

                modeloTabla.addRow(fila);
            }

            tableContents.close();

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

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
        }

        return tablaScrollPane;
    }
}
