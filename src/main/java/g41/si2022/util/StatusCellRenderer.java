package g41.si2022.util;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import g41.si2022.util.state.InscripcionState;

/**
 * Custom cell renderer for the {@link InscripcionState} enum.
 * It renders the cell with a different background color depending on the state of the inscription.
 * @author Juan Mier
 */
public class StatusCellRenderer extends DefaultTableCellRenderer {

    private int statusColIndex;
    private static final int COLOR_TRANSPARENCY = 150;
    private static final int LIGHT_COLOR_TRANSPARENCY = 75;

    /**
     * Main constructor for the StatusCellRenderer class.
     * The state can be any string that contains common names for the states.
     * @param statusColIndex The index of the column that contains the State in the table model.
     * @implNote The col index in the table model is not necessarily the same as the col index in the table.
     */
    public StatusCellRenderer(int statusColIndex) {
        super();
        this.statusColIndex = statusColIndex;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        TableModel tableModel = table.getModel();
        String status = tableModel.getValueAt(table.convertRowIndexToModel(row), statusColIndex).toString();

        if(status.contains("PAGAD")) l.setBackground(new Color(0, 255, 0, COLOR_TRANSPARENCY));
        else if(status.contains("CANCELAD")) l.setBackground(new Color(255, 0, 0, COLOR_TRANSPARENCY));
        else if(status.contains("_EXC")) l.setBackground(new Color(255, 100, 0, COLOR_TRANSPARENCY));
        else if(status.contains("EXCES")) l.setBackground(new Color(255, 255, 0, COLOR_TRANSPARENCY));
        else if(status.contains("RETRASAD")) l.setBackground(new Color(255, 165, 0, COLOR_TRANSPARENCY));
        else if(status.contains("ESPERA")) l.setBackground(new Color(100, 255, 0, LIGHT_COLOR_TRANSPARENCY));
        else l.setBackground(new Color(0, 0, 0, 0));

        return l;
    }
}
