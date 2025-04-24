package view.form;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.google.gson.reflect.TypeToken;
import dto.KhachHangDTO;
import dto.NhanVienDTO;
import dto.PhieuDatPhongDTO;
import dto.PhongDTO;
import socket.SocketManager;
import utils.custom_element.*;
import dao.*;
import model.*;
import org.jdesktop.swingx.JXDatePicker;

public class DatPhong_FORM extends JPanel implements Openable {
    private JTextField txtNgayDat;
    private int currentPage = 1;
    private final int rowsPerPage = 5;
    private DefaultTableModel tableModel;
    private DateTimePicker dateTimeNgayDen, dateTimeNgayDi;
    private JXDatePicker dateNgayDat, dateNgaySinh;
    private JComboBox<String> cmbLoaiPhong, cmbSoPhong;
    private JTextField txtSDT, txtTenKhachHang, txtDiaChi, txtEmail, txtCCCD, txtSoKhach;
    private PhieuDatPhong_DAO phieuDatPhongDAO;
    private Phong_DAO phongDAO;
    private JTextField txtSearch;
    private JTable table;
    private TaiKhoan_DAO taiKhoanDAO;
    private KhachHang_DAO khachHangDAO;
    //    @Override
    public void open() {
        khachHangDAO = new KhachHang_DAO();
        taiKhoanDAO = new TaiKhoan_DAO();
        phongDAO = new Phong_DAO();
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
        loadTableData();
    }

    public DatPhong_FORM() {

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
        txtSearch.addActionListener(e -> handleTimKiem());
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
        Box boxForm1 = createFormBox("Ngày đặt", dateNgayDat = new JXDatePicker());
        Box boxForm2 = createFormBox("Ngày đến", dateTimeNgayDen = new DateTimePicker());
        Box boxForm3 = createFormBox("Ngày đi", dateTimeNgayDi = new DateTimePicker());
        Box boxForm4 = createFormBox("CCCD", txtCCCD = new JTextField());
//        Box boxForm5 = createFormBox("Ngày sinh", dateNgaySinh = new JXDatePicker());
        Box boxForm6 = createFormBox("Tên khách hàng", txtTenKhachHang = new JTextField());
//        Box boxForm7 = createFormBox("Địa chỉ", txtDiaChi = new JTextField());
        Box boxForm8 = createFormBox("Email", txtEmail = new JTextField());
        Box boxForm9 = createFormBox("Số điện thoại", txtSDT = new JTextField());
        Box boxForm10 = createFormBox("Loại phòng", cmbLoaiPhong = new JComboBox<>(new String[]{"Chọn", "Phòng Deluxe", "Phòng Đôi", "Phòng Gia Đình", "Phòng Đơn"}));
        Box boxForm11 = createFormBox("Số phòng", cmbSoPhong = new JComboBox<>());
        Box boxForm12 = createFormBox("Số khách", txtSoKhach = new JTextField());
        dateTimeNgayDen.addActionListener(e -> handleChonLoaiPhong());
        dateTimeNgayDi.addActionListener(e -> handleChonLoaiPhong());
        cmbSoPhong.setEnabled(false);
        cmbLoaiPhong.addActionListener(e -> handleChonLoaiPhong());
        cmbSoPhong.addActionListener(e -> updateTenPhong((String) cmbSoPhong.getSelectedItem()));
        txtCCCD.addActionListener(e -> handleSearchCustomer());
        txtCCCD.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                handleSearchCustomer();
            }
        });

        form.add(boxForm1);
        form.add(boxForm2);
        form.add(boxForm3);
        form.add(boxForm4);
//        form.add(boxForm5);
        form.add(boxForm6);
