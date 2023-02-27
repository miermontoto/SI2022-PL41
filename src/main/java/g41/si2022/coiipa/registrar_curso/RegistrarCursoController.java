package g41.si2022.coiipa.registrar_curso;

import g41.si2022.util.SwingUtil;

public class RegistrarCursoController {
	private RegistrarCursoModel model;
	private RegistrarCursoView view;

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
						new String[] { "nombre", "apellidos", "email", "direccion" }
				)
		);
		SwingUtil.autoAdjustColumns(this.view.getTablaProfesores());
	}
}
