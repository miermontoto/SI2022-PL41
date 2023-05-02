
package g41.si2022.coiipa.gestionar_inscripciones;

import java.util.ArrayList;
import java.util.Date;
import java.util.List; // <- cabronazo
import java.util.Optional;

import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.awt.event.MouseAdapter;

import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.ui.util.Dialog;
import g41.si2022.util.StatusCellRenderer;
import g41.si2022.util.Util;
import g41.si2022.util.state.CursoState;
import g41.si2022.util.state.InscripcionState;
import g41.si2022.util.state.StateUtilities;

public class GestionarInscripcionesController extends g41.si2022.mvc.Controller<GestionarInscripcionesView, GestionarInscripcionesModel> {

	private String idInscripcion = null;
	private String idAlumno = null;
	private String idCurso = null;
	private String nombreCompleto = null;
	private String nombreCurso = null;
	private List<InscripcionDTO> inscripciones;
	private double aDevolver;
	private LocalDate today;
	private List<Integer> avisadas;

	// optimization
	private TableModel[] modelStorage = new TableModel[2];

	public GestionarInscripcionesController(GestionarInscripcionesModel modelo, GestionarInscripcionesView vista) {
		super(vista, modelo);
		avisadas = new ArrayList<>();
	}

	@Override
	public void initNonVolatileData() {
		getView().getBtnInsertarPago().addActionListener(e -> handlePagar());
		getView().getChkAll().addActionListener(e -> getListaInscripciones(true));
		getView().getBtnCancelarInscripcion().addActionListener(e -> handleDevolver());
		getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		getView().getBtnAvisar().addActionListener(e -> handleAvisar());
	}


	@Override
	public void initVolatileData() {
		today = getView().getMain().getToday();
		modelStorage = new TableModel[2];
		getListaInscripciones(false);
		setControls(false); // Inicio la vista con todo deshabilitado
	}

	private void setControls(boolean status) {
		getView().getBtnInsertarPago().setEnabled(status);
		getView().getTxtImporte().setEnabled(status);
		getView().getDatePicker().setEnabled(status);
	}

	private void eraseControls() {
		getView().getBtnAvisar().setEnabled(false);
		getView().getTxtImporte().setText("");
		getView().getDatePicker().setText("");
		setControls(false);
	}

	private void handleAvisar() {
		int id = Integer.parseInt(idInscripcion);
		if(avisadas.contains(id)) {
			if (!Dialog.confirm("Ya se ha avisado previamente a este alumno.\n¿Desea volver a avisar el retraso?")) {
				return;
			}
			avisadas.remove((Object) id);
		}

		avisadas.add(id);
		Util.sendEmail(getModel().getEmailAlumno(idAlumno), "COIIPA: Recordatorio de pago", "Estimado/a " + nombreCompleto + ",\n\n" +
			"Le recordamos que tiene una inscripción pendiente de pago para el curso " + nombreCurso + ".\n" +
			"Mientras no se realice el pago, su plaza no estará fijada.\n\n" +
			"Atentamente,\n" +
			"Equipo de gestión del COIIPA"
		);
		Dialog.show("Aviso por retraso enviado.");
	}

	private void handleSelect() {
		eraseControls(); // Borramos también el aviso de pago insertado con éxito/error
		JTable table = getView().getTableInscripciones();
		int fila = table.getSelectedRow();
		if (fila == -1) return;
		fila = table.convertRowIndexToModel(fila);

		TableModel model = this.getView().getTableInscripciones().getModel();
		idInscripcion = model.getValueAt(fila, 0).toString();
		idAlumno = model.getValueAt(fila, 1).toString();
		idCurso = model.getValueAt(fila, 2).toString();
		nombreCompleto = model.getValueAt(fila, 3).toString() + " " + model.getValueAt(fila, 4).toString();
		nombreCurso = (String) model.getValueAt(fila, 5);

		getView().getDatePicker().setDate(today);

		InscripcionState estado = inscripciones.get(fila).getEstado();
		setControls(estado != InscripcionState.PAGADA && estado != InscripcionState.CANCELADA);
		getView().getBtnCancelarInscripcion().setEnabled(estado != InscripcionState.CANCELADA);
		getView().getBtnAvisar().setEnabled(estado == InscripcionState.RETRASADA || estado == InscripcionState.RETRASADA_EXCESO);

		Double costeCurso = Double.valueOf(model.getValueAt(fila,  7).toString());
		Double importePagado = Double.valueOf(model.getValueAt(fila, 8).toString());
		Double calculoDevolver = Math.min(costeCurso, importePagado);
		getView().getLblInfoDiferencia().setText(String.valueOf(importePagado - costeCurso) + "€");

		// Calculamos el número de días que quedan
		Date fechaActual = Date.from(getView().getMain().getToday().atStartOfDay(ZoneId.systemDefault()).toInstant());
		Date fechaInscr = Util.isoStringToDate(model.getValueAt(fila, 6).toString());
		Date fechaCurso = Util.isoStringToDate(getModel().getFechaCurso(idCurso));

		long dias = ChronoUnit.DAYS.between(fechaActual.toInstant(), fechaCurso.toInstant());

		String diasDesdeInscr = String.valueOf(ChronoUnit.DAYS.between(fechaInscr.toInstant(), fechaActual.toInstant()));
		String diasHastaCurso = String.valueOf(dias);
		getView().getLblInfoDias().setText(diasDesdeInscr + " | " + diasHastaCurso);

		aDevolver = 0; // Calculamos le importe que le será devuelto al usuario.

		if(dias > 7) aDevolver = calculoDevolver;
		else if(dias >= 3 && dias <= 7) aDevolver = calculoDevolver / 2;
		else aDevolver = 0;

		// Si el usuario ha pagado más de lo que corresponde, se le devuelve el exceso
		// sobre el cálculo anterior.
		if(importePagado > costeCurso) aDevolver += importePagado - costeCurso;

		this.getView().getLblDevolverCalculo().setText(aDevolver + "€");
	}

