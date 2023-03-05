package g41.si2022.coiipa.registrar_pago;

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
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.Tab;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import javax.swing.JCheckBox;

@Getter
public class RegistrarPagoView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JPanel formPanel;
	private JLabel lblNombreInscripcion;
	private JLabel lblTitulo;
	private JButton btnInsertarPago;

	private DatePicker datePicker;
	private JFormattedTextField txtImporte;
	private JLabel lblError;
	private JPanel panel;
	private JCheckBox chkAll;

	public RegistrarPagoView(SwingMain main) {
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

		formPanel.add(lblTitulo, "cell 0 0,growx,aligny top");
		lblTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

				JLabel label = new JLabel("Nombre del inscrito: ");
				formPanel.add(label, "cell 0 1,alignx left,aligny center");
		lblNombreInscripcion = new JLabel("No se ha selecciona ningún nombre");
		formPanel.add(lblNombreInscripcion, "cell 0 2,alignx left,aligny center");
		JLabel label_1 = new JLabel("Introducir importe recibido (€): ");
		formPanel.add(label_1, "cell 0 3,alignx left,aligny center");
		formPanel.add(new JLabel("Introducir fecha del pago:"), "cell 0 5");

	    NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(true);
	    formatter.setCommitsOnValidEdit(false);

		formPanel.add(txtImporte = new JFormattedTextField(formatter), "cell 0 4,growx");
		formPanel.add(datePicker = new DatePicker(), "cell 0 6,growx,aligny center");

				btnInsertarPago = new JButton("Insertar pago");
				formPanel.add(btnInsertarPago, "cell 0 8");

		lblError = new JLabel("");
		formPanel.add(lblError, "cell 0 10");
		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

	@Override
	public void initController() { new RegistrarPagoController(this, new RegistrarPagoModel()); }
}
