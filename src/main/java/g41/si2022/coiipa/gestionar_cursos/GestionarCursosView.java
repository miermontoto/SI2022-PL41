package g41.si2022.coiipa.gestionar_cursos;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import g41.si2022.util.BetterDatePicker;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.Tab;
import lombok.Getter;

@Getter
public class GestionarCursosView extends Tab {

    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;
    private JComboBox<String> cbFiltro;
    private JTextArea txtDescripcion;
    private JTextArea txtProfesor;
    private JTextArea txtLugar;
    private BetterDatePicker startDate, endDate;

    public GestionarCursosView(SwingMain main)
    {
        super(main);
        initialize();
    }

    private void initialize()
    {
        this.setLayout(new BorderLayout());

        // ------------- main.BorderLayout.NORTH -------------
        JPanel mainNorthPanel = new JPanel(new BorderLayout());

        // Elements of mainNnorthPanel
        JLabel lblCursos = new JLabel("Cursos:");
        tablaCursos = new JTable();
        tablaCursos.setDefaultEditor(Object.class, null);
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrCursos = new JScrollPane(tablaCursos);

        // Add mainNorthPanel to this
        this.add(mainNorthPanel, BorderLayout.NORTH);

        // centerPanel (BorderLayout.CENTER) with label and table
        JPanel centerPanel = new JPanel(new BorderLayout());

        // eastPanel's elements
        JLabel lblFiltro = new JLabel("Filtrar por:");
        cbFiltro = new JComboBox<String>();

        // Add items to comboBox
        cbFiltro.addItem("Fecha");
        cbFiltro.addItem("Estado");

        JPanel anotherNorthPanel = new JPanel(new GridLayout());

        // Add elements to centerPanel
        anotherNorthPanel.add(lblFiltro);
        anotherNorthPanel.add(cbFiltro);
        anotherNorthPanel.add(startDate = new BetterDatePicker());
        anotherNorthPanel.add(endDate = new BetterDatePicker());
        centerPanel.add(anotherNorthPanel, BorderLayout.NORTH);
        centerPanel.add(lblCursos, BorderLayout.CENTER);
        centerPanel.add(scrCursos, BorderLayout.CENTER);

        // rightPanel (BorderLayout.EAST) with label and comboBox
        // JPanel eastPanel = new JPanel(new GridLayout());

        
        // Add items to eastPanel
        // eastPanel.add(lblFiltro);
        // eastPanel.add(cbFiltro);
        // eastPanel.add(startDate = new BetterDatePicker());
        // eastPanel.add(endDate = new BetterDatePicker());

        // Add elements to mainNorthPanel
        mainNorthPanel.add(centerPanel, BorderLayout.NORTH);
        // mainNorthPanel.add(eastPanel, BorderLayout.EAST);

        // ------------- main.BorderLayout.CENTER -------------
        JPanel mainCenterPanel = new JPanel(new GridLayout(4, 2));

        // Elements of mainCenterPanel
        JLabel lblDescripcion = new JLabel("Objetivos y contenidos del curso:");
        txtDescripcion = new JTextArea();
        JLabel lblLugar = new JLabel("Lugar:");
        txtLugar = new JTextArea();
        JLabel lblProfesor = new JLabel("Profesor:");
        txtProfesor = new JTextArea();

        // Add elements to mainCenterPanel
        mainCenterPanel.add(lblDescripcion);
        mainCenterPanel.add(txtDescripcion);
        mainCenterPanel.add(lblLugar);
        mainCenterPanel.add(txtLugar);
        mainCenterPanel.add(lblProfesor);
        mainCenterPanel.add(txtProfesor);

        // Add mainCenterPanel to this
        this.add(mainCenterPanel, BorderLayout.CENTER);
    }

    @Override
	protected void initController() { new GestionarCursosController(new GestionarCursosModel(), this); }

}
