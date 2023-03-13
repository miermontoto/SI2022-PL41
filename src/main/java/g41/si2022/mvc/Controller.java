package g41.si2022.mvc;

import g41.si2022.ui.Tab;

/**
 * <p>
 * A <code>Controller</code> is the intermediary that joins the data supplied by the {@link Model}
 * with the components contained in the {@link Tab}.
 * </p> <p> 
 * This means that a <code>Controller</code> must define all the behaviour of a given screen.
 * Some of the things a controller usually does are:
 * <ul>
 * <li>Creating and assigning Listeners.</li>
 * </li>Sending data from the {@link Model} to the {@link Tab}.
 * </ul> 
 * </p>
 * 
 * @author Alex // UO281827
 *
 * @param <T> Type representing the View. This must <code>extend {@link Tab}</code>
 * @param <M> Type representing the Model. This must <code>extend {@link Model}</code>
 * 
 * @see Model
 * @see Tab
 */
public abstract class Controller <T extends Tab, M extends Model> {

	/**
	 * {@link Tab} that this <code>Controller</code> is supposed to supply data to.
	 * 
	 * @see Tab
	 */
	private T myTab;
	
	/**
	 * {@link Model} that this <code>Controller</code> is supposed to get data from.
	 * 
	 * @see Model
	 */
	private M myModel;
	
	/**
	 * Checker used to control wether this <code>Controller</code> has been loaded with 
	 * non volatile data or not.
	 */
	private boolean nonVolatileLoaded;

	/**
	 * Creates a new <code>Controller</code>.
	 * 
	 * @param myTab The tab that this controller uses.
	 * @param myModel The model that this controller uses.
	 * 
	 * @see Tab
	 * @see Model
	 */
	public Controller (T myTab, M myModel) {
		this.myTab = myTab;
		this.myModel = myModel;
		this.nonVolatileLoaded = false;
	}

	public T getTab () { return this.myTab; }
	public T getView () { return this.getTab(); }
	public M getModel () { return this.myModel; }

	/**
	 * <p>
	 * DO NOT RUN THIS METHOD.
	 * </p> <p>
	 * This method is only expected to be ran by <code>Tab</code> when this controller's <code>Tab</code>
	 * is set to visible the first time.
	 * </p> <p>
	 * The <code>nonVolatileLoaded</code> keeps track of wether the non volatile contents of a {@link Tab}
	 * has been loaded or not. Setting this to <code>False</code> will make it so the non volatile contents of the
	 * {@link Tab} are loaded again.
	 * </p>
	 *
	 * @param nonVolatileLoaded next value for the <code>nonVolatileLoaded</code> flag.
	 * 
	 * @see Tab
	 */
	public void setNonVolatileLoaded (boolean nonVolatileLoaded) {
		this.nonVolatileLoaded = nonVolatileLoaded;
	}

	/**
	 * Checks if the non volatile contents of a {@link Tab} have been loaded or not.<br>
	 * The fact that this returns <code>false</code> implies that the non volatile contents should be loaded
	 * when the next {@link Tab#setVisible()} is ran with <code>true</code>.
	 *
	 * @return <code>true</code> if the non volatile contents of the {@link Tab} have been loaded, <code>false</code> if the have not.
	 * 
	 * @see Tab
	 */
	public boolean isNonVolatileLoaded () {
		return this.nonVolatileLoaded;
	}

	/**
	 * <p>
	 * <dfn>Loads all the NON VOLATILE DATA. This is data that does not get updated / changed throughout the
	 * execution of the program.</dfn>
	 * </p> <p>
	 * This function should add all the Listeners to the components of the {@link Tab}, and fill structures such
	 * as JComboBoxes that only contain States.
	 * </p>
	 * This function should only be called ONCE per run.
	 * 
	 * @see Tab
	 */
	public abstract void initNonVolatileData();

	/**
	 * <p>
	 * Loads all the VOLATILE DATA. This is data that may get upated / change throughout the execution
	 * of the program.
	 * </p> <p>
	 * This function should add all the data to JTables as long as this data may be updated by INSERT or UPDATE
	 * SQL commands.
	 * </p>
	 * This function should be called EVERY TIME the tab is loaded shown.
	 */
	public abstract void initVolatileData();

}
