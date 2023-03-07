package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;
import java.awt.Color;

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
import javax.swing.BorderFactory;
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
		boolean isSignin = radioSignin.isSelected();
		txtEmail.setEnabled(!isSignin);
		txtNombre.setEnabled(!isSignin);
		txtApellidos.setEnabled(!isSignin);
		txtTelefono.setEnabled(!isSignin);
		txtEmailLogin.setEnabled(isSignin);
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

		JPanel panel = new JPanel(new BorderLayout());
		this.add(panel, BorderLayout.CENTER);
		panel.add(radioPanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new GridLayout(1, 0));
		mainPanel.add(this.signupPanel());
		mainPanel.add(this.signinPanel());
		panel.add(mainPanel, BorderLayout.CENTER);
		this.add(makeBottomPanel(), BorderLayout.SOUTH);

		radioSignup.setSelected(true);
		btnInscribir.setEnabled(false);
		txtEmailLogin.setEnabled(false);
	}

	private JPanel radioPanel () {
		JPanel radioPanel = new JPanel(new java.awt.FlowLayout());
		radioPanel.add(radioSignup);
		radioPanel.add(radioSignin);
		radioSignin.addActionListener( e -> toggle() );
		radioSignup.addActionListener( e -> toggle() );
		return radioPanel;
	}

	private JPanel signupPanel () {
		JPanel output = new JPanel (new BorderLayout());
		output.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
		output.add(JLabelFactory.getLabel(FontType.subtitle, "Registro de usuario"), BorderLayout.NORTH);
		txtNombre = new JTextField("", InscribirUsuarioView.textFieldSize);
		txtApellidos = new JTextField();
		txtEmail = new JTextField();
		txtTelefono = new JTextField();

		lblSignup = new JLabel("");

		JPanel signupPanel = new JPanel();
		output.add(signupPanel, BorderLayout.CENTER);
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

		return output;
	}

	private JPanel signinPanel () {
		txtEmailLogin = new JTextField("", InscribirUsuarioView.textFieldSize);
		lblSignin = new JLabel("");

		JPanel output = new JPanel(new BorderLayout());
		output.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));
		output.add(JLabelFactory.getLabel(FontType.subtitle, "Inicio de sesión"), BorderLayout.NORTH);

		JPanel signinPanel = new JPanel(new GridBagLayout());
		output.add(signinPanel, BorderLayout.CENTER);
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

		return output;
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
