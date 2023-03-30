package g41.si2022.ui.components.table;

import javax.swing.table.DefaultTableModel;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * RowAppendableJTable.
 * <p>
 * This class is a {@link javax.swing.JTable} that initially contains a single row.<br>
 * A row has a default value that corresponds to that of the column header.<br>
 * When a row has been filled with data different from that of the column header, a new row will appear below to continue filling in data.
 * </p>
 * 
 * @author Alex // UO281827
 *
 * @see javax.swing.JTable
 */
public class RowAppendableJTable extends javax.swing.JTable {

	private static final long serialVersionUID = 1L;

	/**
	 * Contains the names of the columns.<br>
	 * This are the strings that will appear on the new row's cells.
	 */
	private String[] columnNames;

	/**
	 * Contains the Regex restrictions for the cells.<br>
	 * By default a cell accepts any kind of data. However, you may if you wish
	 * only allow the values that match this regex for any given column.
	 */
	private Map<Integer, Pattern> columnMatchers;

	/**
	 * Creates a new {@code RowAppendableJTable}. 
	 * This Table will allow only the text that matches the Regex {@link Pattern} for that given column.
	 * 
	 * @param columnNames Names of the columns
	 * @param columnMatchers Regex to be applied to each column. If there is no entry, no regex will be checked and any data will be allowed.
	 * 
	 * @see Pattern
	 */
	public RowAppendableJTable (
			String[] columnNames,
			Map<Integer, Pattern> columnMatchers) {
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

	/**
	 * Creates a new {@code RowAppendableJTable}. This Table will allow any kind of text to be inserted in the cells.
	 * 
	 * @param columnNames Names of the columns
	 */
	public RowAppendableJTable (String[] columnNames) {
		this(columnNames, null);
	}

}
