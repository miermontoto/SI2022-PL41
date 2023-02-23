package g41.SI2022.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
public class SwingMain {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SwingMain() {
		initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Main");
		frame.setBounds(0, 0, 640, 480);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Tabs are the main content of the window. This will contain all the other GUIs.
		JTabbedPane tabs = new JTabbedPane();
		java.util.TreeMap<String, Tab> theTabs = new java.util.TreeMap<String, Tab> ();

		// ↓↓↓ ONLY MODIFY THIS IN ORDER TO ADD NEW TABS ↓↓↓
		theTabs.put("testTab", new TestPanel(this));
		theTabs.put("Registrar Curso", new g41.SI2022.coiipa.registrarCurso.RegistrarCurso(this));

		// ↑↑↑ ONLY MODIFY THIS IN ORDER TO ADD NEW TABS ↑↑↑
		
		theTabs.forEach((name, tab) -> tabs.add(name, tab));
		frame.add(tabs);
	}

	public JFrame getFrame() { return this.frame; }

}
