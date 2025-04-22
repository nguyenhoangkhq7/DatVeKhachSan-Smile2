package view.form;

import utils.custom_element.*;
import dao.Phong_DAO;
import model.Phong;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TimKiemPhong_FORM extends JPanel  implements ActionListener, MouseListener {
    private JTextField txtMaPhong, txtTenPhong, txtSoNguoi;
    private JComboBox<String> cmbLoaiPhong, cmbTrangThai;
    private DefaultTableModel tableModel;
    private JTable table;
    private Phong_DAO phongDAO;
    public TimKiemPhong_FORM() {
        phongDAO = new Phong_DAO();
        setLayout(new BorderLayout());
        setBackground(new Color(16, 16, 20));
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
//        northPanel.setBorder(BorderFactory.createLineBorder(Color.pink));
        northPanel.setBorder(BorderFactory.createEmptyBorder(21, 34, 50, 50));
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        String[] loaiPhongOptions = {"Tất cả"};
        String[] trangThaiOptions = {"Tất cả", "Còn trống", "Đã đặt trước", "Đang sử dụng", "Đang sửa chữa"};


        RoundedButton btnTimKiem = createHandleButton("Tìm kiếm");
        RoundedButton btnCapNhatPhong = createHandleButton("Cập nhật phòng");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        Box b1 = Box.createHorizontalBox();
        Box b2 = Box.createHorizontalBox();
        b1.add(createFormBox("Mã phòng", txtMaPhong = new JTextField()));
        b1.add(createFormBox("Tên phòng", txtTenPhong = new JTextField()));
        b1.add(createFormBox("Loại phòng", cmbLoaiPhong = new JComboBox<>(loaiPhongOptions)));
        b1.add(createFormBox("Số người", txtSoNguoi = new JTextField()));
        b1.add(createFormBox("Trạng thái", cmbTrangThai = new JComboBox<>(trangThaiOptions)));

        b2.add(Box.createHorizontalGlue());
        b2.add(btnTimKiem);
        b2.add(Box.createHorizontalStrut(70));
        b2.add(btnCapNhatPhong);
        b2.add(Box.createHorizontalStrut(70));
        b2.add(btnLamMoi);

        // Tieu de
        JLabel titleLabel = new JLabel("Danh sách phòng");
        titleLabel.setFont(FontManager.getManrope(Font.BOLD, 16));
        titleLabel.setForeground(Color.white);

        RoundedPanel titlePanel = new RoundedPanel(10, 0, new Color(27, 112, 213));
        titlePanel.setPreferredSize(new Dimension(1642, 50));
        titlePanel.setMinimumSize(new Dimension(1642, 50));
        titlePanel.setMaximumSize(new Dimension(1642, 50));
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.X_AXIS));

        titlePanel.add(Box.createHorizontalStrut(15));
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createHorizontalGlue());


        // Tạo bang
        Box b6 = Box.createHorizontalBox();
        String[] colName = {"Mã phòng", "Tên phòng", "Loại phòng", "Giá phòng", "Số người", "Trạng thái"};
        tableModel = new DefaultTableModel(colName, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll;
        b6.add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table.setBackground(new Color(24, 24, 28));
        table.setForeground(Color.WHITE);
        table.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table.setRowHeight(55);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 55));
        header.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer = new CustomCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer);
        }

        scroll.setBounds(30, 380, 1642, 200);
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);

        northPanel.add(b1);
        northPanel.add(b2);

        Box centerBox = Box.createVerticalBox();
        centerBox.add(titlePanel);
        centerBox.add(Box.createVerticalStrut(5));
        centerBox.add(scroll);
        centerBox.setBorder(BorderFactory.createEmptyBorder(0, 10, 50, 10));
        add(northPanel, BorderLayout.NORTH);
        add(centerBox, BorderLayout.CENTER);

        loadTableData();
        table.addMouseListener( this);
    }

    private Box createFormBox(String label, JTextField txt) {
        Box b = Box.createVerticalBox();
        Dimension boxSize = new Dimension(332, 110);
        b.setPreferredSize(boxSize);
        b.setMaximumSize(boxSize);
        b.setMaximumSize(boxSize);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);

        Box lblBox = Box.createHorizontalBox();
        lblBox.setPreferredSize(new Dimension(255, 20));
        lblBox.setMaximumSize(new Dimension(255, 20));
        lblBox.setMinimumSize(new Dimension(255, 20));
        lblBox.add(lbl);

        txt.setBackground(new Color(40, 40, 44));
        Dimension txtFieldSize = new Dimension(260, 45);
        txt.setPreferredSize(txtFieldSize);
        txt.setMaximumSize(txtFieldSize);
        txt.setMinimumSize(txtFieldSize);
        txt.setForeground(Color.white);
        txt.setFont(FontManager.getManrope(Font.PLAIN, 14));
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        CompoundBorder combinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), emptyBorder);
        txt.setBorder(emptyBorder);

        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txt.setBorder(combinedBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                txt.setBorder(emptyBorder);
            }
        });

        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(txt);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 68));

        return b;
    }

    private Box createFormBox(String label, JComboBox<String> cmb) {
        Box b = Box.createVerticalBox();
        Dimension boxSize = new Dimension(332, 110);
        b.setPreferredSize(boxSize);
        b.setMaximumSize(boxSize);
        b.setMaximumSize(boxSize);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);

        Box lblBox = Box.createHorizontalBox();
        lblBox.setPreferredSize(new Dimension(255, 20));
        lblBox.setMaximumSize(new Dimension(255, 20));
        lblBox.setMinimumSize(new Dimension(255, 20));
        lblBox.add(lbl);

        cmb.setBackground(new Color(40, 40, 44));
        Dimension txtFieldSize = new Dimension(260, 45);
        cmb.setPreferredSize(txtFieldSize);
        cmb.setMaximumSize(txtFieldSize);
        cmb.setMinimumSize(txtFieldSize);
        cmb.setFont(FontManager.getManrope(Font.PLAIN, 14));
        cmb.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(cmb);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 72));

        return b;
    }

    private RoundedButton createHandleButton(String buttonLabel) {
        RoundedButton button = new RoundedButton(buttonLabel, 5);
        Dimension buttonSize = new Dimension(259, 45);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        Color defaultBackground = new Color(80, 80, 88);
        Color hoverBackground = new Color(89, 98, 136);

        button.setBackground(defaultBackground);
        button.setForeground(Color.white);
        button.setFont(FontManager.getManrope(Font.PLAIN, 16));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(this);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBackground);
            }
        });

        return button;
    }

    private void loadTableData() {
//        ArrayList<Phong> dsPhong = phongDAO.getDSPhong();
//        for (Phong p : dsPhong) {
//            tableModel.addRow(new Object[]{
//                    p.getMaPhong(),
//                    p.getTenPhong(),
//                    p.getLoaiPhong(),
//                    p.getGiaPhong(),
//                    p.getSoNguoi(),
//                    p.getTrangThai()
//            });
//        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RoundedButton btn = (RoundedButton) e.getSource();

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = table.getSelectedRow();
        txtMaPhong.setText(table.getValueAt(row, 0).toString());
        txtTenPhong.setText(table.getValueAt(row, 1).toString());
        cmbLoaiPhong.setSelectedItem(table.getValueAt(row, 2).toString());
        txtSoNguoi.setText(table.getValueAt(row, 4).toString());
        cmbTrangThai.setSelectedItem(table.getValueAt(row, 5).toString());
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
}
