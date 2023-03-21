package g41.si2022.coiipa.retrasar_fechas;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;

public class RetrasarFechasController extends g41.si2022.mvc.Controller<RetrasarFechasView, RetrasarFechasModel> {


	public RetrasarFechasController(RetrasarFechasView myTab, RetrasarFechasModel myModel) {
		super(myTab, myModel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initNonVolatileData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initVolatileData() {
		// TODO Auto-generated method stub
		this.updateTables();
		
	}
	
	
	public void updateTables() {
		
		List <CursoDTO> listacursos = this.getModel().getCursos();
		
		this.getView().getCursos().setModel(
				SwingUtil.getTableModelFromPojos(
						listacursos,
						new String[] { "nombre", "start_inscr", "end_inscr", "start", "end"},
						new String[] { "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso"},
						null
						)
				);
		
	}

}
