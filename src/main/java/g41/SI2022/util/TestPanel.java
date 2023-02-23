package g41.SI2022.util;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class TestPanel extends Tab {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	public TestPanel(g41.SI2022.util.SwingMain main) {
		super(main);
		this.setLayout(new BorderLayout());
		JLabel label_2 = new JLabel("NORTH");
		label_2.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label_2, BorderLayout.NORTH);
		JLabel label_3 = new JLabel("EAST");
		label_3.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label_3, BorderLayout.EAST);
		JLabel label_1 = new JLabel("SOUTH");
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label_1, BorderLayout.SOUTH);
		JLabel label_4 = new JLabel("WEST");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label_4, BorderLayout.WEST);
		JLabel label = new JLabel("CENTER");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(label, BorderLayout.CENTER);
	}

	@Override
	protected void initController() {
		// Instantiate the Controller and Model here
	}

}
