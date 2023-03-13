package g41.si2022.coiipa.registrar_curso;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;
import lombok.Getter;

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

@Getter
public class RegistrarCursoView extends g41.si2022.ui.Tab {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextArea txtDescripcion, txtLocalizacion;
	private JIntegerTextField txtPlazas;
	private BetterDatePicker dateInscrStart, dateInscrEnd;
	private BetterDatePicker dateCursoStart, dateCursoEnd;
	private JTextField txtCoste;
	private JTable tableProfesores;
	private JButton btnRegistrar;

	public void setNombreCurso (String nombreCurso) { this.txtNombre.setText(nombreCurso); }
	public void setObjetivosDescripcion (String objetivosDescripcion) { this.txtDescripcion.setText(objetivosDescripcion); }
	public void setPlazas (int plazas) { this.txtPlazas.setText(String.format("%d", Math.max(0, plazas))); }

	public RegistrarCursoView (g41.si2022.ui.SwingMain main) {
		super(main, RegistrarCursoModel.class, RegistrarCursoView.class, RegistrarCursoController.class);
	}

	@Override
	protected void initView() {
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
				centerPanel.add(this.txtNombre = new JTextField(), right);
			}
		} { // Objetivos y Descripcion
			{ // Label
				left.gridy = 1;
				left.weighty = 2;
				centerPanel.add(new JLabel("Objetivos y descripción:"), left);
			} { // Input
				right.gridy = 1;
				right.weighty = 2;
				this.txtDescripcion = new JTextArea();
				this.txtDescripcion.setLineWrap(true);
				this.txtDescripcion.setRows(5);
				centerPanel.add(this.txtDescripcion, right);
			}
		} { // Plazas
			{ // Label
				left.gridy = 2;
				left.weighty = 1;
				centerPanel.add(new JLabel("Plazas:"), left);
			} { // Input
				right.gridy = 2;
				centerPanel.add(this.txtPlazas = new JIntegerTextField(), right);
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
					panelInscDesde.add(new JPanel(), BorderLayout.CENTER);
					panelInscDesde.add(this.dateInscrStart = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} {
					panelInscripciones.add(new JPanel(), BorderLayout.CENTER);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(new JLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(new JPanel(), BorderLayout.CENTER);
					panelInscHasta.add(this.dateInscrEnd = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscHasta, BorderLayout.EAST);
				}
			}
		} { // Curso
			{ // Label
				left.gridy = 4;
				centerPanel.add(new JLabel("Curso:"), left);
			} { // Input
				JPanel panelInscripciones = new JPanel(new BorderLayout());
				right.gridy = 4;
				right.fill = GridBagConstraints.WEST;
				centerPanel.add(panelInscripciones, right);
				{ // Desde
					JPanel panelInscDesde = new JPanel(new BorderLayout());
					panelInscDesde.add(new JLabel("Desde"), BorderLayout.WEST);
					panelInscDesde.add(new JPanel(), BorderLayout.CENTER);
					panelInscDesde.add(this.dateCursoStart = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} {
					panelInscripciones.add(new JPanel(), BorderLayout.CENTER);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(new JLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(new JPanel(), BorderLayout.CENTER);
					panelInscHasta.add(this.dateCursoEnd = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscHasta, BorderLayout.EAST);
				}
			}
		} { // Localizacion
			{ // Label
				left.gridy = 5;
				centerPanel.add(new JLabel("Localizacion:"), left);
			} { // Input
				right.gridy = 5;
				right.fill = GridBagConstraints.BOTH;
				this.txtLocalizacion = new JTextArea();
				this.txtLocalizacion.setLineWrap(true);
				this.txtLocalizacion.setRows(2);
				centerPanel.add(this.txtLocalizacion, right);
			}
		} { // Coste
			{ // Label
				left.gridy = 6;
				centerPanel.add(new JLabel("Coste Inscripción:"), left);
			} { // Input
				right.gridy = 6;
				right.fill = GridBagConstraints.BOTH;
				centerPanel.add(this.txtCoste = new JTextField(), right);
			}
		}

		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());
		bottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Seleccionar profesor"), BorderLayout.NORTH);

		JScrollPane sp = new JScrollPane();
		sp.getVerticalScrollBar().setUnitIncrement(20);
		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 150
		));

		this.tableProfesores = new JTable();
		this.tableProfesores.setName("Profesor:");
		this.tableProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		// this.profTable.setDefaultEditor(Object.class, null); // No editable

		sp.setViewportView(this.tableProfesores);
		bottomPane.add(sp, BorderLayout.CENTER);

		this.btnRegistrar = new JButton();
		this.btnRegistrar.setText("Registrar Curso");
		bottomPane.add(this.btnRegistrar, BorderLayout.SOUTH);

		this.add(bottomPane, BorderLayout.SOUTH);
		add(JLabelFactory.getLabel(FontType.title, "Registrar Curso"), BorderLayout.NORTH);
	}

}
