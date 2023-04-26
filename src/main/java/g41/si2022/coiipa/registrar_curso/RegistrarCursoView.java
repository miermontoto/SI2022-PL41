package g41.si2022.coiipa.registrar_curso;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

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
	private JTable tableEventos;
	private JButton btnAddEvento;
	private JButton btnRemoveEvento;
	private JButton btnRegistrar;
	private JComponent[] focusableComponents;

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
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
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
				//txtDescripcion.setBorder(BorderFactory.createLineBorder(Color.GRAY));
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
				centerPanel.add(this.tablaCostes = new g41.si2022.ui.components.table.RowAppendableJTable (
						new String[] {"Nombre Colectivo", "Coste"},
						new java.util.TreeMap<Integer, java.util.regex.Pattern> () {
							private static final long serialVersionUID = 1L;
							{
								put(1, java.util.regex.Pattern.compile("[0-9]*"));
							}},
						new boolean[] {true, true}
						), right);
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
		} { // Eventos
			{ // Label
				left.gridy = 6;
				left.fill = GridBagConstraints.BOTH;
				left.weighty = 1;
				centerPanel.add(JLabelFactory.getLabel("Eventos:"), left);
			}
			right.gridy = 6;
			right.fill = GridBagConstraints.BOTH;
			right.weighty = 1;
			JPanel eventosPanel = new JPanel(new BorderLayout());
			centerPanel.add(eventosPanel, right);
			{ // Tabla de eventos
				this.tableEventos = new JTable();
				this.tableEventos.setName("Eventos:");

				JScrollPane sp = new JScrollPane();
				sp.getVerticalScrollBar().setUnitIncrement(20);
				sp.setPreferredSize(new java.awt.Dimension(
						this.getWidth(), 150
						));
				sp.setViewportView(this.tableEventos);
				eventosPanel.add(sp, BorderLayout.CENTER);
			} { // Botones
				JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
				btnPanel.add(this.btnAddEvento = new JButton("Añadir evento"));
				btnPanel.add(this.btnRemoveEvento = new JButton("Eliminar evento"));
				eventosPanel.add(btnPanel, BorderLayout.SOUTH);
				btnAddEvento.setToolTipText("Necesario rango de fechas de curso");
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
				this.tableProfesores
		};
	}

}
