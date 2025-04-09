package view.form;

import connectDB.ConnectDB;
import customElements.CustomCellRenderer;
import customElements.CustomHeaderRenderer;
import customElements.FontManager;
import customElements.RoundedPanel;
import dao.DichVu_DAO;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class DatDichVu_FORM extends  JPanel implements ActionListener, MouseListener {
    private  JButton btnDatDV;
    private JTable table;
    private  DefaultTableModel tableModel;
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
    private String selectedMaHD; // Lưu mã hóa đơn
    private String selectedMaPhong;
    private String selectMaPDP;// Lưu mã phòng
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Nếu nhấn Enter
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

        tableModel = new DefaultTableModel( colName,0);
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
        // Tạo JDialog hiển thị hóa đơn
        JDialog dialog = new JDialog();
        dialog.setSize(1564, 880);
        dialog.setLayout(new BorderLayout());
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        f1 = new Font("Montserrat", Font.PLAIN, 16);
        dialog.getContentPane().setBackground(new Color(31,31,32,255));

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
        closePanel.setOpaque(false); // Đặt trong suốt
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Nếu nhấn Enter
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

        // Tieu de
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

        // Tạo bang
        bLeft.add(Box.createVerticalStrut(10));
        Box bTable1 = Box.createHorizontalBox();
        String[] colName1 = {"Mã dịch vụ", "Tên dịch vụ", "Giá dịch vụ", "Đơn vị tính", "Tồn kho"};

        tableModel1 = new DefaultTableModel(colName1,0);
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

        // Tạo nut
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
        String[] colName2 = {"Tên dịch vụ","Số lượng", "Thành tiền"};
        tableModel2 = new DefaultTableModel( colName2,0);
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

        // Tạo nut
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
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            selectedMaPhong = tableModel.getValueAt(selectedRow, 3).toString(); // Lấy mã phòng
             selectMaPDP = tableModel.getValueAt(selectedRow, 0).toString();
            // Truy vấn SQL để lấy mã hóa đơn (maHD) từ mã phòng
            Connection connection = ConnectDB.getConnection();
            if (connection != null) {
                try {
                    String query = "SELECT hd.maHD\n" +
                            "from ChiTietHoaDon ct join Phong p on ct.maPhong=p.maPhong join PhieuDatPhong pdp on p.maPhong=pdp.maPhong join HoaDon hd on ct.maHD=hd.maHD\n" +
                            "where ct.maPhong =? and maPDP=?";
                    var preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, selectedMaPhong);
                    preparedStatement.setString(2, selectMaPDP);

                    var resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        selectedMaHD = resultSet.getString("maHD");
                    } else {
                        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cho phòng đã chọn!");
                        selectedMaHD = null;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn mã hóa đơn!");
                }
            }


            JOptionPane.showMessageDialog(this,
                    "Đã chọn:\nMã phòng: " + selectedMaPhong + "\nMã phiếu đặt phòng:"+selectMaPDP );
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

    private void timKiem(String keyword) {
        table.clearSelection();

        int foundRow = -1;

        // Tìm dòng chứa từ khóa đầu tiên
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean match = false;

            // Kiểm tra từ khóa trong các cột
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

        int foundRow = -1;

        for (int i = 0; i < tableModel1.getRowCount(); i++) {
            boolean match = false;

            for (int j = 0; j < tableModel1.getColumnCount(); j++) {
                Object cellValue = tableModel1.getValueAt(i, j);
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
            moveRowToTop1(foundRow);

            table1.addRowSelectionInterval(0, 0);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!");
        }
    }

    private void moveRowToTop1(int rowIndex) {
        Object[] rowData = new Object[tableModel1.getColumnCount()];
        for (int i = 0; i < tableModel1.getColumnCount(); i++) {
            rowData[i] = tableModel1.getValueAt(rowIndex, i);
        }
        tableModel1.removeRow(rowIndex);
        tableModel1.insertRow(0, rowData);
    }

    private void loadDichVuData() {
        tableModel1.setRowCount(0);
        Connection connection = ConnectDB.getConnection();
        if (connection != null) {
            try {
                String query = "SELECT * FROM DichVu WHERE trangThai = 1";
                var preparedStatement = connection.prepareStatement(query);
                var resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    String maDV = resultSet.getString("maDV");
                    String tenDV = resultSet.getString("tenDV");
                    double giaDV = resultSet.getDouble("giaDV");
                    String donViTinh = resultSet.getString("donViTinh");
                    int soLuongTon = resultSet.getInt("soLuongTon");

                    tableModel1.addRow(new Object[]{maDV, tenDV, giaDV, donViTinh, soLuongTon});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu dịch vụ!");
            }
        }
    }



    private void loadPhongData() {
        Connection connection = ConnectDB.getConnection();
        if (connection != null) {
            try {
                String query = """
            SELECT PDP.maPDP, P.loaiPhong, P.tenPhong, P.maPhong, PDP.tinhTrangPDP, KH.hoTen, 
                   PDP.ngayDen, PDP.ngayDi
            FROM PhieuDatPhong PDP
            JOIN Phong P ON PDP.maPhong = P.maPhong
            JOIN KhachHang KH ON PDP.maKH = KH.maKH
            WHERE P.trangThai = 1 AND PDP.tinhTrangPDP != 2
            """;
                var preparedStatement = connection.prepareStatement(query);
                var resultSet = preparedStatement.executeQuery();

                tableModel.setRowCount(0); // Xóa dữ liệu cũ
                while (resultSet.next()) {
                    // Lấy dữ liệu từ kết quả truy vấn
                    String maPDP = resultSet.getString("maPDP");
                    String loaiPhong = resultSet.getString("loaiPhong");
                    String tenPhong = resultSet.getString("tenPhong");
                    String maPhong = resultSet.getString("maPhong");
                    int tinhTrang = resultSet.getInt("tinhTrangPDP");  // Lấy tình trạng dưới dạng số
                    String tenKhach = resultSet.getString("hoTen");
                    String ngayDen = resultSet.getString("ngayDen");
                    String ngayDi = resultSet.getString("ngayDi");

                    // Chuyển đổi tình trạng thành chuỗi tương ứng
                    String tinhTrangString;
                    switch (tinhTrang) {
                        case 0:
                            tinhTrangString = "Chờ nhận phòng";
                            break;
                        case 1:
                            tinhTrangString = "Đã nhận phòng";
                            break;
                        default:
                            tinhTrangString = "Không xác định";
                            break;
                    }

                    // Thêm dữ liệu vào tableModel
                    tableModel.addRow(new Object[]{maPDP, loaiPhong, tenPhong, maPhong, tinhTrangString, tenKhach, ngayDen, ngayDi});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu phòng!");
            }
        }
    }


    private void loadDichVuDaDat(String maHD, String maPhong) {
        try {
            Connection connection = ConnectDB.getConnection();
            String query = """
            SELECT tenDV, soLuongDV, giaDV 
            FROM ChiTietHoaDon
            INNER JOIN DichVu ON ChiTietHoaDon.maDV = DichVu.maDV
            WHERE maHD = ? AND maPhong = ? AND soLuongDV > 0
        """;
            var preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, maHD);
            preparedStatement.setString(2, maPhong);
            var resultSet = preparedStatement.executeQuery();

            // Xóa tất cả các hàng trong table trước khi load lại
            tableModel2.setRowCount(0);

            // Định dạng số
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");

            // Thêm các dòng mới từ cơ sở dữ liệu
            while (resultSet.next()) {
                String tenDV = resultSet.getString("tenDV");
                int soLuongDV = resultSet.getInt("soLuongDV");
                double giaDV = resultSet.getDouble("giaDV"); // Lấy giá dịch vụ từ cơ sở dữ liệu
                double thanhTien = soLuongDV * giaDV;       // Tính toán thành tiền

                // Định dạng thành tiền
                String thanhTienFormatted = decimalFormat.format(thanhTien);

                // Thêm hàng vào bảng với giá trị đã định dạng
                tableModel2.addRow(new Object[] { tenDV, soLuongDV, thanhTienFormatted });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dịch vụ đã đặt! Lỗi SQL: " + ex.getMessage());
        }
    }



    private void lamMoiDichVu() {
                tableModel2.setRowCount(0); // Xóa tất cả các dòng trong bảng phiếu đặt
            }

    private void xacNhanDichVu() {
        if (tableModel2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có dịch vụ nào để xác nhận!");
            return;
        }

        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi xác nhận dịch vụ!");
            return;
        }

        Connection connection = ConnectDB.getConnection();
        if (connection == null) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu!");
            return;
        }

        try {
            // Truy vấn thông tin bổ sung từ bảng ChiTietHoaDon
            String fetchQuery = """
            SELECT thoiGianThuePhong, soLuongGiuongPhu, soLuongPhong 
            FROM ChiTietHoaDon 
            WHERE maHD = ? AND maPhong = ?
        """;
            var fetchStatement = connection.prepareStatement(fetchQuery);
            fetchStatement.setString(1, selectedMaHD);
            fetchStatement.setString(2, selectedMaPhong);

            var resultSet = fetchStatement.executeQuery();
            if (!resultSet.next()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin hóa đơn và phòng phù hợp!");
                return;
            }

            // Lấy giá trị từ kết quả truy vấn
            java.sql.Date thoiGianThuePhong = resultSet.getDate("thoiGianThuePhong");
            int soLuongGiuongPhu = resultSet.getInt("soLuongGiuongPhu");
            int soLuongPhong = resultSet.getInt("soLuongPhong");

            // Chuẩn bị câu lệnh kiểm tra và thêm mới
            String getMaDVQuery = "SELECT maDV FROM DichVu WHERE tenDV = ?";
            String checkQuery = """
            SELECT COUNT(*) FROM ChiTietHoaDon 
            WHERE maHD = ? AND maPhong = ? AND maDV = ?
        """;
            String insertQuery = """
            INSERT INTO ChiTietHoaDon (maHD, maPhong, maDV, thoiGianThuePhong, soLuongGiuongPhu, soLuongDV, soLuongPhong) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
            String updateSoLuongTonQuery = "UPDATE DichVu SET soLuongTon = soLuongTon - ? WHERE maDV = ?";

            var getMaDVStatement = connection.prepareStatement(getMaDVQuery);
            var checkStatement = connection.prepareStatement(checkQuery);
            var insertStatement = connection.prepareStatement(insertQuery);
            var updateStatement = connection.prepareStatement(updateSoLuongTonQuery);

            // Lặp qua từng hàng trong table2
            for (int i = 0; i < tableModel2.getRowCount(); i++) {
                String tenDV = tableModel2.getValueAt(i, 0).toString(); // Lấy tên dịch vụ
                int soLuongDV = Integer.parseInt(tableModel2.getValueAt(i, 1).toString()); // Lấy số lượng dịch vụ

                // Truy vấn mã dịch vụ (maDV) từ tên dịch vụ
                getMaDVStatement.setString(1, tenDV);
                var maDVResult = getMaDVStatement.executeQuery();
                if (!maDVResult.next()) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy mã dịch vụ cho: " + tenDV);
                    return; // Dừng nếu không tìm thấy mã dịch vụ
                }
                String maDV = maDVResult.getString("maDV"); // Lấy mã dịch vụ

                // Kiểm tra bản ghi đã tồn tại
                checkStatement.setString(1, selectedMaHD);
                checkStatement.setString(2, selectedMaPhong);
                checkStatement.setString(3, maDV);

                var checkResult = checkStatement.executeQuery();
                checkResult.next();
                int count = checkResult.getInt(1);

                if (count == 0) {
                    // Thêm mới bản ghi nếu chưa tồn tại
                    insertStatement.setString(1, selectedMaHD);
                    insertStatement.setString(2, selectedMaPhong);
                    insertStatement.setString(3, maDV);
                    insertStatement.setDate(4, thoiGianThuePhong);
                    insertStatement.setInt(5, soLuongGiuongPhu);
                    insertStatement.setInt(6, soLuongDV);
                    insertStatement.setInt(7, soLuongPhong);

                    insertStatement.addBatch(); // Thêm vào batch để xử lý hàng loạt
                }

                // Giảm số lượng tồn kho trong cơ sở dữ liệu
                updateStatement.setInt(1, soLuongDV);
                updateStatement.setString(2, maDV);
                updateStatement.addBatch(); // Thêm vào batch để xử lý hàng loạt
            }

            // Thực thi batch
            insertStatement.executeBatch();
            updateStatement.executeBatch();
            JOptionPane.showMessageDialog(this, "Đặt dịch vụ thành công!");
            lamMoiDichVu(); // Làm mới bảng sau khi xác nhận

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi xác nhận dịch vụ! Lỗi SQL: " + ex.getMessage());
        }
    }



    private void themDichVu() {
        int selectedRow = table1.getSelectedRow(); // Lấy dòng được chọn từ bảng dịch vụ
        if (selectedRow != -1) {
            // Lấy dữ liệu dịch vụ
            String tenDichVu = tableModel1.getValueAt(selectedRow, 1).toString();
            int soLuongTon = Integer.parseInt(tableModel1.getValueAt(selectedRow, 4).toString()); // Lấy số lượng tồn
            if (soLuongTon <= 0) {
                JOptionPane.showMessageDialog(this, "Dịch vụ này đã hết hàng!");
                return;
            }

            int soLuong = 1; // Mặc định số lượng thêm là 1

            // Định dạng Locale để xử lý số tiền (vi-VN sử dụng ',' làm dấu phân cách thập phân)
            NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
            double gia = 0.0;
            try {
                gia = format.parse(tableModel1.getValueAt(selectedRow, 2).toString()).doubleValue();
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(this, "Lỗi chuyển đổi giá trị số tiền!");
                return;
            }

            double thanhTien = soLuong * gia;

            // Sử dụng DecimalFormat để định dạng thành tiền
            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
            String thanhTienFormatted = decimalFormat.format(thanhTien);

            // Kiểm tra dịch vụ đã tồn tại trong bảng phiếu đặt
            boolean isExist = false;
            for (int i = 0; i < tableModel2.getRowCount(); i++) {
                if (tableModel2.getValueAt(i, 0).toString().equals(tenDichVu)) {
                    int currentSoLuong = Integer.parseInt(tableModel2.getValueAt(i, 1).toString());
                    int newSoLuong = currentSoLuong + 1;
                    double newThanhTien = newSoLuong * gia;

                    // Cập nhật số lượng và thành tiền đã định dạng
                    tableModel2.setValueAt(newSoLuong, i, 1); // Tăng số lượng
                    tableModel2.setValueAt(decimalFormat.format(newThanhTien), i, 2); // Cập nhật thành tiền
                    isExist = true;
                    break;
                }
            }

            if (!isExist) {
                // Thêm dịch vụ mới vào bảng phiếu đặt với thành tiền đã định dạng
                tableModel2.addRow(new Object[]{tenDichVu, soLuong, thanhTienFormatted});
            }

            // Giảm số lượng tồn trong table1
            tableModel1.setValueAt(soLuongTon - 1, selectedRow, 4);
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dịch vụ!");
        }
    }


    private void xoaDichVu() {
        int selectedRow = table2.getSelectedRow(); // Lấy dòng được chọn
        if (selectedRow != -1) {
            // Lấy thông tin dịch vụ từ table2
            String tenDichVu = tableModel2.getValueAt(selectedRow, 0).toString();
            int soLuongXoa = Integer.parseInt(tableModel2.getValueAt(selectedRow, 1).toString());

            // Xóa dòng khỏi table2
            tableModel2.removeRow(selectedRow);

            // Tìm dòng tương ứng trong table1 để tăng số lượng tồn
            for (int i = 0; i < tableModel1.getRowCount(); i++) {
                if (tableModel1.getValueAt(i, 1).toString().equals(tenDichVu)) {
                    int soLuongTon = Integer.parseInt(tableModel1.getValueAt(i, 4).toString());
                    tableModel1.setValueAt(soLuongTon + soLuongXoa, i, 4); // Cập nhật số lượng tồn
                    break;
                }
            }
        }
    }



}
