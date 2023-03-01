package g41.si2022.coiipa.registrar_pago;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.NumberFormatter;

import com.github.lgooddatepicker.components.DatePicker;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BoxLayout;
import net.miginfocom.swing.MigLayout;
import java.awt.CardLayout;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;

public class RegistrarPagoView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	JScrollPane scrollPane; // Panel de scroll de la tabla
	//private JButton botoncarga;
	private JPanel panel_1;
	private JLabel infonombreinscrito;
	private JLabel nombreinscripcion;
	private JLabel infoquehace;
	private JLabel lblNewLabel;
	private JButton botonpagar;

	//private final JTable tabCursos;


	/**
	 * Create the panel.
	 */
	
	DatePicker datepicker = new DatePicker();
	private JLabel lblNewLabel_1;
	private JFormattedTextField insertarimporte;
	
	public RegistrarPagoView(SwingMain main) {
		super(main);
		setLayout(new BorderLayout(0, 0));
		
		
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new MigLayout("", "[122.00px,grow][]", "[][14px][][][][][][][]"));
		
		infoquehace = new JLabel("Insertar un nuevo pago");
		infoquehace.setFont(new Font("Arial", Font.PLAIN, 14));
		infoquehace.setVerticalAlignment(SwingConstants.TOP);
		infoquehace.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(infoquehace, "cell 0 0,alignx left,aligny top");
		
		infonombreinscrito = new JLabel("Nombre del inscrito: ");
		infonombreinscrito.setVerticalAlignment(SwingConstants.TOP);
		panel_1.add(infonombreinscrito, "cell 0 1,alignx left,aligny center");
		
		nombreinscripcion = new JLabel("N/A");
		panel_1.add(nombreinscripcion, "cell 0 2,alignx left,aligny center");
		
		lblNewLabel = new JLabel("Introducir importe recibido (€): ");
		panel_1.add(lblNewLabel, "cell 0 3,alignx left,aligny center");
		
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(true);
	    // If you want the value to be committed on each keystroke instead of focus lost
	    formatter.setCommitsOnValidEdit(true);
	    
		insertarimporte = new JFormattedTextField(formatter);
		panel_1.add(insertarimporte, "cell 0 4,growx");
		
		
		lblNewLabel_1 = new JLabel("Introducir fecha:");
		panel_1.add(lblNewLabel_1, "cell 0 5");
		panel_1.add(datepicker, "cell 0 6,alignx right,aligny center");
		
				botonpagar = new JButton("Insertar pago");
				
				panel_1.add(botonpagar, "cell 0 8");

		//JPanel panel = new JPanel();
		//add(panel, BorderLayout.NORTH);

		//botoncarga = new JButton("Cargar datos ahora");
		//panel.add(botoncarga);
		tableInscripciones = new JTable();
		scrollPane = new JScrollPane(tableInscripciones); //Añado un panel de scroll
		add(scrollPane, BorderLayout.CENTER); //Lo añado a la vista

		// TODO: Tabla inicial para mostrar.

		//getContentPane().add(tablePanel, "cell 0 5,grow");

		//NO ES MVC, pero para testing, pruebo a cargar la tabla con datos
		String [] columnas = {"Nombre", "Fecha", "Coste", "Estado"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		String [] testData = {"Pepito", "2022-03-01", "50", "SIN PAGAR"};
		modelo.addRow(testData);
		tableInscripciones.setModel(modelo);
		tableInscripciones.setName("tabCursos");
		//tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//tableInscripciones.setDefaultEditor(Object.class, null); //Leer sólo.
		RegistrarPagoController controlador = new RegistrarPagoController(this, new RegistrarPagoModel()); // Inicializo el controlador finalmente
	}



	public JTable getTableInscripciones() {
		return tableInscripciones;
	}



	public void setTableInscripciones(JTable tableInscripciones) {
		this.tableInscripciones = tableInscripciones;
	}



	public JScrollPane getScrollPane() {
		return scrollPane;
	}



	public void setScrollPane(JScrollPane scrollPane) {
		this.scrollPane = scrollPane;
	}



	/*public JButton getBotoncarga() {
		return botoncarga;
	}



	public void setBotoncarga(JButton botoncarga) {
		this.botoncarga = botoncarga;
	}*/



	public JPanel getPanel_1() {
		return panel_1;
	}



	public void setPanel_1(JPanel panel_1) {
		this.panel_1 = panel_1;
	}



	public JLabel getInfonombreinscrito() {
		return infonombreinscrito;
	}



	public void setInfonombreinscrito(JLabel infonombreinscrito) {
		this.infonombreinscrito = infonombreinscrito;
	}



	public JLabel getNombreinscripcion() {
		return nombreinscripcion;
	}



	public void setNombreinscripcion(JLabel nombreinscripcion) {
		this.nombreinscripcion = nombreinscripcion;
	}



	public JLabel getInfoquehace() {
		return infoquehace;
	}



	public void setInfoquehace(JLabel infoquehace) {
		this.infoquehace = infoquehace;
	}



	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}



	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}



	public JButton getBotonpagar() {
		return botonpagar;
	}



	public void setBotonpagar(JButton botonpagar) {
		this.botonpagar = botonpagar;
	}



	public DatePicker getDatepicker() {
		return datepicker;
	}



	public void setDatepicker(DatePicker datepicker) {
		this.datepicker = datepicker;
	}



	public JLabel getLblNewLabel_1() {
		return lblNewLabel_1;
	}



	public void setLblNewLabel_1(JLabel lblNewLabel_1) {
		this.lblNewLabel_1 = lblNewLabel_1;
	}



	public JFormattedTextField getInsertarimporte() {
		return insertarimporte;
	}



	public void setInsertarimporte(JFormattedTextField insertarimporte) {
		this.insertarimporte = insertarimporte;
	}



	public static long getSerialversionuid() {
		return serialVersionUID;
	}



	@Override
	public void initController() { }
}
