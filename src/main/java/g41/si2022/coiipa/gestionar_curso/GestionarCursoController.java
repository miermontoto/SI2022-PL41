package g41.si2022.coiipa.gestionar_curso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;

import g41.si2022.ui.util.Dialog;

import g41.si2022.util.Util;
import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.StateUtilities;


public class GestionarCursoController extends g41.si2022.mvc.Controller<GestionarCursoView, GestionarCursoModel> {

	// Variables globales para los valores seleccionados
	int idCurso;
	String nombreCurso;
	String fechaIniCurso;
	String fechaIniInscr;
	LocalDate localDateCurso;
	LocalDate localDateInscripciones;

	LocalDate localDateFinCurso;
	LocalDate localDateFinInscripciones;
	String fechaFinCurso;
	String fechaFinInscr;
	String plazas;
	String plazasLibres;

	CursoDTO selectedCurso;
	List<CursoDTO> listaCursos;
	List<InscripcionDTO> listaInscr;

	public GestionarCursoController(GestionarCursoView myTab, GestionarCursoModel myModel) {
		super(myTab, myModel);
	}

	@Override
	public void initNonVolatileData() {

		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { handleSelect(); }
		});

		this.getView().getBtnCambiarFechas().addActionListener(e -> handleCambiarFechas());
		this.getView().getBtnCambiarDetalles().addActionListener(e -> handleCambiarDetalles());

		// Acción del botón para cancelar cursos
		this.getView().getBtnCancelarCurso().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String estadoDB = getModel().getDBcursoState(String.valueOf(idCurso));

				// selectedCurso.setEstado(StateUtilities.getCursoState(selectedCurso, getTab().getMain().getToday()));
				// Si el curso no está cancelado, se cancela. En la database sólo se guarda el estado CANCELADO
				// de un curso. Por defecto el atributo estado es null
				if (estadoDB.equals("null")) {
					// Modificar estado del curso a CANCELADO. Se modifica el atributo en la database
					getModel().updateCursoStateToCancelled(String.valueOf(CursoState.CANCELADO), selectedCurso.getId());
					selectedCurso.setEstado(StateUtilities.getCursoState(selectedCurso, getTab().getMain().getToday()));

					// Obtener emails de los alumnos para enviar un correo.
					List<String> emailsAlumnos = getModel().getAlumnosEmail(String.valueOf(idCurso));
					List<String> emailsProfs = getModel().getProfesoresEmail(String.valueOf(idCurso));

					// Enviar correo a alumnos para informar de la cancelación del curso
					for (String email : emailsAlumnos) {
						Util.sendEmail(email, "Cancelación de curso",
						"Desde COIIPA le informamos de que el curso al que estaba inscrito " + selectedCurso.getNombre() + " ha sido cancelado.");
					}

					for (String email : emailsProfs) {
						Util.sendEmail(email, "Cancelación de curso",
						"Desde COIIPA le informamos de que el curso al que estaba inscrito " + selectedCurso.getNombre() + " ha sido cancelado.");
					}

					cancelarInscripciones(selectedCurso); // Cancelar inscripciones relacionadas con el curso

					Dialog.show("Curso cancelado. Se ha enviado un e-mail tanto a los inscritos como al profesorado. Se devolverá el 100% del importe pagado");
				}

				updateTables();
			}
		});
	}

	@Override
	public void initVolatileData() {

		eraseControls(true);
		updateTables();

		//Listener de la tabla, para poder detectar los distintos clicks en la tabla
		getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) { handleSelect(); }
		});

		updateTables();
	}

	private void handleSelect() { // Función para manejar la fila seleccionada de la tabla
		JTable table = getView().getTableInscripciones();
		TableModel model = table.getModel();
		int row = table.convertRowIndexToModel(table.getSelectedRow());

		idCurso = Integer.parseInt(model.getValueAt(row, 0).toString());
		nombreCurso = model.getValueAt(row, 1).toString();
		
		//Obtengo los valores para las distintas fechas que voy a probar.
		fechaIniCurso = model.getValueAt(row, 2).toString();
		fechaFinCurso = model.getValueAt(row, 3).toString();
		fechaIniInscr = model.getValueAt(row, 4).toString();
		fechaFinInscr = model.getValueAt(row, 5).toString();
		
		plazas = model.getValueAt(row, 6).toString();
		plazasLibres = model.getValueAt(row, 7).toString();
		selectedCurso = this.getModel().getCurso(String.valueOf(idCurso));

		if (idCurso == -1) { //Lo que se ha seleccionado está mal.
			eraseControls(true);
			return;
		}

		getView().getLblFechaCurso().setText(fechaIniCurso + " → " + fechaFinCurso);
		getView().getLblFechaInscripcion().setText(fechaIniInscr + " → " + fechaFinInscr);
		getView().getLblInfoNombre().setText(nombreCurso);

		localDateCurso = LocalDate.parse(fechaIniCurso);
		localDateInscripciones = LocalDate.parse(fechaIniInscr);
		localDateFinCurso = LocalDate.parse(fechaFinCurso);
		localDateFinInscripciones = LocalDate.parse(fechaFinInscr);

		getView().getDateNewIniCurso().setDate(localDateCurso);
		getView().getDateNewIniInscr().setDate(localDateInscripciones);
		getView().getDateNewFinCurso().setDate(localDateFinCurso);
		getView().getDateNewFinInscr().setDate(localDateFinInscripciones);
		getView().getTxtFieldPlazas().setText(plazas);

		setControls(true); // Hemos terminado el proceso, habilitamos los controles.
	}


	public void handleCambiarFechas() {

		String fechaIniCursoEdit = getView().getDateNewIniCurso().toString();
		String fechaFinCursoEdit = getView().getDateNewFinCurso().toString();
		String fechaIniInscrEdit = getView().getDateNewIniInscr().toString();
		String fechaFinInscrEdit = getView().getDateNewFinInscr().toString();
		
		boolean returnValue = getModel().updateFechas(idCurso, fechaIniCursoEdit, fechaFinCursoEdit, fechaIniInscrEdit, fechaFinInscrEdit);
		
		if(returnValue) {
			updateTables(); // Actualizamos la tabla de los datos tras producir la inserción
			Dialog.show("Se han modificado las fechas del curso con éxito");
		}
		else {
			Dialog.showError("No hemos modificado nada, ya que había parámetros incorrectos. Por favor, corrígelos e inténtalo de nuevo.");
		}

	}


	// Función que implementa un sistema para comprobar las fechas que se introducen
	// En caso de que algo esté mal, devuelve falso y un mensaje de error.
	/*public boolean checkFechas() {
		String fechaCursoInicio = getView().getDateNewIniCurso().getDateStringOrEmptyString();
		String fechaCursoFin = getView().getDateNewFinCurso().getDateStringOrEmptyString();
		String fechaInscripcionInicio = getView().getDateNewIniInscr().getDateStringOrEmptyString();
		String fechaInscripcionFin = getView().getDateNewFinInscr().getDateStringOrEmptyString();

		if (fechaCursoInicio == "" || fechaCursoFin == "" || fechaInscripcionInicio == "" || fechaInscripcionFin == "") {
			Dialog.showError("Las fechas no pueden ser nulas");
		}

		// Acabamos el curso antes de empezar el curso
		LocalDate start = LocalDate.parse(fechaCursoInicio);
		LocalDate stop = LocalDate.parse(fechaCursoFin);

		if (start.isAfter(stop)) {
			Dialog.showError("No puede acabar el curso antes de la fecha de inicio del curso");
			return false;
		}

		// Acabamos las inscripciones antes de empezar las inscripciones.
		start = LocalDate.parse(fechaInscripcionInicio);
		stop = LocalDate.parse(fechaInscripcionFin);

		if(start.isAfter(stop)) {
			Dialog.showError("No pueden acabar las inscripciones antes de empezar las inscripciones");
			return false;
		}

		// El inicio de inscripciones es después del inicio del curso.

		start = LocalDate.parse(fechaInscripcionInicio);
		stop = LocalDate.parse(fechaCursoInicio);

		if(start.isAfter(stop)) {
			Dialog.showError("No pueden empezar las inscripciones más tarde que el curso");
			return false;
		}

		// Todo ha ido bien, guardamos las fechas.
		this.fechaIniCurso = fechaCursoInicio;
		this.fechaFinCurso = fechaCursoFin;
		this.fechaIniInscr = fechaInscripcionInicio;
		this.fechaFinInscr = fechaInscripcionFin;

		return true;
	}*/


	public void handleCambiarDetalles() {
		String plazasNuevas = this.getView().getTxtFieldPlazas().getText(); //Obtengo el texto.
		int ocupadas = Integer.parseInt(plazas) - Integer.parseInt(plazasLibres);
		int plazasNuevasInt = Integer.parseInt(plazasNuevas);

		if(plazasNuevasInt < ocupadas) {
			Dialog.showError("El nuevo número de plazas debe ser superior al número de plazas ocupadas. Ahora mismo hay ocupadas " + ocupadas);
			return;
		}

		if(plazasNuevasInt <= 0) {
			Dialog.showError("Se han introducido plazas negativas.");
			return;
		}

		this.plazas = plazasNuevas;
		getModel().updateDetalles(idCurso, plazasNuevas);
		updateTables();
		Dialog.show("Se han cambiado los detalles indicados con éxito.");
	}

	private void setControls(boolean status) {
		getView().getDateNewIniCurso().setEnabled(status);
		getView().getDateNewIniInscr().setEnabled(status);
		getView().getDateNewFinCurso().setEnabled(status);
		getView().getDateNewFinInscr().setEnabled(status);
		getView().getBtnCambiarFechas().setEnabled(status);
		getView().getTxtFieldPlazas().setEnabled(status);
		getView().getBtnCambiarDetalles().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {
		getView().getLblInfoNombre().setText("N/A");
		getView().getLblFechaCurso().setText("N/A");
		getView().getDateNewIniCurso().setText("");
		getView().getDateNewIniInscr().setText("");
		getView().getDateNewFinCurso().setText("");
		getView().getDateNewFinInscr().setText("");
		getView().getTxtFieldPlazas().setText("");

		setControls(false);

		/*
		this.getView().getDatePickerNewDateCurso().setEnabled(false);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(false);
		if(eliminarAviso) this.getView().getLblError().setText("");
		*/
	}

	public void updateTables() {
		List <CursoDTO> cursos = this.getModel().getCursos();
		JTable table = this.getView().getTableInscripciones();

		for (CursoDTO curso: cursos)
			curso.setEstado(StateUtilities.getCursoState(curso, getTab().getMain().getToday()));

		table.setModel(
			SwingUtil.getTableModelFromPojos(
				cursos,
				new String[] { "id", "nombre", "start_inscr", "end_inscr", "start", "end", "plazas", "plazas_libres", "estado"},
				new String[] { "Id", "Nombre", "Ini. inscr.", "Fin inscr.", "Ini. curso", "Fin curso",  "Plazas totales", "Plazas libres", "Estado"},
				null
			)
		);

		table.removeColumn(table.getColumnModel().getColumn(0));
		eraseControls(false);
		// SwingUtil.autoAdjustColumns(table);
	}

	/**
	 * Change the status of all inscriptions in the course inscription list to 'CANCELADA'
	 * 
	 * @param curso To cancel it inscriptions
	 */
	public void cancelarInscripciones(CursoDTO curso) {
		listaInscr = getModel().getCursoInscripciones(curso.getId());

		for (InscripcionDTO inscr: listaInscr) 
			getModel().cancelarInscripcion(inscr.getId());
		

	}
}
