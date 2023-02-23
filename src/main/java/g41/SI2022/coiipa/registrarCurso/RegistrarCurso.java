package g41.SI2022.coiipa.registrarCurso;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;
import com.github.lgooddatepicker.components.DatePicker;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RegistrarCurso extends g41.SI2022.util.Tab {

	private static final long serialVersionUID = 1L;
	private JTextField nombreCurso;
	private JTextArea objetivosDescripcion;
	private JIntegerTextField plazas;
	private DatePicker fechaInscripcionIni, fechaInscripcionFin;

	public RegistrarCurso (g41.SI2022.util.SwingMain main) {
		super(main);
		this.setLayout(new BorderLayout(0, 0));
		
		JPanel centerPanel = new JPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		centerPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		centerPanel.add(new JLabel("Nombre:"));
		centerPanel.add(this.nombreCurso = new JTextField());
		
		centerPanel.add(new JLabel("Objetivos y descripción:"));
		centerPanel.add(this.objetivosDescripcion = new JTextArea());
		
		centerPanel.add(new JLabel("Plazas:"));
		centerPanel.add(this.plazas = new JIntegerTextField());

		{
			centerPanel.add(new JLabel("Inscripción:"));

			JPanel panelInscripciones = new JPanel();
			centerPanel.add(panelInscripciones);
			panelInscripciones.add(new JLabel("Desde"));
			panelInscripciones.add(this.fechaInscripcionIni = new DatePicker());
			panelInscripciones.add(new JLabel("Hasta"));
			panelInscripciones.add(this.fechaInscripcionFin = new DatePicker());
		}
		
		add(new JLabel("Registrar Curso"), BorderLayout.NORTH);
		
		JPanel profPanel = new JPanel ();
		profPanel.setLayout(new BorderLayout());
		profPanel.add(new JLabel("Profesor:"), BorderLayout.NORTH);
		javax.swing.JTable profTable = new javax.swing.JTable();

		profPanel.add(profTable, BorderLayout.CENTER);
		this.add(profPanel, BorderLayout.SOUTH);
	}

}
