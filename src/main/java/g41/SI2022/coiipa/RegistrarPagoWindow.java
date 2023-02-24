package g41.SI2022.coiipa;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;

public class RegistrarPagoWindow {

	JFrame frame;
	
	public RegistrarPagoWindow() {
		frame = new JFrame("Insertar un nuevo pago");
		frame.setSize(500,500);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
		
	}
	
}
