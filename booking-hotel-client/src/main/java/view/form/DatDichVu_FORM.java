package view.form;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dto.*;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;
import utils.custom_element.CustomTableCellRenderer;
import dao.DichVu_DAO;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    private JButton btnLamMoi;
    private JButton btnXacNhan;
    private DichVu_DAO dichVuDao;
    private String selectedMaHD;
    private String selectedMaPhong;
    private String selectMaPDP;
    private String selectedTenKhach;
    private JButton btnXoa;

    public DatDichVu_FORM() {
        dichVuDao = new DichVu_DAO();
        setBackground(new Color(16, 16, 20));
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));

        // Tim kiem
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

        // Tạo bang
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
        b.add(Box.createVerticalStrut(300));

        // Tạo nut
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

        // Trong constructor hoặc phương thức khởi tạo
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
                selectedMaPhong = table.getValueAt(selectedRow, 1).toString();
                selectedTenKhach = table.getValueAt(selectedRow, 5).toString();
                phieuDatDichVu();
            }
        });
//        table.addMouseListener(this);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    selectedMaHD = table.getValueAt(selectedRow, 0).toString(); // Cột mã hóa đơn
                    selectedMaPhong = table.getValueAt(selectedRow, 3).toString(); // Cột Phòng (chỉ số 3)
                    selectedTenKhach = table.getValueAt(selectedRow, 5).toString(); // Cột Tên khách (chỉ số 5)
                    System.out.println("Selected MaHD: " + selectedMaHD + ", MaPhong: " + selectedMaPhong + ", TenKhach: " + selectedTenKhach); // Debug
                }
            }
        });
        loadPhongData();
    }

    public void phieuDatDichVu() {
        JDialog dialog = new JDialog();
        dialog.setSize(1564, 880);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // Màu sắc chủ đạo
        Color backgroundColor = new Color(31, 31, 32);
        Color panelColor = new Color(40, 40, 44);
        Color primaryColor = new Color(27, 112, 213);
        Color textColor = Color.WHITE;

        // Thiết lập font
        Font titleFont = FontManager.getManrope(Font.BOLD, 24);
        Font headerFont = FontManager.getManrope(Font.BOLD, 16);
        Font normalFont = FontManager.getManrope(Font.PLAIN, 16);
        Font buttonFont = FontManager.getManrope(Font.BOLD, 16);

        dialog.getContentPane().setBackground(backgroundColor);

        // ========== HEADER SECTION ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(panelColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("PHIẾU ĐẶT DỊCH VỤ");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(textColor);

        // Nút đóng với hiệu ứng hover
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
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor),
                BorderFactory.createEmptyBorder(10, 40, 10, 10)
        ));
        searchField.setBackground(panelColor);
        searchField.setForeground(textColor);
        searchField.setFont(normalFont);
        searchField.setToolTipText("Nhập tên dịch vụ để tìm kiếm...");

        // Search icon
        JLabel searchIcon = new JLabel(new ImageIcon("imgs/TimKiemIcon.png"));
        searchIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        leftPanel.add(searchPanel, BorderLayout.NORTH);

        // Info panel (hiển thị mã đặt phòng, tên khách, tên phòng)
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

        // Thêm infoPanel vào leftPanel, dưới searchPanel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(panelColor);
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(infoPanel, BorderLayout.CENTER);
        leftPanel.add(topPanel, BorderLayout.NORTH);

        // Table panel
        String[] colNames = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Đơn vị tính", "Mô tả"};
        tableModel1 = new DefaultTableModel(colNames, 0);
        table1 = new JTable(tableModel1);

