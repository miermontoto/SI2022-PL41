package g41.si2022.coiipa.consultar_cursos;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import lombok.Getter;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import g41.si2022.util.SwingMain;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import g41.si2022.util.Tab;

@Getter
public class ConsultarCursosView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tablaCursos;
	private JTable tablaInscr;
	private JLabel lblEconomicInfo;

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
		// te lo mereces, has traicionado al puga gamer y al cabo peñas. ~mier

		JPanel panel1 = new JPanel();
		// Add elements
		//panel1.add(btnCargarDatos, BorderLayout.WEST);
		this.add(panel1, BorderLayout.NORTH);

		// ------------- BorderLayout -> CENTER -------------
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(3, 0));
		this.add(centerPanel, BorderLayout.CENTER);

		// lblCursos + tablaCursos
		JPanel panel2 = new JPanel();
		tablaCursos = new JTable();
		JScrollPane scrCursos = new JScrollPane(tablaCursos);
		tablaCursos.setDefaultEditor(Object.class, null);
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Add elements
		panel2.setLayout(new BorderLayout());
		panel2.add(JLabelFactory.getLabel(FontType.subtitle, "Seleccionar curso"), BorderLayout.NORTH);
		panel2.add(scrCursos, BorderLayout.CENTER);
		centerPanel.add(panel2);

		// lblInscr + tablaInscr
		JPanel panel3 = new JPanel();
		tablaInscr = new JTable();
		JScrollPane scrInscr = new JScrollPane(tablaInscr);
		tablaInscr.setDefaultEditor(Object.class, null);
		tablaInscr.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Add elements
		panel3.setLayout(new BorderLayout());
		panel3.add(JLabelFactory.getLabel(FontType.subtitle, "Inscripciones"), BorderLayout.NORTH);
		panel3.add(scrInscr, BorderLayout.CENTER);
		centerPanel.add(panel3);

		// lblBalance + tablaBalance
		JPanel panel4 = new JPanel();
		JPanel panel5 = new JPanel();
		panel4.setLayout(new BorderLayout());
		lblEconomicInfo = new JLabel();

		// Add elements
		panel5.add(lblEconomicInfo);
		panel4.add(JLabelFactory.getLabel(FontType.subtitle, "Balance económico"), BorderLayout.NORTH);
		panel4.add(panel5, BorderLayout.WEST);
		centerPanel.add(panel4);
	}

	@Override
	protected void initController() { new ConsultarCursosController(new ConsultarCursosModel(), this); }
}
