package gui.custom_components.buttons;

import gui.custom_components.predefined_elements.Icons;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ButtonPanelRenderer extends JPanel implements Icons, TableCellRenderer {
    private Image image;

    public ButtonPanelRenderer(int columna) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        switch (columna) {
            case 3 -> image = iconoEditar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            case 2 -> image = iconoDuplicar.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
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