//        customizeTable(table1);
        table1.setRowSelectionAllowed(true); // Cho phép chọn hàng
        table1.setCellSelectionEnabled(false); // Tắt chọn từng ô
        table1.setShowVerticalLines(false); // Ẩn đường lưới dọc (theo yêu cầu trước)
        table1.setRowHeight(55); // Chiều cao hàng
        table1.setBackground(panelColor);
        table1.setForeground(textColor);
        table1.setFont(normalFont);
        table1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                System.out.println("Selected row in table1: " + selectedRow);
                table1.repaint(); // Làm mới giao diện để đảm bảo màu nền cập nhật
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
        btnThem.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(74, 74, 66, 100));
        btnThem.setOpaque(false);
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.setMinimumSize(new Dimension(150, 40));
        btnThem.setMaximumSize(new Dimension(150, 40));
        btnThem.addActionListener(this);
        btnLamMoi = createStyledButton("LÀM MỚI", new Color(100, 100, 100), 180, 45);
        btnLamMoi.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(74, 74, 66, 100));
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setMinimumSize(new Dimension(150, 40));
        btnLamMoi.setOpaque(false);
        btnLamMoi.setMaximumSize(new Dimension(150, 40));
        btnLamMoi.addActionListener(this);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnLamMoi);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ===== RIGHT PANEL - DỊCH VỤ ĐÃ CHỌN =====
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(panelColor);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Selected services table
        String[] colNames2 = {"Tên dịch vụ", "Số lượng", "Thành tiền"};
        tableModel2 = new DefaultTableModel(colNames2, 0);
        table2 = new JTable(tableModel2);
        customizeTable(table2);

        JScrollPane scrollPane2 = new JScrollPane(table2);
        scrollPane2.setBorder(null);
        scrollPane2.getViewport().setBackground(panelColor);
        rightPanel.add(scrollPane2, BorderLayout.CENTER);

        // Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        actionPanel.setBackground(panelColor);

        btnXoa = createStyledButton("XÓA", new Color(200, 60, 60), 180, 45);
        btnXoa.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(151, 69, 35, 100));
        btnXoa.setOpaque(false);
        btnXoa.setPreferredSize(new Dimension(150, 40));
        btnXoa.setMinimumSize(new Dimension(150, 40));
        btnXoa.setMaximumSize(new Dimension(150, 40));
        btnXoa.addActionListener(this);
        btnXacNhan = createStyledButton("XÁC NHẬN", new Color(60, 180, 60), 180, 45);
        btnXacNhan.setFont(FontManager.getManrope(Font.PLAIN, 16));
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

        // Add panels to main content
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        dialog.add(mainPanel, BorderLayout.CENTER);

        // ========== FINAL TOUCHES ==========
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setUndecorated(true);

        // Shadow effect
        dialog.setBackground(new Color(0, 0, 0, 0));
        ((JComponent)dialog.getContentPane()).setBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1)
        );

        dialog.setVisible(true);

        System.out.println("MaHD: " + selectedMaHD + ", MaPhong: " + selectedMaPhong); // Debug
        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(dialog,
                    "Vui lòng chọn một phòng trước khi đặt dịch vụ!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            return; // Không đóng dialog
        }
        // Load data
        loadDichVuData();
        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(dialog,
                    "Vui lòng chọn một phòng trước khi đặt dịch vụ!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
            dialog.dispose();
            return;
        }
        loadDichVuDaDat(selectedMaHD, selectedMaPhong);
    }

    // ========== HELPER METHODS ==========

        private JButton createStyledButton(String text, Color bgColor, int width, int height) {
            JButton button = new JButton(text);
            button.setFont(FontManager.getManrope(Font.BOLD, 16));
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setOpaque(true);
            button.setPreferredSize(new Dimension(width, height));

            // Hover effect
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
        table.setIntercellSpacing(new Dimension(5, 5)); // Tăng khoảng cách ngang và dọc

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(60, 60, 64));
        header.setForeground(Color.WHITE);
        header.setFont(FontManager.getManrope(Font.BOLD, 16));
        header.setPreferredSize(new Dimension(header.getWidth(), 45));
        header.setReorderingAllowed(false);

        // Cell renderer
