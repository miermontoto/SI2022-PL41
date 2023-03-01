package g41.si2022.coiipa.registrar_curso;

import java.awt.event.ContainerEvent;
import java.util.List;

import g41.si2022.coiipa.dto.ProfesorDTO;
import g41.si2022.util.SwingUtil;

public class RegistrarCursoController {
	private RegistrarCursoModel model;
	private RegistrarCursoView view;
	
	private List<ProfesorDTO> profesores;

	public RegistrarCursoController (RegistrarCursoModel m, RegistrarCursoView v) {
		this.model = m;
		this.view = v;
		this.initView();
	}

	public void initView () {
		this.getListaProfesores();
	}

	public void getListaProfesores() {
		view.getTablaProfesores().setModel(
				SwingUtil.getTableModelFromPojos(
						this.model.getListaProfesores(),
						new String[] { "nombre", "apellidos", "email", "direccion", "remuneracion" },
						new java.util.HashMap<Integer, java.util.regex.Pattern> () {
							private static final long serialVersionUID = 1L;
						{
							put(4, java.util.regex.Pattern.compile("\\d+(\\.\\d+)?"));
						}}
				)
		);
		SwingUtil.autoAdjustColumns(this.view.getTablaProfesores());
	}
	
	public void insertCurso () {
		this.model.insertCurso(
				this.view.getName(), this.view.getObjetivosDescripcion(),
				this.view.getInscripcionIni(), this.view.getInscripcionFin(), this.view.getCursoIni(), this.view.getCursoFin(),
				this.view.getPlazas(), 0);
	}

	private void getProfesor () {
		javax.swing.table.TableModel model = this.view.getTablaProfesores().getModel();
		for (int i = 0 ; i < model.getRowCount() ; i++) {
			if (Integer.parseUnsignedInt(
					model.getValueAt(model.getColumnCount()-1, i).toString()) != 0) {
				this.profesores.forEach((x) -> {
					if (x.getNombre().equals(model.getValueAt(model.getColumnCount()-1, i))) {
						
					}
				});
			}
		}
	}
}
