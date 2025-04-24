package view.form;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.List;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.*;
import socket.SocketManager;
import utils.custom_element.*;
import dao.*;
import model.*;
import org.jdesktop.swingx.JXDatePicker;
import utils.session.SessionManager;

public class DatPhong_FORM extends JPanel implements Openable {
    private JTextField txtNgayDat;
    private int currentPage = 1;
    private final int rowsPerPage = 5;
    private DefaultTableModel tableModel;
    private DateTimePicker dateTimeNgayDen, dateTimeNgayDi;
    private JXDatePicker dateNgayDat;
    private JComboBox<String> cmbLoaiPhong, cmbSoPhong;
    private JTextField txtSDT, txtTenKhachHang, txtEmail, txtCCCD, txtSoKhach;
    private PhieuDatPhong_DAO phieuDatPhongDAO;
    private Phong_DAO phongDAO;
    private JTextField txtSearch;
    private JTable table;
    private TaiKhoan_DAO taiKhoanDAO;
    private KhachHang_DAO khachHangDAO;
    private boolean isUpdating = false;

    @Override
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
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        Box northPanelBox = Box.createVerticalBox();
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
        Box boxForm1 = createFormBox("Ngày đặt", dateNgayDat = new JXDatePicker());
        Box boxForm2 = createFormBox("Ngày đến", dateTimeNgayDen = new DateTimePicker());
        Box boxForm3 = createFormBox("Ngày đi", dateTimeNgayDi = new DateTimePicker());
        Box boxForm4 = createFormBox("CCCD", txtCCCD = new JTextField());
        Box boxForm6 = createFormBox("Tên khách hàng", txtTenKhachHang = new JTextField());
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

