package g41.si2022.coiipa.registrar_curso;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.ScrollPaneConstants;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;

import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

import lombok.Getter;

@Getter
public class RegistrarCursoView extends g41.si2022.mvc.View {

	private static final long serialVersionUID = 1L;
	private JTextField txtNombre;
	private JTextArea txtDescripcion;
	private JIntegerTextField txtPlazas;
	private BetterDatePicker dateInscrStart, dateInscrEnd;
	private BetterDatePicker dateCursoStart, dateCursoEnd;
	private JTable tablaCostes;
	private JTable tableProfesores;
	private JTable tableEntidades;
	private JTable tableEventos;
	private JButton btnAddEvento;
	private JButton btnRemoveEvento;
	private JButton btnRegistrar;
	private JComponent[] focusableComponents;
	private JRadioButton rbtn1;
	private JRadioButton rbtn2;

	public void setNombreCurso(String nombreCurso) { this.txtNombre.setText(nombreCurso); }
	public void setObjetivosDescripcion(String objetivosDescripcion) { this.txtDescripcion.setText(objetivosDescripcion); }
	public void setPlazas(int plazas) { this.txtPlazas.setText(String.format("%d", Math.max(0, plazas))); }

	public RegistrarCursoView(g41.si2022.ui.SwingMain main) {
		super(main, RegistrarCursoModel.class, RegistrarCursoView.class, RegistrarCursoController.class);
	}

