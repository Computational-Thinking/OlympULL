package interfaz.admin;

import com.jcraft.jsch.JSchException;
import interfaz.custom_components.Borders;
import interfaz.custom_components.ErrorJOptionPane;
import interfaz.custom_components.Fonts;
import interfaz.custom_components.Icons;
import users.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.*;

public class CheckOlympiadsFrame extends JFrame implements Borders, Fonts, Icons, MouseListener {
    // Panel superior (título y botón de volver)
    JPanel upperPanel;
    // Panel de tabla
    JScrollPane tablaScrollPane;
    // Título de ventana
    JLabel consultaOlimpiadas;
    // Botón de volver
    JButton goBackButton;
    // Modelo de tabla
    DefaultTableModel modeloTabla;
    // Tabla
    JTable tabla;
    // Administrador
    Admin administrador;

    // Constructor
    public CheckOlympiadsFrame(Admin administrador) throws JSchException, SQLException {
        this.administrador = administrador;
        
        // Configuración de ventana
        setSize(950, 465);
        getContentPane().setLayout(new BorderLayout(5, 5));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Consulta de olimpiadas");
        setLocationRelativeTo(null);
        setIconImage(iconoVentana);

        // Botón de volver
        goBackButton = new JButton("< Volver");
        goBackButton.setFont(fuenteBotonesEtiquetas);
        goBackButton.setPreferredSize(new Dimension(90, 30));

        // Etiqueta de título
        consultaOlimpiadas = new JLabel("Consulta de tabla T_OLIMPIADAS");
        consultaOlimpiadas.setFont(fuenteTitulo);

        // Configuración de panel superior
        upperPanel = new JPanel();
        upperPanel.setLayout(new BorderLayout(5, 5));
        upperPanel.setBorder(borde);
        upperPanel.add(consultaOlimpiadas, BorderLayout.CENTER);
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
                fila [i - 1] = tableContents.getObject(i);
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

        // Se añaden los elementos a la ventana
        add(upperPanel, BorderLayout.NORTH);
        add(tablaScrollPane, BorderLayout.CENTER);

        // Acción del botón de volver
        goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AdminFrame(administrador);
                dispose();
            }
        });

        setVisible(true);
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

    // Esto es para añadir los botones a la última columna de la tabla (no necesario y creo que ni siquiera hace falta hacerlo tan complicado)
    static class ButtonPanelRenderer extends JPanel implements TableCellRenderer {
        private ImageIcon buttonIcon;

        public ButtonPanelRenderer(int columna) {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

            switch (columna) {
                case 3 -> buttonIcon = new ImageIcon(iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                case 2 -> buttonIcon = new ImageIcon(iconoDuplicar.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                case 1 -> buttonIcon = new ImageIcon(iconoEliminar.getScaledInstance(20, 20, Image.SCALE_SMOOTH));
                default -> {
                }
            }

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
