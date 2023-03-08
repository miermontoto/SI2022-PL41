package g41.si2022.coiipa.insertar_devolucion;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.CancelacionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Util;

public class InsertarDevolucionController extends g41.si2022.ui.Controller<InsertarDevolucionView, InsertarDevolucionModel> {

	private int idInscripcion = -1;
	private int idAlumno = -1;

	public InsertarDevolucionController(InsertarDevolucionModel modelo, InsertarDevolucionView vista) {
		super(vista, modelo);
	}

	@Override
	protected void initNonVolatileData () {
		this.getView().getBtnCancelarInscripcion().addActionListener(e -> handleDevolver());
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
	}
	
	@Override
	protected void initVolatileData () {
		getListaInscripciones(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado
	}

	private void setControls(boolean status) { 
		this.getView().getBtnCancelarInscripcion().setEnabled(status); 
	}

	private void eraseControls(boolean eliminaraviso) { 
		this.getView().getLblNombreInscripcion().setText("No se ha seleccionado ningún nombre");
		this.getView().getLblImporteDevuelto().setText("");
		if(eliminaraviso) this.getView().getLblError().setText(""); 
	}

	private void handleSelect() {

		eraseControls(true); //Borramos también el aviso de pago insertado con éxito/error
		int fila = this.getView().getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel tempModel = this.getView().getTableInscripciones().getModel();
		idInscripcion = (int) tempModel.getValueAt(fila, 0);
		idAlumno = (int) tempModel.getValueAt(fila, 1);
		this.getView().getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 2));

		Double costeCurso = Double.valueOf((String) tempModel.getValueAt(fila,  3));

		Date fechaActual = Date.from(this.getView().getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fechaCurso = Util.isoStringToDate((String) tempModel.getValueAt(fila, 5));

		//Calculamos el número de días que quedan

		long dias = ChronoUnit.DAYS.between(fechaActual.toInstant(), fechaCurso.toInstant());

		//Calculamos le importe que le será devuelto al usuario.
		double importeDevuelto = 0;

		if(dias > 7) importeDevuelto = costeCurso;
		if(dias > 3 && dias < 7) importeDevuelto = costeCurso / 2;
		if(dias < 3) importeDevuelto = 0;

		//System.out.printf("Coste %f, %f \n", costeCurso, importeDevuelto);
		this.getView().getLblImporteDevuelto().setText(Double.toString(importeDevuelto));

		setControls(true);
	}

	private void handleDevolver() { 
		if(this.getView().getLblImporteDevuelto().getText() != null && this.getView().getLblImporteDevuelto().getText() != "") { 
			//Obtenemos el nombre del inscrito 
			String nombreInscrito = this.getView().getLblNombreInscripcion().getText(); 
			Date fechaActual = Date.from(this.getView().getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant()); 
			double importedevuelto = 0.0; 
			importedevuelto = Double.parseDouble(this.getView().getLblImporteDevuelto().getText()); 
			this.getModel().registrarDevolucion(importedevuelto, Util.dateToIsoString(fechaActual), idInscripcion); 
			enviarEmail(idAlumno, nombreInscrito); 
			SwingUtil.showMessage("La cancelación de la inscripción del alumno " + nombreInscrito + " ha sido realizada con éxito. Se le han devuelto " + importedevuelto + " €", "Servicio de cancelaciones"); 
			getListaInscripciones(); 
			setControls(false); 
		} 
	}

		public void enviarEmail(int idAlumno, String alumno){ 
			String email = this.getModel().getEmailAlumno(Integer.toString(idAlumno));
			Util.sendEmail(email, "Cancelado con éxito", "Hola, hemos realizado la cancelación de tu curso con éxito"); 
		}

		public void getListaInscripciones() {

			this.eraseControls(true);
			// Obtengo la tabla de inscripciones
			JTable table = this.getView().getTableInscripciones();
			Date fechaActual = Date.from(this.getView().getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());

			List<CancelacionDTO> inscripcionesAll = this.getModel().getListaInscripciones(fechaActual);
			//tmodel = SwingUtil.getTableModelFromPojos(inscripcionesAll, new String[] { "id", "alumno_id", "nombre", "coste", "nombre_curso", "inicio_curso" }); //La primera columna estará oculta
			table.setModel(SwingUtil.getTableModelFromPojos(
					inscripcionesAll,
					new String[] { "id", "alumno_id", "nombre", "coste", "nombre_curso", "inicio_curso"},	//La primera columna estará oculta
					new String[] { "ID", "ID Alumno", "Nombre curso", "Coste", "Nombre curso", "Fecha de inicio"},
					null
					));

			// Ocultar foreign keys de la tabla
			for(int i=0; i<2; i++) {
				table.removeColumn(table.getColumnModel().getColumn(0));

			}
			table.setDefaultEditor(Object.class, null); // Deshabilitar edición

			SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
		}
	}
