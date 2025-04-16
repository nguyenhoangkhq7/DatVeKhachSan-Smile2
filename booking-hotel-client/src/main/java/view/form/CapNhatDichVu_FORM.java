package view.form;

import com.google.gson.reflect.TypeToken;
import dto.DichVuDTO;
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
import java.util.ArrayList;

public class CapNhatDichVu_FORM extends JPanel implements ActionListener, MouseListener {

    private final JTextField txtMaDV;
    private final JTextField txtTenDV;
    private final JTextField txtMoTa;
    private final JTextField txtGiaDV;
    private final JTable table;
    private final JTextField txtDVT;
    private final JTextField txtSLT;
    private DefaultTableModel tableModel;

    public CapNhatDichVu_FORM() {
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
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
        JLabel titleLabel = new JLabel("Danh sách dịch vụ");
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
        if (selectedRow != -1) {
            txtMaDV.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenDV.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtGiaDV.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtDVT.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtSLT.setText(tableModel.getValueAt(selectedRow, 5).toString());
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    private void loadTableData() {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void timKiem(String keyword) {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void moveRowToTop(int rowIndex) {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    public void lamMoi() {
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
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void xoaDichVu() {
        // (Giữ nguyên hoặc tích hợp với server nếu cần)
    }

    private void suaDichVu() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng để sửa!");
            return;
        }

        String maDV = tableModel.getValueAt(selectedRow, 0).toString();
        String tenDV = txtTenDV.getText().trim();
        String moTa = txtMoTa.getText().trim();
        String giaDVText = txtGiaDV.getText().trim();
        String donVT = txtDVT.getText().trim();
        String soLTText = txtSLT.getText().trim();

        if (!kiemTraDauVao(maDV, tenDV, moTa, giaDVText)) {
            return;
        }

        double giaDV;
        int soLT;
        try {
            giaDV = Double.parseDouble(giaDVText);
            soLT = Integer.parseInt(soLTText);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ hoặc số lượng tồn phải là số hợp lệ!");
            return;
        }

        DichVuDTO dichVuDTO = new DichVuDTO(maDV, tenDV, giaDV, donVT, moTa);
//        dichVuDTO.setSoLuongTon(soLT);

        Request<DichVuDTO> request = new Request<>("SUA_DICH_VU", dichVuDTO);
        try {
            SocketManager.send(request);
            Type responseType = new TypeToken<Response<String>>(){}.getType();
            Response<String> response = SocketManager.receive(responseType);

            if (response != null && response.isSuccess()) {
                tableModel.setValueAt(tenDV, selectedRow, 1);
                tableModel.setValueAt(moTa, selectedRow, 2);
                tableModel.setValueAt(giaDV, selectedRow, 3);
                tableModel.setValueAt(donVT, selectedRow, 4);
                tableModel.setValueAt(soLT, selectedRow, 5);
                lamMoi();
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật dịch vụ thất bại!");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            int option = JOptionPane.showConfirmDialog(this,
                    "Lỗi kết nối đến server: " + ex.getMessage() + "\nBạn có muốn thử lại?",
                    "Lỗi hệ thống", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
            if (option == JOptionPane.YES_OPTION) {
                suaDichVu();
            }
        }
    }

    private boolean kiemTraDauVao(String maDV, String tenDV, String moTa, String giaDVText) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        int lastTwoDigitsOfYear = currentYear % 100;

        if (!maDV.matches("^[0-9]{2}[A-Z0-9]{4}$")) {
            JOptionPane.showMessageDialog(this, "Mã dịch vụ phải có 2 ký tự đầu là số, 4 ký tự sau là chữ in hoa hoặc số!");
            return false;
        }
        int firstTwoDigits = Integer.parseInt(maDV.substring(0, 2));
        if (firstTwoDigits > lastTwoDigitsOfYear) {
            JOptionPane.showMessageDialog(this, "Hai ký tự đầu của mã dịch vụ không được lớn hơn 2 số cuối của năm hiện tại!");
            return false;
        }

        if (tenDV == null || tenDV.isEmpty() || tenDV.length() > 100) {
            JOptionPane.showMessageDialog(this, "Tên dịch vụ không hợp lệ! (Không được để trống, tối đa 100 ký tự)");
            return false;
        }

        if (moTa == null || moTa.isEmpty() || moTa.length() > 200) {
            JOptionPane.showMessageDialog(this, "Mô tả dịch vụ không hợp lệ! (Không được để trống, tối đa 200 ký tự)");
            return false;
        }

        try {
            double giaDV = Double.parseDouble(giaDVText);
            if (giaDV <= 0) {
                JOptionPane.showMessageDialog(this, "Giá dịch vụ phải lớn hơn 0!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá dịch vụ phải là số hợp lệ!");
            return false;
        }

        return true;
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
        } else if (btn.getText().equals("Thêm")) {
            themDichVu();
        }
    }
}