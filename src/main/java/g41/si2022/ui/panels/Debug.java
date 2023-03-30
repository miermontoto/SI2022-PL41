package g41.si2022.ui.panels;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import g41.si2022.coiipa.registrar_curso.RegistrarCursoModel;
import g41.si2022.ui.SwingMain;
import g41.si2022.ui.util.FontType;
import g41.si2022.ui.util.JLabelFactory;
import g41.si2022.util.db.Database;
import g41.si2022.util.exception.ApplicationException;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;

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

		gbc.gridx = 1;
		gbc.gridy = 3;
		JButton ruby = new JButton("Ruby!");
		ruby.addActionListener(e -> {
			try {
				Process process = Runtime.getRuntime().exec("ruby src/main/resources/generator/main.rb");
				process.waitFor();
				BufferedReader processIn = new BufferedReader(new InputStreamReader(process.getInputStream()));

				String line;
				String output = "";
				while ((line = processIn.readLine()) != null) {
					output += line + "\n";
				}
				status.setText(output);
			} catch(java.io.IOException | InterruptedException io) {}
		});
		all.add(ruby, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		JButton refreshDb = new JButton("⟳ Regenerate");
		refreshDb.addActionListener(e -> {
			ruby.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database regenerated");
		});
		all.add(refreshDb, gbc);

		gbc.gridy = 3;
		gbc.gridheight = 1;
		JButton smallRefresh = new JButton("⟳ Refresh");
		smallRefresh.addActionListener(e -> {
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database refreshed");
		});
		all.add(smallRefresh, gbc);

		gbc.gridx = 3;
		gbc.gridy = 1;
		JButton insertCurso = new JButton("Insert test curso");
		insertCurso.addActionListener(e -> {
			if (db.exists()) {
				try {
					new RegistrarCursoModel().insertCurso("CSGO S2", "", "5", "2023-03-01", "2023-03-31", "2023-04-01", "2023-04-30", "1");
					status.setText("Inserted test curso using RegistrarCursoModel().insertCurso");
				} catch (Exception ex) {
					status.setText("Failed to insert test curso");
					throw new ApplicationException(ex);
				}
			} else status.setText("Failed to insert test curso (db hasn't been created yet)");
		});
		all.add(insertCurso, gbc);

		gbc.gridy = 2;
		JButton dark = new JButton("Toggle dark mode");
		dark.addActionListener(e -> {
			main.toggleDarkMode();
		});
		all.add(dark, gbc);

		insertCurso.setEnabled(false);
		dark.setEnabled(false);

		gbc.gridy = 3;
		JButton close = new JButton("Close window");
		close.addActionListener(e -> { System.exit(0); });
		all.add(close, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 5;
		all.add(status, gbc);

		gbc.gridy = 0;
		// align center
		gbc.gridwidth = 3;
		all.add(JLabelFactory.getLabel(FontType.title, "Database control panel"), gbc);
	}

	public JComponent getComponent() {
		return all;
	}
}
