package g41.si2022.coiipa.gestionar_inscripciones;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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

import org.jdesktop.swingx.JXTitledPanel;

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
	private JLabel lblInfoNombre;
	private JLabel lblInfoDias;
	private JLabel lblInfoDiferencia;
	private JLabel lblDevolverCalculo;
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
		handlePanel.setLayout(new GridLayout(3, 1));

		JXTitledPanel pagarPanel = new JXTitledPanel("Registrar pagos");
		JXTitledPanel devolverPanel = new JXTitledPanel("Registrar devolución");
		JXTitledPanel infoPanel = new JXTitledPanel("Información de la inscripción");

		JPanel pagarPanelContent = new JPanel();
		JPanel devolverPanelContent = new JPanel();
		JPanel infoPanelContent = new JPanel();

		pagarPanel.setContentContainer(pagarPanelContent);
		devolverPanel.setContentContainer(devolverPanelContent);
		infoPanel.setContentContainer(infoPanelContent);

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

		handlePanel.add(infoPanel);
		handlePanel.add(pagarPanel);
		handlePanel.add(devolverPanel);
		this.add(handlePanel, BorderLayout.EAST);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(5, 5, 5, 5);
		Insets next = new Insets(0, 5, 5, 5);

		infoPanelContent.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridy = 0;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nombre del inscrito"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		infoPanelContent.add(lblInfoNombre = JLabelFactory.getLabel("N/A"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 2;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Días desde inscripción | hasta curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		infoPanelContent.add(lblInfoDias = JLabelFactory.getLabel("N/A"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 4;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Diferencia"), gbc);

		gbc.insets = next;
		gbc.gridy = 5;
		infoPanelContent.add(lblInfoDiferencia = JLabelFactory.getLabel("N/A"), gbc);

		pagarPanelContent.setLayout(new GridBagLayout());

		gbc.insets = spacer;
		gbc.gridy = 3;
		pagarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Importe recibido (€)"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		pagarPanelContent.add(txtImporte = new JFormattedTextField(formatter), gbc);

		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		pagarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Fecha del pago"), gbc);

		gbc.insets = next;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanelContent.add(datePicker = new DatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 7;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		pagarPanelContent.add(btnInsertarPago = new JButton("Insertar pago"), gbc);

		devolverPanelContent.setLayout(new GridBagLayout());

		gbc.gridy = 3;
		devolverPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Importe a devolver (€)"), gbc);

		gbc.gridy = 4;
		devolverPanelContent.add(lblDevolverCalculo = JLabelFactory.getLabel("N/A"), gbc);

		gbc.gridy = 8;
		devolverPanelContent.add(btnCancelarInscripcion = new JButton("Cancelar inscripción"), gbc);

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

}
