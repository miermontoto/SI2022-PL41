package g41.si2022.coiipa.registrar_pago;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

import javax.swing.table.TableModel;

import g41.si2022.coiipa.dto.PagoDTO;
import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;

public class RegistrarPagoController {

	private RegistrarPagoView vista;
	private RegistrarPagoModel modelo;
	private RegistrarPagoController controlador;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		this.controlador = this;
		inicializa();
	}

	public void inicializa() {
		vista.getBtnNewButton().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.getListaInscripciones()));

		vista.getBotonpagar().addActionListener(new ActionListener() { //Botón añadir pago
			public void actionPerformed(ActionEvent e) {
				int idinscripcion = Integer.parseInt(vista.getIdinscripcion().getText());
				int importe = Integer.parseInt(vista.getInsertarimporte().getText());
				System.out.printf("Se han pagado %s € para el id %s", importe, idinscripcion);
				Date fechahoy = new Date();
				modelo.registrarPago(importe, Util.dateToIsoString(new Date()), idinscripcion);
				//modelo.actualizarInscripcion(idinscripcion);
				controlador.getListaInscripciones();
			}
		});
		//Acciones al pulsar la tabla

		vista.getTableInscripciones().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int fila = vista.getTableInscripciones().rowAtPoint(evt.getPoint());
		        int columna = vista.getTableInscripciones().columnAtPoint(evt.getPoint());
		        String idinscripcion = (String) vista.getTableInscripciones().getValueAt(fila, 0); //Obtengo los valores del ID de inscripción
		        vista.getIdinscripcion().setText(idinscripcion);//Seleccionamos la etiqueta y le añadimos el valor
		        //System.out.print(fila); //DEBUG

		    }
		});



	}
	public void getListaInscripciones() {

		// Obtengo la lista de insripciones
		List<PagoDTO> inscripciones=modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "coste", "estado"});
		vista.getTableInscripciones().setModel(tmodel); // Le pongo el modelo
		//SwingUtil.autoAdjustColumns(vista.getTableInscripciones()); // Ajustamos las columnas

		// Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
		//this.restoreDetail();

		/*
		// A modo de demo, se muestra tambien la misma informacion en forma de lista en un combobox
		List<Object[]> carrerasList = model.getListaCarrerasArray(Util.isoStringToDate(view.getFechaHoy()));
		ComboBoxModel<Object> lmodel = SwingUtil.getComboModelFromList(carrerasList);
		view.getListaCarreras().setModel(lmodel);
		*/
	}
}
