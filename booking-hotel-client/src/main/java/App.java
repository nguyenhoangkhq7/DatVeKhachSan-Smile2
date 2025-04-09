import java.awt.Color;
import java.sql.SQLException;

import javax.swing.UIManager;

import view.gui.DangNhap_GUI;

public class App {
	public static void main(String[] args) throws SQLException {
		try {
			 UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
	         UIManager.put("nimbusBase", new Color(38, 38, 42));

       } catch (Exception e) {
           e.printStackTrace();
       }
		new DangNhap_GUI();
	}
}
