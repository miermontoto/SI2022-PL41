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
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXTitledPanel;

import javax.swing.JRadioButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import g41.si2022.dto.ColectivoDTO;
import g41.si2022.mvc.View;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;

@Getter
public class InscribirUsuarioView extends View {

	private static final long serialVersionUID = 1L;
	private static final int TEXT_FIELD_SIZE = 15;
	private static final int PANEL_BORDER = 10;

	private static final String SIGNIN_TITLE = "Iniciar sesión";
	private static final String SIGNUP_TITLE = "Registro de usuario";

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

	private JXTitledPanel containerPanel;

	private JLabel lblSignin;
	private JLabel lblSignup;
	private JLabel lblStatus;

	private JComboBox<ColectivoDTO> cbColectivo;

	public InscribirUsuarioView(g41.si2022.ui.SwingMain main) {
		super(main, InscribirUsuarioModel.class, InscribirUsuarioView.class, InscribirUsuarioController.class);
	}

    private void toggle() {
		boolean isSignin = radioSignin.isSelected();
		containerPanel.setTitle(isSignin ? SIGNIN_TITLE : SIGNUP_TITLE);
		containerPanel.setContentContainer(isSignin ? signinPanel() : signupPanel());
    }

    @Override
	protected void initView() {
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		btnInscribir = new JButton("Inscribirse");

		decideUserSelection = new ButtonGroup();
		radioSignin = new JRadioButton(SIGNIN_TITLE);
		radioSignup = new JRadioButton(SIGNUP_TITLE);
		decideUserSelection.add(radioSignin);
		decideUserSelection.add(radioSignup);

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(radioPanel(), BorderLayout.NORTH);

		JPanel mainPanel = new JPanel(new GridLayout(1, 0));
		mainPanel.add(containerPanel = new JXTitledPanel(SIGNUP_TITLE));
		containerPanel.setContentContainer(signupPanel());
		signinPanel();

		panel.add(mainPanel, BorderLayout.CENTER);
		this.add(panel);
		this.add(cbColectivo = new JComboBox<>());
		this.add(makeBottomPanel());

		radioSignup.setSelected(true);
		btnInscribir.setEnabled(false);
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
		txtNombre = new JTextField("", InscribirUsuarioView.TEXT_FIELD_SIZE);
		txtApellidos = new JTextField();
		txtEmail = new JTextField();
		txtTelefono = new JTextField();

		lblSignup = JLabelFactory.getLabel("");

		JPanel signupPanel = new JPanel();
		output.add(signupPanel, BorderLayout.CENTER);
		signupPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(
			InscribirUsuarioView.PANEL_BORDER, 10, 10, InscribirUsuarioView.PANEL_BORDER
		));
		signupPanel.setLayout(new GridLayout(0, 2)); // any rows, 2 columns

		{ // Nombre
			signupPanel.add(JLabelFactory.getLabel("Nombre:"));
			signupPanel.add(txtNombre);
		} { // Apellidos
			signupPanel.add(JLabelFactory.getLabel("Apellidos:"));
			signupPanel.add(txtApellidos);
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
		txtEmailLogin = new JTextField("", InscribirUsuarioView.TEXT_FIELD_SIZE);
		lblSignin = JLabelFactory.getLabel("");

		JPanel output = new JPanel(new BorderLayout());
		output.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 0));

		JPanel signinPanel = new JPanel(new GridBagLayout());
		output.add(signinPanel, BorderLayout.CENTER);
		GridBagConstraints gbc = new GridBagConstraints();
		signinPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(InscribirUsuarioView.PANEL_BORDER, 10, 10, InscribirUsuarioView.PANEL_BORDER));
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

	/**
	 * makeBottomPanel.
	 * This method will create the panel that contains the JTable with the cursos.
	 *
	 * @return Panel with a JTable with the cursos.
	 */
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
