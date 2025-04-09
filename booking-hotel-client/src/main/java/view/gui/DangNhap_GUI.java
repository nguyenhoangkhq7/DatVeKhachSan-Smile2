package view.gui;

import utils.custom_element.BackgroundPanel;
import utils.custom_element.FontManager;
import utils.custom_element.RoundedPanel;
import dao.TaiKhoan_DAO;
import model.TaiKhoan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class DangNhap_GUI extends JFrame {
    private static final long serialVersionUID = 1L;

    private static final Color BACKGROUND_COLOR = new Color(16, 16, 20);
    private static final Color BUTTON_COLOR_SUBMIT = new Color(66, 99, 235);
    private static final Color BUTTON_COLOR_EXIT = new Color(151, 69, 35);
    private static final int FIELD_WIDTH = 600;
    private static final int BUTTON_HEIGHT = 55;
    private TaiKhoan_DAO taiKhoanDAO = new TaiKhoan_DAO();
    public DangNhap_GUI() {
//        ConnectDB.getInstance().connect();
        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        setTitle("Hệ thống quản lý khách sạn");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(1920, 1080);
        setResizable(false);

            JPanel leftPanel = createLeftPanel();
            leftPanel.setFocusable(true);
            leftPanel.requestFocusInWindow();
        JPanel rightPanel = createRightPanel();

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createLeftPanel() {

        BackgroundPanel leftPanel = null;
		try {
			leftPanel = new BackgroundPanel("imgs/LeftBackGround.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
        leftPanel.setPreferredSize(new Dimension(960, getHeight()));
        leftPanel.setBackground(BACKGROUND_COLOR);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        ImageIcon logoIcon = new ImageIcon("imgs/Logo.png");
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setAlignmentX(CENTER_ALIGNMENT);

        RoundedPanel titlePanel = createTitlePanel();
        leftPanel.add(Box.createVerticalStrut(220));
        leftPanel.add(logoLabel);
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.add(titlePanel);

        return leftPanel;
    }

    private RoundedPanel createTitlePanel() {
        RoundedPanel titlePanel = new RoundedPanel(65, 2, new Color(0, 0, 0, 230), new Color(219, 228, 255));
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        titlePanel.setMaximumSize(new Dimension(650, 190));

        JLabel titleLabel = new JLabel("Hệ thống quản lý khách sạn");
        titleLabel.setFont(FontManager.getMerriweather(Font.PLAIN, 36));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);
        titlePanel.add(Box.createVerticalGlue());

        return titlePanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(960, getHeight()));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));


        JLabel welcomeLabel = createLabel("Welcome Back!", new Font("merriweather", Font.PLAIN, 36), new Color(219, 228, 255));

        // Tên đăng nhập
        JLabel userLabel = createLabel("Tên đăng nhập", new Font("Manrope Regular", Font.PLAIN, 14), Color.white);
        JLabel starLabel = createLabel("*", new Font("Manrope Regular", Font.PLAIN, 14), Color.red);
        JTextField userField = new JTextField("Nhập tên đăng nhập");
        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        userField.setMaximumSize(new Dimension(FIELD_WIDTH, 55));
        userField.setBorder(emptyBorder);
        userField.setBackground(new Color(40, 40, 44));
        userField.setForeground(new Color(255, 255, 255, 127));
        userField.setFont(FontManager.getManrope(Font.PLAIN, 16));
        CompoundBorder combinedBorder = BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(83, 152, 255)), emptyBorder);
        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	userField.setBorder(combinedBorder);
                if (userField.getText().equals("Nhập tên đăng nhập")) {
                	userField.setText("");
                	userField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            	userField.setBorder(emptyBorder);
                if (userField.getText().isEmpty()) {
                	userField.setForeground(new Color(255, 255, 255, 127));
                	userField.setText("Nhập tên đăng nhập");
                }
            }
        });
        userField.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('/'), "focusUserField");
        userField.getActionMap().put("focusUserField", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userField.requestFocusInWindow();
            }
        });


        // Mật khẩu
        JLabel passLabel = createLabel("Mật khẩu", new Font("Manrope Regular", Font.PLAIN, 14), Color.white);
        JLabel star2Label = createLabel("*", new Font("Manrope Regular", Font.PLAIN, 14), Color.red);
        JPasswordField passField = new JPasswordField("Nhập mật khẩu");
        passField.setMaximumSize(new Dimension(FIELD_WIDTH, 55));
        passField.setBorder(emptyBorder);
        passField.setBackground(new Color(40, 40, 44));
        passField.setForeground(new Color(255, 255, 255, 127));
        passField.setFont(FontManager.getManrope(Font.PLAIN, 16));
        passField.setEchoChar('\0');
        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
            	passField.setBorder(combinedBorder);
                if (new String(passField.getPassword()).equals("Nhập mật khẩu")) {
                	passField.setText("");
                	passField.setForeground(Color.WHITE);
                	passField.setEchoChar('•');
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
            	passField.setBorder(emptyBorder);
                if (new String(passField.getPassword()).isEmpty()) {
                	passField.setForeground(new Color(255, 255, 255, 127));
                	passField.setText("Nhập mật khẩu");
                	passField.setEchoChar('\0');
                }
            }
        });

        passField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