        DocumentListener searchListener = new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                if (!isUpdating && e.getDocument() == txtCCCD.getDocument()) {
                    handleSearchCustomer();
                }
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                if (!isUpdating && e.getDocument() == txtCCCD.getDocument()) {
                    handleSearchCustomer();
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                if (!isUpdating && e.getDocument() == txtCCCD.getDocument()) {
                    handleSearchCustomer();
                }
            }
        };

        txtCCCD.getDocument().addDocumentListener(searchListener);

        form.add(boxForm1);
        form.add(boxForm2);
        form.add(boxForm3);
        form.add(boxForm4);
        form.add(boxForm6);
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
        setupFocusLossOnClick();

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
                            continue;
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
        if (selectedLoaiPhong != null && !selectedLoaiPhong.equals("Chọn")) {
            updateSoPhongList(selectedLoaiPhong);
            cmbSoPhong.setEnabled(true);
        } else {
            cmbSoPhong.removeAllItems();
            cmbSoPhong.setEnabled(false);
        }
    }

    private void updateSoPhongList(String loaiPhong) {
        try {
            Request<Void> phongRequest = new Request<>("GET_ALL_PHONG", null);
            SocketManager.send(phongRequest);
            Type phongResponseType = new TypeToken<Response<List<PhongDTO>>>() {}.getType();
            Response<List<PhongDTO>> phongResponse = SocketManager.receiveType(phongResponseType);

            if (!phongResponse.isSuccess() || phongResponse.getData() == null) {
                JOptionPane.showMessageDialog(this, "Không thể lấy danh sách phòng: " + (phongResponse.getData() != null ? phongResponse.getData().toString() : "Không có phản hồi từ server"), "Lỗi", JOptionPane.ERROR_MESSAGE);
                cmbSoPhong.removeAllItems();
                cmbSoPhong.setEnabled(false);
                return;
            }

            Map<String, String> loaiPhongMap = new HashMap<>();
            for (PhongDTO phong : phongResponse.getData()) {
                String tenLoai = "Không xác định";
                if (phong.getMaLoai() != null && !phong.getMaLoai().isEmpty()) {
                    Request<String> tenLoaiRequest = new Request<>("GET_TEN_LOAI_BY_MA_LOAI", phong.getMaLoai());
                    SocketManager.send(tenLoaiRequest);
                    Type tenLoaiResponseType = new TypeToken<Response<String>>() {}.getType();
                    Response<String> tenLoaiResponse = SocketManager.receiveType(tenLoaiResponseType);
                    if (tenLoaiResponse.isSuccess() && tenLoaiResponse.getData() != null) {
                        tenLoai = tenLoaiResponse.getData();
                    }
                }
                loaiPhongMap.put(phong.getMaPhong(), tenLoai);
            }

            List<String> dsMaPhong = phongResponse.getData().stream()
                    .filter(phong -> phong.getTinhTrang() == 0)
                    .filter(phong -> loaiPhong.equals(loaiPhongMap.get(phong.getMaPhong())))
                    .map(PhongDTO::getMaPhong)
                    .collect(Collectors.toList());

            cmbSoPhong.removeAllItems();
            if (dsMaPhong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có phòng nào còn trống thuộc loại " + loaiPhong, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                cmbSoPhong.setEnabled(false);
            } else {
                for (String maPhong : dsMaPhong) {
                    cmbSoPhong.addItem(maPhong);
                }
                cmbSoPhong.setEnabled(true);
            }

        } catch (Exception e) {
            String errorMessage = "Lỗi khi cập nhật danh sách số phòng: " + e.getMessage();
            JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            cmbSoPhong.removeAllItems();
            cmbSoPhong.setEnabled(false);
        }
    }

    private void updateTenPhong(String soPhong) {
        // Giữ nguyên
    }

    private KhachHangDTO handleSearchCustomer() {
        String cccd = txtCCCD.getText().trim();
        if (cccd.isEmpty()) {
            return null;
        }

        try {
            System.out.println("Tìm kiếm khách hàng theo CCCD: " + cccd);
            Request<String> request = new Request<>("TIM_KHACH_HANG_THEO_CCCD", cccd);
            SocketManager.send(request);
            Type type = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
            Response<List<KhachHangDTO>> response = SocketManager.receiveType(type);

            KhachHangDTO khachHang = null;
            if (response != null && response.isSuccess() && response.getData() != null && !response.getData().isEmpty()) {
                khachHang = response.getData().get(0);
                System.out.println("Tìm thấy khách hàng: " + khachHang.getHoTen());

                isUpdating = true;
                try {
                    txtTenKhachHang.setText(khachHang.getHoTen() != null ? khachHang.getHoTen() : "");
                    txtSDT.setText(khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "");
                    txtEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
                } finally {
                    isUpdating = false;
                }
            }

            return khachHang;

        } catch (Exception e) {
            String errorMessage = "Lỗi khi tìm kiếm khách hàng: " + e.getMessage();
            System.out.println(errorMessage);
            JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }

    private String generateMaKH() {
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;
        return "KH" + randomNumber;
    }

    private void loadLoaiPhong() {
        // Giữ nguyên
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
        String maPDP;
        Random random = new Random();
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // Tập hợp ký tự để tạo ngẫu nhiên

        do {
            StringBuilder suffix = new StringBuilder();
            // Tạo 8 ký tự ngẫu nhiên (chữ cái in hoa và số)
            for (int i = 0; i < 8; i++) {
                int index = random.nextInt(CHARACTERS.length());
                suffix.append(CHARACTERS.charAt(index));
            }
            // Ghép tiền tố PDP với 8 ký tự ngẫu nhiên
            maPDP = "PDP" + suffix.toString();

            try {
                // Kiểm tra mã có tồn tại chưa
                Request<String> checkRequest = new Request<>("CHECK_PHIEU_DAT_PHONG_EXISTS", maPDP);
                SocketManager.send(checkRequest);
                Type checkResponseType = new TypeToken<Response<Boolean>>() {}.getType();
                Response<Boolean> checkResponse = SocketManager.receiveType(checkResponseType);
                if (checkResponse.isSuccess() && checkResponse.getData()) {
                    maPDP = null; // Nếu mã đã tồn tại, thử tạo mã mới
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } while (maPDP == null);
        return maPDP;
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            String maPDP = (String) table.getValueAt(selectedRow, 0);
            String maPhong = (String) table.getValueAt(selectedRow, 1);
            String tenKhachHang = (String) table.getValueAt(selectedRow, 2);
            Timestamp ngayDat = (Timestamp) table.getValueAt(selectedRow, 3);
            Timestamp ngayDen = (Timestamp) table.getValueAt(selectedRow, 4);
            Timestamp ngayDi = (Timestamp) table.getValueAt(selectedRow, 5);
            String tenNhanVien = (String) table.getValueAt(selectedRow, 6);

            isUpdating = true;
            try {
                txtTenKhachHang.setText(tenKhachHang != null ? tenKhachHang : "");
            } finally {
                isUpdating = false;
            }

            if (tenKhachHang != null && !tenKhachHang.isEmpty()) {
                try {
                    System.out.println("Gửi yêu cầu TIM_KHACH_HANG_THEO_TEN cho tenKhachHang: " + tenKhachHang);
                    Request<String> khRequest = new Request<>("TIM_KHACH_HANG_THEO_TEN", tenKhachHang);
                    SocketManager.send(khRequest);
                    Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
                    Response<List<KhachHangDTO>> khResponse = SocketManager.receiveType(khachHangResponseType);

                    if (khResponse != null && khResponse.isSuccess() && khResponse.getData() != null && !khResponse.getData().isEmpty()) {
                        KhachHangDTO khachHang = khResponse.getData().get(0);
                        isUpdating = true;
                        try {
                            txtCCCD.setText(khachHang.getSoCCCD() != null ? khachHang.getSoCCCD() : "");
                            txtEmail.setText(khachHang.getEmail() != null ? khachHang.getEmail() : "");
                            txtSDT.setText(khachHang.getSoDienThoai() != null ? khachHang.getSoDienThoai() : "");
                        } finally {
                            isUpdating = false;
                        }
                        System.out.println("Đã điền thông tin khách hàng: CCCD=" + khachHang.getSoCCCD() + ", Email=" + khachHang.getEmail() + ", SDT=" + khachHang.getSoDienThoai());
                    } else {
                        String errorMessage = khResponse != null && khResponse.getData() != null
                                ? khResponse.getData().toString()
                                : "Không tìm thấy thông tin khách hàng với tên: " + tenKhachHang;
                        System.out.println("Lỗi: " + errorMessage);
                        isUpdating = true;
                        try {
                            txtCCCD.setText("");
                            txtEmail.setText("");
                            txtSDT.setText("");
                        } finally {
                            isUpdating = false;
                        }
                    }
                } catch (Exception e) {
                    String errorMessage = "Lỗi khi truy vấn thông tin khách hàng: " + e.getMessage();
                    System.out.println(errorMessage);
                    JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    isUpdating = true;
                    try {
                        txtCCCD.setText("");
                        txtEmail.setText("");
                        txtSDT.setText("");
                    } finally {
                        isUpdating = false;
                    }
                }
            } else {
                isUpdating = true;
                try {
                    txtCCCD.setText("");
                    txtEmail.setText("");
                    txtSDT.setText("");
                } finally {
                    isUpdating = false;
                }
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateNgayDat.setDate(ngayDat != null ? new Date(ngayDat.getTime()) : null);
            dateTimeNgayDen.getEditor().setText(ngayDen != null ? dateFormat.format(ngayDen) : "");
            dateTimeNgayDen.setTimeSpinners();
            dateTimeNgayDi.getEditor().setText(ngayDi != null ? dateFormat.format(ngayDi) : "");
            dateTimeNgayDi.setTimeSpinners();

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

    private boolean isValidCCCD(String cccd) {
        return cccd != null && cccd.matches("\\d{12}");
    }

    private boolean isValidTenKhachHang(String ten) {
        return ten != null && ten.matches("[a-zA-Z\\s0-9]+");
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$");
    }

    private boolean isValidSDT(String sdt) {
        return sdt != null && sdt.matches("\\d{10}");
    }

    // Thêm phương thức addRowToTable vào DatPhong_FORM.java
    private void addRowToTable(PhieuDatPhongDTO pdp, String tenKhachHang, String maPhong, String tinhTrangText) {
        NhanVien currentUser = SessionManager.getCurrentUser();
        if (currentUser == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhân viên đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        System.out.println("Nhân viên hiện tại: maNV=" + currentUser.getMaNhanVien() + ", hoTen=" + currentUser.getHoTen());
        String tenNhanVien = currentUser.getHoTen() != null ? currentUser.getHoTen() : "Không xác định";

        String maPDP = pdp.getMaPDP() != null ? pdp.getMaPDP() : "";
        LocalDate ngayDat = pdp.getNgayDatPhong();
        LocalDate ngayDen = pdp.getNgayNhanPhongDuKien();
        LocalDate ngayDi = pdp.getNgayTraPhongDuKien();

        Timestamp ngayDatTimestamp = ngayDat != null ? Timestamp.valueOf(ngayDat.atStartOfDay()) : null;
        Timestamp ngayDenTimestamp = ngayDen != null ? Timestamp.valueOf(ngayDen.atStartOfDay()) : null;
        Timestamp ngayDiTimestamp = ngayDi != null ? Timestamp.valueOf(ngayDi.atStartOfDay()) : null;

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
        System.out.println("Thêm hàng mới: " + Arrays.toString(row));

        table.repaint();
        table.revalidate();
    }

    // Sửa phương thức handleSubmit
    private void handleSubmit() {
        try {
            // 1. Lấy dữ liệu từ form
            String tenKhachHang = txtTenKhachHang.getText().trim();
            String cccd = txtCCCD.getText().trim();
            String sdt = txtSDT.getText().trim();
            String email = txtEmail.getText().trim();
            String soPhong = (String) cmbSoPhong.getSelectedItem();

            // 2. Kiểm tra dữ liệu đầu vào
            if (tenKhachHang.isEmpty() || cccd.isEmpty() || sdt.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (soPhong == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn số phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateNgayDat.getDate() == null || dateTimeNgayDen.getDate() == null || dateTimeNgayDi.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ ngày đặt, ngày đến và ngày đi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidCCCD(cccd)) {
                JOptionPane.showMessageDialog(this, "Số CCCD phải đủ 12 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidTenKhachHang(tenKhachHang)) {
                JOptionPane.showMessageDialog(this, "Tên khách hàng không được chứa ký tự đặc biệt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidEmail(email)) {
                JOptionPane.showMessageDialog(this, "Email phải có định dạng hợp lệ (ví dụ: example@gmail.com)!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidSDT(sdt)) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải đủ 10 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 3. Kiểm tra ràng buộc ngày
            LocalDate ngayHienTai = LocalDate.now();
            LocalDate ngayDat = dateNgayDat.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayDen = dateTimeNgayDen.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayDi = dateTimeNgayDi.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (ngayDat.isBefore(ngayHienTai)) {
                JOptionPane.showMessageDialog(this, "Ngày đặt phải bằng hoặc lớn hơn ngày hiện tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (ngayDen.isBefore(ngayDat)) {
                JOptionPane.showMessageDialog(this, "Ngày đến phải bằng hoặc sau ngày đặt!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!ngayDi.isAfter(ngayDen)) {
                JOptionPane.showMessageDialog(this, "Ngày đi phải sau ngày đến!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 4. Kiểm tra khách hàng
            KhachHangDTO khachHang = handleSearchCustomer();
            String maKH;

            if (khachHang == null) {
                // Tạo khách hàng mới
                KhachHangDTO newCustomer = new KhachHangDTO();
                newCustomer.setMaKH(generateMaKH());
                newCustomer.setHoTen(tenKhachHang);
                newCustomer.setSoCCCD(cccd);
                newCustomer.setSoDienThoai(sdt);
                newCustomer.setEmail(email);

                Request<KhachHangDTO> createCustomerRequest = new Request<>("THEM_KHACH_HANG_BOOLEAN", newCustomer);
                SocketManager.send(createCustomerRequest);
                Type createResponseType = new TypeToken<Response<Boolean>>() {}.getType();
                Response<Boolean> createResponse = SocketManager.receiveType(createResponseType);

                if (!createResponse.isSuccess() || !createResponse.getData()) {
                    JOptionPane.showMessageDialog(this, "Tạo khách hàng mới thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                maKH = newCustomer.getMaKH();
                System.out.println("Tạo khách hàng mới thành công: " + maKH);
            } else {
                // Sử dụng khách hàng hiện có
                maKH = khachHang.getMaKH();
                System.out.println("Sử dụng khách hàng hiện có: " + maKH);
            }

            // 5. Tạo phiếu đặt phòng
            PhieuDatPhongDTO phieuDatPhong = new PhieuDatPhongDTO();
            phieuDatPhong.setMaPDP(getRandomMa());
            phieuDatPhong.setMaKH(maKH);
            phieuDatPhong.setMaNV(SessionManager.getCurrentUser().getMaNhanVien());
            phieuDatPhong.setDsMaPhong(Arrays.asList(soPhong));
            phieuDatPhong.setNgayDatPhong(ngayDat);
            phieuDatPhong.setNgayNhanPhongDuKien(ngayDen);
            phieuDatPhong.setNgayTraPhongDuKien(ngayDi);
            phieuDatPhong.setMaHD("HD-" + UUID.randomUUID().toString());

            // 6. Cập nhật trạng thái phòng
            Request<String> phongRequest = new Request<>("GET_PHONG_BY_MA_PHONG", soPhong);
            SocketManager.send(phongRequest);
            Type phongResponseType = new TypeToken<Response<PhongDTO>>() {}.getType();
            Response<PhongDTO> phongResponse = SocketManager.receiveType(phongResponseType);

            if (!phongResponse.isSuccess() || phongResponse.getData() == null) {
                JOptionPane.showMessageDialog(this, "Không thể lấy thông tin phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            PhongDTO phongDTO = phongResponse.getData();
            if (phongDTO.getTinhTrang() != 0) {
                JOptionPane.showMessageDialog(this, "Phòng đã được đặt hoặc đang sử dụng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            phongDTO.setTinhTrang(1); // Đã đặt
            System.out.println("Dữ liệu gửi đi cho SUA_PHONG: " + new Gson().toJson(phongDTO));
            Request<PhongDTO> updatePhongRequest = new Request<>("SUA_PHONG_BOOLEAN", phongDTO);
            SocketManager.send(updatePhongRequest);
            Type updatePhongResponseType = new TypeToken<Response<Boolean>>() {}.getType();
            Response<Boolean> updatePhongResponse = SocketManager.receiveType(updatePhongResponseType);

            if (!updatePhongResponse.isSuccess() || !updatePhongResponse.getData()) {
                System.out.println("Phản hồi từ SUA_PHONG: " + new Gson().toJson(updatePhongResponse));
                JOptionPane.showMessageDialog(this, "Cập nhật trạng thái phòng thất bại! Chi tiết: " + updatePhongResponse.getData(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 7. Gửi yêu cầu tạo phiếu đặt phòng
            Request<PhieuDatPhongDTO> createPhieuRequest = new Request<>("CREATE_PHIEU_DAT_PHONG", phieuDatPhong);
            SocketManager.send(createPhieuRequest);
            Type createPhieuResponseType = new TypeToken<Response<Boolean>>() {}.getType();
            Response<Boolean> createPhieuResponse = SocketManager.receiveType(createPhieuResponseType);

            if (!createPhieuResponse.isSuccess() || !createPhieuResponse.getData()) {
                JOptionPane.showMessageDialog(this, "Tạo phiếu đặt phòng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 8. Thêm hàng mới vào bảng thay vì tải lại toàn bộ dữ liệu
            JOptionPane.showMessageDialog(this, "Tạo phiếu đặt phòng thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            addRowToTable(phieuDatPhong, tenKhachHang, soPhong, "Đã đặt");

        } catch (Exception e) {
            String errorMessage = "Lỗi khi xử lý: " + e.getMessage();
            JOptionPane.showMessageDialog(this, errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    private void setupFocusLossOnClick() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Component component = SwingUtilities.getDeepestComponentAt(DatPhong_FORM.this, e.getX(), e.getY());
                if (component == DatPhong_FORM.this) {
                    Component focusedComponent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                    if (focusedComponent != null) {
                        focusedComponent.transferFocus();
                    }
                }
            }
        });
    }

    private void handleRefresh() {
        Date currentDate = new Date();
        dateNgayDat.setDate(currentDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date ngayDenDate = calendar.getTime();
        dateTimeNgayDen.setDate(ngayDenDate);
        dateTimeNgayDen.setTimeSpinners();

        calendar.setTime(ngayDenDate);
        calendar.add(Calendar.DAY_OF_MONTH, 2);
        Date ngayDiDate = calendar.getTime();
        dateTimeNgayDi.setDate(ngayDiDate);
        dateTimeNgayDi.setTimeSpinners();

        isUpdating = true;
        try {
            txtSDT.setText("");
            txtTenKhachHang.setText("");
            txtEmail.setText("");
            txtCCCD.setText("");
            cmbLoaiPhong.setSelectedIndex(0);
        } finally {
            isUpdating = false;
        }
    }
}