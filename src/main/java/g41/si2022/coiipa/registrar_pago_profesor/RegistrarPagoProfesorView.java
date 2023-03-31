package g41.si2022.coiipa.registrar_pago_profesor;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import lombok.Getter;
import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

import javax.swing.JCheckBox;

@Getter
public class RegistrarPagoProfesorView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JButton btnInsertarPago;
	private JButton btnInsertarFactura;

	private BetterDatePicker datePago;
	private BetterDatePicker dateFactura;
	private JXComboBox cmbCurso;
	private JXComboBox cmbProfesor;
	private JFormattedTextField txtImporte;
	private JCheckBox chkAll;

	public RegistrarPagoProfesorView(SwingMain main) {
		super(main, RegistrarPagoProfesorModel.class, RegistrarPagoProfesorView.class, RegistrarPagoProfesorController.class);
	}

	@Override
	@SuppressWarnings("unchecked") // odio los combobox
	protected void initView () {
		JPanel formPanel = new JPanel();
		this.setLayout(new BorderLayout(0, 0));

		chkAll = new JCheckBox("Ver todas las facturas");
		chkAll.setHorizontalAlignment(SwingConstants.LEFT);
		add(chkAll, BorderLayout.NORTH);

		formPanel.setLayout(new GridLayout(2, 1));
		formPanel.setPreferredSize(new java.awt.Dimension(SwingMain.DEFAULT_WINDOW_WIDTH/4, SwingMain.DEFAULT_WINDOW_HEIGHT));
		this.add(formPanel, BorderLayout.EAST);

		NumberFormatter formatter = new NumberFormatter(java.text.NumberFormat.getInstance());
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(Integer.MIN_VALUE);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(true);
	    formatter.setCommitsOnValidEdit(true);

		JXTitledPanel pagarTitledPanel = new JXTitledPanel("Pagar factura seleccionada");
		JXTitledPanel registrarTitledPanel = new JXTitledPanel("Registrar nueva factura");
		formPanel.add(pagarTitledPanel);
		formPanel.add(registrarTitledPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(10, 10, 10, 10);
		Insets next = new Insets(0, 10, 0, 10);

		JPanel pagarPanel = new JPanel();
		pagarPanel.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridx = 0;
		gbc.gridy = 0;
		pagarPanel.add(JLabelFactory.getLabel(FontType.bold, "Importe recibido (€)"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		pagarPanel.add(txtImporte = new JFormattedTextField(formatter), gbc);

		gbc.insets = spacer;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		pagarPanel.add(JLabelFactory.getLabel(FontType.bold, "Fecha del pago"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanel.add(datePago = new BetterDatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanel.add(btnInsertarPago = new JButton("Insertar pago"), gbc);

		JPanel registrarPanel = new JPanel();
		registrarPanel.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridy = 0;
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Curso al que pertenece"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		registrarPanel.add(cmbCurso = new JXComboBox(), gbc);
		cmbCurso.addItem("Seleccione un curso");

		gbc.insets = spacer;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Docente"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		registrarPanel.add(cmbProfesor = new JXComboBox(), gbc);
		cmbProfesor.addItem("Seleccione un profesor");
		cmbProfesor.setEnabled(false);

		gbc.insets = spacer;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Fecha de la factura"), gbc);

		gbc.insets = next;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		registrarPanel.add(dateFactura = new BetterDatePicker(), gbc);
		dateFactura.setDate(getMain().getToday());

		gbc.insets = spacer;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(btnInsertarFactura = new JButton("Insertar factura"), gbc);

		pagarTitledPanel.setContentContainer(pagarPanel);
		registrarTitledPanel.setContentContainer(registrarPanel);

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		this.add(formPanel, BorderLayout.EAST);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

}
