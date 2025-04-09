package utils.custom_element;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
public class FontManager {
    private static Font merriweather, manrope;

    static {
        try {
            merriweather = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Merriweather.ttf"));
            manrope = Font.createFont(Font.TRUETYPE_FONT, new File("fonts/Manrope.ttf"));
            
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(merriweather);
            ge.registerFont(manrope);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Font getMerriweather(int style, int size) {
        return new Font(merriweather.getFontName(), style, size);
    }

    public static Font getManrope(int style, int size) {
        return new Font(manrope.getFontName(), style, size);
    }
}