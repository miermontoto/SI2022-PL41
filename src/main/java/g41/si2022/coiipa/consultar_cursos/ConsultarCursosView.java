package g41.si2022.coiipa.consultar_cursos;

import java.awt.BorderLayout;

import javax.swing.JButton;
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
		this.setLayout(new BorderLayout(1, 1));
		
		btnCargarDatos = new JButton("Cargar datos ahora");
		tablaCursos = new JTable();
		tablaCursos.setDefaultEditor(Object.class, null);
		
		
		JPanel panel1 = new JPanel();
		panel1.add(btnCargarDatos, BorderLayout.EAST);
		this.add(panel1, BorderLayout.NORTH);
		
		
		JPanel panel2 = new JPanel();
		
		
		JPanel panel3 = new JPanel();
		
		
		JPanel panel4 = new JPanel();
		
		
		
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
	
	public JTable getTablaCursos() { return this.tablaCursos ;}
	
}
