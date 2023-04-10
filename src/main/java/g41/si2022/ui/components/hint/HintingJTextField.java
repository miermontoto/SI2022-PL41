package g41.si2022.ui.components.hint;

public class HintingJTextField extends Hint {

	private static final long serialVersionUID = 1L;
	
	public HintingJTextField (String hint) {
		super(hint);
	}

	@Override
	public String getUIClassID() {
		return "TextFieldUI";
	}
	
}
	
