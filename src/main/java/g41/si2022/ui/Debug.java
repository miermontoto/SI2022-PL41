package g41.si2022.ui;

import java.awt.BorderLayout;
import lombok.Getter;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import g41.si2022.util.ApplicationException;
import g41.si2022.util.Database;

import java.awt.event.ActionEvent;

@Getter
public class Debug {

	private JPanel all;

	public Debug(SwingMain main) {

		all = new JPanel();

		all.setLayout(new BorderLayout());
		JPanel dbButtons = new JPanel ();
		all.add(dbButtons, BorderLayout.NORTH);
		JPanel otherButtons = new JPanel ();
		all.add(otherButtons, BorderLayout.SOUTH);

		Database db = new Database();

		JLabel status = new JLabel();
		status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		JButton schema = new JButton("Run schema");
		schema.addActionListener(e -> {
			status.setText(db.createDatabase(false) ? "Database created" : "Failed to create database");
		});

		JButton data = new JButton("Load data");
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

		JButton refreshDb = new JButton("⟳");
		refreshDb.addActionListener(e -> {
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database regenerated");
		});

		JButton delete = new JButton("Delete");
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

		JButton isFile = new JButton("Status");
		isFile.addActionListener(e -> {
			status.setText(db.exists() ? "Database exists" : "Database doesn't exist");
		});

		JButton close = new JButton("Close window");
		close.addActionListener(e -> {
			System.exit(0);
		});

		dbButtons.add(refreshDb);
		dbButtons.add(schema);
		dbButtons.add(data);
		dbButtons.add(delete);
		dbButtons.add(isFile);
		otherButtons.add(close);

		all.add(status, BorderLayout.CENTER);
	}

	public JComponent getComponent() {
		return all;
	}
}
