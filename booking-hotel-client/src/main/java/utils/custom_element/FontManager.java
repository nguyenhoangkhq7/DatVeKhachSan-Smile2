package utils.custom_element;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FontManager {
    private static Font merriweather, manrope;

    static {
        try {
            InputStream merrifontStream = FontManager.class.getClassLoader().getResourceAsStream("fonts/Merriweather.ttf");
            InputStream manropeStream = FontManager.class.getClassLoader().getResourceAsStream("fonts/Manrope.ttf");

            if (merrifontStream == null || manropeStream == null) {
                throw new FileNotFoundException("Không tìm thấy file font.");
            }

            merriweather = Font.createFont(Font.TRUETYPE_FONT, merrifontStream);
            manrope = Font.createFont(Font.TRUETYPE_FONT, manropeStream);

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(merriweather);
            ge.registerFont(manrope);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Font getMerriweather(int style, int size) {
        return merriweather.deriveFont(style, size);
    }

    public static Font getManrope(int style, int size) {
        return manrope.deriveFont(style, size);
    }
}
