package utils.custom_element;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	private static final long serialVersionUID = 1L;
	private int radius;

    public RoundedButton(String text, ImageIcon icon, int radius) {
        super(text, icon);
        this.radius = radius;
        setContentAreaFilled(false); // Không tô màu nền mặc định
        setFocusPainted(false); // Không tô màu khi có focus
        setBorderPainted(false); // Không tô viền mặc định
    }
    public RoundedButton(String text, int radius) {
        super(text);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
    }
    @Override
    protected void paintComponent(Graphics g) {
        
    	Graphics2D g2 = (Graphics2D) g;
    	g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        
        super.paintComponent(g);
    }

    @Override
    public boolean contains(int x, int y) {
        // Để cho phép nhấn vào vùng tròn
        return (new Rectangle(0, 0, getWidth(), getHeight())).contains(x, y);
    }
}