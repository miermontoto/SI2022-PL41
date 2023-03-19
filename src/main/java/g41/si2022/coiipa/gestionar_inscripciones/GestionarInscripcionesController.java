package g41.si2022.coiipa.gestionar_inscripciones;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.awt.event.MouseAdapter;

import g41.si2022.util.state.StateUtilities;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Dialog;
import g41.si2022.util.Util;
import g41.si2022.util.renderer.InscripcionStatusCellRenderer;
import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.InscripcionState;

public class GestionarInscripcionesController extends g41.si2022.mvc.Controller<GestionarInscripcionesView, GestionarInscripcionesModel> {

	private String idInscripcion = null;
	private String idAlumno = null;
	private String idCurso = null;
	private String nombreCompleto = null;
	private String nombreCurso = null;
	private List<InscripcionDTO> inscripciones;
	private double aDevolver;
	private LocalDate today;

	public GestionarInscripcionesController(GestionarInscripcionesModel modelo, GestionarInscripcionesView vista) {
		super(vista, modelo);
	}

	@Override
	public void initNonVolatileData() {
		this.getView().getBtnInsertarPago().addActionListener(e -> handlePagar());
		this.getView().getChkAll().addActionListener(e -> getListaInscripciones());
		this.getView().getBtnCancelarInscripcion().addActionListener(e -> handleDevolver());
	}

	@Override
	public void initVolatileData() {
		this.getListaInscripciones();
		setControls(false); // Inicio la vista con todo deshabilitado
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		today = getView().getMain().getToday();
	}

	private void setControls(boolean status) {
		this.getView().getBtnInsertarPago().setEnabled(status);
		this.getView().getTxtImporte().setEnabled(status);
		this.getView().getDatePicker().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {
		this.getView().getLblNombreSeleccionado().setText("");
		this.getView().getTxtImporte().setText("");
		this.getView().getDatePicker().setText("");
		setControls(false);
	}

	private void handleSelect() {
		eraseControls(true); // Borramos también el aviso de pago insertado con éxito/error
		int fila = this.getView().getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel model = this.getView().getTableInscripciones().getModel();
		idInscripcion = (String) model.getValueAt(fila, 0);
		idAlumno = (String) model.getValueAt(fila, 1);
		idCurso = (String) model.getValueAt(fila, 2);
		getView().getDatePicker().setDateToToday();
		nombreCompleto = (String) model.getValueAt(fila, 3) + " " + (String) model.getValueAt(fila, 4);
		nombreCurso = (String) model.getValueAt(fila, 5);
		this.getView().getLblNombreSeleccionado().setText(nombreCompleto);

		InscripcionState estado = inscripciones.get(fila).getEstado();
		setControls(estado != InscripcionState.PAGADA && estado != InscripcionState.CANCELADA);
		getView().getBtnCancelarInscripcion().setEnabled(estado != InscripcionState.CANCELADA);

		Double costeCurso = Double.valueOf((String) model.getValueAt(fila,  7));
		Double importePagado = Double.valueOf((String) model.getValueAt(fila, 8));
		Double calculoDevolver = Math.min(costeCurso, importePagado);

		// Calculamos el número de días que quedan
		Date fechaActual = Date.from(this.getView().getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fechaCurso = Util.isoStringToDate((String) model.getValueAt(fila, 6));
		long dias = ChronoUnit.DAYS.between(fechaActual.toInstant(), fechaCurso.toInstant());

		// Calculamos le importe que le será devuelto al usuario.
		aDevolver = 0;

		if(dias > 7) aDevolver = calculoDevolver;
		else if(dias >= 3 && dias <= 7) aDevolver = calculoDevolver / 2;
		else aDevolver = 0;

		// Si el usuario ha pagado más de lo que corresponde, se le devuelve el exceso
		// sobre el cálculo anterior.
		if(importePagado > costeCurso) aDevolver += importePagado - costeCurso;

		this.getView().getLblCalculoDevolucion().setText(aDevolver + "€");
	}

	private void handleDevolver() {
		CursoState estadoCurso = StateUtilities.getCursoState(idCurso, today);
		if (estadoCurso == CursoState.CERRADO || estadoCurso == CursoState.FINALIZADO || estadoCurso == CursoState.EN_CURSO) {
			Dialog.showError("No se puede cancelar la inscripción de un curso fuera del plazo de inscripción.");
			return;
		}

		this.getModel().registrarDevolucion(aDevolver, getView().getMain().getToday().toString(), idInscripcion);
		Util.sendEmail(getModel().getEmailAlumno(idAlumno), "COIIPA: Cancelación de inscripción", "Se ha cancelado su inscripción"
			+ " al curso \"" + nombreCurso + "\" con éxito. Se le devolverán " + aDevolver + "€.");
		Dialog.show("La cancelación de la inscripción del alumno " + nombreCompleto + " ha sido realizada con éxito. Se le han devuelto " + aDevolver + "€");
		getListaInscripciones();
		setControls(false);
	}

	private void handlePagar() {
		String importe = this.getView().getTxtImporte().getText();

		if(importe.length() == 0 || importe == null || this.getView().getDatePicker().getDate() == null) {
			Dialog.showError("Por favor, rellena todos los campos para continuar"); // Se muestra un error
			return;
		}

		Date fechaPago = java.sql.Date.valueOf(this.getView().getDatePicker().getDate());

		this.getModel().registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion);
		getListaInscripciones(); // Refrescar tabla al insertar el pago
		Dialog.show("Pago por importe de " + importe + " € de parte del alumno " + nombreCompleto + " insertado con éxito");

		InscripcionState estado = StateUtilities.getInscripcionState(idInscripcion); // Estado de la inscr. post inserción
		if(estado == InscripcionState.PAGADA) { // Si pagada, enviar email de plaza cerrada al alumno
			Util.sendEmail(getModel().getEmailAlumno(idAlumno), "COIIPA: inscripción completada",
				"El pago de su inscripción ha sido registrado correctamente y su inscripción ha sido completada.");
		} else Dialog.showWarning("El importe total es incorrecto y la inscripción no está comlpeta.");

		eraseControls(false);
	}

	public void getListaInscripciones() {
		JTable table = this.getView().getTableInscripciones();

		String date = this.getView().getMain().getToday().toString();
		inscripciones = this.getModel().getInscripciones(date);

		new java.util.ArrayList<InscripcionDTO>(inscripciones).forEach(x -> {
			x.setEstado(g41.si2022.util.state.StateUtilities.getInscripcionState(Double.parseDouble(x.getCurso_coste()), Double.parseDouble(x.getPagado())));
			if(this.getModel().isCancelled(x.getId()))
				x.setEstado(InscripcionState.CANCELADA);
			if (!this.getView().getChkAll().isSelected() && x.getEstado() != InscripcionState.PENDIENTE && x.getEstado() != InscripcionState.EXCESO) {
				inscripciones.remove(x);
			}
		});

		table.setModel(SwingUtil.getTableModelFromPojos(
			inscripciones,
			new String[] { "id", "alumno_id", "curso_id", "alumno_nombre", "alumno_apellidos", "curso_nombre", "fecha", "curso_coste","pagado", "estado" },	//La primera columna estará oculta
			new String[] { "", "", "", "Nombre", "Apellidos", "Curso", "Fecha", "Coste", "Importe pagado", "Estado" },
			null
		));

		// Ocultar foreign keys de la tabla
		for(int i=0;i<3;i++) table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición
		table.getColumnModel().getColumn(6).setCellRenderer(new InscripcionStatusCellRenderer(9));

		SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
