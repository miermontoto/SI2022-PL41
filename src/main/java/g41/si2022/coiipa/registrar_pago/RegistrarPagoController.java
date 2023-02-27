package g41.si2022.coiipa.registrar_pago;

import java.util.List;

import javax.swing.table.TableModel;

import g41.si2022.util.SwingUtil;
import g41.si2022.util.Util;

public class RegistrarPagoController {

	private RegistrarPagoView vista;
	private RegistrarPagoModel modelo;

	public RegistrarPagoController(RegistrarPagoView vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		inicializa();
	}

	public void inicializa() {
		vista.getBtnNewButton().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.getListaInscripciones()));
		
		vista.getTableInscripciones().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int fila = vista.getTableInscripciones().rowAtPoint(evt.getPoint());
		        int columna = vista.getTableInscripciones().columnAtPoint(evt.getPoint());
		        System.out.print(fila);
		        
		    }
		});

	}
	public void getListaInscripciones() {

		// Obtengo la lista de insripciones
		List<InsertarPagoDTO> inscripciones=modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "coste", "estado"});
		vista.getTableInscripciones().setModel(tmodel); // Le pongo el modelo
		SwingUtil.autoAdjustColumns(vista.getTableInscripciones()); // Ajustamos las columnas

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
