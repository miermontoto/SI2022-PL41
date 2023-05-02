package g41.si2022.coiipa.gestionar_facturas_empresas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTitledPanel;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;
import g41.si2022.util.Util;
import lombok.Getter;

@Getter
public class GestionarFacturasEmprView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableFacturasEmpr; // Contenedor de la tabla de faacturas a empresas
	private JButton btnInsertarPago;
	private JButton btnInsertarFactura;

	private BetterDatePicker datePago;
	private BetterDatePicker dateFactura;
	private JXComboBox cmbCurso;
	private JFormattedTextField txtImporte;
	private JFormattedTextField txtImporteFactura;
	private JCheckBox chkAll;
	private JLabel lblEmpresa;

	public GestionarFacturasEmprView(SwingMain main) {
		super(main, GestionarFacturasEmprModel.class, GestionarFacturasEmprView.class, GestionarFacturasEmprController.class);
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

		JXTitledPanel pagarTitledPanel = new JXTitledPanel("Pagar factura seleccionada");
		JXTitledPanel registrarTitledPanel = new JXTitledPanel("Registrar nueva factura");
		formPanel.add(pagarTitledPanel);
		formPanel.add(registrarTitledPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(5, 10, 5, 10);
		Insets next = new Insets(0, 10, 0, 10);

		JPanel pagarPanel = new JPanel();
		pagarPanel.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridx = 0;
		gbc.gridy = 0;
		pagarPanel.add(JLabelFactory.getLabel(FontType.bold, "Importe a pagar (€)"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		pagarPanel.add(txtImporte = new JFormattedTextField(Util.getMoneyFormatter()), gbc);

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
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Empresa a la que pertenece"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.NONE;
		registrarPanel.add(lblEmpresa = new JLabel("N/A"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Fecha"), gbc);

		gbc.insets = next;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		registrarPanel.add(dateFactura = new BetterDatePicker(), gbc);
		dateFactura.setDate(getMain().getToday());

		gbc.insets = spacer;
		gbc.gridy = 7;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(JLabelFactory.getLabel(FontType.bold, "Importe (€)"), gbc);

		gbc.insets = next;
		gbc.gridy = 8;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		registrarPanel.add(txtImporteFactura = new JFormattedTextField(Util.getMoneyFormatter()), gbc);

		gbc.insets = spacer;
		gbc.gridy = 9;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		registrarPanel.add(btnInsertarFactura = new JButton("Insertar factura"), gbc);

		pagarTitledPanel.setContentContainer(pagarPanel);
		registrarTitledPanel.setContentContainer(registrarPanel);

		// View Facturas (left big panel)
		JPanel panelFacturas = new JPanel(new GridLayout(1, 1));
		tableFacturasEmpr = new JTable();
		JPanel facturasEmpr = new JPanel(new BorderLayout());
		facturasEmpr.add(JLabelFactory.getLabel(FontType.subtitle, "Facturas a empresas:"), BorderLayout.NORTH);
		facturasEmpr.add(new JScrollPane(tableFacturasEmpr), BorderLayout.CENTER);
		panelFacturas.add(facturasEmpr);
		this.add(panelFacturas, BorderLayout.CENTER);
		this.add(formPanel, BorderLayout.EAST);

		tableFacturasEmpr.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableFacturasEmpr.setDefaultEditor(Object.class, null);
	}
}
