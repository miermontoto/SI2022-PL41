package g41.si2022.ui;

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

	// For some obscure reason, the initController method is called when clicking on the tab
	// Therefore, keeping the initController call in the setVisible method as well will make it so it is ran twice
	// This should not happen
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
