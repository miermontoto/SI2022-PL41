package g41.si2022.coiipa.retrasar_fechas;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import lombok.Getter;
import lombok.Setter;
import java.awt.FlowLayout;



@Getter @Setter
public class RetrasarFechasView extends View {



	JPanel mainPanel; //Panel principal
	JPanel tablePanel; //Panel de la tabla
	JScrollPane sp; //Panel de scrolling para la tabla
	private JTable cursos; //Tabla de cursos


	private static final long serialVersionUID = 1838676467561579641L;

	public RetrasarFechasView(SwingMain main) {

		super(main, RetrasarFechasModel.class, RetrasarFechasView.class, RetrasarFechasController.class);

	}

	@Override
	protected void initView () {

		this.setLayout(new BorderLayout(0, 0));

		mainPanel = new JPanel();
		
		//Tabla de los cursos
		FlowLayout flowLayout = (FlowLayout) mainPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		{
			sp = new JScrollPane();
			sp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
			{
				tablePanel = new JPanel (new java.awt.GridLayout(0, 1));
				{
					this.cursos = new JTable();
					this.cursos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					this.cursos.setDefaultEditor(Object.class, null);
				}
				tablePanel.add(sp);
			}
			sp.setViewportView(this.cursos);

		}
		mainPanel.add(tablePanel, BorderLayout.CENTER);
		
		//Panel secundario de opciones
		

		
		mainPanel.add(tablePanel, BorderLayout.CENTER);

		
		this.add(mainPanel, BorderLayout.CENTER); //Finalmente, añado el mainPanel a la ventana
	}

}