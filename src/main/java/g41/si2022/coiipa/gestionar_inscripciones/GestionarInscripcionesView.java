package g41.si2022.coiipa.gestionar_inscripciones;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ListSelectionModel;
import javax.swing.text.NumberFormatter;

import net.miginfocom.swing.MigLayout;
import com.github.lgooddatepicker.components.DatePicker;
import lombok.Getter;
import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import javax.swing.JCheckBox;

@Getter
public class GestionarInscripcionesView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JLabel lblNombreSeleccionado1;
	private JLabel lblNombreSeleccionado2;
	private JLabel lblCalculoDevolucion;
	private JButton btnInsertarPago;
	private JButton btnCancelarInscripcion;

	private DatePicker datePicker;
	private JFormattedTextField txtImporte;
	private JPanel panel;
	private JCheckBox chkAll;

	public GestionarInscripcionesView(SwingMain main) {
		super(main, GestionarInscripcionesModel.class, GestionarInscripcionesView.class, GestionarInscripcionesController.class);
	}

	@Override
	protected void initView () {
		this.setLayout(new BorderLayout(0, 0));

		JPanel handlePanel = new JPanel();
		handlePanel.setLayout(new BorderLayout());
		JPanel pagarPanel = new JPanel();
		JPanel devolverPanel = new JPanel();

		lblNombreSeleccionado1 = new JLabel();
		lblNombreSeleccionado2 = new JLabel();

		panel = new JPanel();
		this.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		chkAll = new JCheckBox("Ver todas las inscripciones");
		chkAll.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(chkAll);

		NumberFormatter formatter = new NumberFormatter(NumberFormat.getInstance());
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(Integer.MIN_VALUE);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(true);
	    formatter.setCommitsOnValidEdit(true);

		handlePanel.add(pagarPanel, BorderLayout.NORTH);
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.HORIZONTAL);
		handlePanel.add(devolverPanel, BorderLayout.CENTER);
		this.add(handlePanel, BorderLayout.EAST);

		pagarPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(5, 5, 5, 5);
		Insets next = new Insets(0, 5, 5, 5);

		JLabel lblPagarTitulo = JLabelFactory.getLabel(FontType.title, "Registrar pago");
		lblPagarTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblPagarTitulo.setHorizontalAlignment(SwingConstants.CENTER);

		gbc.insets = spacer;
		gbc.gridx = 0;
		gbc.gridy = -1;



		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		pagarPanel.add(lblPagarTitulo, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.WEST;
		pagarPanel.add(new JLabel("Inscrito: "), gbc);

		gbc.insets = next;
		gbc.gridy = 2;
		pagarPanel.add(lblNombreSeleccionado1, gbc);

		gbc.insets = spacer;
		gbc.gridy = 3;
		pagarPanel.add(new JLabel("Introducir importe recibido (€): "), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		pagarPanel.add(txtImporte = new JFormattedTextField(formatter), gbc);

		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		pagarPanel.add(new JLabel("Introducir fecha del pago:"), gbc);

		gbc.insets = next;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanel.add(datePicker = new DatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 7;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanel.add(btnInsertarPago = new JButton("Insertar pago"), gbc);

		gbc.gridy = 8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		pagarPanel.add(separator, gbc);

		devolverPanel.setLayout(new MigLayout("", "[122.00px,grow][]", "[28.00,top][20.00px,center][21.00][22.00][22.00][][][][][][]"));
		JLabel lblDevolverTitulo = JLabelFactory.getLabel(FontType.title, "Cancelar inscripción");
		devolverPanel.add(lblDevolverTitulo, "cell 0 0,growx,aligny top");
		lblDevolverTitulo.setVerticalAlignment(SwingConstants.TOP);
		lblDevolverTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		devolverPanel.add(new JLabel("Nombre del inscrito: "), "cell 0 1,alignx left,aligny center");
		devolverPanel.add(lblNombreSeleccionado2, "cell 0 2,alignx left,aligny center");
		devolverPanel.add(new JLabel("Importe a devolver:"), "cell 0 3,alignx left,aligny center");
		lblCalculoDevolucion = new JLabel("");
		devolverPanel.add(lblCalculoDevolucion, "cell 0 4,alignx left,aligny center");
		btnCancelarInscripcion = new JButton("Cancelar inscripción");
		devolverPanel.add(btnCancelarInscripcion, "cell 0 8");

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

}
