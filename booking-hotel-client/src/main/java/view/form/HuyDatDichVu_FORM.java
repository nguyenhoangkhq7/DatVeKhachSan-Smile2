package view.form;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import dto.DichVuDTO;
import dto.KhachHangDTO;
import dto.PhieuDatPhongDTO;
import dto.PhongDTO;
import model.Request;
import model.Response;
import socket.SocketManager;
import utils.custom_element.*;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class HuyDatDichVu_FORM extends JPanel implements ActionListener,MouseListener {
    private JButton btnDatDV;
    private  JTable table;
    private DefaultTableModel tableModel;
    private Font f1;
    private DefaultTableModel tableModel1;
    private JTable table1;
    private DefaultTableModel tableModel2;
    private JTable table2;
    private JButton btnXacNhan;
    private String selectedMaHD;
    private String selectedMaPhong;
    private String selectMaPDP;
    private List<String> dsMaPDP = new ArrayList<>();

    public HuyDatDichVu_FORM() {
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
        bbutton.add(btnDatDV = new JButton("Hủy dịch vụ"));
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
        f1 = new Font("Montserrat", Font.PLAIN, 16);
        dialog.getContentPane().setBackground(new Color(31,31,32,255));

        JButton btnClose = new JButton("X");
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Arial", Font.BOLD, 16));
        btnClose.setBackground(new Color(255, 69, 58)); // Màu đỏ
        btnClose.setFocusPainted(false);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false); // Đặt trong suốt
        btnClose.setOpaque(true); // Hiển thị màu nền
        btnClose.setPreferredSize(new Dimension(45, 45));

        // Gắn sự kiện đóng dialog
        btnClose.addActionListener(e -> dialog.dispose());

        // Tạo panel chứa nút đóng
        JPanel closePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        closePanel.setOpaque(false); // Đặt trong suốt
        closePanel.add(btnClose);

        // Thêm nút đóng vào JDialog (vị trí trên cùng)
        dialog.add(closePanel, BorderLayout.NORTH);

        Box bdialog = Box.createHorizontalBox();
        Box bLeft = Box.createVerticalBox();
        bLeft.add(Box.createVerticalStrut(10));
        bLeft.setPreferredSize(new Dimension(1550, 880));
        bLeft.setMinimumSize(new Dimension(1550, 880));
        bLeft.setMaximumSize(new Dimension(1550, 880));
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

        JPanel pheader = new JPanel();
        pheader.setLayout(new BoxLayout(pheader, BoxLayout.Y_AXIS));
        pheader.setBackground(new Color(16, 16, 20));
        pheader.setBackground(new Color(40, 40, 44));
        // Thêm tiêu đề và ngày tháng vào JPanel
        JLabel titleDialog = new JLabel("Phiếu đặt dịch vụ");
        titleDialog.setForeground(Color.WHITE);
        titleDialog.setFont(new Font("Montserrat", Font.BOLD, 24));

        Box bheader = Box.createHorizontalBox();
        pheader.add(titleDialog);
        bheader.add(pheader);
        bLeft.add(bheader);
        bLeft.add(Box.createVerticalStrut(50));
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
        //header2.setPreferredSize(new Dimension(header1.getPreferredSize().width, 55));
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
        bLeft.add(bTable2);
        bLeft.add(Box.createVerticalStrut(300));

        // Tạo nut
        Box bbutton2 = Box.createHorizontalBox();

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
        bLeft.add(Box.createVerticalStrut(10));
        bLeft.add(bbutton2);

        // Thêm Box chính vào JDialog
        bdialog.add(Box.createHorizontalStrut(10));
        bdialog.add(bLeft);
        dialog.add(bdialog, BorderLayout.CENTER);

        // Đặt JDialog xuất hiện ở giữa màn hình
        dialog.setLocationRelativeTo(null);
        dialog.setResizable(false);
        dialog.setUndecorated(true);
        dialog.setVisible(true);


        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi hủy dịch vụ!");
            return;
        }

        // Tải danh sách dịch vụ đã đặt
        loadDichVuDaDat(selectedMaHD, selectedMaPhong);



    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == btnDatDV) {
            phieuDatDichVu();
        }  else if (source == btnXacNhan) {
            huyDatDichVu();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            selectedMaPhong = tableModel.getValueAt(selectedRow, 3).toString(); // Lấy mã phòng
//            selectMaPDP = tableModel.getValueAt(selectedRow, 0).toString();
//            // Truy vấn SQL để lấy mã hóa đơn (maHD) từ mã phòng
//            Connection connection = ConnectDB.getConnection();
//            if (connection != null) {
//                try {
//                    String query = "SELECT hd.maHD\n" +
//                            "from ChiTietHoaDon ct join Phong p on ct.maPhong=p.maPhong join PhieuDatPhong pdp on p.maPhong=pdp.maPhong join HoaDon hd on ct.maHD=hd.maHD\n" +
//                            "where ct.maPhong =? and maPDP=?";
//                    var preparedStatement = connection.prepareStatement(query);
//                    preparedStatement.setString(1, selectedMaPhong);
//                    preparedStatement.setString(2, selectMaPDP);
//
//                    var resultSet = preparedStatement.executeQuery();
//                    if (resultSet.next()) {
//                        selectedMaHD = resultSet.getString("maHD"); // Lấy mã hóa đơn
//                    } else {
//                        JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cho phòng đã chọn!");
//                        selectedMaHD = null; // Không tìm thấy mã hóa đơn
//                    }
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                    JOptionPane.showMessageDialog(this, "Lỗi khi truy vấn mã hóa đơn!");
//                }
//            }
//
//            // Hiển thị thông báo đã chọn
//            JOptionPane.showMessageDialog(this,
//                    "Đã chọn:\nMã phòng: " + selectedMaPhong + "\nMã phiếu đặt phòng:"+selectMaPDP );
//        }
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
        table.clearSelection(); // Xóa lựa chọn cũ trên bảng

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
                break; // Chỉ lấy dòng đầu tiên tìm thấy
            }
        }

        if (foundRow != -1) {
            // Di chuyển dòng tìm thấy lên đầu bảng
            moveRowToTop(foundRow);

            // Tạo hiệu ứng hover cho dòng đầu tiên
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
        tableModel.removeRow(rowIndex); // Xóa dòng cũ
        tableModel.insertRow(0, rowData); // Chèn dòng vào đầu bảng
    }

    private void timKiem1(String keyword) {
        table2.clearSelection(); // Xóa lựa chọn cũ trên bảng

        int foundRow = -1;

        // Tìm dòng chứa từ khóa đầu tiên
        for (int i = 0; i < tableModel2.getRowCount(); i++) {
            boolean match = false;

            // Kiểm tra từ khóa trong các cột
            for (int j = 0; j < tableModel2.getColumnCount(); j++) {
                Object cellValue = tableModel2.getValueAt(i, j);
                if (cellValue != null && cellValue.toString().toLowerCase().contains(keyword.toLowerCase())) {
                    match = true;
                    break;
                }
            }

            if (match) {
                foundRow = i;
                break; // Chỉ lấy dòng đầu tiên tìm thấy
            }
        }

        if (foundRow != -1) {
            // Di chuyển dòng tìm thấy lên đầu bảng
            moveRowToTop1(foundRow);

            // Tạo hiệu ứng hover cho dòng đầu tiên
            table2.addRowSelectionInterval(0, 0);
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!");
        }
    }

    private void moveRowToTop1(int rowIndex) {
        Object[] rowData = new Object[tableModel2.getColumnCount()];
        for (int i = 0; i < tableModel2.getColumnCount(); i++) {
            rowData[i] = tableModel2.getValueAt(rowIndex, i);
        }
        tableModel2.removeRow(rowIndex); // Xóa dòng cũ
        tableModel2.insertRow(0, rowData); // Chèn dòng vào đầu bảng
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
                        dsMaPDP.add(pdp.getMaPDP());
                        System.out.println(dsMaPDP);
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
    private void huyDatDichVu() {
        int[] selectedRows = table2.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một dịch vụ để hủy!");
            return;
        }

        if (selectedMaHD == null || selectedMaPhong == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một phòng trước khi hủy dịch vụ!");
            return;
        }

        for (int row : selectedRows) {
            String tenDV = tableModel2.getValueAt(row, 0).toString();
            int soLuongDV = Integer.parseInt(tableModel2.getValueAt(row, 1).toString());

            // Tìm thông tin dịch vụ từ table2 hoặc server
            Request<String> requestFind = new Request<>("TIM_DICH_VU_THEO_TEN", tenDV);
            try {
                SocketManager.send(requestFind);
                Type responseType = new TypeToken<Response<List<DichVuDTO>>>(){}.getType();
                Response<java.util.List<DichVuDTO>> responseFind = SocketManager.receive((Class<Response<List<DichVuDTO>>>) responseType);

                if (responseFind != null && responseFind.isSuccess() && !responseFind.getData().isEmpty()) {
                    DichVuDTO dichVuDTO = responseFind.getData().get(0); // Lấy dịch vụ đầu tiên
//                    dichVuDTO.setSoLuongTon(dichVuDTO.getSoLuongTon() + soLuongDV); // Tăng số lượng tồn

                    // Gửi yêu cầu cập nhật số lượng tồn
                    Request<DichVuDTO> requestUpdate = new Request<>("SUA_DICH_VU", dichVuDTO);
                    SocketManager.send(requestUpdate);
                    Response<String> responseUpdate = SocketManager.receive((Class<Response<String>>) new TypeToken<Response<String>>(){}.getType());

                    if (responseUpdate == null || !responseUpdate.isSuccess()) {
                        JOptionPane.showMessageDialog(this, "Không thể cập nhật số lượng tồn trên server!");
                        return;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin dịch vụ: " + tenDV);
                    return;
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                int option = JOptionPane.showConfirmDialog(this,
                        "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                        "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
                if (option == JOptionPane.YES_OPTION) {
                    huyDatDichVu();
                }
                return;
            }
        }

        // Xóa các hàng đã chọn khỏi giao diện
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            tableModel2.removeRow(selectedRows[i]);
        }

        JOptionPane.showMessageDialog(this, "Hủy dịch vụ thành công!");
        loadDichVuDaDat(selectedMaHD, selectedMaPhong);
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

    // Hàm load lại dịch vụ đã đặt, bỏ qua các dịch vụ có số lượng bằng 0
    private void loadDichVuDaDat(String maHD, String maPhong) {
//        try {
//            Connection connection = ConnectDB.getConnection();
//            String query = """
//            SELECT tenDV, soLuongDV, giaDV
//            FROM ChiTietHoaDon
//            INNER JOIN DichVu ON ChiTietHoaDon.maDV = DichVu.maDV
//            WHERE maHD = ? AND maPhong = ? AND soLuongDV > 0
//        """;
//            var preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, maHD);
//            preparedStatement.setString(2, maPhong);
//            var resultSet = preparedStatement.executeQuery();
//
//            // Xóa tất cả các hàng trong table trước khi load lại
//            tableModel2.setRowCount(0);
//
//            // Định dạng số
//            DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
//
//            // Thêm các dòng mới từ cơ sở dữ liệu
//            while (resultSet.next()) {
//                String tenDV = resultSet.getString("tenDV");
//                int soLuongDV = resultSet.getInt("soLuongDV");
//                double giaDV = resultSet.getDouble("giaDV"); // Lấy giá dịch vụ từ cơ sở dữ liệu
//                double thanhTien = soLuongDV * giaDV;       // Tính toán thành tiền
//
//                // Định dạng thành tiền
//                String thanhTienFormatted = decimalFormat.format(thanhTien);
//
//                // Thêm hàng vào bảng với giá trị đã định dạng
//                tableModel2.addRow(new Object[] { tenDV, soLuongDV, thanhTienFormatted });
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi khi tải dịch vụ đã đặt! Lỗi SQL: " + ex.getMessage());
//        }
    }


}
