package utils.custom_element;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    private static final long serialVersionUID = 1L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (c instanceof JComponent) {
            ((JComponent) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        }
        if (isSelected) {
            c.setBackground(new Color(27, 112, 213)); // Màu xanh khi chọn
            c.setForeground(Color.WHITE);
        } else {
            c.setBackground(new Color(40, 40, 44)); // Màu nền mặc định
            c.setForeground(Color.WHITE);
        }
        return c;
    }


}