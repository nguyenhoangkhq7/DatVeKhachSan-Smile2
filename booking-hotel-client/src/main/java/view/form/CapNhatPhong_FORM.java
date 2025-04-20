package view.form;

import com.google.gson.Gson;
import dto.LoaiPhongDTO;
import dto.PhongDTO;
import model.Request;
import model.Response;
import socket.SocketManager;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CapNhatPhong_FORM extends JPanel implements ActionListener, MouseListener {
    private final JTextArea txaMoTa;
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
        // FocusListener cho ô tìm kiếm phòng
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                txtSearch.setBorder(combinedBorder);  // Thiết lập border khi focus vào
                if (txtSearch.getText().equals("Tìm kiếm tên phòng")) {
                    txtSearch.setText("");  // Xóa text mặc định khi focus
                    txtSearch.setForeground(Color.WHITE);  // Đổi màu chữ khi focus
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);  // Thiết lập lại border khi mất focus
                String keyword = txtSearch.getText().trim();

                if (keyword.isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));  // Đổi màu chữ khi không có text
                    txtSearch.setText("Tìm kiếm tên phòng");  // Đặt lại text mặc định
                }
            }
        });

// 👉 Gọi tìm kiếm khi nhấn Enter
        txtSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (!keyword.isEmpty() && !keyword.equals("Tìm kiếm tên phòng")) {
                timKiemPhong(keyword);  // Gọi phương thức tìm kiếm phòng
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

        String[] trangThaiOptions = {"Còn trống",  "Đang sử dụng"};
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
        txaMoTa = new JTextArea();
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
//        RoundedButton btnXoa = createHandleButton("Xóa");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        b5.add(Box.createVerticalStrut(20));
        b5.add(btnThem);
        b5.add(Box.createVerticalStrut(20));
        b5.add(btnSua);
//        b5.add(Box.createVerticalStrut(20));
//        b5.add(btnXoa);
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
        String[] colName = {"Mã phòng", "Tên phòng", "Loại phòng", "Giá phòng", "Số người", "Trạng thái", "Mô tả"};
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
        loadComboBoxLoaiPhong();
        table.addMouseListener(this);
        btnThem.addActionListener(this);
        btnSua.addActionListener(this);
        btnLamMoi.addActionListener(this);
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
        tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
        Request<Void> request = new Request<>("GET_ALL_PHONG", null);

        try {
            SocketManager.send(request);

            Response response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                List<?> rawList = (List<?>) response.getData();

                if (rawList == null || rawList.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu phòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Gson gson = new Gson();
                for (Object obj : rawList) {
                    String json = gson.toJson(obj);
                    PhongDTO phong = gson.fromJson(json, PhongDTO.class);

                    // Xử lý trạng thái phòng
                    String trangThaiPhong = phong.getTinhTrang() == 0 ? "Còn trống" : "Đang sử dụng";

                    tableModel.addRow(new Object[]{
                            phong.getMaPhong(),
                            phong.getTenPhong(),
                            phong.getMaLoai(),
                            phong.getGiaPhong(),
                            phong.getSoNguoi(),
                            trangThaiPhong, // Hiển thị trạng thái dưới dạng chuỗi
                            phong.getMoTa()
                    });
                }

                System.out.println("Loaded " + rawList.size() + " rooms into table");

            } else {
                JOptionPane.showMessageDialog(this, "Không thể lấy dữ liệu phòng từ server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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


    private void loadComboBoxLoaiPhong() {
        cmbLoaiPhong.removeAllItems(); // Xóa tất cả các mục hiện có trong ComboBox
        Request<Void> request = new Request<>("GET_ALL_LOAIPHONG", null);
        Set<String> addedLoaiPhong = new HashSet<>(); // Dùng Set để lưu trữ các tên loại phòng đã thêm

        try {
            SocketManager.send(request);
            Response response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                List<?> rawList = (List<?>) response.getData();
                Gson gson = new Gson();

                for (Object obj : rawList) {
                    String json = gson.toJson(obj);
                    LoaiPhongDTO loaiPhong = gson.fromJson(json, LoaiPhongDTO.class);

                    // Kiểm tra nếu tên loại phòng đã tồn tại trong Set, nếu chưa thì thêm vào ComboBox
                    if (loaiPhong != null && !addedLoaiPhong.contains(loaiPhong.getTenLoai())) {
                        cmbLoaiPhong.addItem(loaiPhong.getTenLoai());
                        addedLoaiPhong.add(loaiPhong.getTenLoai()); // Thêm tên loại phòng vào Set
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu loại phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối server khi load loại phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

//    private void suaPhong() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        if (!validateAllInputs()) {
//            return;
//        }
//
//        try {
//            String maPhong = (String) tableModel.getValueAt(selectedRow, 0);
//            String tenPhong = txtTenPhong.getText().trim();
//            double giaPhong = Double.parseDouble(txtGiaPhong.getText().trim());
//            int tinhTrang = Integer.parseInt(cmbTrangThai.getText().trim());
//
//            String moTa = txtMoTa.getText().trim();
//            String maLoai = txtMaLoai.getText().trim();
//
//            PhongDTO phong = new PhongDTO();
//            phong.setMaPhong(maPhong);
//            phong.setTenPhong(tenPhong);
//            phong.setGiaPhong(giaPhong);
//            phong.setTinhTrang(tinhTrang);
//            phong.setSoNguoi(soNguoi);
//            phong.setMoTa(moTa);
//            phong.setMaLoai(maLoai);
//
//            Request<PhongDTO> request = new Request<>("SUA_PHONG", phong);
//            SocketManager.send(request);
//
//            Response response = SocketManager.receive(Response.class);
//
//            if (response != null && response.isSuccess()) {
//                tableModel.setValueAt(tenPhong, selectedRow, 1);
//                tableModel.setValueAt(giaPhong, selectedRow, 2);
//                tableModel.setValueAt(tinhTrang, selectedRow, 3);
//                tableModel.setValueAt(soNguoi, selectedRow, 4);
//                tableModel.setValueAt(moTa, selectedRow, 5);
//                tableModel.setValueAt(maLoai, selectedRow, 6);
//
//                JOptionPane.showMessageDialog(this, "Cập nhật thông tin phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//                lamMoi();
//            } else {
//                String error = response != null ? response.getData().toString() : "Lỗi không xác định";
//                JOptionPane.showMessageDialog(this, "Cập nhật phòng thất bại: " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi không mong muốn: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
//        }
//    }
//
private void timKiemPhong(String keyword) {
    Request<String> request = new Request<>("TIM_PHONG_NANG_CAO", keyword.trim());

    try {
        SocketManager.send(request);
        Response response = SocketManager.receive(Response.class); // Không generic
        if (response != null && response.isSuccess()) {
            // Ép kiểu dữ liệu trả về
            List<?> rawList = (List<?>) response.getData();
            List<PhongDTO> ds = new ArrayList<>();
            for (Object obj : rawList) {
                ds.add(new Gson().fromJson(new Gson().toJson(obj), PhongDTO.class));
            }

            tableModel.setRowCount(0); // Xóa dữ liệu cũ trong bảng
            for (PhongDTO phong : ds) {
                tableModel.addRow(new Object[]{
                        phong.getMaPhong(),
                        phong.getTenPhong(),
                        phong.getMaLoai(),
                        phong.getGiaPhong(),
                        phong.getSoNguoi(),
                        phong.getTinhTrang() == 0 ? "Còn trống" : "Đang sử dụng", // Chuyển trạng thái thành chữ
                        phong.getMoTa()
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

    private void lamMoiPhong() {
        txtTenPhong.setText("");
        txtGiaPhong.setText("");
        txtSoNguoi.setText("");
        txaMoTa.setText("");
        cmbLoaiPhong.setSelectedIndex(0); // nếu comboBox có danh sách loại phòng
        cmbTrangThai.setSelectedIndex(0); // nếu có comboBox tình trạng (VD: Trống/Đang sử dụng)
        txtTenPhong.requestFocus();
        loadTableData(); // load lại dữ liệu bảng phòng
    }

    public void mouseClicked(MouseEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            // Tạm thời vô hiệu hóa để tránh trigger nhiều lần
            table.setEnabled(false);

            txtTenPhong.setText(tableModel.getValueAt(selectedRow, 1).toString());
            cmbLoaiPhong.setSelectedItem(tableModel.getValueAt(selectedRow, 2).toString());
            txtGiaPhong.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtSoNguoi.setText(tableModel.getValueAt(selectedRow, 4).toString());
            cmbTrangThai.setSelectedItem(tableModel.getValueAt(selectedRow, 5).toString());
            txaMoTa.setText(tableModel.getValueAt(selectedRow, 6).toString());


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

    @Override
    public void actionPerformed(ActionEvent e) {
        RoundedButton btn = (RoundedButton) e.getSource();

        if (btn.getText().equals("Thêm")) {

        } else if (btn.getText().equals("Sửa")) {

        } else if (btn.getText().equals("Làm mới")) {
            lamMoiPhong();
        }
    }

}