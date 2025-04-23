package view.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import dto.KhachHangDTO;
import dto.NhanVienDTO;
import dto.PhieuDatPhongDTO;
import dto.PhongDTO;
import model.*;
import socket.SocketManager;
import utils.custom_element.*;
import dao.KhachHang_DAO;
import dao.NhanVien_DAO;
import dao.PhieuDatPhong_DAO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class HuyDatPhong_FORM extends JPanel implements ActionListener, Openable {
    private DefaultTableModel tableModel;
    private JTable table;
    private PhieuDatPhong_DAO phieuDatPhongDAO;
    private JButton btnHuyDatPhong;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhong;
    private JTextField searchField;
    @Override
    public void open() {
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
//        dsPhieuDatPhong = phieuDatPhongDAO.getDSPhieuDatPhongDangCho();
//        loadTableDataInline();
        loadTableData();
    }
    public HuyDatPhong_FORM() {
        setBackground(new Color(16, 16, 20));
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));

        // Tim kiem
        searchField = new JTextField("Tìm kiếm");
        Border emptyBorder = BorderFactory.createEmptyBorder(13, 52, 12, 0);
        searchField.setBounds(0, 0, 280, 45);
        searchField.setBorder(emptyBorder);
        searchField.setBackground(new Color(40, 40, 44));
        searchField.setForeground(new Color(255, 255, 255, 125));
        searchField.setFont(FontManager.getManrope(Font.PLAIN, 15));
        CompoundBorder combinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), emptyBorder);
        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setBorder(combinedBorder);
                if (searchField.getText().equals("Tìm kiếm")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(emptyBorder);
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(new Color(255, 255, 255, 125));
                    searchField.setText("Tìm kiếm");
                }
            }
        });
        searchField.addActionListener(e -> handleTimKiem());
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
        searchPanel.add(searchField);
        Box bsearch = Box.createHorizontalBox();
        bsearch.add(Box.createHorizontalStrut(0));
        bsearch.add(searchPanel);
        bsearch.add(Box.createGlue());
        b.add(bsearch);
        b.add(Box.createVerticalStrut(20));

        // Tieu de
        JLabel titleLabel = new JLabel("Danh sách đặt trước");
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
        b.add(titlePanel);
        b.add(Box.createVerticalStrut(5));

        // Tạo bang
        Box b2 = Box.createHorizontalBox();
        String[] headers = {"Mã phiếu đặt phòng", "Số phòng", "Tên khách hàng", "Ngày đặt", "Ngày đến", "Ngày đi", "Nhân viên tạo phiếu"};
        tableModel = new DefaultTableModel(headers, 0);
        JScrollPane scroll;
        b2.add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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
        b.add(b2);
        b.add(Box.createVerticalStrut(300));

        // Tạo nut
        Box bbutton = Box.createHorizontalBox();
        bbutton.add(Box.createHorizontalStrut(1400));
        bbutton.add(btnHuyDatPhong = new JButton("Hủy đặt phòng"));
        btnHuyDatPhong.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnHuyDatPhong.setForeground(Color.WHITE);
        btnHuyDatPhong.setBackground(new Color(66, 99, 235));
        btnHuyDatPhong.setOpaque(false);
        btnHuyDatPhong.setPreferredSize(new Dimension(200, 50));
        btnHuyDatPhong.setMinimumSize(new Dimension(200, 50));
        btnHuyDatPhong.setMaximumSize(new Dimension(200, 50));
        btnHuyDatPhong.addActionListener(this);
        b.add(bbutton);
        add(b, BorderLayout.CENTER);
    }

    private void loadTableData() {
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            try {
                // Get all customers
                System.out.println("Sending request: GET_ALL_KHACH_HANG");
                Request<Void> khRequest = new Request<>("GET_ALL_KHACH_HANG", null);
                SocketManager.send(khRequest);
                System.out.println("Waiting for response...");
                Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
                Response<List<KhachHangDTO>> khResponse = SocketManager.receiveType(khachHangResponseType);
                if (!khResponse.isSuccess()) {
                    throw new Exception("Failed to fetch customer data: " + (khResponse.getData() != null ? khResponse.getData().toString() : "No error message"));
                }

                Map<String, String> khachHangMap = new HashMap<>();
                for (KhachHangDTO kh : khResponse.getData()) {
                    khachHangMap.put(kh.getMaKH(), kh.getHoTen());
                }

                // Get all employees
                System.out.println("Sending request: GET_ALL_NHAN_VIEN");
                Request<Void> nvRequest = new Request<>("GET_ALL_NHAN_VIEN", null);
                SocketManager.send(nvRequest);
                Type nhanVienResponseType = new TypeToken<Response<List<NhanVienDTO>>>() {}.getType();
                Response<List<NhanVienDTO>> nvResponse = SocketManager.receiveType(nhanVienResponseType);
                if (!nvResponse.isSuccess()) {
                    throw new Exception("Failed to fetch employee data: " + (nvResponse.getData() != null ? nvResponse.getData().toString() : "No error message"));
                }

                Map<String, String> nhanVienMap = new HashMap<>();
                for (NhanVienDTO nv : nvResponse.getData()) {
                    nhanVienMap.put(nv.getMaNhanVien(), nv.getHoTen());
                }

                // Get booking data for rooms with "Đã đặt" status
                System.out.println("Sending request: GET_PHIEU_DAT_PHONG_DA_DAT");
                Request<Void> request = new Request<>("GET_PHIEU_DAT_PHONG_DA_DAT", null);
                SocketManager.send(request);

                Type pdpResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>() {}.getType();
                Response<List<PhieuDatPhongDTO>> pdpResponse = SocketManager.receiveType(pdpResponseType);

                if (!pdpResponse.isSuccess()) {
                    String message = pdpResponse.getData() != null ? pdpResponse.getData().toString() : "No error message from server";
                    JOptionPane.showMessageDialog(this, "Server error: " + message, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                List<PhieuDatPhongDTO> pdpListDTO = pdpResponse.getData();
                if (pdpListDTO == null || pdpListDTO.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "No bookings found for booked rooms", "Notification", JOptionPane.INFORMATION_MESSAGE);
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
                        Object[] row = new Object[]{
                                maPDP,
                                maPhong,
                                tenKhachHang,
                                ngayDat != null ? Date.valueOf(ngayDat) : null,
                                ngayDen != null ? Date.valueOf(ngayDen) : null,
                                ngayDi != null ? Date.valueOf(ngayDi) : null,
                                tenNhanVien
                        };
                        tableModel.addRow(row);
                        System.out.println("Row: " + Arrays.toString(row));
                    }
                }

                table.repaint();
                table.revalidate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error loading customer/employee data: " + e.getMessage(), "System Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnHuyDatPhong) {
            int selectedRow = table.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt phòng để hủy!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Lấy maPDP và maPhong từ hàng được chọn
            String maPDP = (String) tableModel.getValueAt(selectedRow, 0);
            String maPhong = (String) tableModel.getValueAt(selectedRow, 1);

            // Hiển thị hộp thoại xác nhận
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn hủy đặt phòng phiếu " + maPDP + " cho phòng " + maPhong + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Tạo dữ liệu gửi đi: maPDP và maPhong
                    JsonObject requestData = new JsonObject();
                    requestData.addProperty("maPDP", maPDP);
                    requestData.addProperty("maPhong", maPhong);

                    // Gửi yêu cầu HUY_DAT_PHONG đến server
                    System.out.println("Sending request: HUY_DAT_PHONG for maPDP = " + maPDP + ", maPhong = " + maPhong);
                    Request<JsonObject> request = new Request<>("HUY_DAT_PHONG", requestData);
                    SocketManager.send(request);

                    // Nhận phản hồi từ server
                    Type responseType = new TypeToken<Response<String>>() {}.getType();
                    Response<String> response = SocketManager.receiveType(responseType);

                    // Log phản hồi từ server
                    System.out.println("RECEIVED JSON: " + response.toString());

                    if (response.isSuccess()) {
                        JOptionPane.showMessageDialog(this, "Đã hủy đặt phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                        // Tải lại toàn bộ dữ liệu từ server
                        loadTableData();
                    } else {
                        String errorMessage = response.getData() != null ? response.getData() : "Không có thông điệp lỗi từ server";
                        JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi hủy đặt phòng: " + errorMessage, "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    private void handleTimKiem() {
        String tuKhoa = searchField.getText().trim().toLowerCase();
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
}
