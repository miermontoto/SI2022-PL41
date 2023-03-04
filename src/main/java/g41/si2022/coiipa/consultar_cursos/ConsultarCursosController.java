package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.InscripcionDTO;
import g41.si2022.util.SwingUtil;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.table.TableModel;
import g41.si2022.util.ApplicationException;

public class ConsultarCursosController {

	private ConsultarCursosModel model;
	private ConsultarCursosView view;

	private List<CursoDTO> cursos;
	private List<InscripcionDTO> inscripciones;

	public ConsultarCursosController(ConsultarCursosModel m, ConsultarCursosView v)
	{
		this.model = m;
		this.view = v;
		initView();
	}

	public void initView()
	{
		getListaCursos();
		view.getTablaCursos().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent ent) {
				SwingUtil.exceptionWrapper(() -> getValueCurso());

			}
		});
	}

	public void getListaCursos() {
		cursos = model.getListaCursos();
        TableModel tableModel = SwingUtil.getTableModelFromPojos(cursos, new String[] { "nombre", "estado", "plazas", "start", "end" },
        		new String[] { "Nombre", "Estado", "Plazas", "Fecha ini. curso", "Fecha fin curso" }, null);
        view.getTablaCursos().setModel(tableModel);
        SwingUtil.autoAdjustColumns(view.getTablaCursos());
	}

	public void getValueCurso() {
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
				getListaInscripciones(curso.getId());
				return;
			}
		}
        throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void getListaInscripciones(String idCurso) {
		inscripciones = model.getListaInscr(idCurso);
		TableModel tableModel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "fecha", "estado" },
				new String[] { "Fecha de inscripción", "Estado"}, null);
		view.getTablaInscr().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getTablaInscr());
	}
}
