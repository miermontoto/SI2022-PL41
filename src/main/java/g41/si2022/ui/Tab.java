package g41.si2022.ui;

import java.lang.reflect.InvocationTargetException;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class Tab extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
	private final SwingMain main;
	private Controller<? extends Tab, ? extends Model> controller;

	@SuppressWarnings("unchecked")
	public Tab (SwingMain main,
			Class<? extends Model> m,
			Class<? extends Tab> v,
			Class<? extends Controller<? extends Tab, ? extends Model>> c
			) {
		this.main = main;
		this.initView();
		java.util.stream.Stream.of(c.getDeclaredConstructors())
			.filter(x -> java.util.List.of(x.getParameterTypes()).contains(m) && java.util.List.of(x.getParameterTypes()).contains(v))
			.collect(Collectors.toList())
			.forEach(x -> {
				try {
					Tab.this.controller = x.getParameterTypes()[0].equals(m)
							? (Controller<? extends Tab, ? extends Model>) x.newInstance(m.getDeclaredConstructor().newInstance(), this)
							: (Controller<? extends Tab, ? extends Model>) x.newInstance(this, m.getDeclaredConstructor().newInstance());
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			});
	}

	public SwingMain getMain () {
		return this.main;
	}

	@Override
	public void setVisible (boolean visible) {
		if (visible) {
			if (this.controller != null && !this.controller.isNonVolatileLoaded ()) {
				this.controller.setNonVolatileLoaded(true);
				this.controller.initNonVolatileData();
			}
			this.controller.initVolatileData();
		}
		super.setVisible(visible);
	}

	protected abstract void initView ();

}
