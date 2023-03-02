package g41.si2022.util;

import java.awt.Font;
import javax.swing.JLabel;

/**
 * 
 * @author Alex // UO281827
 *
 * Factory Method used to generate new Labels.
 * This uses the FontType enum to generate titles, subtitles and general-purpose labels.
 */
public class JLabelFactory {

	/**
	 * Constructor is set to private to avoid its usage.
	 */
	private JLabelFactory () { }
	
	/**
	 * Gets a new Label with general-purpose font.
	 * 
	 * @param txt Text contained in the label
	 * @return normal JLabel
	 */
	public static JLabel getLabel (String txt) {
		return JLabelFactory.getLabel(FontType.normal, txt);
	}

	/**
	 * Gets a new Label with custom font.
	 * 
	 * @param type Type of font to be used (title, subtitle or normal)
	 * @param txt Text contained int he label
	 * @return custom label
	 */
	public static JLabel getLabel (FontType type, String txt) {
		JLabel output = new JLabel (txt);
		output.setFont(JLabelFactory.getFont(type));
		return output;
	}
	
	/**
	 * Gets a font given its FontType.
	 * 	-> FontType.title
	 *  -> FontType.subtitle
	 *  -> FontType.normal
	 * 
	 * @param type Type of font to be returned
	 * @return The font that was specified
	 */
	private static Font getFont (FontType type) {
		switch (type) {
		case title: return new Font("Serif", Font.BOLD, 20);
		case subtitle: return new Font("Serif", Font.ITALIC, 15);
		case normal: return new Font("Serif", Font.PLAIN, 12);
		}
		return null;
	}

}
