package g41.si2022.coiipa.registrar_pago_profesor;

import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import net.miginfocom.swing.MigLayout;
import lombok.Getter;

import g41.si2022.util.BetterDatePicker;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.Tab;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import javax.swing.JCheckBox;

@Getter
public class RegistrarPagoProfesorView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JPanel formPanel;
	private JLabel lblNombreDocente;
	private JLabel lblNombreCurso;
	private JLabel lblTitulo;
	private JButton btnInsertarPago;

	private BetterDatePicker datePicker;
	private JFormattedTextField txtImporte;
	private JLabel lblError;
	private JPanel panel;
	private JCheckBox chkAll;

	public RegistrarPagoProfesorView(SwingMain main) {
		super(main);
		formPanel = new JPanel();
		this.setLayout(new BorderLayout(0, 0));

		panel = new JPanel();
		add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		chkAll = new JCheckBox("Ver todas las facturas");
		chkAll.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chkAll);

		formPanel.setLayout(new MigLayout("", "[122.00px,grow][]", "[28.00,top][20.00px,center][21.00][22.00][22.00][][][][][][]"));
		this.add(formPanel, BorderLayout.EAST);

		lblTitulo = JLabelFactory.getLabel(FontType.title, "Pagar facturas");

		formPanel.add(lblTitulo, "cell 0 0,growx,aligny top");
		lblTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblDocente = new JLabel("Nombre del docente: ");
		formPanel.add(lblDocente, "cell 0 1,alignx left,aligny center");
		lblNombreDocente = new JLabel("N/A");
		formPanel.add(lblNombreDocente, "cell 0 2,alignx left,aligny center");

		JLabel lblCurso = new JLabel("Nombre del curso: ");
		formPanel.add(lblCurso, "cell 0 3,alignx left,aligny center");
		lblNombreCurso = new JLabel("N/A");
		formPanel.add(lblNombreCurso, "cell 0 4,alignx left,aligny center");

		formPanel.add(new JLabel("Introducir fecha del pago:"), "cell 0 5");
		formPanel.add(datePicker = new BetterDatePicker(), "cell 0 6,growx,aligny center");

		btnInsertarPago = new JButton("Registrar pago");
		formPanel.add(btnInsertarPago, "cell 0 8");

		lblError = new JLabel("");
		formPanel.add(lblError, "cell 0 10");
		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

	@Override
	public void initController() { new RegistrarPagoProfesorController(this, new RegistrarPagoProfesorModel()); }
}
