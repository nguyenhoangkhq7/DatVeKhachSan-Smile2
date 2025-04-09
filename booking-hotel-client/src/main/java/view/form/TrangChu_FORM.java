package view.form;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

import utils.custom_element.CustomCellRenderer;
import utils.custom_element.CustomHeaderRenderer;
import utils.custom_element.FontManager;
import utils.custom_element.RoundedPanel;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
public class TrangChu_FORM extends JPanel {
	private static final long serialVersionUID = 1L;
	private int currentPage = 1;
	private final int rowsPerPage = 7;
	DefaultTableModel tableModel;
	JLabel pageNumber;
	private ArrayList<Object[]> data;
	public TrangChu_FORM() {
		data = new ArrayList<>();
		setBackground(new Color(16, 16, 20));
		setLayout(new BorderLayout());

		RoundedPanel infoBox1 = createInfoBox(4, "PHÒNG ĐẾN TRONG NGÀY", new Color(10, 213, 157, 179));
		RoundedPanel infoBox2 = createInfoBox(2, "PHÒNG ĐI TRONG NGÀY", new Color(255, 191, 91, 179));
		RoundedPanel infoBox3 = createInfoBox(4, "PHÒNG CÓ KHÁCH", new Color(116, 185, 255, 179), "10%");
		RoundedPanel infoBox4 = createInfoBox(6, "KHÁCH ĐANG Ở", new Color(255, 118, 117, 179));

		Box infoBoxContainer = Box.createHorizontalBox();
		infoBoxContainer.add(infoBox1);
		infoBoxContainer.add(Box.createHorizontalStrut(14));
		infoBoxContainer.add(infoBox2);
		infoBoxContainer.add(Box.createHorizontalStrut(14));
		infoBoxContainer.add(infoBox3);
		infoBoxContainer.add(Box.createHorizontalStrut(14));
		infoBoxContainer.add(infoBox4);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
		northPanel.setOpaque(false);
		northPanel.add(Box.createVerticalStrut(35));
		northPanel.add(infoBoxContainer);
		northPanel.add(Box.createVerticalStrut(55));

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
		centerPanel.setOpaque(false);

		JLabel titleLabel = new JLabel("Thống kê hôm nay");
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

		String[] tableHeaders = "Mã đặt phòng;Phòng;Tên khách;Ngày đến;Ngày đi;Thời gian;Doanh thu".split(";");
		tableModel = new DefaultTableModel(tableHeaders, 0) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// Thiết lập màu nền cho JTable
		JTable table = new JTable(tableModel);
		table.setBackground(new Color(24, 24, 28));
		table.setForeground(Color.WHITE);
		table.setFont(FontManager.getManrope(Font.PLAIN, 14));
		table.setRowHeight(55);

		// Thiết lập header của bảng
		JTableHeader header = table.getTableHeader();
		header.setDefaultRenderer(new CustomHeaderRenderer(new Color(38, 38, 42), Color.white));
		header.setPreferredSize(new Dimension(header.getPreferredSize().width, 55));
		header.setReorderingAllowed(false);

		CustomCellRenderer cellRenderer = new CustomCellRenderer();
		for (int i = 0; i < table.getColumnCount(); i++) {
			TableColumn column = table.getColumnModel().getColumn(i);
			column.setCellRenderer(cellRenderer);
		}
		// Thiết lập JScrollPane
		JScrollPane scroll = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(1642, 335));
		scroll.setBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9));
		scroll.getViewport().setOpaque(false);
		scroll.setViewportBorder(null);
		Dimension navButtonSize = new Dimension(35, 35);
		Color navButtonColor = Color.black;
		Color pageNumberColor = new Color(27, 112, 213);
		JButton prevButton = new JButton(new ImageIcon("imgs/prevIcon.png"));
		prevButton.setPreferredSize(navButtonSize);
		prevButton.setBackground(navButtonColor);
		RoundedPanel pagePanel = new RoundedPanel(5, 0, new Color(255, 255, 255, 0), pageNumberColor);
		pagePanel.setOpaque(false);
		pageNumber = new JLabel("1", JLabel.CENTER);
		pageNumber.setFont(FontManager.getManrope(Font.BOLD, 14));
		pageNumber.setForeground(pageNumberColor);
		pagePanel.setPreferredSize(new Dimension(29, 29));
		pagePanel.add(pageNumber);
		JButton nextButton = new JButton(new ImageIcon("imgs/nextIcon.png"));
		nextButton.setPreferredSize(navButtonSize);
		nextButton.setBackground(navButtonColor);

		prevButton.addActionListener(e -> {
			if (currentPage > 1) {
				currentPage--;
				loadPage(currentPage);
			}
		});

		nextButton.addActionListener(e -> {
			if (currentPage < getTotalPages()) {
				currentPage++;
				loadPage(currentPage);
			}
		});

		JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		navPanel.setOpaque(false);
		navPanel.setPreferredSize(new Dimension(1655, 50));
		navPanel.setMaximumSize(new Dimension(1655, 50));
		navPanel.setMinimumSize(new Dimension(1655, 50));
		navPanel.add(prevButton);
		navPanel.add(pagePanel);
		navPanel.add(nextButton);
		JPanel doanhThuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		doanhThuPanel.setOpaque(false);
		doanhThuPanel.setPreferredSize(new Dimension(0, 0));
		centerPanel.add(titlePanel);
		centerPanel.add(scroll);
		centerPanel.add(navPanel);
		centerPanel.add(doanhThuPanel);
		JLabel tongDoanhThuLabel = new JLabel("Tổng doanh thu: 3.300.000 VND");
		tongDoanhThuLabel.setFont(FontManager.getManrope(Font.BOLD, 16));
		tongDoanhThuLabel.setForeground(Color.white);
		tongDoanhThuLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		doanhThuPanel.add(tongDoanhThuLabel);
		add(northPanel, BorderLayout.NORTH);
		add(centerPanel, BorderLayout.CENTER);

		loadListThongKeHomNay();
		loadPage(currentPage);
	}

	private RoundedPanel createInfoBox(int number, String infoTitle, Color boxColor) {
		return createInfoBox(number, infoTitle, boxColor, null);
	}

	private RoundedPanel createInfoBox(int number, String infoTitle, Color boxColor, String percent) {
		RoundedPanel infoBox = new RoundedPanel(10, 0, boxColor);
		infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
		JLabel numberLabel = new JLabel(String.valueOf(number));
		JLabel titleLabel = new JLabel(infoTitle);
		numberLabel.setForeground(Color.WHITE);
		titleLabel.setForeground(Color.WHITE);
		numberLabel.setFont(FontManager.getManrope(Font.BOLD, 64));
		titleLabel.setFont(FontManager.getManrope(Font.BOLD, 24));
		Box box1 = Box.createHorizontalBox();
		Box box2 = Box.createHorizontalBox();
		box1.add(Box.createHorizontalStrut(15));
		box1.add(numberLabel);
		box1.setAlignmentX(LEFT_ALIGNMENT);
		box2.add(Box.createHorizontalStrut(15));
		box2.add(titleLabel);
		box2.setAlignmentX(LEFT_ALIGNMENT);

		if (percent != null) {
			JLabel percentLabel = new JLabel(percent);
			percentLabel.setForeground(Color.WHITE);
			percentLabel.setFont(FontManager.getManrope(Font.BOLD, 24));
			box2.add(Box.createHorizontalGlue());
			box2.add(percentLabel);
			box2.add(Box.createHorizontalStrut(15));
		}

		infoBox.add(Box.createVerticalStrut(68));
		infoBox.add(box1);
		infoBox.add(Box.createVerticalStrut(5));
		infoBox.add(box2);
		infoBox.setOpaque(false);
		infoBox.setPreferredSize(new Dimension(400, 200));
		infoBox.setMinimumSize(new Dimension(400, 200));
		infoBox.setMaximumSize(new Dimension(400, 200));
		return infoBox;
	}

	private void loadPage(int page) {
		pageNumber.setText(String.valueOf(currentPage));
		tableModel.setRowCount(0);
		int start = (page - 1) * rowsPerPage;
		int end = Math.min(start + rowsPerPage, data.size());
		for (int i = start; i < end; i++) {
			tableModel.addRow(data.get(i));
		}
	}

	private int getTotalPages() {
		return (int) Math.ceil((double) data.size() / rowsPerPage);
	}

	private void loadListThongKeHomNay() {
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		try {
//			// Kết nối đến cơ sở dữ liệu
//			con = ConnectDB.getInstance().getConnection();
//			String sql = "SELECT * FROM PhieuDatPhong pdp " +
//					"JOIN KhachHang kh ON pdp.maKH = kh.maKH";
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sql);
//			while (rs.next()) {
//				String maPDP = rs.getString("maPDP");
//				String maPhong = rs.getString("maPhong");
//				String hoTen = rs.getString("hoTen");
//
//				// Lấy ngày từ ResultSet và chuyển sang LocalDate
//				Date ngayDenDate = rs.getDate("ngayDen");
//				Date ngayDiDate = rs.getDate("ngayDi");
//
//				// Chuyển đổi Date thành LocalDate
//				LocalDate ngayDen = ngayDenDate.toLocalDate();
//				LocalDate ngayDi = ngayDiDate.toLocalDate();
//
//				// Tính số ngày
//				long soNgay = ChronoUnit.DAYS.between(ngayDen, ngayDi);
//
//				Object[] rowData = {maPDP, maPhong, hoTen, ngayDen, ngayDi, soNgay, 0};
//				data.add(rowData);  // Thêm vào ArrayList
//			}
//		} catch (SQLException e){
//			e.printStackTrace();
//		}
	}
}