package g41.SI2022.coiipa;

import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.table.TableModel;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class RegistrarPagoController {

	RegistrarPago vista;
	RegistrarPagoModel modelo;
	
	public RegistrarPagoController(RegistrarPago vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		inicializa();
	}
	
	public void inicializa() {
		
		vista.getBtnNewButton().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.getListaInscripciones()));

	
	}
	
	public void getListaInscripciones() {
		
		//Obtengo la lista de insripciones
		List<insertapagoDTO> inscripciones=modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel=SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "coste", "estado"});
		vista.getTableInscripciones().setModel(tmodel); //Le pongo el modelo
		SwingUtil.autoAdjustColumns(vista.getTableInscripciones()); //Ajustamos las columnas
		
		//Como se guarda la clave del ultimo elemento seleccionado, restaura la seleccion de los detalles
		//this.restoreDetail();

		/*//A modo de demo, se muestra tambien la misma informacion en forma de lista en un combobox
		List<Object[]> carrerasList=model.getListaCarrerasArray(Util.isoStringToDate(view.getFechaHoy()));
		ComboBoxModel<Object> lmodel=SwingUtil.getComboModelFromList(carrerasList);
		view.getListaCarreras().setModel(lmodel);*/
	}
	
}
