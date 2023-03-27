package g41.si2022.coiipa.gestionar_curso;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;

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
		this.initThings();
		this.updateTables();
		
	}
	
	int idCurso = 0;
	
	public void initThings() {
		
		this.getView().getTableInscripciones().addMouseListener( new MouseAdapter()
		{
		    public void mousePressed(MouseEvent e)
		    {
		        JTable source = (JTable)e.getSource();
		        int row = source.rowAtPoint( e.getPoint() );
		        int column = source.columnAtPoint( e.getPoint() );

		        if (! source.isRowSelected(row))
		        	idCurso = (int) source.getValueAt(row, 0);
		        
		        System.out.println(idCurso);
		        }
		});
		
	}
	
	public void updateTables() {
		
		List <CursoDTO> listacursos = this.getModel().getCursos();
		
		this.getView().getTableInscripciones().setModel(
				SwingUtil.getTableModelFromPojos(
						listacursos,
						new String[] { "id", "nombre", "start_inscr", "end_inscr", "start", "end"},
						new String[] { "Id", "Nombre", "Inicio Inscripciones", "Fin Inscripciones", "Inicio Curso", "Fin Curso"},
						null
						)
				);
		
	}

}
