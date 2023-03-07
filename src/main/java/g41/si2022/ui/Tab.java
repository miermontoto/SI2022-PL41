package g41.si2022.ui;

public abstract class Tab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private final SwingMain main;

	public Tab (SwingMain main) {
		this.main = main;
	}

	public SwingMain getMain () {
		return this.main;
	}

	protected abstract void initController();
}
