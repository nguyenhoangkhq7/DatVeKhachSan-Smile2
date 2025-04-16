import java.awt.Color;
import java.sql.SQLException;

import javax.swing.UIManager;

import socket.SocketManager;
import view.gui.DangNhap_GUI;
import view.gui.GiaoDienChinh_GUI;

public class ClientApp {
	public static void main(String[] args) throws SQLException {
		try {
			 SocketManager.connect("localhost", 12345); // Kết nối 1 lần
			 UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	         UIManager.put("nimbusBase", new Color(38, 38, 42));
       } catch (Exception e) {
           e.printStackTrace();
       }
		new DangNhap_GUI();
//		new GiaoDienChinh_GUI();
	}
}
