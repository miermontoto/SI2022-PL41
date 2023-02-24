package g41.SI2022.coiipa;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.JFrame;
import javax.swing.table.TableModel;

import giis.demo.tkrun.CarreraDisplayDTO;
import giis.demo.util.SwingUtil;
import giis.demo.util.Util;

public class RegistrarPagoController {

	RegistrarPago vista; //La vista de la pantalla principal
	RegistrarPagoWindow vista2; //Ventanuca que se abre al darle click sobre la tabla
	RegistrarPagoModel modelo; //El modelo de datos de la BBDD.
	
	public RegistrarPagoController(RegistrarPago vista, RegistrarPagoModel modelo) {
		this.vista = vista;
		this.modelo = modelo;
		vista2 = new RegistrarPagoWindow();
		inicializa();
	}
	
	public void inicializa() {
		
		//Vista pincipal
		
		vista.getBtnNewButton().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.getListaInscripciones()));

		vista.getTableInscripciones().addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				//vista2 = new RegistrarPagoWindow();
				vista2.getFrame().setVisible(true);
		    }
		    });
				
		vista2.getBotonaceptar().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.inscribir()));
	
	}
	
	public void inscribir() {
		//Obtengo el id que el usuario metió en su campo
		
		int idinscripcion= Integer.parseInt(vista2.getIdInscripcion());
		System.out.println(idinscripcion); //DEBUG
		modelo.insertNewPago(idinscripcion, 0, Util.isoStringToDate("2023-01-24"));
		vista2.getFrame().setVisible(false); //Ponemos el frame en modo "invisible"
		vista2.getFrame().dispose(); //Lo eliminamos de la vista.
	}
	
	public void getListaInscripciones() {
		
		//Obtengo la lista de insripciones
		List<RegistrarPagoDTO> inscripciones=modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel=SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "nombre", "coste", "estado"});
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
