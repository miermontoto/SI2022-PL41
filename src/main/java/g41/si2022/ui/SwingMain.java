package g41.si2022.ui;

import java.awt.Insets;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.awt.GridBagConstraints;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;

import g41.si2022.util.BetterDatePicker;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import g41.si2022.util.Database;


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
					Database db = new Database();
					if (db.createDatabase(true)) db.loadDatabase();
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
		frame = new JFrame();
		frame.setTitle("Programa de gestión del COIIPA");
		frame.setSize(1280, 720);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		total = new JPanel();
		total.setLayout(new GridBagLayout());

		navigation = new JPanel();
		navigation.setLayout(new BorderLayout());

		JButton btnBack = new JButton("← Volver");
		btnBack.addActionListener(e -> {
			setMainPanel(mainMenu);
			setNavigation(false);
		});
		lblTitle = JLabelFactory.getLabel(FontType.subtitle, "test");
		navigation.add(btnBack, BorderLayout.WEST);
		navigation.add(lblTitle, BorderLayout.EAST);
		navigation.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
		// mainMenu
		mainMenu = new JPanel();

		today = new BetterDatePicker();
		today.setDateToToday();

		TabbedFrame profesional = new TabsProfesional(this);
		TabbedFrame responsable = new TabsResponsable(this);
		TabbedFrame secretaria = new TabsSecretaria(this);
		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainMenu.add(JLabelFactory.getLabel(FontType.title, "  Selección de usuario"), gbc);

		gbc.gridy = 1;
		JButton btnSecretaria = new JButton("Secretaria administrativa");
		btnSecretaria.addActionListener(e -> {setMainPanel(secretaria.getComponent(), "Secretaría administrativa");});
		mainMenu.add(btnSecretaria, gbc);

		gbc.gridy = 2;
		JButton btnResponsable = new JButton("Responsable de formación");
		btnResponsable.addActionListener(e -> {setMainPanel(responsable.getComponent(), "Responsable de formación");});
		mainMenu.add(btnResponsable, gbc);

		gbc.gridy = 3;
		JButton btnProfesional = new JButton("Profesional (alumnado)");
		btnProfesional.addActionListener(e -> {setMainPanel(profesional.getComponent(), "Alumnado");});
		mainMenu.add(btnProfesional, gbc);

		gbc.gridx = 0;
		gbc.gridy = 6;
		mainMenu.add(new JLabel("Today:"), gbc);
		gbc.gridx = 1;
		mainMenu.add(today, gbc);

		gbc.gridx = 2;
		gbc.gridy = 6;
		JButton btnDebug = new JButton("Debug menu");
		btnDebug.addActionListener(e -> {setMainPanel(new Debug(this).getComponent(), "Debug menu");});
		mainMenu.add(btnDebug, gbc);

		try {
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridheight = 6;
			mainMenu.add(new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/logo.png")))), gbc);
		} catch (IOException ioe) {}

		setMainPanel(mainMenu);
		setNavigation(false);

		frame.getContentPane().add(total);
	}

	public JFrame getFrame() { return this.frame; }
	public void setMainPanel(JComponent panel, String title) {
		if (panel instanceof JTabbedPane) {
			((Tab) ((JTabbedPane) panel).getSelectedComponent()).initController();
		}
		GridBagConstraints gbc = new GridBagConstraints();
		total.removeAll();

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.ipady = 20; // Make "back" button bigger
		gbc.fill = GridBagConstraints.HORIZONTAL;
		total.add(navigation, gbc);
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.BOTH;
		if (panel == mainMenu) { // Center only main menu
			gbc.weightx = 0;
			gbc.weighty = 0;
		} else { // Fill the rest
			gbc.weightx = 1;
			gbc.weighty = 1;
		}
		updateTitle(title);
		total.add(panel, gbc);
		total.repaint();
		total.revalidate();
		setNavigation(true);
	}

	public void updateTitle(String title) {
		navigation.remove(lblTitle);
		lblTitle = JLabelFactory.getLabel(FontType.subtitle, title+" ");
		navigation.add(lblTitle, BorderLayout.EAST);
	}

	public void setMainPanel(JComponent panel) {
		setMainPanel(panel, panel.getName());
	}

	public JPanel getMainMenu() { return this.mainMenu; }
	public LocalDate getToday() { return getTodayPicker().getDate();}
	public BetterDatePicker getTodayPicker() { return this.today; }
	public void setNavigation(boolean visible) { navigation.setVisible(visible); }
}