	@Override
	protected void initView() {
		this.setLayout(new BorderLayout(0, 0));

		JPanel centerPanel = new JPanel();
		centerPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setViewportView(centerPanel);
		this.add(scrollPane, BorderLayout.CENTER);
		centerPanel.setLayout(new GridBagLayout());
		GridBagConstraints left = new GridBagConstraints();
		GridBagConstraints right = new GridBagConstraints();

		// Título
		scrollPane.setColumnHeaderView(JLabelFactory.getLabel(FontType.subtitle, "Información sobre el curso"));

		{ // Nombre del curso
			{ // Label
				left.insets = new Insets(15, 0, 0, 0);
				left.fill = GridBagConstraints.BOTH;
				left.gridx = 0;
				left.gridy = 0;
				left.weighty = 1;
				centerPanel.add(JLabelFactory.getLabel("Nombre:"), left);
			} { // Input
				right.insets = new Insets(15, 15, 0, 0);
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
				centerPanel.add(JLabelFactory.getLabel("Objetivos y descripción:"), left);
			} { // Input
				right.gridy = 1;
				right.weighty = 2;
				txtDescripcion = new JTextArea();
				txtDescripcion.setLineWrap(true);
				txtDescripcion.setRows(5);
				centerPanel.add(this.txtDescripcion, right);
			}
		} { // Plazas
			{ // Label
				left.gridy = 2;
				left.weighty = 1;
				centerPanel.add(JLabelFactory.getLabel("Plazas:"), left);
			} { // Input
				right.gridy = 2;
				centerPanel.add(this.txtPlazas = new JIntegerTextField(), right);
			}
		} { // Coste
			{ // Label
				left.gridy = 3;
				centerPanel.add(JLabelFactory.getLabel("Coste de inscripción:"), left);
			} { // Input
				right.gridy = 3;
				right.fill = GridBagConstraints.BOTH;
				centerPanel.add(this.tablaCostes = new g41.si2022.ui.components.table.RowAppendableJTable(
						new String[] {"Nombre Colectivo", "Coste"},
						new java.util.TreeMap<Integer, java.util.regex.Pattern>() {
							private static final long serialVersionUID = 1L;
							{
								put(1, java.util.regex.Pattern.compile("\\d*"));
							}
						},
						new boolean[] {true, true}
					), right
				);
			}
		} { // Inscripcion
			{ // Label
				left.gridy = 4;
				centerPanel.add(JLabelFactory.getLabel("Inscripción:"), left);
			} { // Input
				JPanel panelInscripciones = new JPanel(new BorderLayout());
				right.gridy = 4;
				right.fill = GridBagConstraints.WEST;
				centerPanel.add(panelInscripciones, right);
				{ // Desde
					JPanel panelInscDesde = new JPanel(new BorderLayout());
					panelInscDesde.add(JLabelFactory.getLabel("Desde"), BorderLayout.WEST);
					panelInscDesde.add(new JPanel(), BorderLayout.CENTER);
					panelInscDesde.add(this.dateInscrStart = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} {
					panelInscripciones.add(new JPanel(), BorderLayout.CENTER);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(JLabelFactory.getLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(new JPanel(), BorderLayout.CENTER);
					panelInscHasta.add(this.dateInscrEnd = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscHasta, BorderLayout.EAST);
				}
			}
		} { // Curso
			{ // Label
				left.gridy = 5;
				centerPanel.add(JLabelFactory.getLabel("Curso:"), left);
			} { // Input
				JPanel panelInscripciones = new JPanel(new BorderLayout());
				right.gridy = 5;
				right.fill = GridBagConstraints.WEST;
				centerPanel.add(panelInscripciones, right);
				{ // Desde
					JPanel panelInscDesde = new JPanel(new BorderLayout());
					panelInscDesde.add(JLabelFactory.getLabel("Desde"), BorderLayout.WEST);
					panelInscDesde.add(new JPanel(), BorderLayout.CENTER);
					panelInscDesde.add(this.dateCursoStart = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscDesde, BorderLayout.WEST);
				} {
					panelInscripciones.add(new JPanel(), BorderLayout.CENTER);
				} { // Hasta
					JPanel panelInscHasta = new JPanel(new BorderLayout());
					panelInscHasta.add(JLabelFactory.getLabel("Hasta"), BorderLayout.WEST);
					panelInscHasta.add(new JPanel(), BorderLayout.CENTER);
					panelInscHasta.add(this.dateCursoEnd = new BetterDatePicker(), BorderLayout.EAST);
					panelInscripciones.add(panelInscHasta, BorderLayout.EAST);
				}
			}
		} { // Sesiones
			{ // Label
				left.gridy = 6;
				left.fill = GridBagConstraints.BOTH;
				left.weighty = 1;
				centerPanel.add(JLabelFactory.getLabel("Sesiones:"), left);
			}
			right.gridy = 6;
			right.fill = GridBagConstraints.BOTH;
			right.weighty = 1;
			JPanel sesionesPanel = new JPanel(new BorderLayout());
			centerPanel.add(sesionesPanel, right);
			{ // Tabla de sesiones
				this.tableSesiones = new JTable();
				this.tableSesiones.setName("Sesiones:");

				JScrollPane sp = new JScrollPane();
				sp.getVerticalScrollBar().setUnitIncrement(20);
				sp.setPreferredSize(new java.awt.Dimension(
						this.getWidth(), 150
						));
				sp.setViewportView(this.tableSesiones);
				sesionesPanel.add(sp, BorderLayout.CENTER);
			} { // Botones
				JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
				btnPanel.add(this.btnAddEvento = new JButton("Añadir evento"));
				btnPanel.add(this.btnRemoveEvento = new JButton("Eliminar evento"));
				eventosPanel.add(btnPanel, BorderLayout.SOUTH);
				btnAddEvento.setToolTipText("Necesario rango de fechas de curso");
			} { // Radio buttons
				left.gridy = 7;
				right.gridy = 7;
				right.gridx = 2;
				rbtn1 = new JRadioButton("Profesor/es");
				rbtn2 = new JRadioButton("Empresa");
				ButtonGroup btGroup = new ButtonGroup();
				btGroup.add(rbtn1);
				btGroup.add(rbtn2);
				centerPanel.add(rbtn1, left);
				centerPanel.add(rbtn2, right);
			}
		}

		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());
		
		JPanel leftBottomPane = new JPanel();
		leftBottomPane.setLayout(new BorderLayout());
		leftBottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Seleccionar profesor"), BorderLayout.NORTH);
		JScrollPane sp1 = new JScrollPane();
		sp1.getVerticalScrollBar().setUnitIncrement(20);
		sp1.setPreferredSize(new java.awt.Dimension(725, 150));

		JPanel rightBottomPane = new JPanel();
		rightBottomPane.setLayout(new BorderLayout());
		rightBottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Seleccionar empresa"), BorderLayout.NORTH);
		JScrollPane sp2 = new JScrollPane();
		sp2.getVerticalScrollBar().setUnitIncrement(20);
		sp2.setPreferredSize(new java.awt.Dimension(500, 150));

		this.tableProfesores = new JTable();
		this.tableProfesores.setName("Profesor:");
		this.tableProfesores.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		this.tableEntidades = new JTable();
		this.tableEntidades.setName("Entidad:");
		this.tableEntidades.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//  Add tableProfesores
		sp1.setViewportView(this.tableProfesores);
		leftBottomPane.add(sp1, BorderLayout.CENTER);

		// Add tableEntidades
		sp2.setViewportView(this.tableEntidades);
		rightBottomPane.add(sp2, BorderLayout.CENTER);

		bottomPane.add(leftBottomPane, BorderLayout.WEST);
		bottomPane.add(rightBottomPane, BorderLayout.EAST);

		this.btnRegistrar = new JButton();
		this.btnRegistrar.setText("Registrar curso");
		bottomPane.add(this.btnRegistrar, BorderLayout.SOUTH);

		this.add(bottomPane, BorderLayout.SOUTH);

		focusableComponents = new JComponent[] {
				this.txtNombre,
				this.txtPlazas,
				this.dateInscrStart,
				this.dateInscrEnd,
				this.dateCursoStart,
				this.dateCursoEnd,
				this.tablaCostes,
				this.tableEventos,
				this.tableProfesores,
				this.tableEntidades
		};
	}

}
