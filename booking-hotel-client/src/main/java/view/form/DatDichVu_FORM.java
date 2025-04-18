package view.form;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.*;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;
import dao.DichVu_DAO;

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

        btnDatDV.addActionListener(this);
        table.addMouseListener(this);
        loadPhongData();
    }

    public void phieuDatDichVu() {
        JDialog dialog = new JDialog();
        dialog.setSize(1564, 880);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        f1 = new Font("Montserrat", Font.PLAIN, 16);
        dialog.getContentPane().setBackground(new Color(31, 31, 32, 255));

        JButton btnClose = new JButton("X");
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setBackground(new Color(255, 69, 58));
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setOpaque(true);
        btnClose.setPreferredSize(new Dimension(45, 45));

        btnClose.addActionListener(e -> dialog.dispose());

        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setOpaque(false);
        closePanel.add(btnClose);

        dialog.add(closePanel, BorderLayout.NORTH);

        Box bdialog = Box.createHorizontalBox();
        Box bLeft = Box.createVerticalBox();
        bLeft.add(Box.createVerticalStrut(10));
        bLeft.setPreferredSize(new Dimension(950, 880));
        bLeft.setMinimumSize(new Dimension(950, 880));
        bLeft.setMaximumSize(new Dimension(950, 880));
        JTextField searchField1 = new JTextField("Tìm kiếm tên dịch vụ");
        Border emptyBorder = BorderFactory.createEmptyBorder(13, 52, 12, 0);
        searchField1.setBounds(0, 0, 280, 45);
        searchField1.setBorder(emptyBorder);
        searchField1.setBackground(new Color(40, 40, 44));
        searchField1.setForeground(new Color(255, 255, 255, 125));
        searchField1.setFont(FontManager.getManrope(Font.PLAIN, 15));
        CompoundBorder combinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), emptyBorder);
        searchField1.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                searchField1.setBorder(combinedBorder);
                if (searchField1.getText().equals("Tìm kiếm tên dịch vụ")) {
                    searchField1.setText("");
                    searchField1.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                searchField1.setBorder(emptyBorder);
                if (searchField1.getText().isEmpty()) {
                    searchField1.setForeground(new Color(255, 255, 255, 125));
                    searchField1.setText("Tìm kiếm tên dịch vụ");
                }
            }
        });
        searchField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String keyword = searchField1.getText().trim();
                    if (!keyword.equals("") && !keyword.equals("Tìm kiếm tên dịch vụ")) {
                        timKiem1(keyword);
                    }
                }
            }
        });
        JLabel searchIcon1 = new JLabel(new ImageIcon("imgs/TimKiemIcon.png"));
        searchIcon1.setBounds(12, 12, 24, 24);

        JPanel searchPanel1 = new JPanel();
        searchPanel1.setOpaque(false);
        searchPanel1.setLayout(null);
        Dimension searchPanelSize = new Dimension(280, 45);
        searchPanel1.setPreferredSize(searchPanelSize);
        searchPanel1.setMinimumSize(searchPanelSize);
        searchPanel1.setMaximumSize(searchPanelSize);

        searchPanel1.add(searchIcon1);
        searchPanel1.add(searchField1);
        Box bsearch = Box.createHorizontalBox();
        bsearch.add(Box.createHorizontalStrut(0));
        bsearch.add(searchPanel1);
        bsearch.add(Box.createGlue());
        bLeft.add(bsearch);
        bLeft.add(Box.createVerticalStrut(20));

        bLeft.add(Box.createVerticalStrut(10));
        JLabel titleLabel1 = new JLabel("Danh sách dịch vụ");
        titleLabel1.setFont(FontManager.getManrope(Font.BOLD, 16));
        titleLabel1.setForeground(Color.white);

        RoundedPanel titlePanel1 = new RoundedPanel(10, 0, new Color(27, 112, 213));
        titlePanel1.setPreferredSize(new Dimension(950, 50));
        titlePanel1.setMinimumSize(new Dimension(950, 50));
        titlePanel1.setMaximumSize(new Dimension(950, 50));
        titlePanel1.setOpaque(false);
        titlePanel1.setLayout(new BoxLayout(titlePanel1, BoxLayout.X_AXIS));

        titlePanel1.add(Box.createHorizontalStrut(15));
        titlePanel1.add(titleLabel1);
        titlePanel1.add(Box.createHorizontalGlue());
        bLeft.add(titlePanel1);
        bLeft.add(Box.createVerticalStrut(5));

        bLeft.add(Box.createVerticalStrut(10));
        Box bTable1 = Box.createHorizontalBox();
        String[] colName1 = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Đơn vị tính", "Tồn kho"};
        tableModel1 = new DefaultTableModel(colName1, 0);
        JScrollPane scroll1;
        bTable1.add(scroll1 = new JScrollPane(table1 = new JTable(tableModel1), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table1.setBackground(new Color(24, 24, 28));
        table1.setForeground(Color.WHITE);
        table1.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table1.setRowHeight(55);

        JTableHeader header1 = table1.getTableHeader();
        header1.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header1.setPreferredSize(new Dimension(header1.getPreferredSize().width, 55));
        header1.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer1 = new CustomCellRenderer();
        for (int i = 0; i < table1.getColumnCount(); i++) {
            TableColumn column = table1.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer1);
        }

        scroll1.setBounds(30, 380, 940, 700);
        scroll1.setBorder(null);
        scroll1.getViewport().setOpaque(false);
        scroll1.setViewportBorder(null);
        bLeft.add(bTable1);
        bLeft.add(Box.createVerticalStrut(300));

        Box bbutton1 = Box.createHorizontalBox();
        bbutton1.add(Box.createHorizontalStrut(500));
        bbutton1.add(btnThem = new JButton("Thêm"));
        btnThem.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnThem.setForeground(Color.WHITE);
        btnThem.setBackground(new Color(74, 74, 66, 100));
        btnThem.setOpaque(false);
        btnThem.setPreferredSize(new Dimension(150, 40));
        btnThem.setMinimumSize(new Dimension(150, 40));
        btnThem.setMaximumSize(new Dimension(150, 40));
        btnThem.addActionListener(this);
        bbutton1.add(Box.createHorizontalStrut(20));
        bbutton1.add(btnLamMoi = new JButton("Làm mới"));
        btnLamMoi.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setBackground(new Color(74, 74, 66, 100));
        btnLamMoi.setOpaque(false);
        btnLamMoi.setPreferredSize(new Dimension(150, 40));
        btnLamMoi.setMinimumSize(new Dimension(150, 40));
        btnLamMoi.setMaximumSize(new Dimension(150, 40));
        btnLamMoi.addActionListener(this);
        bLeft.add(Box.createVerticalStrut(10));
        bLeft.add(bbutton1);

        Box bRight = Box.createVerticalBox();
        bRight.add(Box.createVerticalStrut(65));
        bRight.setPreferredSize(new Dimension(563, 880));
        bRight.setMinimumSize(new Dimension(563, 880));
        bRight.setMaximumSize(new Dimension(563, 880));
        JPanel pheader = new JPanel();
        pheader.setLayout(new BoxLayout(pheader, BoxLayout.Y_AXIS));
        pheader.setBackground(new Color(16, 16, 20));
        pheader.setBackground(new Color(40, 40, 44));

        JLabel titleDialog = new JLabel("Phiếu đặt dịch vụ");
        titleDialog.setForeground(Color.WHITE);
        titleDialog.setFont(new Font("Montserrat", Font.BOLD, 24));

        Box bheader = Box.createHorizontalBox();
        pheader.add(titleDialog);
        bheader.add(pheader);
        bRight.add(bheader);
        bRight.add(Box.createVerticalStrut(50));
        Box bTable2 = Box.createHorizontalBox();
        String[] colName2 = {"Tên dịch vụ", "Số lượng", "Thành tiền"};
        tableModel2 = new DefaultTableModel(colName2, 0);
        JScrollPane scroll2;
        bTable2.add(scroll2 = new JScrollPane(table2 = new JTable(tableModel2), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table2.setBackground(new Color(24, 24, 28));
        table2.setForeground(Color.WHITE);
        table2.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table2.setRowHeight(55);

        JTableHeader header2 = table2.getTableHeader();
        header2.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header2.setPreferredSize(new Dimension(header1.getPreferredSize().width, 55));
        header2.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer2 = new CustomCellRenderer();
        for (int i = 0; i < table2.getColumnCount(); i++) {
            TableColumn column = table2.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer2);
        }

        scroll2.setBounds(30, 380, 563, 700);
        scroll2.setBorder(null);
        scroll2.getViewport().setOpaque(false);
        scroll2.setViewportBorder(null);
        bRight.add(bTable2);
        bRight.add(Box.createVerticalStrut(300));

        Box bbutton2 = Box.createHorizontalBox();
        bbutton2.add(Box.createHorizontalStrut(200));
        bbutton2.add(btnXoa = new JButton("Xóa"));
        btnXoa.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnXoa.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(151, 69, 35, 100));
        btnXoa.setOpaque(false);
        btnXoa.setPreferredSize(new Dimension(150, 40));
        btnXoa.setMinimumSize(new Dimension(150, 40));
        btnXoa.setMaximumSize(new Dimension(150, 40));
        btnXoa.addActionListener(this);
        bbutton2.add(Box.createHorizontalStrut(20));
        bbutton2.add(btnXacNhan = new JButton("Xác nhận"));
        btnXacNhan.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setBackground(new Color(51, 70, 50, 100));
        btnXacNhan.setOpaque(false);
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        btnXacNhan.setMinimumSize(new Dimension(150, 40));
        btnXacNhan.setMaximumSize(new Dimension(150, 40));
        btnXacNhan.addActionListener(this);
        bRight.add(Box.createVerticalStrut(10));
        bRight.add(bbutton2);

        bdialog.add(Box.createHorizontalStrut(10));
        bdialog.add(bLeft);
        bdialog.add(Box.createHorizontalStrut(20));
        bdialog.add(bRight);
        dialog.add(bdialog, BorderLayout.CENTER);

        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.setVisible(true);

        loadDichVuData();
        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi đặt dịch vụ!");
            return;
        }

        loadDichVuDaDat(selectedMaHD, selectedMaPhong);
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
        tableModel1.setRowCount(0);
        Request<Void> request = new Request<>("GET_ALL_DICH_VU", null);
        try {
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
            Response<List<DichVuDTO>> response = SocketManager.receiveType(responseType);

            if (response != null && response.isSuccess()) {
                List<DichVuDTO> dsDichVu = response.getData();
                if (dsDichVu == null || dsDichVu.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Không có dữ liệu dịch vụ!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

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
                System.out.println("Loaded " + dsDichVu.size() + " services into table1");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể lấy dữ liệu dịch vụ từ server!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                loadDichVuData();
            }
        }
        table1.repaint();
        table1.revalidate();
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