package g41.si2022.coiipa.gestionar_curso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.CursoDTO;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Util;
import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.StateUtilities;

public class GestionarCursoController extends g41.si2022.mvc.Controller<GestionarCursoView, GestionarCursoModel> {


	int idCurso;
	String nombreCurso;
	String fechaCurso;
	String fechaInscripciones;
	LocalDate localDateCurso;
	LocalDate localDateInscripciones;
	CursoDTO selectedCurso;
	List<CursoDTO> listaCursos;

	public GestionarCursoController(GestionarCursoView myTab, GestionarCursoModel myModel) {
		super(myTab, myModel);
	}

	@Override
	public void initNonVolatileData() {
		// Acción del botón para cancelar cursos
		this.getView().getBtnCancelarCurso().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String estadoDB;
				selectedCurso.setEstado(StateUtilities.getCursoState(selectedCurso, getTab().getMain().getToday()));
				estadoDB = getModel().getDBcursoState(String.valueOf(idCurso));
				// Si el curso no está cancelado, se cancela. En la database sólo se guarda el estado CANCELADO
				// de un curso. Por defecto el atributo estado es null
				if (estadoDB.equals("null")) {
					// Modificar estado del curso a CANCELADO. Se modifica el atributo en la database
					getModel().updateCursoStateToCancelled(String.valueOf(CursoState.CANCELADO), selectedCurso.getId());
					selectedCurso.setEstado(StateUtilities.getCursoState(selectedCurso, getTab().getMain().getToday()));
					// Obtener emails de los alumnos para enviar un correo.
					List<String> emails = getModel().getAlumnosEmail(String.valueOf(idCurso));
					// Enviar correo a alumnos para informar de la cancelación del curso
					for (String email: emails) 
						Util.sendEmail(email, "Cancelación de curso",
						"Desde COIIPA le informamos de que el curso al que estaba inscrito " + selectedCurso.getNombre() + " ha sido cancelado.");
					
					System.out.printf("El curso %s ha sido cancelado\n", nombreCurso);
				}
				updateTables();
			}
		});
	}

	@Override
	public void initVolatileData() {
		this.initThings();
		this.updateTables();
	}

	public void initThings() {

		eraseControls(true); //Deshabilitamos los controles hasta que ocurra algo en la tabla.

		//Listener de la tabla, para poder detectar los distintos clicks en la tabla
		this.getView().getTableInscripciones().addMouseListener( new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{

				handleSelect(); //Que handleSelect() se encargue de todo lo relacionado con esta selección.

			}
		});

	}

	private void handleSelect() { //Función para manejar la fila seleccionada de la tabla

		JTable table = getView().getTableInscripciones();
		TableModel model = this.getView().getTableInscripciones().getModel();

		//Obtengo los datos de la tabla y los almaceno en variables globales (por si a otros métodos les hacen falta)

		idCurso = table.convertRowIndexToModel(table.getSelectedRow()) + 1;
		nombreCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 1).toString();
		fechaInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 2).toString();
		fechaCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 4).toString();
		selectedCurso = getModel().getCurso(String.valueOf(idCurso));
		//DEBUG
		// System.out.printf("ID: %s, nombre: %s, fecha del curso: %s, fecha de inscripciones: %s \n", idCurso, nombreCurso, fechaCurso, fechaInscripciones);

		if (idCurso == -1) { //Lo que se ha seleccionado está mal.
			eraseControls(true);
			return;
		}


		//Set de etiquetas en la IU.
		this.getView().getLblFechaCurso().setText(fechaCurso);
		this.getView().getLblInfoNombre().setText(nombreCurso);

		//Conversión a localDate
		localDateCurso = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaCurso)));
		localDateInscripciones = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaInscripciones)));

		//Set de fechas en la IU.
		this.getView().getDatePickerNewDateCurso().setDate(localDateCurso);
		this.getView().getDatePickerNewDateInscripciones().setDate(localDateInscripciones);

		//Hemos terminado el proceso, habilitamos los controles
		setControls(true);

		/*String date = model.getValueAt(row, 6).toString();
		if (date.equals("")) {
			this.getView().getDatePicker().setDateToToday();
			setControls(true);
		} else {
			this.getView().getDatePicker().setDate(LocalDate.parse(date));
			setControls(false);
		}*/
		
	}

	private void setControls(boolean status) {
		
		//Habilito los datePicker
		this.getView().getDatePickerNewDateCurso().setEnabled(status);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(status);
		//Habilito el botón de cambiar las fechas.
		this.getView().getBtnCambiarFechas().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {

		//Etiquetas de información
		this.getView().getLblInfoNombre().setText("N/A");
		this.getView().getLblFechaCurso().setText("N/A");

		//Datepicker
		this.getView().getDatePickerNewDateCurso().setText("");
		this.getView().getDatePickerNewDateInscripciones().setText("");

		setControls(false); //Apagamos los controles

		/*
		this.getView().getDatePickerNewDateCurso().setEnabled(false);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(false);
		 */

		//if(eliminarAviso) this.getView().getLblError().setText("");
	}

	public void updateTables() {

		listaCursos = this.getModel().getCursos();

		for (CursoDTO curso: listaCursos) 
			curso.setEstado(StateUtilities.getCursoState(curso, this.getTab().getMain().getToday()));
		
		this.getView().getTableInscripciones().setModel(
				SwingUtil.getTableModelFromPojos(
						listaCursos,
						new String[] { "id", "nombre", "start_inscr", "end_inscr", "start", "end", "estado"},
						new String[] { "Id" ,"Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso", "Estado"},
						null
						)
				);
		//Ocultamos la columna 0 de la vista.
		this.getView().getTableInscripciones().removeColumn(this.getView().getTableInscripciones().getColumnModel().getColumn(0));
	}
}
