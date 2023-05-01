package g41.si2022.ui.components.table;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.commons.beanutils.PropertyUtils;

import g41.si2022.util.exception.UnexpectedException;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * RowAppendableJTable.
 * <p>
 * This class is a {@link javax.swing.JTable} that initially contains a single row.<br>
 * A row has a default value that corresponds to that of the column header.<br>
 * When all mandatory columns of a row have been filled with data different from their respective column header,
 * a new row will appear below to continue filling in data.
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
	 */
	private String[] columnNames;

	/**
	 * Contains the names of the columns that are displayed.<br>
	 * This are the strings that will appear on the new row's cells.
	 */
	private String[] visibleColumnNames;

	/**
	 * Contains the Regex restrictions for the cells.<br>
	 * By default a cell accepts any kind of data. However, you may if you wish
	 * only allow the values that match this regex for any given column.
	 */
	private Map<Integer, Pattern> columnMatchers;

	/**
	 * Some columns may not be mandatory. This array should contain {@code true} for all mandatory columns and {@code false}
	 * for optional columns.<br>
	 * A new row will be aded only when all mandatory columns are filled.
	 */
	private boolean[] mandatoryColumns;

	/**
	 * Creates a new {@code RowAppendableJTable}.
	 * This Table will allow only the text that matches the Regex {@link Pattern} for that given column.
	 *
	 * @param columnNames Names of the columns
	 * @param treeMap Regex to be applied to each column. If there is no entry, no regex will be checked and any data will be allowed.
	 * @param mandatoryColumn Array to determine wheter a given column is mandatory or not. Non-mandatory columns will be skipped when adding new rows.
	 *
	 * @see Pattern
	 */
	public RowAppendableJTable (
			String[] columnNames,
			Map<Integer, Pattern> treeMap,
			boolean[] mandatoryColumns) {
		this(columnNames, columnNames, treeMap, mandatoryColumns);
	}

	/**
	 * Creates a new {@code RowAppendableJTable}.
	 * This Table will allow only the text that matches the Regex {@link Pattern} for that given column.<br>
	 * This Table will also allow to hide some of the columns.
	 *
	 * @param columnNames Names of the columns
	 * @param visibleColumnNames Names of the columns that are actually visible (omit the ones that are not visible)
	 * @param treeMap Regex to be applied to each column. If there is no entry, no regex will be checked and any data will be allowed.
	 * @param mandatoryColumn Array to determine wether a given column is mandatory or not. Non-mandatory columns will be skipped when adding new rows.
	 *
	 * @see Pattern
	 */
	public RowAppendableJTable (
			String[] columnNames,
			String[] visibleColumnNames,
			Map<Integer, Pattern> treeMap,
			boolean[] mandatoryColumns) {
		super();
		this.visibleColumnNames = visibleColumnNames;
		this.columnNames = columnNames;
		this.columnMatchers = treeMap;
		this.mandatoryColumns = mandatoryColumns == null ? this.getFullMandatoryArray(columnNames.length) : mandatoryColumns;
		// FIXME: Column Names are not displayed
		//		  (Check g41.si2022.coiipa.inscribir_multiples_usuarios.InscribirMultiplesUsuariosView.java)
		this.setModel(this.getTableModelFromPojos(null, null, columnNames, treeMap));
	}

	private boolean[] getFullMandatoryArray (int size) {
		boolean[] out = new boolean[size];
		for (int i = 0 ; i < size ; i++) out[i] = true;
		return out;
	}

	/**
	 * Creates a new {@code RowAppendableJTable}.
	 * This Table will allow any kind of text to be inserted in the cells and all columns are mandatory.
	 *
	 * @param columnNames Names of the columns
	 */
	public RowAppendableJTable (String[] columnNames) {
		this(columnNames, columnNames, null, null);
	}

	/*
	@Override
	public Object getValueAt(int row, int col) {
		return super.getValueAt(row, col).equals(this.columnNames[col]) ? this.columnNames[col] : super.getValueAt(row, col);
	}
	*/

	public <E> void setData (
			List<E> pojos,
			String[] columnIdentifiers,
			String[] columnNames,
			Map<Integer, Pattern> matchers) {
		this.setModel(this.getTableModelFromPojos(
			pojos,
			columnIdentifiers,
			columnNames != null ? columnNames : this.getColumnNames(),
			matchers != null ? matchers : this.columnMatchers
		));
	}

	/**
	 * Returns the column names of this table.
	 *
	 * @return Column names of this table
	 */
	public String[] getColumnNames () {
		return this.columnNames;
	}

	/**
	 * Returns the data contained in this table.
	 * <p>
	 * This data is represented by a List of Maps. Each entry in the list is one row,
	 * the map is a relation between the column names and the value they hold.<br>
	 * If a column's data has not been filled, {@code null} will be set in that position of the returned data structure.
	 * </p> <p>
	 * Note that this method is O(n^2) and that it will not return the last empty row (nor any empty rows if that was possible at all).
	 * </p>
	 *
	 * @return Data structure containing the data from this table.
	 */
	public List<Map<String, String>> getData () {
		List<Map<String, String>> out = new java.util.ArrayList<Map<String, String>> ();
		for (int i = 0 ; i < this.getRowCount() ; i++) {
			out.add(new java.util.HashMap<String, String> ());
			for (int j = 0 ; j < RowAppendableJTable.this.getColumnNames().length ; j++)
				out.get(i).put(this.getColumnNames()[j],
						this.getValueAt(i, j).toString().trim().equals(this.getColumnNames()[j].trim()) ||
						this.getValueAt(i, j).toString().trim().isEmpty()
								? null
								: this.getValueAt(i, j).toString()
				);
		}
		return out.stream()
				.filter(row -> row.values().stream()
						.anyMatch(value -> value != null))
				.collect(java.util.stream.Collectors.toList());
	}

	private <E> TableModel getTableModelFromPojos(List<E> pojos, String[] colProperties,
			String[] colNames, java.util.Map<Integer, java.util.regex.Pattern> writeableColumns) {
		// Creación inicial del tablemodel y dimensionamiento
		// Hay que tener en cuenta que para que la tabla pueda mostrar las columnas deberá estar dentro de un JScrollPane
		TableModel tm = null;
		if (colProperties != null && colNames.length != colProperties.length) throw new UnexpectedException("colProperties es distinto en tamaño a colNames");
		if (pojos == null) pojos = new java.util.ArrayList<E> ();
		tm = new DefaultTableModel(colNames, pojos.size()) {
			private static final long serialVersionUID = 1L;

			@Override
			public void setValueAt(Object value, int row, int column) {
				if ( value == null ||
						RowAppendableJTable.this.columnMatchers == null ||
						RowAppendableJTable.this.columnMatchers.get(column) == null ||
						RowAppendableJTable.this.columnMatchers.get(column).matcher(value.toString()).matches()
						) super.setValueAt(value, row, column);
			}

			@Override
			public boolean isCellEditable (int row, int column) {
				return writeableColumns == null ? false : writeableColumns.keySet().stream().anyMatch( x -> x == column);
			}
		};

		// Carga cada uno de los valores de pojos usando PropertyUtils (de apache coommons beanutils)
		for (int i = 0; i < pojos.size(); i++) {
			for (int j=0; j<colProperties.length; j++) {
				try {
					Object value=PropertyUtils.getSimpleProperty(pojos.get(i), colProperties[j]);
					tm.setValueAt(value, i, j);
				} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new UnexpectedException(e);
				}
			}
		}

		tm.addTableModelListener(e -> {
			int row = e.getLastRow();
			if (row == this.getRowCount() - 1) {
				boolean emptyCell = false;
				for (int i = 0 ; i < this.getColumnCount() && !emptyCell ; i++)
					emptyCell = (this.mandatoryColumns[i] &&
							(this.getValueAt(row, i) == null ||
							this.getValueAt(row, i).equals("") ||
							this.getValueAt(row, i).equals(visibleColumnNames[i])));
					if (!emptyCell) {
						// Add a new empty row to the table when the last row is edited
						String[] emptyRow = new String[this.columnNames.length];
						for (int i = 0 ; i < this.columnNames.length ; i++) emptyRow[i] = columnNames[i];
						((DefaultTableModel) this.getModel()).addRow(emptyRow);
					}
			}
		});
		((DefaultTableModel)tm).addRow(columnNames);

		return tm;
	}

}
