package g41.si2022.coiipa.insertar_devolucion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.cancelacionDTO;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.ApplicationException;
import g41.si2022.util.Util;

public class InsertarDevolucionController {

	private InsertarDevolucionView vista;
	private InsertarDevolucionModel modelo;

	public InsertarDevolucionController(InsertarDevolucionView vista, InsertarDevolucionModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		this.initView();
	}

	private int idInscripcion = -1;
	private int idAlumno = -1;

	public void initView() {

		//Establecemos la fecha a hoy


		getListaInscripciones(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado

		vista.getBtnCancelarInscripcion().addActionListener(e -> handleDevolver());
		vista.getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
	}

	private void setControls(boolean status) {

		vista.getBtnCancelarInscripcion().setEnabled(status);
	}

	private void eraseControls(boolean eliminaraviso) {
		vista.getLblNombreInscripcion().setText("No se ha seleccionado ningún nombre");
		vista.getLblImporteDevuelto().setText("");
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

		Double costeCurso = Double.valueOf((String) tempModel.getValueAt(fila,  3));

		Date fechaActual = Date.from(vista.getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fechaCurso = Util.isoStringToDate((String) tempModel.getValueAt(fila, 5));

		//Calculamos el número de días que quedan

		long dias = ChronoUnit.DAYS.between(fechaActual.toInstant(), fechaCurso.toInstant());

		//Calculamos le importe que le será devuelto al usuario.
		double importeDevuelto = 0;

		if(dias > 7) importeDevuelto = costeCurso;
		if(dias > 3 && dias < 7) importeDevuelto = costeCurso / 2;
		if(dias < 3) importeDevuelto = 0;

		System.out.printf("Coste %f, %f \n", costeCurso, importeDevuelto);
		vista.getLblImporteDevuelto().setText(Double.toString(importeDevuelto));

		setControls(true);
	}

	private void handleDevolver() {
		//Obtenemos el nombre del inscrito
		String nombreInscrito = vista.getLblNombreInscripcion().getText();

		//if(nombreInscrito == "No se ha seleccionado ningún nombre" || importeString == null || vista.getDatePicker().getDate() == null) {

		vista.getLblError().setText("Por favor, rellena todos los campos para continuar"); //Mostramos un error
		return;

		//} else {

		// Hacemos las conversiones a Date y int
		//	Date fechaPago = java.sql.Date.valueOf(vista.getDatePicker().getDate());
		//	int importe = Integer.parseInt(importeString);

		//	System.out.printf("Se han pagado %s € para el alumno %s, en la inscripción: %d, con fecha %s\n", importe, nombreInscrito, idInscripcion, fechaPago.toString()); //DEBUG
		//	modelo.registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion); // Registro en la BBDD el pago
		//	enviarEmail(idAlumno, vista.getLblNombreInscripcion().getText()); // Envío un email al alumno
		//	getListaInscripciones(); // Refrescamos la tabla al terminar de inscribir a la persona
		//Si había algún error habilitado en la etiqueta, lo deshabilitamos y mostramos éxito
		//	vista.getLblError().setText("Pago insertado con éxito");
		//Ponemos las entradas en blanco
		//	eraseControls(false);

		//}
	}

	public void enviarEmail(int idalumno, String alumno){
		String directory = System.getProperty("user.dir"); // Directorio principal del programa
		String target = directory + "/target/"; // Calculamos el target de este proyecto

		try (FileWriter fw = new FileWriter(target + "email.txt")) {
			fw.write("Hola, " + alumno + ", te escribimos para comunicarte que se ha cancelado tu inscripción con éxito. \n");
			fw.close();
		} catch (IOException e) { throw new ApplicationException(e); }
	}

	public void getListaInscripciones() {

		this.eraseControls(true);
		//Obtengo la tabla de inscripciones
		JTable table = vista.getTableInscripciones();
		TableModel tmodel; //Modelo de la tabla
		Date fechaActual = Date.from(vista.getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());

		List<cancelacionDTO> inscripcionesAll = modelo.getListaInscripciones(fechaActual);
		//tmodel = SwingUtil.getTableModelFromPojos(inscripcionesAll, new String[] { "id", "alumno_id", "nombre", "coste", "nombre_curso", "inicio_curso" }); //La primera columna estará oculta
		table.setModel(SwingUtil.getTableModelFromPojos(
				inscripcionesAll,
				new String[] { "id", "alumno_id", "nombre", "coste", "nombre_curso", "inicio_curso"},	//La primera columna estará oculta
				new String[] { "ID", "ID Alumno", "Nombre curso", "Coste", "Nombre curso", "Fecha de inicio"},
				null
				));

		// Ocultar foreign keys de la tabla
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
