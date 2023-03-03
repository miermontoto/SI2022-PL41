package g41.si2022.coiipa.registrar_pago;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;
import g41.si2022.util.ApplicationException;

public class RegistrarPagoController {

	private RegistrarPagoView vista;
	private RegistrarPagoModel modelo;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		this.initView();
	}

	private int idInscripcion = -1;
	private int idAlumno = -1;

	public void initView() {
		getListaInscripciones(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado

		vista.getBtnInsertarPago().addActionListener(e -> handleInsertar());
		vista.getTableInscripciones().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		vista.getChkAll().addActionListener(e -> getListaInscripciones());
	}

	private void setControls(boolean status) {
		vista.getBtnInsertarPago().setEnabled(status);
		vista.getTxtImporte().setEnabled(status);
		vista.getDatePicker().setEnabled(status);
	}
	
	private void eraseControls(boolean eliminaraviso) {
		vista.getLblNombreInscripcion().setText("No se ha seleccionado ningún nombre");
		vista.getTxtImporte().setText("");
		vista.getDatePicker().setText("");
		if(eliminaraviso) vista.getLblError().setText("");
	}

	private void handleSelect() {
		eraseControls(true); //Borramos también el aviso de pago insertado con éxito/error
		int fila = vista.getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel tempModel = vista.getTableInscripciones().getModel();
		idInscripcion = (int) tempModel.getValueAt(fila, 0);
		idAlumno = (int) tempModel.getValueAt(fila, 1);
		vista.getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 2));

		setControls(true);
	}

	private void handleInsertar() {
		String nombreInscrito = vista.getLblNombreInscripcion().getText();
		String importeString = vista.getTxtImporte().getText();
		System.out.println(importeString);

		if(nombreInscrito == "No se ha seleccionado ningún nombre" || importeString == null || vista.getDatePicker().getDate() == null) {
			
			vista.getLblError().setText("Por favor, rellena todos los campos para continuar"); //Mostramos un error
			return;
			
		} else {
			
			// Hacemos las conversiones a Date y int
			Date fechaPago = java.sql.Date.valueOf(vista.getDatePicker().getDate());
			int importe = Integer.parseInt(importeString);

			System.out.printf("Se han pagado %s € para el alumno %s, en la inscripción: %d, con fecha %s\n", importe, nombreInscrito, idInscripcion, fechaPago.toString()); //DEBUG
			modelo.registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion); // Registro en la BBDD el pago
			enviarEmail(idAlumno, vista.getLblNombreInscripcion().getText()); // Envío un email al alumno
			getListaInscripciones(); // Refrescamos la tabla al terminar de inscribir a la persona
			//Si había algún error habilitado en la etiqueta, lo deshabilitamos y mostramos éxito
			vista.getLblError().setText("Pago insertado con éxito");
			//Ponemos las entradas en blanco
			eraseControls(false);
			
		}
	}

	public void enviarEmail(int idalumno, String alumno){
		String directory = System.getProperty("user.dir"); // Directorio principal del programa
		String target = directory + "/target/"; // Calculamos el target de este proyecto

		try (FileWriter fw = new FileWriter(target + "email.txt")) {
		    fw.write("Hola, " + alumno + ", te escribimos para comunicarte que te has inscrito con éxito. \n");
		    fw.close();
		} catch (IOException e) { throw new ApplicationException(e); }
	}

	public void getListaInscripciones() {
		JTable table = vista.getTableInscripciones();
		TableModel tmodel; //Modelo de la tabla a inicializar según checkbox
		if(vista.getChkAll().isSelected()) {
			
		List<PagoDTO> inscripcionesAll = modelo.getListaInscripcionesCompleta(Util.isoStringToDate("2022-05-15"));
		tmodel = SwingUtil.getTableModelFromPojos(inscripcionesAll, new String[] { "id", "alumno_id", "nombre", "fecha", "coste", "estado" }); //La primera columna estará oculta
		
		} else {
			
		List<PagoDTO> inscripcionesPagadas = modelo.getListaInscripcionesPagadas(Util.isoStringToDate("2022-05-15"));
		tmodel = SwingUtil.getTableModelFromPojos(inscripcionesPagadas, new String[] { "id", "alumno_id", "nombre", "fecha", "coste", "estado" }); //La primera columna estará oculta
		
		}
		
		table.setModel(tmodel);

		// Ocultar foreign keys de la tabla
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		//SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
