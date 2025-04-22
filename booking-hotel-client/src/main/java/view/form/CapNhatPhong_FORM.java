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

public class CapNhatPhong_FORM extends JPanel implements ActionListener {
    private DefaultTableModel tableModel;
    private JTable table;
    private Phong_DAO phongDAO;
    private JTextField txtTenPhong, txtGiaPhong, txtSoNguoi;
    private JComboBox<String> cmbLoaiPhong, cmbTrangThai;
    public CapNhatPhong_FORM() {
        phongDAO = new Phong_DAO();
        setBackground(new Color(16, 16, 20));
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(10));
        // Tim kiem
        JTextField txtSearch = new JTextField("Tìm kiếm tên phòng");
        Border emptyBorder = BorderFactory.createEmptyBorder(13, 52, 12, 0);
        txtSearch.setBounds(0, 0, 280, 45);
        txtSearch.setBorder(emptyBorder);
        txtSearch.setBackground(new Color(40, 40, 44));
        txtSearch.setForeground(new Color(255, 255, 255, 125));
        txtSearch.setFont(FontManager.getManrope(Font.PLAIN, 15));
        CompoundBorder combinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), emptyBorder);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtSearch.setBorder(combinedBorder);
                if (txtSearch.getText().equals("Tìm kiếm tên phòng")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm tên phòng");
                }
            }
        });

        JLabel searchIcon = new JLabel(new ImageIcon("imgs/TimKiemIcon.png"));
        searchIcon.setBounds(12, 12, 24, 24);

        JPanel searchPanel = new JPanel();
        searchPanel.setOpaque(false);
        searchPanel.setLayout(null);
        Dimension searchPanelSize = new Dimension(280, 45);
        searchPanel.setPreferredSize(searchPanelSize);
        searchPanel.setMinimumSize(searchPanelSize);
        searchPanel.setMaximumSize(searchPanelSize);

        searchPanel.add(searchIcon);
        searchPanel.add(txtSearch);
        Box searchBox = Box.createHorizontalBox();
        searchBox.add(Box.createHorizontalStrut(0));
        searchBox.add(searchPanel);
        searchBox.add(Box.createGlue());


        // Form
        Box b1 = Box.createVerticalBox();
        Box b2 = Box.createHorizontalBox();
        b2.add(createFormBox("Tên phòng", txtTenPhong = new JTextField()));
        b2.add(createFormBox("Loại phòng", cmbLoaiPhong = new JComboBox<>()));
        b2.add(createFormBox("Giá phòng", txtGiaPhong = new JTextField()));
        b2.add(createFormBox("Số người", txtSoNguoi = new JTextField()));

        String[] trangThaiOptions = {"Còn trống", "Đã đặt trước", "Đang sử dụng", "Đang sửa chữa"};
        b2.add(createFormBox("Trạng thái", cmbTrangThai = new JComboBox<>(trangThaiOptions)));
        Dimension b2Size = new Dimension(1642, 100);
        b2.setPreferredSize(b2Size);
        b2.setMinimumSize(b2Size);
        b2.setMaximumSize(b2Size);


        Box b3 = Box.createHorizontalBox();
        Box b4 = Box.createVerticalBox();
        JLabel lblMota = new JLabel("Mô tả");
        lblMota.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lblMota.setForeground(Color.WHITE);
        Dimension lblMotaSize = new Dimension(1245, 20);

        Box lblMotaBox = Box.createHorizontalBox();
        lblMotaBox.setOpaque(false);
        lblMotaBox.setPreferredSize(lblMotaSize);
        lblMotaBox.setMaximumSize(lblMotaSize);
        lblMotaBox.setMinimumSize(lblMotaSize);

        JTextArea txaMoTa = new JTextArea();
        Dimension txaMoTaSize = new Dimension(1250, 220);
        txaMoTa.setPreferredSize(txaMoTaSize);
        txaMoTa.setMaximumSize(txaMoTaSize);
        txaMoTa.setMinimumSize(txaMoTaSize);
        txaMoTa.setBackground(new Color(40, 40, 44));
        txaMoTa.setForeground(Color.white);
        txaMoTa.setFont(FontManager.getManrope(Font.PLAIN, 14));
        txaMoTa.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 0));

        lblMotaBox.add(lblMota);
        b4.add(lblMotaBox);
        b4.add(txaMoTa);
        Box b5 = Box.createVerticalBox();

        RoundedButton btnThem = createHandleButton("Thêm");
        RoundedButton btnSua = createHandleButton("Sửa");
        RoundedButton btnXoa = createHandleButton("Xóa");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        b5.add(Box.createVerticalStrut(20));
        b5.add(btnThem);
        b5.add(Box.createVerticalStrut(20));
        b5.add(btnSua);
        b5.add(Box.createVerticalStrut(20));
        b5.add(btnXoa);
        b5.add(Box.createVerticalStrut(20));
        b5.add(btnLamMoi);


        b3.add(b4);
        b3.add(Box.createHorizontalStrut(80));
        b3.add(b5);

        b1.add(b2);
        b1.add(b3);


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

        
        mainBox.add(searchBox);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(b1);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(titlePanel);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(b6);
        add(mainBox);

        loadTableData();
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
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 72));

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

    private void addPhong() {
        String tenPhong = txtTenPhong.getText();
        String loaiPhong = (String) cmbLoaiPhong.getSelectedItem();
        String giaPhong = txtGiaPhong.getText();
        String soNguoi = txtSoNguoi.getText();
        String trangThai = (String) cmbTrangThai.getSelectedItem();

        // Kiểm tra và xử lý dữ liệu nhập trước khi thêm vào đối tượng Phong
        System.out.println("Tên phòng: " + tenPhong);
        System.out.println("Loại phòng: " + loaiPhong);
        System.out.println("Giá phòng: " + giaPhong);
        System.out.println("Số người: " + soNguoi);
        System.out.println("Trạng thái: " + trangThai);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        RoundedButton btn = (RoundedButton) e.getSource();
        String buttonLabel = btn.getText();
        switch (buttonLabel) {
            case "Thêm":
                addPhong();
                break;
            case "Sửa":
                break;
            case "Xóa":
                break;
            case "Làm mới":
                break;
        }
    }
}