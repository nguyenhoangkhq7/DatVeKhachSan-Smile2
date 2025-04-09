package view.form;

import utils.custom_element.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class ThongKeDoanhThu_FORM extends JPanel implements ActionListener {

    private final JComboBox<Object> cbNam;
    private final JRadioButton radThang;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JRadioButton radNam;

    public ThongKeDoanhThu_FORM(){

        setBackground(new Color(16, 16, 20));
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(10));

        // Form
        Box b1 = Box.createVerticalBox();
        Box b2 = Box.createHorizontalBox();




        b2.add(createFormBox("Năm",cbNam = new JComboBox<>()));
        for (int i = 2022; i <= 2024; i++) {
            cbNam.addItem(i);
        }
        b2.add(createFormBox1("Theo năm",radNam = new JRadioButton()));
        b2.add(createFormBox1("Theo tháng",radThang = new JRadioButton()));

        ButtonGroup group = new ButtonGroup();
        group.add(radNam);
        group.add(radThang);
        Dimension b2Size = new Dimension(1642, 100);
        b2.setPreferredSize(b2Size);
        b2.setMinimumSize(b2Size);
        b2.setMaximumSize(b2Size);


        Box b3 = Box.createHorizontalBox();
        Box b4 = Box.createHorizontalBox();


        RoundedButton btnThongKe = createHandleButton("Thống kê");
        RoundedButton btnLamMoi = createHandleButton("Làm mới");
        RoundedButton btnExport = createHandleButton("Xuất báo cáo");


        b4.add(Box.createHorizontalGlue());

        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnThongKe);
        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnExport);
        b4.add(Box.createHorizontalStrut(72));
        b4.add(btnLamMoi);
        b4.add(Box.createHorizontalStrut(55));


        b3.add(b4);


        b1.add(b2);
        b1.add(b3);


        // Tieu de
        JLabel titleLabel = new JLabel("Thống kê doanh thu");
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
        String[] colName = {"STT", "Thời gian", "Doanh thu dịch vụ", "Doanh thu phòng", "Tổng doanh thu"};
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



        mainBox.add(b1);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(titlePanel);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(b6);
        add(mainBox);


        //loadTableData();


    }
    private Box createFormBox(String label,  JComboBox jbc) {
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




        jbc.setBackground(new Color(40, 40, 44));
        Dimension jbcFieldSize = new Dimension(260, 45);
        jbc.setPreferredSize(jbcFieldSize);
        jbc.setMaximumSize(jbcFieldSize);
        jbc.setMinimumSize(jbcFieldSize);
        jbc.setForeground(Color.white);
        jbc.setFont(FontManager.getManrope(Font.PLAIN, 14));
        Border jbcemptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
        CompoundBorder jbccombinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), jbcemptyBorder);
        jbc.setBorder(jbcemptyBorder);
        jbc.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                jbc.setBorder(jbccombinedBorder);
            }

            @Override
            public void focusLost(FocusEvent e) {
                jbc.setBorder(jbcemptyBorder);
            }
        });


        b.add(lblBox);
        b.add(Box.createVerticalStrut(6));
        b.add(jbc);

        b.setBorder(BorderFactory.createEmptyBorder(0, 0, 35, 72));

        return b;
    }

