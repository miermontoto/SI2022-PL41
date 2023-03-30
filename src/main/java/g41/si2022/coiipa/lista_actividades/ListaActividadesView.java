package g41.si2022.coiipa.lista_actividades;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTextArea;
import org.jdesktop.swingx.JXTitledPanel;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.BetterDatePicker;
import g41.si2022.ui.util.JLabelFactory;
import g41.si2022.util.state.CursoState;
import lombok.Getter;

@Getter
public class ListaActividadesView extends View {

    private static final long serialVersionUID = 1L;
    private JTable tablaCursos;
    private JComboBox<CursoState> cbFiltro;
    private JXTextArea infoDescripcion;
    private JXComboBox infoProfesores;
    private JXComboBox infoLocalizaciones;
    private JTable tableEventos;
    private BetterDatePicker startDate, endDate;

    public ListaActividadesView(SwingMain main) {
		super(main, ListaActividadesModel.class, ListaActividadesView.class, ListaActividadesController.class);
    }

    @Override
    protected void initView() {
        this.setLayout(new BorderLayout());

        tablaCursos = new JTable();
        tablaCursos.setDefaultEditor(Object.class, null);
		tablaCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrCursos = new JScrollPane(tablaCursos);

        JLabel lblFiltro = JLabelFactory.getLabel("Filtrar por:");
        cbFiltro = new JComboBox<CursoState>();

        JPanel filterPanel = new JPanel(new GridLayout());

        // Add elements to centerPanel
        filterPanel.add(lblFiltro);
        filterPanel.add(cbFiltro);
        filterPanel.add(startDate = new BetterDatePicker());
        filterPanel.add(endDate = new BetterDatePicker());

        // Add elements to mainNorthPanel
        this.add(filterPanel, BorderLayout.NORTH);
        this.add(scrCursos, BorderLayout.CENTER);

        // ------------- main.BorderLayout.CENTER -------------
        JPanel infoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Elements of mainCenterPanel
        infoDescripcion = new JXTextArea("N/A");
        infoLocalizaciones = new JXComboBox();
        infoProfesores = new JXComboBox();

        // Add elements to mainCenterPanel
        gbc.insets = new Insets(5, 1, 5, 1);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        infoPanel.add(JLabelFactory.getLabel("Descripción:"), gbc);
        gbc.gridy = 1;
        infoPanel.add(JLabelFactory.getLabel("Localizaciones:"), gbc);
        gbc.gridy = 2;
        infoPanel.add(JLabelFactory.getLabel("Docentes:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        //gbc.anchor = GridBagConstraints.LINE_END;
        infoPanel.add(infoDescripcion, gbc);
        infoDescripcion.setEditable(false);
        gbc.gridy = 1;
        infoPanel.add(infoLocalizaciones, gbc);
        gbc.gridy = 2;
        infoPanel.add(infoProfesores, gbc);

        JPanel eventosPanel = new JPanel(new BorderLayout());
        tableEventos = new JTable();
        tableEventos.setDefaultEditor(Object.class, null);
        tableEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrEventos = new JScrollPane(tableEventos);
        eventosPanel.add(scrEventos, BorderLayout.CENTER);
        eventosPanel.add(JLabelFactory.getLabel("Eventos:"), BorderLayout.NORTH);

        JXTitledPanel eventosTitledPanel = new JXTitledPanel("Eventos", eventosPanel);
        eventosTitledPanel.setContentContainer(scrEventos);

        JXTitledPanel infoTitledPanel = new JXTitledPanel("Información del curso", infoPanel);
        infoTitledPanel.setContentContainer(infoPanel);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
        bottomPanel.add(infoTitledPanel);
        bottomPanel.add(eventosTitledPanel);
        bottomPanel.setPreferredSize(new java.awt.Dimension(getMain().getFrame().getWidth(), getMain().getFrame().getHeight() / 4));
        this.add(bottomPanel, BorderLayout.SOUTH);
    }

}
