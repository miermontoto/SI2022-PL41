package g41.si2022.coiipa.gestionar_curso;

import java.awt.BorderLayout;
import java.awt.Dimension;
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

import org.jdesktop.swingx.JXTitledPanel;

import com.github.lgooddatepicker.components.DatePicker;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;
import g41.si2022.util.Util;
import lombok.Getter;

@Getter
public class GestionarCursoView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones;
	private JScrollPane scrollPane;
	private JLabel lblInfoNombre;
	private JLabel lblFechaCurso;
	private JLabel lblFechaInscripcion;
	private JLabel lblDevolverCalculo;
	private JButton btnCambiarFechas;
	private JButton btnCambiarDetalles;

	private BetterDatePicker dateNewIniCurso;
	private BetterDatePicker dateNewIniInscr;
	private BetterDatePicker dateNewFinCurso;
	private BetterDatePicker dateNewFinInscr;

	private JFormattedTextField txtFieldPlazas;

	// Datepicker para cambiar la fecha del curso y de las inscripciones
	private DatePicker datePickerNewDateCurso;
	private DatePicker datePickerNewDateInscripciones;

	private JFormattedTextField txtImporte;

	private JPanel panel;
	private JCheckBox chkAll;
	private JButton btnCancelarCurso;

	public GestionarCursoView(SwingMain main) {
		super(main, GestionarCursoModel.class, GestionarCursoView.class, GestionarCursoController.class);
	}

	@Override
	protected void initView () {
		this.setLayout(new BorderLayout(0, 0));

		JPanel handlePanel = new JPanel();
		handlePanel.setLayout(new GridLayout(3, 1));

		JXTitledPanel retrasarPanel = new JXTitledPanel("Cambiar fechas");
		JXTitledPanel cancelarPanel = new JXTitledPanel("Cancelar curso");
		JXTitledPanel devolverPanel = new JXTitledPanel("Gestionar curso");
		JXTitledPanel infoPanel = new JXTitledPanel("Información del curso");

		JPanel retrasarPanelContent = new JPanel();
		JPanel cancelarPanelContent = new JPanel();
		JPanel devolverPanelContent = new JPanel();
		JPanel infoPanelContent = new JPanel();

		retrasarPanel.setContentContainer(retrasarPanelContent);
		cancelarPanel.setContentContainer(cancelarPanelContent);
		devolverPanel.setContentContainer(devolverPanelContent);
		infoPanel.setContentContainer(infoPanelContent);

		panel = new JPanel();
		this.add(panel, BorderLayout.NORTH);
		panel.setLayout(new BorderLayout(0, 0));

		handlePanel.add(infoPanel);
		handlePanel.add(retrasarPanel);
		handlePanel.add(cancelarPanel);

		this.add(handlePanel, BorderLayout.EAST);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(5, 5, 5, 5);
		Insets next = new Insets(0, 5, 5, 5);

		infoPanelContent.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.CENTER;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Curso seleccionado"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		infoPanelContent.add(lblInfoNombre = JLabelFactory.getLabel("N/A"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 3;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Fechas del curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		infoPanelContent.add(lblFechaInscripcion = JLabelFactory.getLabel("N/A"), gbc);

		gbc.gridy = 5;
		infoPanelContent.add(lblFechaCurso = JLabelFactory.getLabel("N/A"), gbc);


		// Parte del panel de cambio de fechas
		retrasarPanelContent.setLayout(new GridBagLayout());

		gbc.insets = spacer;
		gbc.gridy = 0;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nuevas fechas de inscripción"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		retrasarPanelContent.add(dateNewIniInscr = new BetterDatePicker(), gbc);

		gbc.insets = next;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		retrasarPanelContent.add(dateNewFinInscr = new BetterDatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.weightx = 0.0;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nuevas fechas de curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		retrasarPanelContent.add(dateNewIniCurso = new BetterDatePicker(), gbc);

		gbc.gridx = 1;
		gbc.weightx = 0.0;
		retrasarPanelContent.add(dateNewFinCurso = new BetterDatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.weightx = 1.0;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		retrasarPanelContent.add(btnCambiarFechas = new JButton("Cambiar fechas"), gbc);

		cancelarPanelContent.setLayout(new GridBagLayout());

		gbc.insets = spacer;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		devolverPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Cambiar número de plazas"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		devolverPanelContent.add(txtFieldPlazas = new JFormattedTextField(Util.getMoneyFormatter()), gbc);

		gbc.gridy = 2;
		devolverPanelContent.add(btnCambiarDetalles = new JButton("Cambiar detalles"), gbc);

		gbc.gridy = 9;
		gbc.gridheight = 2;
		btnCancelarCurso = new JButton("Cancelar curso");
		btnCancelarCurso.setPreferredSize(new Dimension(200, 100));
		cancelarPanelContent.add(btnCancelarCurso, gbc);

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}
}
