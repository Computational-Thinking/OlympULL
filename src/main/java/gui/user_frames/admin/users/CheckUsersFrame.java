package gui.user_frames.admin.users;

import com.jcraft.jsch.JSchException;
import file_management.FileReader;
import file_management.FileWriter;
import file_management.PropertiesReader;
import gui.user_frames.admin.AdminFrame;
import gui.custom_components.buttons.ButtonPanelRenderer;
import gui.custom_components.option_panes.ErrorJOptionPane;
import gui.custom_components.option_panes.MessageJOptionPane;
import gui.custom_components.predefined_elements.Borders;
import gui.custom_components.predefined_elements.Fonts;
import gui.custom_components.predefined_elements.Icons;
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

public class CheckUsersFrame extends CheckTableFrameTemplate implements Borders, Fonts, Icons, MouseListener {
    // Panel de tabla
    JScrollPane tablaScrollPane;

    // Modelo de tabla
    DefaultTableModel modeloTabla;

    // Tabla
    JTable tabla;
    Admin admin;
    // File path
    String fileName = PropertiesReader.getDataFilesPath() + "/" + PropertiesReader.getUsersFileName();

    // Constructor
    public CheckUsersFrame(Admin admin) throws JSchException, SQLException {
        super(admin, "Consulta de tabla T_USUARIOS");

        this.admin = admin;

        add(createJScrollPane(), BorderLayout.CENTER);

        this.setVisible(true);

        // Acción del botón de volver
        getGoBackButton().addActionListener(e -> {
            new AdminFrame(admin);
            dispose();
        });

        getExportButton().addActionListener(e -> {
            try {
                ArrayList<String> data = new ArrayList<>();

                ResultSet dataSet = admin.selectRows("T_USUARIOS", "NOMBRE");

                while(dataSet.next()) {
                    String name = "'" + dataSet.getString(1) + "'";
                    String password = "'" + dataSet.getString(2) + "'";
                    String type = "'" + dataSet.getString(3) + "'";

                    data.add(name + ", " + password + ", " + type);
                }

                FileWriter writer = new FileWriter(fileName, "T_EQUIPOS", data);

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
                    if (admin.importData(tableName, tableTuple, where) == 0) ++insertions;
                }

                reader.close();

                new MessageJOptionPane("Se han insertado " + insertions + " registros nuevos en " + tableName);

                if (insertions > 0) {
                    new CheckUsersFrame(admin);
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

        String nombre = (String) modeloTabla.getValueAt(row, 0);
        String type = (String) modeloTabla.getValueAt(row, 1);
        String pass = "";

        try {
            String where = "WHERE NOMBRE='" + nombre + "'";
            ResultSet password = admin.selectRows("T_USUARIOS", "PASSWORD", "PASSWORD", where);

            if (password.next()) {
                pass = password.getString("PASSWORD");
            }

            password.close();

        } catch (SQLException ex) {
            new ErrorJOptionPane(ex.getMessage());
            dispose();
        }

        if (columna == tabla.getColumnCount() - 3) {
            new ModifyUserFrame(admin, nombre, pass, type);
            dispose();
        } else if (columna == tabla.getColumnCount() - 2) {
            nombre = "Copia de " + modeloTabla.getValueAt(row, 0);
            try {
                admin.createUser(nombre, pass, type);
                new CheckUsersFrame(admin);
                dispose();
            } catch (JSchException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        } else if (columna == tabla.getColumnCount() - 1) {
            try {
                if (admin.deleteUser(nombre) == 0) {
                    new CheckUsersFrame(admin);
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

            ResultSet data = admin.selectRows("T_USUARIOS", "NOMBRE, TIPO", "TIPO", "");
            ResultSetMetaData metaData = data.getMetaData();
            int numeroColumnas = metaData.getColumnCount();

            // Se insertan las filas traídas de la MV en la tabla de la ventana
            for (int i = 1; i <= numeroColumnas; ++i) {
                modeloTabla.addColumn(metaData.getColumnName(i));
            }

            modeloTabla.addColumn(""); // Columna de editar
            modeloTabla.addColumn(""); // Columna de duplicar
            modeloTabla.addColumn(""); // Columna de eliminar

            while (data.next()) {
                Object[] fila = new Object[numeroColumnas];
                for (int i = 1; i <= numeroColumnas; ++i) {
                    fila[i - 1] = data.getObject(i);
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
