package view.form;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dto.*;
import model.NhanVien;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;
import utils.custom_element.CustomTableCellRenderer;
import dao.DichVu_DAO;
import utils.session.SessionManager;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;


class QuantityCellEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel;
    private JLabel quantityLabel;
    private JButton minusButton;
    private JButton plusButton;
    private int currentValue;
    private JTable table2; // Bảng dịch vụ đã chọn
    private JTable table1; // Bảng danh sách dịch vụ

    public QuantityCellEditor(JTable table2, JTable table1) {
        this.table2 = table2;
        this.table1 = table1;

        panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        panel.setBackground(new Color(50, 50, 54));

        minusButton = new JButton(new ImageIcon("booking-hotel-client/src/main/resources/imgs/MinusIcon.png"));
        minusButton.addActionListener(e -> changeQuantity(-1));

        quantityLabel = new JLabel("1", SwingConstants.CENTER);
        quantityLabel.setFont(FontManager.getManrope(Font.PLAIN, 16));
        quantityLabel.setForeground(Color.WHITE);

        plusButton = new JButton(new ImageIcon("booking-hotel-client/src/main/resources/imgs/PlusIcon.png"));
        plusButton.addActionListener(e -> changeQuantity(1));

        panel.add(minusButton);
        panel.add(quantityLabel);
        panel.add(plusButton);
    }

    private void changeQuantity(int delta) {
        currentValue = Math.max(1, currentValue + delta); // Đảm bảo số lượng >= 1
        quantityLabel.setText(String.valueOf(currentValue));

        // Lấy tên dịch vụ từ dòng hiện tại trong table2
        int row = table2.getEditingRow();
        String tenDichVu = (String) table2.getValueAt(row, 0); // Cột 0: Tên dịch vụ

        // Lấy giá dịch vụ từ table1
        double giaDichVu = getGiaDichVu(tenDichVu);

        // Tính thành tiền mới
        double thanhTien = currentValue * giaDichVu;
        DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
        String thanhTienFormatted = decimalFormat.format(thanhTien) + " VNĐ";

        // Cập nhật số lượng và thành tiền vào table2
        DefaultTableModel model = (DefaultTableModel) table2.getModel();
        model.setValueAt(currentValue, row, 1); // Cột 1: Số lượng
        model.setValueAt(thanhTienFormatted, row, 2); // Cột 2: Thành tiền

        fireEditingStopped(); // Kết thúc chỉnh sửa
    }

    // Hàm lấy giá dịch vụ từ table1 dựa trên tên dịch vụ
    private double getGiaDichVu(String tenDichVu) {
        for (int i = 0; i < table1.getRowCount(); i++) {
            if (table1.getValueAt(i, 1).equals(tenDichVu)) { // Cột 1: Tên dịch vụ
                String giaStr = table1.getValueAt(i, 2).toString() // Cột 2: Giá dịch vụ
                        .replace(" VNĐ", "")
                        .replace(",", "");
                return Double.parseDouble(giaStr);
            }
        }
        return 0.0; // Trả về 0 nếu không tìm thấy
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentValue = (value != null) ? Integer.parseInt(value.toString()) : 1;
        quantityLabel.setText(String.valueOf(currentValue));
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return currentValue;
    }
}

class QuantityCellRenderer extends JPanel implements TableCellRenderer {
    private JLabel quantityLabel;

    public QuantityCellRenderer() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        setBackground(new Color(50, 50, 54));
        quantityLabel = new JLabel("", SwingConstants.CENTER);
        quantityLabel.setFont(FontManager.getManrope(Font.PLAIN, 16));
        quantityLabel.setForeground(Color.WHITE);
        add(quantityLabel);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        quantityLabel.setText(value != null ? value.toString() : "1");
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            quantityLabel.setForeground(table.getSelectionForeground());
        } else {
            setBackground(new Color(50, 50, 54));
            quantityLabel.setForeground(Color.WHITE);
        }
        return this;
    }
}

public class DatDichVu_FORM extends JPanel implements ActionListener, MouseListener {
    private JButton btnDatDV;
    private JTable table;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableModel1;
    private JTable table1;
    private Font f1;
    private Component buttonPanel;
    private JButton btnThem;
    private DefaultTableModel tableModel2;
    private JTable table2;
    private DefaultTableModel tableModel3; // Thêm tableModel3 cho table3
    private JButton btnLamMoi;
    private JButton btnXacNhan;
    private DichVu_DAO dichVuDao;
    private String selectedMaHD;
    private String selectedMaPhong;
    private String selectedTenKhach;
    private JButton btnXoa;
    Color backgroundColor = new Color(31, 31, 32);
    Color panelColor = new Color(40, 40, 44);
    Color primaryColor = new Color(27, 112, 213);
    Color textColor = Color.WHITE;
    private final Font normalFont = FontManager.getManrope(Font.PLAIN, 16);

