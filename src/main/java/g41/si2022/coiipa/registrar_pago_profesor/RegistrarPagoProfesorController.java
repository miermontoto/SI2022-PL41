package g41.si2022.coiipa.registrar_pago_profesor;

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

public class RegistrarPagoProfesorController {

	private RegistrarPagoProfesorView view;
	private RegistrarPagoProfesorModel model;

	public RegistrarPagoProfesorController(RegistrarPagoProfesorView vista, RegistrarPagoProfesorModel modelo) {
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
		view.getLblNombreInscripcion().setText("No se ha seleccionado ninguna factura");
		view.getTxtImporte().setText("");
		view.getDatePicker().setText("");
		if(eliminarAviso) view.getLblError().setText("");
		setControls(false);
	}
}
