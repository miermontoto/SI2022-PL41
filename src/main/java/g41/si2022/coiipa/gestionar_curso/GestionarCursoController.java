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

	//Variables globales para los valores seleccionados
	int idCurso;
	int idTabla;
	String nombreCurso;
	String fechaCurso;
	String fechaInscripciones;
	LocalDate localDateCurso;
	LocalDate localDateInscripciones;
	LocalDate localDateFinCurso;
	LocalDate localDateFinInscripciones;
	String fechaFinCurso;
	String fechaFinInscripciones;
	String plazas;
	String plazasLibres;


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
				
				  	JTable source = (JTable)e.getSource();
		            int row = source.rowAtPoint(e.getPoint() );
		            int column = source.columnAtPoint(e.getPoint() );
		            
		            idTabla = row;

				handleSelect(); //Que handleSelect() se encargue de todo lo relacionado con esta selección.

			}
		});
		
		this.getView().getBtnCambiarFechas().addActionListener(e -> handleCambiarFechas());
		this.getView().getBtnCambiarDetalles().addActionListener(e -> handleCambiarDetalles());

	}

	private void handleSelect() { //Función para manejar la fila seleccionada de la tabla


		TableModel model = this.getView().getTableInscripciones().getModel();

		//Obtengo los datos de la tabla y los almaceno en variables globales (por si a otros métodos les hacen falta)

		idCurso = Integer.parseInt(this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 0).toString());
		nombreCurso = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 1).toString();
		fechaCurso = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 2).toString();
		fechaFinCurso = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 3).toString();
		fechaInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 4).toString();
		fechaFinInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 5).toString();
		plazas = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 6).toString();
		plazasLibres = this.getView().getTableInscripciones().getModel().getValueAt(idTabla, 7).toString();

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
		this.getView().getTxtFieldPlazas().setText(plazas);

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
			this.getModel().updateFechas(idCurso, fechaCurso, fechaFinCurso, fechaInscripciones, fechaFinInscripciones);
			updateTables(); //Actualizamos la tabla de los datos tras producir la inserción
			alert("Éxito", "Se han modificado las fechas del curso con éxito", JOptionPane.INFORMATION_MESSAGE);


		}
		else
		{
			alert("ERROR", "No hemos modificado nada, ya que había parámetros incorrectos. Por favor, corrígelos e inténtalo de nuevo.", JOptionPane.ERROR_MESSAGE);
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
		fechaCursoFin = this.getView().getDatePickerNewDateFinCurso().getDateStringOrEmptyString(); 
		fechaInscripcionInicio = this.getView().getDatePickerNewDateInscripciones().getDateStringOrEmptyString();
		fechaInscripcionFin = this.getView().getDatePickerNewDateFinInscripciones().getDateStringOrEmptyString();
		
		//DEBUG
		
		System.out.printf("Fecha del inico del curso es: %s \n", fechaCursoInicio);
		System.out.printf("Fecha del fin del curso es: %s \n", fechaCursoFin);
		System.out.printf("Fecha del inico de inscripciones es: %s \n", fechaInscripcionInicio);
		System.out.printf("Fecha del fin de inscripciones es: %s \n", fechaInscripcionFin);
		
		
		if (fechaCursoInicio == "" || fechaCursoFin == "" || fechaInscripcionInicio == "" || fechaInscripcionFin == "") 
		{
			
			alert("ERROR","Las fechas no pueden ser nulas", JOptionPane.ERROR_MESSAGE);
			
		}

		DateTimeFormatter f = DateTimeFormatter.ofPattern( "yyyy-MM-dd"); //Formateador de fechas

		//Acabamos el curso antes de empezar el curso
		
		LocalDate start = LocalDate.parse( fechaCursoInicio , f );
		LocalDate stop = LocalDate.parse( fechaCursoFin , f );
				
		if(!(start.isBefore( stop ))) {
			alert("ERROR", "No puede acabar el curso antes de la fecha de inicio del curso", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//Acabamos las inscripciones antes de empezar las inscripciones.
		
		start = LocalDate.parse( fechaInscripcionInicio , f );
		stop = LocalDate.parse( fechaInscripcionFin , f );
		
		if(!(start.isBefore( stop ))) {
			alert("ERROR", "No pueden acabar las inscripciones antes de empezar las inscripciones", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//El inicio de inscripciones es después del inicio del curso.
		
		start = LocalDate.parse( fechaInscripcionInicio , f );
		stop = LocalDate.parse( fechaCursoInicio , f );
		
		if(!(start.isBefore( stop ))) {
			alert("ERROR", "No pueden empezar las inscripciones más tarde que el curso", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		
		//Todo ha ido bien
		
		//Asigno las inscripciones a la variable global, y devuelvo verdadero
		
		this.fechaCurso = fechaCursoInicio;
		this.fechaFinCurso = fechaCursoFin;
		this.fechaInscripciones = fechaInscripcionInicio;
		this.fechaFinInscripciones = fechaInscripcionFin;
		
		return true;
		
	}
	
	
	public void handleCambiarDetalles() {
		
		String plazasNuevas = this.getView().getTxtFieldPlazas().getText(); //Obtengo el texto.
		
		int ocupadas = Integer.parseInt(plazas) - Integer.parseInt(plazasLibres);
		
		if(Integer.parseInt(plazasNuevas) >= ocupadas)
		{
			if(Integer.parseInt(plazasNuevas) <= 0) 
			{
				alert("ERROR", "No puedes poner plazas negativas. Corrígelo", JOptionPane.ERROR_MESSAGE);

			}
			else 
			{
				this.plazas = plazasNuevas; //Actualizo variable global de control.
				this.getModel().updateDetalles(idCurso, plazasNuevas); //Llamo a la BBDD
				updateTables(); //Llamo a la función de actualización de tablas
				alert("Éxito", "Se han cambiado los detalles indicados con éxito.", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			alert("ERROR", "No puedes poner menos plazas que inscritos ya hay. Ahora mismo hay ocupadas " + ocupadas, JOptionPane.ERROR_MESSAGE);
		}

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
		
		//Habilito panel de cambiar otros detalles
		this.getView().getTxtFieldPlazas().setEnabled(status);
		this.getView().getBtnCambiarDetalles().setEnabled(status);
		
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
		
		this.getView().getTxtFieldPlazas().setText("");
		
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
						new String[] { "id", "nombre", "start", "end", "start_inscr", "end_inscr",  "plazas", "plazas_libres"},
						new String[] { "Id", "Nombre", "Inicio Curso", "Fin Curso", "Inicio Inscripciones", "Fin Inscripciones",  "Plazas disponibles", "Plazas libres"},
						null
						)
				);
		//Ocultamos la columna 0 de la vista.
		this.getView().getTableInscripciones().removeColumn(this.getView().getTableInscripciones().getColumnModel().getColumn(0));
		eraseControls(false); //Borramos los controles.
	}

}
