package view.form;

import utils.custom_element.*;
import dao.DichVu_DAO;
import model.DichVu;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CapNhatDichVu_FORM extends JPanel implements ActionListener, MouseListener {


    private final JTextField txtMaDV;
    private final JTextField txtTenDV;
    private final JTextField txtMoTa;
    private final JTextField txtGiaDV;
    private final JTable table;
    private final JTextField txtDVT;
    private final JTextField txtSLT;
    private  DefaultTableModel tableModel;
    private DichVu_DAO dichVuDao;

    public CapNhatDichVu_FORM() {
        dichVuDao = new DichVu_DAO();
        setBackground(new Color(16, 16, 20));
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(10));
        // Tim kiem
        JTextField txtSearch = new JTextField("Tìm kiếm tên dịch vụ");
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
                if (txtSearch.getText().equals("Tìm kiếm tên dịch vụ")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                txtSearch.setBorder(emptyBorder);
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setForeground(new Color(255, 255, 255, 125));
                    txtSearch.setText("Tìm kiếm tên dịch vụ");
                }
            }
        });
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) { // Nếu nhấn Enter
                    String keyword = txtSearch.getText().trim();
                    if (!keyword.equals("") && !keyword.equals("Tìm kiếm tên dịch vụ")) {
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
        searchPanel.add(txtSearch);
        Box searchBox = Box.createHorizontalBox();
        searchBox.add(Box.createHorizontalStrut(0));
        searchBox.add(searchPanel);
        searchBox.add(Box.createGlue());


        // Form
        Box b1 = Box.createVerticalBox();
        Box b2 = Box.createHorizontalBox();
        b2.add(createFormBox("Mã dịch vụ", txtMaDV = new JTextField()));
        b2.add(createFormBox("Tên dịch vụ", txtTenDV = new JTextField()));
        b2.add(createFormBox("Gíá dịch vụ", txtGiaDV = new JTextField()));
        b2.add(createFormBox("Đơn vị tính", txtDVT = new JTextField()));
        b2.add(createFormBox("Số lượng tồn", txtSLT = new JTextField()));


        Dimension b2Size = new Dimension(1642, 100);
        b2.setPreferredSize(b2Size);
        b2.setMinimumSize(b2Size);
        b2.setMaximumSize(b2Size);


        Box b3 = Box.createHorizontalBox();
        Box b4 = Box.createHorizontalBox();

        RoundedButton btnThem = createHandleButton("Thêm");
        RoundedButton btnSua = createHandleButton("Sửa");
        RoundedButton btnXoa = createHandleButton("Xóa");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");

        b4.add(Box.createHorizontalGlue());
        b4.add(createFormBox("Mô tả", txtMoTa = new JTextField()));
        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnThem);
        b4.add(Box.createHorizontalStrut(50));
        b4.add(btnSua);
        b4.add(Box.createHorizontalStrut(50));
        b4.add(btnXoa);
        b4.add(Box.createHorizontalStrut(50));
        b4.add(btnLamMoi);
        b4.add(Box.createHorizontalStrut(55));


        b3.add(b4);


        b1.add(b2);
        b1.add(b3);


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


        // Tạo bang
        Box b6 = Box.createHorizontalBox();
        String[] colName = {"Mã dịch vụ", "Tên dịch vụ", "Mô tả", "Giá dịch vụ", "Đơn vị tính", "Số lượng tồn"};
        tableModel = new DefaultTableModel(colName, 0) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JScrollPane scroll;
        b6.add(scroll = new JScrollPane(table = new JTable(tableModel), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
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


        mainBox.add(searchBox);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(b1);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(titlePanel);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(b6);
        add(mainBox);


        table.addMouseListener(this);
        loadTableData();


    }

    private Box createFormBox(String label, JTextField txt) {
        Box b = Box.createVerticalBox();
        Dimension boxSize = new Dimension(332, 110);
        b.setPreferredSize(boxSize);
        b.setMaximumSize(boxSize);
        b.setMaximumSize(boxSize);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FontManager.getManrope(Font.PLAIN, 15));
        lbl.setForeground(Color.WHITE);

        Box lblBox = Box.createHorizontalBox();
        lblBox.setPreferredSize(new Dimension(255, 20));
        lblBox.setMaximumSize(new Dimension(255, 20));
        lblBox.setMinimumSize(new Dimension(255, 20));
        lblBox.add(lbl);

        txt.setBackground(new Color(40, 40, 44));
        Dimension txtFieldSize = new Dimension(260, 45);
        txt.setPreferredSize(txtFieldSize);
        txt.setMaximumSize(txtFieldSize);
        txt.setMinimumSize(txtFieldSize);
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


        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(txt);
        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 72));

        return b;
    }
    private RoundedButton createHandleButton(String buttonLabel) {
        RoundedButton button = new RoundedButton(buttonLabel, 5);
        Dimension buttonSize = new Dimension(259, 45);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        button.setMinimumSize(buttonSize);
        Color defaultBackground = new Color(80, 80, 88);
        Color hoverBackground = new Color(89, 98, 136);

        button.setBackground(defaultBackground);
        button.setForeground(Color.white);
        button.setFont(FontManager.getManrope(Font.PLAIN, 16));
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.addActionListener(this);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverBackground);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(defaultBackground);
            }
        });

        return button;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) { // Kiểm tra xem có hàng nào được chọn không
            txtMaDV.setText(tableModel.getValueAt(selectedRow,0).toString());
            txtTenDV.setText(tableModel.getValueAt(selectedRow,1).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow,2).toString());
            txtGiaDV.setText(tableModel.getValueAt(selectedRow,3).toString());
            txtDVT.setText(tableModel.getValueAt(selectedRow,4).toString());
            txtSLT.setText(tableModel.getValueAt(selectedRow,5).toString());


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

    private void loadTableData() {
//        // Xóa tất cả các dòng trong bảng trước khi tải dữ liệu mới
//        tableModel.setRowCount(0);
//
//        ArrayList<DichVu> dsDichVu = dichVuDao.getDSDichVu();
//        for (DichVu dv: dsDichVu) {
//            if (dv.getTrangThai() == 1) { // Chỉ thêm khách hàng có trạng thái khác 0
//                tableModel.addRow(new Object[]{
//                        dv.getMaDV(),
//                        dv.getTenDV(),
//                        dv.getMoTa(),
//                        dv.getGiaDV(),
//                        dv.getDonViTinh(),
//                        dv.getSoLuongTon()
//                });
//            }
//        }
    }
    private void timKiem(String keyword) {
//        table.clearSelection(); // Xóa lựa chọn cũ trên bảng
//
//        int foundRow = -1;
//
//        // Tìm dòng chứa từ khóa đầu tiên
//        for (int i = 0; i < tableModel.getRowCount(); i++) {
//            boolean match = false;
//
//            // Kiểm tra từ khóa trong các cột
//            for (int j = 0; j < tableModel.getColumnCount(); j++) {
//                if (tableModel.getValueAt(i, j).toString().toLowerCase().contains(keyword.toLowerCase())) {
//                    match = true;
//                    break;
//                }
//            }
//
//            if (match) {
//                foundRow = i;
//                break; // Chỉ lấy dòng đầu tiên tìm thấy
//            }
//        }
//
//        if (foundRow != -1) {
//            // Di chuyển dòng tìm thấy lên đầu bảng
//            moveRowToTop(foundRow);
//
//            // Điền thông tin từ dòng được tìm thấy vào các ô nhập liệu
//            txtMaDV.setText(tableModel.getValueAt(0,0).toString());
//            txtTenDV.setText(tableModel.getValueAt(0, 1).toString());
//            txtMoTa.setText(tableModel.getValueAt(0, 2).toString());
//            txtGiaDV.setText(tableModel.getValueAt(0, 3).toString());
//
//
//            // Tạo hiệu ứng hover cho dòng đầu tiên
//            table.addRowSelectionInterval(0, 0);
//
//        } else {
//            JOptionPane.showMessageDialog(this, "Không tìm thấy kết quả nào!");
//        }
    }
    private void moveRowToTop(int rowIndex) {
//        Object[] rowData = new Object[tableModel.getColumnCount()];
//        for (int i = 0; i < tableModel.getColumnCount(); i++) {
//            rowData[i] = tableModel.getValueAt(rowIndex, i);
//        }
//        tableModel.removeRow(rowIndex); // Xóa dòng cũ
//        tableModel.insertRow(0, rowData); // Chèn dòng vào đầu bảng
    }
    public void lamMoi(){
        txtMaDV.setText("");
        txtTenDV.setText("");
        txtMoTa.setText("");
        txtGiaDV.setText("");
        txtDVT.setText("");
        txtSLT.setText("");
        txtMaDV.requestFocus();
        loadTableData();
    }
    private void themDichVu() {
//        try {
//            // Lấy thông tin từ các trường nhập liệu
//            String maDV = txtMaDV.getText().trim();
//            String tenDV = txtTenDV.getText().trim();
//            String moTa = txtMoTa.getText().trim();
//            String giaDVText = txtGiaDV.getText().trim();
//            String donVT = txtDVT.getText().trim();
//            int soLT = Integer.parseInt(txtSLT.getText().trim());
//
//            // Gọi hàm kiểm tra đầu vào
//            if (!kiemTraDauVao(maDV, tenDV, moTa, giaDVText)) {
//                return; // Dừng nếu dữ liệu không hợp lệ
//            }
//
//            // Kiểm tra trùng mã dịch vụ
//            if (dichVuDao.kiemTraTrungMaDichVu(maDV)) {
//                JOptionPane.showMessageDialog(this, "Mã dịch vụ đã tồn tại. Vui lòng nhập mã khác!");
//                return; // Dừng nếu mã dịch vụ đã tồn tại
//            }
//
//            double giaDV = Double.parseDouble(giaDVText); // Giá trị đã được kiểm tra
//
//            // Tạo đối tượng DichVu
//            DichVu dv = new DichVu(maDV, tenDV, giaDV, donVT, soLT, moTa, 1);
//
//            // Gọi hàm thêm dịch vụ từ lớp xử lý
//            boolean isAdded = dichVuDao.themDichVu(dv);
//
//            // Kiểm tra kết quả và thông báo
//            if (isAdded) {
//                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thành công!");
//                lamMoi(); // Hàm xóa dữ liệu nhập sau khi thêm thành công
//            } else {
//                JOptionPane.showMessageDialog(this, "Thêm dịch vụ thất bại. Vui lòng thử lại!");
//            }
//        } catch (Exception e) {
//            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage());
//        }
    }

    private void xoaDichVu() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String maDV = (String) tableModel.getValueAt(selectedRow, 0);
