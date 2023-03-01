package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.util.SwingUtil;

public class ConsultarCursosController {
	
	private ConsultarCursosModel model;
	private ConsultarCursosView view;
	
	public ConsultarCursosController(ConsultarCursosModel m, ConsultarCursosView v)
	{
		this.model = m;
		this.view = v;
		initView();
	}
	
	public void initView() 	
	{
		this.getListaCursos();
	}
	
	public void getListaCursos() {
		view.getTablaCursos().setModel(
				SwingUtil.getTableModelFromPojos(
						this.model.getListaCursos(),
						new String[] { "Nombre", "Estado", "Plazas", "Inicio", "Fin" } 
				)
		);
		SwingUtil.autoAdjustColumns(view.getTablaCursos());
	}
	
}
