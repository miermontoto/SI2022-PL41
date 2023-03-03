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

	private RegistrarPagoView vista;
	private RegistrarPagoModel modelo;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		this.initView();
	}

	private String idInscripcion = null;
	private String idAlumno = null;

	public void initView() {
		getListaInscripciones(); // Precarga inicial de la lista de inscripciones
		setControls(false); // Inicio la vista con todo deshabilitado

		vista.getBtnInsertarPago().addActionListener(e -> handleInsertar());
		vista.getTableInscripciones().addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseReleased(MouseEvent evt) { handleSelect(); }
		});
		vista.getChkAll().addActionListener(e -> getListaInscripciones());
	}

	private void setControls(boolean status) {
		vista.getBtnInsertarPago().setEnabled(status);
		vista.getTxtImporte().setEnabled(status);
		vista.getDatePicker().setEnabled(status);
	}

	private void eraseControls(boolean eliminarAviso) {
		vista.getLblNombreInscripcion().setText("No se ha seleccionado ningún nombre");
		vista.getTxtImporte().setText("");
		vista.getDatePicker().setText("");
		if(eliminarAviso) vista.getLblError().setText("");
		setControls(false);
	}

	private void handleSelect() {
		eraseControls(true); // Borramos también el aviso de pago insertado con éxito/error
		int fila = vista.getTableInscripciones().getSelectedRow();
		if (fila == -1) return;

		TableModel tempModel = vista.getTableInscripciones().getModel();
		idInscripcion = (String) tempModel.getValueAt(fila, 0);
		idAlumno = (String) tempModel.getValueAt(fila, 1);
		vista.getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 2));

		setControls(true);
	}

	private void handleInsertar() {
		String nombreInscrito = vista.getLblNombreInscripcion().getText();
		String importe = vista.getTxtImporte().getText();

		if(nombreInscrito == "No se ha seleccionado ningún nombre" || importe == null || vista.getDatePicker().getDate() == null) {
			vista.getLblError().setText("Por favor, rellena todos los campos para continuar"); // Se muestra un error
			return;
		}

		String email = modelo.getEmailAlumno(idAlumno);
		Date fechaPago = java.sql.Date.valueOf(vista.getDatePicker().getDate());

		modelo.registrarPago(importe, Util.dateToIsoString(fechaPago), idInscripcion); // Registro en la BBDD el pago
		Util.sendEmail(email, "COIIPA: pago registrado", "Su pago ha sido registrado correctamente."); // Envío un email al alumno
		getListaInscripciones(); // Refrescamos la tabla al terminar de inscribir a la persona
		// Si había algún error habilitado en la etiqueta, se deshabilita y mostramos éxito
		vista.getLblError().setText("Pago insertado con éxito");
		SwingUtil.showMessage("Pago insertado con éxito", "Registro de pagos");
		eraseControls(false); // Entradas en blanco
	}

	public void getListaInscripciones() {
		JTable table = vista.getTableInscripciones();
		TableModel tmodel; // Modelo de la tabla a inicializar según checkbox
		List<PagoDTO> inscripciones;
		if(vista.getChkAll().isSelected()) {
			inscripciones = modelo.getListaInscripcionesCompleta(Util.isoStringToDate("2022-05-15"));
		} else {
			inscripciones = modelo.getListaInscripcionesSinPagar(Util.isoStringToDate("2022-05-15"));
		}

		tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "id", "alumno_id", "nombre", "fecha", "coste", "estado" }); //La primera columna estará oculta
		table.setModel(tmodel);

		// Ocultar foreign keys de la tabla
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.removeColumn(table.getColumnModel().getColumn(0));
		table.setDefaultEditor(Object.class, null); // Deshabilitar edición

		//SwingUtil.autoAdjustColumns(table); // Ajustamos las columnas
	}
}
