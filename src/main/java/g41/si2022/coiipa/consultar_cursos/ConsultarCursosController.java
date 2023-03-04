package g41.si2022.coiipa.consultar_cursos;

import g41.si2022.coiipa.dto.CursoDTO;
import g41.si2022.coiipa.dto.DocenciaDTO;
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
	private String gastos;
	private String ingresosEstimados;
	private String ingresosReales;
	private String balanceEstimado;
	private String balanceReal;

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
				SwingUtil.exceptionWrapper(() -> balance());
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

	public void balance() {
		for (CursoDTO curso : cursos) {
			if (curso.getNombre().equals(SwingUtil.getSelectedKey(view.getTablaCursos()))) {
				gastos = model.getGastos(curso.getId());
				ingresosEstimados = model.getIngresosEstimados(curso.getId());
				String balanceEstimado = gastos.equals("-") ? ingresosEstimados : String.valueOf(Double.parseDouble(ingresosEstimados) - Integer.parseInt(gastos));
				StringBuilder sb = new StringBuilder();
				sb.append("<html><body>");
				sb.append("<p><b>Gastos:</b> " + gastos + "€");
				sb.append("<p><b>Ingresos estimados:</b> " + ingresosEstimados + "€");
				sb.append("<p><b>Balance estimado:</b> " + balanceEstimado + "€");
				sb.append("</body></html>");
				view.getLblEconomicInfo().setText(sb.toString());
				return;
			}
		}
		throw new ApplicationException("Curso seleccionado desconocido");
	}

	public void getListaInscripciones(String idCurso) {
		inscripciones = model.getListaInscr(idCurso);
		TableModel tableModel = SwingUtil.getTableModelFromPojos(inscripciones, new String[] { "inscripcion_fecha", "alumno_nombre" },
				new String[] { "Fecha de inscripción", "Alumno"}, null);
		view.getTablaInscr().setModel(tableModel);
		SwingUtil.autoAdjustColumns(view.getTablaInscr());
	}

	public void getListaBalance() {

	}
}
