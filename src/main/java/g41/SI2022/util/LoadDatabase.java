package g41.SI2022.util;

import javax.swing.JButton;

public class LoadDatabase extends Tab {

	public LoadDatabase(SwingMain main) {
		super(main);
		Database db = new Database();

		JButton schema = new JButton("Execute schema.sql");
		schema.addActionListener(e -> {
			db.createDatabase(false);
		});

		JButton data = new JButton("Execute data.sql");
		data.addActionListener(e -> {
			if (db.isCreated()) db.loadDatabase();
			else System.err.println("Database hasn't been created");
		});

		this.add(schema);
		this.add(data);
	}

}
