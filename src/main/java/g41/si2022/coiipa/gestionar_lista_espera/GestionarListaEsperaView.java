package g41.si2022.coiipa.gestionar_lista_espera;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.text.NumberFormatter;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
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
import javax.swing.JComboBox;

@Getter
public class GestionarListaEsperaView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JButton btnEliminarListaEspera;
	private JButton btnInsertarFactura;

	private BetterDatePicker datePago;
	private BetterDatePicker dateFactura;
	private JComboBox<String> cmbCurso;
	DefaultComboBoxModel<String> cmbCursoModel;
	private JFormattedTextField txtImporte;
	private JCheckBox chkAll;

	JLabel nombreApellidosLabel;
	JLabel fechaListaLabel;
	JLabel error;


	public GestionarListaEsperaView(SwingMain main) {
		super(main, GestionarListaEsperaModel.class, GestionarListaEsperaView.class, GestionarListaEsperaController.class);
	}

	@Override
	//@SuppressWarnings("unchecked") // odio los combobox
	protected void initView () {
		JPanel formPanel = new JPanel();
		this.setLayout(new BorderLayout(0, 0));

		formPanel.setLayout(new GridLayout(2, 1));
		formPanel.setPreferredSize(new java.awt.Dimension(SwingMain.DEFAULT_WINDOW_WIDTH/4, SwingMain.DEFAULT_WINDOW_HEIGHT));
		this.add(formPanel, BorderLayout.EAST);

		NumberFormatter formatter = new NumberFormatter(java.text.NumberFormat.getInstance());
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(Integer.MIN_VALUE);
		formatter.setMaximum(Integer.MAX_VALUE);
		formatter.setAllowsInvalid(true);
		formatter.setCommitsOnValidEdit(true);

		JXTitledPanel seleccionarCursoPanel = new JXTitledPanel("Seleccionar curso");
		JXTitledPanel datosInscripcionPanel = new JXTitledPanel("Datos del alumno seleccionado");
		formPanel.add(seleccionarCursoPanel);
		formPanel.add(datosInscripcionPanel);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(10, 10, 10, 10);
		Insets next = new Insets(0, 10, 0, 10);

		JPanel pagarPanel = new JPanel();
		pagarPanel.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.CENTER;
		pagarPanel.add(JLabelFactory.getLabel(FontType.bold, "Seleccionar curso: "), gbc);

		gbc.gridy = 2;
		pagarPanel.add(error = JLabelFactory.getLabel(FontType.bold, "Sin alumnos en lista de espera"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.CENTER;
		cmbCursoModel = new DefaultComboBoxModel<>();
		pagarPanel.add(this.cmbCurso = new JComboBox<String>(cmbCursoModel), gbc);

		seleccionarCursoPanel.setContentContainer(pagarPanel);

		JPanel datosPanel = new JPanel();
		datosPanel.setLayout(new GridBagLayout());

		gbc.gridy = 0;
		datosPanel.add(JLabelFactory.getLabel(FontType.bold, "Nombre del alumno: "), gbc);
		gbc.gridy = 1;
		datosPanel.add(nombreApellidosLabel = new JLabel("Seleccionar alumno"), gbc);

		gbc.insets = spacer;	
		gbc.gridy = 2;
		datosPanel.add(JLabelFactory.getLabel(FontType.bold, "Fecha de entrada en la lista de espera: "), gbc);

		gbc.gridy = 3;
		datosPanel.add(fechaListaLabel = new JLabel("Seleccionar alumno"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		datosPanel.add(btnEliminarListaEspera = new JButton("Eliminar de la lista de espera"), gbc);

		datosInscripcionPanel.setContentContainer(datosPanel);

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		this.add(formPanel, BorderLayout.EAST);

		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

}
