package g41.si2022.coiipa.gestionar_curso;

import java.util.List;

import g41.si2022.dto.CursoDTO;
import g41.si2022.ui.SwingUtil;

public class GestionarCursoController extends g41.si2022.mvc.Controller<GestionarCursoView, GestionarCursoModel> {


	public GestionarCursoController(GestionarCursoView myTab, GestionarCursoModel myModel) {
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
		
		this.getView().getTableInscripciones().setModel(
				SwingUtil.getTableModelFromPojos(
						listacursos,
						new String[] { "nombre", "start_inscr", "end_inscr", "start", "end"},
						new String[] { "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso"},
						null
						)
				);
		
	}

}