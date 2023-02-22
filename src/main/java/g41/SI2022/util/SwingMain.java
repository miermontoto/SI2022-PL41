package g41.SI2022.util;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import g41.SI2022.coiipa.*;

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

		/*JButton btnEjecutarTkrun = new JButton("Ejecutar COIIPA");
		btnEjecutarTkrun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: abrir pantalla principal
			}
		});
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
		frame.getContentPane().add(btnEjecutarTkrun);


		JButton btnInicializarBaseDeDatos = new JButton("Init BBDD");
		btnInicializarBaseDeDatos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db=new Database();
				db.createDatabase(false);
				// TODO: no está preparado todavía para la nueva estructura de la bbdd
			}
		});
		frame.getContentPane().add(btnInicializarBaseDeDatos);

		JButton btnCargarDatosIniciales = new JButton("Cargar datos iniciales");
		btnCargarDatosIniciales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db=new Database();
				db.createDatabase(false);
				db.loadDatabase();
				// TODO: no está preparado todavía para la nueva estructura de la bbdd
			}
		});
		frame.getContentPane().add(btnCargarDatosIniciales);*/
		
		// tabs is the main content of the window. This will contain all the other GUIs.
		JTabbedPane tabs = new JTabbedPane();
		RegistrarPago registrarPago = new RegistrarPago();
		tabs.add("Registrar un pago", registrarPago);
		
		frame.add(tabs);
	}

	public JFrame getFrame() { return this.frame; }

}
