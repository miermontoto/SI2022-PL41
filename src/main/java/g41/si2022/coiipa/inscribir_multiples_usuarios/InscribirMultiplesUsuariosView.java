package g41.si2022.coiipa.inscribir_multiples_usuarios;

import java.awt.BorderLayout;

import lombok.Getter;

import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTitledPanel;

import g41.si2022.mvc.View;
import g41.si2022.ui.components.table.RowAppendableComponentableJTable;
import g41.si2022.ui.components.table.RowAppendableJTable;
import g41.si2022.ui.components.table.editors.JComboBoxEditor;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

@Getter
public class InscribirMultiplesUsuariosView extends View {

	private static final long serialVersionUID = 1L;

	private static final String signTitle = "Inscribir Alumnos";

	private JButton btnInscribir;
	private JTable tablaCursos;
	private RowAppendableJTable tablaInscritos;

	private JXTitledPanel containerPanel;
	private LinkedList<g41.si2022.ui.components.table.editors.JComboBoxEditor<String>> comboBoxEditors;

	private JLabel lblSignin;
	private JLabel lblSignup;
	private JLabel lblStatus;

	public InscribirMultiplesUsuariosView(g41.si2022.ui.SwingMain main) {
		super(main, InscribirMultiplesUsuariosModel.class, InscribirMultiplesUsuariosView.class, InscribirMultiplesUsuariosController.class);
	}

	@Override
	protected void initView() {
		this.setLayout(new BorderLayout());
		btnInscribir = new JButton("Inscribirse");
		JScrollPane sp = new JScrollPane ();
		sp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel inscrollPanel = new JXTitledPanel(signTitle);
		this.comboBoxEditors = new LinkedList<JComboBoxEditor<String>> ();
		this.comboBoxEditors.add(new JComboBoxEditor<String> ());
		inscrollPanel.add(
		this.tablaInscritos = new RowAppendableComponentableJTable (
				new String[]{"Nombre", "Apellidos", "Email", "Telefono", "Colectivo"},
				new java.util.TreeMap<Integer, java.util.regex.Pattern> () {
					private static final long serialVersionUID = 1L;
					{
						this.put(2, Pattern.compile("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+"));
						this.put(3, Pattern.compile("^(\\d{3}( )?){3}$"));
					}},
				new boolean[] {true, true, true, false, true},
				new java.util.TreeMap<Integer, javax.swing.table.TableCellEditor> () {
					private static final long serialVersionUID = 1L;
					{
						this.put(4, InscribirMultiplesUsuariosView.this.getComboBoxEditors().getFirst());
					}}
				), BorderLayout.CENTER);

		sp.setViewportView(inscrollPanel);

		this.add(sp, BorderLayout.CENTER);
		this.add(makeCursosPanel(), BorderLayout.SOUTH);

		btnInscribir.setEnabled(false);
	}

	private JPanel makeCursosPanel() {

		JPanel bottomPane = new JPanel(new BorderLayout());

		tablaCursos = new JTable();
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(this.tablaCursos);

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 200
				));

		JPanel inscribirPanel = new JPanel();
		lblStatus = JLabelFactory.getLabel("");
		inscribirPanel.add(btnInscribir);
		inscribirPanel.add(lblStatus);

		bottomPane.add(sp, BorderLayout.CENTER);
		bottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Cursos disponibles"), BorderLayout.NORTH);
		bottomPane.add(inscribirPanel, BorderLayout.SOUTH);

		return bottomPane;
	}

}
