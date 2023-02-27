package g41.si2022.util;

public abstract class Tab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private final SwingMain main;
	private boolean isInitializedController;

	public Tab (SwingMain main) {
		this.isInitializedController = false;
		this.main = main;
	}

	/**
	 * Returns the SwingMain.
	 * @return SwingMain.
	 */
	public SwingMain getMain () {
		return this.main;
	}

	@Override
	public void setVisible (boolean visible) {
		super.setVisible(visible);
		if (visible && !isInitializedController) {
			this.isInitializedController = true;
			this.initController();
		}
	}

	protected abstract void initController();
}
