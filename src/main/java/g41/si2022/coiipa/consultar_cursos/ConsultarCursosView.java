package g41.si2022.coiipa.consultar_cursos;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class ConsultarCursosView extends Tab {
	
	private static final long serialVersionUID = 1L;
	private JTable tablaCursos;
	private JTable tablaInscr;
	private JTable tablaBalance;
	
	public ConsultarCursosView(SwingMain main) {
		super(main);
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		
		// ------------- BorderLayout -> NORTH -------------
		// El botón sobra pero lo dejo porque mola
		JButton btnCargarDatos = new JButton("Cargar cursos");
		JPanel panel1 = new JPanel();
		// Add elements
		panel1.add(btnCargarDatos, BorderLayout.WEST);
		this.add(panel1, BorderLayout.NORTH);
		
		// ------------- BorderLayout -> CENTER -------------
		JPanel centerPanel = new JPanel();
		// Add elements
		centerPanel.setLayout(new GridLayout(3, 0));
		this.add(centerPanel, BorderLayout.CENTER);
		
		// lblCursos + tablaCursos
		JPanel panel2 = new JPanel();
		JLabel lblCursos = new JLabel("Seleccionar curso:");
		tablaCursos = new JTable();
		JScrollPane scrCursos = new JScrollPane(tablaCursos);
		tablaCursos.setDefaultEditor(Object.class, null);
		// Add elements
		panel2.setLayout(new BorderLayout());
		panel2.add(lblCursos, BorderLayout.NORTH);
		panel2.add(scrCursos, BorderLayout.CENTER);
		centerPanel.add(panel2);
		
		// lblInscr + tablaInscr
		JPanel panel3 = new JPanel();
		JLabel lblInscr = new JLabel("Inscripciones:");
		JScrollPane scrInscr = new JScrollPane();
		tablaInscr = new JTable();
		tablaInscr.setDefaultEditor(Object.class, null);
		// Add elements
		panel3.setLayout(new BorderLayout());
		panel3.add(lblInscr, BorderLayout.NORTH);
		panel3.add(tablaInscr, BorderLayout.CENTER);
		panel3.add(scrInscr, BorderLayout.EAST);
		centerPanel.add(panel3);
		
		// lblBalance + tablaBalance
		JPanel panel4 = new JPanel();
		JLabel lblBalance = new JLabel("Balance económico:");
		JScrollPane scrBalance = new JScrollPane();
		tablaBalance = new JTable();
		tablaBalance.setDefaultEditor(Object.class, null);
		// Add elements
		panel4.setLayout(new BorderLayout());
		panel4.add(lblBalance, BorderLayout.NORTH);
		panel4.add(tablaBalance, BorderLayout.CENTER);
		panel4.add(scrBalance, BorderLayout.EAST);
		centerPanel.add(panel4);	
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
	
	public JTable getTablaCursos() { return this.tablaCursos; }
	public JTable getTablaInscr() { return this.tablaInscr; }
	public JTable getTablaBalance() { return this.tablaBalance; }
	
}
