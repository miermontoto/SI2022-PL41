package g41.si2022.coiipa.consultar_cursos;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class ConsultarCursosView extends Tab {
	
	private static final long serialVersionUID = 1L;
	private JTable tablaCursos;
	private JTable tablaInscr;
	private JTable tablaBalance;
	private JButton btnCargarDatos;
	private JScrollPane scrollPane;
	
	public ConsultarCursosView(SwingMain main) {
		super(main);
		initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));
		
		btnCargarDatos = new JButton("Cargar cursos");
		tablaCursos = new JTable();
		tablaCursos.setDefaultEditor(Object.class, null);
		
		JPanel panel1 = new JPanel();
		panel1.add(btnCargarDatos, BorderLayout.WEST);
		this.add(panel1, BorderLayout.NORTH);
		
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout());
		this.add(centerPanel, BorderLayout.CENTER);
		
		JPanel panel2 = new JPanel();
		JLabel lblCursos = new JLabel("Seleccionar curso:");
		panel2.setLayout(new BorderLayout());
		panel2.add(lblCursos, BorderLayout.NORTH);
		panel2.add(tablaCursos, BorderLayout.CENTER);
		centerPanel.add(panel2);
		
		JPanel panel3 = new JPanel();
		JLabel lblInscr = new JLabel("Inscripciones:");
		panel3.setLayout(new BorderLayout());
		panel3.add(lblInscr, BorderLayout.NORTH);
		centerPanel.add(panel3);
		
		JPanel panel4 = new JPanel();
		JLabel lblBalance = new JLabel("Balance económico:");
		panel4.setLayout(new BorderLayout());
		panel4.add(lblBalance, BorderLayout.NORTH);
		centerPanel.add(panel4);	
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
	
	public JTable getTablaCursos() { return this.tablaCursos ;}
	
}
