package g41.si2022.coiipa.gestionar_curso;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Util;

public class GestionarCursoController extends g41.si2022.mvc.Controller<GestionarCursoView, GestionarCursoModel> {


	int idCurso;
	String nombreCurso;
	String fechaCurso;
	String fechaInscripciones;
	LocalDate localDateCurso;
	LocalDate localDateInscripciones;
	String fechaFinCurso;
	String fechaFinInscripciones;
	LocalDate localDateFinCurso;
	LocalDate localDateFinInscripciones;

	public GestionarCursoController(GestionarCursoView myTab, GestionarCursoModel myModel) {
		super(myTab, myModel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initNonVolatileData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initVolatileData() {
		// TODO Auto-generated method stub
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
		
		this.getView().getBtnCambiarFechas().addActionListener(e -> handleCambiarFechas());

	}

	private void handleSelect() { //Función para manejar la fila seleccionada de la tabla


		TableModel model = this.getView().getTableInscripciones().getModel();

		//Obtengo los datos de la tabla y los almaceno en variables globales (por si a otros métodos les hacen falta)

		idCurso = this.getView().getTableInscripciones().getSelectedRow();
		nombreCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 1).toString();
		fechaInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 2).toString();
		fechaCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 4).toString();
		fechaFinInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 3).toString();
		fechaFinCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 5).toString();

		//DEBUG

		System.out.printf("ID: %s, nombre: %s, fecha del curso: %s, fecha de inscripciones: %s \n", idCurso, nombreCurso, fechaCurso, fechaInscripciones);

		if(idCurso == -1) { //Lo que se ha seleccionado está mal.
			eraseControls(true);
			return;
		}


		//Set de etiquetas en la IU.
		this.getView().getLblFechaCurso().setText(fechaCurso);
		this.getView().getLblInfoNombre().setText(nombreCurso);

		//Conversión a localDate
		localDateCurso = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaCurso)));
		localDateInscripciones = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaInscripciones)));
		localDateFinCurso = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaFinCurso)));
		localDateFinInscripciones = LocalDate.parse( new SimpleDateFormat("yyyy-MM-dd").format(Util.isoStringToDate(fechaFinInscripciones)));

		//Set de fechas en la IU.
		this.getView().getDatePickerNewDateCurso().setDate(localDateCurso);
		this.getView().getDatePickerNewDateInscripciones().setDate(localDateInscripciones);
		this.getView().getDatePickerNewDateFinCurso().setDate(localDateFinCurso);
		this.getView().getDatePickerNewDateFinInscripciones().setDate(localDateFinInscripciones);

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
	
	
	public void handleCambiarFechas() {
		
		if(checkFechas())
		{
			//this.getModel()fechaCurso.updateFechas(fechaCurso, fechaFinCurso, fechaInscripciones, fechaFinInscripciones);
		}
		else
		{
			alert("Éxito", "Se han modificado las fechas del curso con éxito", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}
	
	//Función que implementa un sistema para comprobar las fechas que se introducen
	//En caso de que algo esté mal, devuelve falso yun mensaje de error.
	public boolean checkFechas() {
		
		String fechaCursoInicio;
		String fechaCursoFin;
		String fechaInscripcionInicio;
		String fechaInscripcionFin;
		
		fechaCursoInicio = this.getView().getDatePickerNewDateCurso().getDateStringOrEmptyString();
		fechaCursoFin = this.getView().getDatePickerNewDateInscripciones().getDateStringOrEmptyString();
		fechaInscripcionInicio = this.getView().getDatePickerNewDateFinCurso().getDateStringOrEmptyString();
		fechaInscripcionFin = this.getView().getDatePickerNewDateFinInscripciones().getDateStringOrEmptyString();
		
		DateTimeFormatter f = DateTimeFormatter.ofPattern( "yyyy-MM-dd" ); //Formateador de fechas
		
		if (fechaCursoInicio == "" || fechaCursoFin == "" || fechaInscripcionInicio == "" || fechaInscripcionFin == "") 
		{
			
			alert("Las fechas no pueden ser nulas", "ERROR", JOptionPane.ERROR_MESSAGE);
			
		}

		
		LocalDateTime start = LocalDateTime.parse( fechaCursoInicio , f );
		LocalDateTime stop = LocalDateTime.parse( fechaCursoFin , f );
		
		if(!(start.isBefore( stop ))) {
			alert("No puede acabar el curso antes de la fecha de inicio", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		 start = LocalDateTime.parse( fechaInscripcionInicio , f );
		 stop = LocalDateTime.parse( fechaInscripcionFin , f );
		
		if(!(start.isBefore( stop ))) {
			alert("No pueden acabar las inscripciones antes de empezar", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		start = LocalDateTime.parse( fechaInscripcionInicio , f );
		 stop = LocalDateTime.parse( fechaCursoInicio , f );
		
		if(!(start.isBefore( stop ))) {
			alert("No pueden empezar las inscripciones más tarde que el curso", "ERROR", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		
		//Todo ha ido bien
		
		//Asigno las inscripciones a la variable global
		
		this.fechaCurso = fechaCursoInicio;
		this.fechaFinCurso = fechaCursoFin;
		this.fechaInscripciones = fechaInscripcionInicio;
		this.fechaFinInscripciones = fechaInscripcionFin;
		
		return true;
		
	}
	
	public void alert(String title, String msg, int level) {
		
		JOptionPane.showMessageDialog(null,msg , title, level);

	}

	private void setControls(boolean status) {
		
		//Habilito los datePicker
		this.getView().getDatePickerNewDateCurso().setEnabled(status);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(status);
		this.getView().getDatePickerNewDateFinCurso().setEnabled(status);
		this.getView().getDatePickerNewDateFinInscripciones().setEnabled(status);
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
		this.getView().getDatePickerNewDateFinCurso().setText("");
		this.getView().getDatePickerNewDateFinInscripciones().setText("");
		
		setControls(false); //Apagamos los controles

		/*
		this.getView().getDatePickerNewDateCurso().setEnabled(false);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(false);
		 */

		//if(eliminarAviso) this.getView().getLblError().setText("");
	}

	public void updateTables() {

		List <CursoDTO> listacursos = this.getModel().getCursos();

		this.getView().getTableInscripciones().setModel(
				SwingUtil.getTableModelFromPojos(
						listacursos,
						new String[] { "id", "nombre", "start_inscr", "end_inscr", "start", "end", "plazas", "plazas_libres"},
						new String[] { "Id", "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso", "Plazas disponibles", "Plazas libres"},
						null
						)
				);
		//Ocultamos la columna 0 de la vista.
		this.getView().getTableInscripciones().removeColumn(this.getView().getTableInscripciones().getColumnModel().getColumn(0));

	}

}
