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
import java.time.LocalDate;
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
    private JTextField txtSearchHoTen, txtSearchEmail, txtSearchSoDT;
    private NhanVien_DAO NhanVien_DAO;
    private static Set<String> danhSachMaNhanVien = new HashSet<>();

    private static final Map<Integer, String> CHUC_VU_MAP = new HashMap<>();
    private static final Map<String, Integer> CHUC_VU_MAP_REVERSE = new HashMap<>();
    static {
        CHUC_VU_MAP.put(1, "Lễ tân");
        CHUC_VU_MAP.put(2, "Quản lý");
        CHUC_VU_MAP.put(3, "Admin");
        CHUC_VU_MAP_REVERSE.put("Lễ tân", 1);
        CHUC_VU_MAP_REVERSE.put("Quản lý", 2);
        CHUC_VU_MAP_REVERSE.put("Admin", 3);
    }

    public void open() {
        NhanVien_DAO = new NhanVien_DAO();
        loadTableData();
    }

    public CapNhatNhanVien_FORM() {
        setLayout(new BorderLayout());
        setBackground(new Color(16, 16, 20));

        // North panel
        JPanel northPanel = new JPanel();
        northPanel.setOpaque(false);
        northPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        Box northPanelBox = Box.createVerticalBox();

        // Tìm kiếm đơn giản
        txtSearch = new JTextField("Tìm kiếm theo tên");
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
                if (txtSearch.getText().equals("Tìm kiếm theo tên")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm theo tên");
                }
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String keyword = txtSearch.getText().trim();
                    if (!keyword.equals("") && !keyword.equals("Tìm kiếm theo tên")) {
                        timKiemTheoTen(keyword);
                    } else {
                        JOptionPane.showMessageDialog(CapNhatNhanVien_FORM.this, "Vui lòng nhập tên nhân viên để tìm kiếm!");
                    }
                }
            }
        });

        JLabel searchIcon = new JLabel(new ImageIcon("imgs/TimKiemIcon.png"));
        searchIcon.setBounds(12, 12, 24, 24);

        JButton btnSearch = new JButton("Tìm");
        btnSearch.setBackground(new Color(27, 112, 213));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFont(FontManager.getManrope(Font.PLAIN, 15));
        btnSearch.setPreferredSize(new Dimension(80, 45));
        btnSearch.addActionListener(e -> {
            String keyword = txtSearch.getText().trim();
            if (!keyword.equals("") && !keyword.equals("Tìm kiếm theo tên")) {
                timKiemTheoTen(keyword);
            } else {
                JOptionPane.showMessageDialog(CapNhatNhanVien_FORM.this, "Vui lòng nhập tên nhân viên để tìm kiếm!");
            }
        });

        JButton btnClearSearch = new JButton("Xóa tìm kiếm");
        btnClearSearch.setBackground(new Color(151, 114, 35));
        btnClearSearch.setForeground(Color.WHITE);
        btnClearSearch.setFont(FontManager.getManrope(Font.PLAIN, 15));
        btnClearSearch.setPreferredSize(new Dimension(120, 45));
        btnClearSearch.addActionListener(e -> {
            txtSearch.setText("Tìm kiếm theo tên");
            txtSearch.setForeground(new Color(255, 255, 255, 125));
            loadTableData();
        });

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
        searchBox.add(Box.createHorizontalStrut(10));
        searchBox.add(btnSearch);
        searchBox.add(Box.createHorizontalStrut(10));
        searchBox.add(btnClearSearch);
        searchBox.add(Box.createGlue());

        // Tìm kiếm nâng cao
        JPanel advancedSearchPanel = new JPanel();
        advancedSearchPanel.setOpaque(false);
        advancedSearchPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        advancedSearchPanel.setPreferredSize(new Dimension(1642, 60));

        txtSearchHoTen = new JTextField("Họ tên");
        txtSearchEmail = new JTextField("Email");
        txtSearchSoDT = new JTextField("Số điện thoại");
        Dimension fieldSize = new Dimension(200, 45);
        txtSearchHoTen.setPreferredSize(fieldSize);
        txtSearchEmail.setPreferredSize(fieldSize);
        txtSearchSoDT.setPreferredSize(fieldSize);

        for (JTextField field : new JTextField[]{txtSearchHoTen, txtSearchEmail, txtSearchSoDT}) {
            field.setBackground(new Color(40, 40, 44));
            field.setForeground(new Color(255, 255, 255, 125));
            field.setFont(FontManager.getManrope(Font.PLAIN, 15));
            field.setBorder(emptyBorder);
            field.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    field.setBorder(combinedBorder);
                    if (field.getText().equals(field.getName())) {
                        field.setText("");
                        field.setForeground(Color.WHITE);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    field.setBorder(emptyBorder);
                    if (field.getText().isEmpty()) {
                        field.setForeground(new Color(255, 255, 255, 125));
                        field.setText(field.getName());
                    }
                }
            });
            field.setName(field.getText());
        }

        JButton btnAdvancedSearch = new JButton("Tìm kiếm nâng cao");
        btnAdvancedSearch.setBackground(new Color(27, 112, 213));
        btnAdvancedSearch.setForeground(Color.WHITE);
        btnAdvancedSearch.setPreferredSize(new Dimension(200, 45));
        btnAdvancedSearch.setFont(FontManager.getManrope(Font.PLAIN, 15));
        btnAdvancedSearch.addActionListener(e -> timKiemNangCao());

        advancedSearchPanel.add(txtSearchHoTen);
        advancedSearchPanel.add(txtSearchEmail);
        advancedSearchPanel.add(txtSearchSoDT);
        advancedSearchPanel.add(btnAdvancedSearch);

        // Form nhập liệu
        JPanel form = new JPanel();
        form.setPreferredSize(new Dimension(1400, 350));
        form.setOpaque(false);

        Box boxForm1 = createFormBox("Họ tên", txtHoTen = new JTextField());
        Box boxForm2 = createFormBox("Chức vụ", cmbChucVu = new JComboBox<>(new String[]{"Chọn", "Lễ tân", "Quản lý", "Admin"}));
        Box boxForm3 = createFormBox("Ngày sinh", dateNgaySinh = new JXDatePicker());
        Box boxForm4 = createFormBox("Ngày vào làm", dateNgayVaoLam = new JXDatePicker());
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
        northPanelBox.add(advancedSearchPanel);
        northPanelBox.add(Box.createVerticalStrut(10));
        northPanelBox.add(form);
        northPanelBox.setPreferredSize(new Dimension(1642, 480));
        northPanel.add(northPanelBox);

        // Center panel
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
                    String hoTen = tableModel.getValueAt(row, 1) != null ? String.valueOf(tableModel.getValueAt(row, 1)) : "";
                    String chucVu = tableModel.getValueAt(row, 2) != null ? String.valueOf(tableModel.getValueAt(row, 2)) : "";
                    Date ngaySinh = (Date) tableModel.getValueAt(row, 3);
                    Date ngayVaoLam = (Date) tableModel.getValueAt(row, 4);
                    String soDT = tableModel.getValueAt(row, 5) != null ? String.valueOf(tableModel.getValueAt(row, 5)) : "";
                    String diaChi = tableModel.getValueAt(row, 6) != null ? String.valueOf(tableModel.getValueAt(row, 6)) : "";
                    String email = tableModel.getValueAt(row, 7) != null ? String.valueOf(tableModel.getValueAt(row, 7)) : "";
                    Double luongCoBan = tableModel.getValueAt(row, 8) != null ? (Double) tableModel.getValueAt(row, 8) : 0.0;
                    Double heSoLuong = tableModel.getValueAt(row, 9) != null ? (Double) tableModel.getValueAt(row, 9) : 0.0;

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
                } else if (chucVu.equals("Quản lý")) {
                    txtLuong.setText("20000000");
                    txtHSL.setText("4.0");
                    txtLuong.setEditable(false);
                    txtHSL.setEditable(false);
                } else if (chucVu.equals("Admin")) {
                    txtLuong.setText("25000000");
                    txtHSL.setText("5.0");
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
        scroll.setPreferredSize(new Dimension(1642, 230));
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(titlePanel);
        centerPanel.add(scroll);

        // South panel
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
//        tableModel.setRowCount(0);
//
//        Request<Void> request = new Request<>("GET_ALL_NHAN_VIEN", null);
//        try {
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<List<NhanVienDTO>>>(){}.getType();
//            Response<List<NhanVienDTO>> response = SocketManager.receive(responseType);
//
//            if (response != null && response.isSuccess()) {
//                List<NhanVienDTO> dsNhanVien = response.getData();
//                if (dsNhanVien == null || dsNhanVien.isEmpty()) {
//                    JOptionPane.showMessageDialog(this,
//                            "Không có dữ liệu nhân viên!",
//                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
//
//                danhSachMaNhanVien.clear();
//                for (NhanVienDTO nv : dsNhanVien) {
//                    danhSachMaNhanVien.add(nv.getMaNhanVien());
//                    String chucVu = CHUC_VU_MAP.getOrDefault(nv.getChucVu(), "Không xác định");
//                    tableModel.addRow(new Object[]{
//                            nv.getMaNhanVien(),
//                            nv.getHoTen(),
//                            chucVu,
//                            nv.getNgaySinh() != null ? java.sql.Date.valueOf(nv.getNgaySinh()) : null,
//                            nv.getNgayVaoLam() != null ? java.sql.Date.valueOf(nv.getNgayVaoLam()) : null,
//                            String.valueOf(nv.getSDT()),
//                            nv.getDiaChi(),
//                            nv.getEmail(),
//                            nv.getLuongCoBan(),
//                            nv.getHeSoLuong()
//                    });
//                }
//                System.out.println("Loaded " + dsNhanVien.size() + " employees into table");
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Không thể lấy dữ liệu nhân viên từ server!",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this,
//                    "Lỗi kết nối đến server khi tải dữ liệu: " + ex.getMessage(),
//                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
//        }
//        table.repaint();
//        table.revalidate();
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

    private void timKiemTheoTen(String keyword) {
//        try {
//            Request<String> request = new Request<>("TIM_NHAN_VIEN_THEO_TEN", keyword);
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<List<NhanVienDTO>>>(){}.getType();
//            Response<List<NhanVienDTO>> response = SocketManager.receive(responseType);
//
//            if (response != null && response.isSuccess()) {
//                List<NhanVienDTO> dsNhanVien = response.getData();
//                if (dsNhanVien == null || dsNhanVien.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào với tên \"" + keyword + "\"!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
//
//                tableModel.setRowCount(0);
//                for (NhanVienDTO nv : dsNhanVien) {
//                    String chucVu = CHUC_VU_MAP.getOrDefault(nv.getChucVu(), "Không xác định");
//                    tableModel.addRow(new Object[]{
//                            nv.getMaNhanVien(),
//                            nv.getHoTen(),
//                            chucVu,
//                            nv.getNgaySinh() != null ? java.sql.Date.valueOf(nv.getNgaySinh()) : null,
//                            nv.getNgayVaoLam() != null ? java.sql.Date.valueOf(nv.getNgayVaoLam()) : null,
//                            String.valueOf(nv.getSDT()),
//                            nv.getDiaChi(),
//                            nv.getEmail(),
//                            nv.getLuongCoBan(),
//                            nv.getHeSoLuong()
//                    });
//                }
//                table.repaint();
//                table.revalidate();
//                JOptionPane.showMessageDialog(this, "Đã tìm thấy " + dsNhanVien.size() + " nhân viên!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
//            } else {
//                String errorMsg = response != null ? response.getData().toString() : "Lỗi không xác định từ server";
//                JOptionPane.showMessageDialog(this, "Không thể tìm kiếm nhân viên: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
//        }
    }

    private void timKiemNangCao() {
//        String hoTen = txtSearchHoTen.getText().trim();
//        String email = txtSearchEmail.getText().trim();
//        String sdt = txtSearchSoDT.getText().trim();
//
//        if (hoTen.equals("Họ tên")) hoTen = "";
//        if (email.equals("Email")) email = "";
//        if (sdt.equals("Số điện thoại")) sdt = "";
//
//        if (hoTen.isEmpty() && email.isEmpty() && sdt.isEmpty()) {
//            JOptionPane.showMessageDialog(this, "Vui lòng nhập ít nhất một tiêu chí tìm kiếm!");
//            return;
//        }
//
//        try {
//            String[] criteria = new String[]{hoTen, email, sdt};
//            Request<String[]> request = new Request<>("TIM_NHAN_VIEN_NANG_CAO", criteria);
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<List<NhanVienDTO>>>(){}.getType();
//            Response<List<NhanVienDTO>> response = SocketManager.receive(responseType);
//
//            if (response != null && response.isSuccess()) {
//                List<NhanVienDTO> dsNhanVien = response.getData();
//                if (dsNhanVien == null || dsNhanVien.isEmpty()) {
//                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!");
//                    return;
//                }
//
//                tableModel.setRowCount(0);
//                for (NhanVienDTO nv : dsNhanVien) {
//                    String chucVu = CHUC_VU_MAP.getOrDefault(nv.getChucVu(), "Không xác định");
//                    tableModel.addRow(new Object[]{
//                            nv.getMaNhanVien(),
//                            nv.getHoTen(),
//                            chucVu,
//                            nv.getNgaySinh() != null ? java.sql.Date.valueOf(nv.getNgaySinh()) : null,
//                            nv.getNgayVaoLam() != null ? java.sql.Date.valueOf(nv.getNgayVaoLam()) : null,
//                            String.valueOf(nv.getSDT()),
//                            nv.getDiaChi(),
//                            nv.getEmail(),
//                            nv.getLuongCoBan(),
//                            nv.getHeSoLuong()
//                    });
//                }
//                table.repaint();
//                table.revalidate();
//            } else {
//                JOptionPane.showMessageDialog(this, "Không thể tìm kiếm nhân viên: " + (response != null ? response.getData() : "Lỗi không xác định"));
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage());
//        }
    }

    private void moveRowToTop(int rowIndex) {
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(rowIndex, i);
        }
        tableModel.removeRow(rowIndex);
        tableModel.insertRow(0, rowData);
    }

    private void lamMoi() {
        txtHoTen.setText("");
        cmbChucVu.setSelectedIndex(0);
        txtSoDT.setText("");
        txtDiaChi.setText("");
        txtEmail.setText("");
        txtLuong.setText("");
        txtHSL.setText("");
        txtHoTen.requestFocus();

        txtSearchHoTen.setText("Họ tên");
        txtSearchHoTen.setForeground(new Color(255, 255, 255, 125));
        txtSearchEmail.setText("Email");
        txtSearchEmail.setForeground(new Color(255, 255, 255, 125));
        txtSearchSoDT.setText("Số điện thoại");
        txtSearchSoDT.setForeground(new Color(255, 255, 255, 125));

        Date today = new Date();
        dateNgaySinh.setDate(today);
        dateNgayVaoLam.setDate(today);

        loadTableData();
    }

    private void xoaNhanVien() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
