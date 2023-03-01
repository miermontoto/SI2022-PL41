package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class InscribirUsuarioView extends Tab {

	private static final long serialVersionUID = 1L;
	private JButton btnCargarCursos;
    private JTable tablaCursos;
    private JScrollPane scrollPane;

    public InscribirUsuarioView(SwingMain main) {
        super(main);
        initialize();
    }

    private void initialize() {
        btnCargarCursos = new JButton("Cargar cursos");
        tablaCursos = new JTable();
        tablaCursos.setDefaultEditor(Object.class, null);
        scrollPane = new JScrollPane(tablaCursos);
        JPanel btnPanel = new JPanel();
        JPanel cursosContainer = new JPanel();
        cursosContainer.add(scrollPane);
        btnPanel.add(btnCargarCursos);
        this.setLayout(new BorderLayout());
        this.add(btnPanel, BorderLayout.NORTH);
        this.add(cursosContainer, BorderLayout.CENTER);
        tablaCursos.setName("Cursos disponibles");
    }

    @Override
    public void initController() { new InscribirUsuarioController(new InscribirUsuarioModel(), this); }


    public JButton getBtnCargarCursos() {
        return btnCargarCursos;
    }

    public JTable getTablaCursos() {
        return tablaCursos;
    }

}
