package g41.si2022.coiipa.registrar_pago;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;
import g41.si2022.util.ApplicationException;

public class RegistrarPagoController {

	private RegistrarPagoView vista;
	private RegistrarPagoModel modelo;
	private RegistrarPagoController controlador;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		this.controlador = this;
		this.initView();
	}

	private int idInscripcion = -1;
	private int idAlumno = -1;

	public void initView() {
		// Precarga inicial de la lista de inscripciones
		this.getListaInscripciones();

		// Inicio la vista con todo deshabilitado
		vista.getBtnInsertarPago().setEnabled(false);
        controlador.vista.getTxtImporte().setEnabled(false);
        controlador.vista.getDatepicker().setEnabled(false);

		vista.getBtnInsertarPago().addActionListener(e -> handleInsertar());

		vista.getTableInscripciones().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseReleased(java.awt.event.MouseEvent evt) { handleSelect(); }
		});
	}

	private void handleSelect() {
		int fila = vista.getTableInscripciones().getSelectedRow();
		System.out.println(fila);

		TableModel tempModel = vista.getTableInscripciones().getModel();
		idInscripcion = (int) tempModel.getValueAt(fila, 0);
		idAlumno = (int) tempModel.getValueAt(fila, 1);
		controlador.vista.getLblNombreInscripcion().setText((String) tempModel.getValueAt(fila, 2));

		controlador.vista.getTxtImporte().setEnabled(true);
		controlador.vista.getBtnInsertarPago().setEnabled(true);
		controlador.vista.getDatepicker().setEnabled(true);
	}

	private void handleInsertar() {
		String nombreInscrito = vista.getLblNombreInscripcion().getText();
		String importeString = vista.getTxtImporte().getText();

		if(nombreInscrito != "N/A" && importeString != null && vista.getDatepicker().getDate() != null) {

			// Hacemos las conversiones a Date y int
			Date fechaPago = java.sql.Date.valueOf(vista.getDatepicker().getDate());
			int importe = Integer.parseInt(importeString);

			// Imprimo info de deb
			System.out.printf("Se han pagado %s € para el alumno %s , en la inscripción: %d , con fecha %s \n", importe, nombreInscrito, idInscripcion, fechaPago.toString()); //DEBUG

			// Registro en la BBDD el pago
			modelo.registrarPago(importe, Util.dateToIsoString(fechaPago) , controlador.idInscripcion);

			// Envío un email al alumno
			enviarEmail(idAlumno, vista.getLblNombreInscripcion().getText());

			// Refresco la tabla de inscripciones
			controlador.getListaInscripciones(); // Refrescamos la tabla al terminar de inscribir a la persona
		} else System.err.printf("ERROR, has de rellenar todos los campos \n");
	}

	public void enviarEmail(int idalumno, String alumno){
		String directory = System.getProperty("user.dir"); // Directorio principal del programa
		String target = directory + "/target/"; // Calculamos el target de este proyecto

		try (FileWriter fw = new FileWriter(target + "email.txt")) {
		    fw.write("Hola, " + alumno + ", te escribimos para comunicarte que te has inscrito con éxito. \n");
		    fw.close();
		} catch (IOException e) { throw new ApplicationException(e); }
	}

	public void getListaInscripciones() {
		// Obtengo la lista de insripciones
		List<PagoDTO> inscripciones = modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "alumno_id", "nombre", "fecha", "coste", "estado"}); //La primera columna estará oculta
		vista.getTableInscripciones().setModel(tmodel); // Le pongo el modelo
		vista.getTableInscripciones().removeColumn(vista.getTableInscripciones().getColumnModel().getColumn(0)); //Ocultamos la columna del id inscripcion
		vista.getTableInscripciones().removeColumn(vista.getTableInscripciones().getColumnModel().getColumn(0)); //Ocultamos la columna del id alumno
		vista.getTableInscripciones().setDefaultEditor(Object.class, null);

		SwingUtil.autoAdjustColumns(vista.getTableInscripciones()); // Ajustamos las columnas
	}
}
