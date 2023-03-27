package g41.si2022.coiipa.gestionar_curso;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;

public class GestionarCursoController extends g41.si2022.mvc.Controller<GestionarCursoView, GestionarCursoModel> {


	int idCurso;
	String nombreCurso;
	String fechaCurso;
	String fechaInscripciones;
	
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
		
		this.getView().getTableInscripciones().addMouseListener( new MouseAdapter()
		{
		    public void mousePressed(MouseEvent e)
		    {

		        handleSelect(); //Que handleSelect() se encargue de todo lo relacionado con esta selección.
		        
		        }
		});
		
	}
	
	private void handleSelect() { //Función para manejar la fila seleccionada de la tabla
		
		
		TableModel model = this.getView().getTableInscripciones().getModel();
		
		//Obtengo los datos de la tabla y los almaceno en variables globales (por si a otros métodos les hacen falta)
		idCurso = this.getView().getTableInscripciones().getSelectedRow();
		nombreCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 1).toString();
		fechaInscripciones = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 2).toString();
		fechaCurso = this.getView().getTableInscripciones().getModel().getValueAt(idCurso, 4).toString();

		//DEBUG
		
		System.out.printf("ID: %s, nombre: %s, fecha del curso: %s, fecha de inscripciones: %s \n", idCurso, nombreCurso, fechaCurso, fechaInscripciones);

		
		if(idCurso == -1) {
			eraseControls(true);
			return;
		}
		
		
		//Set de etiquetas en la IU.
		this.getView().getLblFechaCurso().setText(fechaCurso);
		this.getView().getLblInfoNombre().setText(nombreCurso);
		this.getView().getDatePickerNewDateCurso().setText(fechaCurso);
		this.getView().getDatePickerNewDateInscripciones().setText(fechaInscripciones);
		
		
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
		this.getView().getBtnCambiarFechas().setEnabled(status);
		//this.getView().getDatePicker().setEnabled(status);
	}
	
	private void eraseControls(boolean eliminarAviso) {
		
		//Etiquetas de información
		this.getView().getLblInfoNombre().setText("N/A");
		this.getView().getLblFechaCurso().setText("N/A");
		
		//Datepicker
		this.getView().getDatePickerNewDateCurso().setText("");
		this.getView().getDatePickerNewDateInscripciones().setText("");
		this.getView().getDatePickerNewDateCurso().setEnabled(false);
		this.getView().getDatePickerNewDateInscripciones().setEnabled(false);

		
		//if(eliminarAviso) this.getView().getLblError().setText("");
		setControls(false);
	}
	
	public void updateTables() {
		
		List <CursoDTO> listacursos = this.getModel().getCursos();
		
		this.getView().getTableInscripciones().setModel(
				SwingUtil.getTableModelFromPojos(
						listacursos,
						new String[] { "id", "nombre", "start_inscr", "end_inscr", "start", "end"},
						new String[] { "Id", "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso"},
						null
						)
				);
		//Ocultamos la columna 0 de la vista.
		this.getView().getTableInscripciones().removeColumn(this.getView().getTableInscripciones().getColumnModel().getColumn(0));
		
	}

}
