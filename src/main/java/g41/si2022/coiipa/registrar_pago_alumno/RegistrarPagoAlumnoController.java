package g41.si2022.coiipa.registrar_pago_alumno;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.util.InscripcionState;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;

public class RegistrarPagoAlumnoController {

	private RegistrarPagoAlumnoView view;
	private RegistrarPagoAlumnoModel model;

	public RegistrarPagoAlumnoController(RegistrarPagoAlumnoView vista, RegistrarPagoAlumnoModel modelo) {
		this.view = vista;
		this.model = modelo;
		this.initView();
	}

	private String idInscripcion = null;
	private String idAlumno = null;
	private String idCurso = null;

	public void initView() {
		getListaInscripciones(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado

		view.getBtnInsertarPago().addActionListener(e -> handleInsertar());
		view.getTableInscripciones().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		view.getChkAll().addActionListener(e -> getListaInscripciones());
	}

	private void setControls(boolean status) {
		view.getBtnInsertarPago().setEnabled(status);
		view.getTxtImporte().setEnabled(status);
		view.getDatePicker().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {
		view.getLblNombreInscripcion().setText("No se ha seleccionado ningún nombre");
		view.getTxtImporte().setText("");
		view.getDatePicker().setText("");
		if(eliminarAviso) view.getLblError().setText("");
		setControls(false);
	}

	private void handleSelect() {
		eraseControls(true); // Borramos también el aviso de pago insertado con éxito/error
		int fila = view.getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel tempModel = view.getTableInscripciones().getModel();
		idInscripcion = (String) tempModel.getValueAt(fila, 0);
		idAlumno = (String) tempModel.getValueAt(fila, 1);
		idCurso = (String) tempModel.getValueAt(fila, 2);
		view.getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 3));

		setControls(true);
	}

	private void handleInsertar() {
		String nombreInscrito = view.getLblNombreInscripcion().getText();
		String importe = view.getTxtImporte().getText();

		if(nombreInscrito == "No se ha seleccionado ningún nombre" || importe == null || view.getDatePicker().getDate() == null) {
			view.getLblError().setText("Por favor, rellena todos los campos para continuar"); // Se muestra un error
			return;
		}

		String email = model.getEmailAlumno(idAlumno);
		Date fechaPago = java.sql.Date.valueOf(view.getDatePicker().getDate());

		model.registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion); // Registro en la BBDD el pago
		Util.sendEmail(email, "COIIPA: pago registrado", "Su pago ha sido registrado correctamente."); // Envío un email al alumno
		getListaInscripciones(); // Refrescamos la tabla al terminar de inscribir a la persona
		// Si había algún error habilitado en la etiqueta, se deshabilita y mostramos éxito
		//view.getLblError().setText("Pago insertado con éxito");
		SwingUtil.showMessage("Pago por importe de " + importe + " € de parte del alumno " + nombreInscrito + " insertado con éxito", "Registro de pagos");
		eraseControls(false); // Entradas en blanco
	}

	public void getListaInscripciones() {
		JTable table = view.getTableInscripciones();
		List<InscripcionDTO> inscripciones;

		String date = view.getMain().getToday().toString();
		inscripciones = model.getInscripciones(date);

		new java.util.ArrayList<InscripcionDTO>(inscripciones).forEach(x -> {
			x.setEstado(g41.si2022.util.StateUtilities.getInscripcionState(Double.parseDouble(x.getCurso_coste()), model.getPagos(x.getInscripcion_alumno_id(), x.getInscripcion_curso_id())));
			if(model.isCancelled(idInscripcion))
				x.setEstado(InscripcionState.CANCELADA);
			
			if (!view.getChkAll().isSelected() && (x.getEstado() == InscripcionState.PENDIENTE || x.getEstado() ==InscripcionState.CANCELADA)) {
				inscripciones.remove(x);
			}
		});

		table.setModel(SwingUtil.getTableModelFromPojos(
				inscripciones,
				new String[] { "inscripcion_id", "inscripcion_alumno_id", "inscripcion_curso_id", "alumno_nombre", "curso_nombre", "inscripcion_fecha", "curso_coste","inscripcion_pagado", "estado" },	//La primera columna estará oculta
				new String[] { "ID", "ID Alumno", "ID Curso", "Nombre Alumno", "Nombre Curso", "Fecha", "Coste", "Pagado", "Estado" },
				null
				));

		// Ocultar foreign keys de la tabla
		for(int i=0;i<3;i++) table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		//SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
