package g41.SI2022.coiipa.registrarCurso;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;
import com.github.lgooddatepicker.components.DatePicker;

import java.awt.GridLayout;
import java.util.Date;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RegistrarCurso extends g41.SI2022.util.Tab {

	private static final long serialVersionUID = 1L;
	private JTextField nombreCurso;
	private JTextArea objetivosDescripcion;
	private JIntegerTextField plazas;
	private DatePicker fechaInscripcionIni, fechaInscripcionFin;
	
	public String getNombreCurso () { return this.nombreCurso.getText().trim(); }
	public String getObjetivosDescripcion () { return this.objetivosDescripcion.getText().trim(); }
	public int getPlazas () { return Integer.parseInt(this.plazas.getText().trim()); }
	public Date getInscripcionIni () { return new Date (this.fechaInscripcionIni.getDate().toEpochDay()); }
	public Date getInscripcionFin () { return new Date (this.fechaInscripcionFin.getDate().toEpochDay()); }

	public void setNombreCurso (String nombreCurso) { this.nombreCurso.setText(nombreCurso); }
	public void setObjetivosDescripcion (String objetivosDescripcion) { this.objetivosDescripcion.setText(objetivosDescripcion); }
	public void setPlazas (int plazas) { this.plazas.setText(String.format("%d", Math.max(0, plazas))); }

	public RegistrarCurso (g41.SI2022.util.SwingMain main) {
		super(main);
		this.initialize();
	}
	
	private void initialize () {
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
