package g41.si2022.ui;

public abstract class Tab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private final SwingMain main;
	private boolean opened;

	public Tab (SwingMain main) {
		this.main = main;
		this.opened = false;
	}

	public SwingMain getMain () {
		return this.main;
	}
	
	public boolean isOpened () {
		return opened;
	}
	
	public void abstractInitController () {
		this.opened = true;
		this.initController();
	}

	protected abstract void initController();
	
}
