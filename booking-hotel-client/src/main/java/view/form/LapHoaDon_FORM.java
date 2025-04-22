package view.form;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import dto.*;
import model.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import socket.SocketManager;
import utils.custom_element.*;
import dao.KhachHang_DAO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import javax.imageio.ImageIO;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;


public class LapHoaDon_FORM extends JPanel implements ActionListener {
    private static final long serialVersionUID = 1L;
    private final DefaultTableModel tableModel3;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


    private JTextField txttimkiem;
    private List<HoaDonDTO> dsHoaDon = new ArrayList<>();

    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter1;
    private TableRowSorter<DefaultTableModel> sorter2;
    private JTable table;
    private JTable table2;
    private JButton btnlaphd;
    private JLabel day;
    private JTextField searchField;
    private DefaultTableModel tableModel1;
    private JTable table1;
    private JLabel lbhoten;
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
    private double giaPhong;

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
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateFilter();
            }

            private void updateFilter() {
                String keyword = searchField.getText().trim();

                // Nếu ô tìm kiếm rỗng hoặc chứa placeholder
                if (keyword.isEmpty() || keyword.equals("Tìm kiếm")) {
                    sorter1.setRowFilter(null);
                    sorter2.setRowFilter(null);
                } else {
                    // Tìm kiếm không phân biệt hoa thường
                    RowFilter<DefaultTableModel, Object> rf = RowFilter.regexFilter("(?i)" + Pattern.quote(keyword));
                    sorter1.setRowFilter(rf);
                    sorter2.setRowFilter(rf);
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
        String[] colName = {"Mã đặt phòng", "Loại phòng", "Tên phòng", "Phòng", "Trạng thái", "Tên khách", "Ngày đến", "Ngày đi", "Số đêm","Số điện thoại", "CCCD","Mã khách hàng", "Mã nhân viên","Tiền Phòng"};
        tableModel = new DefaultTableModel(colName, 0); // Khởi tạo tableModel3
        JScrollPane scroll;

        b2.add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        table.setBackground(new Color(24, 24, 28));
        table.setForeground(Color.WHITE);
        table.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table.setRowHeight(55);
        table.setSelectionBackground(new Color(66, 99, 235));sorter1 = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter1);

        // Lấy TableColumnModel
        TableColumnModel columnModel = table.getColumnModel();

// Ẩn cột "Số điện thoại" (cột chỉ số 9)
        columnModel.getColumn(9).setMinWidth(0);
        columnModel.getColumn(9).setMaxWidth(0);
        columnModel.getColumn(9).setWidth(0);

// Ẩn cột "CCCD" (cột chỉ số 10)
        columnModel.getColumn(10).setMinWidth(0);
        columnModel.getColumn(10).setMaxWidth(0);
        columnModel.getColumn(10).setWidth(0);
        // Ẩn cột "CCCD" (cột chỉ số 10)
        columnModel.getColumn(11).setMinWidth(0);
        columnModel.getColumn(11).setMaxWidth(0);
        columnModel.getColumn(11).setWidth(0);
        // Ẩn cột "CCCD" (cột chỉ số 10)
        columnModel.getColumn(12).setMinWidth(0);
        columnModel.getColumn(12).setMaxWidth(0);
        columnModel.getColumn(12).setWidth(0);

        // Ẩn cột "CCCD" (cột chỉ số 10)
        columnModel.getColumn(13).setMinWidth(0);
        columnModel.getColumn(13).setMaxWidth(0);
        columnModel.getColumn(13).setWidth(0);


        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 55));
        header.setReorderingAllowed(false);

        CustomCellRenderer cellRenderer = new CustomCellRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setCellRenderer(cellRenderer);
        }

        scroll.setPreferredSize(new Dimension(1642, 300));
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);
        b.add(b2);
        b.add(Box.createVerticalStrut(50));


        loadPhongData();

// Gọi repaint và revalidate sau khi tải dữ liệu xong
        table.repaint();
        table.revalidate();

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
        String[] colName3 = {"Mã hóa đơn", "Mã Khách Hàng", "Mã Nhân Viên", "Mã PDP", "Ngày Lập", "Ngày Nhận", "Ngày Trả", "Số Lượng"};



        // Khởi tạo tableModel3 trước khi tạo JTable
        tableModel3 = new DefaultTableModel(null, colName3);  // Khởi tạo tableModel3

        // Tạo JTable với tableModel3
        table2 = new JTable(tableModel3);
         sorter2 = new TableRowSorter<>(tableModel3);
        table2.setRowSorter(sorter2);
        sorter2.setRowFilter(null); // Xóa bộ lọc sau khi tải dữ liệu


        // Thêm ListSelectionListener để theo dõi sự kiện chọn dòng
        // Tạo sự kiện khi chọn dòng trong bảng
        table2.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table2.getSelectedRow();  // Lấy chỉ số dòng được chọn
                    if (selectedRow != -1) {
                        // Dòng đã được chọn
                        System.out.println("Row " + selectedRow + " is selected.");

                        // Bật nút "Lập Hóa Đơn" khi có dòng được chọn
                        btnlaphd.setEnabled(true); // Giả sử nút ban đầu bị vô hiệu hóa
                    } else {
                        // Không có dòng nào được chọn
                        System.out.println("No row selected.");
                        btnlaphd.setEnabled(false); // Tắt nút khi không có dòng được chọn
                    }
                }
            }
        });



