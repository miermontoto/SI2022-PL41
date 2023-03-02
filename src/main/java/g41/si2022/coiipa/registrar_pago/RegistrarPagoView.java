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

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;

@Getter
public class RegistrarPagoView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JPanel formPanel;
	private JLabel lblNombreInscripcion;
	private JLabel lblTitulo;
	private JButton btnInsertarPago;

	DatePicker datepicker = new DatePicker();
	private JFormattedTextField txtImporte;

	public RegistrarPagoView(SwingMain main) {
		super(main);
		this.setLayout(new BorderLayout(0, 0));

		formPanel = new JPanel();
		formPanel.setLayout(new MigLayout("", "[122.00px,grow][]", "[][14px][][][][][][][]"));
		add(formPanel, BorderLayout.EAST);

		formPanel.add(lblTitulo = JLabelFactory.getLabel(FontType.title, "Insertar un nuevo pago"),
				"cell 0 0,alignx left,aligny top");
		lblTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		formPanel.add(new JLabel("Nombre del inscrito: "), "cell 0 1,alignx left,aligny center");

		lblNombreInscripcion = new JLabel("N/A");
		formPanel.add(lblNombreInscripcion, "cell 0 2,alignx left,aligny center");

		formPanel.add(new JLabel("Introducir importe recibido (€): "), "cell 0 3,alignx left,aligny center");

		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(true);
	    formatter.setCommitsOnValidEdit(true);

		txtImporte = new JFormattedTextField(formatter);
		formPanel.add(txtImporte, "cell 0 4,growx");

		formPanel.add(new JLabel("Introducir fecha del pago:"), "cell 0 5");
		formPanel.add(datepicker, "cell 0 6,growx,aligny center");

		btnInsertarPago = new JButton("Insertar pago");

		formPanel.add(btnInsertarPago, "cell 0 8");
		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

	@Override
	public void initController() { new RegistrarPagoController(this, new RegistrarPagoModel()); }
}
