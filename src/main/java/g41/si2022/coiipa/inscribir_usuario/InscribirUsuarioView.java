package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;

public class InscribirUsuarioView extends Tab {

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
        scrollPane = new JScrollPane(tablaCursos);
        JPanel cursosContainer = new JPanel();
        cursosContainer.add(btnCargarCursos);
        cursosContainer.add(scrollPane);
        this.setLayout(new BorderLayout());
        this.add(cursosContainer, BorderLayout.SOUTH);
    }

    @Override
    public void initController() { }


    public JButton getBtnCargarCursos() {
        return btnCargarCursos;
    }

    public JTable getTablaCursos() {
        return tablaCursos;
    }

}
