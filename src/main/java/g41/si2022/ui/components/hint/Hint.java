package g41.si2022.ui.components.hint;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.text.JTextComponent;

/**
 * Represents a text component that has a hint
 * The hint is hidden when the item is focused 
 * 
 * @author Alex // UO281827
 *
 */
public abstract class Hint extends JTextComponent implements FocusListener {

	private static final long serialVersionUID = 1L;
	private String hint;
	private boolean showingHint;
	
	public Hint (String hint) {
		super();
		this.hint = hint;
		super.setText(hint);
		this.showingHint = true;
		super.addFocusListener(this);
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText("");
			showingHint = false;
		}
		this.repaint();
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (this.getText().isEmpty()) {
			super.setText(hint);
			showingHint = true;
		}
		this.repaint();
	}
	
	/**
	 * Returns the text of this component.
	 * 
	 * @return Text
	 */
	public String getText() {
		return this.showingHint ? "" : super.getText();
	}
	
	/**
	 * Changes the hint
	 * 
	 * @param hint New hint to be displayed
	 */
	public void setHint(String hint) {
		this.hint = hint;
		this.focusLost(null);
	}
	
	/**
	 * getHint.
	 * Returns the hint of this JTextComponent
	 * 
	 * @return Hint of this JTextComponent 
	 */
	public String getHint () {
		return this.hint;
	}
	
	/**
	 * getUIClassID.
	 * Every UI component has a String that is associated to the actual image of the component.<br>
	 * This string should be returned here so the component shows.
	 */
	public abstract String getUIClassID ();
}
