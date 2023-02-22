package g41.SI2022.util;

import java.awt.EventQueue;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import g41.SI2022.coiipa.TestPanel;

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
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		frame.setBounds(0, 0, 287, 185);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// tabs is the main content of the window. This will contain all the other GUIs.
		JTabbedPane tabs = new JTabbedPane();
		TestPanel testPanel = new TestPanel();
		tabs.add("Testitle", testPanel);

		frame.add(tabs);
	}

	public JFrame getFrame() { return this.frame; }

}
