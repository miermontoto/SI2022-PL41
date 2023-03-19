package g41.si2022.ui.panels;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import g41.si2022.ui.SwingMain;
import g41.si2022.util.FontType;
import g41.si2022.util.JLabelFactory;
import g41.si2022.util.db.Database;
import g41.si2022.util.exception.ApplicationException;

import java.awt.event.ActionEvent;

public class Debug {

	private JPanel all;

	public Debug(SwingMain main) {

		all = new JPanel();

		all.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		Database db = new Database();

		JLabel status = new JLabel();
		status.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.BOTH;
		JButton schema = new JButton("Run schema");
		schema.addActionListener(e -> {
			status.setText(db.createDatabase(false) ? "Database created" : "Failed to create database");
		});
		all.add(schema, gbc);

		gbc.gridy = 2;
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
		all.add(data, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		JButton refreshDb = new JButton("⟳ Refresh db");
		refreshDb.addActionListener(e -> {
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database regenerated");
		});
		all.add(refreshDb, gbc);

		gbc.gridy = 3;
		gbc.gridheight = 1;
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
		all.add(delete, gbc);

		gbc.gridx = 1;
		JButton isFile = new JButton("Status");
		isFile.addActionListener(e -> {
			status.setText(db.exists() ? "Database exists" : "Database doesn't exist");
		});
		all.add(isFile, gbc);

		gbc.gridx = 2;
		for(int i = 1; i < 4; i++) {
			gbc.gridy = i;
			all.add(JLabelFactory.getLabel("          "), gbc);
		}

		gbc.gridx = 3;
		gbc.gridy = 2;
		JButton dark = new JButton("Toggle dark mode");
		dark.addActionListener(e -> {
			main.toggleDarkMode();
		});
		all.add(dark, gbc);

		gbc.gridy = 3;
		JButton close = new JButton("Close window");
		close.addActionListener(e -> { System.exit(0); });
		all.add(close, gbc);

		gbc.gridy = 4;
		gbc.gridx = 1;
		gbc.gridwidth = 2;
		all.add(status, gbc);

		gbc.gridy = 0;
		all.add(JLabelFactory.getLabel(FontType.title, "Database control panel"), gbc);
	}

	public JComponent getComponent() {
		return all;
	}
}
