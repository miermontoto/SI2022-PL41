package g41.si2022.mvc;

import java.lang.reflect.InvocationTargetException;

import g41.si2022.ui.SwingMain;

/**
 * <code>Abstract Tab</code>. This class represents a {@link JPanel} that is part of a {@link JTabbedPane}.
 * A <code>Tab</code> is equivalent to a View in the context of MVC model.
 *
 * <p>
 * A <code>Tab</code> has a {@link Controller} and a {@link Model} to complete the MVC model. However, only
 * the {@link Controller} is kept as part of the <code>Tab</code>, while the {@link Model} will be part of the
 * {@link Controller}.
 * </p>
 * <p>
 * Note that this <code>Tab</code> is expected to implement <a href="https://en.wikipedia.org/wiki/Lazy_loading">Lazy Loading</a>
 * so that all its data (contained in the {@link Controller}) is loaded only if the user requests it and only when it is
 * requested. If it was not for this behaviour, all the data would be loaded on startup, even if this particular <code>Tab</code>
 * was not going to be used. <br>
 * Until this <code>Tab</code> is set to visible ({@link #setVisible()}), it will be a
 * <a href="https://en.wikipedia.org/wiki/Lazy_loading#Ghost">Ghost</a> containing only the GUI elements.
 * <p>
 *
 * @see JPanel
 * @see JTabbedPane
 * @see <a href="https://en.wikipedia.org/wiki/Lazy_loading">Lazy Loading</a>
 *
 * @author Alex // UO281827
 */
public abstract class View extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * A Tab has a reference to the main {@link SwingMain} this is mainly used
	 * to call the {@link SwingMain#getToday()} method that will return today's date.
	 *
	 * @see SwingMain
	 */
	private final SwingMain main;

	/**
	 * A Tab has a reference to its {@link Controller} whose job is to link the GUI components
	 * with the data retrieved by the {@link Model}.
	 *
	 * @see Controller
	 * @see Model
	 */
	private Controller<? extends View, ? extends Model> controller;

	/**
	 * <p>
	 * The {@link #setVisible} method from this class
	 * is called a few times before actually being visible.
	 * </p>
	 * <p>
	 * This value is used to delay the {@link Controller#initNonVolatileData}
	 * and {@link Controller#initVolatileData} calls so
	 * <a href="https://en.wikipedia.org/wiki/Lazy_loading">Lazy Loading</a> actually works.
	 * </p>
	 *
	 * @see Controller
	 * @see <a href="https://en.wikipedia.org/wiki/Lazy_loading">Lazy Loading</a>
	 */
	private int delayCount;

	/**
	 * Creates a new <code>Tab</code>.<br>
	 * Creating a new <code>Tab</code> will make it so the View is initialized according to this {@link #initView}
	 * method, instantiate the {@link Model} and {@link Controller}.
	 *
	 * @param main Reference to the {@link SwingMain} window.
	 * @param m Class of the {@link Model} that will be used. This must <code>extend {@link Model}</code>.
	 * @param v Class of the {@link View} (Tab) that will be used. This must <code>extend {@link View}</code>.
	 * @param c Class of the {@link Controller} that will be used. This must <code>extend {@link Controller}</code>.
	 *
	 * @see Model
	 * @see Controller
	 * @see SwingMain
	 */
	@SuppressWarnings("unchecked")
	public View (SwingMain main,
			Class<? extends Model> m,
			Class<? extends View> v,
			Class<? extends Controller<? extends View, ? extends Model>> c
			) {
		this.delayCount = 2;
		this.main = main;
		this.initView();
		java.util.stream.Stream.of(c.getDeclaredConstructors())
			.filter(x ->
						 java.util.Arrays.asList(x.getParameterTypes()).contains(m) &&
						 java.util.Arrays.asList(x.getParameterTypes()).contains(v))
			.forEach(x -> {
				try {
					View.this.controller = x.getParameterTypes()[0].equals(m)
							? (Controller<? extends View, ? extends Model>)
									x.newInstance(m.getDeclaredConstructor().newInstance(), this)
							: (Controller<? extends View, ? extends Model>)
							x.newInstance(this, m.getDeclaredConstructor().newInstance());
				} catch (NoSuchMethodException | SecurityException | InstantiationException |
						IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
	}

	/**
	 * Returns the {@link SwinMain} program. This is used mainly to get today's date.
	 *
	 * @return Reference to the main window.
	 *
	 * @see SwingMain
	 */
	public SwingMain getMain () { return this.main; }

	/**
	 * {@inheritDoc}
	 * This will avoid initializing the {@link Controller} data
	 * (i.e. running {@link Controller#initNonVolatileData} and {@link Controller#initVolatileData}
	 * until the user actually wants to see this <code>Tab</code>.
	 *
	 * @see View
	 */
	@Override
	public void setVisible (boolean visible) {
		if (this.delayCount-- <= 0 && visible) {
			if (this.controller != null && !this.controller.isNonVolatileLoaded ()) {
				this.controller.setNonVolatileLoaded(true);
				this.controller.initNonVolatileData();
			}
			this.controller.initVolatileData();
		}
		super.setVisible(visible);
	}

	/**
	 * Creates all the GUI elements of this <code>tab</code>.<br>
	 * No data or listeners should be created here, all of that behaviour goes in the {@link Controller}.
	 *
	 * @see Controller
	 */
	protected abstract void initView ();

}
