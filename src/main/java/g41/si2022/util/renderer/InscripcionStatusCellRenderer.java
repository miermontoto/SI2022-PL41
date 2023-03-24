package g41.si2022.util.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import g41.si2022.util.enums.state.InscripcionState;

/**
 * Custom cell renderer for the {@link InscripcionState} enum.
 * It renders the cell with a different background color depending on the state of the inscription.
 * @author Juan Mier
 */
public class InscripcionStatusCellRenderer extends DefaultTableCellRenderer {

    private int statusColIndex;
    private static final int COLOR_TRANSPARENCY = 150;

    /**
     * Main constructor for the InscripcionStatusCellRenderer class.
     * @param statusColIndex The index of the column that contains the InscripcionState in the table model.
     * @implNote The col index in the table model is not necessarily the same as the col index in the table.
     */
    public InscripcionStatusCellRenderer(int statusColIndex) {
        super();
        this.statusColIndex = statusColIndex;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {

        JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

        TableModel tableModel = table.getModel();
        InscripcionState status = (InscripcionState) tableModel.getValueAt(row, statusColIndex);

        switch(status) {
            case PAGADA:
                l.setBackground(new Color(0, 255, 0, COLOR_TRANSPARENCY));
                break;
            case CANCELADA:
                l.setBackground(new Color(255, 0, 0, COLOR_TRANSPARENCY));
                break;
            case EXCESO:
                l.setBackground(new Color(255, 255, 0, COLOR_TRANSPARENCY));
                break;
            case RETRASADA:
                l.setBackground(new Color(255, 165, 0, COLOR_TRANSPARENCY));
                break;
            case RETRASADA_EXCESO:
                l.setBackground(new Color(255, 100, 0, COLOR_TRANSPARENCY));
                break;
            default:
                l.setBackground(new Color(0, 0, 0, 0));
                break;
        }

        return l;
    }
}
