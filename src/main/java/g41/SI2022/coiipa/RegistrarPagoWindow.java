package g41.SI2022.coiipa;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class RegistrarPagoWindow {

	JFrame frame;
	private JTextField textfieldidinscripcion;
	private JButton botonaceptar;
	
	public RegistrarPagoWindow() {
		frame = new JFrame("Insertar un nuevo pago");
		frame.setSize(500,500);
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Introducir ID de inscripción:");
		lblNewLabel.setBounds(35, 58, 161, 14);
		frame.getContentPane().add(lblNewLabel);
		
		botonaceptar = new JButton("Aceptar");
		botonaceptar.setBounds(178, 427, 89, 23);
		frame.getContentPane().add(botonaceptar);
		
		textfieldidinscripcion = new JTextField();
		textfieldidinscripcion.setBounds(206, 55, 247, 20);
		frame.getContentPane().add(textfieldidinscripcion);
		textfieldidinscripcion.setColumns(10);
        //frame.setVisible(true);
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JTextField getTextField() {
		return textfieldidinscripcion;
	}

	public void setTextField(JTextField textField) {
		this.textfieldidinscripcion = textField;
	}

	public JButton getBotonaceptar() {
		return botonaceptar;
	}

	public void setBotonaceptar(JButton botonaceptar) {
		this.botonaceptar = botonaceptar;
	}
	
	public String getIdInscripcion() {
		return this.textfieldidinscripcion.getText();
	}
	
	
	
}