//        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
//            @Override
//            public Component getTableCellRendererComponent(JTable table, Object value,
//                                                           boolean isSelected, boolean hasFocus, int row, int column) {
//                Component c = super.getTableCellRendererComponent(table, value,
//                        isSelected, hasFocus, row, column);
//                c.setBackground(row % 2 == 0 ? new Color(50, 50, 54) : new Color(45, 45, 49));
//                return c;
//            }
//        };
//        renderer.setHorizontalAlignment(SwingConstants.CENTER);
//
//        for (int i = 0; i < table.getColumnCount(); i++) {
//            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
//        }
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
        // (Giữ nguyên phần code hiện tại hoặc tích hợp với server nếu cần)
        if (e.getSource() == table) {
            int row = table.getSelectedRow();
            if (row != -1) {
                selectedMaHD = table.getValueAt(row, 0).toString(); // Cột mã hóa đơn
                selectedMaPhong = table.getValueAt(row, 1).toString(); // Cột mã phòng
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
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                for (DichVuDTO dv : dsDichVu) {
                    tableModel1.addRow(new Object[]{
                            dv.getMaDV(),
                            dv.getTenDV(),
                            decimalFormat.format(dv.getDonGia()),
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
        // Xóa dữ liệu cũ trong bảng
        tableModel1.setRowCount(0);
        // Tạo yêu cầu gửi đến server
        Request<Void> request = new Request<>("GET_ALL_DICH_VU", null);

        try {
            // Gửi yêu cầu và nhận phản hồi
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> response = SocketManager.receiveType(responseType);

            // Xử lý phản hồi từ server
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

            // Định dạng số tiền
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

            // Thêm dữ liệu vào bảng
            for (DichVuDTO dv : dsDichVu) {
                tableModel1.addRow(new Object[]{
                        dv.getMaDV(),               // Mã dịch vụ
                        dv.getTenDV(),              // Tên dịch vụ
                        decimalFormat.format(dv.getDonGia()) + " VNĐ", // Giá dịch vụ
                        dv.getDonViTinh(),          // Đơn vị tính
                        dv.getMoTa()                // Mô tả
                });
            }

            // Cập nhật giao diện bảng
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

    // Hàm tùy chỉnh giao diện bảng (giữ nguyên)
    private void customizeTableAppearance() {
        table1.setBackground(new Color(24, 24, 28));
        table1.setForeground(Color.WHITE);
        table1.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table1.setRowHeight(55);
        table1.getTableHeader().setFont(FontManager.getManrope(Font.BOLD, 16));
        table1.getTableHeader().setBackground(new Color(40, 40, 45));
        table1.getTableHeader().setForeground(Color.WHITE);
//        table1.setShowHorizontalLines(false);
        table1.setSelectionBackground(new Color(60, 60, 70));
    }

    // Hàm xử lý lỗi kết nối (giữ nguyên)
    private void handleConnectionError(IOException ex) {
        ex.printStackTrace();
        int option = JOptionPane.showConfirmDialog(this,
                "Lỗi kết nối đến server: " + ex.getMessage() +
                        "\nBạn có muốn thử lại không?",
                "Lỗi hệ thống",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE);

        if (option == JOptionPane.YES_OPTION) {
            loadDichVuData(); // Thử lại nếu người dùng chọn Yes
        }
    }

    private void loadPhongData() {
        tableModel.setRowCount(0);

        try {
            // 1. Lấy danh sách phiếu đặt phòng
            Request<Void> request = new Request<>("GET_ALL_PHIEU_DAT_PHONG", null);
            SocketManager.send(request);
            Type phieuResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>() {}.getType();
            Response<List<PhieuDatPhongDTO>> response = SocketManager.receiveType(phieuResponseType);

            if (response == null || !response.isSuccess() || response.getData() == null) {
                String errorMsg = response == null ? "Không nhận được phản hồi từ server"
                        : (response.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
                System.out.println("GET_ALL_PHIEU_DAT_PHONG failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<PhieuDatPhongDTO> dsPhieuDatPhong = response.getData();
            if (dsPhieuDatPhong.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu phiếu đặt phòng",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 2. Lấy danh sách phòng
            Map<String, PhongDTO> phongMap = new HashMap<>();
            Request<Void> phongRequest = new Request<>("GET_ALL_PHONG", null);
            SocketManager.send(phongRequest);
            Type phongResponseType = new TypeToken<Response<List<PhongDTO>>>() {}.getType();
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

            // 3. Lấy danh sách khách hàng
            Map<String, KhachHangDTO> khachHangMap = new HashMap<>();
            Request<Void> khachHangRequest = new Request<>("GET_ALL_KHACH_HANG", null);
            SocketManager.send(khachHangRequest);
            Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
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

            // 4. Xử lý dữ liệu và hiển thị
            StringBuilder errorMessages = new StringBuilder();
            int errorCount = 0;

            for (PhieuDatPhongDTO pdp : dsPhieuDatPhong) {
                // Xử lý thông tin khách hàng
                String tenKhachHang = "Không xác định";
                if (pdp.getMaKH() != null) {
                    KhachHangDTO kh = khachHangMap.get(pdp.getMaKH());
                    tenKhachHang = kh != null ? kh.getHoTen() : "Khách hàng không tồn tại";
                }

                // Xử lý danh sách phòng
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

                // Thêm từng phòng vào bảng
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

            // Hiển thị lỗi nếu có
            if (errorCount > 0) {
                JOptionPane.showMessageDialog(this,
                        "Có " + errorCount + " lỗi xảy ra:\n" + errorMessages.toString(),
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

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

    // Hàm hỗ trợ
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

    private void loadDichVuDaDat(String maHD, String maPhong) {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void lamMoiDichVu() {
        tableModel2.setRowCount(0);
    }

    private void xacNhanDichVu() {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void themDichVu() {
        int selectedRow = table1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ!");
            return;
        }

        String maDV = tableModel1.getValueAt(selectedRow, 0).toString();
        String tenDichVu = tableModel1.getValueAt(selectedRow, 1).toString();
        int soLuongTon = Integer.parseInt(tableModel1.getValueAt(selectedRow, 4).toString());
        if (soLuongTon <= 0) {
            JOptionPane.showMessageDialog(this, "Dịch vụ này đã hết hàng!");
            return;
        }

        int soLuong = 1;
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        double gia = 0.0;
        try {
            gia = format.parse(tableModel1.getValueAt(selectedRow, 2).toString()).doubleValue();
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Lỗi chuyển đổi giá trị số tiền!");
            return;
        }

        DichVuDTO dichVuDTO = new DichVuDTO();
        dichVuDTO.setMaDV(maDV);
        dichVuDTO.setTenDV(tenDichVu);
        dichVuDTO.setDonGia(gia);
        dichVuDTO.setDonViTinh(tableModel1.getValueAt(selectedRow, 3).toString());
        dichVuDTO.setMoTa(tableModel1.getValueAt(selectedRow, 4).toString());

        Request<DichVuDTO> request = new Request<>("SUA_DICH_VU", dichVuDTO);
        try {
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<String>>(){}.getType();
            Response<String> response = SocketManager.receive(Response.class);

            if (response != null && response.isSuccess()) {
                tableModel1.setValueAt(soLuongTon - soLuong, selectedRow, 4);
                double thanhTien = soLuong * gia;
                DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
                String thanhTienFormatted = decimalFormat.format(thanhTien);

                boolean isExist = false;
                for (int i = 0; i < tableModel2.getRowCount(); i++) {
                    if (tableModel2.getValueAt(i, 0).toString().equals(tenDichVu)) {
                        int currentSoLuong = Integer.parseInt(tableModel2.getValueAt(i, 1).toString());
                        int newSoLuong = currentSoLuong + soLuong;
                        double newThanhTien = newSoLuong * gia;
                        tableModel2.setValueAt(newSoLuong, i, 1);
                        tableModel2.setValueAt(decimalFormat.format(newThanhTien), i, 2);
                        isExist = true;
                        break;
                    }
                }

                if (!isExist) {
                    tableModel2.addRow(new Object[]{tenDichVu, soLuong, thanhTienFormatted});
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không thể cập nhật số lượng tồn trên server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                themDichVu();
            }
        }
    }

    private void xoaDichVu() {
        int selectedRow = table2.getSelectedRow();
        if (selectedRow != -1) {
            String tenDichVu = tableModel2.getValueAt(selectedRow, 0).toString();
            int soLuongXoa = Integer.parseInt(tableModel2.getValueAt(selectedRow, 1).toString());
            tableModel2.removeRow(selectedRow);
            for (int i = 0; i < tableModel1.getRowCount(); i++) {
                if (tableModel1.getValueAt(i, 1).toString().equals(tenDichVu)) {
                    int soLuongTon = Integer.parseInt(tableModel1.getValueAt(i, 4).toString());
                    tableModel1.setValueAt(soLuongTon + soLuongXoa, i, 4);
                    break;
                }
            }
        }
    }
}