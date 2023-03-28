package g41.si2022.ui.components;

import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.regex.Pattern;

public class RowAppendableJTable extends javax.swing.JTable {

	private static final long serialVersionUID = 1L;

	private String[] columnNames;
	private Map<Integer, Pattern> columnMatchers;

	public RowAppendableJTable (String[] columnNames, Map<Integer, Pattern> columnMatchers) {
		super();
		this.columnNames = columnNames;
		this.columnMatchers = columnMatchers;
		// FIXME: Column Names are not displayed
		//		  (Check g41.si2022.coiipa.inscribir_multiples_usuarios.InscribirMultiplesUsuariosView.java)
		javax.swing.table.TableModel tm = new DefaultTableModel(this.columnNames, 0) {
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
					if (this.getValueAt(row, i) == null || this.getValueAt(row, i).equals("") || this.getValueAt(row,  i).equals(columnNames[i]))
						emptyCell = true;
				if (!emptyCell) {
					// Add a new empty row to the table when the last row is edited
					String[] emptyRow = new String[this.columnNames.length];
					for (int i = 0 ; i < this.columnNames.length ; i++) emptyRow[i] = columnNames[i];
					((DefaultTableModel) this.getModel()).addRow(emptyRow);
				}
			}
		});	
		((DefaultTableModel)tm).addRow(columnNames);
		this.setModel(tm);
	}
	
	public RowAppendableJTable (String[] columnNames) {
		this(columnNames, null);
	}
}
