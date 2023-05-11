package g41.si2022.ui.components;

import java.awt.Component;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import g41.si2022.ui.components.hint.HintingJTextField;

public class HintingCellEditor extends AbstractCellEditor implements TableCellEditor {

	private static final long serialVersionUID = 1L;
	private HintingJTextField source;

	public HintingCellEditor (String hint) {
		this.source = new HintingJTextField(hint);
	}

	@Override
	public Object getCellEditorValue() {
		return this.source.getText().isEmpty() ? this.source.getHint() : this.source.getText();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return this.source;
	}

}
