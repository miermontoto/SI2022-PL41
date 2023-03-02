package g41.si2022.util;

import java.awt.Font;
import javax.swing.JLabel;

public class JLabelFactory {

	private JLabelFactory () { }
	
	public static JLabel getLabel (FontType type, String txt) {
		JLabel output = new JLabel (txt);
		output.setFont(JLabelFactory.getFont(type));
		return output;
	}
	
	private static Font getFont (FontType type) {
		switch (type) {
		case title: return new Font("Serif", Font.BOLD, 20);
		case subtitle: return new Font("Serif", Font.ITALIC, 15);
		case normal: return new Font("Serif", Font.PLAIN, 12);
		}
		return null;
	}

}
