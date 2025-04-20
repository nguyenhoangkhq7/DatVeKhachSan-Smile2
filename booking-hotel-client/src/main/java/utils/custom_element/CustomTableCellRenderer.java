package utils.custom_element;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableCellRenderer extends DefaultTableCellRenderer {
    private final Color selectedColor = new Color(27, 112, 213); // Màu xanh
    private final Color defaultBackground = new Color(40, 40, 44); // Màu nền mặc định
    private final Color defaultForeground = Color.WHITE; // Màu chữ mặc định

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            c.setBackground(selectedColor);
            c.setForeground(defaultForeground);
        } else {
            c.setBackground(defaultBackground);
            c.setForeground(defaultForeground);
        }

        return c;
    }
}
