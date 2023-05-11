package g41.si2022.ui.components.table.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;

public class JComboBoxEditor<T> extends AbstractCellEditor implements TableCellEditor, ActionListener {

	private static final long serialVersionUID = 1L;
	private JComboBox<T> theCb;

	public JComboBoxEditor() {
		this(new java.util.ArrayList<>());
	}

	public JComboBoxEditor(java.util.List<T> data) {
		this.theCb = new JComboBox<>();
		this.setData(data);
	}

	public void setData(java.util.List<T> data) {
		this.theCb.removeAllItems();
		data.forEach(item -> this.theCb.addItem(item));
	}

	@Override
	public Object getCellEditorValue() {
		return this.theCb.getSelectedItem();
	}

	@Override
	public void actionPerformed(ActionEvent e) { }

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value != null) this.theCb.setSelectedItem(value);
		return this.theCb;
	}

	public JComboBox<T> getComboBox() {
		return this.theCb;
	}

}
