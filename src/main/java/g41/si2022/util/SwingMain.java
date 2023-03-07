package g41.si2022.util;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.FlatLightLaf;


/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
public class SwingMain {

	private JFrame frame;
	private JTabbedPane tabs;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FlatLightLaf.setup();
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
	public void initialize() {
		frame = new JFrame();
		frame.setTitle("Programa de gestión del COIIPA");
		frame.setSize(640, 480);
		// frame.setSize(640*2, 480*2);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Tabs are the main content of the window. This will contain all the other GUIs.
		tabs = new JTabbedPane();
		Map<String, Tab> theTabs = new TreeMap<String, Tab>();

		tabs.add(new Debug(this), 0);
		tabs.setTitleAt(0, "Debug");

		// ↓↓↓ ONLY MODIFY THIS IN ORDER TO ADD NEW TABS ↓↓↓
		theTabs.put("Registrar curso", new g41.si2022.coiipa.registrar_curso.RegistrarCursoView(this));
		theTabs.put("Registrar pago alumno", new g41.si2022.coiipa.registrar_pago_alumno.RegistrarPagoAlumnoView(this));
		theTabs.put("Registrar devolución", new g41.si2022.coiipa.insertar_devolucion.InsertarDevolucionView(this));
		theTabs.put("Inscribir usuario", new g41.si2022.coiipa.inscribir_usuario.InscribirUsuarioView(this));
		theTabs.put("Consultar cursos", new g41.si2022.coiipa.consultar_cursos.ConsultarCursosView(this));
		theTabs.put("Consultar Ingresos y Gastos", new g41.si2022.coiipa.consultar_ingresos_gastos.ConsultarIngresosGastosView(this));
		theTabs.put("Registrar pago profesor", new g41.si2022.coiipa.registrar_pago_profesor.RegistrarPagoProfesorView(this));
		theTabs.put("Gestionar cursos", new g41.si2022.coiipa.gestionar_cursos.GestionarCursosView(this));
		// ↑↑↑ ONLY MODIFY THIS IN ORDER TO ADD NEW TABS ↑↑↑

		theTabs.forEach((name, tab) -> tabs.add(name, tab));

		tabs.addChangeListener(e -> {
			Tab tab = (Tab) tabs.getSelectedComponent();
			if (tab != null) tab.initController();
		});

		frame.add(tabs);
		frame.repaint();
		frame.revalidate();
	}

	public JFrame getFrame() { return this.frame; }

	public LocalDate getToday() { return ((Debug) this.tabs.getComponentAt(0)).getToday().getDate();}
	public BetterDatePicker getTodayPicker() { return ((Debug) this.tabs.getComponentAt(0)).getToday();}
}
