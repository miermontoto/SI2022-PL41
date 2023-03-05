package g41.si2022.coiipa.gestionar_cursos;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import g41.si2022.util.SwingMain;
import g41.si2022.util.Tab;
import lombok.Getter;

@Getter
public class GestionarCursosView extends Tab {
    
    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;
    
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
        // Add mainNorthPanel to this
        this.add(mainNorthPanel, BorderLayout.NORTH);

        // Elements of mainNnorthPanel
        JLabel lblCursos = new JLabel("Cursos:");
        tablaCursos = new JTable();
        tablaCursos.setDefaultEditor(Object.class, null);
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrCursos = new JScrollPane(tablaCursos);
       
        // centerPanel (BorderLayout.CENTER) with label and table
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // Add elements to centerPanel
        centerPanel.add(lblCursos, BorderLayout.NORTH);
        centerPanel.add(scrCursos, BorderLayout.CENTER);
       
        // rightPanel (BorderLayout.EAST) with label and comboBox
        JPanel eastPanel = new JPanel(new GridLayout());
       
        // eastPanel's elements
        JLabel lblFiltro = new JLabel("Filtrar por:");
        JComboBox<String> cbFiltro = new JComboBox<String>();
       
        // Add items to comboBox
        cbFiltro.addItem("Fecha");
        cbFiltro.addItem("Estado");
       
        // Add items to eastPanel
        eastPanel.add(lblFiltro);
        eastPanel.add(cbFiltro);

        // Add elements to mainNorthPanel
        mainNorthPanel.add(centerPanel, BorderLayout.NORTH);
        mainNorthPanel.add(eastPanel, BorderLayout.EAST);

        // ------------- main.BorderLayout.CENTER -------------
        JPanel mainCenterPanel = new JPanel(new GridLayout(4, 2));

        // Elements of mainCenterPanel
        JLabel lblObjetivos = new JLabel("Objetivos del curso:");
        JTextArea txtObjetivos = new JTextArea();
        JLabel lblContenidos = new JLabel("Contenidos principales:");
        JTextArea txtContenidos = new JTextArea();
        JLabel lblLugar = new JLabel("Lugar:");
        JTextField txtLugar = new JTextField();
        JLabel lblProfesor = new JLabel("Profesor:");
        JTextField txtProfesor = new JTextField();
        
        // Add elements to mainCenterPanel
        mainCenterPanel.add(lblObjetivos);
        mainCenterPanel.add(txtObjetivos);
        mainCenterPanel.add(lblContenidos);
        mainCenterPanel.add(txtContenidos);
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
