package g41.SI2022.coiipa;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JLabel;

public class TestPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TestPanel() {
		this.setLayout(new BorderLayout());
		this.add(new JLabel("NORTH"), BorderLayout.NORTH);
		this.add(new JLabel("EAST"), BorderLayout.EAST);
		this.add(new JLabel("SOUTH"), BorderLayout.SOUTH);
		this.add(new JLabel("WEST"), BorderLayout.WEST);
		this.add(new JLabel("CENTER"), BorderLayout.CENTER);
	}

}
