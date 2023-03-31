package g41.si2022.ui;

import java.awt.event.MouseEvent;
import java.awt.Insets;
import java.awt.EventQueue;
import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.GridBagConstraints;
import java.awt.Desktop;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.event.MouseInputAdapter;

import com.formdev.flatlaf.FlatLightLaf;
import lombok.Getter;
import g41.si2022.mvc.View;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.panels.Debug;
import g41.si2022.ui.panels.TabbedFrame;
import g41.si2022.ui.panels.TabsProfesional;
import g41.si2022.ui.panels.TabsResponsable;
import g41.si2022.ui.panels.TabsSecretaria;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;
import g41.si2022.util.Pair;
import g41.si2022.util.db.Database;

/**
 * Punto de entrada principal que incluye botones para la ejecucion de las pantallas
 * de las aplicaciones de ejemplo
 * y acciones de inicializacion de la base de datos.
 * No sigue MVC pues es solamente temporal para que durante el desarrollo se tenga posibilidad
 * de realizar acciones de inicializacion
 */
@Getter
public class SwingMain {

	public static final int DEFAULT_WINDOW_WIDTH = 1280;
	public static final int DEFAULT_WINDOW_HEIGHT = 720;

	private JFrame frame;
	private BetterDatePicker today;
	private JPanel mainMenu;
	private JPanel navigation;
	private JPanel total;
	private JLabel lblTitle;
	private JComponent[] passProtected;
	private static boolean isDark = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FlatLightLaf.setup();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Database db = new Database(); // Avoid crashing if the db is not created
					if (db.createDatabase(true)) db.loadDatabase();
					SwingMain window = new SwingMain();
					window.frame.setVisible(true);
				} catch (Exception e) { e.printStackTrace(); }
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException
	 */
	public SwingMain() throws IOException {
		frame = new JFrame();
		frame.setTitle("Programa de gestión del COIIPA");
		frame.setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		total = new JPanel();
		total.setLayout(new GridBagLayout());

		this.makeNavigation();

		// mainMenu
		mainMenu = new JPanel();

		today = new BetterDatePicker();
		today.setDateToToday();

		mainMenu.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainMenu.add(JLabelFactory.getLabel(FontType.title, "  Selección de usuario"), gbc);

		Map<String, Pair<JButton, TabbedFrame>> tabbedFrameButtons = new TreeMap<>();

		tabbedFrameButtons.put("Secretaría administrativa", new Pair<JButton, TabbedFrame>(new JButton(), new TabsSecretaria(this)));
		tabbedFrameButtons.put("Responsable de formación", new Pair<JButton, TabbedFrame>(new JButton(), new TabsResponsable(this)));
		tabbedFrameButtons.put("Alumnado", new Pair<JButton, TabbedFrame>(new JButton(), new TabsProfesional(this)));

		tabbedFrameButtons.forEach((name, pair) -> {
			pair.getFirst().setText(name);
			pair.getFirst().addActionListener(e -> {
				setMainPanel(pair.getSecond().getComponent(), name);
				((View) ((javax.swing.JTabbedPane) pair.getSecond().getComponent()).getSelectedComponent()).initVolatileData();
			});
		});

		int i = 1;
		for(Pair<JButton, TabbedFrame> v : tabbedFrameButtons.values()) {
			gbc.gridy = i++;
			mainMenu.add(v.getFirst(), gbc);
		}

		this.makeDebug(gbc);
		this.makeLogo(gbc);

		setMainPanel(mainMenu);
		setNavigation(false);

		frame.getContentPane().add(total);
	}

	private void makeLogo (GridBagConstraints gbc) {
		try {
			gbc.gridx = 2;
			gbc.gridy = 0;
			gbc.gridheight = 6;
			JLabel img = new JLabel(new ImageIcon(ImageIO.read(new File("src/main/resources/logo.png"))));
			mainMenu.add(img, gbc);
			img.addMouseListener(new MouseInputAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URL("https://www.coiipa.org/").toURI());
					} catch (Exception ex) {}
				}
			});
		} catch (Exception e) {}
	}

	private void makeDebug (GridBagConstraints gbc) {
		gbc.gridx = 0;
		gbc.gridy = 6;
		mainMenu.add(new JLabel("Hoy:"), gbc);
		gbc.gridx = 1;
		mainMenu.add(today, gbc);

		gbc.gridx = 2;
		gbc.gridy = 6;
		JButton btnDebug = new JButton("Debug menu");
		btnDebug.addActionListener(e -> {setMainPanel(new Debug(this).getComponent(), "Debug menu");});
		mainMenu.add(btnDebug, gbc);
	}

	private void makeNavigation () {
		this.navigation = new JPanel();
		this.navigation.setLayout(new BorderLayout());

		JButton btnBack = new JButton("← Volver");
		btnBack.addActionListener(e -> {
			setMainPanel(mainMenu);
			setNavigation(false);
		});
		lblTitle = JLabelFactory.getLabel(FontType.subtitle, "test");
		this.navigation.add(btnBack, BorderLayout.WEST);
		this.navigation.add(lblTitle, BorderLayout.EAST);
		this.navigation.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.SOUTH);
	}

	public JFrame getFrame() { return this.frame; }

	public void setMainPanel(JComponent panel, String title) {
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
		lblTitle = JLabelFactory.getLabel(FontType.subtitle, title + " ");
		navigation.add(lblTitle, BorderLayout.EAST);
	}

	public void setMainPanel(JComponent panel) {
		setMainPanel(panel, panel.getName());
	}

	public JPanel getMainMenu() { return this.mainMenu; }
	public LocalDate getToday() { return getTodayPicker().getDate(); }
	public BetterDatePicker getTodayPicker() { return this.today; }
	public void setNavigation(boolean visible) { navigation.setVisible(visible); }
}
