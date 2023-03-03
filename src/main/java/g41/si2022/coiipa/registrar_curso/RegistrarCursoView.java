package g41.si2022.coiipa.registrar_curso;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;

import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;

import g41.si2022.util.BetterDatePicker;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

public class RegistrarCursoView extends g41.si2022.util.Tab {

	private static final long serialVersionUID = 1L;
	private JTextField nombreCurso;
	private JTextArea objetivosDescripcion, localizacion;
	private JIntegerTextField plazas;
	private BetterDatePicker fechaInscripcionIni, fechaInscripcionFin;
	private BetterDatePicker fechaCursoIni, fechaCursoFin;
	private JTable profTable;
	private JButton registrarCurso;

	public String getNombreCurso () { return this.nombreCurso.getText().trim(); }
	public String getObjetivosDescripcion () { return this.objetivosDescripcion.getText().trim(); }
	public int getPlazas () { return Integer.parseInt(this.plazas.getText().trim()); }
	public String getInscripcionIniDate () { return this.fechaInscripcionIni.getDate().toString(); }
	public BetterDatePicker getInscripcionIni () { return this.fechaInscripcionIni; }
	public String getInscripcionFinDate () { return this.fechaInscripcionFin.getDate().toString(); }
	public BetterDatePicker getInscripcionFin () { return this.fechaInscripcionFin; }
	public String getCursoIniDate () { return this.fechaCursoIni.getDate().toString(); }
	public BetterDatePicker getCursoIni () { return this.fechaCursoIni; }
	public String getCursoFinDate () { return this.fechaCursoFin.getDate().toString(); }
	public BetterDatePicker getCursoFin () { return this.fechaCursoFin; }
	public JTable getTablaProfesores() { return this.profTable; }
	public JButton getSubmitButton() { return this.registrarCurso; }

	public void setNombreCurso (String nombreCurso) { this.nombreCurso.setText(nombreCurso); }
	public void setObjetivosDescripcion (String objetivosDescripcion) { this.objetivosDescripcion.setText(objetivosDescripcion); }
	public void setPlazas (int plazas) { this.plazas.setText(String.format("%d", Math.max(0, plazas))); }

	public RegistrarCursoView (g41.si2022.util.SwingMain main) {
		super(main);
		this.initView();
	}

	private void initView() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JScrollPane mainSp = new JScrollPane();
		mainSp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		mainSp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		mainSp.setViewportView(centerPanel);
		this.add(mainSp, BorderLayout.CENTER);
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints left = new GridBagConstraints();
		GridBagConstraints right = new GridBagConstraints();

		// Título
		mainSp.setColumnHeaderView(JLabelFactory.getLabel(FontType.subtitle, "Información sobre el curso"));

		{ // Nombre del curso
			{ // Label
				left.insets = new java.awt.Insets(15, 0, 0, 0);
				left.fill = GridBagConstraints.BOTH;
				left.gridx = 0;
				left.gridy = 0;
				left.weighty = 1;
				centerPanel.add(new JLabel("Nombre:"), left);
			} { // Input
				right.insets = new java.awt.Insets(15, 15, 0, 0);
				right.fill = GridBagConstraints.BOTH;
				right.gridx = 1;
				right.gridy = 0;
				right.weighty = 1;
				centerPanel.add(this.nombreCurso = new JTextField(), right);
			}
		} { // Objetivos y Descripcion
			{ // Label
				left.gridy = 1;
				left.weighty = 2;
				centerPanel.add(new JLabel("Objetivos y descripción:"), left);
			} { // Input
				right.gridy = 1;
				right.weighty = 2;
				this.objetivosDescripcion = new JTextArea();
				this.objetivosDescripcion.setLineWrap(true);
				this.objetivosDescripcion.setRows(5);
				centerPanel.add(this.objetivosDescripcion, right);
			}
		} { // Plazas
			{ // Label
				left.gridy = 2;
				left.weighty = 1;
				centerPanel.add(new JLabel("Plazas:"), left);
			} { // Input
				right.gridy = 2;
				centerPanel.add(this.plazas = new JIntegerTextField(), right);
			}
		} { // Inscripcion
			{ // Label
				left.gridy = 3;
				centerPanel.add(new JLabel("Inscripción:"), left);
			} { // Input
				JPanel panelInscripciones = new JPanel(new BorderLayout());
				right.gridy = 3;
				right.fill = GridBagConstraints.WEST;
				centerPanel.add(panelInscripciones, right);
				{ // Desde
					JPanel panelInscDesde = new JPanel(new BorderLayout());
					panelInscDesde.add(new JLabel("Desde"), BorderLayout.WEST);
					panelInscDesde.add(this.fechaInscripcionIni = new BetterDatePicker(), BorderLayout.CENTER);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(new JLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(this.fechaInscripcionFin = new BetterDatePicker(), BorderLayout.CENTER);
					panelInscripciones.add(panelInscHasta, BorderLayout.CENTER);
				}
			}
		} { // Curso
			{ // Label
				left.gridy = 4;
				centerPanel.add(new JLabel("Curso:"), left);
			} { // Input
				JPanel panelInscripciones = new JPanel(new BorderLayout());
				right.gridy = 4;
				centerPanel.add(panelInscripciones, right);
				{ // Desde
					JPanel panelInscDesde = new JPanel(new BorderLayout());
					panelInscDesde.add(new JLabel("Desde"), BorderLayout.WEST);
					panelInscDesde.add(this.fechaCursoIni = new BetterDatePicker(), BorderLayout.CENTER);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(new JLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(this.fechaCursoFin = new BetterDatePicker(), BorderLayout.CENTER);
					panelInscripciones.add(panelInscHasta, BorderLayout.CENTER);
				}
			}
		} { // Localizacion
			{ // Label
				left.gridy = 5;
				centerPanel.add(new JLabel("Localizacion:"), left);
			}
			{ // Input
				right.gridy = 5;
				right.fill = GridBagConstraints.BOTH;
				this.localizacion = new JTextArea();
				this.localizacion.setLineWrap(true);
				this.localizacion.setRows(2);
				centerPanel.add(this.localizacion, right);
			}
		}

		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());

		bottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Seleccionar profesor"), BorderLayout.NORTH);

		JScrollPane sp = new JScrollPane();

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 150
		));

		this.profTable = new JTable();
		this.profTable.setName("Profesor:");
		this.profTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// this.profTable.setDefaultEditor(Object.class, null); // No editable

		sp.setViewportView(this.profTable);
		bottomPane.add(sp, BorderLayout.CENTER);

		this.registrarCurso = new JButton();
		this.registrarCurso.setText("Registrar Curso");
		bottomPane.add(this.registrarCurso, BorderLayout.SOUTH);

		this.add(bottomPane, BorderLayout.SOUTH);
		add(new JLabel("Registrar Curso"), BorderLayout.NORTH);
	}

	@Override
	public void initController() {
		new RegistrarCursoController(new RegistrarCursoModel(), this);
	}

}
