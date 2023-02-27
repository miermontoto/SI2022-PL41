package g41.SI2022.coiipa.registrarCurso;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import g41.SI2022.util.SwingUtil;

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
		this.addListeners();
	}

	public void getListaProfesores() {
		view.getTablaProfesores().setModel(
				SwingUtil.getTableModelFromPojos(
						this.model.getListaProfesores(),
						new String[] { "nombre", "apellidos", "email", "direccion", "remuneracion"},
						new java.util.HashMap<Integer, java.util.regex.Pattern> () {
							private static final long serialVersionUID = 1L;
						{
							put(4, java.util.regex.Pattern.compile("\\d+(\\.\\d+)?"));
						}}
				)
		);
		SwingUtil.autoAdjustColumns(this.view.getTablaProfesores());
	}
	
	public void addListeners () {
		this.view.getSubmitButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent evt) {
				System.out.println("Triggered");
			}
		});
	}
}
