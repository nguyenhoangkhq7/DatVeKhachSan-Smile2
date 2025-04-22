package utils.custom_element;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Image backgroundImage;

	public BackgroundPanel(String fileName) throws IOException {
		InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
		if (is == null) {
			throw new FileNotFoundException("Không tìm thấy file: " + fileName);
		}
		backgroundImage = ImageIO.read(is);
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, this);
	}
}