package g41.si2022.ui;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;

import g41.si2022.util.BetterDatePicker;


/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
@Getter
public class SwingMain {

	private JFrame frame;
	private BetterDatePicker today;
	private JPanel mainMenu;
	private JPanel navigation;
	private JPanel total;

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
		frame.setSize(640*2, 480*2);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		total = new JPanel();
		{ // Navigation panel
			navigation = new JPanel();
			navigation.setLayout(new BorderLayout());

			JButton btnBack = new JButton("< Back");
			btnBack.addActionListener(e -> {
				setMainPanel(mainMenu);
				setNavigation(false);
			});
			JLabel lblTitle = new JLabel("Title");
			navigation.add(btnBack, BorderLayout.WEST);
			navigation.add(lblTitle, BorderLayout.EAST);
			setNavigation(false);
		} { // mainMenu
			mainMenu = new JPanel();

			JButton btnResponsable = new JButton("Responsable de formación");
			JButton btnSecretaria = new JButton("Secretaria administrativa");
			JButton btnProfesional = new JButton("Profesional (alumnado)");

			today = new BetterDatePicker();
			today.setDateToToday();

			TabbedFrame profesional = new TabsProfesional(this);
			TabbedFrame responsable = new TabsResponsable(this);
			TabbedFrame secretaria = new TabsSecretaria(this);

			btnProfesional.addActionListener(e -> {setMainPanel(profesional.getComponent());});
			btnResponsable.addActionListener(e -> {setMainPanel(responsable.getComponent());});
			btnSecretaria.addActionListener(e -> {setMainPanel(secretaria.getComponent());});

			mainMenu.add(today);
			mainMenu.add(btnResponsable);
			mainMenu.add(btnSecretaria);
			mainMenu.add(btnProfesional);
		}

		setMainPanel(mainMenu);
		setNavigation(false);
		frame.add(total);
	}

	public JFrame getFrame() { return this.frame; }
	public void setMainPanel(JComponent panel) {
		total.removeAll();
		total.add(navigation);
		total.add(panel);
		total.repaint();
		total.revalidate();
		setNavigation(true);
	}

	public JPanel getMainMenu() { return this.mainMenu; }
	public LocalDate getToday() { return getTodayPicker().getDate();}
	public BetterDatePicker getTodayPicker() { return this.today; }
	public void setNavigation(boolean visible) { navigation.setVisible(visible); }
}