//        form.add(boxForm7);
        form.add(boxForm8);
        form.add(boxForm9);
        form.add(boxForm10);
        form.add(boxForm11);
        form.add(boxForm12);
        northPanelBox.add(searchBox);
        northPanelBox.add(Box.createVerticalStrut(10));
        northPanelBox.add(form);
        northPanelBox.setPreferredSize(new Dimension(1642, 380));
        northPanel.add(northPanelBox);
        // center
        JLabel titleLabel = new JLabel("Lịch sử đặt phòng");
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


        String[] headers = {"Mã phiếu đặt phòng", "Số phòng", "Tên khách hàng", "Ngày đặt", "Ngày đến", "Ngày đi", "Nhân viên tạo phiếu", "Tình trạng"};
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

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        JButton submitButton = createButton("Xác nhận", new Color(66, 99, 235));
        JButton refreshButton = createButton("Làm mới", new Color(151, 69, 35));
        southPanel.add(submitButton);
        southPanel.add(refreshButton);
        add(northPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> handleSubmit());
        refreshButton.addActionListener(e -> handleRefresh());
        loadLoaiPhong();

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    fillFormFromSelectedRow();
                }
            }
        });
    }

    private void loadTableData() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            try {
                // Lấy danh sách khách hàng
                System.out.println("Gửi yêu cầu: GET_ALL_KHACH_HANG");
                Request<Void> khRequest = new Request<>("GET_ALL_KHACH_HANG", null);
                SocketManager.send(khRequest);
                Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
                Response<List<KhachHangDTO>> khResponse = SocketManager.receiveType(khachHangResponseType);
                if (!khResponse.isSuccess()) {
                    throw new Exception("Không thể lấy dữ liệu khách hàng: " + (khResponse.getData() != null ? khResponse.getData().toString() : "Không có thông báo lỗi"));
                }

                Map<String, String> khachHangMap = new HashMap<>();
                for (KhachHangDTO kh : khResponse.getData()) {
                    khachHangMap.put(kh.getMaKH(), kh.getHoTen());
                }

                // Lấy danh sách nhân viên
                System.out.println("Gửi yêu cầu: GET_ALL_NHAN_VIEN");
                Request<Void> nvRequest = new Request<>("GET_ALL_NHAN_VIEN", null);
                SocketManager.send(nvRequest);
                Type nhanVienResponseType = new TypeToken<Response<List<NhanVienDTO>>>() {}.getType();
                Response<List<NhanVienDTO>> nvResponse = SocketManager.receiveType(nhanVienResponseType);
                if (!nvResponse.isSuccess()) {
                    throw new Exception("Không thể lấy dữ liệu nhân viên: " + (nvResponse.getData() != null ? nvResponse.getData().toString() : "Không có thông báo lỗi"));
                }

                Map<String, String> nhanVienMap = new HashMap<>();
                for (NhanVienDTO nv : nvResponse.getData()) {
                    nhanVienMap.put(nv.getMaNhanVien(), nv.getHoTen());
                }

                // Lấy danh sách phòng để tra cứu tình trạng và loại phòng
                System.out.println("Gửi yêu cầu: GET_ALL_PHONG");
                Request<Void> phongRequest = new Request<>("GET_ALL_PHONG", null);
                SocketManager.send(phongRequest);
                Type phongResponseType = new TypeToken<Response<List<PhongDTO>>>() {}.getType();
                Response<List<PhongDTO>> phongResponse = SocketManager.receiveType(phongResponseType);
                if (!phongResponse.isSuccess()) {
                    throw new Exception("Không thể lấy dữ liệu phòng: " + (phongResponse.getData() != null ? phongResponse.getData().toString() : "Không có thông báo lỗi"));
                }

                Map<String, Integer> phongTinhTrangMap = new HashMap<>();
                Map<String, String> phongLoaiPhongMap = new HashMap<>();
                for (PhongDTO phong : phongResponse.getData()) {
                    phongTinhTrangMap.put(phong.getMaPhong(), phong.getTinhTrang());
                    String tenLoai = "Không xác định";
                    if (phong.getMaLoai() != null && !phong.getMaLoai().isEmpty()) {
                        // Gửi yêu cầu lấy tenLoai dựa trên maLoai
                        System.out.println("Gửi yêu cầu GET_TEN_LOAI_BY_MA_LOAI cho maLoai: " + phong.getMaLoai());
                        Request<String> tenLoaiRequest = new Request<>("GET_TEN_LOAI_BY_MA_LOAI", phong.getMaLoai());
                        SocketManager.send(tenLoaiRequest);
                        Type tenLoaiResponseType = new TypeToken<Response<String>>() {}.getType();
                        Response<String> tenLoaiResponse = SocketManager.receiveType(tenLoaiResponseType);
                        if (tenLoaiResponse.isSuccess() && tenLoaiResponse.getData() != null) {
                            tenLoai = tenLoaiResponse.getData();
                        } else {
                            System.out.println("Không lấy được tenLoai cho maLoai: " + phong.getMaLoai() + ", maPhong: " + phong.getMaPhong());
                        }
                    } else {
                        System.out.println("maLoai null hoặc rỗng cho maPhong: " + phong.getMaPhong());
                    }
                    phongLoaiPhongMap.put(phong.getMaPhong(), tenLoai);
                    System.out.println("Lưu tenLoai: " + tenLoai + " cho maPhong: " + phong.getMaPhong());
                }

                // Lấy danh sách phiếu đặt phòng với phòng có tình trạng "Đã đặt" (tinh_trang = 1)
                System.out.println("Gửi yêu cầu: GET_PHIEU_DAT_PHONG_DA_DAT");
                Request<Void> request = new Request<>("GET_PHIEU_DAT_PHONG_DA_DAT", null);
                SocketManager.send(request);

                Type pdpResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>() {}.getType();
                Response<List<PhieuDatPhongDTO>> pdpResponse = SocketManager.receiveType(pdpResponseType);

                if (!pdpResponse.isSuccess()) {
                    String message = pdpResponse.getData() != null ? pdpResponse.getData().toString() : "Không có thông báo lỗi từ server";
                    JOptionPane.showMessageDialog(this, "Lỗi server: " + message, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<PhieuDatPhongDTO> pdpListDTO = pdpResponse.getData();
                if (pdpListDTO == null || pdpListDTO.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy phiếu đặt phòng nào cho phòng đã đặt", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Hiển thị từng mã phòng riêng lẻ
                for (PhieuDatPhongDTO pdp : pdpListDTO) {
                    String maPDP = pdp.getMaPDP() != null ? pdp.getMaPDP() : "";
                    String maKH = pdp.getMaKH() != null ? pdp.getMaKH() : "";
                    String maNV = pdp.getMaNV() != null ? pdp.getMaNV() : "";
                    List<String> dsMaPhong = pdp.getDsMaPhong() != null ? pdp.getDsMaPhong() : new ArrayList<>();
                    LocalDate ngayDat = pdp.getNgayDatPhong();
                    LocalDate ngayDen = pdp.getNgayNhanPhongDuKien();
                    LocalDate ngayDi = pdp.getNgayTraPhongDuKien();

                    String tenKhachHang = khachHangMap.getOrDefault(maKH, "");
                    String tenNhanVien = nhanVienMap.getOrDefault(maNV, "");

                    for (String maPhong : dsMaPhong) {
                        if (!phongTinhTrangMap.containsKey(maPhong)) {
                            System.out.println("Bỏ qua maPhong không hợp lệ: " + maPhong);
                            continue; // Bỏ qua maPhong không tồn tại
                        }

                        Integer tinhTrangPhong = phongTinhTrangMap.getOrDefault(maPhong, -1);
                        String tinhTrangText = switch (tinhTrangPhong) {
                            case 0 -> "Còn trống";
                            case 1 -> "Đã đặt";
                            case 2 -> "Đang sử dụng";
                            case 3 -> "Đang dọn dẹp";
                            case 4 -> "Đang bảo trì";
                            case 5 -> "Tạm khóa";
                            default -> "Không xác định";
                        };

                        // Sử dụng Timestamp cho tất cả các ngày
                        Timestamp ngayDatTimestamp = ngayDat != null ? Timestamp.valueOf(ngayDat.atStartOfDay()) : null;
                        Timestamp ngayDenTimestamp = ngayDen != null ? Timestamp.valueOf(ngayDen.atStartOfDay()) : null;
                        Timestamp ngayDiTimestamp = ngayDi != null ? Timestamp.valueOf(ngayDi.atStartOfDay()) : null;

                        String tenLoai = phongLoaiPhongMap.getOrDefault(maPhong, "Không xác định");
                        System.out.println("Thêm dòng với maPhong: " + maPhong + ", tenLoai: " + tenLoai);

                        Object[] row = new Object[]{
                                maPDP,
                                maPhong,
                                tenKhachHang,
                                ngayDatTimestamp,
                                ngayDenTimestamp,
                                ngayDiTimestamp,
                                tenNhanVien,
                                tinhTrangText
                        };
                        tableModel.addRow(row);
                        System.out.println("Row: " + Arrays.toString(row));
                    }
                }

                table.repaint();
                table.revalidate();
            } catch (Exception e) {
                String errorMessage = "Lỗi khi tải dữ liệu: " + e.getMessage();
                System.out.println(errorMessage);
                JOptionPane.showMessageDialog(this, errorMessage, "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void handleTimKiem() {
        String tuKhoa = txtSearch.getText().trim().toLowerCase();
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
        if (tuKhoa.equals("Tìm kiếm") || tuKhoa.isEmpty()) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String maPDP = model.getValueAt(i, 0).toString().toLowerCase();
            if (maPDP.contains(tuKhoa)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                break;
            }
        }
    }

    private void handleChonLoaiPhong() {
        String selectedLoaiPhong = (String) cmbLoaiPhong.getSelectedItem();
        if (!selectedLoaiPhong.equals("Chọn")) {
            updateSoPhongList(selectedLoaiPhong);
            cmbSoPhong.setEnabled(true);
        } else {
            cmbSoPhong.removeAllItems();
            cmbSoPhong.setEnabled(false);
        }
    }

    private void updateSoPhongList(String loaiPhong) {
//        Date ngayDen = dateTimeNgayDen.getDate();
//        Date ngayDi = dateTimeNgayDi.getDate();
//
//        cmbSoPhong.removeAllItems();
//        cmbSoPhong.addItem("Chọn");
//        ArrayList<String> dsSoPhong = phongDAO.getSoPhongByLoaiPhong(loaiPhong);
//
//        ArrayList<String> dsSoPhongDaDat = phieuDatPhongDAO.getSoPhongDaDat(ngayDen, ngayDi);
//
//        for (String soPhong : dsSoPhong) {
//            if (!dsSoPhongDaDat.contains(soPhong)) {
//                cmbSoPhong.addItem(soPhong);
//            }
//        }
    }

    private void updateTenPhong(String soPhong) {
//        Phong_DAO phongDAO = new Phong_DAO();
//        String tenPhong = phongDAO.getTenPhongBySoPhong(soPhong);
    }

    private void handleSearchCustomer() {
//        String cccd = txtCCCD.getText().trim();
//        if (!cccd.isEmpty()) {
//            KhachHang khachHang = khachHangDAO.searchKhachHangBangCCCD(cccd);
//
//            if (khachHang != null) {
//                txtTenKhachHang.setText(khachHang.getHoTen());
//                txtDiaChi.setText(khachHang.getDiaChi());
//                txtEmail.setText(khachHang.getEmail());
//                txtSDT.setText(khachHang.getSdt());
//                dateNgaySinh.setDate(khachHang.getNgaySinh());
//            }
//        }
    }

    private void loadLoaiPhong() {
//        LoaiPhong_DAO loaiPhongDAO = new LoaiPhong_DAO();
//        ArrayList<LoaiPhong> dsLoaiPhong = loaiPhongDAO.getDSLoaiPhong();
//
//        for (LoaiPhong loaiPhong : dsLoaiPhong) {
//            cmbLoaiPhong.addItem(loaiPhong.getTenLoaiPhong());
//        }

    }

    private JButton createButton(String buttonLabel, Color buttonColor) {
        JButton button = new JButton(buttonLabel);
        button.setBackground(buttonColor);
        button.setForeground(Color.white);
        button.setPreferredSize(new Dimension(259, 45));
        button.setFont(FontManager.getManrope(Font.PLAIN, 16));
        return button;
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

    private Box createFormBox(String label, DateTimePicker dateTime) {
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
        dateTime.setPreferredSize(new Dimension(331, 45));
        dateTime.setMaximumSize(new Dimension(331, 45));
        dateTime.setMinimumSize(new Dimension(331, 45));
        dateTime.setFont(FontManager.getManrope(Font.PLAIN, 14));
        Border emptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        dateTime.setBorder(emptyBorder);
        dateTime.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
        dateTime.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));

        dateTime.setDate(date);

        lblBox.add(Box.createHorizontalStrut(10));
        lblBox.add(lbl);
        lblBox.add(Box.createHorizontalGlue());
        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(dateTime);
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
        datePicker.setFormats("MM/dd/yyyy");
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

    private String getRandomMa() {
        Random random = new Random();
        StringBuilder letters = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            char randomLetter = (char) ('A' + random.nextInt(26));
            letters.append(randomLetter);
        }

        StringBuilder numbers = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            int randomNumber = random.nextInt(10);
            numbers.append(randomNumber);
        }
        return letters.toString() + "-" + numbers.toString();
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // Lấy dữ liệu từ dòng được chọn
            String maPDP = (String) table.getValueAt(selectedRow, 0);
            String maPhong = (String) table.getValueAt(selectedRow, 1);
            String tenKhachHang = (String) table.getValueAt(selectedRow, 2);
            Timestamp ngayDat = (Timestamp) table.getValueAt(selectedRow, 3);
            Timestamp ngayDen = (Timestamp) table.getValueAt(selectedRow, 4);
            Timestamp ngayDi = (Timestamp) table.getValueAt(selectedRow, 5);
            String tenNhanVien = (String) table.getValueAt(selectedRow, 6);

            // Cập nhật các trường nhập liệu
            txtTenKhachHang.setText(tenKhachHang != null ? tenKhachHang : "");

            // Truy vấn thông tin khách hàng dựa trên tenKhachHang
            if (tenKhachHang != null && !tenKhachHang.isEmpty()) {
                try {
                    System.out.println("Gửi yêu cầu TIM_KHACH_HANG_THEO_TEN cho tenKhachHang: " + tenKhachHang);
                    Request<String> khRequest = new Request<>("TIM_KHACH_HANG_THEO_TEN", tenKhachHang);
                    SocketManager.send(khRequest);
                    Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
                    Response<List<KhachHangDTO>> khResponse = SocketManager.receiveType(khachHangResponseType);

                    if (khResponse != null && khResponse.isSuccess() && khResponse.getData() != null && !khResponse.getData().isEmpty()) {
                        // Lấy khách hàng đầu tiên khớp với tên
                        KhachHangDTO khachHang = khResponse.getData().get(0);
                        txtCCCD.setText(khachHang.getSoCCCD() != null ? khachHang.getSoCCCD() : "");
                        txtEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
                        txtSDT.setText(khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "");
                        System.out.println("Đã điền thông tin khách hàng: CCCD=" + khachHang.getSoCCCD() + ", Email=" + khachHang.getEmail() + ", SDT=" + khachHang.getSoDienThoai());
                    } else {
                        String errorMessage = khResponse != null && khResponse.getData() != null
                                ? khResponse.getData().toString()
                                : "Không tìm thấy thông tin khách hàng với tên: " + tenKhachHang;
                        System.out.println("Lỗi: " + errorMessage);
                        // Xóa các trường nếu không tìm thấy khách hàng
                        txtCCCD.setText("");
                        txtEmail.setText("");
                        txtSDT.setText("");
                    }
                } catch (Exception e) {
                    String errorMessage = "Lỗi khi truy vấn thông tin khách hàng: " + e.getMessage();
                    System.out.println(errorMessage);
                    JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    // Xóa các trường nếu có lỗi
                    txtCCCD.setText("");
                    txtEmail.setText("");
                    txtSDT.setText("");
                }
            } else {
                // Nếu tenKhachHang rỗng, xóa các trường
                txtCCCD.setText("");
                txtEmail.setText("");
                txtSDT.setText("");
            }

            // Cập nhật các trường ngày
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateNgayDat.setDate(ngayDat != null ? new Date(ngayDat.getTime()) : null);
            dateTimeNgayDen.getEditor().setText(ngayDen != null ? dateFormat.format(ngayDen) : "");
            dateTimeNgayDen.setTimeSpinners();
            dateTimeNgayDi.getEditor().setText(ngayDi != null ? dateFormat.format(ngayDi) : "");
            dateTimeNgayDi.setTimeSpinners();

            // Lấy tên loại phòng từ mã phòng
            String tenLoai = layTenLoaiPhong(maPhong);
            if (tenLoai == null) return;

            capNhatComboLoaiPhong(tenLoai);
            capNhatComboSoPhong(tenLoai, maPhong);

        } catch (Exception e) {
            String errorMessage = "Lỗi khi điền dữ liệu từ dòng được chọn: " + e.getMessage();
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String layTenLoaiPhong(String maPhong) {
        try {
            Request<String> request = new Request<>("GET_TEN_LOAI_PHONG_BY_MA_PHONG", maPhong);
            SocketManager.send(request);
            Type type = new TypeToken<Response<String>>() {}.getType();
            Response<String> response = SocketManager.receiveType(type);

            if (response != null && response.isSuccess()) {
                return response.getData();
            } else {
                String error = response != null ? response.getData().toString() : "Không có phản hồi từ server.";
                JOptionPane.showMessageDialog(this, "Lỗi lấy tên loại phòng: " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi lấy tên loại phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void capNhatComboLoaiPhong(String tenLoai) {
        boolean exists = false;
        for (int i = 0; i < cmbLoaiPhong.getItemCount(); i++) {
            if (tenLoai.equals(cmbLoaiPhong.getItemAt(i))) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            cmbLoaiPhong.addItem(tenLoai);
        }
        cmbLoaiPhong.setSelectedItem(tenLoai);
    }

    private void capNhatComboSoPhong(String tenLoai, String maPhong) {
        try {
            Request<String> request = new Request<>("GET_SO_PHONG_BY_LOAI_PHONG", tenLoai);
            SocketManager.send(request);
            Type type = new TypeToken<Response<List<String>>>() {}.getType();
            Response<List<String>> response = SocketManager.receiveType(type);

            if (response != null && response.isSuccess() && response.getData() != null) {
                List<String> dsSoPhong = response.getData();
                cmbSoPhong.removeAllItems();
                for (String soPhong : dsSoPhong) {
                    cmbSoPhong.addItem(soPhong);
                }
                cmbSoPhong.setSelectedItem(maPhong);
                cmbSoPhong.setEnabled(true);
            } else {
                String error = response != null ? response.getData().toString() : "Phản hồi từ server là null.";
                JOptionPane.showMessageDialog(this, "Không thể lấy danh sách phòng: " + error, "Lỗi", JOptionPane.ERROR_MESSAGE);
                cmbSoPhong.removeAllItems();
                cmbSoPhong.setEnabled(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống khi lấy danh sách số phòng: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            cmbSoPhong.removeAllItems();
            cmbSoPhong.setEnabled(false);
        }
    }

    private void handleSubmit() {
//        Date ngayDat = dateNgayDat.getDate();
//        Date ngayDen = dateTimeNgayDen.getDate();
//        Date ngayDi = dateTimeNgayDi.getDate();
//        String sdt = txtSDT.getText().trim();
//        Date ngaySinh = dateNgaySinh.getDate();
//        String tenKhachHang = txtTenKhachHang.getText().trim();
//        String email = txtEmail.getText().trim();
//        String diaChi = txtDiaChi.getText().trim();
//        String cccd = txtCCCD.getText().trim();
//        String loaiPhong = (String) cmbLoaiPhong.getSelectedItem();
//        String soPhong = (String) cmbSoPhong.getSelectedItem();
//        String soKhach = txtSoKhach.getText().trim();
//
//        if (ngayDat == null || ngayDen == null || ngayDi == null || ngaySinh == null || sdt.isEmpty() || tenKhachHang.isEmpty() || cccd.isEmpty() || loaiPhong.equals("Chọn") || soPhong.equals("Chọn") || soKhach.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng điền đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//
//        if (!ngayDi.after(ngayDen)) {
//            JOptionPane.showMessageDialog(this, "Ngày đi phải sau ngày đến.", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        Phong p = new Phong(soPhong);
//        NhanVien nv = new NhanVien(taiKhoanDAO.getCurrentNhanVien().getMaNV(), taiKhoanDAO.getCurrentNhanVien().getHoTen());
//
//        KhachHang kh = null;
//        if (khachHangDAO.doesCustomerExist(cccd)) {
//            kh = khachHangDAO.searchKhachHangBangCCCD(cccd);
//            kh.setSdt(sdt);
//            kh.setEmail(email);
//            kh.setDiaChi(diaChi);
//        } else {
//            String maKH = getRandomMa();
//            while (khachHangDAO.doesCustomerIdExist(maKH)) {
//                maKH = getRandomMa();
//            }
//            kh = new KhachHang(maKH, tenKhachHang, diaChi, sdt, email, cccd, 1, ngaySinh);
//        }
//        String maPDP = getRandomMa();
//        while (phieuDatPhongDAO.doesPDPIdExist(maPDP)){
//            maPDP = getRandomMa();
//        }
//        PhieuDatPhong phieuDatPhong = new PhieuDatPhong(maPDP, p, nv, kh, ngayDi, ngayDen, ngayDat, 0);
//        if (phieuDatPhongDAO.datPhong(phieuDatPhong)) {
//            JOptionPane.showMessageDialog(this, "Đã đặt phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//            tableModel.insertRow(0, new Object[]{
//                    maPDP,
//                    soPhong,
//                    tenKhachHang,
//                    new java.sql.Date(ngayDat.getTime()),
//                    new Timestamp(ngayDen.getTime()),
//                    new Timestamp(ngayDi.getTime()),
//                    nv.getHoTen(),
//                    phieuDatPhong.getTinhTrangPDP()
//            });
//            handleRefresh();
//        } else {
//            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi đặt phòng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void handleRefresh() {
        dateNgayDat.setDate(new Date());
        dateTimeNgayDen.setDate(new Date());
        dateTimeNgayDi.setDate(new Date());
        dateNgaySinh.setDate(new Date());
        txtSDT.setText("");
        txtTenKhachHang.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtCCCD.setText("");
        cmbLoaiPhong.setSelectedIndex(0);
        txtSoKhach.setText("");
    }
}
