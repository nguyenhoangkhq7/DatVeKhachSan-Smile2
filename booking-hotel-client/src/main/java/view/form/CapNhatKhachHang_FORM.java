package view.form;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.KhachHangDTO;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;
import dao.KhachHang_DAO;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                String keyword = txtSearch.getText().trim();

                if (keyword.isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm tên khách hàng");
                }
            }
        });

// 👉 Gọi tìm kiếm khi nhấn Enter
        txtSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (!keyword.isEmpty() && !keyword.equals("Tìm kiếm tên khách hàng")) {
                timKiem(keyword);
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
//        RoundedButton btnXoa = createHandleButton("Xóa");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        b4.add(Box.createHorizontalGlue());
        b4.add(btnSua);
//        b4.add(Box.createHorizontalStrut(72));
//        b4.add(btnXoa);
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
        String[] colName = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Email", "CCCD"};
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
        btnSua.addActionListener(this);
//        btnXoa.addActionListener(this);
        btnLamMoi.addActionListener(this);
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
//        button.addActionListener(this);

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
        tableModel.setRowCount(0);
        Request<Void> request = new Request<>("GET_ALL_KHACH_HANG", null);

        try {
            SocketManager.send(request);

            // Nhận về Response mà không cần TypeToken
            Response response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                List<?> rawList = (List<?>) response.getData();

                if (rawList == null || rawList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu khách hàng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Dùng Gson để chuyển từng phần tử LinkedTreeMap -> KhachHangDTO
                Gson gson = new Gson();
                for (Object obj : rawList) {
                    String json = gson.toJson(obj);
                    KhachHangDTO kh = gson.fromJson(json, KhachHangDTO.class);

                    tableModel.addRow(new Object[]{
                            kh.getMaKH(),
                            kh.getHoTen(),
                            kh.getSoDienThoai(),
                            kh.getEmail(),
                            kh.getSoCCCD()
                    });
                }

                System.out.println("Loaded " + rawList.size() + " customers into table");

            } else {
                JOptionPane.showMessageDialog(this, "Không thể lấy dữ liệu khách hàng từ server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + e.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                loadTableData();
            }
        }

        table.repaint();
        table.revalidate();
    }




    private void themKhachHang(KhachHangDTO khachHang) {
        Request<KhachHangDTO> request = new Request<>("THEM_KHACH_HANG", khachHang);
        try {
            SocketManager.send(request);
            Response<String> response = SocketManager.receive(Response.class);

            if (response != null) {
                JOptionPane.showMessageDialog(this,
                        response.getData(),
                        response.isSuccess() ? "Thành công" : "Lỗi",
                        response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi gửi dữ liệu: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void lamMoi(){
        txtTenKhachHang.setText("");
        txtSDT.setText("");
        txtEmail.setText("");
        txtCCCD.setText("");
        txtTenKhachHang.requestFocus();
        loadTableData();
    }
//    private void xoaKhachHang() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String maKH = (String) tableModel.getValueAt(selectedRow, 0);
//            try {
//                if (khachHangDAO.xoaKH(maKH)) {
//                    tableModel.removeRow(selectedRow); // Xóa dòng khỏi bảng
//                    lamMoi();
//                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
//
//                    // Đặt lại trạng thái lựa chọn của bảng
//                    table.clearSelection();
//                } else {
//                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại!");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa khách hàng!");
//            }
//        }else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
//        }
//    }



    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateAllInputs()) {
            return;
        }

        try {
            String maKH = (String) tableModel.getValueAt(selectedRow, 0);
            String hoTen = txtTenKhachHang.getText().trim();
            String soDienThoai = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String soCCCD = txtCCCD.getText().trim();

            KhachHangDTO kh = new KhachHangDTO();
            kh.setMaKH(maKH);
            kh.setHoTen(hoTen);
            kh.setSoDienThoai(soDienThoai);
            kh.setEmail(email);
            kh.setSoCCCD(soCCCD);

            Request<KhachHangDTO> request = new Request<>("SUA_KHACH_HANG", kh);
            SocketManager.send(request);

            Response response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                tableModel.setValueAt(hoTen, selectedRow, 1);
                tableModel.setValueAt(soDienThoai, selectedRow, 2);
                tableModel.setValueAt(email, selectedRow, 3);
                tableModel.setValueAt(soCCCD, selectedRow, 4);

                JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                lamMoi();
            } else {
                String error = response != null ? response.getData().toString() : "Lỗi không xác định";
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại: " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi không mong muốn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void timKiem(String keyword) {
        Request<String> request = new Request<>("TIM_KHACH_HANG_NANG_CAO", keyword.trim());

        try {
            SocketManager.send(request);
            Response response = SocketManager.receive(Response.class); // Không generic
            if (response != null && response.isSuccess()) {
                // Ép kiểu dữ liệu trả về
                List<?> rawList = (List<?>) response.getData();
                List<KhachHangDTO> ds = new ArrayList<>();
                for (Object obj : rawList) {
                    ds.add(new Gson().fromJson(new Gson().toJson(obj), KhachHangDTO.class));
                }

                tableModel.setRowCount(0);
                for (KhachHangDTO kh : ds) {
                    tableModel.addRow(new Object[]{
                            kh.getMaKH(),
                            kh.getHoTen(),
                            kh.getSoDienThoai(),
                            kh.getEmail(),
                            kh.getSoCCCD()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy dữ liệu phù hợp!", "Kết quả", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm kiếm nâng cao: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }

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

                suaKhachHang();

        } else if (btn.getText().equals("Làm mới")) {
            lamMoi();
        }
    }


    private boolean validateAllInputs() {


        // Kiểm tra form có rỗng toàn bộ không (tránh validate khi vừa làm mới)
        if (txtTenKhachHang.getText().trim().isEmpty() &&
                txtSDT.getText().trim().isEmpty() &&
                txtEmail.getText().trim().isEmpty() &&
                txtCCCD.getText().trim().isEmpty()) {
            return true;
        }

        // Kiểm tra từng field có rỗng không
        if (txtTenKhachHang.getText().trim().isEmpty() ||
                txtSDT.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() ||
                txtCCCD.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // Regex kiểm tra
        if (!isValidInput(txtTenKhachHang.getText().trim(), "^[\\p{L} .']+$")) {
            showError("Tên khách hàng không hợp lệ. Chỉ chứa chữ cái, khoảng trắng, dấu chấm và dấu nháy đơn.", txtTenKhachHang);
            return false;
        }

        if (!isValidInput(txtSDT.getText().trim(), "^\\(\\d{3}\\) \\d{3}-\\d{4}$")) {
            showError("Số điện thoại không hợp lệ. Định dạng đúng: (XXX) XXX-XXXX.", txtSDT);
            return false;
        }

        if (!isValidInput(txtEmail.getText().trim(), "^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,6}$")) {
            showError("Email không hợp lệ.", txtEmail);
            return false;
        }

        if (!isValidInput(txtCCCD.getText().trim(), "^\\d{12}$")) {
            showError("CCCD phải gồm đúng 12 chữ số.", txtCCCD);
            return false;
        }

        return true;
    }



    @Override
    public void mouseClicked(MouseEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Tạm thời vô hiệu hóa các sự kiện để tránh lặp
            table.setEnabled(false);

            txtTenKhachHang.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSDT.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtCCCD.setText(tableModel.getValueAt(selectedRow, 4).toString());

            // Kích hoạt lại bảng
            table.setEnabled(true);
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
