package g41.si2022.util;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Debug extends Tab {

	private static final long serialVersionUID = -4249195203893017275L;

	public Debug(SwingMain main) {
		super(main);

		this.setLayout(new BorderLayout());
		JPanel dbButtons = new JPanel ();
		this.add(dbButtons, BorderLayout.NORTH);
		JPanel otherButtons = new JPanel ();
		this.add(otherButtons, BorderLayout.SOUTH);

		Database db = new Database();

		JLabel status = new JLabel();
		status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		JButton schema = new JButton("Execute schema.sql");
		schema.addActionListener(e -> {
			status.setText(db.createDatabase(false) ? "Database created" : "Failed to create database");
		});

		JButton data = new JButton("Execute data.sql");
		data.addActionListener(e -> {
			if (db.exists()) {
				try {
					db.loadDatabase();
					status.setText("Loaded data into database");
				} catch (Exception ex) {
					status.setText("Failed to load data");
					throw new ApplicationException(ex);
				}
			} else status.setText("Failed to load data (db hasn't been created yet)");
		});

		JButton delete = new JButton("Delete database");
		delete.addActionListener(e -> {
			if (db.exists()) {
				try {
					db.deleteDatabase();
					status.setText("Database deleted");
				} catch (Exception ex) {
					status.setText("Failed to delete database");
					throw new ApplicationException(ex);
				}
			} else status.setText("There is no database to delete");
		});

		JButton isFile = new JButton("File exists?");
		isFile.addActionListener(e -> {
			status.setText(db.exists() ? "Database exists" : "Database doesn't exist");
		});

		JButton close = new JButton("Close window");
		close.addActionListener(e -> {
			System.exit(0);
		});

		JButton refresh = new JButton("Refresh tabs");
		refresh.addActionListener(e -> {
			main.initialize();
			status.setText("Tabs refreshed");
		});

		dbButtons.add(schema);
		dbButtons.add(data);
		dbButtons.add(delete);
		dbButtons.add(isFile);
		otherButtons.add(refresh);
		otherButtons.add(close);

		this.add(status, BorderLayout.CENTER);
	}

	@Override
	protected void initController() { }
}
