package g41.si2022.coiipa.registrar_pago;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
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


	int idinscripcionseleccionado = 0; //Cuando seleccionamos algo en la tabla, aquí se pone el ID de esa inscripción.
	int idalumnoseleccionado = 0; //Cuando seleccionamos algo en el alumno, aquí se pone el ID de ese alumno.


	public void inicializa() {

		//Precarga inicial de la lista de inscripciones

		this.getListaInscripciones();

		//Inicio la vista con todo deshabilitado

		vista.getBotonpagar().setEnabled(false);
        controlador.vista.getInsertarimporte().setEnabled(false);
        controlador.vista.getDatepicker().setEnabled(false);

		//vista.getBotoncarga().addActionListener(e -> SwingUtil.exceptionWrapper(() -> this.getListaInscripciones()));




		//Acción al darle al botón de pagar:
		vista.getBotonpagar().addActionListener(new ActionListener() { //Botón añadir pago
			public void actionPerformed(ActionEvent e) {

				String nombreinscrito = vista.getNombreinscripcion().getText();
				String importestring = vista.getInsertarimporte().getText();

				// Obtengo la fecha que nos han introducidp
				LocalDate fechapago = vista.getDatepicker().getDate();

				if(nombreinscrito != "N/A" && importestring != null && fechapago != null) {

					// Hacemos las conversiones a Date y int
					Date fechapagod = java.sql.Date.valueOf(fechapago);
					int importe = Integer.parseInt(importestring);

					// Imprimo info de deb
					System.out.printf("Se han pagado %s € para el alumno %s , en la inscripción: %d , con fecha %s \n", importe, nombreinscrito, idinscripcionseleccionado, fechapagod.toString()); //DEBUG

					// Registro en la BBDD el pago
					modelo.registrarPago(importe, Util.dateToIsoString(fechapagod) , controlador.idinscripcionseleccionado);

					//Envío un email al alumno
					
					enviaremail(idalumnoseleccionado, vista.getNombreinscripcion().getText());
					
					//Refresco la tabla de inscripciones
					controlador.getListaInscripciones(); //Refrescamos la tabla al terminar de inscribir a la persona
				} else System.err.printf("ERROR, has de rellenar todos los campos \n");
			}
		});
		
		// Acciones al pulsar la tabla

		vista.getTableInscripciones().addMouseListener(new java.awt.event.MouseAdapter() {
		    @Override
		    public void mouseClicked(java.awt.event.MouseEvent evt) {
		        int fila = vista.getTableInscripciones().rowAtPoint(evt.getPoint()); // Obtengo la fila donde se hizo click
		        //int columna = vista.getTableInscripciones().columnAtPoint(evt.getPoint());

		        // Obtengo los dos ID
		        idinscripcionseleccionado = (int) vista.getTableInscripciones().getModel().getValueAt(fila, 0); //Obtengo del modelo de la tabla el id
		        idalumnoseleccionado = (int) vista.getTableInscripciones().getModel().getValueAt(fila, 1);

		        // Obtengo el nombre del alumno y lo pongo en seleccionar.

		        String nombrealumno = (String) vista.getTableInscripciones().getModel().getValueAt(fila, 2);
		        controlador.vista.getNombreinscripcion().setText(nombrealumno);

		        // Habilito los campos para introducir texton y el botón del pago
		        controlador.vista.getInsertarimporte().setEnabled(true);
		        controlador.vista.getBotonpagar().setEnabled(true);
		        controlador.vista.getDatepicker().setEnabled(true);

		        //System.out.print(fila); //DEBUG
		        //System.out.printf("ID inscripción seleccionado %d \n", idinscripcionseleccionado); //DEBUG
		        //System.out.printf("Nombre del alumno seleccionado %s, su id es %d \n", nombrealumno, idalumnoseleccionado); //DEBUG
		    }
		});

	}
	
	public void enviaremail(int idalumno, String alumno){
		
		String carpetadetrabajo=System.getProperty("user.dir"); //Directorio principal del programa

		String target=carpetadetrabajo+"/target/"; //Calculamos el target de este proyecto
		
		 
	      FileWriter escribiremail;
		try {
			escribiremail = new FileWriter(target + "email.txt");
		    escribiremail.write("Hola, " + alumno + ", te escribimos para comunicarte que te has inscrito con éxito. \n");
		    escribiremail.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		 
	}

	public void getListaInscripciones() {
		// Obtengo la lista de insripciones
		List<PagoDTO> inscripciones=modelo.getListaInscripciones(Util.isoStringToDate("2022-05-15"));
		TableModel tmodel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] {"id", "alumno_id", "nombre", "fecha", "coste", "estado"}); //La primera columna estará oculta
		vista.getTableInscripciones().setModel(tmodel); // Le pongo el modelo
		vista.getTableInscripciones().removeColumn(vista.getTableInscripciones().getColumnModel().getColumn(0)); //Ocultamos la columna del id inscripcion
		vista.getTableInscripciones().removeColumn(vista.getTableInscripciones().getColumnModel().getColumn(0)); //Ocultamos la columna del id alumno
		vista.getTableInscripciones().setDefaultEditor(Object.class, null);

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
