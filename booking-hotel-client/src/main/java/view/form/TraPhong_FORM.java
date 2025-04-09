package view.form;

import utils.custom_element.*;
import dao.PhieuDatPhong_DAO;
import model.PhieuDatPhong;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;

public class TraPhong_FORM extends JPanel implements ActionListener, Openable {
    private DefaultTableModel tableModel;
    private JTable table;
    private PhieuDatPhong_DAO phieuDatPhongDAO;
    private JButton btnTraPhong;
    private ArrayList<PhieuDatPhong> dsPhieuDatPhong;
    private JTextField searchField;
    @Override
    public void open() {
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
//        dsPhieuDatPhong = phieuDatPhongDAO.getDSPhieuDatPhongDaNhan();
        loadTableData();
    }
    public TraPhong_FORM() {
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
        searchField.addActionListener(e -> handleTimKiem());
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
        JLabel titleLabel = new JLabel("Danh sách đặt trước");
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
        String[] headers = {"Mã phiếu đặt phòng", "Số phòng", "Tên khách hàng", "Ngày đặt", "Ngày đến", "Ngày đi", "Nhân viên tạo phiếu"};
        tableModel = new DefaultTableModel(headers, 0);
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
        bbutton.add(btnTraPhong = new JButton("Trả phòng"));
        btnTraPhong.setFont(FontManager.getManrope(Font.PLAIN, 16));
        btnTraPhong.setForeground(Color.WHITE);
        btnTraPhong.setBackground(new Color(66, 99, 235));
        btnTraPhong.setOpaque(false);
        btnTraPhong.setPreferredSize(new Dimension(200, 50));
        btnTraPhong.setMinimumSize(new Dimension(200, 50));
        btnTraPhong.setMaximumSize(new Dimension(200, 50));
        btnTraPhong.addActionListener(this);
        b.add(bbutton);
        add(b, BorderLayout.CENTER);
    }
    private void loadTableData(){
//        tableModel.setRowCount(0);
//        for (PhieuDatPhong pdp : dsPhieuDatPhong) {
//            tableModel.addRow(new Object[]{
//                    pdp.getMaPDP(),
//                    pdp.getPhong().getMaPhong(),
//                    pdp.getKhachHang().getHoTen(),
//                    pdp.getNgayDat(),
//                    pdp.getNgayDen(),
//                    pdp.getNgayDi(),
//                    pdp.getNhanVien().getHoTen()
//            });
//        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
//        if (e.getSource() == btnTraPhong) {
//            int selectedRow = table.getSelectedRow();
//
//            if (selectedRow == -1) {
//                JOptionPane.showMessageDialog(this, "Vui lòng chọn một phiếu đặt phòng để trả phòng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                return;
//            }
//
//
//            String maPDP = (String) tableModel.getValueAt(selectedRow, 0);
//            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn trả phòng phiếu "+maPDP+" ?", "Xác nhận", JOptionPane.YES_NO_OPTION);
//            if (confirm == JOptionPane.YES_OPTION) {
//                if (phieuDatPhongDAO.traPhong(maPDP)) {
//                    JOptionPane.showMessageDialog(this, "Đã trả phòng thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
//                    tableModel.removeRow(selectedRow);
//                } else {
//                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi trả phòng!", "Thông báo", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//
//        }
    }
    private void handleTimKiem() {
        String tuKhoa = searchField.getText().trim().toLowerCase();
        phieuDatPhongDAO = new PhieuDatPhong_DAO();
        if (tuKhoa.equals("Tìm kiếm") || tuKhoa.isEmpty()) {
            return;
        }
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String maPDP = model.getValueAt(i, 0).toString().toLowerCase();
            if (maPDP.contains(tuKhoa)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                break;
            }
        }
    }
}
