package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;
import lombok.Getter;
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

        JPanel mainSp = new JPanel();
        mainSp.setLayout(new BorderLayout());

		JPanel signinPanel = new JPanel();
		signinPanel.setLayout(new GridBagLayout());
        mainSp.add(signinPanel, BorderLayout.WEST);
        mainSp.add(new JSeparator(), BorderLayout.CENTER);
		GridBagConstraints left = new GridBagConstraints();
		GridBagConstraints right = new GridBagConstraints();
        this.add(mainSp, BorderLayout.CENTER);

		{ // Nombre
			{ // Label
				left.fill = GridBagConstraints.BOTH;
				left.gridx = 0;
				left.gridy = 0;
				left.weighty = 0;
				signinPanel.add(new JLabel("Nombre:"), left);
			} { // Input
				right.fill = GridBagConstraints.BOTH;
				right.gridx = 1;
				right.gridy = 0;
				right.weighty = 1;
				signinPanel.add(txtNombre, right);
			}
		} { // Apellidos
			{ // Label
				left.gridy = 1;
				left.weighty = 2;
				signinPanel.add(new JLabel("Apellidos:"), left);
			} { // Input
				right.gridy = 1;
				right.weighty = 2;
                signinPanel.add(txtApellidos, right);
            }
		} { // Email
			{ // Label
				left.gridy = 2;
				left.weighty = 1;
				signinPanel.add(new JLabel("Email:"), left);
			} { // Input
				right.gridy = 2;
				signinPanel.add(txtEmail, right);
			}
		} { // Teléfono
			{ // Label
				left.gridy = 3;
				signinPanel.add(new JLabel("Teléfono (opcional):"), left);
			} { // Input
				right.gridy = 3;
				signinPanel.add(txtTelefono, right);
			}
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
