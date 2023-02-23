package g41.SI2022.util;

import javax.swing.JButton;
import javax.swing.JLabel;

public class LoadDatabase extends Tab {

	private static final long serialVersionUID = -4249195203893017275L;

	public LoadDatabase(SwingMain main) {
		super(main);
		Database db = new Database();

		JLabel status = new JLabel();

		JButton schema = new JButton("Execute schema.sql");
		schema.addActionListener(e -> {
			status.setText(db.createDatabase(false) ? "Database created" : "Failed to create database");
		});

		JButton data = new JButton("Execute data.sql");
		data.addActionListener(e -> {
			if (db.isCreated()) {
				try {
					db.loadDatabase();
					status.setText("Loaded data into database");
				} catch (Exception ex) {
					status.setText("Failed to load data");
					throw new ApplicationException(ex);
				}
			} else status.setText("Failed to load data (db hasn't been created yet)");
		});

		this.add(schema);
		this.add(data);
		this.add(status);
	}
}
