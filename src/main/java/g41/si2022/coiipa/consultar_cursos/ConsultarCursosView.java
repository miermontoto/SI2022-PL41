package g41.si2022.coiipa.consultar_cursos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import lombok.Getter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import g41.si2022.util.SwingMain;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import g41.si2022.util.Tab;

@Getter
public class ConsultarCursosView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tablaCursos;
	private JTable tablaInscr;

	public ConsultarCursosView(SwingMain main) {
		super(main);
		initialize();
	}

	private void initialize() {
		this.setLayout(new BorderLayout(0, 0));

		// ------------- BorderLayout -> NORTH -------------
		// El botón sobra pero lo dejo porque mola
		// JButton btnCargarDatos = new JButton("Cargar cursos");
		// El botón no mola una mierda, te lo arranco de las manos. ~mier
		// ahora estoy triste. ~rubennmg
		// no pasa nada, el botón te observa desde los cielos y te quiere. ~mier
		// ya no es lo mismo, estoy enfadado. ~rubennmg

		JPanel panel1 = new JPanel();
		// Add elements
		//panel1.add(btnCargarDatos, BorderLayout.WEST);
		this.add(panel1, BorderLayout.NORTH);

		// ------------- BorderLayout -> CENTER -------------
		JPanel centerPanel = new JPanel();
		// Add elements
		centerPanel.setLayout(new GridLayout(3, 0));
		this.add(centerPanel, BorderLayout.CENTER);

		// lblCursos + tablaCursos
		JPanel panel2 = new JPanel();
		JLabel lblCursos = JLabelFactory.getLabel(FontType.subtitle, "Seleccionar curso:");
		tablaCursos = new JTable();
		JScrollPane scrCursos = new JScrollPane(tablaCursos);
		tablaCursos.setDefaultEditor(Object.class, null);
		// Add elements
		panel2.setLayout(new BorderLayout());
		panel2.add(lblCursos, BorderLayout.NORTH);
		panel2.add(scrCursos, BorderLayout.CENTER);
		centerPanel.add(panel2);

		// lblInscr + tablaInscr
		JPanel panel3 = new JPanel();
		JLabel lblInscr = new JLabel("Inscripciones:");
		tablaInscr = new JTable();
		JScrollPane scrInscr = new JScrollPane(tablaInscr);
		tablaInscr.setDefaultEditor(Object.class, null);
		// Add elements
		panel3.setLayout(new BorderLayout());
		panel3.add(lblInscr, BorderLayout.NORTH);
		panel3.add(scrInscr, BorderLayout.CENTER);
		centerPanel.add(panel3);

		// lblBalance + tablaBalance
		JPanel panel4 = new JPanel();
		panel4.setLayout(new BorderLayout());
		JLabel lblBalance = new JLabel("Balance económico:");
		JLabel lblGastos = new JLabel("Gastos");
		JTextField txtGastos = new JTextField();
		JLabel lblIngrEst = new JLabel("Ingresos estimados:");
		JTextField txtIngrEst = new JTextField();
		JLabel lblIngrReales = new JLabel("Ingresos reales:");
		JTextField txtIngrReales = new JTextField();
		JPanel panel5 = new JPanel();
		panel5.setLayout(new GridLayout());
		// Add elements
		panel5.add(lblGastos);
		panel5.add(txtGastos);
		panel5.add(lblIngrEst);
		panel5.add(txtIngrEst);
		panel5.add(lblIngrReales);
		panel5.add(txtIngrReales);
		panel4.add(lblBalance, BorderLayout.NORTH);
		panel4.add(panel5, BorderLayout.CENTER);
		centerPanel.add(panel4);
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
}
