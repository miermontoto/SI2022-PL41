package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;
import lombok.Getter;
import lombok.Data;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JRadioButton;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import g41.si2022.util.Tab;

@Data @Getter
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

	public InscribirUsuarioView(g41.si2022.util.SwingMain main) {
		super(main);
		initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout());
		this.add(new JLabel("Inscripción de alumnado"), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new java.awt.BorderLayout());
		mainPanel.add(this.makeSigninPanel(), BorderLayout.EAST);
		mainPanel.add(this.makeLoginPanel(), BorderLayout.WEST);
		mainPanel.add(new JSeparator(JSeparator.VERTICAL), BorderLayout.CENTER);
		this.add(mainPanel, BorderLayout.CENTER);
		this.add(makeBottomPanel(), BorderLayout.SOUTH);
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
        { // login radiobutton
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            loginPanel.add(new JRadioButton("Ya tengo cuenta"), gbc);
        } { // signup radiobutton
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 1;
            gbc.gridwidth = 2;
            loginPanel.add(new JRadioButton("No tengo cuenta"), gbc);
        } { // email
			gbc.fill = GridBagConstraints.HORIZONTAL;
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weighty = 0.4;
			loginPanel.add(new JLabel("email:"), gbc);
			gbc.gridx = 1;
			loginPanel.add(this.txtEmailLogin, gbc);
		}

		return loginPanel;
	}

	private JPanel makeBottomPanel () {
		JPanel bottomPane = new JPanel(new BorderLayout());

		this.tablaCursos = new JTable();
		JScrollPane sp = new JScrollPane(this.tablaCursos);

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 200
				));

		bottomPane.add(sp, BorderLayout.CENTER);
		bottomPane.add(new JLabel("Cursos disponibles"), BorderLayout.NORTH);
		bottomPane.add(this.btnInscribir = new JButton("Inscribirse"), BorderLayout.SOUTH);

		return bottomPane;
	}

	@Override
	public void initController() { new InscribirUsuarioController(new InscribirUsuarioModel(), this); }
}
