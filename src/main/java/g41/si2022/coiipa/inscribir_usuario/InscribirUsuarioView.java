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

import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import g41.si2022.util.Tab;

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

	public InscribirUsuarioView(g41.si2022.util.SwingMain main) {
		super(main);
		initialize();
	}

    private void toggle() {
		JTextField[] fields = {txtEmail, txtNombre, txtApellidos, txtTelefono, txtEmailLogin};
        for(JTextField field : fields) field.setEnabled(!field.isEnabled());
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
		JPanel radioPanel = new JPanel();
		radioPanel.add(radioSignin);
		radioPanel.add(radioSignup);
		radioSignin.addActionListener( e -> toggle() );
		radioSignup.addActionListener( e -> toggle() );

		JPanel mainPanel = new JPanel(new java.awt.BorderLayout());
		mainPanel.add(this.makeSigninPanel(), BorderLayout.EAST);
		mainPanel.add(this.makeLoginPanel(), BorderLayout.WEST);
		mainPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);
    	mainPanel.add(radioPanel, BorderLayout.NORTH);
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(makeBottomPanel(), BorderLayout.SOUTH);

		radioSignup.setSelected(true);
		btnInscribir.setEnabled(false);
		txtEmailLogin.setEnabled(false);
	}

	private JPanel makeSigninPanel () {
		this.txtNombre = new JTextField("", InscribirUsuarioView.textFieldSize);
		this.txtApellidos = new JTextField();
		this.txtEmail = new JTextField();
		this.txtTelefono = new JTextField();

		JPanel signinPanel = new JPanel();
		signinPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			InscribirUsuarioView.panelBorder, 10, 10, InscribirUsuarioView.panelBorder
		));
		signinPanel.setLayout(new GridLayout(0, 2)); // any rows, 2 columns

		{ // Nombre
			signinPanel.add(new JLabel("Nombre:"));
			signinPanel.add(txtNombre);
		} { // Apellidos
			signinPanel.add(new JLabel("Apellidos:"));
			signinPanel.add(txtApellidos);
		} { // Email
			signinPanel.add(new JLabel("Email:"));
			signinPanel.add(txtEmail);
		} { // Teléfono
			signinPanel.add(new JLabel("Teléfono (opcional):"));
			signinPanel.add(txtTelefono);
		}

		return signinPanel;
	}

	private JPanel makeLoginPanel () {
		this.txtEmailLogin = new JTextField("", InscribirUsuarioView.textFieldSize);

		JPanel loginPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		loginPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(InscribirUsuarioView.panelBorder, 10, 10, InscribirUsuarioView.panelBorder));
        { // email
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.weighty = 0.4;
			loginPanel.add(new JLabel("email:"), gbc);
			gbc.gridx = 1;
			loginPanel.add(this.txtEmailLogin, gbc);
		}

		return loginPanel;
	}

	private JPanel makeBottomPanel () {
		JPanel bottomPane = new JPanel(new BorderLayout());

		tablaCursos = new JTable();
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sp = new JScrollPane(this.tablaCursos);

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 200
    	));

		bottomPane.add(sp, BorderLayout.CENTER);
		bottomPane.add(JLabelFactory.getLabel(FontType.subtitle, "Cursos disponibles"), BorderLayout.NORTH);
		bottomPane.add(btnInscribir, BorderLayout.SOUTH);

		return bottomPane;
	}

	@Override
	public void initController() { new InscribirUsuarioController(new InscribirUsuarioModel(), this); }
}