//            return;
//        }
//
//        String maNhanVien = (String) tableModel.getValueAt(selectedRow, 0);
//        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa nhân viên " + maNhanVien + "?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
//        if (confirm != JOptionPane.YES_OPTION) {
//            return;
//        }
//
//        try {
//            Request<String> request = new Request<>("XOA_NHAN_VIEN", maNhanVien);
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<String>>(){}.getType();
//            Response<String> response = SocketManager.receive(responseType);
//
//            if (response != null && response.isSuccess()) {
//                tableModel.removeRow(selectedRow);
//                danhSachMaNhanVien.remove(maNhanVien);
//                JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
//            } else {
//                JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại: " + (response != null ? response.getData() : "Lỗi không xác định"));
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage());
//        }
    }

    private void suaNhanVien() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
//            return;
//        }
//
//        if (!kiemTraDuLieu()) {
//            return;
//        }
//
//        try {
//            String maNhanVien = (String) tableModel.getValueAt(selectedRow, 0);
//            String hoTen = txtHoTen.getText().trim();
//            String chucVuStr = (String) cmbChucVu.getSelectedItem();
//            Integer chucVu = CHUC_VU_MAP_REVERSE.get(chucVuStr);
//            Date ngaySinh = dateNgaySinh.getDate();
//            LocalDate ngaySinhLD = new java.sql.Date(ngaySinh.getTime()).toLocalDate();
//            Date ngayVaoLam = dateNgayVaoLam.getDate();
//            LocalDate ngayVaoLamLD = new java.sql.Date(ngayVaoLam.getTime()).toLocalDate();
//            String soDT = txtSoDT.getText().trim();
//            String diaChi = txtDiaChi.getText().trim();
//            String email = txtEmail.getText().trim();
//            double luongCoBan = Double.parseDouble(txtLuong.getText().trim());
//            double heSoLuong = Double.parseDouble(txtHSL.getText().trim());
//
//            NhanVienDTO nv = new NhanVienDTO();
//            nv.setMaNhanVien(maNhanVien);
//            nv.setHoTen(hoTen);
//            nv.setChucVu(chucVu);
//            nv.setNgaySinh(ngaySinhLD);
//            nv.setNgayVaoLam(ngayVaoLamLD);
//            nv.setSDT(String.valueOf(Integer.parseInt(soDT)));
//            nv.setDiaChi(diaChi);
//            nv.setEmail(email);
//            nv.setLuongCoBan(luongCoBan);
//            nv.setHeSoLuong(heSoLuong);
//
//            Request<NhanVienDTO> request = new Request<>("CAP_NHAT_NHAN_VIEN", nv);
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<String>>(){}.getType();
//            Response<String> response = SocketManager.receive(responseType);
//
//            if (response != null && response.isSuccess()) {
//                tableModel.setValueAt(hoTen, selectedRow, 1);
//                tableModel.setValueAt(chucVuStr, selectedRow, 2);
//                tableModel.setValueAt(java.sql.Date.valueOf(ngaySinhLD), selectedRow, 3);
//                tableModel.setValueAt(java.sql.Date.valueOf(ngayVaoLamLD), selectedRow, 4);
//                tableModel.setValueAt(soDT, selectedRow, 5);
//                tableModel.setValueAt(diaChi, selectedRow, 6);
//                tableModel.setValueAt(email, selectedRow, 7);
//                tableModel.setValueAt(luongCoBan, selectedRow, 8);
//                tableModel.setValueAt(heSoLuong, selectedRow, 9);
//
//                table.repaint();
//                table.revalidate();
//                JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
//            } else {
//                JOptionPane.showMessageDialog(this, "Sửa nhân viên thất bại: " + (response != null ? response.getData() : "Lỗi không xác định"));
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi không mong muốn: " + ex.getMessage());
//        }
    }

    private boolean kiemTraDuLieu() {
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ tên không được để trống!");
            txtHoTen.requestFocus();
            return false;
        }

        String chucVu = (String) cmbChucVu.getSelectedItem();
        if (chucVu.equals("Chọn")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn chức vụ!");
            cmbChucVu.requestFocus();
            return false;
        }

        Date ngaySinh = dateNgaySinh.getDate();
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Ngày sinh không được để trống!");
            dateNgaySinh.requestFocus();
            return false;
        }

        Date ngayVaoLam = dateNgayVaoLam.getDate();
        if (ngayVaoLam == null) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm không được để trống!");
            dateNgayVaoLam.requestFocus();
            return false;
        }

        Date ngayHienTai = new Date();
        if (ngayVaoLam.after(ngayHienTai)) {
            JOptionPane.showMessageDialog(this, "Ngày vào làm phải trước hoặc bằng ngày hiện tại!");
            dateNgayVaoLam.requestFocus();
            return false;
        }

        long tuoi = (ngayVaoLam.getTime() - ngaySinh.getTime()) / (1000L * 60 * 60 * 24 * 365);
        if (tuoi < 18) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải đủ 18 tuổi khi vào làm!");
            dateNgaySinh.requestFocus();
            return false;
        }

        String soDT = txtSoDT.getText().trim();
        String regexSoDT = "^0[0-9]{9}$";
        if (!Pattern.matches(regexSoDT, soDT)) {
            JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!");
            txtSoDT.requestFocus();
            return false;
        }

        String diaChi = txtDiaChi.getText().trim();
        if (diaChi.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Địa chỉ không được để trống!");
            txtDiaChi.requestFocus();
            return false;
        }

        String email = txtEmail.getText().trim();
        String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!Pattern.matches(regexEmail, email)) {
            JOptionPane.showMessageDialog(this, "Email không hợp lệ!");
            txtEmail.requestFocus();
            return false;
        }

        String luongStr = txtLuong.getText().trim();
        double luongCoBan;
        try {
            luongCoBan = Double.parseDouble(luongStr);
            if (luongCoBan <= 0) {
                JOptionPane.showMessageDialog(this, "Lương cơ bản phải lớn hơn 0!");
                txtLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lương cơ bản phải là một số hợp lệ!");
            txtLuong.requestFocus();
            return false;
        }

        String hslStr = txtHSL.getText().trim();
        double heSoLuong;
        try {
            heSoLuong = Double.parseDouble(hslStr);
            if (heSoLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Hệ số lương phải lớn hơn 0!");
                txtHSL.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Hệ số lương phải là một số hợp lệ!");
            txtHSL.requestFocus();
            return false;
        }

        return true;
    }

    private void themNhanVien() {
//        try {
//            if (!kiemTraDuLieu()) {
//                return;
//            }
//
//            String hoTen = txtHoTen.getText().trim();
//            String chucVuStr = (String) cmbChucVu.getSelectedItem();
//            Integer chucVu = CHUC_VU_MAP_REVERSE.get(chucVuStr);
//            Date ngaySinh = dateNgaySinh.getDate();
//            LocalDate ngaySinhLD = new java.sql.Date(ngaySinh.getTime()).toLocalDate();
//            Date ngayVaoLam = dateNgayVaoLam.getDate();
//            LocalDate ngayVaoLamLD = new java.sql.Date(ngayVaoLam.getTime()).toLocalDate();
//            String soDT = txtSoDT.getText().trim();
//            String diaChi = txtDiaChi.getText().trim();
//            String email = txtEmail.getText().trim();
//            double luongCoBan = Double.parseDouble(txtLuong.getText().trim());
//            double heSoLuong = Double.parseDouble(txtHSL.getText().trim());
//
//            String maNV = taoMaNhanVien();
//
//            NhanVienDTO nv = new NhanVienDTO();
//            nv.setMaNhanVien(maNV);
//            nv.setHoTen(hoTen);
//            nv.setChucVu(chucVu);
//            nv.setNgaySinh(ngaySinhLD);
//            nv.setNgayVaoLam(ngayVaoLamLD);
//            nv.setSDT(String.valueOf(Integer.parseInt(soDT)));
//            nv.setDiaChi(diaChi);
//            nv.setEmail(email);
//            nv.setLuongCoBan(luongCoBan);
//            nv.setHeSoLuong(heSoLuong);
//
//            String jsonRequest = SocketManager.getGson().toJson(nv);
//            System.out.println("Preparing to send NhanVienDTO: " + jsonRequest);
//
//            Request<NhanVienDTO> request = new Request<>("THEM_NHAN_VIEN", nv);
//            SocketManager.send(request);
//            Type responseType = new TypeToken<Response<String>>(){}.getType();
//            Response<String> response = SocketManager.receive(responseType);
//
//            System.out.println("Received response for THEM_NHAN_VIEN: " + (response != null ? SocketManager.getGson().toJson(response) : "null"));
//
//            if (response != null && response.isSuccess()) {
//                Object[] rowData = new Object[]{
//                        nv.getMaNhanVien(),
//                        nv.getHoTen(),
//                        CHUC_VU_MAP.getOrDefault(nv.getChucVu(), "Không xác định"),
//                        java.sql.Date.valueOf(nv.getNgaySinh()),
//                        java.sql.Date.valueOf(nv.getNgayVaoLam()),
//                        String.valueOf(nv.getSDT()),
//                        nv.getDiaChi(),
//                        nv.getEmail(),
//                        nv.getLuongCoBan(),
//                        nv.getHeSoLuong()
//                };
//                tableModel.insertRow(0, rowData);
//                System.out.println("Inserted row into tableModel: maNhanVien=" + maNV + ", rowCount=" + tableModel.getRowCount());
//
//                table.repaint();
//                table.revalidate();
//
//                lamMoi();
//                JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công! Mã nhân viên: " + maNV);
//            } else {
//                danhSachMaNhanVien.remove(maNV);
//                String errorMsg = response != null ? response.getData() : "Lỗi không xác định từ server";
//                System.out.println("Server error for THEM_NHAN_VIEN: " + errorMsg);
//                JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại: " + errorMsg);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            System.out.println("IOException in themNhanVien: " + ex.getMessage());
//            JOptionPane.showMessageDialog(this, "Lỗi kết nối đến server: " + ex.getMessage());
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            System.out.println("Unexpected error in themNhanVien: " + ex.getMessage());
//            JOptionPane.showMessageDialog(this, "Lỗi không mong muốn: " + ex.getMessage());
//        }
    }

    private String taoMaNhanVien() {
        String maNV;
        Random random = new Random();
        do {
            int soNgauNhien = random.nextInt(10000);
            maNV = String.format("NV%04d", soNgauNhien);
        } while (danhSachMaNhanVien.contains(maNV));
        danhSachMaNhanVien.add(maNV);
        return maNV;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}