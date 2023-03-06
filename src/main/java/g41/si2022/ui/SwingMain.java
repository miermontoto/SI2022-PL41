package g41.si2022.ui;

import java.awt.EventQueue;
import java.time.LocalDate;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;

import g41.si2022.util.BetterDatePicker;
import net.miginfocom.swing.MigLayout;


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
	private JLabel lblTitle;

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
		frame.setSize(1280, 720);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		total = new JPanel();
		total.setLayout(new GridBagLayout());

		navigation = new JPanel();
		navigation.setLayout(new BorderLayout());

		JButton btnBack = new JButton("< Back");
		btnBack.addActionListener(e -> {
			setMainPanel(mainMenu);
			setNavigation(false);
		});
		JLabel lblTitle = new JLabel("");
		navigation.add(btnBack, BorderLayout.WEST);
		navigation.add(lblTitle, BorderLayout.EAST);
		// mainMenu
		mainMenu = new JPanel();

		today = new BetterDatePicker();
		today.setDateToToday();

		TabbedFrame profesional = new TabsProfesional(this);
		TabbedFrame responsable = new TabsResponsable(this);
		TabbedFrame secretaria = new TabsSecretaria(this);
		mainMenu.setLayout(new MigLayout("", "[174px][222px][214px]", "[25px][][][][][][][][]"));

		mainMenu.add(new JLabel("Today:"), "cell 0 6,alignx right,aligny center");
		mainMenu.add(today, "cell 1 6,alignx left,aligny center");

		setMainPanel(mainMenu);
		setNavigation(false);

		JButton btnSecretaria = new JButton("Secretaria administrativa");
		btnSecretaria.addActionListener(e -> {setMainPanel(secretaria.getComponent());});
		mainMenu.add(btnSecretaria, "cell 1 0,alignx left,aligny top");

		JButton btnResponsable = new JButton("Responsable de formación");
		btnResponsable.addActionListener(e -> {setMainPanel(responsable.getComponent());});
		mainMenu.add(btnResponsable, "cell 1 2,alignx left,aligny top");

		JButton btnProfesional = new JButton("Profesional (alumnado)");
		btnProfesional.addActionListener(e -> {setMainPanel(profesional.getComponent());});
		mainMenu.add(btnProfesional, "cell 1 4,alignx left,aligny top");

		JButton btnDebug = new JButton("Debug menu");
		btnDebug.addActionListener(e -> {setMainPanel(new Debug(this).getComponent());});
		mainMenu.add(btnDebug, "cell 1 8,alignx left,aligny top");

		frame.getContentPane().add(total);
	}

	public JFrame getFrame() { return this.frame; }
	public void setMainPanel(JComponent panel) {
		if (panel instanceof JTabbedPane) {
			((Tab) ((JTabbedPane) panel).getSelectedComponent()).initController();
		}
		GridBagConstraints gbc = new GridBagConstraints();
		total.removeAll();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		total.add(navigation, gbc);
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		if (panel == mainMenu) {
			gbc.weightx = 0;
			gbc.weighty = 0;
		} else {
			gbc.weightx = 1;
			gbc.weighty = 1;
		}
		total.add(panel, gbc);
		total.repaint();
		total.revalidate();
		setNavigation(true);
	}

	public JPanel getMainMenu() { return this.mainMenu; }
	public LocalDate getToday() { return getTodayPicker().getDate();}
	public BetterDatePicker getTodayPicker() { return this.today; }
	public void setNavigation(boolean visible) { navigation.setVisible(visible); }
}
