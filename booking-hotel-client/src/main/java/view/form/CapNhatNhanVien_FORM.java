package view.form;

import com.google.gson.reflect.TypeToken;
import dto.NhanVienDTO;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;
import dao.NhanVien_DAO;
import model.NhanVien;
import org.jdesktop.swingx.JXDatePicker;

import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.List;
import java.util.regex.Pattern;

public class CapNhatNhanVien_FORM extends JPanel implements Openable, ActionListener {
    private final JTextField txtHoTen;
    private final JComboBox<String> cmbChucVu;
    private final JTextField txtHSL, txtSoDT, txtDiaChi, txtLuong, txtEmail;
    private final JTable table;
    private DefaultTableModel tableModel;
    private final JXDatePicker dateNgaySinh, dateNgayVaoLam;
    private JTextField txtSearch;
    private NhanVien_DAO NhanVien_DAO;
    private static Set<String> danhSachMaNhanVien = new HashSet<>();
    public void open() {
        NhanVien_DAO = new NhanVien_DAO();
//        ArrayList<NhanVien> dsNV = NhanVien_DAO.getDSNhanVien();
        loadTableData();
//        loadLoaiPhong();
    }
    public CapNhatNhanVien_FORM() {

        setLayout(new BorderLayout());
        setBackground(new Color(16, 16, 20));
        // north
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        Box northPanelBox = Box.createVerticalBox();
        // Tim kiem
        txtSearch = new JTextField("Tìm kiếm");
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
                if (txtSearch.getText().equals("Tìm kiếm")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm");
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


        JPanel form = new JPanel();
        form.setPreferredSize(new Dimension(1400, 350));
        form.setOpaque(false);
        // BoxForm
        Box boxForm1 = createFormBox("Họ tên", txtHoTen = new JTextField());
        Box boxForm2 = createFormBox("Chức vụ", cmbChucVu = new JComboBox<>(new String[]{"Chọn", "Lễ tân", "Quản lý", "Admin"}));
        Box boxForm3 = createFormBox("Ngày sinh",dateNgaySinh = new JXDatePicker());
        Box boxForm4 = createFormBox("Ngày vào làm",dateNgayVaoLam = new JXDatePicker());
        Box boxForm5 = createFormBox("Số điện thoại", txtSoDT = new JTextField());
        Box boxForm6 = createFormBox("Địa chỉ", txtDiaChi = new JTextField());
        Box boxForm7 = createFormBox("Email", txtEmail = new JTextField());
        Box boxForm8 = createFormBox("Lương cơ bản", txtLuong = new JTextField());
        Box boxForm9 = createFormBox("Hệ số lương", txtHSL = new JTextField());
        txtHSL.setEditable(false);


        form.add(boxForm1);
        form.add(boxForm2);
        form.add(boxForm3);
        form.add(boxForm4);
        form.add(boxForm5);
        form.add(boxForm6);
        form.add(boxForm7);
        form.add(boxForm8);
        form.add(boxForm9);

        northPanelBox.add(searchBox);
        northPanelBox.add(Box.createVerticalStrut(10));
        northPanelBox.add(form);
        northPanelBox.setPreferredSize(new Dimension(1642, 380));
        northPanel.add(northPanelBox);

        //center

        JLabel titleLabel = new JLabel("Danh sách nhân viên");
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


        String[] headers = {"Mã nhân viên", "Họ tên", "Chức vụ", "Ngày sinh", "Ngày vào làm", "SDT", "Địa chỉ", "Email", "Lương cơ bản", "Hệ số lương"};
        tableModel = new DefaultTableModel(headers, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        table = new JTable(tableModel);
        table.setBackground(new Color(24, 24, 28));
        table.setForeground(Color.WHITE);
        table.setFont(FontManager.getManrope(Font.PLAIN, 14));
        table.setRowHeight(55);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = table.rowAtPoint(evt.getPoint());
                if (row >= 0) {
//                    String maNV = (String) tableModel.getValueAt(row, 0);
                    String hoTen = (String) tableModel.getValueAt(row, 1);
                    String chucVu = (String) tableModel.getValueAt(row, 2);
                    Date ngaySinh = (Date) tableModel.getValueAt(row, 3);
                    Date ngayVaoLam = (Date) tableModel.getValueAt(row, 4);
                    String soDT = (String) tableModel.getValueAt(row, 5);
                    String diaChi = (String) tableModel.getValueAt(row, 6);
                    String email = (String) tableModel.getValueAt(row, 7);

                    Double luongCoBan = (Double) tableModel.getValueAt(row, 8);
                    Double heSoLuong = (Double) tableModel.getValueAt(row, 9);

                    txtHoTen.setText(hoTen);
                    cmbChucVu.setSelectedItem(chucVu);
                    dateNgaySinh.setDate(ngaySinh);
                    dateNgayVaoLam.setDate(ngayVaoLam);
                    txtSoDT.setText(soDT);
                    txtDiaChi.setText(diaChi);
                    txtEmail.setText(email);
                    txtLuong.setText(String.valueOf(luongCoBan));
                    txtHSL.setText(String.valueOf(heSoLuong));
                }
            }
        });
        cmbChucVu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chucVu = (String) cmbChucVu.getSelectedItem();
                if (chucVu.equals("Lễ tân")) {
                    txtLuong.setText("8000000");
                    txtHSL.setText("2.34");
                    txtLuong.setEditable(false);
                    txtHSL.setEditable(false);
                } else if (chucVu.equals("Quản lý")){
                    txtLuong.setText("20000000");
                    txtHSL.setText("4.0");
                    txtLuong.setEditable(false);
                    txtHSL.setEditable(false);
                } else {
                    txtLuong.setEditable(true);
                    txtHSL.setEditable(true);
                    txtLuong.setText("");
                    txtHSL.setText("");
                }
            }
        });
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 55));
        header.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer = new CustomCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(1642, 330));
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);


        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(titlePanel);
        centerPanel.add(scroll);

        //southPanel
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        JButton addButton = createButton("Thêm", new Color(10, 123, 66));
        JButton deleteButton = createButton("Xóa", new Color(255, 117, 142));
        JButton updateButton = createButton("Sửa", new Color(151, 114, 35));
        JButton refreshButton = createButton("Làm mới", new Color(89, 38, 136, 242));
        addButton.addActionListener(e -> themNhanVien());
        updateButton.addActionListener(e -> suaNhanVien());
        deleteButton.addActionListener(e -> xoaNhanVien());
        refreshButton.addActionListener(e -> lamMoi());
        southPanel.add(addButton);
        southPanel.add(deleteButton);
        southPanel.add(updateButton);
        southPanel.add(refreshButton);

        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

    }

    private void loadTableData() {
        // Xóa tất cả các dòng trong bảng trước khi tải dữ liệu mới
        tableModel.setRowCount(0);

        // Gửi yêu cầu đến server để lấy danh sách nhân viên
        Request<Void> request = new Request<>("GET_ALL_NHAN_VIEN", null);
        try {
            SocketManager.send(request); // Gửi object Request
            // Sử dụng TypeToken để chỉ định kiểu generic
            Type responseType = new TypeToken<Response<List<NhanVienDTO>>>(){}.getType();
            Response<List<NhanVienDTO>> response = SocketManager.receive(responseType);

            if (response != null && response.isSuccess()) {
                List<NhanVienDTO> dsNhanVien = response.getData();
                if (dsNhanVien == null || dsNhanVien.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                            "Không có dữ liệu nhân viên!",
                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Load dữ liệu vào bảng
                for (NhanVienDTO nv : dsNhanVien) {
                    tableModel.addRow(new Object[]{
                            nv.getMaNhanVien(),
                            nv.getHoTen(),
                            nv.getChucVu(),
                            nv.getNgaySinh() != null ? java.sql.Date.valueOf(nv.getNgaySinh()) : null, // Chuyển LocalDate thành Date
                            nv.getNgayVaoLam() != null ? java.sql.Date.valueOf(nv.getNgayVaoLam()) : null, // Chuyển LocalDate thành Date
                            nv.getSDT(),
                            nv.getDiaChi(),
                            nv.getEmail(),
                            nv.getLuongCoBan(),
                            nv.getHeSoLuong()
                    });
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không thể lấy dữ liệu nhân viên từ server!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }
    private Box createFormBox(String label, JTextField txt) {
        Box b = Box.createVerticalBox();
        b.setPreferredSize(new Dimension(331, 106));
        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(259, 20));
        lbl.setMaximumSize(new Dimension(259, 20));
        lbl.setMinimumSize(new Dimension(259, 20));
        txt.setBackground(new Color(40, 40, 44));
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
        b.add(lbl);
        b.add(Box.createVerticalStrut(6));
        b.add(txt);
        b.setBorder(BorderFactory.createEmptyBorder(0, 36, 35, 36));
        return b;
    }

    private Box createFormBox(String label, JComboBox<String> cmb) {
        Box b = Box.createVerticalBox();
        b.setPreferredSize(new Dimension(331, 106));
        Box lblBox = Box.createHorizontalBox();
        lblBox.setPreferredSize(new Dimension(331, 20));
        lblBox.setMaximumSize(new Dimension(331, 20));
        lblBox.setMinimumSize(new Dimension(331, 20));
        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);
        lbl.setPreferredSize(new Dimension(259, 20));
        lbl.setMaximumSize(new Dimension(259, 20));
        lbl.setMinimumSize(new Dimension(259, 20));
        cmb.setFont(FontManager.getManrope(Font.PLAIN, 14));
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        cmb.setBorder(emptyBorder);

        lblBox.add(Box.createHorizontalStrut(5));
        lblBox.add(lbl);
        lblBox.add(Box.createHorizontalGlue());
        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(cmb);
        b.setBorder(BorderFactory.createEmptyBorder(0, 36, 35, 36));
        return b;
    }
    private Box createFormBox(String label, JXDatePicker datePicker) {
        Date date = new Date();
        Box b = Box.createVerticalBox();
        b.setPreferredSize(new Dimension(335, 106));
        Box lblBox = Box.createHorizontalBox();
        lblBox.setPreferredSize(new Dimension(335, 20));
        lblBox.setMaximumSize(new Dimension(335, 20));
        lblBox.setMinimumSize(new Dimension(335, 20));
        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);
        datePicker.setPreferredSize(new Dimension(331, 45));
        datePicker.setMaximumSize(new Dimension(331, 45));
        datePicker.setMinimumSize(new Dimension(331, 45));
        datePicker.setFont(FontManager.getManrope(Font.PLAIN, 14));
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        datePicker.setBorder(emptyBorder);
        datePicker.setFormats("dd/MM/yyyy");
        datePicker.setDate(date);
        JFormattedTextField dateTextField = datePicker.getEditor();
        dateTextField.setBackground(new Color(40, 40, 44));
        dateTextField.setForeground(Color.white);

        lblBox.add(Box.createHorizontalStrut(10));
        lblBox.add(lbl);
        lblBox.add(Box.createHorizontalGlue());
        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(datePicker);
        b.setBorder(BorderFactory.createEmptyBorder(0, 36, 35, 36));
        return b;
    }
    private JButton createButton(String buttonLabel, Color buttonColor) {
        JButton button = new JButton(buttonLabel);
        button.setBackground(buttonColor);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(259, 45));
        button.setFont(FontManager.getManrope(Font.PLAIN, 16));
        return button;
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
            txtHoTen.setText(tableModel.getValueAt(0, 1).toString());
            cmbChucVu.setSelectedItem(tableModel.getValueAt(0, 2).toString());
            dateNgaySinh.setDate((Date) tableModel.getValueAt(0, 3));
            dateNgayVaoLam.setDate((Date) tableModel.getValueAt(0, 4));
            txtSoDT.setText(tableModel.getValueAt(0, 5).toString());
            txtDiaChi.setText(tableModel.getValueAt(0, 6).toString());
            txtEmail.setText(tableModel.getValueAt(0, 7).toString());
            txtLuong.setText(tableModel.getValueAt(0, 8).toString());
            txtHSL.setText(tableModel.getValueAt(0, 9).toString());

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

    private void lamMoi() {
        // Xóa nội dung các trường nhập liệu
        txtHoTen.setText("");
        cmbChucVu.setSelectedIndex(0);
        txtSoDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtLuong.setText("");
        txtHSL.setText("");
        txtHoTen.requestFocus();
        loadTableData();

        // Đặt ngày sinh và ngày vào làm về ngày hiện tại
        Date today = new Date(); // Lấy ngày hiện tại
        dateNgaySinh.setDate(today);
        dateNgayVaoLam.setDate(today);
    }
    private void xoaNhanVien() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String maNV = (String) tableModel.getValueAt(selectedRow, 0);
//            try {
//                if (NhanVien_DAO.xoaNV(maNV)) {
//                    tableModel.removeRow(selectedRow); // Xóa dòng khỏi bảng
//                    lamMoi();
//                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
//
//                    // Đặt lại trạng thái lựa chọn của bảng
//                    table.clearSelection();
//                } else {
//                    JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa nhân viên!");
//            }
//        }else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
//        }
    }
    private void suaNhanVien() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            // Lấy thông tin từ các trường nhập liệu