//            try {
//                if (dichVuDao.xoaDV(maDV)) {
//                    tableModel.removeRow(selectedRow); // Xóa dòng khỏi bảng
//                    lamMoi();
//                    JOptionPane.showMessageDialog(this, "Xóa dịch vụ thành công!");
//
//                    // Đặt lại trạng thái lựa chọn của bảng
//                    table.clearSelection();
//                } else {
//                    JOptionPane.showMessageDialog(this, "Xóa dịch vụ thất bại!");
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi xóa dịch vụ!");
//            }
//        }else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để xóa!");
//        }
    }
    private void suaDichVu() {
//        int selectedRow = table.getSelectedRow();
//        if (selectedRow != -1) {
//            String maDV = (String) tableModel.getValueAt(selectedRow, 0); // Lấy mã dịch vụ từ bảng
//
//            String tenDV = txtTenDV.getText().trim();
//            String moTa = txtMoTa.getText().trim();
//            String giaDVText = txtGiaDV.getText().trim();
//            String donVT = txtDVT.getText().trim();
//            int soLT = Integer.parseInt(txtSLT.getText().trim());
//
//            // Gọi hàm kiểm tra đầu vào
//            if (!kiemTraDauVao(maDV, tenDV, moTa, giaDVText)) {
//                return; // Dừng nếu dữ liệu không hợp lệ
//            }
//
//            double giaDV = Double.parseDouble(giaDVText); // Giá trị đã được kiểm tra
//
//            try {
//                // Tạo đối tượng DichVu mới
//                DichVu dv = new DichVu(maDV, tenDV, giaDV, donVT, soLT, moTa, 1);
//
//                if (dichVuDao.suaDichVu(dv)) {
//                    // Cập nhật dữ liệu trên bảng
//                    tableModel.setValueAt(tenDV, selectedRow, 1);
//                    tableModel.setValueAt(moTa, selectedRow, 2);
//                    tableModel.setValueAt(giaDV, selectedRow, 3);
//                    lamMoi();
//
//                    JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
//                } else {
//                    JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thất bại!");
//                }
//            } catch (Exception e) {
//                JOptionPane.showMessageDialog(this, "Đã xảy ra lỗi khi cập nhật dịch vụ: " + e.getMessage());
//            }
//        } else {
//            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
//        }
    }

    private boolean kiemTraDauVao(String maDV, String tenDV, String moTa, String giaDVText) {
//        // Lấy năm hiện tại
//        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
//        int lastTwoDigitsOfYear = currentYear % 100;
//
//        // Kiểm tra mã dịch vụ (2 số đầu và 4 ký tự tiếp theo là chữ in hoa hoặc số)
//        if (!maDV.matches("^[0-9]{2}[A-Z0-9]{4}$")) {
//            JOptionPane.showMessageDialog(this, "Mã dịch vụ phải có 2 ký tự đầu là số, 4 ký tự sau là chữ in hoa hoặc số!");
//            return false;
//        }
//        int firstTwoDigits = Integer.parseInt(maDV.substring(0, 2));
//        if (firstTwoDigits > lastTwoDigitsOfYear) {
//            JOptionPane.showMessageDialog(this, "Hai ký tự đầu của mã dịch vụ không được lớn hơn 2 số cuối của năm hiện tại!");
//            return false;
//        }
//
//        // Kiểm tra tên dịch vụ
//        if (tenDV == null || tenDV.isEmpty() || tenDV.length() > 100) {
//            JOptionPane.showMessageDialog(this, "Tên dịch vụ không hợp lệ! (Không được để trống, tối đa 100 ký tự)");
//            return false;
//        }
//
//        // Kiểm tra mô tả dịch vụ
//        if (moTa == null || moTa.isEmpty() || moTa.length() > 200) {
//            JOptionPane.showMessageDialog(this, "Mô tả dịch vụ không hợp lệ! (Không được để trống, tối đa 200 ký tự)");
//            return false;
//        }
//
//        // Kiểm tra giá dịch vụ
//        try {
//            double giaDV = Double.parseDouble(giaDVText);
//            if (giaDV <= 0) {
//                JOptionPane.showMessageDialog(this, "Giá dịch vụ phải lớn hơn 0!");
//                return false;
//            }
//        } catch (NumberFormatException e) {
//            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
//            return false;
//        }
//
//        return true; // Dữ liệu hợp lệ
    return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RoundedButton btn = (RoundedButton) e.getSource();

        if (btn.getText().equals("Sửa")) {

            suaDichVu();
        } else if (btn.getText().equals("Xóa")) {
           xoaDichVu();
        } else if (btn.getText().equals("Làm mới")) {
            lamMoi();
        } else if(btn.getText().equals("Thêm")){
            themDichVu();
        }
    }
}
