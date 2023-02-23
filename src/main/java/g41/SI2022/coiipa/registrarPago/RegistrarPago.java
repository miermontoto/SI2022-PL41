package g41.SI2022.coiipa.registrarPago;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import g41.SI2022.util.Tab;

public class RegistrarPago extends Tab {

	private static final long serialVersionUID = 1L;
	/**
	 * @wbp.nonvisual location=41,-21
	 */
	private final JTable tabCursos;

	/**
	 * Create the panel.
	 */
	public RegistrarPago(g41.SI2022.util.SwingMain main) {
		super(main);
		this.setLayout(new BorderLayout());
		
		//TODO. Tabla inicial para mostrar.
		
		
		/*this.add(new JLabel("NORTH"), BorderLayout.NORTH);
		this.add(new JLabel("EAST"), BorderLayout.EAST);
		this.add(new JLabel("SOUTH"), BorderLayout.SOUTH);
		this.add(new JLabel("WEST"), BorderLayout.WEST);
		this.add(new JLabel("CENTER"), BorderLayout.CENTER);*/
		
		tabCursos = new JTable();
		tabCursos.setName("tabCursos");
		tabCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabCursos.setDefaultEditor(Object.class, null); //Leer sólo.
		JScrollPane tablePanel = new JScrollPane(tabCursos);
		//getContentPane().add(tablePanel, "cell 0 5,grow");
	}

	@Override
	protected void initController() {
		// TODO Auto-generated method stub
	}

}