    public DatDichVu_FORM() {
        dichVuDao = new DichVu_DAO();
        setBackground(new Color(16, 16, 20));
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));

        // Tìm kiếm
        JTextField searchField = new JTextField("Tìm kiếm mã phòng");
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
                if (searchField.getText().equals("Tìm kiếm mã phòng")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(emptyBorder);
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(new Color(255, 255, 255, 125));
                    searchField.setText("Tìm kiếm mã phòng");
                }
            }
        });
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String keyword = searchField.getText().trim();
                    if (!keyword.equals("") && !keyword.equals("Tìm kiếm mã phòng")) {
                        timKiem(keyword);
                    }
                }
            }
        });
        JLabel searchIcon = new JLabel(new ImageIcon("booking-hotel-client/src/main/resources/imgs/TimKiemIcon.png"));
        searchIcon.setBounds(12, 12, 24, 24);

        // Tiêu đề
        b.add(Box.createVerticalStrut(20));
        JLabel titleLabel = new JLabel("Danh sách phòng đã đặt");
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

        // Tạo bảng
        Box b2 = Box.createHorizontalBox();
        String[] colName = {"Mã đặt phòng", "Loại phòng", "Tên phòng", "Phòng", "Trạng thái", "Tên khách", "Ngày đến", "Ngày đi"};
        tableModel = new DefaultTableModel(colName, 0);
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
        b.add(Box.createVerticalStrut(5));
        b.add(Box.createVerticalStrut(20));

        JLabel titleLabel3 = new JLabel("Danh sách phòng đã đặt dịch vụ");
        titleLabel3.setFont(FontManager.getManrope(Font.BOLD, 16));
        titleLabel3.setForeground(Color.white);

        RoundedPanel titlePanel3 = new RoundedPanel(10, 0, new Color(27, 112, 213));
        titlePanel3.setPreferredSize(new Dimension(1642, 50));
        titlePanel3.setMinimumSize(new Dimension(1642, 50));
        titlePanel3.setMaximumSize(new Dimension(1642, 50));
        titlePanel3.setOpaque(false);
        titlePanel3.setLayout(new BoxLayout(titlePanel3, BoxLayout.X_AXIS));

        titlePanel3.add(Box.createHorizontalStrut(15));
        titlePanel3.add(titleLabel3);
        titlePanel3.add(Box.createHorizontalGlue());
        b.add(titlePanel3);
        b.add(Box.createVerticalStrut(5));

        Box b3 = Box.createHorizontalBox();
        String[] colName3 = {"Mã đặt phòng", "Phòng", "Tên khách", "Dịch vụ đã đặt"};
        tableModel3 = new DefaultTableModel(colName3, 0);
        JTable table3 = new JTable(tableModel3);
        JScrollPane scroll3 = new JScrollPane(table3, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll3.setPreferredSize(new Dimension(1642, 250)); // Thay setBounds bằng setPreferredSize
        scroll3.setBorder(null);
        scroll3.getViewport().setOpaque(false);
        scroll3.setViewportBorder(null);
        b3.add(scroll3);

        table3.setBackground(new Color(24, 24, 28));
        table3.setForeground(Color.WHITE);
        table3.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table3.setRowHeight(55);

        JTableHeader header3 = table3.getTableHeader();
        header3.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header3.setPreferredSize(new Dimension(header3.getPreferredSize().width, 55));
        header3.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer3 = new CustomCellRenderer();
        for (int i = 0; i < table3.getColumnCount(); i++) {
            TableColumn column = table3.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer3);
        }
        loadDichVuDaDatData(); // Thay loadDichVuDaDat() bằng loadDichVuDaDatData()

        b.add(b3);
        b.add(Box.createVerticalStrut(50));

        // Tạo nút
        Box bbutton = Box.createHorizontalBox();
        bbutton.add(Box.createHorizontalStrut(1400));
        bbutton.add(btnDatDV = new JButton("Đặt dịch vụ"));
        btnDatDV.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnDatDV.setForeground(Color.WHITE);
        btnDatDV.setBackground(new Color(66, 99, 235));
        btnDatDV.setOpaque(false);
        btnDatDV.setPreferredSize(new Dimension(200, 50));
        btnDatDV.setMinimumSize(new Dimension(200, 50));
        btnDatDV.setMaximumSize(new Dimension(200, 50));
        btnDatDV.addActionListener(this);
        b.add(bbutton);
        add(b, BorderLayout.CENTER);

        btnDatDV.addActionListener(e -> {
            if (table.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null,
                        "Không có phòng nào để chọn!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            } else if (table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(null,
                        "Vui lòng chọn một phòng trước khi đặt dịch vụ!",
                        "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                int selectedRow = table.getSelectedRow();
                selectedMaHD = table.getValueAt(selectedRow, 0).toString();
                selectedMaPhong = table.getValueAt(selectedRow, 3).toString();
                selectedTenKhach = table.getValueAt(selectedRow, 5).toString();
                phieuDatDichVu();
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedMaHD = table.getValueAt(selectedRow, 0).toString();
                    selectedMaPhong = table.getValueAt(selectedRow, 3).toString();
                    selectedTenKhach = table.getValueAt(selectedRow, 5).toString();
                    System.out.println("Selected MaHD: " + selectedMaHD + ", MaPhong: " + selectedMaPhong + ", TenKhach: " + selectedTenKhach);
                }
            }
        });
        loadPhongData();
    }

    private void loadDichVuDaDatData() {
        tableModel3.setRowCount(0);

        try {
            // Sử dụng API get by maPDP thay vì get all
            Request<String> request = new Request<>("GET_PHIEU_DAT_DICH_VU_BY_MA_PDP", selectedMaHD);
            SocketManager.send(request);

            Type responseType = new TypeToken<Response<List<PhieuDatDichVuDTO>>>(){}.getType();
            Response<List<PhieuDatDichVuDTO>> response = SocketManager.receiveType(responseType);

            if (response == null || !response.isSuccess() || response.getData() == null) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu dịch vụ đã đặt", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            List<PhieuDatDichVuDTO> dsPhieuDatDichVu = response.getData();
            if (dsPhieuDatDichVu.isEmpty()) {
                System.out.println("Không có phiếu đặt dịch vụ nào.");
                return;
            }

            // Bước 2: Lấy danh sách phiếu đặt phòng
            Map<String, PhieuDatPhongDTO> phieuDatPhongMap = new HashMap<>();
            Request<Void> phieuDatPhongRequest = new Request<>("GET_ALL_PHIEU_DAT_PHONG", null);
            SocketManager.send(phieuDatPhongRequest);
            Type phieuDatPhongResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>(){}.getType();
            Response<List<PhieuDatPhongDTO>> phieuDatPhongResponse = SocketManager.receiveType(phieuDatPhongResponseType);

            if (phieuDatPhongResponse == null || !phieuDatPhongResponse.isSuccess() || phieuDatPhongResponse.getData() == null) {
                String errorMsg = phieuDatPhongResponse == null ? "Không nhận được phản hồi từ server"
                        : (phieuDatPhongResponse.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách phiếu đặt phòng: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<PhieuDatPhongDTO> dsPhieuDatPhong = phieuDatPhongResponse.getData();
            for (PhieuDatPhongDTO pdp : dsPhieuDatPhong) {
                phieuDatPhongMap.put(pdp.getMaPDP(), pdp);
            }

            // Bước 3: Lấy danh sách khách hàng
            Map<String, KhachHangDTO> khachHangMap = new HashMap<>();
            Request<Void> khachHangRequest = new Request<>("GET_ALL_KHACH_HANG", null);
            SocketManager.send(khachHangRequest);
            Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>(){}.getType();
            Response<List<KhachHangDTO>> khachHangResponse = SocketManager.receiveType(khachHangResponseType);

            if (khachHangResponse == null || !khachHangResponse.isSuccess() || khachHangResponse.getData() == null) {
                String errorMsg = khachHangResponse == null ? "Không nhận được phản hồi từ server"
                        : (khachHangResponse.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách khách hàng: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<KhachHangDTO> dsKhachHang = khachHangResponse.getData();
            for (KhachHangDTO kh : dsKhachHang) {
                khachHangMap.put(kh.getMaKH(), kh);
            }

            // Bước 4: Lấy danh sách dịch vụ
            Map<String, DichVuDTO> dichVuMap = new HashMap<>();
            Request<Void> dichVuRequest = new Request<>("GET_ALL_DICH_VU", null);
            SocketManager.send(dichVuRequest);
            Type dichVuResponseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> dichVuResponse = SocketManager.receiveType(dichVuResponseType);

            if (dichVuResponse == null || !dichVuResponse.isSuccess() || dichVuResponse.getData() == null) {
                String errorMsg = dichVuResponse == null ? "Không nhận được phản hồi từ server"
                        : (dichVuResponse.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách dịch vụ: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<DichVuDTO> dsDichVu = dichVuResponse.getData();
            for (DichVuDTO dv : dsDichVu) {
                dichVuMap.put(dv.getMaDV(), dv);
            }

            // Bước 5: Điền dữ liệu vào table3
            for (PhieuDatDichVuDTO pddv : dsPhieuDatDichVu) {
                if (!pddv.getMaPDP().equals(selectedMaHD)) continue; // Chỉ load theo maPDP đã chọn

                // Lấy thông tin khách hàng
                KhachHangDTO khachHang = khachHangMap.get(pddv.getMaKH());
                String tenKhach = khachHang != null ? khachHang.getHoTen() : "Không xác định";

                // Lấy danh sách dịch vụ đã đặt
                List<String> dsMaDV = pddv.getDsMaDV();
                StringBuilder dichVuDaDat = new StringBuilder();
                if (dsMaDV != null && !dsMaDV.isEmpty()) {
                    for (String maDV : dsMaDV) {
                        DichVuDTO dichVu = dichVuMap.get(maDV);
                        if (dichVu != null) {
                            if (dichVuDaDat.length() > 0) {
                                dichVuDaDat.append(", ");
                            }
                            dichVuDaDat.append(dichVu.getTenDV());
                        }
                    }
                } else {
                    dichVuDaDat.append("Không có dịch vụ");
                }

                // Lấy thông tin phiếu đặt phòng
                PhieuDatPhongDTO pdp = phieuDatPhongMap.get(pddv.getMaPDP());
                String maPhong = "N/A";
                if (pdp != null && pdp.getDsMaPhong() != null && !pdp.getDsMaPhong().isEmpty()) {
                    maPhong = pdp.getDsMaPhong().get(0);
                }

                tableModel3.addRow(new Object[]{
                        pddv.getMaPDP(), // Mã đặt phòng
                        maPhong,
                        tenKhach,
                        dichVuDaDat.toString()
                });

                break; // Nếu bạn chỉ muốn hiển thị 1 dòng duy nhất
            }


        } catch (IOException ex) {
            ex.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                loadDichVuDaDatData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void phieuDatDichVu() {
        JDialog dialog = new JDialog();
        dialog.setSize(1564, 880);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        Color backgroundColor = new Color(31, 31, 32);
        Color panelColor = new Color(40, 40, 44);
        Color primaryColor = new Color(27, 112, 213);
        Color textColor = Color.WHITE;

        Font titleFont = FontManager.getManrope(Font.BOLD, 24);
        Font headerFont = FontManager.getManrope(Font.BOLD, 16);
        Font normalFont = FontManager.getManrope(Font.PLAIN, 16);
        Font italicFont = FontManager.getManrope(Font.ITALIC, 16);
        Font buttonFont = FontManager.getManrope(Font.BOLD, 16);

        dialog.getContentPane().setBackground(backgroundColor);

        // ========== HEADER SECTION ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(panelColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("PHIẾU ĐẶT DỊCH VỤ");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);

        JButton btnClose = createStyledButton("X", new Color(255, 69, 58), 45, 45);
        btnClose.addActionListener(e -> dialog.dispose());

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnClose, BorderLayout.EAST);
        dialog.add(headerPanel, BorderLayout.NORTH);

        // ========== MAIN CONTENT SECTION ==========
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);

        // ===== LEFT PANEL - DANH SÁCH DỊCH VỤ =====
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(panelColor);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Search panel
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(panelColor);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JTextField searchField = new JTextField();
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 40, 10, 10);
        Border focusBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                emptyBorder
        );
        searchField.setBorder(emptyBorder);
        searchField.setBackground(panelColor);
        searchField.setForeground(new Color(255, 255, 255, 125));
        searchField.setFont(italicFont);
        searchField.setText("Tìm kiếm dịch vụ");
        searchField.setToolTipText("Nhập tên dịch vụ để tìm kiếm...");

        searchField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField.setBorder(focusBorder);
                if (searchField.getText().equals("Tìm kiếm dịch vụ")) {
                    searchField.setText("");
                    searchField.setForeground(textColor);
                    searchField.setFont(normalFont);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField.setBorder(emptyBorder);
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Tìm kiếm dịch vụ");
                    searchField.setForeground(new Color(255, 255, 255, 125));
                    searchField.setFont(italicFont);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateField();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateField();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateField();
            }

            private void updateField() {
                if (searchField.getText().isEmpty() && !searchField.hasFocus()) {
                    searchField.setText("Tìm kiếm dịch vụ");
                    searchField.setForeground(new Color(255, 255, 255, 125));
                    searchField.setFont(italicFont);
                }
            }
        });

        JLabel searchIcon = new JLabel(new ImageIcon("booking-hotel-client/src/main/resources/imgs/TimKiemIcon.png"));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 5));
        infoPanel.setBackground(panelColor);
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblMaDatPhong = new JLabel("Mã đặt phòng:");
        lblMaDatPhong.setFont(normalFont);
        lblMaDatPhong.setForeground(textColor);
        JLabel lblMaDatPhongValue = new JLabel(selectedMaHD != null ? selectedMaHD : "N/A");
        lblMaDatPhongValue.setFont(normalFont);
        lblMaDatPhongValue.setForeground(textColor);

        JLabel lblTenKhach = new JLabel("Tên khách:");
        lblTenKhach.setFont(normalFont);
        lblTenKhach.setForeground(textColor);
        JLabel lblTenKhachValue = new JLabel(selectedTenKhach != null ? selectedTenKhach : "N/A");
        lblTenKhachValue.setFont(normalFont);
        lblTenKhachValue.setForeground(textColor);

        JLabel lblTenPhong = new JLabel("Tên phòng:");
        lblTenPhong.setFont(normalFont);
        lblTenPhong.setForeground(textColor);
        JLabel lblTenPhongValue = new JLabel(selectedMaPhong != null ? selectedMaPhong : "N/A");
        lblTenPhongValue.setFont(normalFont);
        lblTenPhongValue.setForeground(textColor);

        infoPanel.add(lblMaDatPhong);
        infoPanel.add(lblMaDatPhongValue);
        infoPanel.add(lblTenKhach);
        infoPanel.add(lblTenKhachValue);
        infoPanel.add(lblTenPhong);
        infoPanel.add(lblTenPhongValue);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelColor);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        leftPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel
        String[] colNames = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Đơn vị tính", "Mô tả"};
        tableModel1 = new DefaultTableModel(colNames, 0);
        table1 = new JTable(tableModel1);

        table1.setRowSelectionAllowed(true);
        table1.setCellSelectionEnabled(false);
        table1.setShowVerticalLines(false);
        table1.setRowHeight(55);
        table1.setBackground(panelColor);
        table1.setForeground(textColor);
        table1.setFont(normalFont);
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                System.out.println("Selected row in table1: " + selectedRow);
                table1.repaint();
            }
        });
        table1.setDefaultRenderer(Object.class, new CustomTableCellRenderer());
        JScrollPane scrollPane1 = new JScrollPane(table1);
        scrollPane1.setBorder(null);
        scrollPane1.getViewport().setBackground(panelColor);
        leftPanel.add(scrollPane1, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(panelColor);

        btnThem = createStyledButton("THÊM DỊCH VỤ", primaryColor, 180, 45);
        btnThem.setFont(buttonFont);
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(74, 74, 66, 100));
        btnThem.setOpaque(false);
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.setMinimumSize(new Dimension(150, 40));
        btnThem.setMaximumSize(new Dimension(150, 40));
        btnThem.addActionListener(this);

        btnLamMoi = createStyledButton("LÀM MỚI", new Color(100, 100, 100), 180, 45);
        btnLamMoi.setFont(buttonFont);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(74, 74, 66, 100));
        btnLamMoi.setOpaque(false);
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setMinimumSize(new Dimension(150, 40));
        btnLamMoi.setMaximumSize(new Dimension(150, 40));
        btnLamMoi.addActionListener(this);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnLamMoi);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== RIGHT PANEL - DỊCH VỤ ĐÃ CHỌN =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(panelColor);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        String[] colNames2 = {"Tên dịch vụ", "Số lượng", "Thành tiền"};
        tableModel2 = new DefaultTableModel(colNames2, 0);
        table2 = new JTable(tableModel2);
        customizeTable(table2);

        TableColumn quantityColumn = table2.getColumnModel().getColumn(1);
        // Đặt Renderer và Editor cho cột số lượng (cột 1)
        table2.getColumnModel().getColumn(1).setCellRenderer(new QuantityCellRenderer());
        table2.getColumnModel().getColumn(1).setCellEditor(new QuantityCellEditor(table2, table1));

        JScrollPane scrollPane2 = new JScrollPane(table2);
        scrollPane2.setBorder(null);
        scrollPane2.getViewport().setBackground(panelColor);
        rightPanel.add(scrollPane2, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setBackground(panelColor);

        btnXoa = createStyledButton("XÓA", new Color(200, 60, 60), 180, 45);
        btnXoa.setFont(buttonFont);
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(151, 69, 35, 100));
        btnXoa.setOpaque(false);
        btnXoa.setPreferredSize(new Dimension(150, 40));
        btnXoa.setMinimumSize(new Dimension(150, 40));
        btnXoa.setMaximumSize(new Dimension(150, 40));
        btnXoa.addActionListener(this);

        btnXacNhan = createStyledButton("XÁC NHẬN", new Color(60, 180, 60), 180, 45);
        btnXacNhan.setFont(buttonFont);
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(new Color(51, 70, 50, 100));
        btnXacNhan.setOpaque(false);
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        btnXacNhan.setMinimumSize(new Dimension(150, 40));
        btnXacNhan.setMaximumSize(new Dimension(150, 40));
        btnXacNhan.addActionListener(this);

        actionPanel.add(btnXoa);
        actionPanel.add(btnXacNhan);
        rightPanel.add(actionPanel, BorderLayout.SOUTH);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        dialog.add(mainPanel, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setUndecorated(true);

        dialog.setBackground(new Color(0, 0, 0, 0));
        ((JComponent) dialog.getContentPane()).setBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1)
        );

        dialog.setVisible(true);

        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(dialog,
                    "Vui lòng chọn một phòng trước khi đặt dịch vụ!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            dialog.dispose();
            return;
        }

        loadDichVuData();
    }

    private JButton createStyledButton(String text, Color bgColor, int width, int height) {
        JButton button = new JButton(text);
        button.setFont(FontManager.getManrope(Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setPreferredSize(new Dimension(width, height));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void customizeTable(JTable table) {
        table.setBackground(new Color(50, 50, 54));
        table.setForeground(Color.WHITE);
        table.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table.setRowHeight(40);
        table.setSelectionBackground(new Color(27, 112, 213));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(80, 80, 80));
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());
        table.setIntercellSpacing(new Dimension(5, 5));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(60, 60, 64));
        header.setForeground(Color.WHITE);
        header.setFont(FontManager.getManrope(Font.BOLD, 16));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnDatDV) {
            phieuDatDichVu();
        } else if (source == btnThem) {
            themDichVu();
        } else if (source == btnXacNhan) {
            xacNhanDichVu();
        } else if (source == btnLamMoi) {
            lamMoiDichVu();
        } else if (source == btnXoa) {
            xoaDichVu();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == table) {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedMaHD = table.getValueAt(row, 0).toString();
                selectedMaPhong = table.getValueAt(row, 3).toString();
                System.out.println("Selected MaHD: " + selectedMaHD + ", MaPhong: " + selectedMaPhong);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void timKiem(String keyword) {
        table.clearSelection();
        int foundRow = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean match = false;
            for (int j = 0; j < tableModel.getColumnCount(); j++) {
                Object cellValue = tableModel.getValueAt(i, j);
                if (cellValue != null && cellValue.toString().toLowerCase().contains(keyword.toLowerCase())) {
                    match = true;
                    break;
                }
            }
            if (match) {
                foundRow = i;
                break;
            }
        }
        if (foundRow != -1) {
            moveRowToTop(foundRow);
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
        tableModel.removeRow(rowIndex);
        tableModel.insertRow(0, rowData);
    }

    private void timKiem1(String keyword) {
        table1.clearSelection();

        Request<String> request = new Request<>("TIM_DICH_VU_THEO_TEN", keyword);
        try {
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                List<DichVuDTO> dsDichVu = response.getData();
                if (dsDichVu == null || dsDichVu.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                tableModel1.setRowCount(0);
                DecimalFormat decimalFormat = new DecimalFormat("#,###.PV");
                for (DichVuDTO dv : dsDichVu) {
                    tableModel1.addRow(new Object[]{
                            dv.getMaDV(),
                            dv.getTenDV(),
                            decimalFormat.format(dv.getDonGia()) + " VNĐ",
                            dv.getDonViTinh(),
                            dv.getMoTa()
                    });
                }

                if (tableModel1.getRowCount() > 0) {
                    table1.addRowSelectionInterval(0, 0);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không thể tìm kiếm dịch vụ từ server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                timKiem1(keyword);
            }
        }
    }

    private void loadDichVuData() {
        tableModel1.setRowCount(0);
        Request<Void> request = new Request<>("GET_ALL_DICH_VU", null);

        try {
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> response = SocketManager.receiveType(responseType);

            if (response == null) {
                JOptionPane.showMessageDialog(this,
                        "Không nhận được phản hồi từ server!",
                        "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!response.isSuccess()) {
                String errorMsg = response.getData() != null ?
                        response.getData().toString() : "Lỗi không xác định";
                JOptionPane.showMessageDialog(this,
                        "Lỗi từ server: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<DichVuDTO> dsDichVu = response.getData();
            if (dsDichVu == null || dsDichVu.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không có dữ liệu dịch vụ!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            for (DichVuDTO dv : dsDichVu) {
                tableModel1.addRow(new Object[]{
                        dv.getMaDV(),
                        dv.getTenDV(),
                        decimalFormat.format(dv.getDonGia()) + " VNĐ",
                        dv.getDonViTinh(),
                        dv.getMoTa()
                });
            }

            customizeTableAppearance();
            System.out.println("Đã tải " + dsDichVu.size() + " dịch vụ vào bảng");

        } catch (IOException ex) {
            handleConnectionError(ex);
        } catch (JsonSyntaxException ex) {
            JOptionPane.showMessageDialog(this,
                    "Dữ liệu nhận được không hợp lệ: " + ex.getMessage(),
                    "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loadDichVuDaDat(String maPDP) {
        tableModel3.setRowCount(0); // Xóa dữ liệu cũ trong table3

        try {
            // Gửi yêu cầu đến server
            Request<String> request = new Request<>("GET_PHIEU_DAT_DICH_VU_BY_MA_PDP", maPDP);
            SocketManager.send(request);

            // Nhận phản hồi
            Type responseType = new TypeToken<Response<List<PhieuDatDichVuDTO>>>(){}.getType();
            Response<List<PhieuDatDichVuDTO>> response = SocketManager.receiveType(responseType);

            if (response == null || !response.isSuccess() || response.getData() == null) {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi lấy danh sách dịch vụ đã đặt ",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<PhieuDatDichVuDTO> dsPhieuDatDichVu = response.getData();
            if (dsPhieuDatDichVu.isEmpty()) {
                return;
            }

            // Lấy danh sách dịch vụ để ánh xạ tên
            Map<String, String> dichVuMap = new HashMap<>();
            Request<Void> dichVuRequest = new Request<>("GET_ALL_DICH_VU", null);
            SocketManager.send(dichVuRequest);
            Type dichVuResponseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> dichVuResponse = SocketManager.receiveType(dichVuResponseType);

            if (dichVuResponse != null && dichVuResponse.isSuccess() && dichVuResponse.getData() != null) {
                dichVuResponse.getData().forEach(dv -> dichVuMap.put(dv.getMaDV(), dv.getTenDV()));
            }

            // Hiển thị dữ liệu lên table3
            for (PhieuDatDichVuDTO pddv : dsPhieuDatDichVu) {
                StringBuilder dichVuDaDat = new StringBuilder();
                List<String> dsMaDV = pddv.getDsMaDV();

                if (dsMaDV != null && !dsMaDV.isEmpty()) {
                    for (String maDV : dsMaDV) {
                        String tenDV = dichVuMap.getOrDefault(maDV, "Dịch vụ không xác định");
                        if (dichVuDaDat.length() > 0) {
                            dichVuDaDat.append(", ");
                        }
                        dichVuDaDat.append(tenDV);
                    }
                } else {
                    dichVuDaDat.append("Không có dịch vụ");
                }

                tableModel3.addRow(new Object[]{
                        pddv.getMaPDP(),
                        selectedMaPhong, // Lấy từ biến đã chọn
                        selectedTenKhach, // Lấy từ biến đã chọn
                        dichVuDaDat.toString()
                });
            }

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void customizeTableAppearance() {
        table1.setBackground(new Color(24, 24, 28));
        table1.setForeground(Color.WHITE);
        table1.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table1.setRowHeight(55);
        table1.getTableHeader().setFont(FontManager.getManrope(Font.BOLD, 16));
        table1.getTableHeader().setBackground(new Color(40, 40, 45));
        table1.getTableHeader().setForeground(Color.WHITE);
        table1.setSelectionBackground(new Color(60, 60, 70));
    }

    private void handleConnectionError(IOException ex) {
        ex.printStackTrace();
        int option = JOptionPane.showConfirmDialog(this,
                "Lỗi kết nối đến server: " + ex.getMessage() +
                        "\nBạn có muốn thử lại không?",
                "Lỗi hệ thống",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            loadDichVuData();
        }
    }

    private void loadPhongData() {
        tableModel.setRowCount(0);

        try {
            // Gửi yêu cầu lấy danh sách phiếu đặt phòng
            Request<Void> request = new Request<>("GET_ALL_PHIEU_DAT_PHONG", null);
            SocketManager.send(request);

            // Nhận JSON gốc từ server
            String rawJson = SocketManager.receiveRaw();
            System.out.println("Raw JSON response: " + rawJson);

            // Phân tích JSON để kiểm tra trường "data"
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            JsonObject jsonObject = gson.fromJson(rawJson, JsonObject.class);

            // Kiểm tra trường "success"
            if (!jsonObject.has("success") || !jsonObject.get("success").getAsBoolean()) {
                String errorMsg = jsonObject.has("data") ? jsonObject.get("data").getAsString() : "Yêu cầu không thành công";
                System.out.println("GET_ALL_PHIEU_DAT_PHONG failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra trường "data" có phải là mảng không
            if (!jsonObject.has("data") || !jsonObject.get("data").isJsonArray()) {
                String errorMsg = jsonObject.has("data") ? jsonObject.get("data").getAsString() : "Dữ liệu không hợp lệ";
                System.out.println("GET_ALL_PHIEU_DAT_PHONG failed: Expected array but got: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Nếu "data" là mảng, phân tích thành List<PhieuDatPhongDTO>
            Type phieuResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>(){}.getType();
            Response<List<PhieuDatPhongDTO>> response = gson.fromJson(rawJson, phieuResponseType);

            List<PhieuDatPhongDTO> dsPhieuDatPhong = response.getData();
            if (dsPhieuDatPhong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu phiếu đặt phòng",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Lấy danh sách phòng
            Map<String, PhongDTO> phongMap = new HashMap<>();
            Request<Void> phongRequest = new Request<>("GET_ALL_PHONG", null);
            SocketManager.send(phongRequest);
            Type phongResponseType = new TypeToken<Response<List<PhongDTO>>>(){}.getType();
            Response<List<PhongDTO>> phongResponse = SocketManager.receiveType(phongResponseType);

            if (phongResponse != null && phongResponse.isSuccess() && phongResponse.getData() != null) {
                phongResponse.getData().forEach(phong -> {
                    System.out.println("Phòng: " + phong.getMaPhong() + " - " + phong.getTenPhong());
                    phongMap.put(phong.getMaPhong(), phong);
                });
            } else {
                String errorMsg = phongResponse == null ? "Không nhận được phản hồi từ server"
                        : !phongResponse.isSuccess() ? "Yêu cầu không thành công"
                        : "Dữ liệu phòng trống";
                System.out.println("GET_ALL_PHONG failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách phòng: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // Lấy danh sách khách hàng
            Map<String, KhachHangDTO> khachHangMap = new HashMap<>();
            Request<Void> khachHangRequest = new Request<>("GET_ALL_KHACH_HANG", null);
            SocketManager.send(khachHangRequest);
            Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>(){}.getType();
            Response<List<KhachHangDTO>> khachHangResponse = SocketManager.receiveType(khachHangResponseType);

            if (khachHangResponse != null && khachHangResponse.isSuccess() && khachHangResponse.getData() != null) {
                khachHangResponse.getData().forEach(kh -> khachHangMap.put(kh.getMaKH(), kh));
            } else {
                String errorMsg = khachHangResponse == null ? "Không nhận được phản hồi từ server"
                        : !khachHangResponse.isSuccess() ? "Yêu cầu không thành công"
                        : "Dữ liệu khách hàng trống";
                System.out.println("GET_ALL_KHACH_HANG failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách khách hàng: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // Xử lý dữ liệu và thêm vào bảng
            StringBuilder errorMessages = new StringBuilder();
            int errorCount = 0;

            for (PhieuDatPhongDTO pdp : dsPhieuDatPhong) {
                String tenKhachHang = "Không xác định";
                if (pdp.getMaKH() != null) {
                    KhachHangDTO kh = khachHangMap.get(pdp.getMaKH());
                    tenKhachHang = kh != null ? kh.getHoTen() : "Khách hàng không tồn tại";
                }

                List<String> dsMaPhong = pdp.getDsMaPhong();
                if (dsMaPhong == null || dsMaPhong.isEmpty()) {
                    tableModel.addRow(new Object[]{
                            pdp.getMaPDP(),
                            "N/A", "N/A", "N/A", "N/A",
                            tenKhachHang,
                            formatDate(pdp.getNgayNhanPhongDuKien()),
                            formatDate(pdp.getNgayTraPhongDuKien())
                    });
                    continue;
                }

                for (String maPhong : dsMaPhong) {
                    PhongDTO phong = phongMap.get(maPhong);
                    if (phong == null) {
                        errorMessages.append("Không tìm thấy phòng ").append(maPhong)
                                .append(" trong phiếu ").append(pdp.getMaPDP()).append("\n");
                        errorCount++;
                        continue;
                    }

                    if (phong.getTinhTrang() != 0) {
                        tableModel.addRow(new Object[]{
                                pdp.getMaPDP(),
                                phong.getMaLoai() != null ? phong.getMaLoai() : "N/A",
                                phong.getTenPhong() != null ? phong.getTenPhong() : "N/A",
                                phong.getMaPhong() != null ? phong.getMaPhong() : "N/A",
                                getTrangThaiPhong(phong.getTinhTrang()),
                                tenKhachHang,
                                formatDate(pdp.getNgayNhanPhongDuKien()),
                                formatDate(pdp.getNgayTraPhongDuKien())
                        });
                    }
                }
            }

            if (errorCount > 0) {
                JOptionPane.showMessageDialog(this,
                        "Có " + errorCount + " lỗi xảy ra:\n" + errorMessages.toString(),
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (JsonSyntaxException e) {
            System.out.println("JSON Syntax Error in loadPhongData: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi định dạng dữ liệu từ server: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            System.out.println("Unexpected error in loadPhongData: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            table.repaint();
            table.revalidate();
        }
    }

    private String formatDate(LocalDate date) {
        return date != null ? date.toString() : "N/A";
    }

    private String getTrangThaiPhong(int tinhTrang) {
        switch (tinhTrang) {
            case 0: return "Trống";
            case 1: return "Đã đặt";
            case 2: return "Đang sử dụng";
            default: return "Không xác định";
        }
    }

    private void lamMoiDichVu() {
        tableModel2.setRowCount(0);
    }

    private void xacNhanDichVu() {
        System.out.println("Bắt đầu xác nhận dịch vụ...");

        // Kiểm tra điều kiện tiên quyết
        if (tableModel2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng thêm ít nhất một dịch vụ trước khi xác nhận!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedMaHD == null || selectedMaHD.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không tìm thấy mã phiếu đặt phòng!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Lấy thông tin khách hàng
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn khách hàng từ bảng!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kiểm tra và lấy thông tin nhân viên từ session
        if (!SessionManager.isUserLoggedIn()) {
            JOptionPane.showMessageDialog(this,
                    "Phiên làm việc đã hết hạn. Vui lòng đăng nhập lại!",
                    "Lỗi phiên làm việc", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien currentUser = SessionManager.getCurrentUser();
        String maNV = currentUser.getMaNhanVien();
        String maKH = table.getValueAt(selectedRow, 5).toString();

        // Tạo DTO
        PhieuDatDichVuDTO phieuDatDichVuDTO = new PhieuDatDichVuDTO();
        phieuDatDichVuDTO.setMaPDP(selectedMaHD);
        phieuDatDichVuDTO.setMaKH(maKH);
        phieuDatDichVuDTO.setMaNV(maNV);
        phieuDatDichVuDTO.setNgayDatDichVu(LocalDateTime.now());

        // Lấy danh sách mã dịch vụ
        List<String> dsMaDV = new ArrayList<>();
        for (int i = 0; i < tableModel2.getRowCount(); i++) {
            String tenDichVu = tableModel2.getValueAt(i, 0).toString();
            boolean found = false;

            for (int j = 0; j < tableModel1.getRowCount(); j++) {
                if (tableModel1.getValueAt(j, 1).toString().equals(tenDichVu)) {
                    dsMaDV.add(tableModel1.getValueAt(j, 0).toString());
                    found = true;
                    break;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy mã dịch vụ cho: " + tenDichVu,
                        "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        phieuDatDichVuDTO.setDsMaDV(dsMaDV);

        // Gửi request
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            System.out.println("Dữ liệu gửi đi: " + gson.toJson(phieuDatDichVuDTO));

            Request<PhieuDatDichVuDTO> request = new Request<>("CREATE_PHIEU_DAT_DICH_VU", phieuDatDichVuDTO);
            SocketManager.send(request);

            Response<?> response = SocketManager.receive(Response.class);
            if (response == null) {
                throw new IOException("Không nhận được phản hồi từ server");
            }

            System.out.println("Phản hồi từ server: " + gson.toJson(response));

            if (response.isSuccess()) {
                JOptionPane.showMessageDialog(this,
                        "Đặt dịch vụ thành công!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadDichVuDaDatData();

                // Đóng dialog
                Window window = SwingUtilities.getWindowAncestor(this);
                if (window != null) {
                    window.dispose();
                }
            } else {
                throw new Exception("Lỗi từ server: " + (response.getData() != null ? response.getData().toString() : "Không có thông tin lỗi"));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage(),
                    "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi hệ thống: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themDichVu() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ!");
            return;
        }

        String maDV = tableModel1.getValueAt(selectedRow, 0).toString();
        String tenDichVu = tableModel1.getValueAt(selectedRow, 1).toString();
        int soLuong = 1;
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        double gia = 0.0;
        try {
            String giaStr = tableModel1.getValueAt(selectedRow, 2).toString().replace(" VNĐ", "");
            gia = format.parse(giaStr).doubleValue();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi chuyển đổi giá trị số tiền!");
            return;
        }

        boolean isExist = false;
        for (int i = 0; i < tableModel2.getRowCount(); i++) {
            if (tableModel2.getValueAt(i, 0).toString().equals(tenDichVu)) {
                int currentSoLuong = Integer.parseInt(tableModel2.getValueAt(i, 1).toString());
                int newSoLuong = currentSoLuong + soLuong;
                double newThanhTien = newSoLuong * gia;
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                tableModel2.setValueAt(newSoLuong, i, 1);
                tableModel2.setValueAt(decimalFormat.format(newThanhTien) + " VNĐ", i, 2);
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            double thanhTien = soLuong * gia;
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            String thanhTienFormatted = decimalFormat.format(thanhTien) + " VNĐ";
            tableModel2.addRow(new Object[]{tenDichVu, soLuong, thanhTienFormatted});
        }

        table2.repaint();
        table1.repaint();
    }

    private void xoaDichVu() {
        int selectedRow = table2.getSelectedRow();
        if (selectedRow != -1) {
            tableModel2.removeRow(selectedRow);
        }
    }
}