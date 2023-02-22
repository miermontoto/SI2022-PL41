package g41.SI2022.util;

public class Tab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private final SwingMain main;
	
	public Tab (SwingMain main) {
		this.main = main;
	}
	
	/**
	 * Returns the SwingMain.
	 * @return SwingMain.
	 */
	public SwingMain getMain () {
		return this.main;
	}

}
