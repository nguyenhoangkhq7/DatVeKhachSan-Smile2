package view.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;

import customElements.FontManager;
import customElements.Openable;
import customElements.RoundedButton;
import customElements.SubMenuPanel;
import form.*;

import java.util.ArrayList;

public class GiaoDienChinh_GUI extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel sidebar;
	private JPanel centerPanel;
	private CardLayout cardLayout;
	private ArrayList<SubMenuPanel> dsSubMenuPanel;
	public JLabel pageLabel;
	public RoundedButton selectedButton;

	public GiaoDienChinh_GUI() {
		dsSubMenuPanel = new ArrayList<SubMenuPanel>(10);
		setTitle("Hệ thống quản lý khách sạn");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		setSize(1920, 1080);
		setResizable(false);
		centerPanel = createCenter();
		JPanel sidebarPanel = createSidebar();
		JPanel rightPanel = new JPanel(new BorderLayout());
		JPanel leftHeaderPanel = createLeftHeader();
		JPanel rightHeaderPanel = createRightHeader();
		JPanel headerPanel = createHeader();

		headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
		headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
		rightPanel.add(headerPanel, BorderLayout.NORTH);
		rightPanel.add(centerPanel, BorderLayout.CENTER);
		add(sidebarPanel, BorderLayout.WEST);
		add(rightPanel, BorderLayout.CENTER);
		setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);

	}

	private JPanel createSidebar() {
		sidebar = new JPanel();
		sidebar.setPreferredSize(new Dimension(260, getHeight()));
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setBackground(new Color(24, 24, 28));

		Border matteBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(45, 45, 48));
		Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		Border combinedBorder = BorderFactory.createCompoundBorder(matteBorder, emptyBorder);
		sidebar.setBorder(combinedBorder);

		Box sidebarMenu = Box.createVerticalBox();
		sidebarMenu.setAlignmentX(CENTER_ALIGNMENT);

		// Các menu item và submenu tương ứng
		String[][] menuItems = {
				{ "Trang chủ", "TrangChu", "TrangChu" }, // Không có submenu
				{ "Phòng", null, "Phong" },
				{ "Nhân viên", null, "NhanVien"},
				{ "Khách hàng", null, "KhachHang" },
				{ "Dịch vụ", null, "DichVu" },
				{ "Hóa đơn", null, "HoaDon" },
				{ "Thống kê", null, "ThongKe" },
		};

		String[][][] subMenuItems = {
				null, // Trang chủ
				{
						{ "Đặt phòng", "DatPhong" },
						{ "Hủy đặt phòng", "HuyDatPhong" },
						{ "Nhận phòng", "NhanPhong"},
						{ "Trả phòng", "TraPhong" },
						{ "Đổi phòng", "DoiPhong" },
						{ "Cập nhật phòng", "CapNhatPhong" },

				}, // Phòng

				{
						{ "Cập nhật nhân viên", "CapNhatNhanVien" },
				},
				//Nhân viên

				{
						{ "Cập nhật khách hàng", "CapNhatKhachHang" }
				}, // Khách hàng
				{
						{ "Đặt dịch vụ", "DatDichVu" },
						{ "Hủy đặt dịch vụ", "HuyDatDichVu" },
						{ "Cập nhật dịch vụ", "CapNhatDichVu" },

				}, // Dịch vụ
				{
						{ "Lập hóa đơn", "LapHoaDon" },
//
				}, // Hóa đơn
				{

						{ "Thống kê doanh thu", "ThongKeDoanhThu" },

				},
					// Thống kê

		};

		for (int i = 0; i < menuItems.length; i++) {
			String[] item = menuItems[i];
			String[][] subItems = subMenuItems[i];

			RoundedButton menuButton = new RoundedButton(item[0], new ImageIcon("imgs/" + item[2] + "Icon.png"), 5);
			menuButton.setHorizontalAlignment(SwingConstants.LEFT);
			menuButton.setIconTextGap(19);
			menuButton.setFont(FontManager.getManrope(Font.PLAIN, 16));
			menuButton.setPreferredSize(new Dimension(230, 50));
			menuButton.setMaximumSize(new Dimension(230, 50));
			menuButton.setMinimumSize(new Dimension(230, 50));
			menuButton.setBackground(new Color(24, 24, 28));
			menuButton.setForeground(Color.WHITE);
			menuButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

			menuButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					if (selectedButton != menuButton) {
						menuButton.setBackground(new Color(34, 43, 83));
					}
				}

				@Override
				public void mouseExited(MouseEvent e) {
					if (selectedButton != menuButton) {
						menuButton.setBackground(new Color(24, 24, 28));
					}

				}

				@Override
				public void mouseClicked(MouseEvent e) {
					if (selectedButton != menuButton && subItems == null) {
						// Thay đổi màu của menu được chọn
						selectedButton.setBackground(new Color(24, 24, 28));
						selectedButton = menuButton;
						selectedButton.setBackground(new Color(91, 122, 249));
						pageLabel.setText(item[0].toUpperCase());

						// Chuyển sang trang tương ứng
						cardLayout.show(centerPanel, item[2]);

						JPanel selectedPanel = (JPanel) centerPanel.getComponent(getCardIndex(item[2]));
						if (selectedPanel instanceof Openable) {
							((Openable) selectedPanel).open();
						}

						updateButtonColor();
					}
				}
			});
			if (item[1] == "TrangChu") {
				selectedButton = menuButton;
				selectedButton.setBackground(new Color(91, 122, 249));
			}

			// Nếu có submenu, thêm SubMenuPanel vào dưới menu chính
			if (subItems != null) {
				SubMenuPanel subMenu = new SubMenuPanel(subItems, cardLayout, centerPanel, menuButton, this);
				subMenu.setVisible(false); // Ban đầu submenu ẩn
				sidebarMenu.add(menuButton);
				sidebarMenu.add(subMenu);
				menuButton.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						subMenu.setVisible(!subMenu.isVisible()); // Toggle submenu
						sidebar.revalidate();
						sidebar.repaint();
					}
				});
				dsSubMenuPanel.add(subMenu);
			} else {
				sidebarMenu.add(menuButton);
			}
			sidebarMenu.add(Box.createVerticalStrut(20));
		}

		JLabel sidebarLogo = new JLabel(new ImageIcon("imgs/SidebarLogo.png"));
		sidebarLogo.setAlignmentX(CENTER_ALIGNMENT);
		sidebar.add(Box.createVerticalStrut(15));
		sidebar.add(sidebarLogo);
		sidebar.add(Box.createVerticalStrut(40));
		JScrollPane scrollSideBar = new JScrollPane(sidebarMenu, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollSideBar.setBorder(null);
		scrollSideBar.getViewport().setOpaque(false);
		scrollSideBar.setViewportBorder(null);
		scrollSideBar.getVerticalScrollBar().setPreferredSize(new Dimension(5, Integer.MAX_VALUE));
		scrollSideBar.getVerticalScrollBar().setUnitIncrement(10);
		sidebar.add(scrollSideBar);

		sidebarMenu.add(Box.createVerticalGlue());

		RoundedButton logoutButton = new RoundedButton("Đăng xuất", new ImageIcon("imgs/LogoutIcon.png"), 5);
		logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
		logoutButton.setIconTextGap(19);
		logoutButton.setFont(FontManager.getManrope(Font.PLAIN, 16));
		logoutButton.setPreferredSize(new Dimension(230, 50));
		logoutButton.setMaximumSize(new Dimension(230, 50));
		logoutButton.setMinimumSize(new Dimension(230, 50));
		logoutButton.setBackground(new Color(24, 24, 28));
		logoutButton.setForeground(Color.WHITE);
		logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
		logoutButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				logoutButton.setBackground(new Color(34, 43, 83));
			}

			@Override
			public void mouseExited(MouseEvent e) {
				logoutButton.setBackground(new Color(24, 24, 28));
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
				new DangNhap_GUI();
			}
		});
		sidebarMenu.add(logoutButton);
		return sidebar;
	}

	private int getCardIndex(String cardName) {
		for (int i = 0; i < centerPanel.getComponentCount(); i++) {
			if (centerPanel.getComponent(i).getName().equals(cardName)) {
				return i;
			}
		}
		return -1;
	}

	private JPanel createLeftHeader() {
		JPanel leftHeader = new JPanel(null);
		leftHeader.setPreferredSize(new Dimension(1360, 80));
		leftHeader.setBackground(new Color(24, 24, 28));
		leftHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(45, 45, 48)));

		JLabel phoneIcon = new JLabel(new ImageIcon("imgs/PhoneIcon.png"));
		pageLabel = new JLabel("TRANG CHỦ");
