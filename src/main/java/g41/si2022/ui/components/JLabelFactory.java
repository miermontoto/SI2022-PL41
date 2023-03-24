package g41.si2022.ui.components;

import java.awt.Font;
import javax.swing.JLabel;

import g41.si2022.util.enums.FontType;

/**
 * Factory Method used to generate new {@link JLabel}.
 * This uses the {@link FontType} enum to generate titles, subtitles and general-purpose {@link JLabel}.
 *
 * @see JLabel
 * @see FontType
 * @see <a href="https://en.wikipedia.org/wiki/Factory_method_pattern">Factory Method</a>
 *
 * @author Alex // UO281827
 */
public class JLabelFactory {

	public static final String REGULAR_FONT = new JLabel().getFont().getFamily();
	public static final int REGULAR_FONT_SIZE =  new JLabel().getFont().getSize();

	/**
	 * Constructor is set to private to avoid its usage.
	 */
	private JLabelFactory() { }

	/**
	 * Gets a new {@link JLabel} with {@link FontType#NORMAL} {@link Font}.
	 *
	 * @param txt Text contained in the {@link JLabel}
	 * @return normal {@link JLabel}
	 *
	 * @see FontType
	 * @see Font
	 * @see JLabel
	 */
	public static JLabel getLabel(String txt) {
		return JLabelFactory.getLabel(FontType.normal, txt);
	}

	/**
	 * Gets a new {@link JLabel} with custom {@link Font}.
	 *
	 * @param type {@link FontType} to be used ({@link FontType#TITLE}, {@link FontType#SUBTITLE} or {@link FontType#NORMAL})
	 * @param txt Text contained int he {@link JLabel}
	 * @return custom {@link JLabel}
	 *
	 * @see FontType
	 * @see JLabel
	 */
	public static JLabel getLabel(FontType type, String txt) {
		JLabel output = new JLabel(txt);
		output.setFont(JLabelFactory.getFont(type));
		return output;
	}

	/**
	 * Gets a {@link Font} given its {@link FontType}.
	 * <ul>
	 * 	<li>FontType.title</li>
	 *  <li>FontType.subtitle</li>
	 *  <li>FontType.normal</li>
	 * </ul>
	 *
	 * @param type Type of font to be returned
	 * @return The font that was specified
	 *
	 * @see Font
	 * @see FontType
	 */
	private static Font getFont(FontType type) {
		switch (type) {
			case title: return new Font("Serif", Font.BOLD, 20);
			case subtitle: return new Font("Serif", Font.ITALIC, 15);
			case bold: return new Font(REGULAR_FONT, Font.BOLD, REGULAR_FONT_SIZE);
			default: return new Font(REGULAR_FONT, Font.PLAIN, REGULAR_FONT_SIZE);
		}
	}

	public static JLabel empty() {
		return getLabel("N/A");
	}

}