// Tạo JScrollPane cho table2
        JScrollPane scroll3 = new JScrollPane(table2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        b3.add(scroll3);

// Cài đặt các thuộc tính cho bảng
        table2.setBackground(new Color(24, 24, 28));
        table2.setForeground(Color.WHITE);
        table2.setFont(FontManager.getManrope(Font.PLAIN, 16));
        table2.setRowHeight(60);
        // Cho phép chọn một dòng tại một thời điểm
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

// Cho phép chọn một dòng tại một thời điểm
        table2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


// Thiết lập Header
        JTableHeader header3 = table2.getTableHeader();
        header3.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
        header3.setPreferredSize(new Dimension(header3.getPreferredSize().width, 55));
        header3.setReorderingAllowed(false);

// Thêm vào giao diện
        scroll3.setPreferredSize(new Dimension(1642, 300));
        scroll3.setBorder(null);
        scroll3.getViewport().setOpaque(false);
        scroll3.setViewportBorder(null);
        b.add(b3);
        b.add(Box.createVerticalStrut(80));

// Gọi loadTableData() sau khi bảng đã được tạo và thêm vào giao diện
        loadTableData();


// Gọi repaint và revalidate sau khi tải dữ liệu xong
        table2.repaint();
        table2.revalidate();

        table.repaint();
        table.revalidate();



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


















public HoaDonDTO openHoaDon() {
    // Lấy bảng từ tableModel
    DefaultTableModel model = (DefaultTableModel) table.getModel();

    // Lấy chỉ số dòng được chọn
    int selectedRow = table.getSelectedRow();

    // Kiểm tra xem có dòng nào được chọn không
    if (selectedRow < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng trong bảng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    // Lấy thông tin từ bảng
    String tenKhachHang = model.getValueAt(selectedRow, 5).toString();  // Tên khách
    String soDienThoai = model.getValueAt(selectedRow, 9).toString();   // Số điện thoại
    String cccd = model.getValueAt(selectedRow, 10).toString();         // CCCD
    String maKH = model.getValueAt(selectedRow, 11).toString();         // Mã khách hàng
    Object ngayDenObj = model.getValueAt(selectedRow, 6);
    Object ngayDiObj = model.getValueAt(selectedRow, 7);
    // Lấy giá tiền phòng từ bảng
    String giaPhongStr = model.getValueAt(selectedRow, 13).toString();  // Lấy giá trị từ cột 13 (giả sử là giá phòng)

    try {
        // Loại bỏ dấu phẩy và các ký tự không phải số
        String numericValue = giaPhongStr.replaceAll("[^0-9.]", "");

        // Chuyển đổi giá trị thành kiểu double
         giaPhong = Double.parseDouble(numericValue);

        // Tiến hành xử lý với 'giaPhong'
        System.out.println("Giá phòng: " + giaPhong);
    } catch (NumberFormatException e) {
        System.out.println("Lỗi định dạng số: " + e.getMessage());
    }



    // Xử lý ngày nhận và trả phòng
    String ngayDen = ngayDenObj != null ? ngayDenObj.toString().trim() : "";
    String ngayDi = ngayDiObj != null ? ngayDiObj.toString().trim() : "";

    // In thông tin để gỡ lỗi
    System.out.println("Tên khách: " + tenKhachHang);
    System.out.println("Ngày đến: " + ngayDen);
    System.out.println("Ngày đi: " + ngayDi);
    System.out.println("Số điện thoại: " + soDienThoai);
    System.out.println("CCCD: " + cccd);
    System.out.println("Mã khách hàng: " + maKH);

    // Tạo JDialog hiển thị hóa đơn
    JDialog dialog = new JDialog();
    dialog.setTitle("Hóa đơn");
    dialog.setSize(800, 850);
    dialog.setLayout(new BorderLayout());
    dialog.getContentPane().setBackground(new Color(40, 40, 44));

    Box bdialog = Box.createVerticalBox();

    // Tạo header
    JPanel pheader = new JPanel();
    pheader.setLayout(new BoxLayout(pheader, BoxLayout.Y_AXIS));
    pheader.setBackground(new Color(40, 40, 44));
    JLabel titleDialog = new JLabel("   HÓA ĐƠN");
    titleDialog.setForeground(Color.WHITE);
    titleDialog.setFont(new Font("Montserrat", Font.BOLD, 32));

    LocalDate currentDate = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
    String formattedDate = currentDate.format(formatter);
    JLabel day = new JLabel(formattedDate);
    day.setForeground(Color.WHITE);
    day.setFont(new Font("Montserrat", Font.PLAIN, 16));

    pheader.add(titleDialog);
    pheader.add(day);

    // Thêm nút X vào header
    Box bheader = Box.createHorizontalBox();
    bheader.add(pheader);
    bheader.add(Box.createHorizontalGlue()); // Đẩy nút X sang bên phải
    JButton btnX = new JButton("X");
    btnX.setPreferredSize(new Dimension(40, 40));
    btnX.setBackground(new Color(200, 50, 50)); // Màu đỏ cho nút X
    btnX.setForeground(Color.WHITE);
    btnX.setFont(new Font("Arial", Font.BOLD, 25));
    btnX.setBorder(BorderFactory.createEmptyBorder());
    btnX.addActionListener(e -> {
        dialog.dispose(); // Đóng dialog khi nhấn X
    });
    bheader.add(btnX);
    bdialog.add(bheader);
    bdialog.add(Box.createVerticalStrut(10));

    // Tạo panel thông tin khách hàng
    JPanel customerInfoPanel = new JPanel();
    customerInfoPanel.setForeground(Color.WHITE);
    customerInfoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    customerInfoPanel.setBackground(new Color(40, 40, 44));
    customerInfoPanel.setPreferredSize(new Dimension(700, 200));
    customerInfoPanel.setMinimumSize(new Dimension(700, 200));
    customerInfoPanel.setMaximumSize(new Dimension(700, 200));

    JPanel bcustomerInfoPanel = new JPanel();
    bcustomerInfoPanel.setLayout(new BoxLayout(bcustomerInfoPanel, BoxLayout.Y_AXIS));
    bcustomerInfoPanel.setBackground(new Color(40, 40, 44));

    // Họ tên
    Box bhoten = Box.createHorizontalBox();
    lbhoten = new JLabel("Họ tên khách hàng:");
    lbhoten1 = new JLabel(tenKhachHang);
    bhoten.add(lbhoten);
    bhoten.add(lbhoten1);
    bhoten.setAlignmentX(Component.LEFT_ALIGNMENT);
    bhoten.add(Box.createHorizontalStrut(120));
    bcustomerInfoPanel.add(bhoten);
    bcustomerInfoPanel.add(Box.createVerticalStrut(15));

    // Số điện thoại và CCCD
    Box bsodt = Box.createHorizontalBox();
    lbsdt = new JLabel("Số điện thoại:");
    lbsdt1 = new JLabel(soDienThoai);
    lbcccd = new JLabel("Số CCCD:");
    lbcccd1 = new JLabel(cccd);
    bsodt.add(lbsdt);
    bsodt.add(lbsdt1);
    bsodt.add(Box.createHorizontalStrut(195));
    bsodt.add(lbcccd);
    bsodt.add(lbcccd1);
    bsodt.setAlignmentX(Component.LEFT_ALIGNMENT);
    bcustomerInfoPanel.add(bsodt);
    bcustomerInfoPanel.add(Box.createVerticalStrut(15));

    // Ngày nhận và trả phòng
    Box bngay = Box.createHorizontalBox();
    lbngaynhan = new JLabel("Ngày nhận phòng:");
    lbngaynhan1 = new JLabel(ngayDen);
    lbngaytra = new JLabel("Ngày trả phòng:");
    lbngaytra1 = new JLabel(ngayDi);
    bngay.add(lbngaynhan);
    bngay.add(lbngaynhan1);
    bngay.add(Box.createHorizontalStrut(85));
    bngay.add(lbngaytra);
    bngay.add(lbngaytra1);
    bngay.setAlignmentX(Component.LEFT_ALIGNMENT);
    bcustomerInfoPanel.add(bngay);
    bcustomerInfoPanel.add(Box.createVerticalStrut(15));

    // Mã số thuế
    Box bmasothue = Box.createHorizontalBox();
    lbmasothue = new JLabel("Mã số thuế:");
    lbmasothue1 = new JLabel(generateTaxCode());
    bmasothue.add(lbmasothue);
    bmasothue.add(lbmasothue1);
    bmasothue.setAlignmentX(Component.LEFT_ALIGNMENT);
    bcustomerInfoPanel.add(bmasothue);

    // Thiết lập font
    Font f = new Font("Montserrat", Font.BOLD, 16);
    Font f1 = new Font("Montserrat", Font.PLAIN, 16);
    lbhoten.setForeground(Color.WHITE);
    lbhoten.setFont(f);
    lbhoten1.setForeground(Color.WHITE);
    lbhoten1.setFont(f1);
    lbsdt.setForeground(Color.WHITE);
    lbsdt.setFont(f);
    lbsdt1.setForeground(Color.WHITE);
    lbsdt1.setFont(f1);
    lbcccd.setForeground(Color.WHITE);
    lbcccd.setFont(f);
    lbcccd1.setForeground(Color.WHITE);
    lbcccd1.setFont(f1);
    lbngaynhan.setForeground(Color.WHITE);
    lbngaynhan.setFont(f);
    lbngaynhan1.setForeground(Color.WHITE);
    lbngaynhan1.setFont(f1);
    lbngaytra.setForeground(Color.WHITE);
    lbngaytra.setFont(f);
    lbngaytra1.setForeground(Color.WHITE);
    lbngaytra1.setFont(f1);
    lbmasothue.setForeground(Color.WHITE);
    lbmasothue.setFont(f);
    lbmasothue1.setForeground(Color.WHITE);
    lbmasothue1.setFont(f1);

    customerInfoPanel.add(bcustomerInfoPanel);
    bdialog.add(customerInfoPanel);
    bdialog.add(Box.createVerticalStrut(10));

    // Tạo bảng hiển thị dịch vụ
    String[] columnNames = {"STT", "Tên dịch vụ", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"};
    tableModel1 = new DefaultTableModel(null, columnNames);
    table1 = new JTable(tableModel1);
    JScrollPane scroll1 = new JScrollPane(table1, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    bdialog.add(scroll1);

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

    // Load dịch vụ lên tableModel1
    loadPhieuDatDichVu(maKH);

    // Tổng tiền
    Box btotal = Box.createHorizontalBox();
    JLabel totalLabel = new JLabel("Tổng tiền: ", SwingConstants.RIGHT);
    JLabel totalLabel1 = new JLabel("");
    totalLabel.setForeground(Color.WHITE);
    totalLabel1.setForeground(Color.WHITE);
    totalLabel.setFont(new Font("Montserrat", Font.PLAIN, 16));

    // Tính tổng tiền
    // Tính tổng tiền
    double total = 0.0;
    for (int i = 0; i < tableModel1.getRowCount(); i++) {
        String thanhTienStr = (String) tableModel1.getValueAt(i, 5);
        if (!thanhTienStr.equals("N/A")) {
            total += Double.parseDouble(thanhTienStr.replace(",", ""));
        }
    }

// Thêm giá phòng vào tổng tiền
    total += giaPhong;  // giaPhong là giá phòng bạn đã có trước đó, tính bằng double

// Hiển thị tổng tiền sau khi đã cộng thêm giá phòng
    System.out.println("Tổng tiền: " + String.format("%,.0f VNĐ", total));

    totalLabel1.setText(String.format("%,.0f VNĐ", total));

    btotal.add(Box.createHorizontalStrut(500));
    btotal.add(totalLabel);
    btotal.add(totalLabel1);
    bdialog.add(btotal);
    bdialog.add(Box.createVerticalStrut(90));

    // Thêm nút Xác nhận và Xuất hóa đơn
    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonPanel.setBackground(new Color(40, 40, 44));

    JButton btnXacNhan = new JButton("Xác nhận");
    btnXacNhan.setPreferredSize(new Dimension(150, 40));
    btnXacNhan.setBackground(new Color(74, 74, 66));
    btnXacNhan.setForeground(Color.WHITE);
    btnXacNhan.setFont(f1);
    btnXacNhan.addActionListener(e -> {
        int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có muốn lập hóa đơn", "Thành công", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Random random = new Random();
                String maHD = String.format("HD%04d", random.nextInt(1000));
                String maNV = (String) table.getValueAt(row, 12);
                String maPDP = (String) table.getValueAt(row, 0);
                String ngayNhan = (String) table.getValueAt(row, 6);
                String ngayTra = (String) table.getValueAt(row, 7);
                String ngayLap = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

                // Tạo DTO để gửi lên server
                HoaDonDTO hoaDon = new HoaDonDTO();
                hoaDon.setMaHD(maHD);
                hoaDon.setMaKH(maKH);
                hoaDon.setMaNV(maNV);
                hoaDon.setMaPDP(maPDP);
                hoaDon.setNgayLapHD(LocalDateTime.now());
                hoaDon.setNgayNhanPhong(LocalDate.parse(ngayNhan).atStartOfDay());
                hoaDon.setNgayTraPhong(LocalDate.parse(ngayTra).atStartOfDay());
                hoaDon.setSoPhongDat(1); // hoặc parse từ dữ liệu nếu cần

                // Gửi về server và cập nhật bảng nếu thành công
                themHoaDon(hoaDon);

                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn một hàng trong bảng");
            }
        }
    });

    JButton btnXuatHD = new JButton("Xuất hóa đơn");
    btnXuatHD.setPreferredSize(new Dimension(150, 40));
    btnXuatHD.setBackground(new Color(51, 70, 50));
    btnXuatHD.setForeground(Color.WHITE);
    btnXuatHD.setFont(f1);
    btnXuatHD.addActionListener(e -> {
        exportToPDF(table1,giaPhong);
    });

    buttonPanel.add(btnXacNhan);
    buttonPanel.add(btnXuatHD);
    bdialog.add(buttonPanel);
    // Tạo label hiển thị giá tiền phòng
    Box bgiaPhong = Box.createHorizontalBox();
    JLabel giaPhongLabel = new JLabel("Giá phòng: ");
    JLabel giaPhongValue = new JLabel(String.format("%,.0f VNĐ", giaPhong));
    giaPhongLabel.setForeground(Color.WHITE);
    giaPhongValue.setForeground(Color.WHITE);
    giaPhongLabel.setFont(f);
    giaPhongValue.setFont(f1);

    bgiaPhong.add(giaPhongLabel);
    bgiaPhong.add(giaPhongValue);
    bgiaPhong.setAlignmentX(Component.LEFT_ALIGNMENT);

// Thêm label vào dialog
    customerInfoPanel.add(bgiaPhong);

    // Thêm Box chính vào JDialog
    dialog.add(bdialog, BorderLayout.CENTER);
    dialog.setLocationRelativeTo(null);
    dialog.setResizable(false);
    dialog.setUndecorated(true);
    dialog.setVisible(true);

    return null;
}

    private LocalDateTime parseDateTime(Object value) {
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }

        String dateStr = value != null ? value.toString().trim() : "";
        if (dateStr.isEmpty()) {
            System.err.println("Empty date string");
            return null;
        }

        // Các định dạng ngày được hỗ trợ
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME, // e.g., "2025-04-26T12:00:00"
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd") // e.g., "2025-04-26"
        };

        for (DateTimeFormatter formatter : formatters) {
            try {
                // Nếu là định dạng chỉ có ngày (yyyy-MM-dd)
                if (formatter == formatters[3]) { // So sánh trực tiếp với định dạng yyyy-MM-dd
                    return LocalDate.parse(dateStr, formatter).atStartOfDay();
                }
                // Thử parse các định dạng có thời gian
                return LocalDateTime.parse(dateStr, formatter);
            } catch (DateTimeParseException e) {
                // Thử định dạng tiếp theo
            }
        }

        System.err.println("Could not parse date: " + dateStr);
        return null;
    }


    // Hàm sinh mã số thuế ngẫu nhiên
    public static String generateTaxCode() {
        Random random = new Random();

        // Mã tỉnh (6 chữ số)
        int provinceCode = random.nextInt(1000000); // Mã tỉnh từ 000000 đến 999999
        String provinceCodeStr = String.format("%06d", provinceCode);

        // Mã số cá nhân (7 chữ số)
        int personalCode = random.nextInt(10000000); // Mã cá nhân từ 0000000 đến 9999999
        String personalCodeStr = String.format("%07d", personalCode);

        // Chữ số kiểm tra (1 chữ số)
        int checkDigit = random.nextInt(10); // Chữ số kiểm tra từ 0 đến 9

        // Kết hợp thành mã số thuế
        return provinceCodeStr + personalCodeStr + checkDigit;
    }














    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnlaphd) {
            openHoaDon();
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

    // Hàm này sẽ được gọi khi bạn muốn tải dữ liệu hóa đơn
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
                String soDienThoai = "Không xác định";
                String cccd = "Không xác định";
                if (pdp.getMaKH() != null) {
                    KhachHangDTO kh = khachHangMap.get(pdp.getMaKH());
                    tenKhachHang = kh != null ? kh.getHoTen() : "Khách hàng không tồn tại";
                    soDienThoai = kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "Không có số điện thoại";
                    cccd = kh.getSoCCCD() != null ? kh.getSoCCCD() : "Không có CCCD";
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
                        long soDemO = 0;
                        if (pdp.getNgayNhanPhongDuKien() != null && pdp.getNgayTraPhongDuKien() != null) {
                            soDemO = ChronoUnit.DAYS.between(pdp.getNgayNhanPhongDuKien(), pdp.getNgayTraPhongDuKien());
                            if (soDemO < 0) soDemO = 0; // tránh lỗi nếu ngày đến sau ngày đi
                        }

                        // Tính tiền phòng
                        double tongTienPhong = phong.getGiaPhong() * soDemO;

                        // Thêm thông tin vào bảng
                        tableModel.addRow(new Object[]{
                                pdp.getMaPDP(),
                                phong.getMaLoai() != null ? phong.getMaLoai() : "N/A",
                                phong.getTenPhong() != null ? phong.getTenPhong() : "N/A",
                                phong.getMaPhong() != null ? phong.getMaPhong() : "N/A",
                                getTrangThaiPhong(phong.getTinhTrang()),
                                tenKhachHang,
                                formatDate(pdp.getNgayNhanPhongDuKien()),
                                formatDate(pdp.getNgayTraPhongDuKien()),
                                soDemO,
                                soDienThoai,
                                cccd,
                                pdp.getMaKH(),
                                pdp.getMaNV(),
                                String.format("%,.0f", tongTienPhong) + " VNĐ" // Hiển thị tiền phòng
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


    //    private void loadPhongData() {
//        tableModel.setRowCount(0);
//
//        try {
//            // 1. Lấy danh sách phiếu đặt phòng
//            Request<Void> request = new Request<>("GET_ALL_PHIEU_DAT_PHONG", null);
//            SocketManager.send(request);
//            Type phieuResponseType = new TypeToken<Response<List<PhieuDatPhongDTO>>>() {}.getType();
//            Response<List<PhieuDatPhongDTO>> response = SocketManager.receiveType(phieuResponseType);
//
//            if (response == null || !response.isSuccess() || response.getData() == null) {
//                String errorMsg = response == null ? "Không nhận được phản hồi từ server"
//                        : (response.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
//                System.out.println("GET_ALL_PHIEU_DAT_PHONG failed: " + errorMsg);
//                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu: " + errorMsg,
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//                return;
//            }
//
//            List<PhieuDatPhongDTO> dsPhieuDatPhong = response.getData();
//            if (dsPhieuDatPhong.isEmpty()) {
//                JOptionPane.showMessageDialog(this, "Không có dữ liệu phiếu đặt phòng",
//                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                return;
//            }
//
//            // 2. Lấy danh sách phòng
//            Map<String, PhongDTO> phongMap = new HashMap<>();
//            Request<Void> phongRequest = new Request<>("GET_ALL_PHONG", null);
//            SocketManager.send(phongRequest);
//            Type phongResponseType = new TypeToken<Response<List<PhongDTO>>>() {}.getType();
//            Response<List<PhongDTO>> phongResponse = SocketManager.receiveType(phongResponseType);
//
//            if (phongResponse != null && phongResponse.isSuccess() && phongResponse.getData() != null) {
//                phongResponse.getData().forEach(phong -> {
//                    System.out.println("Phòng: " + phong.getMaPhong() + " - " + phong.getTenPhong());
//                    phongMap.put(phong.getMaPhong(), phong);
//                });
//            } else {
//                String errorMsg = phongResponse == null ? "Không nhận được phản hồi từ server"
//                        : !phongResponse.isSuccess() ? "Yêu cầu không thành công"
//                        : "Dữ liệu phòng trống";
//                System.out.println("GET_ALL_PHONG failed: " + errorMsg);
//                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách phòng: " + errorMsg,
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//
//            // 3. Lấy danh sách khách hàng
//            Map<String, KhachHangDTO> khachHangMap = new HashMap<>();
//            Request<Void> khachHangRequest = new Request<>("GET_ALL_KHACH_HANG", null);
//            SocketManager.send(khachHangRequest);
//            Type khachHangResponseType = new TypeToken<Response<List<KhachHangDTO>>>() {}.getType();
//            Response<List<KhachHangDTO>> khachHangResponse = SocketManager.receiveType(khachHangResponseType);
//
//            if (khachHangResponse != null && khachHangResponse.isSuccess() && khachHangResponse.getData() != null) {
//                khachHangResponse.getData().forEach(kh -> khachHangMap.put(kh.getMaKH(), kh));
//            } else {
//                String errorMsg = khachHangResponse == null ? "Không nhận được phản hồi từ server"
//                        : !khachHangResponse.isSuccess() ? "Yêu cầu không thành công"
//                        : "Dữ liệu khách hàng trống";
//                System.out.println("GET_ALL_KHACH_HANG failed: " + errorMsg);
//                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách khách hàng: " + errorMsg,
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//
//            // 4. Xử lý dữ liệu và hiển thị
//            StringBuilder errorMessages = new StringBuilder();
//            int errorCount = 0;
//
//            for (PhieuDatPhongDTO pdp : dsPhieuDatPhong) {
//                // Xử lý thông tin khách hàng
//                String tenKhachHang = "Không xác định";
//                String soDienThoai = "Không xác định";
//                String cccd = "Không xác định";
//                if (pdp.getMaKH() != null) {
//                    KhachHangDTO kh = khachHangMap.get(pdp.getMaKH());
//                    tenKhachHang = kh != null ? kh.getHoTen() : "Khách hàng không tồn tại";
//                    soDienThoai = kh.getSoDienThoai() != null ? kh.getSoDienThoai() : "Không có số điện thoại";
//                    cccd = kh.getSoCCCD() != null ? kh.getSoCCCD() : "Không có CCCD";
//                }
//
//                // Xử lý danh sách phòng
//                List<String> dsMaPhong = pdp.getDsMaPhong();
//                if (dsMaPhong == null || dsMaPhong.isEmpty()) {
//                    tableModel.addRow(new Object[]{
//                            pdp.getMaPDP(),
//                            "N/A", "N/A", "N/A", "N/A",
//                            tenKhachHang,
//                            formatDate(pdp.getNgayNhanPhongDuKien()),
//                            formatDate(pdp.getNgayTraPhongDuKien())
//                    });
//                    continue;
//                }
//
//                // Thêm từng phòng vào bảng
//                for (String maPhong : dsMaPhong) {
//                    PhongDTO phong = phongMap.get(maPhong);
//                    if (phong == null) {
//                        errorMessages.append("Không tìm thấy phòng ").append(maPhong)
//                                .append(" trong phiếu ").append(pdp.getMaPDP()).append("\n");
//                        errorCount++;
//                        continue;
//                    }
//
//                    if (phong.getTinhTrang() != 0) {
//                        long soDemO = 0;
//                        if (pdp.getNgayNhanPhongDuKien() != null && pdp.getNgayTraPhongDuKien() != null) {
//                            soDemO = ChronoUnit.DAYS.between(pdp.getNgayNhanPhongDuKien(), pdp.getNgayTraPhongDuKien());
//                            if (soDemO < 0) soDemO = 0; // tránh lỗi nếu ngày đến sau ngày đi
//                        }
//
//                        tableModel.addRow(new Object[]{
//                                pdp.getMaPDP(),
//                                phong.getMaLoai() != null ? phong.getMaLoai() : "N/A",
//                                phong.getTenPhong() != null ? phong.getTenPhong() : "N/A",
//                                phong.getMaPhong() != null ? phong.getMaPhong() : "N/A",
//                                getTrangThaiPhong(phong.getTinhTrang()),
//                                tenKhachHang,
//                                formatDate(pdp.getNgayNhanPhongDuKien()),
//                                formatDate(pdp.getNgayTraPhongDuKien()),
//                                soDemO,
//                                soDienThoai,
//                                cccd,
//                                pdp.getMaKH(),
//                                pdp.getMaNV()
//
//
//                        });
//                    }
//                }
//            }
//
//            // Hiển thị lỗi nếu có
//            if (errorCount > 0) {
//                JOptionPane.showMessageDialog(this,
//                        "Có " + errorCount + " lỗi xảy ra:\n" + errorMessages.toString(),
//                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
//            }
//
//        } catch (Exception e) {
//            System.out.println("Unexpected error in loadPhongData: " + e.getMessage());
//            e.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(),
//                    "Lỗi", JOptionPane.ERROR_MESSAGE);
//        } finally {
//            table.repaint();
//            table.revalidate();
//        }
//    }
//
//    // Hàm hỗ trợ
//    private String formatDate(LocalDate date) {
//        return date != null ? date.toString() : "N/A";
//    }
//
//    private String getTrangThaiPhong(int tinhTrang) {
//        switch (tinhTrang) {
//            case 0: return "Trống";
//            case 1: return "Đã đặt";
//            case 2: return "Đang sử dụng";
//            default: return "Không xác định";
//        }
//    }
    private void loadPhieuDatDichVu(String maKH) {
        tableModel1.setRowCount(0);

        try {
            // 1. Kiểm tra mã khách hàng
            if (maKH == null || maKH.trim().isEmpty()) {
                System.out.println("Mã khách hàng rỗng hoặc null");
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            System.out.println("Đang tải phiếu đặt dịch vụ cho maKH: " + maKH);

            // 2. Lấy danh sách phiếu đặt dịch vụ theo mã khách hàng
            Request<String> request = new Request<>("FIND_BY_MA_KH", maKH);
            SocketManager.send(request);
            Type phieuResponseType = new TypeToken<Response<List<PhieuDatDichVuDTO>>>() {}.getType();
            Response<List<PhieuDatDichVuDTO>> response = SocketManager.receiveType(phieuResponseType);

            if (response == null || !response.isSuccess() || response.getData() == null) {
                String errorMsg = response == null ? "Không nhận được phản hồi từ server"
                        : (response.getData() == null ? "Dữ liệu trống" : "Lỗi không xác định");
                System.out.println("FIND_BY_MA_KH failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy dữ liệu phiếu đặt dịch vụ: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            List<PhieuDatDichVuDTO> dsPhieuDatDichVu = response.getData();
            System.out.println("Số phiếu đặt dịch vụ tìm thấy: " + dsPhieuDatDichVu.size());
            if (dsPhieuDatDichVu.isEmpty()) {
                System.out.println("Không có phiếu đặt dịch vụ cho maKH: " + maKH);
                JOptionPane.showMessageDialog(this, "Không có phiếu đặt dịch vụ cho khách hàng " + maKH,
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // 3. Lấy danh sách dịch vụ
            Map<String, DichVuDTO> dichVuMap = new HashMap<>();
            Request<Void> dichVuRequest = new Request<>("GET_ALL_DICH_VU", null);
            SocketManager.send(dichVuRequest);
            Type dichVuResponseType = new TypeToken<Response<List<DichVuDTO>>>() {}.getType();
            Response<List<DichVuDTO>> dichVuResponse = SocketManager.receiveType(dichVuResponseType);

            if (dichVuResponse != null && dichVuResponse.isSuccess() && dichVuResponse.getData() != null) {
                dichVuResponse.getData().forEach(dv -> {
                    System.out.println("Dịch vụ: " + dv.getMaDV() + " - " + dv.getTenDV() + " - Đơn giá: " + dv.getDonGia());
                    dichVuMap.put(dv.getMaDV(), dv);
                });
                System.out.println("Số dịch vụ tìm thấy: " + dichVuMap.size());
            } else {
                String errorMsg = dichVuResponse == null ? "Không nhận được phản hồi từ server"
                        : !dichVuResponse.isSuccess() ? "Yêu cầu không thành công"
                        : "Dữ liệu dịch vụ trống";
                System.out.println("GET_ALL_DICH_VU failed: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi khi lấy danh sách dịch vụ: " + errorMsg,
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            // 4. Xử lý dữ liệu và hiển thị
            StringBuilder errorMessages = new StringBuilder();
            int errorCount = 0;
            int stt = 1; // Biến đếm STT

            for (PhieuDatDichVuDTO pddv : dsPhieuDatDichVu) {
                System.out.println("Xử lý phiếu: " + pddv.getMaPDDV() + ", dsMaDV: " + pddv.getDsMaDV());

                // Xử lý danh sách dịch vụ
                List<String> dsMaDV = pddv.getDsMaDV();
                if (dsMaDV == null || dsMaDV.isEmpty()) {
                    System.out.println("Phiếu " + pddv.getMaPDDV() + " không có dsMaDV");
                    tableModel1.addRow(new Object[]{
                            stt++,
                            "N/A",
                            "N/A",
                            pddv.getSoLuongDichVu(),
                            "N/A",
                            "N/A"
                    });
                    continue;
                }

                // Thêm từng dịch vụ vào bảng
                for (String maDV : dsMaDV) {
                    System.out.println("Kiểm tra maDV: " + maDV);
                    DichVuDTO dichVu = dichVuMap.get(maDV);
                    if (dichVu == null) {
                        errorMessages.append("Không tìm thấy dịch vụ ").append(maDV)
                                .append(" trong phiếu ").append(pddv.getMaPDDV()).append("\n");
                        errorCount++;
                        System.out.println("Dịch vụ " + maDV + " không tồn tại trong dichVuMap");
                        continue;
                    }

                    // Tính thành tiền: số lượng * đơn giá
                    double thanhTien = pddv.getSoLuongDichVu() * dichVu.getDonGia();
                    System.out.println("Thêm hàng: " + dichVu.getTenDV() + ", Thành tiền: " + thanhTien);

                    tableModel1.addRow(new Object[]{
                            stt++,
                            dichVu.getTenDV() != null ? dichVu.getTenDV() : "N/A",
                            dichVu.getDonViTinh() != null ? dichVu.getDonViTinh() : "Cái",
                            pddv.getSoLuongDichVu(),
                            dichVu.getDonGia() == 0.0 ? "N/A" : String.format("%,.0f", dichVu.getDonGia()),
                            thanhTien == 0.0 ? "N/A" : String.format("%,.0f", thanhTien)
                    });
                }
            }

            // Hiển thị lỗi nếu có
            if (errorCount > 0) {
                System.out.println("Có " + errorCount + " lỗi: " + errorMessages.toString());
                JOptionPane.showMessageDialog(this,
                        "Có " + errorCount + " lỗi xảy ra:\n" + errorMessages.toString(),
                        "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }

        } catch (Exception e) {
            System.out.println("Unexpected error in loadPhieuDatDichVu: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            if (table1 != null) {
                table1.repaint();
                table1.revalidate();
            } else {
                System.err.println("table1 chưa được khởi tạo khi loadPhieuDatDichVu.");
            }
        }

    }


    // Hàm hỗ trợ
    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.toString() : "N/A";
    }






//    private void loadTableData() {
//        // Xóa tất cả các dòng cũ trong tableModel3
//        tableModel3.setRowCount(0);
//
//        // Gửi request và nhận dữ liệu hóa đơn
//        Request<Void> request = new Request<>("GET_ALL_HOA_DON", null);
//        SocketManager.send(request);
//
//        try {
//            // Dùng TypeToken để deserialize đúng kiểu
//            Type responseType = new TypeToken<Response<List<HoaDonDTO>>>() {}.getType();
//            Response<List<HoaDonDTO>> response = SocketManager.receiveType(responseType);
//
//            if (response != null && response.isSuccess()) {
//                List<HoaDonDTO> dsHoaDon = response.getData();
//
//                if (dsHoaDon == null || dsHoaDon.isEmpty()) {
//                    JOptionPane.showMessageDialog(this,
//                            "Không có dữ liệu hóa đơn!",
//                            "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    return;
//                }
//
//                // Duyệt qua danh sách hóa đơn và thêm vào table
//                for (HoaDonDTO hd : dsHoaDon) {
//                    tableModel3.addRow(new Object[]{
//                            hd.getMaHD(),
//                            hd.getMaKH(),
//                            hd.getMaNV(),
//                            hd.getMaPDP(),
//                            hd.getNgayLapHD(),
//                            hd.getNgayNhanPhong(),
//                            hd.getNgayTraPhong(),
//                            hd.getSoPhongDat()
//                    });
//                }
//                System.out.println("Loaded " + dsHoaDon.size() + " invoices into table");
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Không thể lấy dữ liệu hóa đơn từ server!",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this,
//                    "Lỗi kết nối đến server khi tải dữ liệu: " + ex.getMessage(),
//                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
//        }
//
//
//// Hàm hỗ trợ
//
//
//
////        // Cập nhật bảng sau khi dữ liệu được tải
////        table2.repaint();
////        table2.revalidate();
//    }
private void loadTableData() {
    // Xóa tất cả các dòng cũ trong tableModel3
    tableModel3.setRowCount(0);

    // Gửi request và nhận dữ liệu hóa đơn
    Request<Void> request = new Request<>("GET_ALL_HOA_DON", null);
    SocketManager.send(request);

    try {
        // Dùng TypeToken để deserialize đúng kiểu
        Type responseType = new TypeToken<Response<List<HoaDonDTO>>>() {}.getType();
        Response<List<HoaDonDTO>> response = SocketManager.receiveType(responseType);

        if (response != null && response.isSuccess()) {
            List<HoaDonDTO> dsHoaDon = response.getData();

            if (dsHoaDon == null || dsHoaDon.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Không có dữ liệu hóa đơn!",
                        "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Sắp xếp danh sách hóa đơn theo ngày lập (ngayLapHD) tăng dần
            dsHoaDon.sort((hd1, hd2) -> {
                if (hd1.getNgayLapHD() == null || hd2.getNgayLapHD() == null) {
                    return 0; // Nếu một trong hai ngày lập là null, giữ nguyên thứ tự
                }
                return hd1.getNgayLapHD().compareTo(hd2.getNgayLapHD());
            });

            System.out.println(dsHoaDon);
            // Duyệt qua danh sách hóa đơn và thêm vào table
            for (HoaDonDTO hd : dsHoaDon) {
                // Kiểm tra và chuyển đổi các giá trị LocalDateTime
                String ngayLapHD = (hd.getNgayLapHD() != null) ? formatLocalDateTime(hd.getNgayLapHD()) : "N/A";
                String ngayNhanPhong = (hd.getNgayNhanPhong() != null) ? formatLocalDateTime(hd.getNgayNhanPhong()) : "N/A";
                String ngayTraPhong = (hd.getNgayTraPhong() != null) ? formatLocalDateTime(hd.getNgayTraPhong()) : "N/A";

                tableModel3.addRow(new Object[]{
                        hd.getMaHD(),
                        hd.getMaKH(),
                        hd.getMaNV(),
                        hd.getMaPDP(),
                        ngayLapHD,
                        ngayNhanPhong,
                        ngayTraPhong,
                        hd.getSoPhongDat()
                });
            }
            System.out.println("Loaded " + dsHoaDon.size() + " invoices into table");
        } else {
            JOptionPane.showMessageDialog(this,
                    "Không thể lấy dữ liệu hóa đơn từ server!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    } catch (IOException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this,
                "Lỗi kết nối đến server khi tải dữ liệu: " + ex.getMessage(),
                "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
    }
}

    private String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            // Đảm bảo sử dụng định dạng chuẩn ISO khi hiển thị
            return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        return "N/A";
    }


    public void themHoaDon(HoaDonDTO hoaDon) {
        // Kiểm tra trùng maPDP
        for (HoaDonDTO hd : dsHoaDon) {
            if (hd.getMaPDP() != null && hd.getMaPDP().equals(hoaDon.getMaPDP())) {
                JOptionPane.showMessageDialog(this,
                        "Mã phiếu đặt phòng này đã được lập hóa đơn trước đó!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return; // Không gửi yêu cầu nếu bị trùng
            }
        }

        Request<HoaDonDTO> request = new Request<>("CREATE_HOA_DON", hoaDon);
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .create();

            String json = gson.toJson(request);
            System.out.println("JSON sent to server: " + json);

            SocketManager.send(request);
            Response<String> response = SocketManager.receive(Response.class);

            if (response != null) {
                JOptionPane.showMessageDialog(this,
                        response.getData(),
                        response.isSuccess() ? "Thành công" : "Lỗi",
                        response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);

                if (response.isSuccess()) {
                    loadTableData();
                    table2.repaint();
                    table2.revalidate();
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không nhận được phản hồi từ server",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Lỗi khi gửi dữ liệu: " + ex.getMessage(),
                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

//    public void themHoaDon(HoaDonDTO hoaDon) {
//        Request<HoaDonDTO> request = new Request<>("CREATE_HOA_DON", hoaDon);
//        try {
//            // Tạo Gson với LocalDateTimeAdapter để xử lý kiểu LocalDateTime
//            Gson gson = new GsonBuilder()
//                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
//                    .create();
//
//            // In JSON để kiểm tra dữ liệu gửi đi
//            String json = gson.toJson(request);
//            System.out.println("JSON sent to server: " + json);
//
//            // Gửi yêu cầu đến server
//            SocketManager.send(request);
//
//            // Nhận phản hồi từ server
//            Response<String> response = SocketManager.receive(Response.class);
//
//            if (response != null) {
//                // Hiển thị thông báo thành công hoặc lỗi
//                JOptionPane.showMessageDialog(this,
//                        response.getData(),
//                        response.isSuccess() ? "Thành công" : "Lỗi",
//                        response.isSuccess() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
//
//                // Nếu tạo hóa đơn thành công, tải lại toàn bộ dữ liệu từ server
//                if (response.isSuccess()) {
//                    loadTableData(); // Tải lại danh sách hóa đơn đã được sắp xếp
//                    table2.repaint();
//                    table2.revalidate();
//                }
//            } else {
//                JOptionPane.showMessageDialog(this,
//                        "Không nhận được phản hồi từ server",
//                        "Lỗi", JOptionPane.ERROR_MESSAGE);
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this,
//                    "Lỗi khi gửi dữ liệu: " + ex.getMessage(),
//                    "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
//        }
//    }

















    public BufferedImage generateQRCodeImage(String content, int width, int height) throws WriterException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    public void exportToPDF(JTable table1, double giaPhong) {
        String filePath = "C:/Users/MSI/Desktop/hoadon/hoa_don_" + System.currentTimeMillis() + ".pdf";

        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = null;

        try {
            File fontFile = new File("C:/Windows/Fonts/times.ttf");
            if (!fontFile.exists()) {
                throw new IOException("Font file không tồn tại: " + fontFile.getAbsolutePath());
            }

            PDType0Font font = PDType0Font.load(document, fontFile);
            contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(font, 12);
            contentStream.setLeading(14f);

            // === THÔNG TIN HÓA ĐƠN ===
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText("HÓA ĐƠN");
            contentStream.newLine();

            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("'Ngày' dd 'tháng' MM 'năm' yyyy");
            contentStream.showText("Ngày lập: " + currentDate.format(formatter));
            contentStream.newLine();
            contentStream.showText("Họ tên khách hàng: " + lbhoten1.getText());
            contentStream.newLine();
            contentStream.showText("Số điện thoại: " + lbsdt1.getText());
            contentStream.newLine();
            contentStream.showText("Số CCCD: " + lbcccd1.getText());
            contentStream.newLine();
            contentStream.showText("Mã số thuế: " + lbmasothue1.getText());
            contentStream.newLine();
            contentStream.showText("Ngày nhận phòng: " + lbngaynhan1.getText());
            contentStream.newLine();
            contentStream.showText("Ngày trả phòng: " + lbngaytra1.getText());
            contentStream.newLine();

            // Hiển thị giá phòng dưới ngày trả phòng
            contentStream.showText("Giá phòng: " + String.format("%,.0f", giaPhong) + " VNĐ");
            contentStream.newLine();
            contentStream.newLine();

            contentStream.showText("Danh sách dịch vụ:");
            contentStream.endText();

            float marginLeft = 50;
            float startY = 620;
            float rowHeight = 15;
            float currentY = startY;

            String[] columnNames = {"STT", "Tên dịch vụ", "Đơn vị tính", "Số lượng", "Đơn giá", "Thành tiền"};
            contentStream.setFont(font, 10);

            // In tiêu đề cột
            contentStream.beginText();
            contentStream.newLineAtOffset(marginLeft, currentY);
            for (String col : columnNames) {
                contentStream.showText(String.format("%-20s", col));
            }
            contentStream.endText();
            currentY -= rowHeight;

            double tongTien = 0;

            // Tính tổng tiền từ bảng dịch vụ
            for (int i = 0; i < table1.getRowCount(); i++) {
                if (currentY < 50) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, 10);
                    currentY = 750;
                }

                contentStream.beginText();
                contentStream.newLineAtOffset(marginLeft, currentY);
                for (int j = 0; j < table1.getColumnCount(); j++) {
                    Object cell = table1.getValueAt(i, j);
                    contentStream.showText(String.format("%-20s", cell != null ? cell.toString() : ""));
                }
                contentStream.endText();

                // Cộng tổng tiền dịch vụ
                Object thanhTienObj = table1.getValueAt(i, table1.getColumnCount() - 1);
                try {
                    if (thanhTienObj != null) {
                        tongTien += Double.parseDouble(thanhTienObj.toString().replace(",", ""));
                    }
                } catch (NumberFormatException ignored) {}

                currentY -= rowHeight;
            }

            // Cộng giá phòng vào tổng tiền
            tongTien += giaPhong;

            // Hiển thị tổng tiền sau khi đã cộng thêm giá phòng
            contentStream.beginText();
            contentStream.newLineAtOffset(marginLeft, currentY - 20);
            contentStream.setFont(font, 12);
            contentStream.showText("TỔNG TIỀN: " + String.format("%,.0f VNĐ", tongTien));
            contentStream.endText();

            // === TẠO MÃ QR THANH TOÁN ===
            String qrContent = "Thanh toán hóa đơn: " + String.format("%,.0f", tongTien) + " VND";
            BufferedImage qrImage = generateQRCodeImage(qrContent, 150, 150);
            File qrFile = new File("temp_qr.png");
            ImageIO.write(qrImage, "png", qrFile);

            PDImageXObject pdImage = PDImageXObject.createFromFileByContent(qrFile, document);
            contentStream.drawImage(pdImage, 400, currentY - 150, 120, 120);

            contentStream.close();
            document.save(filePath);
            document.close();

            if (qrFile.exists()) qrFile.delete();

            JOptionPane.showMessageDialog(null, "Hóa đơn đã được xuất ra PDF kèm mã QR thanh toán: " + filePath,
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (contentStream != null) contentStream.close();
                if (document != null) document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }








}





