package g41.si2022.coiipa.insertar_devolucion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.cancelacionDTO;
import g41.si2022.ui.SwingUtil;
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

		//System.out.printf("Coste %f, %f \n", costeCurso, importeDevuelto);
		vista.getLblImporteDevuelto().setText(Double.toString(importeDevuelto));

		setControls(true);
	}

	private void handleDevolver() {
		//Obtenemos el nombre del inscrito
		String nombreInscrito = vista.getLblNombreInscripcion().getText();

			Date fechaActual = Date.from(vista.getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
			double importedevuelto = Double.parseDouble(vista.getLblImporteDevuelto().getText());

			modelo.registrarDevolucion(importedevuelto, Util.dateToIsoString(fechaActual), idInscripcion);

			enviarEmail(idAlumno, nombreInscrito);

			SwingUtil.showMessage("La inscripción del alumno " + nombreInscrito + " ha sido realizada con éxito. Se le han devuelto " + importedevuelto + " €", "Servicio de cancelaciones");

			getListaInscripciones();

			setControls(false);
	}

		public void enviarEmail(int idAlumno, String alumno){

			String email = modelo.getEmailAlumno(Integer.toString(idAlumno));
			Util.sendEmail(email, "Cancelado con éxito", "Hola, hemos realizado la cancelación de tu curso con éxito");


		}

		public void getListaInscripciones() {

			this.eraseControls(true);
			// Obtengo la tabla de inscripciones
			JTable table = vista.getTableInscripciones();
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
