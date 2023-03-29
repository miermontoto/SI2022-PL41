package g41.si2022.coiipa.gestionar_curso;

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
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

import javax.swing.JCheckBox;

@Getter
public class GestionarCursoView extends View {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	private JScrollPane scrollPane; // Panel de scroll de la tabla
	private JLabel lblInfoNombre;
	//private JLabel lblInfoDias;
	private JLabel lblFechaCurso;
	private JLabel lblDevolverCalculo;
	private JButton btnCambiarFechas;
	private JButton btnCambiarDetalles;

	//Datepicker para cambiar la fecha del curso y de las inscripciones
	private DatePicker datePickerNewDateCurso;
	private DatePicker datePickerNewDateInscripciones;
	private DatePicker datePickerNewDateFinCurso;
	private DatePicker datePickerNewDateFinInscripciones;

	
	private JFormattedTextField txtFieldPlazas;
	private JPanel panel;
	private JCheckBox chkAll;

	public GestionarCursoView(SwingMain main) {
		super(main, GestionarCursoModel.class, GestionarCursoView.class, GestionarCursoController.class);
	}

	@Override
	protected void initView () {
		this.setLayout(new BorderLayout(0, 0));

		JPanel handlePanel = new JPanel();
		handlePanel.setLayout(new GridLayout(3, 1));

		JXTitledPanel retrasarPanel = new JXTitledPanel("Cambiar fechas");
		JXTitledPanel cambiarDetallesPanel = new JXTitledPanel("Cambiar otros detalles");
		JXTitledPanel infoPanel = new JXTitledPanel("Información del curso");

		JPanel retrasarPanelContent = new JPanel();
		JPanel devolverPanelContent = new JPanel();
		JPanel infoPanelContent = new JPanel();

		retrasarPanel.setContentContainer(retrasarPanelContent);
		cambiarDetallesPanel.setContentContainer(devolverPanelContent);
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
		handlePanel.add(retrasarPanel);
		handlePanel.add(cambiarDetallesPanel);
		this.add(handlePanel, BorderLayout.EAST);

		GridBagConstraints gbc = new GridBagConstraints();
		Insets spacer = new Insets(5, 5, 5, 5);
		Insets next = new Insets(0, 5, 5, 5);

		infoPanelContent.setLayout(new GridBagLayout());
		gbc.insets = spacer;
		gbc.gridy = 0;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Curso seleccionado:"), gbc);

		gbc.insets = next;
		gbc.gridy = 1;
		infoPanelContent.add(lblInfoNombre = JLabelFactory.getLabel("N/A"), gbc);

		gbc.insets = spacer;
		gbc.gridy = 2;
		/*infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Días desde inscripción | hasta curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 3;
		infoPanelContent.add(lblInfoDias = JLabelFactory.getLabel("N/A"), gbc);*/

		gbc.insets = spacer;
		gbc.gridy = 3;
		infoPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Fecha de inicio del curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		infoPanelContent.add(lblFechaCurso = JLabelFactory.getLabel("N/A"), gbc);

		
		//Parte del panel de cambio de fechas
		
		retrasarPanelContent.setLayout(new GridBagLayout());

		gbc.insets = spacer;
		gbc.gridy = 3;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nueva fecha de inicio para el curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		retrasarPanelContent.add(datePickerNewDateCurso = new DatePicker(), gbc);
		
		gbc.insets = spacer;
		gbc.gridy = 3;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nueva fecha de fin del curso"), gbc);

		gbc.insets = next;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		retrasarPanelContent.add(datePickerNewDateFinCurso = new DatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nueva fecha para el inicio de inscripciones"), gbc);

		gbc.insets = next;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		retrasarPanelContent.add(datePickerNewDateInscripciones = new DatePicker(), gbc);
		
		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0.0;
		retrasarPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Nueva fecha para el fin de inscripciones"), gbc);

		gbc.insets = next;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;
		gbc.anchor = GridBagConstraints.CENTER;
		retrasarPanelContent.add(datePickerNewDateFinInscripciones = new DatePicker(), gbc);

		gbc.insets = spacer;
		gbc.gridy = 25;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		retrasarPanelContent.add(btnCambiarFechas = new JButton("Cambiar fechas"), gbc);

		devolverPanelContent.setLayout(new GridBagLayout());

		gbc.insets = spacer;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		devolverPanelContent.add(JLabelFactory.getLabel(FontType.bold, "Cambiar número de plazas"), gbc);
		
		gbc.insets = spacer;
		gbc.gridy = 5;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.fill = GridBagConstraints.CENTER;
		gbc.weightx = 1.0;
		devolverPanelContent.add(txtFieldPlazas = new JFormattedTextField(formatter), gbc);
	   

		gbc.gridy = 8;
		devolverPanelContent.add(btnCambiarDetalles = new JButton("Cambiar detalles"), gbc);

		tableInscripciones = new JTable();
		this.add(new JScrollPane(tableInscripciones), BorderLayout.CENTER);
		tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableInscripciones.setDefaultEditor(Object.class, null);
	}

}
