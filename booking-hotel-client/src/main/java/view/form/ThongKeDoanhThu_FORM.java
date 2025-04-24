package view.form;

import model.Request;
import model.Response;
import socket.SocketManager;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;

public class ThongKeDoanhThu_FORM extends JPanel implements ActionListener {

    private final JComboBox<Object> cbNam;
    private final JRadioButton radThang;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final JRadioButton radNam;

    public ThongKeDoanhThu_FORM() {
        setBackground(new Color(16, 16, 20));
        Box mainBox = Box.createVerticalBox();
        mainBox.add(Box.createVerticalStrut(10));

        // Form
        Box b1 = Box.createVerticalBox();
        Box b2 = Box.createHorizontalBox();

        b2.add(createFormBox("Năm", cbNam = new JComboBox<>()));
        // Tải danh sách năm từ dữ liệu doanh thu
        loadYearsFromRevenueData();

        b2.add(createFormBox1("Theo năm", radNam = new JRadioButton()));
        b2.add(createFormBox1("Theo tháng", radThang = new JRadioButton()));

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

        scroll.setPreferredSize(new Dimension(1642, 800));
        scroll.setBorder(null);
        scroll.getViewport().setOpaque(false);
        scroll.setViewportBorder(null);
        scroll.getViewport().setBackground(Color.DARK_GRAY);

        mainBox.add(b1);
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(titlePanel);
        mainBox.add(Box.createVerticalStrut(5));
        mainBox.add(b6);
        add(mainBox);

        btnThongKe.addActionListener(this);
        btnLamMoi.addActionListener(this);
        btnExport.addActionListener(this);
    }

    private void loadYearsFromRevenueData() {
        Set<Integer> years = new TreeSet<>(); // Sử dụng TreeSet để lưu các năm duy nhất và sắp xếp

        try {
            // Lấy dữ liệu doanh thu phòng
            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_PHONG để lấy năm...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_PHONG", null));
            Response<?> resPhong = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_PHONG: " + resPhong);
            if (resPhong != null && resPhong.isSuccess()) {
                List<?> dsPhong = (List<?>) resPhong.getData();
                System.out.println("Dữ liệu phòng: " + dsPhong);
                if (dsPhong.isEmpty()) {
                    System.out.println("Danh sách phòng rỗng!");
                }
                for (Object rowObj : dsPhong) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue(); // Lấy năm
                    years.add(nam); // Thêm năm vào tập hợp
                    System.out.println("Năm từ phòng: " + nam);
                }
            } else {
                String errorMsg = resPhong != null && resPhong.getData() != null ? resPhong.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu phòng. Lỗi: " + errorMsg);
            }

            // Lấy dữ liệu doanh thu dịch vụ
            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_DICH_VU để lấy năm...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_DICH_VU", null));
            Response<?> resDV = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_DICH_VU: " + resDV);
            if (resDV != null && resDV.isSuccess()) {
                List<?> dsDV = (List<?>) resDV.getData();
                System.out.println("Dữ liệu dịch vụ: " + dsDV);
                if (dsDV.isEmpty()) {
                    System.out.println("Danh sách dịch vụ rỗng!");
                }
                for (Object rowObj : dsDV) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue(); // Lấy năm
                    years.add(nam); // Thêm năm vào tập hợp
                    System.out.println("Năm từ dịch vụ: " + nam);
                }
            } else {
                String errorMsg = resDV != null && resDV.getData() != null ? resDV.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu dịch vụ. Lỗi: " + errorMsg);
            }

            // Cập nhật JComboBox
            if (years.isEmpty()) {
                System.out.println("Không tìm thấy năm nào trong dữ liệu doanh thu!");
                JOptionPane.showMessageDialog(this, "Không có năm nào trong cơ sở dữ liệu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                // Thêm năm mặc định
                cbNam.addItem(2025);
            } else {
                cbNam.removeAllItems(); // Xóa các mục hiện tại
                for (Integer year : years) {
                    cbNam.addItem(year); // Thêm các năm vào JComboBox
                }
                // Chọn năm mới nhất
                cbNam.setSelectedIndex(cbNam.getItemCount() - 1);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối khi tải danh sách năm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            // Thêm năm mặc định nếu có lỗi
            cbNam.addItem(2025);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xử lý dữ liệu năm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            // Thêm năm mặc định nếu có lỗi
            cbNam.addItem(2025);
        }
    }

