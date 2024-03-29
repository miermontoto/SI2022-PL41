package g41.si2022.ui.components.table;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

/**
 * RowAppendableComponentableJTable.
 * <p>
 * This class is a {@link javax.swing.JTable} that may have some of its row be other Components.<br>
 * The components accepted by this {@link JTable} must implement the {@link javax.swing.table.TableCellEditor} interface. 
 * Check <a href=https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#usingothereditors>Using other Editors</a> for more info.
 * This {@link javax.swing.JTable} will have only one row by default.
 * A row has a default value that corresponds to that of the column header.<br>
 * When a row has been filled with data different from that of the column header, a new row will appear below to continue filling in data.
 * </p>
 * <p>
 * In order to make a component available to this class, it must implement the {@link javax.swing.table.TableCellEditor} interface.<br>
 * Check <a href=https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#usingothereditors>this documentation</a> and 
 * <a href=http://www.java2s.com/Code/Java/Swing-JFC/CreatingaCustomTableCellEditorinaJTableComponent.htm>this example</a> for more details
 * </p>
 * <p>
 * At the time of writing this documentation, the class does not keep track of the components it contains. This means you are not able to retrieve / set data
 * by using this class.<br>
 * Therefore, you are most likely to be interested in keeping a named instance (or an instance held in a data structure) of each component this table contains in order to access them.
 * </p>
 * 
 * @author Alex // UO281827
 * 
 * @see JTable
 */
public class RowAppendableComponentableJTable extends RowAppendableJTable {

	private static final long serialVersionUID = 1L;
	
	private Map<Integer, TableCellEditor> componentsMap;

	/**
	 * Creates a new RowAppendableComponentableJTable.
	 * If a column has no {@code columnMatcher}, any data will be allowed.
	 * If a column has no {@code component}, the default component will be used.
	 * If a column has both a {@code columnMatcher} and a {@code component}, the component takes priority and the Matcher is ignored.
	 * If a column is tagged as optional, new rows will be added even if the column has not been filled.
	 * 
	 * @param columnNames Names of the columns
	 * @param columnMatchers {@link Pattern} that will be matched with each column.
	 * @param mandatory Array that tags as {@code true} the columns that are mandatory and {@code false} those that are not.
	 * @param treeMap {@link TableCellEditor} that will be used on each column.
	 */
	public RowAppendableComponentableJTable(
			String[] columnNames, 
			Map<Integer, Pattern> columnMatchers,
			boolean[] mandatory,
			TreeMap<Integer, javax.swing.table.TableCellEditor> treeMap) {
		super(columnNames, 
				columnMatchers.entrySet().stream()
				.filter((entry) -> !treeMap.containsKey(entry.getKey()))
				.collect(Collectors.toMap((entry) -> entry.getKey(), (entry) -> entry.getValue())),
				mandatory);
		this.componentsMap = treeMap;
		if (treeMap != null) 
			treeMap.forEach( (col, comp) -> this.getColumnModel().getColumn(col).setCellEditor(comp));
	}
	
	/**
	 * Creates a new RowAppendableComponentableJTable.
	 * If a column has no {@code component}, the default component will be used.
	 * If a column has both a {@code columnMatcher} and a {@code component}, the component takes priority and the Matcher is ignored.
	 * 
	 * @param columnNames Names of the columns
	 * @param components {@link TableCellEditor} that will be used on each column.
	 */
	public RowAppendableComponentableJTable(
			String[] columnNames, 
			Map<Integer, javax.swing.table.TableCellEditor> components) {
		super(columnNames);
		if (components != null) 
			components.forEach( (col, comp) -> this.getColumnModel().getColumn(col).setCellEditor(comp));
	}
	
	@Override
	public boolean isCellEditable (int row, int column) {
		return super.isCellEditable(row, column) || this.getComponentsMap().containsKey(column);
	}
	
	public Map<Integer, TableCellEditor> getComponentsMap () {
		return this.componentsMap;
	}

}
