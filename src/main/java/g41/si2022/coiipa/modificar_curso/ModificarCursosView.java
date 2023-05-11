package g41.si2022.coiipa.modificar_curso;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import org.jdesktop.swingx.JXTitledPanel;

import com.github.lgooddatepicker.zinternaltools.JIntegerTextField;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.components.table.RowAppendableJTable;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;
import lombok.Getter;

@Getter
public class ModificarCursosView extends View {

    private static final long serialVersionUID = 1L;
	private JTable tableCursos;
    private JTextField txtNombre;
    private JTextArea txtDescripcion;
    private JTextField txtPlazas;
    private RowAppendableJTable tablaCostes;
    private JTable tableProfesores;
    private JTable tableSesiones;
    private JButton btnAddSesion;
    private JButton btnRemoveSesion;
    private JButton btnGuardar;
	private JLabel infoNombre;
	private JLabel infoFechaInscr;
	private JLabel infoFechaCurso;
	private JLabel infoPlazasOcupadas;

    public ModificarCursosView(SwingMain main) {
        super(main, ModificarCursosModel.class, ModificarCursosView.class, ModificarCursosController.class);
    }

    @Override
    protected void initView() {
        this.setLayout(new BorderLayout(0, 0));
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(0, 1));

        JXTitledPanel panelTabla = new JXTitledPanel("Cursos");
        JXTitledPanel panelInfo = new JXTitledPanel("Información del curso");
        JXTitledPanel panelModificar = new JXTitledPanel("Modificar curso");

        JPanel contentTabla = new JPanel();
        JPanel contentInfo = new JPanel();
        JPanel contentModificar = new JPanel();

        JScrollPane jscr = new JScrollPane();
        jscr.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jscr.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jscr.getVerticalScrollBar().setUnitIncrement(10);
        jscr.setViewportView(contentModificar);
        panelTabla.setContentContainer(contentTabla);
        panelInfo.setContentContainer(contentInfo);
        panelModificar.setContentContainer(jscr);

        contentTabla.add(new JScrollPane(tableCursos = new JTable()));
        tableCursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableCursos.setDefaultEditor(Object.class, null);

        contentModificar.setLayout(new GridBagLayout());
        GridBagConstraints left = new GridBagConstraints();
        GridBagConstraints right = new GridBagConstraints();

