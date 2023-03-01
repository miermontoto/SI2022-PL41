package g41.si2022.coiipa.consultar_cursos;

import javax.swing.JTable;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class ConsultarCursosView extends Tab{
	
	private static final long serialVersionUID = 1L;
	private JTable tablaCursos;
	private JTable tablaInscr;
	private JTable tablaBalance;
	
	public ConsultarCursosView(SwingMain main) {
		super(main);
		this.initialize();
	}
	
	private void initialize() {
		tablaCursos = new JTable();
		tablaCursos.setModel(new javax.swing.table.DefaultTableModel
				());
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
}
