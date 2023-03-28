package g41.si2022.coiipa.lista_actividades;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.components.JLabelFactory;
import g41.si2022.util.state.CursoState;
import lombok.Getter;

@Getter
public class ListaActividadesView extends View {

    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;
    private JComboBox<CursoState> cbFiltro;
    private JLabel txtDescripcion;
    private JLabel txtProfesor;
    private JLabel txtLugar;
    private BetterDatePicker startDate, endDate;

    public ListaActividadesView(SwingMain main) {
		super(main, ListaActividadesModel.class, ListaActividadesView.class, ListaActividadesController.class);
    }

    @Override
    protected void initView() {
        this.setLayout(new BorderLayout());

        // ------------- main.BorderLayout.NORTH -------------
        JPanel mainNorthPanel = new JPanel(new BorderLayout());

        // Elements of mainNnorthPanel
        JLabel lblCursos = JLabelFactory.getLabel("Cursos:");
        tablaCursos = new JTable();
        tablaCursos.setDefaultEditor(Object.class, null);
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrCursos = new JScrollPane(tablaCursos);

        this.add(mainNorthPanel, BorderLayout.NORTH);

        // centerPanel (BorderLayout.CENTER) with label and table
        JPanel centerPanel = new JPanel(new BorderLayout());

        // eastPanel's elements
        JLabel lblFiltro = JLabelFactory.getLabel("Filtrar por:");
        cbFiltro = new JComboBox<CursoState>();

        JPanel anotherNorthPanel = new JPanel(new GridLayout());

        // Add elements to centerPanel
        anotherNorthPanel.add(lblFiltro);
        anotherNorthPanel.add(cbFiltro);
        anotherNorthPanel.add(startDate = new BetterDatePicker());
        anotherNorthPanel.add(endDate = new BetterDatePicker());
        centerPanel.add(anotherNorthPanel, BorderLayout.NORTH);
        centerPanel.add(lblCursos, BorderLayout.CENTER);
        centerPanel.add(scrCursos, BorderLayout.CENTER);

        // Add elements to mainNorthPanel
        mainNorthPanel.add(centerPanel, BorderLayout.NORTH);

        // ------------- main.BorderLayout.CENTER -------------
        JPanel mainCenterPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Elements of mainCenterPanel
        txtDescripcion = JLabelFactory.empty();
        txtLugar = JLabelFactory.empty();
        txtProfesor = JLabelFactory.empty();

        // Add elements to mainCenterPanel
        gbc.insets = new Insets(5, 1, 5, 1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        mainCenterPanel.add(JLabelFactory.getLabel("Descripción del curso:"), gbc);
        gbc.gridy = 1;
        mainCenterPanel.add(JLabelFactory.getLabel("Localizaciones:"), gbc);
        gbc.gridy = 2;
        mainCenterPanel.add(JLabelFactory.getLabel("Docentes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        //gbc.anchor = GridBagConstraints.LINE_END;
        mainCenterPanel.add(txtDescripcion, gbc);
        gbc.gridy = 1;
        mainCenterPanel.add(txtLugar, gbc);
        gbc.gridy = 2;
        mainCenterPanel.add(txtProfesor, gbc);

        // Add mainCenterPanel to this
        this.add(mainCenterPanel, BorderLayout.CENTER);
    }

}