private Box createFormBox1(String label, JRadioButton rad){
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

    rad.setBackground(new Color(40, 40, 44));
    Dimension radFieldSize = new Dimension(260, 45);
    rad.setPreferredSize(radFieldSize);
    rad.setMaximumSize(radFieldSize);
    rad.setMinimumSize(radFieldSize);
    rad.setForeground(Color.white);
    rad.setFont(FontManager.getManrope(Font.PLAIN, 14));
    Border rademptyBorder = BorderFactory.createEmptyBorder(0, 10, 0, 0);
    CompoundBorder radcombinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), rademptyBorder);
    rad.setBorder(rademptyBorder);
    rad.addFocusListener(new FocusAdapter() {
        @Override
        public void focusGained(FocusEvent e) {
            rad.setBorder(radcombinedBorder);
        }

        @Override
        public void focusLost(FocusEvent e) {
            rad.setBorder(rademptyBorder);
        }
    });
    b.add(lblBox);
    b.add(Box.createVerticalStrut(6));
    b.add(rad);
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
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source instanceof RoundedButton) {
            RoundedButton button = (RoundedButton) source;
            if ("Thống kê".equals(button.getText())) {
                if (radNam.isSelected()) {
                    // Thống kê theo năm
                    loadRevenueDataByYear();
                } else if (radThang.isSelected()) {
                    // Kiểm tra xem năm đã được chọn chưa
                    Object selectedYear = cbNam.getSelectedItem();
                    if (selectedYear == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn năm trước khi thống kê theo tháng.");
                    } else {
                        // Thống kê theo tháng
                        loadRevenueDataByMonth((int) selectedYear);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn loại thống kê (theo năm hoặc theo tháng).");
                }
            } else if ("Làm mới".equals(button.getText())) {
                // Làm mới giao diện
                resetForm();
            }else if ("Xuất báo cáo".equals(button.getText())) {
                exportReport();
            }

        }
    }



    private void loadRevenueDataByYear() {
//        String query = "SELECT YEAR(hd.ngayLapHD) AS Nam, " +
//                "SUM(CASE WHEN dv.giaDV IS NOT NULL THEN cthd.soLuongDV * dv.giaDV ELSE 0 END) AS DoanhThuDichVu, " +
//                "SUM(CASE WHEN p.giaPhong IS NOT NULL THEN DATEDIFF(DAY, hd.ngayNhanPhong, hd.ngayTraPhong) * p.giaPhong ELSE 0 END) AS DoanhThuPhong " +
//                "FROM HoaDon hd " +
//                "LEFT JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
//                "LEFT JOIN DichVu dv ON cthd.maDV = dv.maDV " +
//                "LEFT JOIN Phong p ON hd.maPhong = p.maPhong " +
//                "GROUP BY YEAR(hd.ngayLapHD)";
//        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(query);
//             ResultSet rs = stmt.executeQuery()) {
//
//            tableModel.setRowCount(0); // Làm mới bảng
//            DecimalFormat df = new DecimalFormat("#,###.00"); // Định dạng số với 2 chữ số thập phân
//
//            while (rs.next()) {
//                int year = rs.getInt("Nam");
//                double revenueService = rs.getDouble("DoanhThuDichVu");
//                double revenueRoom = rs.getDouble("DoanhThuPhong");
//                double totalRevenue = revenueService + revenueRoom;
//
//                // Định dạng doanh thu để tránh giá trị "E"
//                String formattedRevenueService = df.format(revenueService);
//                String formattedRevenueRoom = df.format(revenueRoom);
//                String formattedTotalRevenue = df.format(totalRevenue);
//
//                tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, year, formattedRevenueService, formattedRevenueRoom, formattedTotalRevenue});
//            }
//            table.repaint(); // Cập nhật giao diện bảng
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
//        }
    }

    private void loadRevenueDataByMonth(int year) {
//        String query = "SELECT MONTH(hd.ngayLapHD) AS Thang, " +
//                "SUM(CASE WHEN dv.giaDV IS NOT NULL THEN cthd.soLuongDV * dv.giaDV ELSE 0 END) AS DoanhThuDichVu, " +
//                "SUM(CASE WHEN p.giaPhong IS NOT NULL THEN DATEDIFF(DAY, hd.ngayNhanPhong, hd.ngayTraPhong) * p.giaPhong ELSE 0 END) AS DoanhThuPhong " +
//                "FROM HoaDon hd " +
//                "LEFT JOIN ChiTietHoaDon cthd ON hd.maHD = cthd.maHD " +
//                "LEFT JOIN DichVu dv ON cthd.maDV = dv.maDV " +
//                "LEFT JOIN Phong p ON hd.maPhong = p.maPhong " +
//                "WHERE YEAR(hd.ngayLapHD) = ? " +
//                "GROUP BY MONTH(hd.ngayLapHD)";
//        try (PreparedStatement stmt = ConnectDB.getConnection().prepareStatement(query)) {
//
//            stmt.setInt(1, year);
//            try (ResultSet rs = stmt.executeQuery()) {
//                tableModel.setRowCount(0); // Làm mới bảng
//                DecimalFormat df = new DecimalFormat("#,###.00"); // Định dạng số với 2 chữ số thập phân
//
//                while (rs.next()) {
//                    int month = rs.getInt("Thang");
//                    double revenueService = rs.getDouble("DoanhThuDichVu");
//                    double revenueRoom = rs.getDouble("DoanhThuPhong");
//                    double totalRevenue = revenueService + revenueRoom;
//
//                    // Định dạng doanh thu để tránh giá trị "E"
//                    String formattedRevenueService = df.format(revenueService);
//                    String formattedRevenueRoom = df.format(revenueRoom);
//                    String formattedTotalRevenue = df.format(totalRevenue);
//
//                    tableModel.addRow(new Object[]{tableModel.getRowCount() + 1, "Tháng " + month, formattedRevenueService, formattedRevenueRoom, formattedTotalRevenue});
//                }
//                table.repaint(); // Cập nhật giao diện bảng
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu: " + ex.getMessage());
//        }
    }

    private void resetForm() {
        // Xóa dữ liệu bảng
        tableModel.setRowCount(0);

        // Đặt lại radio button
        radNam.setSelected(false);
        radThang.setSelected(false);

        // Đặt lại combo box năm về trạng thái mặc định (chọn mục đầu tiên)
        if (cbNam.getItemCount() > 0) {
            cbNam.setSelectedIndex(0);
        }

        // Hiển thị thông báo (tùy chọn)
        JOptionPane.showMessageDialog(this, "Form đã được làm mới.");
    }

    private void exportReport() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu báo cáo");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = new File(fileChooser.getSelectedFile() + ".xlsx");
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Thống kê doanh thu");

                // Ghi tiêu đề cột
                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                // Ghi dữ liệu từ bảng
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

                // Lưu file Excel
                try (FileOutputStream fileOut = new FileOutputStream(fileToSave)) {
                    workbook.write(fileOut);
                    JOptionPane.showMessageDialog(this, "Báo cáo đã được lưu thành công!");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi lưu file: " + ex.getMessage());
            }
        }
    }

}




