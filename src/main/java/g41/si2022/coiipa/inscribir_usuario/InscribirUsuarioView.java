package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;
import lombok.Getter;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import g41.si2022.ui.Tab;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;

@Getter
public class InscribirUsuarioView extends Tab {

	private static final long serialVersionUID = 1L;
	private static final int textFieldSize = 15;
	private static final int panelBorder = 10;

	private JButton btnInscribir;
	private JTable tablaCursos;

	private JTextField txtNombre;
	private JTextField txtApellidos;
	private JTextField txtEmail;
	private JTextField txtTelefono;

	private JTextField txtEmailLogin;

    private ButtonGroup decideUserSelection;
    private JRadioButton radioSignin;
    private JRadioButton radioSignup;

	private JLabel lblSignin;
	private JLabel lblSignup;
	private JLabel lblStatus;

	public InscribirUsuarioView(g41.si2022.ui.SwingMain main) {
		super(main);
		initialize();
	}

    private void toggle() {
		JTextField[] fields = {txtEmail, txtNombre, txtApellidos, txtTelefono, txtEmailLogin};
        for(JTextField field : fields) field.setEnabled(!field.isEnabled());
		lblSignin.setText("");
		lblSignup.setText("");
    }

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(JLabelFactory.getLabel(FontType.title, "Inscripción de alumnado"), BorderLayout.NORTH);
		btnInscribir = new JButton("Inscribirse");

		decideUserSelection = new ButtonGroup();
		radioSignin = new JRadioButton("Iniciar sesión");
		radioSignup = new JRadioButton("Registrarse");
		decideUserSelection.add(radioSignin);
		decideUserSelection.add(radioSignup);
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 0));
		topPanel.add(JLabelFactory.getLabel(FontType.subtitle, "Información del usuario"));

		JPanel radioPanel = new JPanel();
		radioPanel.add(radioSignin);
		radioPanel.add(radioSignup);
		topPanel.add(radioPanel);

		radioSignin.addActionListener( e -> toggle() );
		radioSignup.addActionListener( e -> toggle() );

		JPanel mainPanel = new JPanel(new java.awt.BorderLayout());
		mainPanel.add(this.signupPanel(), BorderLayout.EAST);
		mainPanel.add(this.signinPanel(), BorderLayout.WEST);
		mainPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);
    	mainPanel.add(topPanel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(makeBottomPanel(), BorderLayout.SOUTH);

		radioSignup.setSelected(true);
		btnInscribir.setEnabled(false);
		txtEmailLogin.setEnabled(false);
	}

	private JPanel signupPanel () {
		txtNombre = new JTextField("", InscribirUsuarioView.textFieldSize);
		txtApellidos = new JTextField();
		txtEmail = new JTextField();
		txtTelefono = new JTextField();

		lblSignup = new JLabel("");

		JPanel signupPanel = new JPanel();
		signupPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			InscribirUsuarioView.panelBorder, 10, 10, InscribirUsuarioView.panelBorder
		));
		signupPanel.setLayout(new GridLayout(0, 2)); // any rows, 2 columns

		{ // Nombre
			signupPanel.add(new JLabel("Nombre:"));
			signupPanel.add(txtNombre);
		} { // Apellidos
			signupPanel.add(new JLabel("Apellidos:"));
			signupPanel.add(txtApellidos);
		} { // Email
			signupPanel.add(new JLabel("Email:"));
			signupPanel.add(txtEmail);
		} { // Teléfono
			signupPanel.add(new JLabel("Teléfono (opcional):"));
			signupPanel.add(txtTelefono);
		} { // Label (estado)
			signupPanel.add(new JLabel("")); // padding
			signupPanel.add(lblSignup);
		}

		return signupPanel;
	}

	private JPanel signinPanel () {
		txtEmailLogin = new JTextField("", InscribirUsuarioView.textFieldSize);
		lblSignin = new JLabel("");

		JPanel signinPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		signinPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(InscribirUsuarioView.panelBorder, 10, 10, InscribirUsuarioView.panelBorder));
        { // email
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.4;
			signinPanel.add(new JLabel("email:"), gbc);
			gbc.gridx = 1;
			signinPanel.add(txtEmailLogin, gbc);
			gbc.gridy = -1;
			signinPanel.add(lblSignin, gbc);
		}

		return signinPanel;
	}

	private JPanel makeBottomPanel () {
		JPanel bottomPane = new JPanel(new BorderLayout());

		tablaCursos = new JTable();
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(this.tablaCursos);

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 200
    	));

		JPanel inscribirPanel = new JPanel();
		lblStatus = new JLabel("");
		inscribirPanel.add(btnInscribir);
		inscribirPanel.add(lblStatus);

		bottomPane.add(sp, BorderLayout.CENTER);
		bottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Cursos disponibles"), BorderLayout.NORTH);
		bottomPane.add(inscribirPanel, BorderLayout.SOUTH);

		return bottomPane;
	}

	@Override
	public void initController() { new InscribirUsuarioController(new InscribirUsuarioModel(), this); }
}
