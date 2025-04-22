package view.form;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import model.Request;
import socket.SocketManager;
import utils.custom_element.*;
import dao.PhieuDatPhong_DAO;
import model.PhieuDatPhong;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NhanPhong_FORM extends JPanel implements ActionListener, Openable {
    private DefaultTableModel tableModel;
    private JTable table;
    private PhieuDatPhong_DAO phieuDatPhongDAO;
    private JButton btnNhanPhong;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhong;
    private JTextField searchField;
    @Override
    public void open() {
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
//        dsPhieuDatPhong = phieuDatPhongDAO.getDSPhieuDatPhongDangCho();
        loadTableData();
    }
    public NhanPhong_FORM() {
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
        bbutton.add(btnNhanPhong = new JButton("Nhận phòng"));
        btnNhanPhong.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnNhanPhong.setForeground(Color.WHITE);
        btnNhanPhong.setBackground(new Color(66, 99, 235));
        btnNhanPhong.setOpaque(false);
        btnNhanPhong.setPreferredSize(new Dimension(200, 50));
        btnNhanPhong.setMinimumSize(new Dimension(200, 50));
        btnNhanPhong.setMaximumSize(new Dimension(200, 50));
        btnNhanPhong.addActionListener(this);
        b.add(bbutton);
        add(b, BorderLayout.CENTER);
    }
    private void loadTableData(){
        SwingUtilities.invokeLater(() -> {
            tableModel.setRowCount(0);

            try {
                com.google.gson.JsonParser parser = new com.google.gson.JsonParser();

                // Lấy tất cả khách hàng
                Request<Void> khRequest = new Request<>("GET_ALL_KHACH_HANG", null);
                SocketManager.send(khRequest);
                String khJson = SocketManager.receiveRaw();
                Map<String, String> khachHangMap = new HashMap<>();

                // Phân tích JSON khách hàng
                JsonElement khElement = parser.parse(khJson);
                if (khElement.isJsonObject()) {
                    JsonObject khObj = khElement.getAsJsonObject();
                    if (khObj.get("success").getAsBoolean() && khObj.has("data")) {
                        JsonArray khArray = khObj.get("data").getAsJsonArray();
                        for (int i = 0; i < khArray.size(); i++) {
                            JsonObject kh = khArray.get(i).getAsJsonObject();
                            String maKH = kh.get("maKH").getAsString();
                            String hoTen = kh.has("hoTen") ? kh.get("hoTen").getAsString() : "";
                            khachHangMap.put(maKH, hoTen);
                        }
                    } else {
                        throw new Exception("Phản hồi từ server không hợp lệ: " + khJson);
                    }
                } else if (khElement.isJsonArray()) {
                    // Trường hợp dữ liệu trả về trực tiếp là mảng JSON
                    JsonArray khArray = khElement.getAsJsonArray();
                    for (int i = 0; i < khArray.size(); i++) {
                        JsonObject kh = khArray.get(i).getAsJsonObject();
                        String maKH = kh.get("maKH").getAsString();
                        String hoTen = kh.has("hoTen") ? kh.get("hoTen").getAsString() : "";
                        khachHangMap.put(maKH, hoTen);
                    }
                } else {
                    throw new Exception("Dữ liệu khách hàng không đúng định dạng JSON: " + khJson);
                }

                // Lấy tất cả nhân viên
                Request<Void> nvRequest = new Request<>("GET_ALL_NHAN_VIEN", null);
                SocketManager.send(nvRequest);
                String nvJson = SocketManager.receiveRaw();
                Map<String, String> nhanVienMap = new HashMap<>();

                // Phân tích JSON nhân viên
                JsonElement nvElement = parser.parse(nvJson);
                if (nvElement.isJsonObject()) {
                    JsonObject nvObj = nvElement.getAsJsonObject();
                    if (nvObj.get("success").getAsBoolean() && nvObj.has("data")) {
                        JsonArray nvArray = nvObj.get("data").getAsJsonArray();
                        for (int i = 0; i < nvArray.size(); i++) {
                            JsonObject nv = nvArray.get(i).getAsJsonObject();
                            String maNV = nv.get("maNhanVien").getAsString();
                            String hoTen = nv.has("hoTen") ? nv.get("hoTen").getAsString() : "";
                            nhanVienMap.put(maNV, hoTen);
                        }
                    } else {
                        throw new Exception("Phản hồi từ server không hợp lệ: " + nvJson);
                    }
                } else if (nvElement.isJsonArray()) {
                    // Trường hợp dữ liệu trả về trực tiếp là mảng JSON
                    JsonArray nvArray = nvElement.getAsJsonArray();
                    for (int i = 0; i < nvArray.size(); i++) {
                        JsonObject nv = nvArray.get(i).getAsJsonObject();
                        String maNV = nv.get("maNhanVien").getAsString();
                        String hoTen = nv.has("hoTen") ? nv.get("hoTen").getAsString() : "";
                        nhanVienMap.put(maNV, hoTen);
                    }
                } else {
                    throw new Exception("Dữ liệu nhân viên không đúng định dạng JSON: " + nvJson);
                }

                // Lấy tất cả phiếu đặt phòng
                Request<Void> request = new Request<>("GET_ALL_PHIEU_DAT_PHONG", null);
                int maxRetries = 3;
                int retryCount = 0;

                while (retryCount < maxRetries) {
                    try {
                        SocketManager.send(request);
                        String jsonResponse = SocketManager.receiveRaw();
                        JsonElement responseElement = parser.parse(jsonResponse);

                        JsonArray dataArray;
                        if (responseElement.isJsonObject()) {
                            JsonObject jsonObject = responseElement.getAsJsonObject();
                            boolean success = jsonObject.get("success").getAsBoolean();
                            if (!success || !jsonObject.has("data") || jsonObject.get("data").isJsonNull()) {
                                JOptionPane.showMessageDialog(this,
                                        "Không thể lấy dữ liệu phiếu đặt phòng từ server!",
                                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                                break;
                            }
                            dataArray = jsonObject.get("data").getAsJsonArray();
                        } else if (responseElement.isJsonArray()) {
                            dataArray = responseElement.getAsJsonArray();
                        } else {
                            throw new Exception("Dữ liệu phiếu đặt phòng không đúng định dạng JSON: " + jsonResponse);
                        }

                        if (dataArray.size() == 0) {
                            JOptionPane.showMessageDialog(this,
                                    "Không có dữ liệu phiếu đặt phòng!",
                                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }

                        for (int i = 0; i < dataArray.size(); i++) {
                            JsonObject pdpObj = dataArray.get(i).getAsJsonObject();

                            String maPDP = pdpObj.has("maPDP") ? pdpObj.get("maPDP").getAsString() : "";
                            String maKH = pdpObj.has("maKH") ? pdpObj.get("maKH").getAsString() : "";
                            String maNV = pdpObj.has("maNV") ? pdpObj.get("maNV").getAsString() : "";

                            String soPhong = "";
                            if (pdpObj.has("dsMaPhong") && !pdpObj.get("dsMaPhong").isJsonNull()) {
                                JsonArray dsMaPhongArray = pdpObj.get("dsMaPhong").getAsJsonArray();
                                List<String> dsMaPhong = new ArrayList<>();
                                for (int j = 0; j < dsMaPhongArray.size(); j++) {
                                    dsMaPhong.add(dsMaPhongArray.get(j).getAsString());
                                }
                                soPhong = String.join(", ", dsMaPhong);
                            }

                            LocalDate ngayDat = null;
                            if (pdpObj.has("ngayDatPhong") && !pdpObj.get("ngayDatPhong").isJsonNull()) {
                                String ngayDatStr = pdpObj.get("ngayDatPhong").getAsString();
                                ngayDat = LocalDate.parse(ngayDatStr);
                            }

                            LocalDate ngayDen = null;
                            if (pdpObj.has("ngayNhanPhongDuKien") && !pdpObj.get("ngayNhanPhongDuKien").isJsonNull()) {
                                String ngayDenStr = pdpObj.get("ngayNhanPhongDuKien").getAsString();
                                ngayDen = LocalDate.parse(ngayDenStr);
                            }

                            LocalDate ngayDi = null;
                            if (pdpObj.has("ngayTraPhongDuKien") && !pdpObj.get("ngayTraPhongDuKien").isJsonNull()) {
                                String ngayDiStr = pdpObj.get("ngayTraPhongDuKien").getAsString();
                                ngayDi = LocalDate.parse(ngayDiStr);
                            }

                            String tenKhachHang = khachHangMap.getOrDefault(maKH, "");
                            String tenNhanVien = nhanVienMap.getOrDefault(maNV, "");

                            tableModel.addRow(new Object[]{
                                    maPDP,
                                    soPhong,
                                    tenKhachHang,
                                    ngayDat != null ? java.sql.Date.valueOf(ngayDat) : null,
                                    ngayDen != null ? java.sql.Date.valueOf(ngayDen) : null,
                                    ngayDi != null ? java.sql.Date.valueOf(ngayDi) : null,
                                    tenNhanVien
                            });
                        }

                        System.out.println("Đã tải " + dataArray.size() + " phiếu đặt phòng vào bảng");
                        break;
                    } catch (IOException ex) {
                        retryCount++;
                        System.err.println("Lỗi kết nối server (lần thử " + retryCount + "): " + ex.getMessage());
                        if (retryCount >= maxRetries) {
                            JOptionPane.showMessageDialog(this,
                                    "Lỗi kết nối đến server sau " + maxRetries + " lần thử: " + ex.getMessage(),
                                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                        int option = JOptionPane.showConfirmDialog(this,
                                "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                                "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                        if (option != JOptionPane.YES_OPTION) {
                            break;
                        }
                    } catch (Exception ex) {
                        retryCount++;
                        System.err.println("Lỗi xử lý dữ liệu (lần thử " + retryCount + "): " + ex.getMessage());
                        ex.printStackTrace();
                        if (retryCount >= maxRetries) {
                            JOptionPane.showMessageDialog(this,
                                    "Lỗi xử lý dữ liệu sau " + maxRetries + " lần thử: " + ex.getMessage(),
                                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
                            break;
                        }
                    }
                }

                table.repaint();
                table.revalidate();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi tải dữ liệu khách hàng/nhân viên: " + e.getMessage(),
                        "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == btnNhanPhong) {
//            int selectedRow = table.getSelectedRow();
//
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt phòng để nhận phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//
//            String maPDP = (String) tableModel.getValueAt(selectedRow, 0);
//            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn nhận phòng phiếu "+maPDP+" ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                if (phieuDatPhongDAO.nhanPhong(maPDP)) {
//                    JOptionPane.showMessageDialog(this, "Đã nhận phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    tableModel.removeRow(selectedRow);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi nhận phòng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//
//        }
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
