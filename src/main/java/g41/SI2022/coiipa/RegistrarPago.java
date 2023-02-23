package g41.SI2022.coiipa;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import g41.SI2022.util.SwingMain;
import g41.SI2022.util.Tab;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JButton;

public class RegistrarPago extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; //Contenedor de la tabla de inscripciones
	JScrollPane scrollPane; //Panel de scroll de la tabla
	private JButton btnNewButton;
	/**
	 * @wbp.nonvisual location=41,-21
	 */
	
	//private final JTable tabCursos;
	

	/**
	 * Create the panel.
	 */
	public RegistrarPago(SwingMain main) {
		super(main);
		this.setLayout(new BorderLayout());
		
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		
		btnNewButton = new JButton("Cargar datos ahora");
		panel.add(btnNewButton);
		tableInscripciones = new JTable();
		scrollPane = new JScrollPane(tableInscripciones); //Añado un panel de scroll
		add(scrollPane, BorderLayout.CENTER); //Lo añado a la vista
		
		

		// TODO: Tabla inicial para mostrar.

		//getContentPane().add(tablePanel, "cell 0 5,grow");
		
		
		//NO ES MVC, pero para testing, pruebo a cargar la tabla con datos
		String [] columnas = {"Inscripción", "Nombre", "Estado"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		String [] testData = {"123", "Pepito", "SIN PAGAR"};
		modelo.addRow(testData);
		tableInscripciones.setModel(modelo);
		tableInscripciones.setName("tabCursos");
		//tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//tableInscripciones.setDefaultEditor(Object.class, null); //Leer sólo.
		RegistrarPagoController controlador = new RegistrarPagoController(this, new RegistrarPagoModel()); //Inicializo el controlador finalmente

		
	}

	public JButton getBtnNewButton() {
		return btnNewButton;
	}

	public void setBtnNewButton(JButton btnNewButton) {
		this.btnNewButton = btnNewButton;
	}

	public JTable getTableInscripciones() {
		return tableInscripciones;
	}

	public void setTableInscripciones(JTable tableInscripciones) {
		this.tableInscripciones = tableInscripciones;
	}

	public JScrollPane getScrollPane() {
		return scrollPane;
	}

	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
