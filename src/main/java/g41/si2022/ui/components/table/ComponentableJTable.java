package g41.si2022.ui.components.table;

import java.awt.Component;
import java.util.Map;

import javax.swing.table.TableCellEditor;

/**
 * ComponentableJTable.
 * <p>
 * This class is a {@link javax.swing.JTable} that may have some of its row be other Components.<br>
 * The components accepted by this {@link JTable} must implement the {@link javax.swing.table.TableCellEditor} interface. 
 * Check <a href=https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#usingothereditors>Using other Editors</a> for more info.
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

 * @author Alex // UO281827
 *
 * @see javax.swing.JTable
 */
public class ComponentableJTable extends javax.swing.JTable {

	private static final long serialVersionUID = 1L;
	
	private Map<Integer, ? extends TableCellEditor> componentsMap;

	/**
	 * Creates a new ComponentableJTable.
	 * This table may have components on some columns. These are set using the map.
	 * If a given column is not featured in the map, this column will have the default component.
	 * 
	 * @param components {@link Map} that relates each column to a component.
	 */
	public ComponentableJTable (java.util.Map<Integer, ? extends javax.swing.table.TableCellEditor> components) {
		super();
		this.componentsMap = components;
		if (components != null)
			components.forEach((col, comp) -> this.getColumnModel().getColumn(col).setCellEditor(comp));
	}
	
	public Map<Integer, ? extends TableCellEditor> getComponentsMap () {
		return this.componentsMap;
	}
	
}
