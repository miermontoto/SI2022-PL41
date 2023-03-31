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
				status.setText(processIn.readLine());
			} catch(java.io.IOException | InterruptedException io) {}
		});
		all.add(ruby, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridheight = 2;
		JButton refresh = new JButton("⟳ Refresh");
		refresh.addActionListener(e -> {
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database refreshed");
		});
		all.add(refresh, gbc);


		gbc.gridy = 3;
		gbc.gridheight = 1;
		JButton regenerate = new JButton("⟳ Regenerate");
		regenerate.addActionListener(e -> {
			ruby.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			schema.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			data.getActionListeners()[0].actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			status.setText("Database regenerated");
		});
		all.add(regenerate, gbc);

		gbc.gridy = 4;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		all.add(status, gbc);

		gbc.gridy = 0;
		gbc.gridwidth = 2;
		all.add(JLabelFactory.getLabel(FontType.title, "Database control panel"), gbc);
	}

	public JComponent getComponent() {
		return all;
	}
}
