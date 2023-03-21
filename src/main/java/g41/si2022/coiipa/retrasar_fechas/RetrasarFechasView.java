package g41.si2022.coiipa.retrasar_fechas;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import g41.si2022.mvc.View;
import g41.si2022.ui.SwingMain;




public class RetrasarFechasView extends View {

	JPanel formPanel;
	
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1838676467561579641L;

public RetrasarFechasView(SwingMain main) {

	super(main, RetrasarFechasModel.class, RetrasarFechasView.class, RetrasarFechasController.class);

}

@Override
protected void initView () {
	formPanel = new JPanel();
	this.setLayout(new BorderLayout(0, 0));
}

}