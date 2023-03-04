package g41.si2022.coiipa.insertar_devolucion;

import java.awt.BorderLayout;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import com.github.lgooddatepicker.components.DatePicker;
import lombok.Getter;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import javax.swing.JCheckBox;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;

@Getter

public class InsertarDevolucionView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JPanel formPanel;
	private JLabel lblNombreInscripcion;
	private JLabel lblTitulo;
	private JButton btnCancelarInscripcion;

	private DatePicker datePicker;
	private JFormattedTextField txtImporte;
	private JLabel lblError;
	private JPanel panel;
	private JCheckBox chkAll;
	private JLabel lblImporteDevuelto;

	public InsertarDevolucionView(SwingMain main) {
		super(main);
		formPanel = new JPanel();
		this.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		chkAll = new JCheckBox("Ver todas las inscripciones");
		chkAll.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chkAll);

		formPanel.setLayout(new MigLayout("", "[122.00px,grow][]", "[28.00,top][20.00px,center][21.00][22.00][22.00][][][][][][]"));
		this.add(formPanel, BorderLayout.EAST);

		lblTitulo = JLabelFactory.getLabel(FontType.title, "Insertar un nuevo pago");
		lblTitulo.setText("Cancelar inscripción:");

		formPanel.add(lblTitulo, "cell 0 0,growx,aligny top");
		lblTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		
				JLabel label = new JLabel("Nombre del inscrito: ");
				formPanel.add(label, "cell 0 1,alignx left,aligny center");
		lblNombreInscripcion = new JLabel("No se ha seleccionado ningún nombre");
		formPanel.add(lblNombreInscripcion, "cell 0 2,alignx left,aligny center");
		JLabel lblFecha = new JLabel("Seleccionar fecha de hoy:");
		formPanel.add(lblFecha, "cell 0 3");

		formPanel.add(datePicker = new DatePicker(), "cell 0 4,growx,aligny center");
				JLabel lblImporteADevolver = new JLabel("Importe a devolver:");
				formPanel.add(lblImporteADevolver, "cell 0 5,alignx left,aligny center");
				
				lblImporteDevuelto = new JLabel("");
				formPanel.add(lblImporteDevuelto, "cell 0 6");
		
				btnCancelarInscripcion = new JButton("Cancelar inscripción");
				formPanel.add(btnCancelarInscripcion, "cell 0 8");
		
		lblError = new JLabel("");
		formPanel.add(lblError, "cell 0 10");
		tableInscripciones = new JTable();
		tableInscripciones.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
			}
		));
		JScrollPane scrollPane_1 = new JScrollPane(tableInscripciones);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.add(scrollPane_1, BorderLayout.WEST);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

	@Override
	public void initController() { new InsertarDevolucionController(this, new InsertarDevolucionModel()); }
}