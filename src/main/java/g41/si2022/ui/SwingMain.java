package g41.si2022.ui;

import java.awt.EventQueue;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
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
	private JPanel mainPanel;
	private BetterDatePicker today;

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

		mainPanel = new JPanel();

		JButton btnResponsable = new JButton("Responsable de formación");
		JButton btnSecretaria = new JButton("Secretaria administrativa");
		JButton btnProfesional = new JButton("Profesional (alumnado)");

		today = new BetterDatePicker();
		today.setDateToToday();
		mainPanel.add(today);
		mainPanel.add(btnResponsable);
		mainPanel.add(btnSecretaria);
		mainPanel.add(btnProfesional);

		btnProfesional.addActionListener(e -> new TabsProfesional(this));
		btnResponsable.addActionListener(e -> new TabsResponsable(this));
		btnSecretaria.addActionListener(e -> new TabsSecretaria(this));

		frame.add(mainPanel);
		frame.repaint();
		frame.revalidate();
	}

	public JFrame getFrame() { return this.frame; }

	public LocalDate getToday() { return this.today.getDate();}
	public BetterDatePicker getTodayPicker() { return this.today;}
}