    private Box createFormBox(String label, JComboBox jbc) {
        Box b = Box.createVerticalBox();
        Dimension boxSize = new Dimension(332, 110);
        b.setPreferredSize(boxSize);
        b.setMaximumSize(boxSize);
        b.setMinimumSize(boxSize);

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

    private Box createFormBox1(String label, JRadioButton rad) {
        Box b = Box.createVerticalBox();
        Dimension boxSize = new Dimension(332, 110);
        b.setPreferredSize(boxSize);
        b.setMaximumSize(boxSize);
        b.setMinimumSize(boxSize);

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
            System.out.println("Nút được nhấn: " + button.getText() + ", radNam: " + radNam.isSelected() + ", radThang: " + radThang.isSelected());
            if ("Thống kê".equals(button.getText())) {
                if (radNam.isSelected()) {
                    loadRevenueDataByYear();
                } else if (radThang.isSelected()) {
                    Object selectedYear = cbNam.getSelectedItem();
                    if (selectedYear == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn năm trước khi thống kê theo tháng.");
                    } else {
                        loadRevenueDataByMonth((int) selectedYear);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn loại thống kê (theo năm hoặc theo tháng).");
                }
            } else if ("Làm mới".equals(button.getText())) {
                resetForm();
            } else if ("Xuất báo cáo".equals(button.getText())) {
                exportReport();
            }
        }
    }

    private void loadRevenueDataByYear() {
        tableModel.setRowCount(0);
        System.out.println("Bắt đầu tải dữ liệu doanh thu theo năm...");

        Map<Integer, Double> phongMap = new HashMap<>();
        Map<Integer, Double> dvMap = new HashMap<>();

        try {
            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_PHONG...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_PHONG", null));
            Response<?> resPhong = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_PHONG: " + resPhong);
            if (resPhong != null && resPhong.isSuccess()) {
                List<?> dsPhong = (List<?>) resPhong.getData();
                System.out.println("Dữ liệu phòng: " + dsPhong);
                if (dsPhong.isEmpty()) {
                    System.out.println("Danh sách phòng rỗng!");
                }
                for (Object rowObj : dsPhong) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue();
                    double dtPhong = ((Number) row.get(2)).doubleValue();
                    phongMap.put(nam, phongMap.getOrDefault(nam, 0.0) + dtPhong);
                    System.out.println("Hàng phòng: Năm " + nam + ", " + dtPhong);
                }
            } else {
                String errorMsg = resPhong != null && resPhong.getData() != null ? resPhong.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu phòng. Lỗi: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi tải doanh thu phòng: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_DICH_VU...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_DICH_VU", null));
            Response<?> resDV = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_DICH_VU: " + resDV);
            if (resDV != null && resDV.isSuccess()) {
                List<?> dsDV = (List<?>) resDV.getData();
                System.out.println("Dữ liệu dịch vụ: " + dsDV);
                if (dsDV.isEmpty()) {
                    System.out.println("Danh sách dịch vụ rỗng!");
                }
                for (Object rowObj : dsDV) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue();
                    double dtDV = ((Number) row.get(2)).doubleValue();
                    dvMap.put(nam, dvMap.getOrDefault(nam, 0.0) + dtDV);
                    System.out.println("Hàng dịch vụ: Năm " + nam + ", " + dtDV);
                }
            } else {
                String errorMsg = resDV != null && resDV.getData() != null ? resDV.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu dịch vụ. Lỗi: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi tải doanh thu dịch vụ: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            Set<Integer> tatCaNam = new TreeSet<>();
            tatCaNam.addAll(phongMap.keySet());
            tatCaNam.addAll(dvMap.keySet());
            System.out.println("Tất cả năm: " + tatCaNam);

            DecimalFormat df = new DecimalFormat("#,###");
            int stt = 1;

            for (Integer nam : tatCaNam) {
                double dtPhong = phongMap.getOrDefault(nam, 0.0);
                double dtDV = dvMap.getOrDefault(nam, 0.0);
                double tong = dtPhong + dtDV;

                tableModel.addRow(new Object[]{
                        stt++,
                        String.valueOf(nam),
                        df.format(dtDV),
                        df.format(dtPhong),
                        df.format(tong)
                });
                System.out.println("Đã thêm hàng: Năm " + nam);
            }

            System.out.println("Số hàng trong bảng: " + tableModel.getRowCount());
            table.repaint();
            table.revalidate();

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xử lý dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRevenueDataByMonth(int year) {
        tableModel.setRowCount(0);
        System.out.println("Bắt đầu tải dữ liệu doanh thu theo tháng cho năm: " + year);

        Map<String, Double> phongMap = new HashMap<>();
        Map<String, Double> dvMap = new HashMap<>();

        try {
            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_PHONG...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_PHONG", null));
            Response<?> resPhong = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_PHONG: " + resPhong);
            if (resPhong != null && resPhong.isSuccess()) {
                List<?> ds = (List<?>) resPhong.getData();
                System.out.println("Dữ liệu phòng: " + ds);
                if (ds.isEmpty()) {
                    System.out.println("Danh sách phòng rỗng!");
                }
                for (Object rowObj : ds) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue();
                    int thang = ((Number) row.get(1)).intValue();
                    String thangNam = nam + "/" + thang;
                    if (nam == year) {
                        double dtPhong = ((Number) row.get(2)).doubleValue();
                        phongMap.put(thangNam, dtPhong);
                        System.out.println("Hàng phòng: " + thangNam + ", " + dtPhong);
                    }
                }
            } else {
                String errorMsg = resPhong != null && resPhong.getData() != null ? resPhong.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu phòng. Lỗi: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi tải doanh thu phòng: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            System.out.println("Gửi yêu cầu THONG_KE_DOANH_THU_DICH_VU...");
            SocketManager.send(new Request<>("THONG_KE_DOANH_THU_DICH_VU", null));
            Response<?> resDV = SocketManager.receive(Response.class);
            System.out.println("Phản hồi THONG_KE_DOANH_THU_DICH_VU: " + resDV);
            if (resDV != null && resDV.isSuccess()) {
                List<?> ds = (List<?>) resDV.getData();
                System.out.println("Dữ liệu dịch vụ: " + ds);
                if (ds.isEmpty()) {
                    System.out.println("Danh sách dịch vụ rỗng!");
                }
                for (Object rowObj : ds) {
                    List<?> row = (List<?>) rowObj;
                    int nam = ((Number) row.get(0)).intValue();
                    int thang = ((Number) row.get(1)).intValue();
                    String thangNam = nam + "/" + thang;
                    if (nam == year) {
                        double dtDV = ((Number) row.get(2)).doubleValue();
                        dvMap.put(thangNam, dtDV);
                        System.out.println("Hàng dịch vụ: " + thangNam + ", " + dtDV);
                    }
                }
            } else {
                String errorMsg = resDV != null && resDV.getData() != null ? resDV.getData().toString() : "Không nhận được dữ liệu từ server";
                System.out.println("Không nhận được dữ liệu dịch vụ. Lỗi: " + errorMsg);
                JOptionPane.showMessageDialog(this, "Lỗi tải doanh thu dịch vụ: " + errorMsg, "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            Set<String> tatCaThang = new TreeSet<>((a, b) -> {
                int thangA = Integer.parseInt(a.split("/")[1]);
                int thangB = Integer.parseInt(b.split("/")[1]);
                return Integer.compare(thangA, thangB);
            });

            tatCaThang.addAll(phongMap.keySet());
            tatCaThang.addAll(dvMap.keySet());
            System.out.println("Tất cả tháng: " + tatCaThang);

            DecimalFormat df = new DecimalFormat("#,###");
            int stt = 1;

            for (String thangNam : tatCaThang) {
                double dtPhong = phongMap.getOrDefault(thangNam, 0.0);
                double dtDV = dvMap.getOrDefault(thangNam, 0.0);
                double tong = dtPhong + dtDV;

                tableModel.addRow(new Object[]{
                        stt++,
                        thangNam,
                        df.format(dtDV),
                        df.format(dtPhong),
                        df.format(tong)
                });
                System.out.println("Đã thêm hàng: " + thangNam);
            }

            System.out.println("Số hàng trong bảng: " + tableModel.getRowCount());
            table.repaint();
            table.revalidate();

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu để hiển thị cho năm " + year + ".", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xử lý dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        tableModel.setRowCount(0);
        radNam.setSelected(false);
        radThang.setSelected(false);
        if (cbNam.getItemCount() > 0) {
            cbNam.setSelectedIndex(0);
        }
        System.out.println("Form đã được làm mới, số hàng: " + tableModel.getRowCount());
        JOptionPane.showMessageDialog(this, "Form đã được làm mới.");
        table.repaint();
        table.revalidate();
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

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < tableModel.getColumnCount(); i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(tableModel.getColumnName(i));
                }

                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    Row row = sheet.createRow(i + 1);
                    for (int j = 0; j < tableModel.getColumnCount(); j++) {
                        Object value = tableModel.getValueAt(i, j);
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }

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