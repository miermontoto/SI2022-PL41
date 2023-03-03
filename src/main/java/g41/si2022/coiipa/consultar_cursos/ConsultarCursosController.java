package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.util.SwingUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ConsultarCursosController {
	
	private ConsultarCursosModel model;
	private ConsultarCursosView view;
	private String idCurso;
	
	public ConsultarCursosController(ConsultarCursosModel m, ConsultarCursosView v)
	{
		this.model = m;
		this.view = v;
		initView();
	}
	
	public void initView() 	
	{
		this.getListaCursos();
		view.getTablaCursos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> getValueCurso());
			}		});
	}
	
	public void getListaCursos() {
		view.getTablaCursos().setModel(
				SwingUtil.getTableModelFromPojos(
						this.model.getListaCursos(),
						new String[] { "nombre", "estado", "plazas", "start", "end" } 
				)
		);
		 
		String[] columnNames = { "Nombre", "Estado", "Plazas", "Inicio", "Fin" };
		for(int i=0; i<columnNames.length; i++) {
			view.getTablaCursos().getColumnModel().getColumn(i).setHeaderValue(columnNames[i]);
		}
		
		SwingUtil.autoAdjustColumns(view.getTablaCursos());
	}

	public void getValueCurso() {
		
	}
	
	public void getListaInscripciones() {
		
		
	}
	

	
}