//            String maNV = (String) tableModel.getValueAt(selectedRow, 0);
//            String hoTen = txtHoTen.getText().trim();
//            String chucVu = cmbChucVu.getSelectedItem().toString();
//            String soDT = txtSoDT.getText().trim();
//            String diaChi = txtDiaChi.getText().trim();
//            String email = txtEmail.getText().trim();
//            double hsl = Double.parseDouble(txtHSL.getText());
//            double luong = Double.parseDouble(txtLuong.getText());
//            Date ngaySinh = dateNgaySinh.getDate();
//            Date ngayVaoLam = dateNgayVaoLam.getDate();
//
//            // Kiểm tra dữ liệu đầu vào trước khi tiếp tục
//            if (!kiemTraDuLieu()) {
//                return; // Nếu kiểm tra không hợp lệ, dừng lại và không tiếp tục
//            }
//
//            // Định dạng lại ngày sinh và ngày vào làm
//            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//            String formattedNgaySinh = ngaySinh != null ? dateFormat.format(ngaySinh) : "";
//            String formattedNgayVaoLam = ngayVaoLam != null ? dateFormat.format(ngayVaoLam) : "";
//
//            // Nếu dữ liệu hợp lệ, tiến hành cập nhật
//            try {
//                NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDT, diaChi, email, ngaySinh, ngayVaoLam, luong, hsl, 1);
//                if (NhanVien_DAO.suaNhanVien(nv)) {
//                    // Cập nhật lại thông tin trên bảng
//                    tableModel.setValueAt(hoTen, selectedRow, 1);
//                    tableModel.setValueAt(chucVu, selectedRow, 2);
//                    tableModel.setValueAt(soDT, selectedRow, 5);
//                    tableModel.setValueAt(diaChi, selectedRow, 6);
//                    tableModel.setValueAt(email, selectedRow, 7);
//                    tableModel.setValueAt(hsl, selectedRow, 9);
//                    tableModel.setValueAt(luong, selectedRow, 8);
//                    tableModel.setValueAt(ngaySinh, selectedRow, 3);
//                    tableModel.setValueAt(ngayVaoLam, selectedRow, 4);
//
//                    lamMoi(); // Làm mới giao diện
//
//                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thành công!");
//                } else {
//                    JOptionPane.showMessageDialog(this, "Cập nhật nhân viên thất bại!");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật nhân viên!");
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
//        }
    }

    private boolean kiemTraDuLieu() {
//        // Kiểm tra ngày sinh đủ 18 tuổi khi vào làm
//        Date ngaySinh = dateNgaySinh.getDate();
//        Date ngayVaoLam = dateNgayVaoLam.getDate();
//
//        // Kiểm tra nếu ngày vào làm trước ngày hiện tại
//        Date ngayHienTai = new Date();
//        if (ngayVaoLam.after(ngayHienTai)) {
//            JOptionPane.showMessageDialog(this, "Ngày vào làm phải trước ngày hiện tại!");
//            return false;
//        }
//
//        // Kiểm tra ngày sinh đủ 18 tuổi
//        long tuoi = (ngayVaoLam.getTime() - ngaySinh.getTime()) / (1000L * 60 * 60 * 24 * 365);
//        if (tuoi < 18) {
//            JOptionPane.showMessageDialog(this, "Ngày sinh phải đủ 18 tuổi khi vào làm!");
//            return false;
//        }
//
//        // Kiểm tra các ô còn lại bằng regex
//
//        // Kiểm tra số điện thoại
//        String soDT = txtSoDT.getText().trim();
//        String regexSoDT = "^(0)[0-9]{9}$"; // Số điện thoại bắt đầu bằng 0 và có 10 chữ số
//        if (!Pattern.matches(regexSoDT, soDT)) {
//            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!");
//            return false;
//        }
//
//        // Kiểm tra email
//        String email = txtEmail.getText().trim();
//        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"; // Kiểm tra email hợp lệ
//        if (!Pattern.matches(regexEmail, email)) {
//            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
//            return false;
//        }
//
//        // Kiểm tra email có bị trùng không
//        if (NhanVien_DAO.kiemTraTrungEmail(email)) {
//            JOptionPane.showMessageDialog(this, "Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác!");
//            return false;
//        }
//        // Kiểm tra tên đầy đủ (hoặc thêm các quy định khác)
//        String hoTen = txtHoTen.getText().trim();
//        if (hoTen.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!");
//            return false;
//        }

        return true; // Nếu tất cả các kiểm tra đều hợp lệ
    }

    private void themNhanVien() {
//        try {
//            if (!kiemTraDuLieu()) {
//                return; // Nếu dữ liệu không hợp lệ, không thực hiện thêm nhân viên
//            }
//
//            // Lấy dữ liệu từ giao diện
//            String hoTen = txtHoTen.getText().trim();
//            String chucVu = cmbChucVu.getSelectedItem().toString().trim();
//
//            // Lấy ngày sinh và ngày vào làm từ giao diện
//            Date ngaySinh = dateNgaySinh.getDate(); // Lấy trực tiếp từ JDateChooser
//            Date ngayVaoLam = dateNgayVaoLam.getDate(); // Lấy trực tiếp từ JDateChooser
//
//            String soDT = txtSoDT.getText().trim();
//            String diaChi = txtDiaChi.getText().trim();
//            String email = txtEmail.getText().trim();
//            double luongCoBan = Double.parseDouble(txtLuong.getText().trim());
//            double heSoLuong = Double.parseDouble(txtHSL.getText().trim());
//
//            // Tạo mã nhân viên tự động
//            String maNV = taoMaNhanVien(ngayVaoLam);
//
//            // Tạo đối tượng NhanVien
//            NhanVien nv = new NhanVien(maNV, hoTen, chucVu, soDT, diaChi, email, ngaySinh, ngayVaoLam, luongCoBan, heSoLuong, 1);
//
//            // Gọi phương thức thêm nhân viên
//            NhanVien_DAO nhanVienDAO = new NhanVien_DAO();
//            boolean kq = nhanVienDAO.themNhanVien(nv);
//
//            // Hiển thị thông báo
//            if (kq) {
//                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! Mã nhân viên: " + maNV);
//                lamMoi();
//            } else {
//                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
//            e.printStackTrace();
//        }
    }

    private String taoMaNhanVien(Date ngayVaoLam) {
        String maNV = "";
//        do {
//            SimpleDateFormat sdf = new SimpleDateFormat("yy");
//            String nam = sdf.format(ngayVaoLam);
//            int soNgauNhien = 10 + (int) (Math.random() * 90);
//            String kyTuNgauNhien = "";
//            for (int i = 0; i < 2; i++) {
//                char kyTu = (char) ('A' + (int) (Math.random() * 26));
//                kyTuNgauNhien += kyTu;
//            }
//
//            // Ghép thành mã nhân viên
//            maNV = nam + "-" + soNgauNhien + kyTuNgauNhien;
//        } while (danhSachMaNhanVien.contains(maNV)); // Kiểm tra trùng lặp
//
//        // Thêm mã vào danh sách để tránh trùng lặp
//        danhSachMaNhanVien.add(maNV);

        return maNV;
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