	private void handleDevolver() {
		CursoState estadoCurso = StateUtilities.getCursoState(idCurso, today);

		if (estadoCurso == CursoState.CERRADO || estadoCurso == CursoState.FINALIZADO || estadoCurso == CursoState.EN_CURSO) {
			Dialog.showError("No se puede cancelar la inscripción de un curso fuera del plazo de inscripción.");
			return;
		}

		getModel().cancelarInscripcion(idInscripcion);
		Util.sendEmail(getModel().getEmailAlumno(idAlumno), "COIIPA: Cancelación de inscripción", "Se ha cancelado su inscripción"
			+ " al curso \"" + nombreCurso + "\" con éxito. Se le devolverán " + aDevolver + "€ en los próximos días.");
		Dialog.show("La cancelación de la inscripción del alumno " + nombreCompleto + " ha sido realizada con éxito. Se le han de devolver " + aDevolver + "€");

		getListaInscripciones(false);
		setControls(false);
	}

	private void handlePagar() {
		String importe = this.getView().getTxtImporte().getText();
		LocalDate fecha = this.getView().getDatePicker().getDate();

		if (importe.length() == 0) {
			Dialog.showError("Por favor, rellena todos los campos para continuar");
			return;
		}

		int ret = Dialog.choices("Tipo de Pago", new String[]{"Pago por transferencia", "Pago por caja"});

		this.getModel().registrarPago(importe, fecha.toString(), idInscripcion);
		Dialog.show("Pago por importe de " + importe + " € de parte del alumno " + nombreCompleto + " insertado con éxito");

		/*
		 * Si se obtiene el estado de la inscripción recién actualizada mediante la función
		 * {@code StateUtilities.getInscripcionState}, se obtiene el estado sin actualizar,
		 * previo al registro del pago, por lo que hay que utilizar otro método. Utilizando
		 * un filter, se itera por toda la lista de inscripciones, por lo que puede que el
		 * rendimiento no sea óptimo.
		 */
		Optional<InscripcionDTO> opt = inscripciones.stream().filter(x -> x.getId().equals(idInscripcion)).findFirst();
		if (!opt.isPresent()) {
			Dialog.showError("Algo ha ido mal al actualizar el estado de la inscripción.\nCompruebe el estado de los datos.");
			return;
		}
		InscripcionState estado = opt.get().updateEstado(today);

		if (estado == InscripcionState.PAGADA) { // Si pagada, enviar email de plaza cerrada al alumno
			Util.sendEmail(getModel().getEmailAlumno(idAlumno), "COIIPA: inscripción completada",
				"El pago de su inscripción ha sido registrado correctamente y su inscripción ha sido completada.");
		} else Dialog.showWarning("El importe total es incorrecto y la inscripción no está comlpeta.");

		if (ret == 1) {
			Util.printReceipt(idAlumno, nombreCompleto, nombreCurso, importe, fecha.toString());
			Dialog.show("Se ha almacenado el correspondiente recibo");
		}

		getListaInscripciones(false); // Refrescar tabla al insertar el pago
		eraseControls();
	}

	public void getListaInscripciones(boolean fromCheckBox) {
		JTable table = this.getView().getTableInscripciones();
		boolean isChecked = getView().getChkAll().isSelected();

		/*
		 * Comprobación que optimiza la función. Si no se ha
		 * salido de la pantalla o modificado datos, se guarda
		 * la información de ambas versiones de la tabla
		 * (todas las inscripciones y solo pendientes) en un
		 * array de modelos de tabla. Esto mejora el rendimiento
		 * del programa al cambiar entre ambas tablas seguido.
		 */
		if (fromCheckBox && modelStorage[0] != null && modelStorage[1] != null) {
			table.setModel(modelStorage[isChecked ? 1 : 0]);
		} else {
			inscripciones = this.getModel().getInscripciones(isChecked, today);

			table.setModel(SwingUtil.getTableModelFromPojos(
				inscripciones,
				new String[] { "id", "alumno_id", "curso_id", "alumno_nombre", "alumno_apellidos", "curso_nombre", "fecha", "curso_coste", "pagado", "estado" }, // La primera columna estará oculta
				new String[] { "", "", "", "Nombre", "Apellidos", "Curso", "Fecha", "Coste", "Importe pagado", "Estado" },
				null
			));

			modelStorage[isChecked ? 1 : 0] = table.getModel();
		}

		// Ocultar foreign keys de la tabla
		for(int i = 0; i < 3; i++) table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		List<RowSorter.SortKey> sortKeys = new ArrayList<>();
		sortKeys.add(new RowSorter.SortKey(5, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(9, SortOrder.ASCENDING));
		sortKeys.add(new RowSorter.SortKey(6, SortOrder.ASCENDING));
		sorter.setSortKeys(sortKeys);
		table.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer(9));

		SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
