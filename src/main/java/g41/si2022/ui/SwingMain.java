package g41.si2022.ui;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.FlatLightLaf;

import g41.si2022.coiipa.consultar_cursos.ConsultarCursosView;
import g41.si2022.coiipa.consultar_ingresos_gastos.ConsultarIngresosGastosView;
import g41.si2022.coiipa.inscribir_usuario.InscribirUsuarioView;
import g41.si2022.coiipa.registrar_curso.RegistrarCursoView;
import g41.si2022.coiipa.registrar_pago.RegistrarPagoView;
import g41.si2022.util.BetterDatePicker;


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
	private Map<String, Map<String, Tab>> theTabs;
	
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
		frame.setSize(640*2, 480*2);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		// Tabs are the main content of the window. This will contain all the other GUIs.
		tabs = new JTabbedPane();
		this.theTabs = new TreeMap<String, Map<String, Tab>>() {{
			put("Other", new TreeMap<String, Tab> () {{
				put("Debug", new Debug(SwingMain.this));
			}});
			put("Secretaria", new TreeMap<String, Tab> () {{
				put("Registrar Pagos", new RegistrarPagoView(SwingMain.this));
				put("Consultar cursos", new ConsultarCursosView(SwingMain.this));
			}});
			put("Responsable", new TreeMap<String, Tab> () {{
				put("Registrar Curso", new RegistrarCursoView(SwingMain.this));
				put("Consultar Balance", new ConsultarIngresosGastosView(SwingMain.this));
			}});
			put("Profesional", new TreeMap<String, Tab> () {{
				put("Inscribirse", new InscribirUsuarioView(SwingMain.this));
			}});
		}};
		this.theTabs.forEach((name, innerTabs) -> {
			tabs.add(name, new JTabbedPane() {{
				innerTabs.forEach((innerName, tab) -> {
					add(innerName, tab);
				});
			}});
		});
		this.getFrame().add(tabs);

		/*
		JTabbedPane other = new JTabbedPane();
		JTabbedPane secretaria = new JTabbedPane();
		JTabbedPane responsable = new JTabbedPane();
		JTabbedPane profesional = new JTabbedPane();

		JTabbedPane[] allTabs = {other, secretaria, responsable, profesional};

		other.add("Debug", new Debug(this));
		secretaria.add("Registrar pagos", new RegistrarPagoView(this));
		secretaria.add("Consultar cursos", new ConsultarCursosView(this));
		responsable.add("Registrar curso", new RegistrarCursoView(this));
		responsable.add("Consultar balance", new ConsultarIngresosGastosView(this));
		profesional.add("Inscribirse", new InscribirUsuarioView(this));

		theTabs.put("Otros", other);
		theTabs.put("Secretaria administrativa", secretaria);
		theTabs.put("Responsable de formación", responsable);
		theTabs.put("Profesional (alumno)", profesional);

		theTabs.forEach((name, tab) -> tabs.add(name, tab));

		for(JTabbedPane t : allTabs) {
			t.addChangeListener(e -> {
				Tab tab = (Tab) t.getSelectedComponent();
				if (tab != null) tab.initController();
			});
		}

		debugTab = (Debug) other.getComponentAt(0);

		frame.add(tabs);
		frame.repaint();
		frame.revalidate();
		*/
	}

	public JFrame getFrame() { return this.frame; }

	public LocalDate getToday() { return getTodayPicker().getDate(); }
	public BetterDatePicker getTodayPicker() { return ((Debug) (this.theTabs.get("Other").get("Debug"))).getToday(); }
}
