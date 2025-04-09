package view.form;

import utils.custom_element.*;
import dao.KhachHang_DAO;
import model.KhachHang;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class CapNhatKhachHang_FORM extends JPanel  implements ActionListener, MouseListener {
    private JTextField txtSDT;
    private DefaultTableModel tableModel;
    private JTable table;
    private KhachHang_DAO khachHangDAO;
    private JTextField txtTenKhachHang, txtDiaChi,  txtCCCD, txtEmail;
    private JComboBox<String> cmbLoaiPhong, cmbTrangThai;
    public CapNhatKhachHang_FORM() {
        khachHangDAO = new KhachHang_DAO();
        setBackground(new Color(16, 16, 20));
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(10));
        // Tim kiem
        JTextField txtSearch = new JTextField("Tìm kiếm tên khách hàng");
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
                if (txtSearch.getText().equals("Tìm kiếm tên khách hàng")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm tên khách hàng");
                }
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Nếu nhấn Enter
                    String keyword = txtSearch.getText().trim();
                    if (!keyword.equals("") && !keyword.equals("Tìm kiếm tên khách hàng")) {
                        timKiem(keyword);
                    }
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
        b2.add(createFormBox("Tên khách hàng", txtTenKhachHang = new JTextField()));
        b2.add(createFormBox("Địa chỉ", txtDiaChi = new JTextField()));
        b2.add(createFormBox("Số điện thoại", txtSDT = new JTextField()));
        b2.add(createFormBox("Email", txtEmail = new JTextField()));
        b2.add(createFormBox("CCCD", txtCCCD = new JTextField()));

        Dimension b2Size = new Dimension(1642, 100);
        b2.setPreferredSize(b2Size);
        b2.setMinimumSize(b2Size);
        b2.setMaximumSize(b2Size);


        Box b3 = Box.createHorizontalBox();
        Box b4 = Box.createHorizontalBox();

        RoundedButton btnSua = createHandleButton("Sửa");
        RoundedButton btnXoa = createHandleButton("Xóa");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        b4.add(Box.createHorizontalGlue());
        b4.add(btnSua);
        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnXoa);
        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnLamMoi);
        b4.add(Box.createHorizontalStrut(55));


        b3.add(b4);


        b1.add(b2);
        b1.add(b3);


        // Tieu de
        JLabel titleLabel = new JLabel("Danh sách khách hàng");
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
        String[] colName = {"Mã khách hàng", "Tên khách hàng", "Địa chỉ", "Số điện thoại", "Email", "CCCD"};
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
        /*btnSua.addActionListener(this);
        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);*/
        table.addMouseListener(this);
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
        // Xóa tất cả các dòng trong bảng trước khi tải dữ liệu mới
        tableModel.setRowCount(0);

        ArrayList<KhachHang> dsKhachHang = khachHangDAO.getDSKhachHang();
        for (KhachHang kh : dsKhachHang) {
            if (kh.getTrangThai() == 1) { // Chỉ thêm khách hàng có trạng thái khác 0
                tableModel.addRow(new Object[]{
                        kh.getMaKH(),
                        kh.getHoTen(),
                        kh.getDiaChi(),
                        kh.getSdt(),
                        kh.getEmail(),
                        kh.getcCCD()
                });
            }
        }
    }


    private void lamMoi(){
        txtTenKhachHang.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        txtTenKhachHang.requestFocus();
        loadTableData();
    }
    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maKH = (String) tableModel.getValueAt(selectedRow, 0);
            try {
                if (khachHangDAO.xoaKH(maKH)) {
                    tableModel.removeRow(selectedRow); // Xóa dòng khỏi bảng
                    lamMoi();
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");

                    // Đặt lại trạng thái lựa chọn của bảng
                    table.clearSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa khách hàng!");
            }
        }else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
        }
    }



    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maKH = (String) tableModel.getValueAt(selectedRow, 0);
            String hoTen = txtTenKhachHang.getText();
            String diaChi = txtDiaChi.getText();
            String sdt = txtSDT.getText();
            String email = txtEmail.getText();
            String cccd = txtCCCD.getText();


            if (!hoTen.isEmpty() && !diaChi.isEmpty() && !sdt.isEmpty() && !email.isEmpty() && !cccd.isEmpty()) {
                try {
                    KhachHang kh = new KhachHang(maKH, hoTen, diaChi, sdt, email, cccd,1, new Date());
                    if (khachHangDAO.suaKhachHang(kh)) {
                        // Update table values after successful update
                        tableModel.setValueAt(hoTen, selectedRow, 1);
                        tableModel.setValueAt(diaChi, selectedRow, 2);
                        tableModel.setValueAt(sdt, selectedRow, 3);
                        tableModel.setValueAt(email, selectedRow, 4);
                        tableModel.setValueAt(cccd, selectedRow, 5);
                        lamMoi();


                        JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
                    } else {
                        JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại!");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật khách hàng!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
        }
    }


    private void timKiem(String keyword) {
        table.clearSelection(); // Xóa lựa chọn cũ trên bảng

        int foundRow = -1;

        // Tìm dòng chứa từ khóa đầu tiên
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean match = false;

            // Kiểm tra từ khóa trong các cột
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                if (tableModel.getValueAt(i, j).toString().toLowerCase().contains(keyword.toLowerCase())) {
                    match = true;
                    break;
                }
            }

            if (match) {
                foundRow = i;
                break; // Chỉ lấy dòng đầu tiên tìm thấy
            }
        }

        if (foundRow != -1) {
            // Di chuyển dòng tìm thấy lên đầu bảng
            moveRowToTop(foundRow);

            // Điền thông tin từ dòng được tìm thấy vào các ô nhập liệu
            txtTenKhachHang.setText(tableModel.getValueAt(0, 1).toString());
            txtDiaChi.setText(tableModel.getValueAt(0, 2).toString());
            txtSDT.setText(tableModel.getValueAt(0, 3).toString());
            txtEmail.setText(tableModel.getValueAt(0, 4).toString());
            txtCCCD.setText(tableModel.getValueAt(0, 5).toString());

            // Tạo hiệu ứng hover cho dòng đầu tiên
            table.addRowSelectionInterval(0, 0);

        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!");
        }
    }
    private void moveRowToTop(int rowIndex) {
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(rowIndex, i);
        }
        tableModel.removeRow(rowIndex); // Xóa dòng cũ
        tableModel.insertRow(0, rowData); // Chèn dòng vào đầu bảng
    }
    private boolean isValidInput(String input, String regex) {
        return input.matches(regex);
    }
    private void showError(String message, JTextField field) {
        JOptionPane.showMessageDialog(this, message, "Thông báo lỗi", JOptionPane.ERROR_MESSAGE);
        field.requestFocus();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        RoundedButton btn = (RoundedButton) e.getSource();

        if (btn.getText().equals("Sửa")) {
            // Kiểm tra dữ liệu trước khi sửa
            if (validateAllInputs()) {
                suaKhachHang(); // Thực hiện sửa nếu dữ liệu hợp lệ
            }
        } else if (btn.getText().equals("Xóa")) {
            xoaKhachHang();
        } else if (btn.getText().equals("Làm mới")) {
            lamMoi();
        }
    }

    private boolean validateAllInputs() {
        if (!isValidInput(txtTenKhachHang.getText().trim(), "^[\\p{L} ]+$")) {
            showError("Tên khách hàng không hợp lệ.", txtTenKhachHang);
            return false;
        }
        if (!isValidInput(txtSDT.getText().trim(), "^\\d{10,11}$")) {
            showError("Số điện thoại không hợp lệ.", txtSDT);
            return false;
        }
        if (!isValidInput(txtEmail.getText().trim(), "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            showError("Email không hợp lệ.", txtEmail);
            return false;
        }
        if (!isValidInput(txtCCCD.getText().trim(), "^\\d{12}$")) {
            showError("CCCD không hợp lệ.", txtCCCD);
            return false;
        }
        return true;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
            txtTenKhachHang.setText(tableModel.getValueAt(selectedRow,1).toString());
            txtDiaChi.setText(tableModel.getValueAt(selectedRow,2).toString());
            txtSDT.setText(tableModel.getValueAt(selectedRow,3).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow,4).toString());
            txtCCCD.setText(tableModel.getValueAt(selectedRow,5).toString());

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
}
