package g41.si2022.coiipa.registrar_pago;

import javax.swing.JScrollPane;

import java.awt.BorderLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
import java.awt.event.ActionEvent;

public class RegistrarPagoView extends Tab {

	private static final long serialVersionUID = 1L;
	private JTable tableInscripciones; // Contenedor de la tabla de inscripciones
	JScrollPane scrollPane; // Panel de scroll de la tabla
	private JButton botoncarga;
	private JPanel panel_1;
	private JLabel infoidinscripcion;
	private JLabel idinscripcion;
	private JLabel infoquehace;
	private JLabel lblNewLabel;
	private JTextField insertarimporte;
	private JButton botonpagar;

	//private final JTable tabCursos;


	/**
	 * Create the panel.
	 */
	public RegistrarPagoView(SwingMain main) {
		super(main);
		setLayout(new BorderLayout(0, 0));
		
		panel_1 = new JPanel();
		add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new MigLayout("", "[131.00px][42.00px,grow][]", "[][14px][][][][][][]"));
		
		infoquehace = new JLabel("Insertar un nuevo pago");
		infoquehace.setFont(new Font("Arial", Font.PLAIN, 14));
		infoquehace.setVerticalAlignment(SwingConstants.TOP);
		infoquehace.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(infoquehace, "cell 0 0,alignx left,aligny top");
		
		infoidinscripcion = new JLabel("Id de inscripción seleccionado: ");
		infoidinscripcion.setVerticalAlignment(SwingConstants.TOP);
		panel_1.add(infoidinscripcion, "cell 0 2,alignx left,aligny center");
		
		idinscripcion = new JLabel("N/A");
		panel_1.add(idinscripcion, "cell 1 2,alignx right,aligny center");
		
		lblNewLabel = new JLabel("Introducir importe recibido (€): ");
		panel_1.add(lblNewLabel, "cell 0 4,alignx left,aligny center");
		
		insertarimporte = new JTextField();
		panel_1.add(insertarimporte, "cell 1 4,growx");
		insertarimporte.setColumns(10);
		
		botonpagar = new JButton("Insertar pago");
		
		panel_1.add(botonpagar, "cell 0 7");

		JPanel panel = new JPanel();
		add(panel, BorderLayout.NORTH);

		botoncarga = new JButton("Cargar datos ahora");
		panel.add(botoncarga);
		tableInscripciones = new JTable();
		scrollPane = new JScrollPane(tableInscripciones); //Añado un panel de scroll
		add(scrollPane, BorderLayout.CENTER); //Lo añado a la vista

		// TODO: Tabla inicial para mostrar.

		//getContentPane().add(tablePanel, "cell 0 5,grow");

		//NO ES MVC, pero para testing, pruebo a cargar la tabla con datos
		String [] columnas = {"Inscripción", "Nombre", "Estado"};
		DefaultTableModel modelo = new DefaultTableModel(columnas, 0);
		String [] testData = {"123", "Pepito", "SIN PAGAR"};
		modelo.addRow(testData);
		tableInscripciones.setModel(modelo);
		tableInscripciones.setName("tabCursos");
		//tableInscripciones.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		//tableInscripciones.setDefaultEditor(Object.class, null); //Leer sólo.
		RegistrarPagoController controlador = new RegistrarPagoController(this, new RegistrarPagoModel()); // Inicializo el controlador finalmente
	}

	public JButton getBtnNewButton() {
		return botoncarga;
	}

	public void setBtnNewButton(JButton btnNewButton) {
		this.botoncarga = btnNewButton;
	}

	public JTable getTableInscripciones() {
		return tableInscripciones;
	}

	public JButton getBotoncarga() {
		return botoncarga;
	}

	public void setBotoncarga(JButton botoncarga) {
		this.botoncarga = botoncarga;
	}

	public JPanel getPanel_1() {
		return panel_1;
	}

	public JLabel getLblNewLabel() {
		return lblNewLabel;
	}

	public void setLblNewLabel(JLabel lblNewLabel) {
		this.lblNewLabel = lblNewLabel;
	}

	public JTextField getInsertarimporte() {
		return insertarimporte;
	}

	public void setInsertarimporte(JTextField insertarimporte) {
		this.insertarimporte = insertarimporte;
	}

	public JButton getBotonpagar() {
		return botonpagar;
	}

	public void setBotonpagar(JButton botonpagar) {
		this.botonpagar = botonpagar;
	}

	public void setIdinscripcion(JLabel idinscripcion) {
		this.idinscripcion = idinscripcion;
	}

	public void setPanel_1(JPanel panel_1) {
		this.panel_1 = panel_1;
	}

	public JLabel getInfoidinscripcion() {
		return infoidinscripcion;
	}

	public void setInfoidinscripcion(JLabel infoidinscripcion) {
		this.infoidinscripcion = infoidinscripcion;
	}

	public JLabel getIdinscripcion() {
		return idinscripcion;
	}

	public void setIdinscripcion(int i) {
		this.idinscripcion.setText(Integer.toString(i));
	}

	public JLabel getInfoquehace() {
		return infoquehace;
	}

	public void setInfoquehace(JLabel infoquehace) {
		this.infoquehace = infoquehace;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public void initController() { }
}
