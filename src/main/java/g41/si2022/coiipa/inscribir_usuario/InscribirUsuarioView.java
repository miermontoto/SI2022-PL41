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
import javax.swing.JSeparator;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

@Getter
public class InscribirUsuarioView extends Tab {

	private static final long serialVersionUID = 1L;
    private JButton btnInscribir;
    private JTable tablaCursos;

    private JTextField txtNombre;
    private JTextField txtApellidos;
    private JTextField txtEmail;
    private JTextField txtTelefono;

	private JTextField txtEmailLogin;

    public InscribirUsuarioView(SwingMain main) {
        super(main);
        initialize();
    }

    private void initialize() {
        this.setLayout(new BorderLayout(0, 0));

        txtNombre = new JTextField("", 25);
        txtApellidos = new JTextField();
        txtEmail = new JTextField();
        txtTelefono = new JTextField();

		txtEmailLogin = new JTextField();

        JPanel mainSp = new JPanel();
		mainSp.setLayout(new BorderLayout());

		JPanel signinPanel = new JPanel();
		JPanel loginPanel = new JPanel(new BorderLayout());
		loginPanel.add(this.txtEmailLogin, BorderLayout.CENTER);
		signinPanel.setLayout(new GridLayout(0, 2));
        mainSp.add(signinPanel, BorderLayout.WEST);
        mainSp.add(new JSeparator(), BorderLayout.CENTER);
        mainSp.add(loginPanel, BorderLayout.EAST);
        this.add(mainSp, BorderLayout.CENTER);

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

		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BorderLayout());

        tablaCursos = new JTable();
		JScrollPane sp = new JScrollPane(tablaCursos);

		sp.setPreferredSize(new java.awt.Dimension(
				this.getWidth(), 200
		));

		bottomPane.add(sp, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        btnInscribir = new JButton("Inscribirse");
        buttons.add(btnInscribir);
        bottomPane.add(new JLabel("Cursos disponibles"), BorderLayout.NORTH);
        bottomPane.add(buttons, BorderLayout.SOUTH);

		this.add(bottomPane, BorderLayout.SOUTH);
		this.add(new JLabel("Inscripción de alumnado"), BorderLayout.NORTH);
    }

    @Override
    public void initController() { new InscribirUsuarioController(new InscribirUsuarioModel(), this); }
}