//		JLabel phoneLabel = new JLabel("1900-1000-0000");

		phoneIcon.setBounds(1070, 25, 30, 30);
		pageLabel.setFont(FontManager.getManrope(Font.BOLD, 28));
		pageLabel.setForeground(Color.white);
		pageLabel.setBounds(10, 23, 400, pageLabel.getPreferredSize().height);
//		phoneLabel.setFont(FontManager.getManrope(Font.BOLD, 24));
//		phoneLabel.setForeground(new Color(10, 213, 118));
//		phoneLabel.setBounds(1120, 24, phoneLabel.getPreferredSize().width, phoneLabel.getPreferredSize().height);
//		leftHeader.add(phoneIcon);
		leftHeader.add(pageLabel);
//		leftHeader.add(phoneLabel);
		return leftHeader;
	}

	private JPanel createRightHeader() {
		JPanel rightHeader = new JPanel();
		rightHeader.setPreferredSize(new Dimension(300, 80));
		rightHeader.setBackground(new Color(24, 24, 28));
		return rightHeader;
	}

	private JPanel createHeader() {
		JPanel header = new JPanel(new BorderLayout());
		header.setPreferredSize(new Dimension(1660, 80));
		header.setBackground(new Color(24, 24, 28));
		return header;
	}

	private JPanel createCenter() {
		cardLayout = new CardLayout();
		JPanel center = new JPanel(cardLayout);

		// Các màn hình
		JPanel trangChuPanel = new TrangChu_FORM();
		JPanel datPhongPanel = new DatPhong_FORM();
		JPanel huyDatPhongPanel = new HuyDatPhong_FORM();
		JPanel nhanPhongPanel = new NhanPhong_FORM();
		JPanel loaiPhongPanel = new LoaiPhong_FORM();
		JPanel timKiemPhongPanel = new TimKiemPhong_FORM();
		JPanel capNhatPhongPanel = new CapNhatPhong_FORM();
		JPanel doiPhongPanel = new DoiPhong_FORM();
		JPanel traPhongPanel = new TraPhong_FORM();

		JPanel timKiemKhachHangPanel = new TimKiemKhachHang_FORM();
		JPanel capNhatKhachHangPanel = new CapNhatKhachHang_FORM();
		JPanel timKiemNhanVienPanel = new TimKiemNhanVien_FORM();
		JPanel capNhatNhanVienPanel = new CapNhatNhanVien_FORM();
		JPanel lapHoaDonPanel = new LapHoaDon_FORM();
		JPanel datDichVuPanel = new DatDichVu_FORM();
		JPanel capNhatDichVuPanel = new CapNhatDichVu_FORM();
		JPanel huyDatDichVu = new HuyDatDichVu_FORM();
		JPanel thongKeDoanhTHu = new ThongKeDoanhThu_FORM();

		trangChuPanel.setName("TrangChu");
		datPhongPanel.setName("DatPhong");
		huyDatPhongPanel.setName("HuyDatPhong");
		nhanPhongPanel.setName("NhanPhong");
		timKiemPhongPanel.setName("TimKiemPhong");
		capNhatPhongPanel.setName("CapNhatPhong");
		doiPhongPanel.setName("DoiPhong");
		traPhongPanel.setName("TraPhong");
		timKiemNhanVienPanel.setName("TimKiemNhanVien");
		capNhatNhanVienPanel.setName("CapNhatNhanVien");
		timKiemKhachHangPanel.setName("TimKiemKhachHang");
		capNhatKhachHangPanel.setName("CapNhatKhach");
		lapHoaDonPanel.setName("LapHoaDon");
		datDichVuPanel.setName("DatDichVu");
		capNhatDichVuPanel.setName("CapNhatDichVu");
		huyDatDichVu.setName("HuyDatDichVu");
		thongKeDoanhTHu.setName("ThongKeDoanhThu");
		// Thêm màn hình
		center.add(trangChuPanel, "TrangChu");
		center.add(datPhongPanel, "DatPhong");
		center.add(huyDatPhongPanel, "HuyDatPhong");
		center.add(nhanPhongPanel, "NhanPhong");
		center.add(traPhongPanel, "TraPhong");
		center.add(doiPhongPanel, "DoiPhong");
		center.add(loaiPhongPanel, "LoaiPhong");
		center.add(timKiemPhongPanel, "TimKiemPhong");
		center.add(capNhatPhongPanel, "CapNhatPhong");
		center.add(timKiemNhanVienPanel, "TimKiemNhanVien");
		center.add(capNhatNhanVienPanel, "CapNhatNhanVien");
		center.add(timKiemKhachHangPanel, "TimKiemKhachHang");
		center.add(capNhatKhachHangPanel, "CapNhatKhachHang");
		center.add(lapHoaDonPanel, "LapHoaDon");
		center.add(datDichVuPanel, "DatDichVu");
		center.add(capNhatDichVuPanel, "CapNhatDichVu");
		center.add(huyDatDichVu, "HuyDatDichVu");
		center.add(thongKeDoanhTHu, "ThongKeDoanhThu");
		return center;
	}

	public void updateButtonColor() {
		for (SubMenuPanel subMenuPanel : dsSubMenuPanel) {
			if (subMenuPanel.getSelectedSubMenu() != null) {
				subMenuPanel.getSelectedSubMenu().setForeground(new Color(148, 148, 148));
				subMenuPanel.setSelectedSubMenu(null);
				break;
			}

		}
	}
}