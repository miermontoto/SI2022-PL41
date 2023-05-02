package g41.si2022.coiipa.inscribir_multiples_usuarios_entidad;

import java.awt.BorderLayout;
import java.awt.Color;

import lombok.Getter;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.JRadioButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import g41.si2022.mvc.View;
import g41.si2022.ui.components.hint.HintingJTextField;
import g41.si2022.ui.components.table.RowAppendableComponentableJTable;
import g41.si2022.ui.components.table.RowAppendableJTable;
import g41.si2022.ui.components.table.editors.JComboBoxEditor;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

@Getter
public class InscribirMultiplesUsuariosEntidadView extends View {

	private static final long serialVersionUID = 1L;
	private static final int panelBorder = 10;

	private static final String signinTitle = "Iniciar sesión";
	private static final String signupTitle = "Registro de grupo";

	private JButton btnInscribir;
	private JTable tablaCursos;
	private RowAppendableJTable tablaInscritos;

	private HintingJTextField txtNombre;
	private HintingJTextField txtEmail;
	private HintingJTextField txtTelefono;
	private HintingJTextField txtEmailLogin;

	private ButtonGroup decideUserSelection;
	private JRadioButton radioSignin;
	private JRadioButton radioSignup;

	private JXTitledPanel containerPanel;

	private JLabel lblSignin;
	private JLabel lblSignup;
	private JLabel lblStatus;

	private java.util.LinkedList<g41.si2022.ui.components.table.editors.JComboBoxEditor<String>> comboBoxEditors;

	public InscribirMultiplesUsuariosEntidadView(g41.si2022.ui.SwingMain main) {
		super(main, InscribirMultiplesUsuariosEntidadModel.class, InscribirMultiplesUsuariosEntidadView.class, InscribirMultiplesUsuariosEntidadController.class);
	}

	private void toggle() {
		boolean isSignin = radioSignin.isSelected();
		containerPanel.setTitle(isSignin ? signinTitle : signupTitle);
		containerPanel.setContentContainer(isSignin ? signinPanel() : signupPanel());
	}

	@Override
	protected void initView() {
		this.setLayout(new BorderLayout());
		btnInscribir = new JButton("Inscribirse");

		decideUserSelection = new ButtonGroup();
		radioSignin = new JRadioButton(signinTitle);
		radioSignup = new JRadioButton(signupTitle);
		decideUserSelection.add(radioSignin);
		decideUserSelection.add(radioSignup);

		JScrollPane sp = new JScrollPane ();
		sp.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		JPanel inscrollPanel = makeLoginPanel();
		JPanel tablesPanel = new JPanel(new GridLayout(0, 1));
		this.comboBoxEditors = new LinkedList<JComboBoxEditor<String>> ();
		this.comboBoxEditors.add(new JComboBoxEditor<String> ());
		inscrollPanel.add(tablesPanel, BorderLayout.SOUTH);
		tablesPanel.add(this.tablaInscritos = new RowAppendableComponentableJTable (
				new String[]{"Nombre", "Apellidos", "Email", "Telefono", "Colectivo"},
				new java.util.TreeMap<Integer, java.util.regex.Pattern> () {
					private static final long serialVersionUID = 1L;
					{
						this.put(0, Pattern.compile(".*"));
						this.put(1, Pattern.compile(".*"));
						this.put(2, Pattern.compile("[^@ \\t\\r\\n]+@[^@ \\t\\r\\n]+\\.[^@ \\t\\r\\n]+"));
						this.put(3, Pattern.compile("^([0-9]{3}( )?){3}$"));
					}},
				new boolean[] {true, true, true, false, true},
				new java.util.TreeMap<Integer, javax.swing.table.TableCellEditor> () {
					private static final long serialVersionUID = 1L;
					{
						this.put(4, InscribirMultiplesUsuariosEntidadView.this.getComboBoxEditors().getFirst());
					}}
				));

		sp.setViewportView(inscrollPanel);

		this.add(sp, BorderLayout.CENTER);
		this.add(makeBottomPanel(), BorderLayout.SOUTH);

		radioSignup.setSelected(true);
		btnInscribir.setEnabled(false);
	}

	private JPanel makeLoginPanel () {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(radioPanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new GridLayout(1, 0));
		mainPanel.add(containerPanel = new JXTitledPanel(signupTitle));
		containerPanel.setContentContainer(signupPanel());
		signinPanel();

		panel.add(mainPanel, BorderLayout.CENTER);
		return panel;
	}

	private JPanel radioPanel() {
		JPanel radioPanel = new JPanel(new java.awt.FlowLayout());
		radioPanel.add(radioSignin);
		radioPanel.add(radioSignup);
		radioSignin.addActionListener( e -> toggle() );
		radioSignup.addActionListener( e -> toggle() );
		return radioPanel;
	}

	private JPanel signupPanel() {
		JPanel output = new JPanel (new BorderLayout());
		output.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		txtNombre = new HintingJTextField("Nombre");
		txtEmail = new HintingJTextField("Email");
		txtTelefono = new HintingJTextField("Telefono");

		lblSignup = JLabelFactory.getLabel("");

		JPanel signupPanel = new JPanel();
		output.add(signupPanel, BorderLayout.CENTER);
		signupPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
				InscribirMultiplesUsuariosEntidadView.panelBorder, 10, 10, InscribirMultiplesUsuariosEntidadView.panelBorder
				));
		signupPanel.setLayout(new GridLayout(0, 2)); // any rows, 2 columns

		{ // Nombre
			signupPanel.add(JLabelFactory.getLabel("Nombre:"));
			signupPanel.add(txtNombre);
		} { // Email
			signupPanel.add(JLabelFactory.getLabel("Email:"));
			signupPanel.add(txtEmail);
		} { // Teléfono
			signupPanel.add(JLabelFactory.getLabel("Teléfono (opcional):"));
			signupPanel.add(txtTelefono);
		} { // Label (estado)
			signupPanel.add(JLabelFactory.getLabel("")); // padding
			signupPanel.add(lblSignup);
		}

		return output;
	}

	private JPanel signinPanel() {
		txtEmailLogin = new HintingJTextField("Email");
		lblSignin = JLabelFactory.getLabel("");

		JPanel output = new JPanel(new BorderLayout());
		output.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));

		JPanel signinPanel = new JPanel(new GridBagLayout());
		output.add(signinPanel, BorderLayout.CENTER);
		GridBagConstraints gbc = new GridBagConstraints();
		signinPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(InscribirMultiplesUsuariosEntidadView.panelBorder, 10, 10, InscribirMultiplesUsuariosEntidadView.panelBorder));
		{ // email
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.4;
			signinPanel.add(JLabelFactory.getLabel("email:"), gbc);
			gbc.gridx = 1;
			signinPanel.add(txtEmailLogin, gbc);
			gbc.gridy = -1;
			signinPanel.add(lblSignin, gbc);
		}

		return output;
	}

	private JPanel makeBottomPanel() {
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
