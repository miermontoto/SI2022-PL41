package g41.si2022.coiipa.registrar_pago_alumno;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.util.state.StateUtilities;
import g41.si2022.dto.InscripcionDTO;
import g41.si2022.ui.SwingUtil;
import g41.si2022.util.Dialog;
import g41.si2022.util.Util;
import g41.si2022.util.state.InscripcionState;

public class RegistrarPagoAlumnoController extends g41.si2022.mvc.Controller<RegistrarPagoAlumnoView, RegistrarPagoAlumnoModel> {

	private String idInscripcion = null;
	private String idAlumno = null;
	//private String idCurso = null;
	private List<InscripcionDTO> inscripciones;

	public RegistrarPagoAlumnoController(RegistrarPagoAlumnoModel modelo, RegistrarPagoAlumnoView vista) {
		super(vista, modelo);
	}

	@Override
	public void initNonVolatileData() {
		this.getView().getBtnInsertarPago().addActionListener(e -> handleInsertar());
		this.getView().getChkAll().addActionListener(e -> getListaInscripciones());
	}

	@Override
	public void initVolatileData() {
		this.getListaInscripciones();
		setControls(false); // Inicio la vista con todo deshabilitado
		this.getView().getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
	}

	private void setControls(boolean status) {
		this.getView().getBtnInsertarPago().setEnabled(status);
		this.getView().getTxtImporte().setEnabled(status);
		this.getView().getDatePicker().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {
		this.getView().getLblNombreSeleccionado().setText("No se ha seleccionado ningún nombre");
		this.getView().getTxtImporte().setText("");
		this.getView().getDatePicker().setText("");
		if(eliminarAviso) this.getView().getLblError().setText("");
		setControls(false);
	}

	private void handleSelect() {
		eraseControls(true); // Borramos también el aviso de pago insertado con éxito/error
		int fila = this.getView().getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel model = this.getView().getTableInscripciones().getModel();
		idInscripcion = (String) model.getValueAt(fila, 0);
		idAlumno = (String) model.getValueAt(fila, 1);
		getView().getDatePicker().setDateToToday();
		this.getView().getLblNombreSeleccionado().setText((String) model.getValueAt(fila, 3)
			+ " " + (String) model.getValueAt(fila, 4));

		InscripcionState estado = inscripciones.get(fila).getEstado();
		setControls(estado != InscripcionState.PAGADA && estado != InscripcionState.CANCELADA);
	}

	private void handleInsertar() {
		String nombreInscrito = this.getView().getLblNombreSeleccionado().getText();
		String importe = this.getView().getTxtImporte().getText();

		if(importe.length() == 0 || importe == null || this.getView().getDatePicker().getDate() == null) {
			Dialog.showError("Por favor, rellena todos los campos para continuar"); // Se muestra un error
			return;
		}

		Date fechaPago = java.sql.Date.valueOf(this.getView().getDatePicker().getDate());

		this.getModel().registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion);
		getListaInscripciones(); // Refrescar tabla al insertar el pago
		Dialog.show("Pago por importe de " + importe + " € de parte del alumno " + nombreInscrito + " insertado con éxito");

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
			if(this.getModel().isCancelled(idInscripcion))
				x.setEstado(InscripcionState.CANCELADA);
			if (!this.getView().getChkAll().isSelected() && x.getEstado() != InscripcionState.PENDIENTE && x.getEstado() != InscripcionState.EXCESO) {
				inscripciones.remove(x);
			}
		});
//alumno_id
		table.setModel(SwingUtil.getTableModelFromPojos(
			inscripciones,
			new String[] { "id", "alumno_id", "curso_id", "alumno_nombre", "alumno_apellidos", "curso_nombre", "fecha", "curso_coste","pagado", "estado" },	//La primera columna estará oculta
			new String[] { "ID", "ID Alumno", "ID Curso", "Nombre Alumno", "Apellidos del alumno", "Nombre Curso", "Fecha", "Coste", "Pagado", "Estado" },
			null
		));

		// Ocultar foreign keys de la tabla
		for(int i=0;i<3;i++) table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		//SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
