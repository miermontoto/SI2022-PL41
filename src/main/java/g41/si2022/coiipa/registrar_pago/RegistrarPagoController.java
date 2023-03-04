package g41.si2022.coiipa.registrar_pago;

import java.util.Date;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.TableModel;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;

public class RegistrarPagoController {

	private RegistrarPagoView view;
	private RegistrarPagoModel model;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.view = vista;
		this.model = modelo;
		this.initView();
	}

	private String idInscripcion = null;
	private String idAlumno = null;

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
		view.getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 2));

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
		view.getLblError().setText("Pago insertado con éxito");
		SwingUtil.showMessage("Pago insertado con éxito", "Registro de pagos");
		eraseControls(false); // Entradas en blanco
	}

	public void getListaInscripciones() {
		JTable table = view.getTableInscripciones();
		TableModel tmodel; // Modelo de la tabla a inicializar según checkbox
		List<PagoDTO> inscripciones;

		String date = view.getMain().getToday().toString();
		inscripciones = view.getChkAll().isSelected() ? model.getInscripciones(date) : model.getInscripcionesPendientes(date);

		tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "id", "alumno_id", "nombre", "fecha", "coste", "estado" }); //La primera columna estará oculta
		table.setModel(tmodel);

		// Ocultar foreign keys de la tabla
		for(int i=0;i<2;i++) table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		//SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
