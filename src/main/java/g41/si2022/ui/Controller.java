package g41.si2022.ui;

public abstract class Controller <T extends Tab, M extends Model> {
	
	private T myTab;
	private M myModel;
	private boolean nonVolatileLoaded;
	
	public Controller (T myTab, M myModel) {
		this.myTab = myTab;
		this.myModel = myModel;
		this.nonVolatileLoaded = false;
	}
	
	public T getTab () { return this.myTab; }
	public T getView () { return this.getTab(); }
	public M getModel () { return this.myModel; }

	/**
	 * DO NOT RUN THIS METHOD.
	 * This method is only expected to be ran by <code>Tab</code> when this controller's <code>Tab</code>
	 * is set to visible the first time.
	 * 
	 * The <code>nonVolatileLoaded</code> keeps track of wether the non volatile contents of a <code>Tab</code>
	 * has been loaded or not. Setting this to <code>False</code> will make it so the non volatile contents of the
	 * <code>Tab</code> are loaded again.
	 * 
	 * @param nonVolatileLoaded next value for the <code>nonVolatileLoaded</code> flag.
	 */
	public void setNonVolatileLoaded (boolean nonVolatileLoaded) {
		this.nonVolatileLoaded = nonVolatileLoaded;
	}
	
	/**
	 * Checks if the non volatile contents of a <code>Tab</code> have been loaded or not.
	 * The fact that this returns <code>false</code> implies that the non volatile contents should be loaded
	 * when the next <code>setVisible</code> is ran with <code>true</code>.
	 * 
	 * @return <code>true</code> if the non volatile contents of the <code>Tab</code> have been loaded, <code>false</code> if the have not.
	 */
	public boolean isNonVolatileLoaded () {
		return this.nonVolatileLoaded;
	}
	
	/**
	 * Loads all the NON VOLATILE DATA. This is data that does not get updated / changed throughout the 
	 * execution of the program.
	 * 
	 * This function should add all the Listeners to the components of the view, and fill structures such 
	 * as JComboBoxes that only contain States.
	 * 
	 * This function should only be called ONCE per run.
	 */
	protected abstract void initNonVolatileData();
	
	/**
	 * Loads all the VOLATILE DATA. This is data that may get upated / change throughout the execution
	 * of the program.
	 * 
	 * This function should add all the data to JTables as long as this data may be updated by INSERT or UPDATE
	 * SQL commands.
	 * 
	 * This function should be called EVERY TIME the tab is loaded shown.
	 */
	protected abstract void initVolatileData();
	
}
