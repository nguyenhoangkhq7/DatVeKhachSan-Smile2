package view.form;

import utils.custom_element.*;
import dao.KhachHang_DAO;
import dao.PhieuDatPhong_DAO;
import model.PhieuDatPhong;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDTrueTypeFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class LapHoaDon_FORM extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final DefaultTableModel tableModel3;
    private JTextField txttimkiem;
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnlaphd;
    private JLabel day;
    private DefaultTableModel tableModel1;
    private JTable table1;
    private JLabel lbhoten1;
    private JLabel lbsophong;
    private JLabel lbsophong1;
    private JLabel lbsdt;
    private JLabel lbsdt1;
    private JLabel lbcccd;
    private JLabel lbcccd1;
    private JLabel lbngaynhan;
    private JLabel lbngaynhan1;
    private JLabel lbngaytra;
    private JLabel lbngaytra1;
    private JLabel lbdiachi;
    private JLabel lbdiachi1;
    private JLabel lbmasothue;
    private JLabel lbmasothue1;
    private Font f;
    private Font f1;
    private Connection conn;
    private KhachHang_DAO khachHang_dao;
    private JButton btnXacNhan;
    private JLabel totalLabel;
    private JLabel totalLabel1;


    public LapHoaDon_FORM()  {
//        try {
//            ConnectDB.getInstance().connect();
//            this.conn = ConnectDB.getConnection();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        setBackground(new Color(16, 16, 20));
        Box b = Box.createVerticalBox();
        b.add(Box.createVerticalStrut(10));

        // Tim kiem
        JTextField searchField = new JTextField("Tìm kiếm");
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
        JLabel titleLabel = new JLabel("Danh sách khách hàng");
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
        String[] colName = {"Mã đặt phòng", "Loại phòng", "Tên phòng", "Phòng", "Trạng thái", "Tên khách", "Ngày đến", "Ngày đi", "Số đêm"};
        Object[][] data = loadDSKH();

        tableModel = new DefaultTableModel(data, colName);
        JScrollPane scroll;
        b2.add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table.setBackground(new Color(24, 24, 28));
        table.setForeground(Color.WHITE);
        table.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table.setRowHeight(60);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 55));
        header.setReorderingAllowed(false);

        scroll.setPreferredSize(new Dimension(1642, 300));
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);
        b.add(b2);
        b.add(Box.createVerticalStrut(50));

        // Bang 2
        JLabel titleLabel2 = new JLabel("Hóa đơn đã được lập");
        titleLabel2.setFont(FontManager.getManrope(Font.BOLD, 16));
        titleLabel2.setForeground(Color.white);

        RoundedPanel titlePanel2 = new RoundedPanel(10, 0, new Color(27, 112, 213));
        titlePanel2.setPreferredSize(new Dimension(1642, 50));
        titlePanel2.setMinimumSize(new Dimension(1642, 50));
        titlePanel2.setMaximumSize(new Dimension(1642, 50));
        titlePanel2.setOpaque(false);
        titlePanel2.setLayout(new BoxLayout(titlePanel2, BoxLayout.X_AXIS));

        titlePanel2.add(Box.createHorizontalStrut(15));
        titlePanel2.add(titleLabel2);
        titlePanel2.add(Box.createHorizontalGlue());
        b.add(titlePanel2);
        b.add(Box.createVerticalStrut(5));

        // Tạo bảng hóa đơn
        Box b3 = Box.createHorizontalBox();
        String[] colName3 = {"Mã hóa đơn", "Mã PDP", "Mã phòng", "Ngày lập", "Tên khách hàng", "Nhân viên lập", "Thành tiền", "Chi tiết"};
        Object[][] data3 = loadDataHD();


        tableModel3 = new DefaultTableModel(data3, colName3);
        JScrollPane scroll3;
        JTable table2 = new JTable(tableModel3);
        b3.add(scroll3 = new JScrollPane(table2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table2.setBackground(new Color(24, 24, 28));
        table2.setForeground(Color.WHITE);
        table2.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table2.setRowHeight(60);

        JTableHeader header3 = table2.getTableHeader();
        header3.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header3.setPreferredSize(new Dimension(header3.getPreferredSize().width, 55));
        header3.setReorderingAllowed(false);

        scroll3.setPreferredSize(new Dimension(1642, 300));
        scroll3.setBorder(null);
        scroll3.getViewport().setOpaque(false);
        scroll3.setViewportBorder(null);
        b.add(b3);
        b.add(Box.createVerticalStrut(80));
        // Custom renderer for the button

        // Tạo nút
        Box bbutton = Box.createHorizontalBox();
        bbutton.add(Box.createHorizontalGlue());
        bbutton.add(btnlaphd = new JButton("Lập hóa đơn"));
        btnlaphd.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnlaphd.setForeground(Color.WHITE);
        btnlaphd.setBackground(new Color(66, 99, 235));
        btnlaphd.setPreferredSize(new Dimension(200, 50));
        btnlaphd.setMinimumSize(new Dimension(200, 50));
        btnlaphd.setMaximumSize(new Dimension(200, 50));
        btnlaphd.addActionListener(this);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        buttonPanel.add(bbutton);
        b.add(buttonPanel);
        add(b, BorderLayout.CENTER);
    }

    //Hoa don
    public void openHoaDon() {
        // Tạo JDialog hiển thị hóa đơn
        JDialog dialog = new JDialog();
        dialog.setTitle("Hóa đơn");
        dialog.setSize(800, 850);
        dialog.setLayout(new BorderLayout());

        // Thiết lập màu nền cho JDialog
        dialog.getContentPane().setBackground(new Color(40, 40, 44));
        Box bdialog = Box.createVerticalBox();

        // Tạo JPanel cho phần header của hóa đơn

        JPanel pheader = new JPanel();
        pheader.setLayout(new BoxLayout(pheader, BoxLayout.Y_AXIS));
        pheader.setBackground(new Color(16, 16, 20));
        pheader.setBackground(new Color(40, 40, 44));
        // Thêm tiêu đề và ngày tháng vào JPanel
        JLabel titleDialog = new JLabel("   HÓA ĐƠN");
        titleDialog.setForeground(Color.WHITE);
        titleDialog.setFont(new Font("Montserrat", Font.BOLD, 32));

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
        String formattedDate = currentDate.format(formatter);
        JLabel day = new JLabel(formattedDate);
        day.setForeground(Color.WHITE);
        day.setFont(new Font("Montserrat", Font.PLAIN, 16));
        Box bheader = Box.createHorizontalBox();
        pheader.add(titleDialog);
        pheader.add(day);
        bheader.add(pheader);
        bdialog.add(bheader);
        bdialog.add(Box.createVerticalStrut(10));

        // Tạo JPanel cho thông tin khách hàng
        JPanel customerInfoPanel = new JPanel();
        customerInfoPanel.setForeground(Color.WHITE);
        customerInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        customerInfoPanel.setBackground(new Color(40, 40, 44));
        customerInfoPanel.setBorder(BorderFactory.createTitledBorder(""));
        customerInfoPanel.setPreferredSize(new Dimension(700, 200));
        customerInfoPanel.setMinimumSize(new Dimension(700, 200));
        customerInfoPanel.setMaximumSize(new Dimension(700, 200));

// Thêm các thông tin khách hàng
        JPanel bcustomerInfoPanel = new JPanel();
        bcustomerInfoPanel.setLayout(new BoxLayout(bcustomerInfoPanel, BoxLayout.Y_AXIS));
        bcustomerInfoPanel.setBackground(new Color(40, 40, 44));

//hoten
        Box bhoten = Box.createHorizontalBox();
        JLabel lbhoten;
        bhoten.add(lbhoten = new JLabel("Họ tên khách hàng:"));
        bhoten.add(lbhoten1 = new JLabel());
        bhoten.setAlignmentX(LEFT_ALIGNMENT);

        bhoten.add(Box.createHorizontalStrut(120));
        bhoten.add(lbsophong = new JLabel("Số phòng:"));
        bhoten.add(lbsophong1 = new JLabel(""));
        bcustomerInfoPanel.add(bhoten);
        bcustomerInfoPanel.add(Box.createVerticalStrut(15));

//sdt
        Box bsodt = Box.createHorizontalBox();
        bsodt.add(lbsdt = new JLabel("Số điện thoại:"));
        bsodt.add(lbsdt1 = new JLabel(""));
        bsodt.add(Box.createHorizontalStrut(195));
        bsodt.add(lbcccd = new JLabel("Số CCCD:"));
        bsodt.add(lbcccd1 = new JLabel(""));
        bsodt.setAlignmentX(LEFT_ALIGNMENT);
        bcustomerInfoPanel.add(bsodt);
        bcustomerInfoPanel.add(Box.createVerticalStrut(15));

//ngay
        Box bngay = Box.createHorizontalBox();
        bngay.add(lbngaynhan = new JLabel("Ngày nhân phòng:"));
        bngay.add(lbngaynhan1 = new JLabel(""));
        bngay.add(Box.createHorizontalStrut(85));
        bngay.add(lbngaytra = new JLabel("Ngày trả phòng:"));
        bngay.add(lbngaytra1 = new JLabel(""));
        bngay.setAlignmentX(LEFT_ALIGNMENT);
        bcustomerInfoPanel.add(bngay);
        bcustomerInfoPanel.add(Box.createVerticalStrut(15));

//diachi
        Box bdiachi = Box.createHorizontalBox();
        bdiachi.add(lbdiachi = new JLabel("Địa chỉ:"));
        bdiachi.add(lbdiachi1 = new JLabel(""));
        bdiachi.setAlignmentX(LEFT_ALIGNMENT);
        bcustomerInfoPanel.add(bdiachi);
        bcustomerInfoPanel.add(Box.createVerticalStrut(15));

//masothue
        Box bmasothue = Box.createHorizontalBox();
        bmasothue.add(lbmasothue = new JLabel("Mã số thuế:"));
        bmasothue.add(lbmasothue1 = new JLabel("1012345"));
        bmasothue.setAlignmentX(LEFT_ALIGNMENT);
        bcustomerInfoPanel.add(bmasothue);

        Font f = new Font("Montserrat", Font.BOLD, 16);
        Font f1 = new Font("Montserrat", Font.PLAIN, 16);

        lbhoten.setForeground(Color.WHITE);
        lbhoten.setFont(f);
        lbhoten1.setForeground(Color.WHITE);
        lbhoten1.setFont(f1);
        lbsophong1.setForeground(Color.WHITE);
        lbsophong1.setFont(f1);
        lbsophong.setForeground(Color.WHITE);
        lbsophong.setFont(f);
        lbsdt1.setForeground(Color.WHITE);
        lbsdt1.setFont(f1);
        lbsdt.setForeground(Color.WHITE);
        lbsdt.setFont(f);
        lbcccd1.setForeground(Color.WHITE);
        lbcccd1.setFont(f1);
        lbcccd.setForeground(Color.WHITE);
        lbcccd.setFont(f);
        lbngaynhan1.setForeground(Color.WHITE);
        lbngaynhan1.setFont(f1);
        lbngaynhan.setForeground(Color.WHITE);
        lbngaynhan.setFont(f);
        lbngaytra.setForeground(Color.WHITE);
        lbngaytra.setFont(f);
        lbngaytra1.setForeground(Color.WHITE);
        lbngaytra1.setFont(f1);
        lbdiachi.setForeground(Color.WHITE);
        lbdiachi.setFont(f);
        lbdiachi1.setForeground(Color.WHITE);
        lbdiachi1.setFont(f1);
        lbmasothue.setForeground(Color.WHITE);
        lbmasothue.setFont(f);
        lbmasothue1.setForeground(Color.WHITE);
        lbmasothue1.setFont(f1);

        customerInfoPanel.add(bcustomerInfoPanel);

        bdialog.add(customerInfoPanel);
        bdialog.add(Box.createVerticalStrut(10));
        // Tạo bảng hiển thị các dịch vụ
        String[] columnNames = {"STT", "Tên dịch vụ", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"};
        int row1 = table.getSelectedRow();
        String maPhong1 = (String) tableModel.getValueAt(row1, 3);
        Object[][] data = loadDV(maPhong1);

        tableModel1 = new DefaultTableModel(data, columnNames);
        JScrollPane scroll1;
        bdialog.add(scroll1 = new JScrollPane(table1 = new JTable(tableModel1), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

        table1.setRowHeight(55);
        table1.setBackground(new Color(40, 40, 44));
        table1.setForeground(Color.WHITE);
        table1.setFont(FontManager.getManrope(Font.PLAIN, 15));

        JTableHeader header1 = table1.getTableHeader();
        header1.setDefaultRenderer(new CustomHeaderRenderer(new Color(88, 84, 92), Color.white));
        header1.setPreferredSize(new Dimension(header1.getPreferredSize().width, 55));
        header1.setFont(FontManager.getManrope(Font.PLAIN, 15));
        header1.setReorderingAllowed(false);

        scroll1.setPreferredSize(new Dimension(700, 400));
        scroll1.setMinimumSize(new Dimension(700, 400));
        scroll1.setMaximumSize(new Dimension(700, 400));
        scroll1.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
        scroll1.getViewport().setOpaque(false);
        scroll1.setViewportBorder(null);
        // Tổng tiền
        Box btotal = Box.createHorizontalBox();
        btotal.add(totalLabel = new JLabel("Tổng tiền: ", SwingConstants.RIGHT));
        btotal.add(totalLabel1 = new JLabel(""));
        totalLabel.setForeground(Color.WHITE);
        totalLabel1.setForeground(Color.WHITE);
        totalLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));
        // totalLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        btotal.add(Box.createHorizontalStrut(500));
        btotal.add(totalLabel);
        btotal.add(totalLabel1);
        bdialog.add(btotal);
        bdialog.add(Box.createVerticalStrut(90));
        // Thêm nút Xác nhận và Xuất hóa đơn
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(40, 40, 44));

        btnXacNhan = new JButton("Xác nhận");
        btnXacNhan.setPreferredSize(new Dimension(150, 40));
        btnXacNhan.setBackground(new Color(74, 74, 66));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(f1);
        btnXacNhan.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có muốn lập hóa đơn", "Thành công", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                int row = table.getSelectedRow();
                int stt = 21;
                if (row >= 0) {
                    Random random = new Random();
                    String maHD = String.format("%08d", random.nextInt(100000000));
                    String maPDP = (String) table.getValueAt(row, 0);
                    String maPhong = (String) table.getValueAt(row, 3);
                    String tenKhachHang = (String) table.getValueAt(row, 5);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    String ngayLap = dateFormat.format(new Date());
                    String tenNhanVien = "Cao Thành Đông";
                    String thanhTien = "905000";
                    String chiTiet = "Xem";
                    themDuLieuVaoTableModel3(stt++, maHD, maPDP, maPhong, ngayLap, tenKhachHang, tenNhanVien, thanhTien, chiTiet);
                    tableModel3.fireTableDataChanged();
                    JOptionPane.showMessageDialog(dialog, "Lập hóa đơn thành công");
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một hàng trong bảng");
                }
            } else if (confirm == JOptionPane.NO_OPTION) {
                dialog.dispose();
            }
        });

        JButton btnXuatHD = new JButton("Xuất hóa đơn");
        btnXuatHD.setPreferredSize(new Dimension(150, 40));
        btnXuatHD.setBackground(new Color(51, 70, 50));
        btnXuatHD.setForeground(Color.WHITE);
        btnXuatHD.setFont(f1);
        btnXuatHD.addActionListener(e -> {
            exportToPDF(table);
        });

        buttonPanel.add(btnXacNhan);
        buttonPanel.add(btnXuatHD);
        bdialog.add(buttonPanel);

        // Thêm Box chính vào JDialog
        dialog.add(bdialog, BorderLayout.CENTER);

        // Đặt JDialog xuất hiện ở giữa màn hình
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.setVisible(true);
    }

    private static JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    public void themDuLieuVaoTableModel3(int i, String maHD, String maPDP, String maPhong, String ngayLap, String tenKhachHang, String tenNhanVien, String thanhTien, String chiTiet) {
        Object[] row = new Object[]{maHD, maPDP, maPhong, ngayLap, tenKhachHang, tenNhanVien, thanhTien, chiTiet};
        tableModel3.addRow(row);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
//        openHoaDon();
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow == -1) {
//            JOptionPane.showMessageDialog(null, "Vui lòng chọn một hàng trong bảng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//            return;
//        }
//        try {
//            String maPDP = (String) tableModel.getValueAt(selectedRow, 0);
//            PhieuDatPhong pdp = new PhieuDatPhong_DAO().getPDPTheoMa(maPDP);
//
//            if (pdp == null) {
//                JOptionPane.showMessageDialog(null, "Không tìm thấy phiếu đặt phòng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            // Hiển thị thông tin khách hàng
//            lbhoten1.setText(pdp.getKhachHang() != null ? pdp.getKhachHang().getHoTen() : "Chưa xác định");
//            lbcccd1.setText(pdp.getKhachHang() != null ? pdp.getKhachHang().getcCCD() : "Chưa xác định");
//            lbdiachi1.setText(pdp.getKhachHang() != null ? pdp.getKhachHang().getDiaChi() : "Chưa xác định");
//            lbsdt1.setText(pdp.getKhachHang() != null ? pdp.getKhachHang().getSdt() : "Chưa xác định");
//            lbsophong1.setText(pdp.getPhong() != null ? pdp.getPhong().getMaPhong() : "Chưa xác định");
//            lbdiachi1.setText(pdp.getKhachHang() != null ? pdp.getKhachHang().getDiaChi() : "Chưa xác định");
//
//            // Hiển thị thông tin ngày
//            lbngaynhan1.setText(pdp.getNgayDen() != null ? pdp.getNgayDen().toString() : "Chưa xác định");
//            lbngaytra1.setText(pdp.getNgayDi() != null ? pdp.getNgayDi().toString() : "Chưa xác định");
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi lấy thông tin phiếu đặt phòng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
//            ex.printStackTrace();
//        }
    }


    //load dữ liệu
    public Object[][] loadDataHD()  {
//        String sql = "SELECT hd.maHD, pdp.maPDP, p.maPhong, hd.ngayLapHD, kh.hoTen AS KhachHang, nv.hoTen AS NhanVien " +
//                "FROM HoaDon hd " +
//                "JOIN KhachHang kh ON hd.maKH = kh.maKH " +
//                "JOIN NhanVien nv ON hd.maNV = nv.maNV " +
//                "JOIN PhieuDatPhong pdp ON pdp.maKH = hd.maKH " +
//                "JOIN Phong p ON p.maPhong = pdp.maPhong";
//        List<Object[]> dataList = new ArrayList<>();
//        ConnectDB.getInstance().connect();
//        this.conn = ConnectDB.getConnection();
//        try (
//                Statement stmt = conn.createStatement();
//                ResultSet rs = stmt.executeQuery(sql)) {
//            int stt = 1;
//            while (rs.next()) {
//                String maHoaDon = rs.getString("maHD");
//                String maPDP = rs.getString("maPDP");
//                String maPhong = rs.getString("maPhong");
//                String ngayLap = rs.getString("ngayLapHD");
//                String tenKhachHang = rs.getString("KhachHang");
//                String tenNhanVien = rs.getString("NhanVien");
//                // Add the actual data to the list
//                dataList.add(new Object[]{maHoaDon, maPDP, maPhong, ngayLap, tenKhachHang, tenNhanVien, 400000, "Xem"});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dataList.toArray(new Object[0][]);
        return null;
    }

    public Object[][] loadDV(String maPhong) {
//        String sql = "SELECT dv.tenDV, dv.donViTinh, ct.soLuongDV, ct.soLuongDV * dv.giaDV as donGia " +
//                "FROM DichVu dv " +
//                "JOIN ChiTietHoaDon ct ON dv.maDV = ct.maDV " +
//                "WHERE ct.maPhong = ? AND ct.soLuongDV > 0"; // Thêm điều kiện soLuongDV > 0
//
//        List<Object[]> dataList = new ArrayList<>();
//        ConnectDB.getInstance().connect();
//        this.conn = ConnectDB.getConnection();
//        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, maPhong);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                int stt = 1;
//                float tongTien = 0;
//                while (rs.next()) {
//                    String tenDV = rs.getString(1);
//                    String donViTinh = rs.getString(2);
//                    String soLuong = rs.getString(3);
//                    String donGia = rs.getString(4);
//                    float thanhTien = Float.parseFloat(soLuong) * Float.parseFloat(donGia);
//                    tongTien += thanhTien;
//                    dataList.add(new Object[]{stt++, tenDV, donViTinh, soLuong, donGia, thanhTien});
//                }
////            totalLabel1.setText(String.format("%.0f", tongTien)); // Nếu cần, bạn có thể hiển thị tổng tiền
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return dataList.toArray(new Object[0][]);
        return null;
    }

    //load DSKH
    public Object[][] loadDSKH() {
//        String sql = "SELECT pdp.maPDP, p.loaiPhong, p.tenPhong, p.maPhong, p.trangThai, kh.hoTen, pdp.ngayDen, pdp.ngayDi " +
//                "FROM PhieuDatPhong pdp " +
//                "JOIN KhachHang kh ON kh.maKH = pdp.maKH " +
//                "JOIN Phong p ON p.maPhong = pdp.maPhong ";
//        List<Object[]> dataList = new ArrayList<>();
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try (Connection con = ConnectDB.getConnection();
//             Statement stmt = con.createStatement();
//             ResultSet rs = stmt.executeQuery(sql)) {
//            while (rs.next()) {
//                String maDatPhong = rs.getString("maPDP");
//                String loaiPhong = rs.getString("loaiPhong");
//                String tenPhong = rs.getString("tenPhong");
//                String maPhong = rs.getString("maPhong");
//                String trangThai = rs.getString("trangThai");
//                String trangThaiHienThi = "1".equals(trangThai) ? "Đang sử dụng" : "không sử dụng";
//                String tenKhachHang = rs.getString("hoTen");
//                Date ngayDen = rs.getDate("ngayDen");
//                Date ngayDi = rs.getDate("ngayDi");
//                String ngayDenStr = ngayDen != null ? dateFormat.format(ngayDen) : "";
//                String ngayDiStr = ngayDi != null ? dateFormat.format(ngayDi) : "";
//                int soDem = 0;
//                if (ngayDen != null && ngayDi != null) {
//                    long diffInMillis = ngayDi.getTime() - ngayDen.getTime();
//                    soDem = (int) (diffInMillis / (1000 * 60 * 60 * 24));
//                }
//                dataList.add(new Object[]{maDatPhong, loaiPhong, tenPhong, maPhong, trangThaiHienThi, tenKhachHang, ngayDenStr, ngayDiStr, soDem});
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return dataList.toArray(new Object[0][]);
        return null;
    }


    public void exportToPDF(JTable table) {
        String filePath = "C:/Users/TRAN HAU/Desktop/hoa_don_" + System.currentTimeMillis() + ".pdf"; // Đường dẫn tệp PDF đầu ra

        try (PDDocument document = new PDDocument()) {
            // Tạo trang PDF mới
            PDPage page = new PDPage();
            document.addPage(page);

            // Bắt đầu vẽ nội dung vào trang
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Tạo đối tượng File cho font chữ
                File fontFile = new File("C:/Windows/Fonts/times.ttf");

                // Kiểm tra sự tồn tại của tệp font
                if (!fontFile.exists()) {
                    throw new IOException("Font file không tồn tại: " + fontFile.getAbsolutePath());
                }

                // Tải phông chữ từ File sử dụng PDType0Font (hỗ trợ Unicode)
                PDType0Font font = PDType0Font.load(document, fontFile);

                // Cài đặt font và kích thước chữ
                contentStream.setFont(font, 12);
                contentStream.setLeading(14f);
                contentStream.beginText(); // Mở văn bản
                contentStream.newLineAtOffset(50, 750); // Vị trí bắt đầu viết nội dung

                // Tiêu đề hóa đơn
                contentStream.showText("HÓA ĐƠN");
                contentStream.newLine();

                // Thêm ngày tháng hiện tại
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
                String formattedDate = currentDate.format(formatter);
                contentStream.showText("Ngày lập: " + formattedDate);
                contentStream.newLine();

                // Thông tin khách hàng
                contentStream.showText("Họ tên khách hàng: " + lbhoten1.getText());
                contentStream.newLine();
                contentStream.showText("Số điện thoại: " + lbsdt1.getText());
                contentStream.newLine();
                contentStream.showText("Số CCCD: " + lbcccd1.getText());
                contentStream.newLine();
                contentStream.showText("Địa chỉ: " + lbdiachi1.getText());
                contentStream.newLine();
                contentStream.showText("Mã số thuế: " + lbmasothue1.getText());
                contentStream.newLine();

                // Thông tin phòng
                contentStream.showText("Mã phòng: " + lbsophong1.getText());
                contentStream.newLine();
                contentStream.showText("Ngày nhận phòng: " + lbngaynhan1.getText());
                contentStream.newLine();
                contentStream.showText("Ngày trả phòng: " + lbngaytra1.getText());
                contentStream.newLine();

                // Đóng phần văn bản của thông tin khách hàng và phòng
                contentStream.endText();

                // Mở lại văn bản để in thông tin từ bảng
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 500); // Vị trí bắt đầu xuất thông tin bảng

                contentStream.showText("Danh sách dịch vụ:");
                contentStream.newLine();

                // Cấu trúc cột cho thông tin bảng
                String[] columnNames = {"STT", "Tên dịch vụ", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"};

                // In tên cột
                contentStream.setFont(font, 10);
                float yPosition = 500; // Vị trí bắt đầu cho bảng

                // Căn chỉnh vị trí các cột
                for (int i = 0; i < columnNames.length; i++) {
                    contentStream.newLineAtOffset(50 + i * 100, yPosition);  // Điều chỉnh vị trí cho các cột
                }

                yPosition -= 20; // Khoảng cách giữa tên cột và dữ liệu

                // Lấy dữ liệu từ bảng và xuất vào PDF theo cột
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        Object cellValue = table.getValueAt(i, j);
                        contentStream.newLineAtOffset(50 + j * 100, yPosition);  // Điều chỉnh vị trí cho từng cột
                    }
                    yPosition -= 20; // Khoảng cách giữa các hàng
                }

                contentStream.endText(); // Đóng phần văn bản của bảng dịch vụ  
            }

            // Lưu tệp PDF vào đường dẫn
            document.save(filePath);

            // Hiển thị thông báo thành công cho người dùng
            JOptionPane.showMessageDialog(null, "Hóa đơn đã được xuất ra file PDF: " + filePath, "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi tải font hoặc lưu file PDF! " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Xuất file thất bại! " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }




}