        { // Nombre del curso
			{ // Label
				left.insets = new Insets(15, 0, 0, 0);
				left.fill = GridBagConstraints.HORIZONTAL;
				left.gridx = 0;
				left.gridy = 0;
				left.weighty = 1;
                left.weightx = 0.5;
				contentModificar.add(JLabelFactory.getLabel("Nombre:"), left);
			} { // Input
				right.insets = new Insets(15, 15, 0, 0);
                right.fill = GridBagConstraints.HORIZONTAL;
				right.gridx = 1;
				right.gridy = 0;
				right.weighty = 1;
                right.weightx = 0.5;
				contentModificar.add(this.txtNombre = new JTextField(), right);
			}
		} { // Objetivos y Descripcion
			{ // Label
				left.gridy = 1;
				left.weighty = 2;
				contentModificar.add(JLabelFactory.getLabel("Objetivos y descripción:"), left);
			} { // Input
				right.gridy = 1;
				right.weighty = 2;
				txtDescripcion = new JTextArea();
				txtDescripcion.setLineWrap(true);
				txtDescripcion.setRows(5);
				contentModificar.add(this.txtDescripcion, right);
			}
		} { // Plazas
			{ // Label
				left.gridy = 2;
				left.weighty = 1;
				contentModificar.add(JLabelFactory.getLabel("Plazas:"), left);
			} { // Input
				right.gridy = 2;
				contentModificar.add(this.txtPlazas = new JIntegerTextField(), right);
			}
		} { // Coste
			{ // Label
				left.gridy = 3;
				contentModificar.add(JLabelFactory.getLabel("Coste de inscripción:"), left);
			} { // Input
				right.gridy = 3;
				right.fill = GridBagConstraints.BOTH;
				contentModificar.add(this.tablaCostes = new g41.si2022.ui.components.table.RowAppendableJTable(
						new String[] {"ID", "Nombre Colectivo", "Coste"},
						new String[] {"Nombre Colectivo", "Coste"},
						new java.util.TreeMap<Integer, java.util.regex.Pattern>() {
							private static final long serialVersionUID = 1L;
							{
								put(2, java.util.regex.Pattern.compile("\\d*(\\.(\\d){1,2})?"));
								put(1, java.util.regex.Pattern.compile(".*"));
							}
						},
						new boolean[] {true, true}
					), right
				);
			}
		} { // Sesiones
			{ // Label
				left.gridy = 6;
				left.fill = GridBagConstraints.BOTH;
				left.weighty = 1;
				contentModificar.add(JLabelFactory.getLabel("Sesiones:"), left);
			}
			right.gridy = 6;
			right.fill = GridBagConstraints.BOTH;
			right.weighty = 1;
			JPanel sesionesPanel = new JPanel(new BorderLayout());
			contentModificar.add(sesionesPanel, right);
			{ // Tabla de sesiones
				tableSesiones = new JTable();
				JScrollPane sp = new JScrollPane();
				sp.getVerticalScrollBar().setUnitIncrement(20);
				sp.setPreferredSize(new Dimension(this.getWidth(), 150));
				sp.setViewportView(this.tableSesiones);
				sesionesPanel.add(sp, BorderLayout.CENTER);
			} { // Botones
				JPanel btnPanel = new JPanel(new GridLayout(1, 2, 10, 10));
				btnPanel.add(this.btnAddSesion = new JButton("Añadir sesión"));
				btnPanel.add(this.btnRemoveSesion = new JButton("Eliminar sesión"));
				sesionesPanel.add(btnPanel, BorderLayout.SOUTH);
			}
		} { // Profesores
            { // Label
                left.gridy = 7;
                left.fill = GridBagConstraints.BOTH;
                left.weighty = 1;
                contentModificar.add(JLabelFactory.getLabel("Profesores:"), left);
            } { // Tabla de profesores
                right.gridy = 7;
                right.fill = GridBagConstraints.BOTH;
                right.weighty = 1;
                JScrollPane sp = new JScrollPane();
				sp.setPreferredSize(new Dimension(this.getWidth(), 150));
				sp.getVerticalScrollBar().setUnitIncrement(20);
				sp.setViewportView(tableProfesores = new JTable());
                contentModificar.add(sp, right);
            }
        } { // Botón de guardar
            left.gridy = 8;
            left.fill = GridBagConstraints.HORIZONTAL;
            left.gridwidth = 2;
            contentModificar.add(btnGuardar = new JButton("Guardar cambios"), left);
        }

		contentInfo.setLayout(new GridBagLayout());

		Insets spacer = new Insets(10, 10, 10, 10);
		Insets next = new Insets(0, 10, 0, 10);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = spacer;
		gbc.anchor = GridBagConstraints.CENTER;
		contentInfo.add(JLabelFactory.getLabel(FontType.bold, "Nombre del curso"), gbc);

		gbc.gridy = 1;
		gbc.insets = next;
		contentInfo.add(infoNombre = new JLabel(), gbc);

		gbc.gridy = 2;
		gbc.insets = spacer;
		contentInfo.add(JLabelFactory.getLabel(FontType.bold, "Fechas de inscripción"), gbc);

		gbc.gridy = 3;
		gbc.insets = next;
		contentInfo.add(infoFechaInscr = new JLabel(), gbc);

		gbc.gridy = 4;
		gbc.insets = spacer;
		contentInfo.add(JLabelFactory.getLabel(FontType.bold, "Fechas del curso"), gbc);

		gbc.gridy = 5;
		gbc.insets = next;
		contentInfo.add(infoFechaCurso = new JLabel(), gbc);

		gbc.gridy = 6;
		gbc.insets = spacer;
		contentInfo.add(JLabelFactory.getLabel(FontType.bold, "Plazas ocupadas"), gbc);

		gbc.gridy = 7;
		gbc.insets = next;
		contentInfo.add(infoPlazasOcupadas = new JLabel(), gbc);

        rightPanel.add(panelInfo);
        rightPanel.add(panelTabla);
        this.add(panelModificar);
        this.add(rightPanel, BorderLayout.EAST);
    }

}
