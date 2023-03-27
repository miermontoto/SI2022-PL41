package g41.si2022.ui.components;

import javax.swing.table.DefaultTableModel;

public class RowAppendableJTable extends javax.swing.JTable {

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private java.util.Map<Integer, java.util.regex.Pattern> columnMatchers;

	public RowAppendableJTable (String[] columnNames) {
		super();
		this.columnNames = columnNames;
		// FIXME: Column Names are not displayed
		//		  (Check g41.si2022.coiipa.inscribir_multiples_usuarios.InscribirMultiplesUsuariosView.java)
		DefaultTableModel tm = new DefaultTableModel(this.columnNames, 1) {
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object value, int row, int column) {
				if ( value == null ||
						RowAppendableJTable.this.columnMatchers == null ||
						RowAppendableJTable.this.columnMatchers.get(column) == null ||
						RowAppendableJTable.this.columnMatchers.get(column).matcher(value.toString()).matches()
						) super.setValueAt(value, row, column);
			}
		};
		tm.addTableModelListener(e -> {
			int row = e.getLastRow();
			if (row == this.getRowCount() - 1) {
				boolean emptyCell = false;
				for (int i = 0 ; i < this.getColumnCount() && !emptyCell ; i++)
					if (this.getValueAt(row, i) == null || this.getValueAt(row, i).equals("")) emptyCell = true;
				if (!emptyCell) {
					// Add a new empty row to the table when the last row is edited
					String[] emptyRow = new String[this.columnNames.length];
					for (int i = 0 ; i < this.columnNames.length ; i++) emptyRow[i] = "";
					((DefaultTableModel) this.getModel()).addRow(emptyRow);
				}
			}
		});	
		this.setModel(tm);
	}
}
