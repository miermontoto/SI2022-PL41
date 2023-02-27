package g41.si2022.coiipa.registrar_pago;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.CardLayout;
import javax.swing.SwingConstants;
import java.awt.Font;

public class RegistrarPagoView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	JScrollPane scrollPane; // Panel de scroll de la tabla
	private JButton botoncarga;
	private JPanel panel_1;
	private JLabel infoidinscripcion;
	private JLabel idinscripcion;
	private JLabel infoquehace;

	//private final JTable tabCursos;


	/**
	 * Create the panel.
	 */
	public RegistrarPagoView(SwingMain main) {
		super(main);
		setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new MigLayout("", "[148px][18px][]", "[][14px][][]"));
		
		infoquehace = new JLabel("Insertar un nuevo pago");
		infoquehace.setFont(new Font("Arial", Font.PLAIN, 14));
		infoquehace.setVerticalAlignment(SwingConstants.TOP);
		infoquehace.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(infoquehace, "cell 0 0,alignx left,aligny top");
		
		infoidinscripcion = new JLabel("Id de inscripción seleccionado: ");
		infoidinscripcion.setVerticalAlignment(SwingConstants.TOP);
		panel_1.add(infoidinscripcion, "cell 0 2,alignx left,aligny top");
		
		idinscripcion = new JLabel("N/A");
		panel_1.add(idinscripcion, "cell 1 2,alignx left,aligny top");

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		botoncarga = new JButton("Cargar datos ahora");
		panel.add(botoncarga);
		tableInscripciones = new JTable();
		scrollPane = new JScrollPane(tableInscripciones); //Añado un panel de scroll
		add(scrollPane); //Lo añado a la vista

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
		RegistrarPagoController controlador = new RegistrarPagoController(this, new RegistrarPagoModel()); // Inicializo el controlador finalmente
	}

	public JButton getBtnNewButton() {
		return botoncarga;
	}

	public void setBtnNewButton(JButton btnNewButton) {
		this.botoncarga = btnNewButton;
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

	@Override
	public void initController() { }
}
