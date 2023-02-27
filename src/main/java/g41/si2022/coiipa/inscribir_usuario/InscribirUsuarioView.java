package g41.si2022.coiipa.inscribir_usuario;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
        JPanel btnPanel = new JPanel();
        JPanel cursosContainer = new JPanel();
        cursosContainer.add(scrollPane);
        btnPanel.add(btnCargarCursos);
        this.setLayout(new BorderLayout());
        this.add(btnPanel, BorderLayout.NORTH);
        this.add(cursosContainer, BorderLayout.CENTER);

        DefaultTableModel emptyModel = new DefaultTableModel(new String[]{"id", "nombre", "plazas", "start_inscr", "end_inscr"}, 0);
        tablaCursos.setModel(emptyModel);
        tablaCursos.setName("Cursos disponibles");


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