//                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
//                    String tenDN = userField.getText();
//                    String matKhau = new String(passField.getPassword());
//
//                    String role = taiKhoanDAO.checkDangNhap(tenDN, matKhau);
//
//                    if (role != null) {
//                        if ("ADMIN".equals(role)) {
//                            // Hiển thị giao diện cho admin
//                            new GiaoDienChinh_GUI();
//                        } else {
//                            // Hiển thị giao diện cho nhân viên
//                            new GiaoDienNhanVien_GUI();
//                        }
//                        dispose();
//                    } else {
//                        JOptionPane.showMessageDialog(DangNhap_GUI.this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
//                    }
//                }
            }
        });
        rightPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rightPanel.requestFocusInWindow();
            }
        });

        userField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    passField.requestFocusInWindow();
                }
            }
        });

        JButton submitButton = createButton("Xác nhận", BUTTON_COLOR_SUBMIT);
        JButton exitButton = createButton("Thoát", BUTTON_COLOR_EXIT);
        
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

//        submitButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String tenDN = userField.getText();
//                String matKhau = new String(passField.getPassword());
//
//                TaiKhoan taiKhoan = taiKhoanDAO.checkDangNhap(tenDN, matKhau);
//
//                if (taiKhoan != null) {
//                    try {
//                        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//                    } catch (Exception ex) {
//                        ex.printStackTrace();
//                    }
//                    new GiaoDienChinh_GUI();
//                    dispose();
//                } else {
//                    JOptionPane.showMessageDialog(DangNhap_GUI.this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
//                }
//            }
//        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                String tenDN = userField.getText();
//                String matKhau = new String(passField.getPassword());
//
//                String role = taiKhoanDAO.checkDangNhap(tenDN, matKhau);
//
//                if (role != null) {
//                    if ("ADMIN".equals(role)) {
//                        // Hiển thị giao diện cho admin
//                        new GiaoDienChinh_GUI();
//                    } else {
//                        // Hiển thị giao diện cho nhân viên
//                        new GiaoDienNhanVien_GUI();
//                    }
//                    dispose();
//                } else {
//                    JOptionPane.showMessageDialog(DangNhap_GUI.this, "Tên đăng nhập hoặc mật khẩu không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
//                }
            }
        });
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        Box userBox = Box.createHorizontalBox();
        userBox.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        userBox.add(userLabel);
        userBox.add(starLabel);

        Box passBox = Box.createHorizontalBox();
        passBox.setMaximumSize(new Dimension(FIELD_WIDTH, 20));
        passBox.add(passLabel);
        passBox.add(star2Label);

        Box buttonBox = Box.createHorizontalBox();
        buttonBox.setMaximumSize(new Dimension(FIELD_WIDTH, BUTTON_HEIGHT));
        buttonBox.add(submitButton);
        buttonBox.add(Box.createHorizontalStrut(13));
        buttonBox.add(exitButton);

        rightPanel.add(Box.createVerticalStrut(300));
        rightPanel.add(welcomeLabel);
        rightPanel.add(Box.createVerticalStrut(45));
        rightPanel.add(userBox);
        rightPanel.add(Box.createVerticalStrut(11));
        rightPanel.add(userField);
        rightPanel.add(Box.createVerticalStrut(34));
        rightPanel.add(passBox);
        rightPanel.add(Box.createVerticalStrut(11));
        rightPanel.add(passField);
        rightPanel.add(Box.createVerticalStrut(56));
        rightPanel.add(buttonBox);

        return rightPanel;
    }

    private JLabel createLabel(String text, Font font, Color foreground) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(foreground);
        label.setAlignmentX(CENTER_ALIGNMENT);
        return label;
    }

    private JButton createButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(295, BUTTON_HEIGHT));
        button.setBackground(backgroundColor);
        button.setFont(new Font("Manrope", Font.PLAIN, 15));
        button.setForeground(Color.WHITE);
		button.setFont(FontManager.getManrope(Font.PLAIN, 16));
        return button;
    }


}
